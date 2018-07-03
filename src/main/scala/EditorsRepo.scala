package edu.holycross.shot.hmtmom

import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


/** Root directory of an HMT editorial repository
* laid out according to 2018 standards.
*
* @param baseDir Name of root directory.
*/
case class EditorsRepo(baseDir: String)  {
  val dseDir = File(baseDir + "/dse")
  val validationDir = File(baseDir + "/validation")
  val editionsDir = File(baseDir + "/editions")
  val paleographyDir = File(baseDir + "/paleography")
  val dirs = Vector(dseDir, editionsDir, validationDir, paleographyDir)

  for (d <- dirs) {
    require(d.exists, "Repository not correctly laid out: missing directory " + d)
  }
  val ctsCatalog = editionsDir/"catalog.cex"
  val ctsCitation = editionsDir/"citation.cex"
  for (conf <- Seq(ctsCatalog, ctsCitation)) {
    require(conf.exists,"Missing required configuration file: " + conf)
  }

}
