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
<div class="data">
  <h4>Organization</h4>
  <div>
    <span class="bold">User ID:</span>
    <span>${session.uid}</span>
  </div>
  <div>
    <span class="bold">SAML User ID:</span>
    <span>${session.uid}@unitedid.org</span>
  </div>
  <div>
    <span class="bold">First name:</span>
    <span>${fieldValue(bean: user, field: "givenName")}</span>
  </div>
  <g:if test="${user.middleName}">
    <div>
      <span class="bold">Middle name:</span>
      <span>${fieldValue(bean: user, field: "middleName")}</span>
    </div>
  </g:if>
  <div>
    <span class="bold">Last name:</span>
    <span>${fieldValue(bean: user, field: "sn")}</span>
  </div>
  <div>
    <span class="bold">Email address:</span>
    <span>${fieldValue(bean: user, field: "email")}</span>
  </div>
</div>
