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
import org.unitedid.auth.client.factors.PasswordFactor
import org.unitedid.auth.client.factors.RevokeFactor

class PasswordUtil {

    static def config = Holders.config
    static def baseURL = (String) config.auth.backend.baseURL
    static def authUsername = (String) config.auth.backend.username
    static def authPassword = (String) config.auth.backend.password

    static def generateSecret(String password, String userId) {
        PasswordFactor passwordFactor = new PasswordFactor(password, new ObjectId().toString())
        AuthClient authClient = new AuthClient(baseURL, authUsername, authPassword)
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
        AuthClient authClient = new AuthClient(baseURL, authUsername, authPassword)
        if (authClient.authenticate(user.id.toString(), passwordFactor)) {
            result = true
        }

        return result
    }

    static def revokeCredential(Credential credential, String userId) {
        AuthClient authClient = new AuthClient(baseURL, authUsername, authPassword)
        RevokeFactor factor = new RevokeFactor("password", credential.credentialId)
        if (authClient.revokeCredential(userId, factor)) {
            return true
        }
        return false
    }
}
