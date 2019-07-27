

crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.4")
scalaVersion := (crossScalaVersions in ThisBuild).value.last



organization := "org.homermultitext"
version := "4.0.0"
licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html"))

resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayRepo("neelsmith", "maven")

libraryDependencies ++= Seq(

  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.github.pathikrit" %% "better-files" % "3.5.0",

  "edu.holycross.shot.cite" %% "xcite" % "4.1.0",
  "edu.holycross.shot" %% "scm" % "7.0.0",
  "edu.holycross.shot" %% "ohco2" % "10.13.0",
  "edu.holycross.shot" %% "citeobj" % "7.3.3",
  "edu.holycross.shot" %% "citerelations" % "2.5.0",
  "edu.holycross.shot" %% "dse" % "5.1.1",
  "edu.holycross.shot" %% "cex" % "6.3.3",
  //"edu.holycross.shot" %% "citeiip" % "1.0.0",
  "org.homermultitext" %% "hmt-textmodel" % "6.0.1",


  "edu.holycross.shot" %% "midvalidator" % "6.7.0"



)
