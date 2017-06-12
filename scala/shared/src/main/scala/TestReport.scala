package edu.holycross.shot.hmtmom

import edu.holycross.shot.cite._


import scala.scalajs.js
import js.annotation.JSExport


@JSExport case class TestResult (before: String, after: String, success: Boolean)


@JSExport case class TestReport
  (testIdentifier: TestIdentifier, results: Vector[TestResult]) {
}
