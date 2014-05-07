grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.7
grails.project.source.level = 1.7

grails.project.fork = [
        // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
        //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

        // configure settings for the test-app JVM, uses the daemon by default
        test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
        // configure settings for the run-app JVM
        run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
        // configure settings for the run-war JVM
        war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
        // configure settings for the Console UI JVM
        console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true

    repositories {
        inherits true

        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
        //runtime 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.1'

        /** Supply groovy 2 support for spock 0.7 */
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"

        runtime 'org.apache.commons:commons-email:1.2'
        runtime 'org.springframework.security:spring-security-core:3.2.2.RELEASE'
        runtime 'org.glassfish.jersey.core:jersey-client:2.7'
        runtime 'com.google.code.gson:gson:2.2.4'
        runtime 'com.yubico:yubico-validation-client2:2.0.1'
    }

    plugins {
        test(":spock:0.7") {
            exclude "spock-grails-support"
        }

        runtime ":hibernate:3.6.10.10"

        build ":tomcat:7.0.52.1"

        runtime ":greenmail:1.3.4"
        runtime ":jquery:1.10.2"
        runtime ":jquery-ui:1.8.24"
        runtime ":jquery-validation:1.9"
        runtime ":mongodb:3.0.0"
        runtime ":quartz:1.0.1"
        runtime ":recaptcha:0.6.7"

        //compile ":cache:1.0.1"
    }
}
