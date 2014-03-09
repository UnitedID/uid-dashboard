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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
    <title><g:layoutTitle default="United ID" /></title>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8" />
    <meta name="robots" content="index,follow" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'forms.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
    <g:layoutHead />
    <g:javascript library="jquery" plugin="jquery"/>
    <jqval:resources/>
    <g:javascript library="application" />
    <g:if test="${params.controller == 'signup'}">
        <g:javascript library="jquery.easing-1.3.pack" />
        <g:javascript library="jquery.fancybox-1.3.4.pack" />
    </g:if>
  </head>
  <body>
    <div id="cornerbanner"></div>
    <div id="wrapper">
      <div id="header">
        <div id="branding">
          <div id="blog-title"><span><a href="http://unitedid.org/about/" title="United ID"><img src="${resource(dir:'images', file:'logo_net_globe_255.png')}" alt="Logo" /></a></span></div>
        </div> <!-- #branding -->
        <div id="access">
        <div id="menu">
          <g:if test="${session?.uid && !session?.tokenAuthRequired}">
            <span class="user"><g:link class="logout" controller="dashboard" action="logout">Logout</g:link> <g:link controller="dashboard">${session.uid}</g:link></span>
          </g:if>
        </div>
      </div>
      </div> <!-- #header -->
      <div id="main">
        <g:layoutBody />
      </div>
      <div id="footer">
        <div id="siteinfo">
        &copy; 2014 United ID.
        </div> <!-- #siteinfo -->
      </div> <!-- #footer -->
    </div> <!-- #wrapper -->
  </body>
</html>
