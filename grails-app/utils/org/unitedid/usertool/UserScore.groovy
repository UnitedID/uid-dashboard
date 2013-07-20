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

class UserScore {
    static def BASE_SCORE = [tokens: 30, givenName: 5, sn: 5, website:5]
    static def ADDRESS_SCORE = [country: 10, address1: 5, zip: 5, city: 5]

    static def calculate(user) {
        def score = 30

        BASE_SCORE.keySet().each {
            if (it == "tokens" && user?.tokens?.size() > 0) {
                score += BASE_SCORE.get(it)
            } else if (user.getProperty(it)) {
                score += BASE_SCORE.get(it)
            }
        }

        if (user.address) {
            ADDRESS_SCORE.keySet().each {
                if (user.address.getProperty(it)) {
                    score += ADDRESS_SCORE.get(it)
                }
            }
        }

        return score
    }

}
