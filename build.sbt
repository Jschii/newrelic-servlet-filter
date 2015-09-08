organization := "yle"

name := "newrelic-servlet-filter"

version := "1.0.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.newrelic.agent.java" % "newrelic-api"      % "3.20.0" % "compile",
  "javax.servlet"           % "javax.servlet-api" % "3.1.0" % "compile,provided",
  "org.scalatra"            %% "scalatra"         % "2.3.1" % "compile,provided"
)

