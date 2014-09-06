/*
 * Copyright (c) 2011 - 2013 United ID.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.unitedid.usertool
import com.yubico.client.v2.YubicoClient
import grails.util.Holders
import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Hex
import org.codehaus.groovy.grails.web.util.WebUtils
import org.unitedid.auth.client.AuthClient
import org.unitedid.auth.client.factors.OATHFactor
import org.unitedid.auth.client.factors.RevokeFactor
import org.unitedid.auth.client.factors.YubiKeyFactor
import org.unitedid.yhsm.YubiHSM

import java.security.MessageDigest

class TokenUtility {

    static def config = Holders.config
    static def baseURL = (String) config.auth.backend.baseURL
    static def authUsername = (String) config.auth.backend.username
    static def authPassword = (String) config.auth.backend.password

    static def removeToken(String userId, Token token) {
        def factor = new RevokeFactor(token.type, token.credentialId)
        def authClient = new AuthClient(baseURL, authUsername, authPassword)

        if (authClient.revokeCredential(userId, factor)) {
            return true
        }

        return false
    }

    static def verifyToken(String userId, Token token, String otp) {
        if (token.type == "yubikey") {
            return verifyYubiKey(userId, token, otp)
        } else if (['oathhotp', 'oathtotp'].contains(token.type)) {
            return verifyOathOtp(userId, token, otp)
        }

        return [false, "Token type '${token.type}' is not supported"]
    }

    static def verifyYubiKey(String userId, Token token, String otp) {
        def factor = new YubiKeyFactor(token.type, token.nonce, otp, token.credentialId)
        AuthClient authClient = new AuthClient(baseURL, authUsername, authPassword)

        if (authClient.authenticate(userId, factor)) {
            return [true, ""]
        }

        return [false, "Failed to validate Yubikey OTP."]
    }

    /***
     * Verify OATH OTP against authentication backend.
     *
     * @param token the token being verifying the OTP against
     * @param otp the user one time password (6-8 digits)
     */
    static def verifyOathOtp(String userId, Token token, String otp) {
        def factor = new OATHFactor(token.type, token.nonce, otp, token.credentialId)
        AuthClient authClient = new AuthClient(baseURL, authUsername, authPassword)

        if (authClient.authenticate(userId, factor)) {
            return [true, ""]
        }

        return [false, "Validation failed for the supplied one-time password, please try again"]
    }

    static def getYubiKeyPublicId(otp) {
        return YubicoClient.getPublicId(otp)
    }

    static def getYubiKeyToken(userId, params) {
        def otp = params.otp
        def identifier = getYubiKeyPublicId(otp)

        YubiHSM hsm = new YubiHSM(config.yhsm.device, (float) 0.5)
        def nonce = hsm.getRandom(12).encodeHex().toString()

        Token token = new Token(type: 'yubikey', nonce: nonce, guiType: 'yubikey', identifier: identifier)

        def factor = new YubiKeyFactor(token.type, nonce, otp, token.credentialId)
        def authClient = new AuthClient(baseURL, authUsername, authPassword)
        if (authClient.addCredential(userId, factor)) {
            return token
        }
        return null
    }

    static def getOATHToken(userId, params) {
        def seed = params.seed
        def session = WebUtils.retrieveGrailsWebRequest().getSession()
        def identifier = sprintf("%s@unitedid.org", session.uid)
        def otp = params.otp
        def googleType = ['googlehotp':'oathhotp', 'googletotp':'oathtotp']
        def tokenType = googleType.containsKey(params.tokenType) ? googleType.get(params.tokenType) : params.tokenType

        if (seed.length() != 40) {
            return null
        }

        def aeadKeyHandle = (int) config.yhsm.oathEncryptKeyHandle

        YubiHSM hsm = new YubiHSM(config.yhsm.device, (float) 0.5)
        def nonce = hsm.getRandom(6).encodeHex().toString()
        def aead = hsm.generateOathAEAD(nonce, aeadKeyHandle, seed)

        Token token = new Token(type: tokenType, nonce: nonce, guiType: "oathtoken", identifier: identifier)

        if (googleType.containsKey(params.tokenType)) {
            token.guiType = "google"
        }

        def factor = new OATHFactor(tokenType, aeadKeyHandle, aead, nonce, otp, token.credentialId)
        def authClient = new AuthClient(baseURL, authUsername, authPassword)
        if (authClient.addCredential(userId, factor)) {
            return token
        }

        return null
    }

    static def generateGoogleAuthKey() {
        def uuid = UUID.randomUUID()
        MessageDigest sha1 = MessageDigest.getInstance("SHA1")
        sha1.update(uuid.toString().getBytes())

        def tmp = uuid.toString() + Hex.encodeHex(sha1.digest())
        sha1.update(tmp.getBytes())
        def sha1digest = sha1.digest()
        String seed = Hex.encodeHex(sha1digest).toString()
        String googleKey = new String(new Base32().encode(sha1digest))

        return [seed, googleKey]
    }
}
