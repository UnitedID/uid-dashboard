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
  <title>Manage account</title>
  <g:javascript src="jquery-custom.js" />
</head>
<body>
<g:render template="manageAccountScriptValidation" />
<g:render template="/generic/messages" />
<div id="container">
  <div id="content">
    <div class="entry-content">
      <div class="data">
        <h4>Manage personal information</h4>
        <div id="manage-account-form" class="form1">
          <g:form useToken="true" method="post" name="manageAccount">
            <fieldset>
              <p>
                <label for="givenName"><g:message code="uid.usertool.account.label.givenName" default="First name" /></label>
                <g:textField name="givenName" value="${userInstance?.givenName}"/>
              </p>
              <p>
                <label for="sn"><g:message code="uid.usertool.account.label.surName" default="Last name" /></label>
                <g:textField name="sn" value="${userInstance?.sn}"/>
              </p>
              <p>
                <label for="address.address1"><g:message code="uid.usertool.account.label.address1" default="Address 1" /></label>
                <g:textField name="address.address1" value="${userInstance?.address?.address1}"/>
              </p>
              <p>
                <label for="address.address2"><g:message code="uid.usertool.account.label.address2" default="Address 2" /></label>
                <g:textField name="address.address2" value="${userInstance?.address?.address2}"/>
              </p>
              <p>
                <label for="address.zip"><g:message code="uid.usertool.account.label.zip" default="Postal code" /></label>
                <g:textField name="address.zip" value="${userInstance?.address?.zip}"/>
              </p>
              <p>
                <label for="address.city"><g:message code="uid.usertool.account.label.city" default="City" /></label>
                <g:textField name="address.city" value="${userInstance?.address?.city}"/>
              </p>
              <p>
                <label for="address.country"><g:message code="uid.usertool.account.label.country" default="Country" /></label>
                <g:countrySelect name="address.country" noSelection="['':' -- Choose country --']" value="${userInstance?.address?.country}"/>
              </p>
              <p>
                <label for="website"><g:message code="uid.usertool.account.label.website" default="Personal website" /></label>
                <g:textField name="website" value="${userInstance?.website}"/>
              </p>
              <p>
                <label></label>
                <g:actionSubmit value="${message(code: 'form.signup.label.submitbutton', default: 'Save')}" action="saveAccount"/>
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
