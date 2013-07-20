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

class InvitationCleanUpJob {
    static triggers = {
        cron name: "invitationCleanUpJob", cronExpression: "0 */5 * * * ?"
    }

    def execute() {
        log.info("invitationCleanUpJob triggered")

        def expiryDate = new Date() - 14
        def invites = null
        try {
            Invite.withTransaction {
                invites = Invite.withCriteria {
                    lt('dateCreated', expiryDate)
                }
            }
        } catch (Exception e) {
            Thread.sleep(500)
            try {
                Invite.withTransaction {
                    invites = Invite.withCriteria {
                        lt('dateCreated', expiryDate)
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("Failed to re-connect to the database", ex)
            }
        }
        invites.each {
            println "Removing expired invite for " + it
            it.delete(flush: true)
        }
    }
}
