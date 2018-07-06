package org.homermultitext.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.dse._
import org.homermultitext.edmodel._
import org.homermultitext.hmtcexbuilder._


import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


/** An object for analyzing and writing reports about the DSE
* relations for material on a single surface (page).
*
* @param pg Page to analyze.
* @param dse DSE records for the whole repository.
* @param txts Corpus composed only of texts on this page.
*/
case class DseReporter(pg:  Cite2Urn, dse: DseVector, txts: Corpus) {



  /**  Compose markdown content juxtaposing indexed image with
  * transcribed text content for a specific page.
  */
  def passageView : String = {
    val viewMd = StringBuilder.newBuilder
    val rows = for (psg <- txts.nodes) yield {
      val img = dse.imagesWRoiForText(psg.urn).head
      val imgmgr = ImageManager()
      val md = imgmgr.markdown(img, 1000)

      val dipl = TeiReader(psg.cex("#")).tokens.map(_.analysis.readWithDiplomatic).mkString(" ")

      dipl + " (*" + psg.urn + "*)" + "  " + md
    }
    rows.mkString("\n\n\n")
  }

  /** Compose markdown report to verify correctness of DSE records.
  *
  * @param pg Page to analyze.
  */
  def dseCorrectness:  String = {
    val bldr = StringBuilder.newBuilder
    bldr.append("\n\n### Correctness\n\n")
    bldr.append("To check for **correctness** of indexing, please verify that text transcriptions and images agree:\n\n")

    bldr.append(passageView)
    bldr.toString
  }



  /** Compose markdown report to verify completeness of DSE records.
  *
  * @param pg Page to analyze.
  */
  def dseCompleteness: String = {
    val bldr = StringBuilder.newBuilder
    bldr.append(s"\n\n## Human verification of DSE records for ${pg.collection}, page ${pg.objectComponent}\n\n###  Completeness\n\n")
    bldr.append(s"To check for **completeness** of coverage, please review these visualizations of DSE relations in ICT2:\n\n")
    bldr.append(s"- [**all** DSE relations of page ${pg.objectComponent} ](${dse.ictForSurface(pg)}).\n\n")

    bldr
    .append("Visualizations for individual documents:\n\n")
    val texts =  dse.textsForTbs(pg).map(_.dropPassage).toVector
    val listItems = for (txt <- texts) yield {
      println("Create view for " + txt + " ...")
      val oneDocDse = DseVector(dse.passages.filter(_.passage ~~ txt))
      "-  all [passages in " + txt + "](" + oneDocDse.ictForSurface(pg) + ")."
    }
    bldr.append(listItems.mkString("\n") + "\n")
    bldr.toString
  }
}
