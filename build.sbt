organization := "yle"

name := "newrelic-servlet-filter"

version := "1.0.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.newrelic.agent.java" % "newrelic-api"      % "3.7.1" % "compile",
  "javax.servlet"           % "javax.servlet-api" % "3.1.0" % "compile,provided",
  "org.scalatra"            %% "scalatra"         % "2.2.1" % "compile,provided"
)

