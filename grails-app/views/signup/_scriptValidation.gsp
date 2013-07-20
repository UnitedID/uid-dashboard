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

$(function() {
  $("#signup").validate({
            success: function(label) {
               label.addClass("success");
            },
            rules: {
              username: {
                required: true,
                usernameCheck:true,
                minlength: 2,
                maxlength: 50,
                validateRegex: "^[a-zA-Z0-9_-]+$"
              },
              mail: {
                required: true,
                email: true,
                mailCheck: true
              },
              password:{
                required: true,
                password: true
              },
              password2: {
                required: true,
                equalTo: "#password"
              }
            },
            messages: {
              username: {
                required: "Please enter a username",
                usernameCheck: "This username is already in use.",
                minlength: "Username need to be at least 2 characters in length",
                validateRegex: "Allowed username characters are A-Z, 0-9, dash and underscore"
              },
              mail: {
                required: "Please enter a valid email address",
                mailCheck: "This email address is already in use."
              },
              password: {
                required: "Please enter a password"
              },
              password2: {
                required: "Please confirm the password",
                equalTo: "Passwords does not match"
              }
            }
  });

});

$.validator.addMethod('usernameCheck', function(username) {
  var postURL = "${createLink(controller: 'validation', action: 'uniqueUsername')}";
  $.ajax({
            cache:false,
            async:false,
            type: "POST",
            data: "username=" + username,
            url: postURL,
            success: function(msg) {
              result = msg;
            }
          });
  return result;
}, '');

$.validator.addMethod('mailCheck', function(mail) {
  var postURL = "${createLink(controller: 'validation', action: 'uniqueMail')}";
  $.ajax({
            cache:false,
            async:false,
            type: "POST",
            data: "mail=" + mail,
            url: postURL,
            success: function(msg) {
              result = msg;
            }
          });
  return result;
}, '');

$(function(){
  $("input[type=submit], input[type=checkbox]").uniform();
});

$(document).ready(function() {
    $("a#inline").fancybox({
		'hideOnContentClick': true
	});
  $("#password").keyup(function() {
    $(this).valid();
  });
})
</script>
