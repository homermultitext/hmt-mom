

scalaVersion := "2.11.8"


organization := "edu.holycross.shot"
version := "2.0.0"
licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))

resolvers += Resolver.jcenterRepo
resolvers += "beta" at "http://beta.hpcc.uh.edu/nexus/content/repositories/releases"
resolvers += Resolver.bintrayRepo("neelsmith", "maven")


libraryDependencies ++= Seq(

  "org.scalatest" %% "scalatest" % "3.0.1" % "test",


  "edu.holycross.shot.cite" %% "xcite" % "2.4.0",
  "edu.holycross.shot" %% "scm" % "4.0.1",
  "edu.holycross.shot" %% "ohco2" % "9.0.1",
  "edu.holycross.shot" %% "citeobj" % "3.1.3",
  "edu.holycross.shot" %% "citerelations" % "1.1.1",
  "edu.holycross.shot" %% "citeiip" % "1.0.0"
)
