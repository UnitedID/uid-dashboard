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

import org.bson.types.ObjectId

class Invite {
    ObjectId id

    String invitingUser

    String fromName
    String mail
    boolean accepted = false
    String inviteKey = new ObjectId().toString()

    Date dateCreated
    Date lastUpdated

    static mapWith = "mongo"

    static mapping = {
        invitingUser index: true
        mail index: true, indexAttributes: [unique: true, dropDups: true]
    }

    static constraints = {
        invitingUser blank: false, nullable: false
        fromName blank: false, nullable: false
        mail blank: false, nullable: false, matches: "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}"
        inviteKey nullable: true

    }

    def beforeInsert() {
        this.mail = mail.toLowerCase()
    }

    public String toString() {
        return mail
    }
}
