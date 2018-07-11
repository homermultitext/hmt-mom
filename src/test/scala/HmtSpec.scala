package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

import better.files._
import File._
import java.io.{File => JFile}

class HmtSpec extends FlatSpec {

  "An MomReporter" should "write a DSE report" in {
    val repo = EditorsRepo("src/test/resources/repos/2018-office3")
    val mom = HmtMom(repo)
    val reporter = MomReporter(mom)
    val pgOff3 = "urn:cite2:hmt:e3.v1:127v"
    val pgB = "urn:cite2:hmt:msB.v1:128v"
    val pgE3 = "urn:cite2:hmt:e3.v1:130v"


    reporter.validate(pgOff3)
  }


}
