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
import com.megatome.grails.RecaptchaService
import com.mongodb.MongoException
import grails.util.Holders

class LoginController {
    static allowedMethods = [authenticate: "POST", validateTokenAuth: "POST"]

    RecaptchaService recaptchaService

    def beforeInterceptor = [action:this.&verifyNoSession]
    def config = Holders.config

    def verifyNoSession = {
        if (session?.uid && !session.tokenAuthRequired)
            redirect(controller: 'dashboard')
    }

    def index = { }


    def authenticate = {
        def username = params?.username?.toString()?.toLowerCase()
        if (!username || !params?.password) {
            session.failedAttempts = session?.failedAttempts ? session.failedAttempts + 1 : 1
            redirect(action: 'index')
            return
        }

        // We need to test twice in case mongodb have lost the connection due to idling too long
        def result
        try {
            result = User.withCriteria {
                or {
                    eq('username', username)
                    eq('mail', username)
                    'in'('mailAlias', username)
                }
                and {
                    eq('active', true)
                }
            }
        } catch (Exception e) {
            Thread.sleep(500)
            try {
                result = User.withCriteria {
                    or {
                        eq('username', username)
                        eq('mail', username)
                        'in'('mailAlias', username)
                    }
                    and {
                        eq('active', true)
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("Failed to re-connect to the database", ex)
            }
        }

        def user = (result.size() == 1) ? result.first() : null

        def recaptchaOK = true
        if (session?.failedAttempts > 2 && !recaptchaService.verifyAnswer(session, request.getRemoteAddr(), params)) {
            recaptchaOK = false
        }

        if (!user || !recaptchaOK || !PasswordUtil.validatePassword(params['password'].toString(), user)) {
            flash.error = "Invalid username, email or password"
            session.failedAttempts = session?.failedAttempts ? session.failedAttempts + 1 : 1
            redirect(action: 'index')
            return
        }

        session.failedAttempts = null
        session.uid = user.username
        session.oid = user.id
        session.role = "user" //TODO: implement roles

        if (checkActiveToken(user?.tokens)) {
            session.tokenAuthRequired = true
            redirect(action: 'tokenAuth')
            return
        }

        redirect(uri: session.forwardURI)
    }

    def signup = {
        redirect(controller: 'signup')
    }

    def tokenAuth = {
        if (!session?.tokenAuthRequired) {
            redirect(action: 'index')
        }
    }

    def validateTokenAuth = {
        if (!session?.tokenAuthRequired) {
            session.invalidate()
            redirect(action: 'index')
            return
        }

        if (!params?.otp) {
            flash.message = "Please enter a one-time password using one of your tokens"
            redirect(action: 'tokenAuth')
            return
        }

        def user = User.findByUsername(session.uid)
        if (!user) {
            session.invalidate()
            redirect(action: 'index')
            return
        }

        if (checkActiveToken(user?.tokens)) {
            if (validateOTP(user, params.otp)) {
                session.tokenAuthRequired = null
                redirect(uri: session.forwardURI)
            } else {
                session.invalidate()
                flash.error = "Invalid one-time password"
                redirect(action: 'index')
            }
        } else {
            flash.message = "No security tokens found" // This really shouldn't happen
            session.invalidate()
            redirect(action: 'index')
        }
    }

    private def validateOTP(User user, String otp) {
        def userId = user.id.toString()
        def status = false

        for (Token token in user.tokens) {
            def data = ""
            if (!token.active)
                continue

            // To reduce the round trips to the auth backend we only verify tokens we know would match the otp length.
            if (otp =~ /[0-9]{6,8}/ && ['oathhotp', 'oathtotp'].contains(token.type)) {
                (status, data) = TokenUtility.verifyToken(userId, token, otp)
            } else if (otp.length() >= 32 && token.type == 'yubikey') {
                (status, data) = TokenUtility.verifyToken(userId, token, otp)
            }

            if (status) {
                return status
            }
        }

        return status
    }

    private def updateOathCounter(Token token, int counter) {
        if (!User.collection.update([username: session.uid, 'tokens.tokId': token.tokId], [$set: ['tokens.$.counter': counter]])) {
            throw new MongoException("Failed to update the OATH counter in the database")
        }
    }

    /**
     * Check if at least one token is active before requiring OTP login
     *
     * @param tokens the user token list
     * @return true if at least one token is active, otherwise false
     */
    private def checkActiveToken(List<Token> tokens) {
        if (tokens?.size() > 0) {
            for (Token token in tokens) {
                if (token.active) {
                    return true
                }
            }
        }
        return false
    }
}
