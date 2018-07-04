

crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.4")
scalaVersion := (crossScalaVersions in ThisBuild).value.last



organization := "org.homermultitext"
version := "3.2.0"
licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")

libraryDependencies ++= Seq(

  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.github.pathikrit" %% "better-files" % "3.5.0",

  "edu.holycross.shot.cite" %% "xcite" % "3.5.0",
  "edu.holycross.shot" %% "scm" % "6.1.1",
  "edu.holycross.shot" %% "ohco2" % "10.9.0",
  "edu.holycross.shot" %% "citeobj" % "7.1.1",
  "edu.holycross.shot" %% "citerelations" % "2.3.0",
  "edu.holycross.shot" %% "dse" % "3.2.0",
  //"edu.holycross.shot" %% "citeiip" % "1.0.0",
  "org.homermultitext" %% "hmt-textmodel" % "3.3.0",
  "org.homermultitext" %% "hmtcexbuilder" % "3.1.2"

)
