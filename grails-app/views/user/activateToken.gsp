%{--
  - Copyright (c) 2011 - 2013 United ID.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  - http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <title>United ID - Token Activation</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:render template="scripts" />
  </head>
  <body>
  <g:render template="activateTokenScripts" />
  <g:render template="/generic/messages" />
  <div id="container">
    <div id="content">
      <div class="entry-content">
        <div class="data">
          <h4>Token Activation</h4>
          <p>
          <g:if test="${token.type == 'yubikey'}">
            To complete the activation for your Yubikey with ID ${uid.yubikeyPublicId(token: token)} please enter a one-time password.
          </g:if>
          <g:elseif test="${token.type == 'googlehotp'}">
            To complete the activation for your Google Authenticator with the label '${token.identifier.encodeAsHTML()}' please enter a one-time password.
          </g:elseif>
          <g:else>
            To complete the activation for your OATH-HOTP token with the label '${token.identifier.encodeAsHTML()}' please enter a one-time password.
          </g:else>
          </p>
          <div class="form1">
          <g:form method="post" name="activateToken">
            <fieldset>
            <g:hiddenField name="authKey" value="${token.authKey}"/>
            <g:if test="${token.type == 'yubikey' }">
              <g:render template="yubikeyotp" />
            </g:if>
            <g:elseif test="${token.type == 'oathhotp' || token.type == 'googlehotp'}">
              <g:render template="oathhotpotp" />
            </g:elseif>
            <p>
              <label></label>
              <g:actionSubmit value="${message(code: 'form.activateToken.label.activate', default: 'Activate token')}" action="activateToken"/>
            </p>
            </fieldset>
          </g:form>
        </div>
        </div>
      </div>
    </div>
  </div>
  <g:render template="/generic/rightMenu" />
  </body>
</html>
