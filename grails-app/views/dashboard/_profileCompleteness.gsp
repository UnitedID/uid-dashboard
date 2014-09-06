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

<%@ page import="org.unitedid.usertool.UserScore" %>

<div id="primary" class="aside main-aside">
  <ul class="xoxo">
    <g:if test="${user?.tokens?.size() == 0}">
    <li>
      <div class="completeness red">
        <strong>Attention!</strong>
        Your United ID account can not be used with a service provider until you have added
        at least one security token.
      </div>
    </li>
    </g:if>
    <li>
      <h4>Profile completeness<span style="float: right;">${score}%</span></h4>
      <div class="completeness">
        <g:if test="${score == 100}">
          Congratulations, your profile is complete!
        </g:if>
        <g:else>
          <ul>
            <uid:getScore user="${user}" showAddress="2" showBase="2" />
          </ul>
        </g:else>
      </div>
    </li>
  </ul>
</div>
