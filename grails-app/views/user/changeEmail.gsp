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
  <title>Manage Email Addresses</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
</head>
<body>
<g:render template="changeEmailScripts"/>
<g:render template="/generic/messages" />
<div id="container">
  <div id="content">
    <div class="entry-content">
      <div class="data">
        <h4>Add email address</h4>
        <div class="form1">
          <g:form action="changeEmail" name="changeEmail">
            <fieldset>
              <p>
                <label for="mail"><g:message code="uid.usertool.changeEmail.addMail.label" default="Email address" /></label>
                <g:textField name="mail" value=""/>
              </p>
              <p>
                <label></label>
                <g:actionSubmit value="${message(code: 'form.changeEmail.label.submitbutton', default: 'Add email address')}" action="changeEmail"/>
                <g:actionSubmit class="cancel" value="${message(code: 'form.signup.label.cancelbutton', default: 'Cancel')}" action="cancel"/>
              </p>
            </fieldset>
          </g:form>
        </div>
      </div>
      <div class="data">
        <h4>Choose your primary email address</h4>
        <g:set var="i" value="${0}" />
        <ul class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <li class="address">${fieldValue(bean: user, field: "mail")}</li>
          <li>Primary address</li>
        </ul>
        <g:set var="i" value="${i + 1}"/>
        <g:each in="${user.mailAlias.sort()}">
          <g:if test="${user.mail != it}">
            <ul class="${(i % 2) == 0 ? 'odd' : 'even'}">
              <li class="address">${it.encodeAsHTML()}</li>
              <li><g:link controller="user" action="promoteEmailPrimary" id="${it}">Make primary</g:link></li>
              <li class="rem"><g:link controller="user" action="removeMailAddress" id="${it}">Remove</g:link></li>
            </ul>
            <g:set var="i" value="${i + 1}" />
          </g:if>
        </g:each>
        <g:if test="${user?.states?.key?.contains('mailAlias')}">
          <g:each in="${user?.states}" var="state">
            <g:if test="${state.key == 'mailAlias'}">
              <ul class="${(i % 2) == 0 ? 'odd' : 'even'}">
                <li class="address">${state.value.encodeAsHTML()}</li>
                <li><g:link controller="user" action="resendEmailActivation" id="${state.value}">Re-send activation</g:link></li>
                <li class="rem"><g:link controller="user" action="removeInactiveMail" id="${state.value}">Remove</g:link></li>
              </ul>
            </g:if>
          </g:each>
        </g:if>
      </div>
    </div>
  </div>
</div>
<g:render template="/generic/rightMenu" />
</body>
</html>
