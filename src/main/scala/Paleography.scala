package org.homermultitext.hmtmom


import edu.holycross.shot.cite._

/** A paleographic observation of a single glyph.
*
* @param reading Reading of text of a single glyph.
* @param img: Visual evidence for the reading (image including region of interest).
*/
case class PaleographicObservation(reading: String, img: Cite2Urn) {

  /** Format pair for CEX. */
  def cex :  String = {
    reading + "#" + img
  }
}

object PaleographyResults {

  /** Compute test results for a paleographic data set in CEX format.
  *
  * @param cex CEX text, including a header line, with paleographic
  * observations.
  */
  def apply (cex: String): TestResults[PaleographicObservation] = {
    def lines = cex.split("\n").toVector
    testCexLines(lines.tail, Vector.empty[PaleographicObservation], Vector.empty[String])
  }



  def testCexLines(lines: Vector[String], good: Vector[PaleographicObservation], bad:  Vector[String] ): TestResults[PaleographicObservation] = {
    if (lines.isEmpty) {
      TestResults(good, bad)

    } else {

      try {
        val cols = lines.head.split("#")
        require(cols.size > 2, "Too few columns in input line: "+ lines.head)
        //Text URN#Reading#Image#Comments
        val txt = CtsUrn(cols(0))
        val reading = cols(1)
        val img = Cite2Urn(cols(2))
        val observation = PaleographicObservation(reading, img)
        testCexLines(lines.tail, good :+ observation, bad)

      } catch {
        case t : Throwable => {
          val msg = "Failed to parse line " + lines.head + ".  Error message: " + t
          testCexLines(lines.tail, good, bad :+ msg)
        }
      }
    }
  }

}
