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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.unitedid.usertool.Token; org.unitedid.usertool.User" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title>Manage Security Tokens</title>
  <g:javascript src="jquery-custom.js" />
  <g:render template="scripts" />
</head>
<body>
<g:render template="tokenScriptValidation" />
<g:render template="/generic/messages" />
<div id="container">
  <div id="content">
    <div class="entry-content">
      <div class="data">
        <h4>Add Security Token</h4>
        <div class="form1">
          <label></label><g:select id="token" name="tokenType" onchange="${remoteFunction(controller: 'user', action: 'selectedToken', update: 'tokenForm', params: '\'tokenType=\' + this.value' )}" noSelection="${['':'-- Choose your token --']}" from="${tokenTypes.entrySet()}" optionKey="key" optionValue="value" />
          <div id="tokenForm"></div>
        </div>
      </div>
      <g:render template="tokenList" model="${[tokens:userInstance.tokens]}"/>
    </div>
  </div>
</div>
<g:render template="/generic/rightMenu" />
</body>
</html>
