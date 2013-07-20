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
  <title>United ID - Forgot your password?</title>
</head>
<body>
<g:render template="/generic/messages" />
<g:render template="uniformScript" />
<g:render template="forgotPasswordScripts"/>
<div id="container">
  <div id="content">
    <h1 class="entry-title">Forgot your password?</h1>
    <div class="entry-content">
      <p>
        To reset your password, type the email address associated with your account.
      </p>
      <div class="form1">
        <g:form action="forgotPassword" name="lostPassword">
          <fieldset>
            <p>
              <label for="mail"><g:message code="form.signup.label.email" default="Email address" /></label>
              <g:textField name="mail" class="${error} email" value="${userInstance?.mail}"/>
              <g:if test="${error}">
                <label for="mail" class="error">No account found with that email address</label>
              </g:if>
            </p>
            <p>
              <label></label>
              <g:submitButton name="forgotPassword" value="${message(code: 'form.forgotPassword.label.submitbutton', default: 'Submit')}" />
            </p>
          </fieldset>
        </g:form>
      </div>
    </div>
  </div>
</div>
</body>
</html>
