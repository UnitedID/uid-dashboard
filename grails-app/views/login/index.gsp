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
  <link rel="stylesheet" href="${resource(dir:'css/fancybox', file:'fancybox.css')}" />
  <title>United ID - Login</title>
</head>
<body>
<g:render template="scripts" />
<g:render template="/generic/messages" />
<div id="container">
  <div id="content">
    <div class="entry-content">
      <div class="data">
        <h4>United ID - Login</h4>
        <div class="form1">
          <g:form id="login" controller="login" action="authenticate">
            <fieldset>
              <p>
                <label for="username"><g:message code="form.signup.label.userId" default="Username or email" /></label>
                <g:textField name="username" />
              </p>
              <p>
                <label for="password"><g:message code="form.signup.label.password" default="Password" /></label>
                <g:passwordField name="password" />
              </p>
              <p class="password">
                <label></label>
                <g:link controller="account" action="forgotPassword"><g:message code="forgot.password" default="Forgot your password?"/></g:link>
              </p>
              <g:if test="${session?.failedAttempts > 2}">
                <recaptcha:ifEnabled>
                  <div class="captcha">
                    <recaptcha:recaptcha theme="white"/>
                    <recaptcha:ifFailed>CAPTCHA Failed</recaptcha:ifFailed>
                  </div>
                </recaptcha:ifEnabled>
              </g:if>
              <p>
                <label></label>
                <g:submitButton name="authenticate" value="${message(code: 'form.login.label.submitbutton', default: 'Login')}" /> <g:actionSubmit name="signup" action="signup" value="${message(code: 'signup', default: 'Signup')}" />
              </p>
            </fieldset>
          </g:form>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
