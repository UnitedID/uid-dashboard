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

<g:if test="${tokens}">
<div class="data">
  <h4>Your security tokens</h4>
    <g:each in="${tokens}" status="i" var="token">
      <div class="token-${(i % 2) == 0 ? 'odd' : 'even'}">
        <div class="wrap">
          <uid:displayToken token="${token}" />
          <g:if test="${token.remove}">
            <span class="link"><g:link controller="user" action="cancelRemoveToken" id="${token.credentialId}">Cancel removal</g:link></span>
          </g:if>
          <g:else>
            <span class="link"><g:link controller="user" action="removeToken" id="${token.credentialId}">Remove token</g:link></span>
          </g:else>
        </div>
        <g:if test="${!token.active}">
          <div class="tokaction">Activation pending<span class="link"><g:link controller="user" action="resendTokenMail" id="${token.credentialId}">Re-send activation mail</g:link></span></div>
        </g:if>
        <g:elseif test="${token.remove}">
          <div class="tokaction">Removal pending<span class="link"><g:link controller="user" action="resendTokenMail" id="${token.credentialId}">Re-send removal mail</g:link></span></div>
        </g:elseif>
      </div>
    </g:each>
</div>
</g:if>
