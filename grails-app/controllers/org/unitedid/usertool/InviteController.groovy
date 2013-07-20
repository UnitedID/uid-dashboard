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

class InviteController {

    static allowedMethods = [index: "GET", sendInvite: "POST", cancel: "POST", uniqueInvitation: "POST"]

    def mailService

    def index = {
        def user = User.findByUsername(session.uid)
        def friends = Invite.findAllByInvitingUser(session.oid.toString())
        def inviteInstance = new Invite()

        if (user?.givenName) {
            inviteInstance.properties.fromName = user.givenName
            if (user?.sn) {
                inviteInstance.properties.fromName += " ${user.sn}"
            }
        } else if (user?.sn) {
            inviteInstance.properties.fromName = user.sn
        }

        [inviteInstance: inviteInstance, friends : friends]
    }

    def sendInvite = {
        def mail = params.mail.toString().toLowerCase()

        if (!Invite.findByMail(mail)) {
            def inviteInstance = new Invite(invitingUser: session.oid.toString())
            inviteInstance.properties['fromName', 'mail'] = params
            if (!inviteInstance.hasErrors() && inviteInstance.save(flush: true)) {
                generateMail(inviteInstance)
                redirect(action: "index")
            } else {
                def friends = Invite.findAllByInvitingUser(session.oid)
                render(view: "index", model : [inviteInstance: inviteInstance, friends: friends])
            }
        } else {
            flash.message = "An invite has already been sent to ${mail} by you or someone else"
            redirect(action: "index")
        }
    }

    def cancel = {
        redirect(controller: "dashboard")
    }

    def uniqueInvitation = {
        String mail = params.mail
        String result = false
        if (!Invite.findByMail(mail.toLowerCase())) {
            result = true
        }
        response.setContentType("text/json;charset=UTF-8")
        render result
    }

    def generateMail(inviteInstance) {
        def inviteUrl = request.siteUrl + g.createLink(controller: "signup", action: "invite", id: inviteInstance.inviteKey, params:[ mail: inviteInstance.mail])
        mailService.send(
                inviteInstance.mail,
                mailService.defaultFrom(),
                mailService.defaultFromName(),
                "You have been invited to try United ID's identity service",
                g.render(template: "mailInvite", model: [inviteInstance: inviteInstance, inviteUrl: inviteUrl]).toString()
        )
    }
}
