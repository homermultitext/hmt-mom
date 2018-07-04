package edu.holycross.shot.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.dse._
import org.homermultitext.edmodel._
import org.homermultitext.hmtcexbuilder._
import java.text.Normalizer

import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


/**
*/
case class MomReporter(mom: HmtMom) {

  val outputDir = mom.repo.validationDir

  // compute these once:
  val dse = mom.dse
  val tkns = mom.tokens
  val corpus = mom.corpus

  val bifolios = Seq("e3", "venB")

  /** Check if passages in a list are actually in corpus
  * or not, and create a Vector of missing passages.
  *
  * @param txts Passages to check.
   */
  def missingPassages(txts: Vector[CtsUrn]):  Vector[CtsUrn] = {
    val accountedFor = for (psg <- txts) yield {
      //println("...looking for  " + psg)
      val matches = corpus ~~ psg
      if (matches.isEmpty) {
        Some(psg)
      } else {
        None
      }
    }
    accountedFor.flatten
  }


  /** Compose markdown report on automated validation of DSE records.
  *
  * @param pageUrn Page to analyze.
  */
  def dseValidation(pageUrn: Cite2Urn): String = {
    val md = StringBuilder.newBuilder

    md.append(s"# DSE records for ${pageUrn.collection}, page " + pageUrn.objectComponent + "\n\n")
    md.append("## Automated validation\n\nInternal consistency of records:\n\n")

    val imgs = dse.imagesForTbs(pageUrn).toVector
    if (imgs.size != 1) {
      md.append(s"Could not validate DSE records:  page ${pageUrn} indexed to more than one image:\n\n" + imgs.map(i => "-  " + i.toString).mkString("\n") + "\n")

    } else {
      val img = imgs(0)
      val imgTxts = dse.textsForImage(img).toVector
      val  tbsTxts = dse.textsForTbs(pageUrn).toVector
      if (imgTxts.size == tbsTxts.size) {
          md.append(s"- **${imgTxts.size}** text passages are indexed to ${imgs.head.objectComponent}\n")
          md.append(s"-  **${tbsTxts.size}** text passages are indexed to ${pageUrn.objectComponent}\n")
      } else {
        md.append(s"- Error in indexing: ${imgTxts.size} text passages indexed to image ${imgs.head.objectComponent}, but ${tbsTxts.size} passages indexed to page ${pageUrn.objectComponent}\n")
      }

      // check image size...
      val dseImgMessage = dse.imagesForTbs(pageUrn).size match {
        case 1 => "Surface indexed to only one image."
        case i: Int => s"**Errorr**:  page ${pageUrn} indexed to ${i} images."
      }
      md.append("\n\nSelection of image for imaging:\n\n" +  dseImgMessage  + "\n\n")

      val missingPsgs = missingPassages(tbsTxts)
      val dseTextMessage =  if (missingPsgs.isEmpty) {
        "All passages indexed in DSE records appear in text corpus."
      } else {
        "The following passages in DSE records do not appear in the text corpus:\n\n" + missingPsgs.mkString("\n") + "\n\n"
      }
      md.append("\n\nRelation of DSE records to text corpus:\n\n" +  dseTextMessage  + "\n\n")
    }
    md.toString
  }


  /** Compose markdown report to verify completeness of DSE records.
  *
  * @param pg Page to analyze.
  */
  def dseCompleteness(pg: Cite2Urn ): String = {
    val bldr = StringBuilder.newBuilder
    bldr.append("\n\n## Human verification of DSE records\n\n###  Completeness\n\n")
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

  def dseCorrectness = {
    val bldr = StringBuilder.newBuilder
    bldr.append("\n\n### Correctness\n\n")
    bldr.append("To check for **correctness** of indexing, please verify that text transcriptions and images agree:\n\n")

    //bldr.append(passageView(dse, c, pg))
  }


  /** Write suite of markdown reports to validate and
  * verify editorial work on a single page.
  *
  * @param pageUrn
  */
  def validate(pageUrn: String) = {
    try {
      val u = Cite2Urn(pageUrn)
      println("Validating page " + u + "...")
      val dirName = u.collection + "-" + u.objectComponent
      val pageDir = mkdirs(outputDir/dirName)

      //Format main page:
      //summarize results of validation and add links for
      // verification for DSE records
      println("Validating  DSE records...")
      val dseValidMd = dseValidation(u)
      println("Formatting page to check completeness...")
      val dseCompleteMd = dseCompleteness(u)
      val dseReport = pageDir/"dse.md"
      dseReport.overwrite(dseValidMd + dseCompleteMd)


      println("\tsummarize results of text profiling")
      println("print page with bad character results")
      println("print page with bad XML results")
      println("print passage view to check correctness")



    } catch {
      case t: Throwable => {
        println("Could not validate " + pageUrn)
        println("Full error message:\n\t")
        println(t.toString + "\n\n")
      }
    }
  }
}
