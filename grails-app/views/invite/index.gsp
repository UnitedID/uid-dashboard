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
  <title>United ID - Invite a friend</title>
  <g:javascript src="jquery-custom.js" />
</head>
<body>
<g:render template="inviteScripts" />
<g:render template="/generic/messages" />
<div id="container">
  <div id="content">
    <div class="entry-content">
      <div class="data">
        <h4>Invite a friend</h4>
        <div class="form1">
          <g:form method="post" name="invite">
            <fieldset>
              <p>
                <label for="fromName"><g:message code="uid.usertool.invite.label.fromName" default="Your name" /></label>
                <g:textField class="fromName" name="fromName" value="${inviteInstance?.fromName}" />
              </p>
              <p>
                <label for="mail"><g:message code="uid.usertool.invite.label.mail" default="Your friends email" /></label>
                <g:textField class="mail" name="mail" value="${inviteInstance?.mail}" />
              </p>
              <p>
                <label></label>
                <g:actionSubmit value="${message(code: 'uid.usertool.invite.label.sendInvite', default: 'Send invite')}" action="sendInvite"/>
                <g:actionSubmit class="cancel" value="${message(code: 'form.signup.label.cancelbutton', default: 'Cancel')}" action="cancel"/>
              </p>
            </fieldset>
          </g:form>
        </div>
      </div>
      <g:render template="sentInvitations" model="${[friends: friends]}"/>
    </div>
  </div>
</div>
<g:render template="/generic/rightMenu" />
</body>
</html>
