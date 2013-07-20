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
<%@ page import="org.unitedid.usertool.User" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title>United ID - Password Reset</title>
</head>
<body>
<g:render template="uniformScript" />
<g:render template="resetPasswordValidation" />
<g:render template="/generic/messages" />
<div id="container">
  <div id="content">
    <h1 class="entry-title">Reset your password</h1>
    <div class="entry-content">
      <p>
        Hello ${displayName},<br /><br />
        To complete your password reset request please provide a new password below.
      </p>
      <div class="form1">
        <g:form action="resetPassword" name="resetPassword">
          <fieldset>
            <g:hiddenField name="userId" value="${userId}"/>
            <g:hiddenField name="key" value="${key}"/>
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
              <g:submitButton name="submitResetPassword" value="${message(code: 'form.resetPassword.label.submitbutton', default: 'Change password')}" />
            </p>
          </fieldset>
        </g:form>
      </div>
    </div>
  </div>
</div>
</body>
</html>
