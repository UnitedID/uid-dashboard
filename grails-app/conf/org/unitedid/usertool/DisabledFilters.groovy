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

class DisabledFilters {

    def dependsOn = [LoginFilters]

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                def disabledList
                try {
                    disabledList = UsertoolConfig.findByName("disabledControllers");
                } catch (Exception e) {
                    Thread.sleep(500)
                    try {
                        disabledList = UsertoolConfig.findByName("disabledControllers");
                    } catch (Exception ex) {
                        throw new RuntimeException("Failed to re-connect to the database")
                    }
                }

                if (disabledList && disabledList?.items?.contains(controllerName)) {
                    if (controllerName == "signup") {
                        if (session?.inviteValid) {
                            return true
                        }
                        if (actionName == "activateAccount" || actionName == "invite") {
                            return true
                        }
                    }
                    redirect(uri:"/")
                } else if (disabledList && disabledList?.items?.contains("signup") && controllerName == "invite") {
                    return true
                } else if (controllerName == "invite") {
                    redirect(controller: "dashboard")
                }
                return true
            }
        }
    }
}
