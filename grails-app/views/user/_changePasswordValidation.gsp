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

<g:javascript library="jquery.uniform.min" />
<g:javascript library="jquery.validate.password" />
<script type="text/javascript">

$(function(){
  $("input[type=submit]").uniform();
});


$(function() {
  $("#changePassword").validate({
            success: function(label) {
               label.addClass("success");
            },
            rules: {
              'password':{
                required: true,
                password: true,
                passwordNotSame: "#currentPassword"
              },
              'password2': {
                required: true,
                equalTo: "#password"
              }
            },
            messages: {
              'password': {
                passwordNotSame: "This password is the same as your current password"
              },
              'password2': {
                required: "Please confirm the password",
                equalTo: "Passwords does not match"
              }
            }
  });

});

$.validator.addMethod('passwordNotSame', function(value, element, param) {
  var target = $(param).unbind(".validate-equalTo").bind("blur.validate-equalTo", function() {
    $(element).valid();
  });
  return value != target.val();
}, '');


$(document).ready(function() {
$("#password").keyup(function() {
  $(this).valid();
});
})
</script>
