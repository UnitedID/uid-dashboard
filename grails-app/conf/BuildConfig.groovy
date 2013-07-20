grails.servlet.version = "2.5"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6


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
    }

    plugins {
        test(":spock:0.7") {
            exclude "spock-grails-support"
        }

        runtime ":hibernate:$grailsVersion"

        build ":tomcat:$grailsVersion"

        runtime ":greenmail:1.3.4"
        runtime ":jquery:1.10.2"
        runtime ":jquery-ui:1.8.24"
        runtime ":jquery-validation:1.9"
        runtime ":mongodb:1.2.0"
        runtime ":quartz:0.4.2"
        runtime ":recaptcha:0.6.5"

        //compile ":cache:1.0.1"
    }
}
