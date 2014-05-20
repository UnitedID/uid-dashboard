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

class DashboardController {

    def index = {
        def user = User.findByUsername(session.uid)
        def visitedSites = PersistentId.findByPrincipalName(session.uid)
        if (user) {
            def score = UserScore.calculate(user)
            render(view: 'index', model: [user: user, visitedSites: visitedSites, score: score])
        } else {
            session.invalidate()
            render(view: '/error/failedLogin')
        }
    }

    def logout = {
        session.invalidate()
        redirect(controller: 'login')
    }

}
