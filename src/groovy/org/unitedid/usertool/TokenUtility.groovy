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
import org.unitedid.auth.client.AuthClient
import org.unitedid.auth.client.OATHFactor
import org.unitedid.auth.client.RevokeFactor

import java.security.MessageDigest
import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Hex
import org.unitedid.yhsm.YubiHSM

class TokenUtility {

    static def config = Holders.config
    static def baseURL = (String) config.auth.backend.baseURL

    static def removeToken(String userId, Token token) {
        def factor = new RevokeFactor(token.type, token.credentialId)
        def authClient = new AuthClient(baseURL)

        if (authClient.revokeCredential(userId, factor)) {
            return true
        }

        return false
    }

    static def verifyToken(String userId, Token token, String otp) {
        if (token.type == "yubikey") {
            def yubiKey = verifyYubiKey(otp)

            if (yubiKey.status == "OK") {
                if (token.identifier == yubiKey.publicId) {
                    return [true, ""]
                } else {
                    return [false, "The YubiKey used does not match the token currently being activated"]
                }
            }
            return [false, yubiKey.status]
        } else if (['oathhotp', 'oathtotp'].contains(token.type)) {
            return verifyOathOtp(userId, token, otp)
        }

        return [false, "Token type '${token.type}' is not supported"]
    }

    static def verifyYubiKey(otp) {
        def yubicoClient = YubicoClient.getClient()
        yubicoClient.setClientId(4711)
        def yubicoResponse = yubicoClient.verify(otp)
        if (yubicoResponse) {
            switch(yubicoResponse.getStatus().toString()) {
                case "OK":
                    def publicId = YubicoClient.getPublicId(otp)
                    return [publicId: publicId, status: "OK"]
                    break
                case "BAD_OTP":
                    return [publicId: null, status: "Invalid OTP format. Please try again."]
                    break
                case "REPLAYED_OTP":
                    def publicId = YubicoClient.getPublicId(otp)
                    return [publicId: publicId, status: "That OTP has already been used. Please try again by using a new OTP from your Yubikey."]
                    break
            }
        } else {
            return [publicId: null, status: "Failed to validate Yubikey OTP."]
        }
    }

    /***
     * Verify OATH OTP against authentication backend.
     *
     * @param token the token being verifying the OTP against
     * @param otp the user one time password (6-8 digits)
     */
    static def verifyOathOtp(String userId, Token token, String otp) {
        def factor = new OATHFactor(token.type, token.nonce, otp, token.credentialId)
        AuthClient authClient = new AuthClient(baseURL)

        if (authClient.authenticate(userId, factor)) {
            return [true, ""]
        }

        return [false, "Validation failed for the supplied one-time password, please try again"]
    }

    static def getOATHToken(userId, params) {
        def seed = params.seed
        def identifier = params.identifier
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
        def authClient = new AuthClient(baseURL)
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
