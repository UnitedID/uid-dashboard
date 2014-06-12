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

class User {
    ObjectId id = new ObjectId()
    Integer uidObjectVersion = 1
    String givenName
    String sn
    String username
    String mail
    String website
    String password
    Credential credential

    boolean acceptTerms
    boolean active = false
    String activationKey = UUID.randomUUID().toString()

    Address address = new Address()
    List<Token> tokens
    List<State> states
    List<String> mailAlias = []

    Date dateCreated
    Date lastUpdated

    static mapWith = "mongo"

    static hasMany = [tokens:Token, states:State]

    static embedded = ['address', 'tokens', 'states', 'credential']

    static transients = ['password']

    static mapping = {
        id generator: "assigned"
        username index:true, indexAttributes: [unique: true, dropDups: true]
        mail index:true, indexAttributes: [unique: true, dropDups: true]
        mailAlias index:true, indexAttributes: [unique: true, dropDups: true]
    }

    static constraints = {
        username blank: false, nullable: false, size: 2..50, matches:"[a-zA-Z0-9_-]+"
        credential blank: true, nullable: true
        givenName blank: true, nullable: true
        sn blank: true, nullable: true
        mail blank: false, nullable: false, matches:"[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}"
        active nullable: false
        website blank: true,
                nullable: true,
                matches:"(https?://)?([-\\w\\.]+)+(:\\d+)?(:\\w+)?(@\\d+)?(@\\w+)?([-\\w\\.]+)(/([-\\w/_\\.]*(\\?\\S+)?)?)?"
        address nullable: true, validator: { val, obj ->
            if (!val?.zip)
                return true
            if (val?.zip && (val.zip == "" || val.zip == null))
                return true
            (val?.zip && val.zip =~ /^[0-9A-Za-z- ]{2,15}$/) ? true : ['invalid.zip']
        }
        tokens nullable: true
        states nullable: true
        mailAlias nullable: true
        acceptTerms validator: { val, obj ->
            !val ? false : true
        }
        /*password blank: false,
                nullable: false*/
        activationKey nullable: true
    }

    def beforeInsert() {
        this.username = username.toLowerCase()
        this.mail = mail.toLowerCase()
    }
}

class Address {
    String id
    String address1
    String address2
    String zip
    String city
    String country

    static constraints = {
        address1 nullable: true
        address2 nullable: true
        zip nullable: true
        city nullable: true
        country nullable: true
    }
}

class Organization {
    String id
    String title
}

class State {
    String key
    String value
    String authKey
    Date date
    Date dateCreated = new Date()

    static belongsTo = User
    static constraints = {

    }

    public String toString() {
        return key + "=" + value
    }
}

class Credential {
    String credentialId
    String salt

    static constraints = {
        credentialId nullable: true
        salt nullable: true
    }
}

class Token {
    String credentialId = new ObjectId().toString()
    String type
    String guiType
    String identifier
    String nonce
    boolean active = false
    boolean remove = false
    String authKey = UUID.randomUUID().toString()

    Date dateCreated = new Date()
    Date lastUpdated = new Date()

    static belongsTo = User
    static constraints = {}
}
