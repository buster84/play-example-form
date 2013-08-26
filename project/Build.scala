import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-example-form"
  val appVersion      = "1.0-SNAPSHOT"

   val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.webjars" % "webjars-play" % "2.1.0",
    "org.fluentlenium" % "fluentlenium-festassert" % "0.9.0" % "test" exclude("org.jboss.netty", "netty"),
    "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.0.3" % "test",
    
    "org.webjars" % "bootstrap" % "2.3.2"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
