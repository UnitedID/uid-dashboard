<script type="text/javascript">
  $(function() {
    $("#addOathHotp").validate({
      success: function(label) {
        label.addClass("success oath");
      },
      rules: {
        identifier: {
          required: true
        },
        seed: {
          required: true,
          validateRegex: "^[a-fA-F0-9]+$",
          exactlength: 40
        },
        otp: {
          required: true,
          minlength: 6,
          maxlength: 8,
          validateRegex: "^[0-9]+$"
        }
      },
      messages: {
        identifier: {
          required: "Please enter a description for your token (ex. serial nr or model)"
        },
        seed: {
          required: "Please enter your OATH-HOTP seed",
          validateRegex: "Invalid OATH seed, only characters A-F and 0-9 allowed"
        },
        otp: {
          required: "Please enter a one-time password",
           validateRegex: "Invalid one-time password, only digits 0-9 and no space allowed"
        }
      }
    });
  });

  $.validator.addMethod("exactlength", function(value, element, param) {
    return this.optional(element) || value.length == param;
  }, jQuery.format("Please enter exactly {0} characters."));

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

<g:form id="tokens" method="post" name="addOathHotp">
  <fieldset>
    <g:hiddenField name="tokenType" value="${tokenType}"/>
    <p>
      <label for="identifier"><g:message code="uid.usertool.token.oathhotp.identifier.label" default="Label (ex. serial nr or text)" /></label>
      <g:textField class="identifier oath" name="identifier" value="" />
    </p>
    <p>
      <label for="seed"><g:message code="uid.usertool.token.oathhotp.seed.label" default="Token seed" /></label>
      <g:textField class="seed oath" name="seed" value="" />
    </p>
    <g:render template="oathhotpotp" />
    <p>
      <label></label>
      <g:actionSubmit value="${message(code: 'form.signup.label.addtokenbutton', default: 'Add token')}" action="addToken"/>
      <g:actionSubmit class="cancel" value="${message(code: 'form.signup.label.cancelbutton', default: 'Cancel')}" action="cancel"/>
    </p>
  </fieldset>
</g:form>
