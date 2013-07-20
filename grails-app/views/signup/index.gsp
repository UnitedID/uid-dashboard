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
  <title>United ID - Sign Up</title>
</head>
<body>
  <g:render template="scriptValidation" />
  <g:render template="/generic/messages" />
<div id="container">
  <div id="content">
    <h1 class="entry-title">Sign Up</h1>
    <div class="entry-content">
      <div class="form1">
        <g:form id="signup" action="register" name="signup">
          <fieldset>
            <p>
              <label for="username"><g:message code="form.signup.label.userId" default="Username" /></label>
              <g:textField name="username" id="username" class="required username" value="${userInstance?.username}" /><span></span>
            </p>
            <p>
              <label for="mail"><g:message code="form.signup.label.email" default="Email" /></label>
              <g:textField name="mail" class="required email" value="${userInstance?.mail}"/>
            </p>
            <div class="password-meter">
              <div class="password-meter-message">&nbsp;</div>
              <div class="password-meter-bg">
                <div class="password-meter-bar"></div>
              </div>
            </div>
            <p>
              <label for="password"><g:message code="form.signup.label.password" default="Password" /></label>
              <g:passwordField name="password" value=""/>
            </p>
            <p>
              <label for="password2"><g:message code="form.signup.label.password2" default="Confirm password" /></label>
              <g:passwordField name="password2" value=""/>
            </p>
            <p>
              <label for="acceptTerms"></label>
              <span><g:checkBox name="acceptTerms" value="${userInstance?.acceptTerms}"/>I agree to the <a id="inline" href="#tos">Terms of Service</a></span>
            </p>
            <recaptcha:ifEnabled>
              <div class="captcha">
                <recaptcha:recaptcha theme="white"/>
                <recaptcha:ifFailed>CAPTCHA Failed</recaptcha:ifFailed>
              </div>
            </recaptcha:ifEnabled>
            <p>
              <label></label>
              <g:submitButton name="register" value="${message(code: 'form.signup.label.submitbutton', default: 'Sign Up')}" />
            </p>
          </fieldset>
        </g:form>
      </div>
    </div>
    <div style="display: none;">
      <div id="tos">
        <g:render template="tos" />
      </div>
    </div>
  </div>
</div>
</body>
</html>
