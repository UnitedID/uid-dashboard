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
<div id="secondary" class="aside main-aside">
  <ul class="xoxo">
    <li>
      <div>
        <ul class="menu">
          <li><g:link controller="dashboard"><g:message code="uid.usertool.dashboard" default="My Account"></g:message></g:link></li>
          <li><g:link controller="user" action="changePassword"><g:message code="uid.usertool.changePassword" /></g:link> </li>
          <li><g:link controller="user" action="manageAccount"><g:message code="uid.usertool.manageAccount" /></g:link> </li>
          <li><g:link controller="user" action="manageTokens"><g:message code="uid.usertool.manageSecurityTokens" /></g:link> </li>
          %{--<li><g:link controller="user" action="manageVisitedSites"><g:message code="uid.usertool.manageVisitedSites" /></g:link></li>--}%
          <uid:showInviteLink/>
          %{--<li><g:link controller="user" action="manageOrganization"><g:message code="uid.usertool.manageOrganization" /></g:link> </li>--}%
       </ul>
      </div>
    </li>
  </ul>
</div>
