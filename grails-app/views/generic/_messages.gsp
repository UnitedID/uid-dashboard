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
<script type="text/javascript">
  setTimeout(function() {
    $('#message').fadeOut('slow');
}, 10000);
</script>
<g:if test="${flash.message}">
  <div id="message" class="message">${flash.message}</div>
</g:if>
<g:if test="${flash.error}">
  <div class="errormsg">${flash.error}</div>
</g:if>
<g:hasErrors bean="${userInstance}">
  <div class="errors">
    <g:renderErrors bean="${userInstance}" as="list" />
  </div>
</g:hasErrors>
<g:hasErrors bean="${inviteInstance}">
  <div class="errors">
    <g:renderErrors bean="${inviteInstance}" as="list" />
  </div>
</g:hasErrors>
