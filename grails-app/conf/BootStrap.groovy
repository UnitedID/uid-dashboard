/*
 * Copyright (c) 2011 - 2013 United ID.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import grails.util.Environment
import org.unitedid.usertool.User

class BootStrap {

    def init = { servletContext ->
        /*if (UsertoolConfig.count() == 0) {
            new UsertoolConfig(name:"disabledControllers", items:['signup', 'welcome']).save(flush: true)
        }*/
        // Add a getSiteUrl to the request objects
        javax.servlet.http.HttpServletRequest.metaClass.getSiteUrl = {
            //return (delegate.scheme + "://" + delegate.serverName + ":" + delegate.serverPort + delegate.getContextPath())
            def siteUrl = delegate.scheme + "://" + delegate.serverName
            def port = delegate.serverPort
            if (port != 443 && port != 80) {
                siteUrl += ":${port}"
            }
            return siteUrl
        }

        /**
         * Environment default bootstrap settings
         */
        if (Environment.current == Environment.PRODUCTION) {
            //
        } else if (Environment.current == Environment.DEVELOPMENT) {
            //
        } else if (Environment.current == Environment.TEST) {
            //
        } else if (Environment.current.name == 'fake_session') {
              if(!User.findByUsername('fakeuser')) {
                  new User(username: 'fakeuser', mail: 'fakeuser@example.com', acceptTerms: true).save(flush: true)
              }
        }

    }

    def destroy = {
    }
}
