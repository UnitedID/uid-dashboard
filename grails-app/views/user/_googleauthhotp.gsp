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

<%@ page import="org.unitedid.usertool.TokenUtility" %>
<script type="text/javascript">
  $(document).ready(function() {
    $('#barcode').click(function() {
      $('#manbarcode').toggle();
    });
  });
  $(function() {
    $("#addGoogleHotp").validate({
      success: function(label) {
        label.addClass("success oath");
      },
      rules: {
        otp: {
          required: true,
          minlength: 6,
          maxlength: 8,
          validateRegex: "^[0-9]+$"
        }
      },
      messages: {
        otp: {
          required: "Please enter a one-time password",
          validateRegex: "Invalid one-time password, only digits 0-9 and no space allowed"
        }
      }
    });
  });
  $(function() {
    $("input[type=submit]").uniform();
  });
</script>
<g:set var="qrcode" value="${TokenUtility.generateGoogleAuthKey()}" />
<g:form id="tokens" method="post" name="addGoogleHotp">
  <fieldset>
  <g:hiddenField name="tokenType" value="googlehotp" />
  <g:hiddenField name="seed" value="${qrcode[0]}" />
    <div class="token">
      1. In Google Authenticator, select Scan Barcode<br/>
      2. Use your phone's camera to scan the barcode below.
    </div>
    <p class="image">
      <label></label>
      <img src="${createLink(controller: 'user', action: 'generateQRCode', id: qrcode[1])}" />
    </p>
    <div id="barcode">
      Can't use the barcode?
    </div>
    <div id="manbarcode">
      <ol>
        <li>In Google Authenticator, select Manually add account.</li>
        <li>In "Enter account name" type ${session.uid}@unitedid.org.</li>
        <li>
          In "Enter key" enter your secret key:<br/>
          <p style="font-weight: bold;"><uid:friendlyGoogleAuth code="${qrcode[1]}"/></p>
        </li>
        <li>Select Counter based type of key.</li>
        <li>Push Save.</li>
      </ol>
    </div>
    <div class="token">
      When the application is configured, type the code generated below.<br/><br/>
    </div>
    <g:render template="oathhotpotp" />
    <p>
      <label></label>
      <g:actionSubmit value="${message(code: 'form.signup.label.addtokenbutton', default: 'Add token')}" action="addToken"/>
      <g:actionSubmit class="cancel" value="${message(code: 'form.signup.label.cancelbutton', default: 'Cancel')}" action="cancel"/>
    </p>
  </fieldset>
</g:form>
