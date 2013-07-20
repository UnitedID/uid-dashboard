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

import org.bson.types.ObjectId
import com.mongodb.MongoException

class AccountController {

    def mailService

    def beforeInterceptor = [action:this.&clearSessionUid, except: 'lostToken']

    def clearSessionUid() {
        if (session.uid) {
            session.uid = null
            session.oid = null
        }
    }

    def index = {
        // Place holder until welcome page is introduced
    }

    def forgotPassword = {
        if (params?.mail) {
            def user = User.findByMail(params.mail)
            if (!user) {
                render(view: "forgotPassword", model: [error: "error"])
                return
            }
            def key = UUID.randomUUID().toString()
            if (user.states.key.contains("resetPassword")) {
                // We need a copy of the list or we run into ConcurrentModificationException
                def states = []
                states += user.states
                states.each {
                    if (it.key.equals("resetPassword")) {
                        user.removeFromStates((State) it)
                    }
                }
            }
            user.addToStates(new State(key: "resetPassword", authKey: key, date: new Date() + 1))

            if (!user.hasErrors() && user.save(flush: true)) {
                generateMail(user, key)
                render(view: "resetPasswordSent")
                return
            }
        }

        render(view: "forgotPassword")
    }

    def resetPassword = {
        if (request.method == "POST") {
            if (params?.userId && params?.key && params?.password && params?.password2) {
                def id = new ObjectId(params.userId.toString())
                def user = User.collection.findOne([_id: id, states: [ $elemMatch : [key: "resetPassword", authKey: params.key, date: [$gte: new Date()]]]  ])
                if (user) {
                    if (params.password != params.password2) {
                        flash.error = "Passwords does not match"
                        render(view: "resetPassword", model: [user: user])
                        return
                    }
                    def aead = PasswordUtil.getAEADFromPassword(params.password)
                    user.password = aead.aead
                    user.nonce = aead.nonce
                    user.salt = aead.salt
                    if (User.collection.update([_id: id], [$set : [password: aead.aead, nonce: aead.nonce, salt: aead.salt], $pull: [states : [key: "resetPassword"]]])) {
                        def encryptMe = "Username: ${user.username}\nPassword: ${params.password}\n"
                        PasswordUtil.storeEscrow(encryptMe, "${user._id}.asc")
                        render(view: "resetPasswordSuccess")
                        return
                    } else {
                        throw new MongoException("An error occurred while writing to the database")
                    }
                }
                flash.error = "Expired or invalid password reset request"
                redirect(action: "forgotPassword")
                return
            }
            flash.error = "Expired or invalid password reset request"
            redirect(action: "forgotPassword")
        } else {
            if (params?.id && params?.key) {
                def user = User.collection.findOne([_id: new ObjectId(params.id.toString()), states: [ $elemMatch : [key: "resetPassword", authKey: params.key, date: [$gte: new Date()]]]])
                if (user) {
                    render(view: "resetPassword", model: [userId: user._id, displayName: DisplayName.getName(user), key: params.key])
                    return
                }
                flash.error = "Expired or invalid password reset request"
            }
            redirect(action: "forgotPassword")
        }
    }

    def lostToken = {

    }

    def generateMail(user, key) {
        def subject = "United ID - Password Reset Request"
        def passwordResetUrl = request.siteUrl + g.createLink(controllerName: "account", action: "resetPassword", id: user.id, params: [key: key])

        def displayName = DisplayName.getName(user)

        mailService.send(
                user.mail,
                mailService.defaultFrom(),
                mailService.defaultFromName(),
                subject,
                g.render(template: "mailPasswordReset", model: [url: passwordResetUrl, displayName: displayName]).toString()
        )
    }
}
