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

class UserScoreTagLib {
    static namespace = "uid"

    def getScore = { attrs ->
        def user = attrs.user
        def address = user.address
        def showBase = Integer.parseInt(attrs.showBase)
        def showAddress = Integer.parseInt(attrs.showAddress)

        def iter = 0

        for (def key : UserScore.BASE_SCORE.keySet()) {
            if (iter >= showBase) {
                break
            }
            if (user.getProperty(key) instanceof List) {
                if (user.getProperty(key).size() > 0) {
                    continue
                }
            } else if (user.getProperty(key)){
                continue
            }

            def score = UserScore.BASE_SCORE.get(key)
            switch(key) {
                case "tokens":
                    out << getScoreLink("manageTokens", "Add security token", score)
                    break
                case "givenName":
                    out << getScoreLink("manageAccount", "Add first name", score)
                    break
                case "sn":
                    out << getScoreLink("manageAccount", "Add last name", score)
                    break
                case "website":
                    out << getScoreLink("manageAccount", "Add personal website", score)
                    break
            }
            iter++
        }

        iter = 0
        for (def key : UserScore.ADDRESS_SCORE.keySet()) {
            if (iter >= showAddress) {
                break
            }

            if (address && address.getProperty(key)){
                continue
            }

            def score = UserScore.ADDRESS_SCORE.get(key)
            switch(key) {
                case "country":
                    out << getScoreLink("manageAccount", "Add country", score)
                    break
                case "address1":
                    out << getScoreLink("manageAccount", "Add address", score)
                    break
                case "zip":
                    out << getScoreLink("manageAccount", "Add postal code", score)
                    break
                case "city":
                    out << getScoreLink("manageAccount", "Add city", score)
                    break
            }
            iter++
        }
    }

    def getScoreLink(action, msg, score) {
        return "<li>" + g.link(controller: "user", action: action) {msg} + " (+${score}%)</li>"
    }

    def getBaseScore = { attrs ->
        def user = attrs.user
        def key = attrs.key
        def msg = attrs.msg
        def controller = attrs.controller
        def action = attrs.action
        if (user.getProperty(key) instanceof List) {
            if (user.getProperty(key).size() == 0) {
                out << "<li>" + g.link(controller: controller, action: action) {msg} + " (+${UserScore.BASE_SCORE.get(key)}%)</li>"
            }
        } else if (!user.getProperty(key)) {
            out << "<li>" + g.link(controller: controller, action: action) {msg} + " (+${UserScore.BASE_SCORE.get(key)}%)</li>"
        }
    }

    def getAddressScore = { attrs ->
        def address = attrs.user
        def key = attrs.key
        def msg = attrs.msg
        def controller = attrs.controller
        def action = attrs.action

        if (!address || !address.getProperty(key)) {
            out << "<li>" + g.link(controller: controller, action: action) {msg} + " (+${UserScore.ADDRESS_SCORE.get(key)}%)</li>"
        }
    }
}
