package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

import better.files._
import File._
import java.io.{File => JFile}

class MomReporterWriteFromBadInputSpec extends FlatSpec {

  "An MomReporter" should "write a DSE report" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val reporter = MomReporter(mom)
    val pg = "urn:cite2:hmt:msA.v1:126r"
    reporter.validate(pg)
  }


}
