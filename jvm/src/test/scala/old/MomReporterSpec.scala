package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

import better.files._
import File._
import java.io.{File => JFile}

class MomReporterSpec extends FlatSpec {

  "An MomReporter" should "throw an exception if asked to validate a page with a bad urn" in pending /* {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val reporter = MomReporter(mom)
    try {
      reporter.validate("BogusUrn")
      fail("Should not have validated bad urn.")
    } catch {
      case t: Throwable => assert(true)
    }
  }*/

  it should "make a directory for reports based on the collection and object ID" in pending /* {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val reporter = MomReporter(mom)
    val testUrnStr = "urn:cite2:testns:mom.v1:demopage"
    reporter.validate(testUrnStr)
    val expectedDir = repo.validationDir/"mom-demopage"
    assert(expectedDir.exists)
    expectedDir.delete()
  }*/

  it should "write a DSE report" in pending /* {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val reporter = MomReporter(mom)
    val pg = "urn:cite2:hmt:msA.v1:126r"
    reporter.validate(pg)
  } */


}
