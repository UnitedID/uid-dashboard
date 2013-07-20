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
  <h4>Personal Information</h4>
  <div>
    <span class="bold">First name:</span>
    <span>${fieldValue(bean: user, field: "givenName")}</span>
  </div>
  <div>
    <span class="bold">Last name:</span>
    <span>${fieldValue(bean: user, field: "sn")}</span>
  </div>
  <div>
    <span class="bold">Address 1:</span>
    <span>${fieldValue(bean: user, field: "address.address1")}</span>
  </div>
  <div>
    <span class="bold">Address 2:</span>
    <span>${fieldValue(bean: user, field: "address.address2")}</span>
  </div>
  <div>
    <span class="bold">Postal code:</span>
    <span>${fieldValue(bean: user, field: "address.zip")}</span>
  </div>
  <div>
    <span class="bold">City:</span>
    <span>${fieldValue(bean: user, field: "address.city")}</span>
  </div>
  <g:if test="${user?.address?.country}">
      <div>
          <span class="bold">Country:</span>
          <span><g:country code="${user?.address?.country}" /></span>
      </div>
  </g:if>
  <div>
    <span class="bold">Personal website:</span>
    <g:if test="${user?.website?.toString()?.startsWith('http://') || user?.website?.toString()?.startsWith('https://')}">
      <span><a href="${fieldValue(bean: user, field: "website")}" target="_blank">${fieldValue(bean: user, field: "website")}</a></span>
    </g:if>
    <g:elseif test="${user?.website}">
      <span><a href="http://${fieldValue(bean: user, field: "website")}" target="_blank">http://${fieldValue(bean: user, field: "website")}</a></span>
    </g:elseif>
  </div>
</div>
