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
import grails.util.Holders
import org.bson.types.ObjectId
import org.unitedid.auth.client.AuthClient
import org.unitedid.auth.client.PasswordFactor
import org.unitedid.auth.client.RevokeFactor

class PasswordUtil {

    static def config = Holders.config
    static def baseURL = (String) config.auth.backend.baseURL

    static def generateSecret(String password, String userId) {
        PasswordFactor passwordFactor = new PasswordFactor(password, new ObjectId().toString())
        AuthClient authClient = new AuthClient(baseURL)
        if (!authClient.addCredential(userId, passwordFactor)) {
            throw new Exception("Add credential failed")
        }
        return new Credential(credentialId: passwordFactor.credentialId, salt: passwordFactor.salt)
    }

    public static boolean validatePassword(String password, User user) {
        def result = false
        print "CredId: " + user.credential.credentialId + " Salt: " + user.credential.salt
        PasswordFactor passwordFactor = new PasswordFactor(password,
                user.credential.credentialId,
                user.credential.salt)
        AuthClient authClient = new AuthClient(baseURL)
        if (authClient.authenticate(user.id.toString(), passwordFactor)) {
            result = true
        }

        return result
    }

    static def revokeCredential(Credential credential, String userId) {
        AuthClient authClient = new AuthClient(baseURL)
        RevokeFactor factor = new RevokeFactor("password", credential.credentialId)
        if (authClient.revokeCredential(userId, factor)) {
            return true
        }
        return false
    }


/*    public static Map<String, String> getAEADFromPassword(String password) {
        def nonce = getRandomNonce()
        def salt = getRandomNonce()

        // Generate a PBKDF2 hash based on password
        def hash = getHashFromPassword(password, salt, false)

        // Use YubiHSM to get an AEAD from nonce and hash
        YubiHSM hsm = new YubiHSM((String) config.yhsm.device)
        def aead = hsm.generateAEAD(nonce, (int) config.yhsm.encryptKeyHandle, hash)
        aead['salt'] = salt

        return aead
    }

    public static String getRandomNonce() {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG")
        byte[] randomBytes = new byte[12]
        rand.nextBytes(randomBytes)
        return new String(Hex.encodeHex(new Base64().encode(randomBytes))).substring(0, 12)
    }

    public static String getHashFromPassword(String password, String salt, boolean convertToHex) {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes("UTF-8"), (int) config.pbkdf2.iterations, (int) config.pbkdf2.length)
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        SecretKey secretKey = keyFactory.generateSecret(keySpec)
        def hash = null
        if (convertToHex) {
            hash = new String(Hex.encodeHex(new Base64().encode(secretKey.getEncoded())))
        } else {
            hash = new String(secretKey.getEncoded())
        }

        return hash
    }

    public static boolean validatePassword(String password, String salt, String aead, String nonce) {
        def hashedPassword = getHashFromPassword(password, salt, false)
        def keyHandle = (int) config.yhsm.decryptKeyHandle

        YubiHSMValidationClient hsm = new YubiHSMValidationClient((String) config.yhsm.ws.validationURL)
        try {
              return hsm.validateAEAD(nonce, keyHandle, aead, hashedPassword)
        } catch (YubiHSMInputException e) {
            // Indicates wrong password was used since the expected size of the AEAD and password doesn't match
            // We return false instead of throwing an exception
            return false
        } catch (Exception e) {
            throw new Exception(e)
        }
    }
    */
}
