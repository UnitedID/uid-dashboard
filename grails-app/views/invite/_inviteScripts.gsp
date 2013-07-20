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
<script type='text/javascript'>
  $(function() {
    $("#invite").validate({
      success: function(label) {
        label.addClass("success");
      },
      rules: {
        fromName: {
          required: true
        },
        mail: {
          required: true,
          email: true,
          uniqueInvitation: true
        }
      },
      messages: {
        fromName: {
          required: "Please enter your name"
        },
        mail: {
          required: "Please enter a email address",
          email: "Please enter a valid email address",
          uniqueInvitation: "An invite has already been sent to this recipient by you or someone else"
        }
      }
    });
  });

  $.validator.addMethod('uniqueInvitation', function(mail) {
     var postURL = "${createLink(controller: 'invite', action: 'uniqueInvitation')}";
    $.ajax({
      cache: false,
      async: false,
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
    $("input[type=submit]").uniform();
  });
</script>
