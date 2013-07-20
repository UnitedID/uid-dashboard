<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
    <title>ERROR 500 - OOPS!</title>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
    <link rel="shortcut icon" href="%{--
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

${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'uid2.css')}" />
  </head>
  <body>
    <div id="float"></div>
    <div id="main">
      <div><h1 style="font-size: 20px;">ERROR 500 - OOPS! AN ERROR HAS OCCURRED!</h1></div>
      <div class="welcome">
        <img src="${resource(dir:'images',file:'404-logo.png')}" alt="Broken logo" />
      </div>
      <div class="errortext">
        Uh oh, this wasn't supposed to happen. If the problem persist feel free to contact us at support@unitedid.org, please
        include the error message below:<br />
        <br />
        <div class="exception">
          <strong>${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br/>
          <strong>URI:</strong> ${request.'javax.servlet.error.request_uri'}<br/>
          <g:if test="${exception}">
            <strong>Exception Message:</strong> ${exception.message?.encodeAsHTML()} <br />
            <strong>Class:</strong> ${exception.className} <br />
            <strong>At Line:</strong> [${exception.lineNumber}] <br />
          </g:if>
        </div>
      </div>
     </div>
    </div>
  </body>
</html>
