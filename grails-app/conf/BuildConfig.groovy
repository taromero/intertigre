grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
	def gebVersion = "0.7.2"
	def seleniumVersion = "2.21.0"
	
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

		test 'mysql:mysql-connector-java:5.1.18'
		runtime 'postgresql:postgresql:8.4-702.jdbc3'
		compile 'joda-time:joda-time:2.1'
		
		//Si pongo las dependencias de selenium en 'test', la aplicacion funciona sin problemas pero eclipse tira un par de errores
		compile("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
			exclude "xml-apis"
		}
		compile("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion")
		compile("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion")
		test "org.codehaus.geb:geb-spock:$gebVersion"
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.7.1"
		runtime ":jquery-ui:1.8.15"
		compile ":twitter-bootstrap:2.0.2.25"
        runtime ":resources:1.1.6"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.4"

        build ":tomcat:$grailsVersion"
		test ":geb:$gebVersion"
		test ":spock:0.6"
		compile ":spring-security-core:1.2.7.1"
		compile ":build-test-data:2.0.3"
		compile ":mail:1.0"
		compile ":lesscss-resources:1.3.0.3"
    }
}
