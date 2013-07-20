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
<g:if test="${friends}">
  <div class="data">
    <h4>Sent invitations</h4>
    <g:each in="${friends}" status="i" var="friend">
      <div class="token-${(i % 2) == 0 ? 'odd' : 'even'}">
        <div class="wrap">
          <span>${fieldValue(bean: friend, field: "mail")}</span>
          <span class="link" style="font-style: italic;">Invite valid until: ${formatDate(date: (friend.dateCreated + 14), format: 'yyyy-MMM-dd')}</span>
        </div>
        <g:if test="${friend.accepted == true}">
          <div class="invite accept">Invite accepted</div>
        </g:if>
        <g:else>
          <div class="invite">Invite pending</div>
        </g:else>
      </div>
    </g:each>
  </div>
</g:if>
