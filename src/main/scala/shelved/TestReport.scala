package org.homermultitext.hmtmom

import edu.holycross.shot.cite._

case class TestResult (before: String, after: String, success: Boolean)

case class TestReport
  (testIdentifier: TestIdentifier, results: Vector[TestResult]) {
}
