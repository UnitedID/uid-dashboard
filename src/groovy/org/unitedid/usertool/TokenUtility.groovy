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

import java.security.MessageDigest
import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Hex
import org.unitedid.yhsm.YubiHSM
import org.unitedid.yhsm.ws.client.YubiHSMValidationClient

class TokenUtility {

    static def config = Holders.config

    static def verifyToken(Token token, String otp) {
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
        } else if (token.type == "oathhotp" || token.type == "googlehotp") {
            return verifyOathHotp(token, otp)
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

    static def verifyOathHotp(Token token, String otp) {
        def keyHandle = (int) config.yhsm.oathValidationKeyHandle
        def lookAhead = (int) config.yhsm.oathLookAhead
        YubiHSMValidationClient hsm = new YubiHSMValidationClient(config.yhsm.ws.validationURL)
        def counter = hsm.validateOathHOTP(token.nonce, keyHandle, token.aead, token.counter, otp, lookAhead)

        // OATH-HOTP validated successfully
        if (counter != 0) {
            return [true, counter]
        }

        return [false, "Validation failed for the supplied one-time password, please try again"]
    }

    static def getOathHOTPToken(params) {
        def seed = params.seed
        def identifier = params.identifier
        def otp = params.otp
        def tokenType = params.tokenType

        if (seed.length() != 40) {
            return null
        }

        def aeadKeyHandle = (int) config.yhsm.oathEncryptKeyHandle
        def validationKeyHandle = (int) config.yhsm.oathValidationKeyHandle
        def nonce = PasswordUtil.getRandomNonce()

        YubiHSM hsm = new YubiHSM(config.yhsm.device, (float) 0.5)
        YubiHSMValidationClient hsmWsClient = new YubiHSMValidationClient(config.yhsm.ws.validationURL)
        def aead = hsm.generateOathHotpAEAD(nonce, aeadKeyHandle, seed)
        def counter = hsmWsClient.validateOathHOTP(nonce, validationKeyHandle, aead, 0, otp, 40)

        if (counter > 0) {
            return new Token(type: tokenType, identifier: identifier, counter: counter, aead: aead, nonce: nonce)
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
