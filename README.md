# newrelic-servlet-filter

**Note**: At the moment this is written in Scala, so you need to have scala as a dependency in your project to use this. 

## Usage


1. Set up your project to use New Relic
2. Build a JAR of newrelic-servlet-filter with `sbt package`
3. Copy the resulting jar file to your project's `lib/` directory
4. Add the following to web.xml:


        <web-app>
            ...

            <filter>
                <filter-name>newrelic</filter-name>
                <filter-class>yle.newrelic.NewRelicTracingFilter</filter-class>
            </filter>
            <filter-mapping>
                <filter-name>newrelic</filter-name>
                <url-pattern>/*</url-pattern>
            </filter-mapping>
        </web-app>

4. You're ready to go. 

