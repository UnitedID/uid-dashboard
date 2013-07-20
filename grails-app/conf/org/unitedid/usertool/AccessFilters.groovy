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

class AccessFilters {

    def dependsOn = [LoginFilters, PublicContentFilters, DisabledFilters]

    def ADMIN = ['adminDashboard','greenmail']
    def USER = ['dashboard', 'user', 'invite']

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                // Always allow access too /greenmail in development mode
                if (Environment.current == Environment.DEVELOPMENT && controllerName == "greenmail") {
                    return true
                }
                if (request.contentIsPublic) { return true }
                // If not public auth is required
                if (!session.uid) {
                    return false
                }

                switch (session.role) {
                    case "admin":
                        if(ADMIN.contains(controllerName)) {
                            return true
                        }
                    case "user":
                        if(USER.contains(controllerName)) {
                            return true
                        }
                    default:
                        def errorMsg = "Role ${session.role} does not have access to ${controllerName}."
                        log.error "Denied access to ${controllerName} for user ${session.uid} with role ${session.role}"
                        render(view: '/error/noAccess', model: [errorMsg])
                        return false
                }
            }
        }
    }

}
