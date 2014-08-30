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
          <g:form useToken="true" controller="login" action="validateTokenAuth">
            <fieldset>
              <p>
                <label for="otp"><g:message code="form.signup.label.otp" default="One-time password" /></label>
                <g:textField  name="otp" class="otp"/>
              </p>
              <p>
                <label></label>
                <g:submitButton name="authenticate" value="${message(code: 'form.login.label.submitbutton', default: 'Continue')}" />
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
