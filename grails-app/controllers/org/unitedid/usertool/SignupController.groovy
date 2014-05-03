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
import org.bson.types.ObjectId

class SignupController {

    RecaptchaService recaptchaService

    static allowedMethods = [index : 'GET',
            createNewUser : 'POST',
            confirmation : ['GET', 'POST']]

    def mailService

    def index = {
        def userInstance = new User()
        userInstance.properties = params
        return [userInstance: userInstance]
    }

    def register = {
        def userInstance = new User(givenName:"", sn:"", website:"")

        userInstance.properties['username', 'mail', 'password', 'acceptTerms'] = params

        // Force validation or later errors will be overwritten by userInstance.save()
        userInstance.validate()

        // Custom error handling, due to issues with @Transient not working in the mongodb plugin.
        if (params['password'] != params['password2']) {
            userInstance.errors.rejectValue("password", "uid.usertool.passwordMissmatch")
        }

        // Custom error handling for uniqueness due to validation problems
        def mail = params?.mail?.toString()?.toLowerCase()
        if (params?.username) {
            if (User.findByUsername(params.username)) {
                userInstance.errors.rejectValue("username", "uid.usertool.validation.username.not.unique")
            }

            if (mail && User.findAll {
                and {
                    or {
                        inList('mailAlias', mail)
                    }
                    eq('mail', mail)
                }
            }) {
                userInstance.errors.rejectValue("mail", "uid.usertool.validation.email.not.unique")
            }
        }

        // Add mailalias
        userInstance.mailAlias.add(mail)

        // Re-captcha
        def recaptchaOK = true
        if (!recaptchaService.verifyAnswer(session, request.getRemoteAddr(), params)) {
            recaptchaOK = false
        }

        // Generate and add credential to auth backend
        userInstance.credential = PasswordUtil.generateSecret(params.password, userInstance.id.toString())

        if (recaptchaOK && !userInstance.hasErrors() && userInstance.save(flush: true)) {
            recaptchaService.cleanUp(session)
            session.mail = userInstance.mail

            // If signup was an invite we need to update the invite data
            if (session?.inviteKey) {
                def invite = Invite.findByInviteKey(session.inviteKey)
                if (invite) {
                    Invite.collection.update([inviteKey: session.inviteKey], [ $set : [ accepted : true]])
                }
            }
            generateActivationMail(mailService.defaultFrom(), userInstance.mail)
            redirect(action: 'confirmation')
        } else {
            render(view: "index", model: [userInstance: userInstance])
        }
    }

    def confirmation = {
        if (!session?.mail) {
            redirect(action: "index")
        } else {
            def mail = session.mail
            session.invalidate()
            [mail: mail]
        }
    }


    def activateAccount = {
        def user = User.findByIdAndActivationKey(new ObjectId(params.id), params.key)

        if (user) {
            if (user.active) {
                def url = request.siteUrl + g.createLink(controller: "dashboard")
                render(view: "activationFailed", model: [message: "Your account has already been activated. Click <a href=\"" + url + "\">here</a> to login."])
                return
            }

            user.properties['active'] = true

            if (!user.hasErrors() && user.save(flush: true)) {
                [userInstance: user]
            } else {
                render(view: "activationFailed", model: [message: "An error have occured, please contact support@unitedid.org if the problem persists."])
            }
        } else {
            render(view: "activationFailed", model: [message: "Invalid user id or activation key."])
        }
    }

    def invite = {
        def invite = Invite.collection.findOne([inviteKey: params.id, mail: params.mail, accepted: false])

        if (invite) {
            session.inviteValid = true
            session.inviteKey = params.id
            redirect(controller: "signup")
        } else {
            render(view: "invite")
        }
    }

    def generateActivationMail(from, to) {
        def user = User.findByMail(to)
        def activationUrl = request.siteUrl + g.createLink(controller: "signup", action: "activateAccount", id: user.id, params: [key: user.activationKey])
        mailService.send(
                to,
                from,
                mailService.defaultFromName(),
                "Account activation request from United ID",
                g.render(template: "mailAccountActivation", model: [activationUrl: activationUrl]).toString())
    }
}
