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

import org.unitedid.yhsm.utility.ModHex

class TokenTagLib {
    static namespace = "uid"

    def displayToken = { attrs ->
        def token = attrs.token
        switch(token.type) {
            case "yubikey":
                out << "<span><img src=\"${resource(dir:'images/dongles', file:'black_yubikey.png')}\" alt=\"Black YubiKey\"/></span>"
                if (token.identifier =~ /^cccc/) {
                    out << "<span>YubiKey - ${fieldValue(bean: token, field: 'identifier')} (${ModHex.toDecimal(token.identifier)})</span>"
                } else {
                    out << "<span>YubiKey - ${fieldValue(bean: token, field: 'identifier')}</span>"
                }
                break
            case "oathhotp":
                out << "<span><img src=\"${resource(dir:'images/dongles', file:'c100-small.png')}\" alt=\"OATH-HOTP token\"/></span>"
                out << "<span>OATH-HOTP - ${fieldValue(bean: token, field: 'identifier')}</span>"
                break
            case "googlehotp":
                out << "<span><img src=\"${resource(dir:'images/dongles', file:'googleauth.png')}\" alt=\"Google Authenticator\"/></span>"
                out << "<span>Google Authenticator - ${fieldValue(bean: token, field: 'identifier')}</span>"
                break
            case "googletotp":
                out << "<span><img src=\"${resource(dir:'images/dongles', file:'googleauth.png')}\" alt=\"Google Authenticator\"/></span>"
                out << "<span>Google Authenticator - ${fieldValue(bean: token, field: 'identifier')}</span>"
                break
        }
    }

    def yubikeyPublicId = { attrs ->
        def token = attrs.token
        if (token.identifier =~ /^cccc/) {
            out << "${token.identifier} (${ModHex.toDecimal(token.identifier)})"
        } else {
            out << token.identifier
        }
    }

    def friendlyGoogleAuth = { attrs ->
        String code = attrs.code.toLowerCase()

        for (int i = 0; i < code.length(); i++) {
            if (i > 0 && i % 4 == 0)
                out << " "
            out << code.charAt(i)
        }
    }
}
