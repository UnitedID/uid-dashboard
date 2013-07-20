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

class ValidationController {

    def uniqueUsername = {
        String username = params.username
        String result = false
        // Mongodb is case-sensitive and doesn't allow searching indexes with
        // non case sensitive, thus we enforce username to lower case
        if (!User.findByUsername(username.toLowerCase())) {
            result = true
        }
        response.setContentType("text/json;charset=UTF-8")
        render result
    }

    def uniqueMail = {
        String mail = params.mail.toString().toLowerCase()
        String result = false
        // Mongodb is case-sensitive and doesn't allow searching indexes with
        // non case sensitive, thus we enforce email to lower case
        if (!User.collection.findOne([$or: [[mail: mail], [states: [$elemMatch: [key:"mailAlias", value: mail]]]]])) {
            result = true
        }
        response.setContentType("text/json;charset=UTF-8")
        render result
    }
}
