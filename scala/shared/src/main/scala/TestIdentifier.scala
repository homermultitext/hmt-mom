package edu.holycross.shot.hmtmom

import edu.holycross.shot.cite._


import scala.scalajs.js
import js.annotation.JSExport


@JSExport case class TestIdentifier(urn: Cite2Urn, label: String, testUnit: String)



object TestIdentifier {


  /** Construct [[TestIdentifier]] object from one line of delimited text.
  *
  * @param cexLine A single line of delimited text.
  * @param delimiter String separating columns of delimited text.
  */
  def apply(cexLine: String, delimiter: String = "#"): TestIdentifier = {
    val columns = cexLine.split(delimiter)
    val urn = try {
      Cite2Urn(columns(0))
    } catch {
      case t: Throwable => throw t
    }
    TestIdentifier(urn, columns(1), columns(2))
  }
}
