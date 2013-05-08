import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "makeke"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.webjars" % "webjars-play" % "2.1.0",
    "org.webjars" % "bootstrap" % "2.3.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
