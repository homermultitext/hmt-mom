package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._
import scala.io.Source


class PaleographySpec extends FlatSpec {

  "The PaleographyResults object" should "analyze CEX as TestResults" in {
    val f = "src/test/resources/iliad16-composite/paleography/iliad16.cex"
    val cex = Source.fromFile(f).getLines.mkString("\n")
    val testOutput = PaleographyResults(cex)
    assert(testOutput.good.size > 700)
    assert(testOutput.bad.size > 30)
  }
}
