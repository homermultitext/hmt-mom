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
    testCexLines(lines, Vector.empty[PaleographicObservation], Vector.empty[String])
  }


  /**  Create test report for a list of CEX Strings
  * recording paleographic observations.
  *
  * @param lines Lines to examine.
  */
  def testCexLines(lines: Vector[String], good: Vector[PaleographicObservation], bad:  Vector[String] ): TestResults[PaleographicObservation] = {
    if (lines.isEmpty) {
      TestResults(good, bad)

    } else {

      try {
        val cols = lines.head.split("#")
        require(cols.size > 2, "Too few columns in input line: "+ lines.head)
        //Observation URN#Text Reading#Image#Comments
        val reading = CtsUrn(cols(1))
        val img = Cite2Urn(cols(2))
        val observation = PaleographicObservation(reading.passageNodeSubref, img)
        testCexLines(lines.tail, good :+ observation, bad)

      } catch {
        case t : Throwable => {
          val msg = "Failed to parse line " + lines.head + ".  Error message: " + t
          testCexLines(lines.tail, good, bad :+ msg)
        }
      }
    }
  }


  /** Compose markdown report for verifying paleographic
  * observations for a single page.
  *
  * @param pg Page to examine.
  * @param observations Paelographic observations to display.
  * @param baseUrl ICT2 location.
  */
  def pageVerification(pg: Cite2Urn, observations: Vector[PaleographicObservation], baseUrl: String): String = {
    val bldr = StringBuilder.newBuilder
    bldr.append(s"\n\n## Human verification of paelographic observations for ${pg.collection}, page ${pg.objectComponent}\n\n###  Completeness\n\n")

    bldr.append(s"To check for **completeness** of coverage, please review these visualizations of paleographic observations in ICT2:\n\n")


    val rois = observations.map(_.img)
    val link = if (rois.size > 0) {
      baseUrl + "?urn=" + rois.mkString("&urn=")
    } else {
      baseUrl
    }
    bldr.append(s"-  [${pg.objectComponent}](${link})\n\n")


    bldr.append("## Correctness\n\n")
    bldr.append("To check for **correctness** of indexing, please verify that text transcriptions and images agree:\n\n")
    val imgmgr = ImageManager()

    bldr.append("| Image     | Reading     |\n| :------------- | :------------- |\n")
    val rows = for (obs <- observations) yield {
      "| " + imgmgr.markdown(obs.img) + " | " + obs.reading + " | \n"
    }
    bldr.append(rows.mkString("\n") + "\n")
    bldr.toString
  }

  /** Write a markdown file summarizing validation for a page.
  *
  * @param img Reference image for the page.
  * @param pg Page to report on individually.
  * @param rslts Results of paleographic observations.
  */
  def pageReport(img: Cite2Urn, pg: Cite2Urn, rslts:  TestResults[PaleographicObservation]): String = {
    val md = StringBuilder.newBuilder

    md.append(s"# Validation of paleographic observations for "+ pg.collection + ", page " + pg.objectComponent + "\n\n")

    val observations =rslts.good.filter(_.img ~~ img)
    md.append("Total observations on this page: " + observations.size +  "\n")


    md.append("\n## Profile of entire repository\n")
    md.append("Synatically valid observations:  " + rslts.good.size + "\n")
    md.append("Observations with syntax errors:  " + rslts.bad.size + "\n")


    if (rslts.bad.nonEmpty) {
      md.append("\n##Errors\n\n")
      val errList = for (err <- rslts.bad) yield {
        "-  " + err + "\n"
      }
      md.append(errList.mkString)
    }

    md.toString
  }

}
