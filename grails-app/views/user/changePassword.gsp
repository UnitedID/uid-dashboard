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
    <title>Change password</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  </head>
  <body>
  <g:render template="changePasswordValidation" />
  <g:render template="/generic/messages" />
  <div id="container">
    <div id="content">
      <div class="entry-content">
        <div class="data">
          <h4>Change password</h4>
          <div class="form1">
            <g:form action="saveNewPassword" name="changePassword">
              <fieldset>
                <p>
                  <label for="currentPassword"><g:message code="form.signup.label.password" default="Current password" /></label>
                  <g:passwordField name="currentPassword" value="${currentPassword}"/>
                </p>
                <div class="password-meter">
                  <div class="password-meter-message">&nbsp;</div>
                  <div class="password-meter-bg">
                    <div class="password-meter-bar"></div>
                  </div>
                </div>
                <p>
                  <label for="password"><g:message code="form.signup.label.password" default="New password" /></label>
                  <g:passwordField name="password" value=""/>
                </p>
                <p>
                  <label for="password2"><g:message code="form.signup.label.password2" default="Confirm new password" /></label>
                  <g:passwordField name="password2" value=""/>
                </p>
                <p>
                  <label></label>
                  <g:actionSubmit value="${message(code: 'form.signup.label.submitbutton', default: 'Save')}" action="saveNewPassword"/>
                  <g:actionSubmit class="cancel" value="${message(code: 'form.signup.label.cancelbutton', default: 'Cancel')}" action="cancel"/>
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
