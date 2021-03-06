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
<%@ page import="org.unitedid.usertool.User" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <link rel="stylesheet" href="${resource(dir:'css/fancybox', file:'fancybox.css')}" />
  <title>United ID - Password Reset Successful</title>
</head>
<body>
<g:render template="uniformScript" />
<g:render template="resetPasswordValidation" />
<g:render template="/generic/messages" />
<div id="container">
  <div id="content">
    <h1 class="entry-title">Your password has been successfully updated</h1>
    <div class="entry-content">
      <p>
        Your password has been successfully updated, to login click <g:link controller="dashboard">here.</g:link>
      </p>
    </div>
  </div>
</div>
</body>
</html>
