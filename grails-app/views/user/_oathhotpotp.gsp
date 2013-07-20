<script type="text/javascript">
  $(function() {
    $("#activateToken").validate({
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
</script>
<p>
  <label for="otp">%{--
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

<g:message code="uid.usertool.token.oathhotp.otp.label" default="One-time password" /></label>
  <g:textField class="otp oath-otp" name="otp" value="" />
</p>
