package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._

import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


class HmtMomValidationSpec extends FlatSpec {

  val repoDir = "./src/test/resources/twins-alpha"

  "An HmtMom" should "support MID validation" in {
    val mom = HmtMom(repoDir)
    mom.validate("urn:cite2:hmt:msB.v1:119r")
    
  }
}
