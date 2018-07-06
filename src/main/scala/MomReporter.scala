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


/**
*/
case class MomReporter(mom: HmtMom) {

  val outputDir = mom.repo.validationDir

  // compute these once:
  val dse = mom.dse
  val corpus = mom.corpus

  val bifolios = Seq("e3", "venB")


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
      val pageDir = outputDir/dirName
      if (pageDir.exists) {
        pageDir.delete()
      }
      mkdirs(pageDir)

      val pageCorpus = corpusForPage(u,dse)
      val pageTokens = TeiReader.fromCorpus(pageCorpus)

      val home = StringBuilder.newBuilder
      home.append(s"# Review of ${u.collection}, page ${u.objectComponent}\n\n")
      home.append("## Summary of automated validation\n\n")

      // DSE valdiation reporting:
      println("Validating  DSE records...")
      val dseReporter =  DseReporter(u, dse, pageCorpus)
      val dseValidMd = dseReporter.dseValidation
      val dseErrors = dseValidMd.contains("## Errors")
      val dseReport = pageDir/"dse-validation.md"
      dseReport.overwrite(dseValidMd)


      if (dseErrors) {
        home.append("-  ![errors](https://raw.githubusercontent.com/wiki/neelsmith/tabulae/images/no.png) DSE: there were errors.  ")

      } else {
        home.append("-  ![errors](https://raw.githubusercontent.com/wiki/neelsmith/tabulae/images/yes.png) DSE: there were no errors. \n")
      }
      home.append("See [details in dse-validation.md](./dse-validation.md)\n")


      // Text validation reporting
      val errHeader = "Token#Reading#Error\n"


      val badChars = HmtMom.badCharTokens(pageTokens)
      if (badChars.isEmpty) {
          home.append("-  ![errors](https://raw.githubusercontent.com/wiki/neelsmith/tabulae/images/yes.png) Character set in editions: there were no errors.\n")
      } else {
        home.append("-  ![errors](https://raw.githubusercontent.com/wiki/neelsmith/tabulae/images/no.png) Character set in editions: there were errors.  ")

        val badCharFile = pageDir/"badCharacters.cex"
        badCharFile.overwrite(HmtMom.badCharTable(badChars))
        home.append("See [details in badCharacters.cex](./badCharacters.cex)\n")
      }

      val badXml = HmtMom.badMarkup(pageTokens).map(_.analysis.errorReport("#"))
      if (badXml.isEmpty) {
          home.append("-  ![errors](https://raw.githubusercontent.com/wiki/neelsmith/tabulae/images/yes.png) XML markup: there were no errors.\n")
      } else {
          home.append("-  ![errors](https://raw.githubusercontent.com/wiki/neelsmith/tabulae/images/no.png) XML markup: there were errors.  ")
          val badXmlFile = pageDir/"badXML.cex"

          badXmlFile.overwrite(errHeader + badXml.mkString("\n"))
          home.append("See [details in badXML.cex](./badXML.cex)\n")
      }


      home.append("-  Named entity identifications.  **TBA**\n")
      home.append("-  Indexing scholia markers.  **TBA**\n")

      home.append("\n\n## Visualizations to review for verification\n\n")

      val dseCompleteMd = dseReporter.dseCompleteness
      val dseCorrectMd = dseReporter.dseCorrectness
      val dseVerify = pageDir/"dse-verification.md"
      val dsePassageMd =
      dseVerify.overwrite(dseCompleteMd + dseCorrectMd)

      home.append("- Completeness of DSE indexing:  see [dse-verification.md](./dse-verification.md)\n")



      // OV of text contents
      home.append("\n## Overview of page's text contents\n\n")
      val wordList = pageDir/"wordlist.txt"
      wordList.overwrite(HmtMom.wordList(pageTokens).mkString("\n"))
      val wordHisto = pageDir/"wordFrequencies.cex"
      wordHisto.overwrite("Word#Frequency\n" + HmtMom.wordHisto(pageTokens).map(_.cex).mkString("\n"))
      val wordIndex = pageDir/"wordIndex.cex"
      val wordHeader = "Character#Codepoint#Frequency\n"


      wordIndex.overwrite(wordHeader + HmtMom.tokenIndex(pageTokens).mkString("\n"))
      val charHisto = pageDir/"characterFrequencies.cex"
      charHisto.overwrite(HmtMom.cpTable(pageTokens))

      val tProf = HmtMom.profileTokens(pageTokens)
      val tokensProfile = for (prof <- tProf) yield {
        "- " + prof._1 + ": " + prof._2 + " tokens. " + prof._3 + " distinct tokens."
      }
      home.append(s"**${pageTokens.size}** analyzed tokens in **${pageCorpus.size}** citable units of text.\n\nDistribution of token types:\n\n")
      home.append(tokensProfile.mkString("\n") + "\n")

      home.append("\nWord data:\n\n")

      home.append("-  frequencies:  see [wordFrequencies.cex](./wordFrequencies.cex)\n")
      home.append("-  index of all occurrences:  see [wordIndex.cex](./wordIndex.cex)\n")
      home.append("-  list of unique words (suitable for morphological analysis):  see [wordlist.txt](./wordlist.txt)\n")


      home.append("\nCharacter data:\n\n")
      home.append("-  frequencies:  see [characterFrequencies.cex](./characterFrequencies.cex)\n")




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
