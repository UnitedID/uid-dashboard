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

package org.unitedid.usertool

import grails.util.Environment

class LoginFilters {

    def dependsOn = [PublicContentFilters]

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                // Always allow access too /greenmail in development mode
                if (Environment.current == Environment.DEVELOPMENT && controllerName == "greenmail") {
                    return true
                }
                if (request.contentIsPublic) { return true }
                if (session?.tokenAuthRequired) { redirect(controller: 'login')}
                if (!session.uid) {
                    if (Environment.current == Environment.PRODUCTION) {
                        session.forwardURI = request.forwardURI - request.contextPath
                        redirect(controller: "login")
                    } else if (Environment.current.name == 'fake_session') {
                        def userInstance
                        try {
                            userInstance = User.findByUsername('fakeuser')
                        } catch (Exception e) {
                            Thread.sleep(500)
                            try {
                                userInstance = User.findByUsername('fakeuser')
                            } catch (Exception ex) {
                                throw new RuntimeException("Failed to re-connect to the database". ex)
                            }
                        }
                        if (userInstance) {
                            session.uid = userInstance.username
                            session.oid = userInstance.id
                            session.role = "admin"
                        }
                        session.forwardURI = request.forwardURI - request.contextPath
                        redirect(controller: "dashboard")
                    } else {
                        session.forwardURI = request.forwardURI - request.contextPath
                        redirect(controller: "login")
                    }
                }
            }
        }
    }
}
