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

  /** Check if passages in a list are actually in corpus
  * or not, and create a Vector of missing passages.
  *
  * @param txts Passages to check.
   */
  def missingPassages(psgs: Vector[CtsUrn]):  Vector[CtsUrn] = {
    val accountedFor = for (psg <- psgs) yield {
      val matches = txts ~~ psg
      if (matches.isEmpty) {
        Some(psg)
      } else {
        None
      }
    }
    accountedFor.flatten
  }


  /** Compose markdown report on automated validation of DSE records.
  */
  def dseValidation: String = {
    val md = StringBuilder.newBuilder
    val errors = StringBuilder.newBuilder

    md.append(s"# Validation of DSE records for ${pg.collection}, page " + pg.objectComponent + "\n\n")
    if (dse.size == 0) {
      md.append("### Errors\n\nNo DSE records found!\n\n")
    } else {
      val imgs = dse.imagesForTbs(pg).toVector
      if (imgs.size != 1) {
        errors.append(s"### Errors\n\nCould not validate DSE records:  page ${pg} indexed to more than one image:\n\n" + imgs.map(i => "-  " + i.toString).mkString("\n") + "\n")

      } else {
        md.append("## Internal consistency of records\n\n")
        val img = imgs(0)
        val imgTxts = dse.textsForImage(img).toVector
        val  tbsTxts = dse.textsForTbs(pg).toVector
        if (imgTxts.size == tbsTxts.size) {
            md.append(s"- **${imgTxts.size}** text passages are indexed to ${imgs.head.objectComponent}\n")
            md.append(s"-  **${tbsTxts.size}** text passages are indexed to ${pg.objectComponent}\n")
        } else {
          md.append(s"- Error in indexing: ${imgTxts.size} text passages indexed to image ${imgs.head.objectComponent}, but ${tbsTxts.size} passages indexed to page ${pg.objectComponent}\n")
          if (errors.isEmpty) {
            errors.append("## Errors\n\n")
          }
          errors.append("There were inconsistencies in indexing. (See details above.)\n\n")
        }

        // check image size...
        val dseImgMessage = dse.imagesForTbs(pg).size match {
          case 1 => "Surface **correctly** indexed to only one image."
          case i: Int => {
            if (errors.isEmpty) {
              errors.append("## Errors\n\n")
            }
            errors.append(
            s"There were errors in indexing:  page ${pg} indexed to ${i} images.\n\n")
            s"**Error**:  page ${pg} indexed to ${i} images."
          }
        }
        md.append("\n\n## Selection of image for imaging\n\n" +  dseImgMessage  + "\n\n")

        val missingPsgs = missingPassages(tbsTxts)
        val dseTextMessage =  if (missingPsgs.isEmpty) {
          "**All** passages indexed in DSE records appear in text corpus."
        } else {
          errors.append("There were errors citing texts. \n\n")
          errors.append("The following passages in DSE records do not appear in the text corpus:\n\n" + missingPsgs.map("-  " + _ ).mkString("\n") + "\n\n")

        }
        md.append("\n\n## Relation of DSE records to text corpus\n\n" +  dseTextMessage  + "\n\n" + errors.toString + "\n\n")
      }
    }

    md.toString
  }

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
