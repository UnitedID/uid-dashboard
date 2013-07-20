<script type="text/javascript">
  $(function() {
    $("#addYubiKey").validate({
      success: function(label) {
        label.addClass("success yk");
      },
      rules: {
        otp: {
          required: true,
          validateRegex: "[cbdefghijklnrtuv]{32,44}"
        }
      },
      messages: {
        otp: {
          required: "Please enter a YubiKey one-time password",
          validateRegex: "Invalid YubiKey one-time password"
        }
      }
    });
  });
  $(function() {
    $("input[type=submit]").uniform();
  });
</script>
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

<g:form id="tokens" method="post" name="addYubiKey">
  <fieldset>
    <g:hiddenField name="tokenType" value="yubikey"/>
    <g:render template="yubikeyotp"/>
    <p>
      <label></label>
      <g:actionSubmit value="${message(code: 'form.signup.label.addtokenbutton', default: 'Add token')}" action="addToken"/>
      <g:actionSubmit class="cancel" value="${message(code: 'form.signup.label.cancelbutton', default: 'Cancel')}" action="cancel"/>
    </p>
  </fieldset>
</g:form>
