package edu.holycross.shot.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._


/**
*/
class TestIdentifierSpec extends FlatSpec {



  "A TestIdentifier" should "have labels and units of tests" in  {
    val urn = Cite2Urn("urn:cite2:hmt:testsuite.2017a:demo1")
    val label = "Test that all codepoints are valid"
    val testUnit = "token"
    val testId = TestIdentifier(urn,label,testUnit)

    assert (testId.label == "Test that all codepoints are valid")
    assert(testId.testUnit == "token" )
  }

  it should "be constructable from CEX source" in {
    val cex = "urn:cite2:hmt:testsuite.2017a:demo1#Test that all codepoints are valid#token"
    val testId = TestIdentifier(cex)
    assert (testId.label == "Test that all codepoints are valid")
    assert(testId.testUnit == "token")
  }

}
