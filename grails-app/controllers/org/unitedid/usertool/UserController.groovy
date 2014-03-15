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
import com.mongodb.MongoException

import static org.unitedid.usertool.TokenUtility.getOathHOTPToken
import static org.unitedid.usertool.TokenUtility.getOATHToken

class UserController {
    static allowedMethods = [saveAccount: "POST", saveNewPassword: "POST", addToken: "POST"]

    static tokenTypes = [
            'yubikey':'YubiKey',
            'oathhotp':'OATH-HOTP (Event based)',
            'googlehotp': 'Google Authenticator (Counter based)',
            'googletotp': 'Google Authenticator (Time based)']

    def mailService

    def index = {
        redirect(controller: "dashboard", action: "index")
    }

    def changePassword = {
        render(view: "changePassword")
    }

    def changeEmail = {
        def user = User.findByUsername(session.uid)
        if (request.method == "POST") {
            if (!params?.mail || !(params.mail ==~ /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}/)) {
                flash.error = "Please enter a valid email address"
                return redirect(action: "changeEmail")
            }
            def mail = params.mail.toString().trim().toLowerCase()

            if (User.collection.findOne([$or: [[mail: mail], [states: [$elemMatch: [key:"mailAlias", value: mail]]]]])) {
                flash.error = "An email address with that name already exists"
                return redirect(action: "changeEmail")
            }

            State state = new State(key: "mailAlias", value: mail, authKey: UUID.randomUUID().toString())
            user.addToStates(state)
            if (!user.hasErrors() && user.save(flush: true)) {
                flash.message = "Secondary email address added, a confirmation request has been sent to ${mail}"
                generateAddEmailMail(mail, state)
                redirect(action: "changeEmail")
            } else {
                [user: user]
            }
        } else {
            [user: user]
        }
    }

    def activateMailAddress = {
        if (!params?.id) {
            flash.error = "Invalid email activation key"
            return redirect(action: "changeEmail")
        }

        def user = User.collection.findOne([username: session.uid, states: [$elemMatch: [key: "mailAlias", authKey: params.id]]])

        if (user) {
            def mailAlias = null
            for (def state in user.states) {
                if (state.authKey == params.id) {
                    mailAlias = state.value
                    break
                }
            }
            if (User.collection.update([username: session.uid], [$push : [mailAlias: mailAlias], $pull: [states : [authKey: params.id]]])) {
                flash.message = "Email '${mailAlias}' has been successfully activated"
                return redirect(action: "changeEmail")
            } else {
                throw new MongoException("Failed while writing data to the database")
            }
        }
        flash.error = "Invalid email activation key"
        redirect(action: "dashboard")
    }

    def removeMailAddress = {
        if (!params?.id) {
            return redirect(action: "changeEmail")
        }

        def username = session.uid  // Can't use session object inside withCriteria, confused with mongodb session
        def user = User.withCriteria {
            and {
                eq('username', username)
                inList('mailAlias', params.id)
            }
        }.get(0)

        if (user) {
            if (user.mail == params.id) {
                flash.message = "Primary email address cannot be removed"
                return redirect(action: "changeEmail")
            }
            user.mailAlias.remove(params.id)
            if (user.save(flush: true)) {
                flash.message = "Email address '${params.id}' has been successfully removed"
                return redirect(action: "changeEmail")
            } else {
                flash.error = "Failed to remove email address"
                return redirect(action: "changeEmail")
            }
        }
        flash.message = "No secondary email address of '${params.id}' was found"
        redirect(action: "changeEmail")
    }

    def removeInactiveMail = {
        if (!params?.id) {
            return redirect(action: "changeEmail")
        }

        def user = User.collection.findOne([username: session.uid, states : [ $elemMatch: [key: 'mailAlias', value: params.id]]])
        if (user) {
            if (User.collection.update([username: session.uid], [$pull: [states: [key: 'mailAlias', value: params.id]]])) {
                flash.message = "Email address '${params.id}' has been successfully removed"
                return redirect(action: "changeEmail")
            } else {
                flash.error = "Failed to remove email address"
                return redirect(action: "changeEmail")
            }
        }
        flash.message = "No email address of '${params.id}' was found"
        redirect(action: "changeMail")
    }

    def changePrimaryMail = {
        if(!params?.id) {
            flash.error = "Invalid change primary email request"
            return redirect(action: "changeEmail")
        }

        def user = User.collection.findOne([username: session.uid, states: [$elemMatch: [key: "mailPrimary", authKey: params.id]]])

        if (user) {
            def newMail = null
            def oldMail = user.mail
            for (def state in user.states) {
                if (state.authKey == params.id) {
                    newMail = state.value
                    break
                }
            }

            if (User.collection.update([username: session.uid], [$pull : [states : [authKey : params.id]], $set : [mail: newMail]])) {
                flash.message = "Email address '${newMail}' has been successfully promoted to your primary address"
                return redirect(action: "changeEmail")
            } else {
                throw new MongoException("Failed while writing data to the database")
            }
        }
        flash.error = "Invalid change primary email request"
        redirect(action: "changeEmail")
    }

    def promoteEmailPrimary = {
        if (!params?.id) {
            return redirect(action: "changeEmail")
        }

        def mail = params.id

        def username = session.uid  // Can't use session object inside withCriteria, confused with mongodb session
        def user = User.withCriteria {
            and {
                eq('username', username)
                inList('mailAlias', params.id)
            }
        }.get(0)

        if (user) {
            if (User.collection.findOne([username: session.uid, states : [$elemMatch: [key: 'mailPrimary', value: mail]]])){
                for (def state in user.states) {
                    if (state.key.equals("mailPrimary") && state.value.equals(mail)) {
                        generatePrimaryEmailChange(mail, state)
                        break
                    }
                }
                flash.message = "A confirmation request have been sent to '${mail.encodeAsHTML()}'. Please check your mail to complete the primary email change."
                return redirect(controller: "dashboard")
            } else {
                State state = new State(key: "mailPrimary", value: mail, authKey: UUID.randomUUID().toString())
                user.addToStates(state)
                if (!user.hasErrors() && user.save(flush: true)) {
                    flash.message = "A confirmation request have been sent to '${mail.encodeAsHTML()}'. Please check your mail to complete the primary email change."
                    generatePrimaryEmailChange(mail, state)
                    return redirect(controller: "dashboard")
                } else {
                    flash.error = "An error has occurred, please try again."
                    return redirect(action: "changeEmail")
                }
            }
        }
        flash.message = "No such email address could be found"
        redirect(action: "changeEmail")
    }

    def resendEmailActivation = {
        if (!params?.id) {
            flash.error = "Invalid email address"
            return redirect(action: "changeEmail")
        }

        def user = User.collection.findOne([username: session.uid, states: [$elemMatch: [key: 'mailAlias', value: params.id]]])

        if (user) {
            for (def state in user.states) {
                if (state.value == params.id) {
                    generateAddEmailMail(params.id, state)
                    flash.message = "An activation request has been sent to ${params.id}"
                    return redirect(action: "changeEmail")
                }
            }
        }

        flash.message = "No such email address could be found"
        redirect(action: "changeEmail")
    }

    def generatePrimaryEmailChange(to, state) {
        def activationUrl = request.siteUrl + g.createLink(controller: "user", action: "changePrimaryMail", id: state.authKey)
        mailService.send(
                to,
                mailService.defaultFrom(),
                mailService.defaultFromName(),
                "United ID - Primary email change request",
                g.render(template: "mailPrimaryActivation", model: [activationUrl: activationUrl]).toString())
    }


    def generateAddEmailMail(to, state) {
        def activationUrl = request.siteUrl + g.createLink(controller: "user", action: "activateMailAddress", id: state.authKey)
        mailService.send(
                to,
                mailService.defaultFrom(),
                mailService.defaultFromName(),
                "United ID - Secondary email activation request",
                g.render(template: "mailEmailActivation", model: [activationUrl: activationUrl]).toString())
    }

    def saveNewPassword = {
        def user = User.findByUsername(session.uid)

        if (user) {
            if (params['currentPassword'] == "") {
                flash.error = "Current password can't be blank"
                return render(view: "changePassword")
            }

            if (params['currentPassword'] != "" && !PasswordUtil.validatePassword(params['currentPassword'].toString(), user.salt, user.password, user.nonce)) {
                flash.error = "Invalid password!"
                render(view: "changePassword")
            } else if (params['password'] != params['password2']) {
                flash.message = "New passwords did not match!"
                render(view: "changePassword", model: [currentPassword: params['currentPassword']])
            } else {
                def aead = PasswordUtil.getAEADFromPassword(params['password'].toString())
                user.properties['password'] = aead.aead
                user.properties['nonce'] = aead.nonce
                user.properties['salt'] = aead.salt

                if (!user.hasErrors() && user.save(flush: true)) {
                    // Save in escrow
                    def encryptMe = "Username: ${user.username}\nPassword: ${params['password']}\n"
                    PasswordUtil.storeEscrow(encryptMe, "${user.id}.asc")
                    flash.message = "Password updated successfully"
                    redirect(controller: "dashboard", action: "index")
                } else {
                    render(view: "changePassword", model: [currentPassword: params['currentPassword']])
                }
            }
        }
    }

    def manageAccount = {
        def userInstance = User.findByUsername(session.uid)
        if (!userInstance) {
            flash.message = "Unknown error occured"
            redirect(controller: "dashboard")
        } else {
            [userInstance: userInstance]
        }
    }

    def saveAccount = {
        def userInstance = User.findByUsername(session.uid)
        if (userInstance) {
            userInstance.properties['givenName', 'sn', 'website', 'address.address1',
                    'address.address2', 'address.zip', 'address.city', 'address.country'] = params
            userInstance.validate()
            if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
                flash.message = "Account information updated successfully"
                redirect(controller: "dashboard")
            } else {
                render(view: "manageAccount", model: [userInstance: userInstance])
            }
        }
    }

    def manageTokens = {
        def userInstance = User.findByUsername(session.uid)
        if (!userInstance) {
            flash.message = "User ${session.uid} not found in database"
            redirect(controller: "dashboard")
        } else {
            [userInstance: userInstance, tokenTypes: tokenTypes]
        }
    }

    def addToken = {
        def tokenType = params['tokenType']
        def token = null
        def message = null

        if (tokenType == "yubikey") {
            def yubiKey = TokenUtility.verifyYubiKey(params.otp)
            if (yubiKey.status == "OK") {
                def existingToken = User.collection.findOne([username: session.uid, 'tokens.identifier': yubiKey.publicId])
                if (existingToken) {
                    flash.error = "A YubiKey with that id already exists."
                    return redirect(controller: "user", action: "manageTokens")
                }
                token = new Token(type:tokenType, identifier: yubiKey.publicId)
                message = "Yubikey with id '" + uid.yubikeyPublicId(token: token) + "' has been added to your account. To activate this token please check your mail for further instructions."
            } else {
                flash.error = yubiKey.status
                return redirect(controller: "user", action: "manageTokens")
            }
        } else if (tokenType == "oathhotp") {
            token = getOathHOTPToken(params)
            if (!token) {
                flash.error = "OATH-HOTP verification failed"
                return redirect(controller: "user", action: "manageTokens")
            }
            message = "An OATH-HOTP token has been added to your account. To activate this token please check your mail for further instructions."
        } else if (tokenType == "googlehotp" || tokenType == "googletotp") {
            token = getOATHToken(params)
            token.identifier = session.uid + "@unitedid.org"
            if (!token) {
                flash.error = "Google Authenticator verification failed"
                return redirect(controller: "user", action: "manageTokens")
            }
            message = "A Google Authenticator token has been added to your account. To activate this token please check your mail for further instructions."
        } else {
            flash.error = "Unknown token type, please try again."
            return redirect(controller: "user", action: "manageTokens")
        }

        def userInstance = User.findByUsername(session.uid)

        if (!userInstance) {
            flash.error = "User not found!"
            return redirect(controller: "signup")
        }

        userInstance.addToTokens(token)

        if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
            flash.message = message
            generateMail(userInstance.mail, token, "add")
            redirect(controller: "dashboard")
        } else {
            redirect(controller: "user", action: "manageTokens")
        }
    }

    def activateToken = {
        if (request.method == "POST") {
            if (!params.otp) {
                flash.error = "Please provide a one-time password using your token"
                return redirect(action: "activateToken")
            }

            //def user = User.collection.findOne([username : session.uid, tokens: [ $elemMatch : [ authKey : params.authKey, identifier : ykStatus.publicId ]]])
            def user = User.collection.findOne([username: session.uid, 'tokens.authKey': params.authKey])
            Token token = getTokenByAuthKey(user.tokens, params.authKey)
            if (user && token != null) {
                def (status, data) = TokenUtility.verifyToken((Token) token, params.otp)
                if (status) {
                    if (token.type == "yubikey" || token.type == "googletotp") {
                        if (User.collection.update([ username : session.uid, 'tokens.authKey' : params.authKey ], [ $set : [ 'tokens.$.active' : true ]])) {
                            flash.message = "Your ${tokenTypes.get(token.type)} token with ${tokenId(token)} has been successfully activated"
                            generateMail(user.mail, null, "${flash.message}.\n")
                            return redirect(controller: "dashboard")
                        } else {
                            throw new MongoException("Failed to update the database")
                        }
                    } else if ((token.type == "oathhotp" || token.type == "googlehotp") && data > 0) {
                        if (User.collection.update([ username : session.uid, 'tokens.authKey' : params.authKey ], [ $set : [ 'tokens.$.active' : true, 'tokens.$.counter' : data ]])) {
                            flash.message = "Your ${tokenTypes.get(token.type)} token with ${tokenId(token)} has been successfully activated"
                            generateMail(user.mail, null, "${flash.message}.\n")
                            return redirect(controller: "dashboard")
                        } else {
                            throw new MongoException("Failed to update the database")
                        }
                    }
                } else {
                    flash.error = data
                    return redirect(controller: "dashboard")
                }
            }

            flash.error = "Invalid activation code, token not found"
            redirect(controller: "dashboard")
        } else {
            def user = User.collection.findOne([username: session.uid, tokens: [ $elemMatch : [ authKey : params.id, active : false]]])
            if (user) {
                def token = getTokenByAuthKey(user.tokens, params.id)
                if (token) {
                    render(view: "activateToken", model: [token: token])
                }
            } else  {
                flash.error = "Invalid activation code, token not found"
                redirect(controller: "dashboard")
            }
        }
    }

    def removeToken = {
        def id = params.id
        if (!id || id.toString().length() != 24) {
            flash.message = "Missing or invalid token object id"
            return redirect(controller: "dashboard")
        }

        def user = User.collection.findOne(['username' : session.uid, 'tokens.tokId' : id])

        if (user) {
            user.tokens.each {
                if (it.tokId == id) {
                    // If the token haven't been activated yet we remove it without a confirmation request
                    if (!it.active) {
                        if (User.collection.update([username : session.uid], [ $pull : [ tokens : [ tokId : id]]])) {
                            flash.message = "The ${tokenTypes.get(it.type)} token with ${tokenId(it)} has been successfully removed from your account."
                        }
                    } else if (!it.remove) {
                        def uuid = UUID.randomUUID().toString()
                        it.authKey = uuid
                        if (User.collection.update([ username : session.uid, "tokens.tokId" : id ], [ $set : [ 'tokens.$.authKey' : uuid, 'tokens.$.remove' : true]])) {
                            generateMail(user.mail, it, "del")
                            flash.message = "Security token removal request sent, please check your mail for further instructions to complete the token removal."
                        }
                    }
                }
            }

        } else {
            flash.message = "Token was not found"
        }
        redirect(controller: "dashboard")
    }

    def confirmRemoveToken = {
        def user = User.collection.findOne(['username' : session.uid, tokens : [ $elemMatch : [ authKey : params.id, remove : true]]])
        if (user) {
            user.tokens.each {
                if (it.authKey == params.id) {
                    if (User.collection.update([username : session.uid], [ $pull : [ tokens : [ authKey : params.id]]])) {
                        flash.message = "Your ${tokenTypes.get(it.type)} token with ${tokenId(it)} has been successfully removed from your account."
                        generateMail(user.mail, it, "${flash.message}.\n")
                    }
                }
            }
        } else {
            flash.message = "Invalid token removal request"
        }
        redirect(controller: "dashboard")
    }

    def cancelRemoveToken = {
        def user = User.collection.findOne([username : session.uid, tokens : [ $elemMatch : [ tokId : params.id, remove: true]]])
        if (user) {
            if (User.collection.update([username : session.uid, 'tokens.tokId' : params.id], [$set : [ 'tokens.$.remove' : false]])) {
                flash.message = "Removal of token has been cancelled."
            }
        } else {
            flash.message = "Token not found"
        }
        redirect(controller: "dashboard")
    }

    def selectedToken = {
        def tokenType = params['tokenType']
        if (tokenType) {
            switch (tokenType) {
                case "yubikey":
                    render(template: "yubikey")
                    break
                case "oathhotp":
                    render(template: "oathhotp")
                    break
                case "googlehotp":
                    render(template: "googleauthhotp")
                    break
                case "googletotp":
                    render(template: "googleauthtotp")
                    break
            }
        } else {
           render "<div></div>"
        }
    }

    def resendTokenMail = {
        def id = params.id
        if (!id || id.toString().length() != 24) {
            flash.message = "Missing or invalid token object id"
            return redirect(controller: "dashboard")
        }
        def user = User.collection.findOne(['username' : session.uid, 'tokens.tokId' : id])

        if (user) {
            user.tokens.each {
                if (it.tokId == id) {
                    if (it.remove) {
                        generateMail(user.mail, it, "del")
                        flash.message = "Security token removal request sent, please check your mail"

                    } else if (!it.active) {
                        generateMail(user.mail, it, "add")
                        flash.message = "Security token activation request sent, please check your mail"
                    }
                }
            }
        } else {
           flash.message = "Token not found"
        }
        redirect(controller: "dashboard")
    }

    def cancel = {
        redirect(controller: "dashboard")
    }


    def generateQRCode = {
        def user = session.uid + "@unitedid.org"
        response.setHeader("Content-disposition", "attachment; filename=qrcode.png")
        response.contentType = "image/png"
        def oathUri = "otpauth://" + params.type + "/" + user + "?secret=" + params.id
        response.outputStream << BarCode.createQrCodeBlue(oathUri, 200, "png")
    }

    def generateMail(to, token, reason) {
        def subject = "United ID - "
        def template
        def activationUrl = ""
        def action = ""
        def cancelUrl = ""
        switch (reason) {
            case "add":
                action = "activateToken"
                template = "mailTokenActivation"
                subject += "Security token activation request"
                reason = "Activation request for your ${tokenTypes.get(token.type)} token with ${tokenId(token)}."
                break
            case "del":
                cancelUrl = request.siteUrl + g.createLink(controller: "user", action: "cancelRemoveToken", id: token.tokId)
                action = "confirmRemoveToken"
                template = "mailTokenRemoval"
                subject += "Security token removal request"
                break
            default:
                subject += "Security token information"
                template = "mailInfo"
        }
        if (token) {
            activationUrl = request.siteUrl + g.createLink(controller: "user", action: action, id: token.authKey)
        }
        mailService.send(
                to,
                mailService.defaultFrom(),
                mailService.defaultFromName(),
                subject,
                g.render(template: template, model: [activationUrl: activationUrl, token: token, cancelUrl: cancelUrl, message: reason]).toString())
    }

    private def tokenId(token) {
        return token.type == 'yubikey' ? "ID ${uid.yubikeyPublicId(token: token)}" : "the label '${token.identifier.encodeAsHTML()}'"
    }

    private def getTokenByAuthKey(tokens, authKey) {
        for (token in tokens) {
            if (token.authKey.equals(authKey)) {
                return token
            }
        }
        return null
    }
}
