package org.homermultitext.hmtmom

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
      val matches = corpus ~~ psg
      if (matches.isEmpty) {
        Some(psg)
      } else {
        None
      }
    }
    accountedFor.flatten
  }


  /** Select a corpus by page reference.
  *
  * @param pg Page to select texts for.
  * @param dse DseVector to consult for records of
  * texts on page.
  */
  def corpusForPage(pg: Cite2Urn, dseV: DseVector) = {
    val textUrns = dseV.textsForTbs(pg).toVector
    val miniCorpora = for (u <- textUrns) yield {
      corpus ~~ u
    }
    HmtMom.mergeCorpusVector(miniCorpora, Corpus(Vector.empty[CitableNode]))
  }
/*
  def diplomaticForCorpus(c: Corpus): Corpus = {
    TeiReader(pageCorpus.cex("#"), "#").tokens

TeiReader(pageCorpus.cex("#"), "#").tokens.map(_.analysis.readWithDiplomatic).mkString(" ")

    Corpus(Vector.empty)
  }
  */

  /** Compose markdown report on automated validation of DSE records.
  *
  * @param pageUrn Page to analyze.
  */
  def dseValidation(pageUrn: Cite2Urn): String = {
    val md = StringBuilder.newBuilder
    val errors = StringBuilder.newBuilder

    md.append(s"# Validation of DSE records for ${pageUrn.collection}, page " + pageUrn.objectComponent + "\n\n")


    val imgs = dse.imagesForTbs(pageUrn).toVector
    if (imgs.size != 1) {
      errors.append(s"### Errors\n\nCould not validate DSE records:  page ${pageUrn} indexed to more than one image:\n\n" + imgs.map(i => "-  " + i.toString).mkString("\n") + "\n")

    } else {
      md.append("## Internal consistency of records\n\n")
      val img = imgs(0)
      val imgTxts = dse.textsForImage(img).toVector
      val  tbsTxts = dse.textsForTbs(pageUrn).toVector
      if (imgTxts.size == tbsTxts.size) {
          md.append(s"- **${imgTxts.size}** text passages are indexed to ${imgs.head.objectComponent}\n")
          md.append(s"-  **${tbsTxts.size}** text passages are indexed to ${pageUrn.objectComponent}\n")
      } else {
        md.append(s"- Error in indexing: ${imgTxts.size} text passages indexed to image ${imgs.head.objectComponent}, but ${tbsTxts.size} passages indexed to page ${pageUrn.objectComponent}\n")
        if (errors.isEmpty) {
          errors.append("## Errors\n\n")
        }
        errors.append("There were inconsistencies in indexing. (See details above.)\n\n")
      }

      // check image size...
      val dseImgMessage = dse.imagesForTbs(pageUrn).size match {
        case 1 => "Surface **correctly** indexed to only one image."
        case i: Int => {
          if (errors.isEmpty) {
            errors.append("## Errors\n\n")
          }
          errors.append(
          s"There were errors in indexing:  page ${pageUrn} indexed to ${i} images.\n\n")
          s"**Error**:  page ${pageUrn} indexed to ${i} images."
        }
      }
      md.append("\n\n## Selection of image for imaging\n\n" +  dseImgMessage  + "\n\n")

      val missingPsgs = missingPassages(tbsTxts)
      val dseTextMessage =  if (missingPsgs.isEmpty) {
        "**All** passages indexed in DSE records appear in text corpus."
      } else {
        "The following passages in DSE records do not appear in the text corpus:\n\n" + missingPsgs.mkString("\n") + "\n\n"
        errors.append("There were errors citing texts.  (See details above). \n\n")
      }
      md.append("\n\n## Relation of DSE records to text corpus\n\n" +  dseTextMessage  + "\n\n" + errors.toString + "\n\n")
    }
    md.toString
  }


  /** Compose markdown report to verify completeness of DSE records.
  *
  * @param pg Page to analyze.
  */
  def dseCompleteness(pg: Cite2Urn ): String = {
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


  // need texts for page
  // need image for each text
  def passageView(surface: Cite2Urn, pageCorpus : Corpus) : String = {
    val viewMd = StringBuilder.newBuilder
  //TeiReader(pageCorpus.cex("#"), "#").tokens
  //TeiReader(pageCorpus.cex("#"), "#").tokens.map(_.analysis.readWithDiplomatic).mkString(" ")

    val rows = for (psg <- pageCorpus.nodes) yield {
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
  def dseCorrectness(pg: Cite2Urn, txts: Corpus):  String = {
    val bldr = StringBuilder.newBuilder
    bldr.append("\n\n### Correctness\n\n")
    bldr.append("To check for **correctness** of indexing, please verify that text transcriptions and images agree:\n\n")

    bldr.append(passageView(pg, txts))
    bldr.toString
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

      val home = StringBuilder.newBuilder
      home.append(s"# Review of ${u.collection}, page ${u.objectComponent}\n\n")
      home.append("## Summary of automated validation\n\n")


      // DSE valdiation reporting:
      println("Validating  DSE records...")
      val dseValidMd = dseValidation(u)
      val dseErrors = dseValidMd.contains("## Errors")

      val dseReport = pageDir/"dse-validation.md"
      dseReport.overwrite(dseValidMd)
      println("print passage view to check correctness")


      // Text validation reporting
      val pageCorpus = corpusForPage(u,dse)
      println("print page with bad character results")
      println("print page with bad XML results")


      if (dseErrors) {
        home.append("-  ![errors](https://raw.githubusercontent.com/wiki/neelsmith/tabulae/images/no.png) DSE: there were errors.  ")

      } else {
        home.append("-  ![errors](https://raw.githubusercontent.com/wiki/neelsmith/tabulae/images/yes.png) DSE: there were no errors.  ")
      }
      home.append("See [details in dse-validation.md](./dse-validation.md)\n")


      home.append("-  Character set in editions.  **TBA**\n")
      home.append("-  XML markup in editions.  **TBA**\n")
      home.append("-  Named entity identifications.  **TBA**\n")
      home.append("-  Indexing scholia markers.  **TBA**\n")



      home.append("## Visualizations to review for verification\n\n")
      val dseCompleteMd = dseCompleteness(u)
      val dseCorrectMd = dseCorrectness(u, pageCorpus)
      val dseVerify = pageDir/"dse-verification.md"
      val dsePassageMd =
      dseVerify.overwrite(dseCompleteMd + dseCorrectMd)

      home.append("- [Completeness of DSE indexing](./dse-verification.md)\n")



      // OV of text contents
      home.append("\n## Overview of page's text contents\n\n")
      val pageTokens = TeiReader.fromCorpus(pageCorpus)
      val tProf = HmtMom.profileTokens(pageTokens)
      val tokensProfile = for (prof <- tProf) yield {
        "- " + prof._1 + ": " + prof._2 + " tokens. " + prof._3 + " distinct tokens."
      }
      home.append(s"**${pageTokens.size}** analyzed tokens in **${pageCorpus.size}** citable units of text.\n\nDistribution of token types:\n\n")
      home.append(tokensProfile.mkString("\n") + "\n")

      val index = pageDir/"summary.md"
      index.overwrite(home.toString)


    } catch {
      case t: Throwable => {
        println("Could not validate " + pageUrn)
        println("Full error message:\n\t")
        println(t.toString + "\n\n")
      }
    }
  }
}
