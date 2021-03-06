name := "spark-hyperloglog"

version := scala.io.Source.fromFile("VERSION").mkString.stripLineEnd

scalaVersion := "2.11.8"

organization := "com.mozilla.telemetry"

// As required by https://github.com/databricks/sbt-spark-package#spark-package-developers
spName := "jklukas/spark-hyperloglog"
sparkVersion := "2.0.2"
sparkComponents ++= Seq("sql")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.twitter" %% "algebird-core" % "0.13.4"
)

credentials += Credentials(Path.userHome / ".ivy2" / ".sbtcredentials")

publishMavenStyle := true

publishTo := {
  val localMaven = "s3://net-mozaws-data-us-west-2-ops-mavenrepo/"
  if (isSnapshot.value)
    Some("snapshots" at localMaven + "snapshots")
  else
    Some("releases"  at localMaven + "releases")
}

licenses += "Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0")

homepage := Some(url("http://github.com/mozilla/spark-hyperloglog"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/mozilla/spark-hyperloglog"),
    "scm:git@github.com:mozilla/spark-hyperloglog.git"
  )
)

developers := List(
  Developer(
    id    = "fbertsch",
    name  = "Frank Bertsch",
    email = "frank@mozilla.com",
    url   = url("http://frankbertsch.com")
  )
)
