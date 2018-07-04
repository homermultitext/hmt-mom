package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._


/**
*/
class TestReportSpec extends FlatSpec {


  "A TestResult" should "have before, after, and success properties" in  {
    val before = "token"
    val after = "valid"
    val success = true
    val testResult = TestResult(before,after,success)
    assert (testResult.before == "token")
    assert (testResult.after == "valid")
    assert(testResult.success)
  }


}
