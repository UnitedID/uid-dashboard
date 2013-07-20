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

<g:if test="${params.action == 'activateToken'}">
<script type="text/javascript">
$(function() {
    $("#activateToken").validate({
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
</script>
</g:if>
<p>
  <label for="otp"><g:message code="uid.usertool.token.label.YubiKey" default="One-time password" /></label>
  <g:textField onkeypress="handle_enter(event);" class="otp yk" name="otp" value="" />
</p>
