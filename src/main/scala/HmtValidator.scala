package org.homermultitext.hmtmom

import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.dse._
import org.homermultitext.edmodel._


/** A class for validating and analyzing a CiteLibrary with HMT project content.
*/
case class HmtValidator(library: CiteLibrary) {

}

object HmtValidator {

  /** Instantiate an [[HmtValidator]] from the CEX
  * serialiation of an HMT CiteLibrary.
  */
  def apply(cex: String) : HmtValidator = {
    HmtValidator(CiteLibrary(cex))
  }




    /** Count number of tokens occurring for each token type.
    *
    * @param tokens Tokens to profile.

    def profileTokens(tokens: Vector[TokenAnalysis]): Vector[(LexicalCategory, Int, Int)] = {
      val tokenTypes = tokens.map(_.analysis.lexicalCategory).distinct

      val profile = for (ttype <- tokenTypes) yield {
        val typeTokens = tokens.filter(_.analysis.lexicalCategory == ttype)
        (ttype,typeTokens.size,typeTokens.map(_.analysis.readWithAlternate).distinct.size )
      }
      profile
    }
*/
    /** Compute index of tokens to passage where they occur.
    *
    * @param tokens Tokens to index.

    def tokenIndex(tokens: Vector[TokenAnalysis]) : Vector[String] = {
      def grouped = stringSeq(tokens).groupBy ( occ => occ.something).toVector
      val idx = for (grp <- grouped) yield {
        val str = grp._1
        val occurrences = grp._2.map(_.urn)
        val flatList = for (occurrence <- occurrences) yield {
          str + "#" + occurrence.toString
        }
        flatList
      }
      idx.flatten
    }
*/
    /** Extract list of unique lexical words from tokens.
    *
    * @param tokens List of tokens to analyze.

    def wordList(tokens: Vector[TokenAnalysis]): Vector[String] = {
      tokens.map(_.analysis.readWithAlternate).distinct
    }
*/
    /** Compute histogram of words for a list of tokens.
    *
    * @param tokens List of tokens to analyze.

    def wordHisto(tokens: Vector[TokenAnalysis]): Vector[StringCount] = {
      val lexTokens = tokens.filter(_.analysis.lexicalCategory == LexicalToken)
      val strs = lexTokens.map(_.analysis.readWithAlternate)
      val grouped = strs.groupBy(w => w).toVector
      val counted =  grouped.map{ case (k,v) => StringCount(k,v.size) }
      counted.sortBy(_.count).reverse
    }
*/
    /** Compute sequence of occurrences for a list of tokens.
    *
    * @param tokens Tokens to analyze

    def stringSeq(tokens: Vector[TokenAnalysis]): Vector[Occurrence[String]] = {
       tokens.map( tkn => {
          Occurrence(tkn.analysis.editionUrn, tkn.analysis.readWithAlternate)
        }
      )
    }
*/

    /** Compute histogram of codepoints for a list of tokens
    * sorted from most frequent to least frequent.
    *
    * @param tokens List of tokens to analyze.

    def cpHisto(tokens: Vector[TokenAnalysis]):  Vector[(Int,Int)] = {
      def cps = hmtCpsFromTokens(tokens)
      val grouped = cps.groupBy(cp => cp).toVector
      val counted =  grouped.map{ case (k,v) => (k,v.size) }
      counted.sortBy(_._2).reverse
    }

    def cpTable(tokens: Vector[TokenAnalysis]): String = {
      cpTableFromHisto(cpHisto(tokens))
    }
    def cpTableFromHisto(histo: Vector[(Int,Int)]): String = {
      val rows = for ((cp,freq) <- histo) yield {
        val s = HmtChars.cpsToString(Vector(cp))
        s"${s}#x${cp.toHexString}#${freq}"
      }
      "Character#CodePoint#Frequency\n" + rows.mkString("\n")
    }
*/
    /** Compose text for a token analysis according to
    * HMT project normalization.
    *
    * @param tkn Token analysis.

    def hmtText(tkn: TokenAnalysis): String = {
      val rdgs = tkn.analysis.readings.map(_.text).mkString
      //get alt reading string
      val alts = if (tkn.hasAlternate) {
        tkn.analysis.alternateReading.get.simpleString
      } else { ""}
      val str = (rdgs + alts).replaceAll("\\s","")
      //option: HmtChars.hmtNormalize(str)
      str
    }
*/
    /** Compute list of codepoints from a list of tokens.
    *
    * @param tokens Tokens to analyze.

    def hmtCpsFromTokens(tokens: Vector[TokenAnalysis]):  Vector[Int] = {
      val snormal = tokens.map(hmtText(_))
      HmtChars.stringToCps(snormal.mkString)
    }
*/
    /** Determine if all codepoints in a list are valid.
    *
    * @param cps List of codepoints.

    def validCPs(cps: Vector[Int]): Boolean = {
      val checkOk =  cps.map(HmtChars.legal(_)).distinct
      // all tokens were valid of only distinct value is true:
      if ((checkOk.size == 1)  && (checkOk(0))) {
        true
      } else {
        false
      }
    }
  */
    /** Filter a token list for tokens containing one or more
    * invalid characters.
    *
    * @param tokens List of tokens.

    def badCharTokens(tokens: Vector[TokenAnalysis]): Vector[TokenAnalysis] = {
      tokens.filterNot(t => {
        val txt = hmtText(t)
        HmtChars.legalChars(txt)
      })
    }

    def badCharTable(tokens: Vector[TokenAnalysis]):  String ={
      val rows = for (t <- tokens) yield {
        val allText = t.analysis.readWithDiplomatic + t.analysis.readWithAlternate
        val badChars= HmtChars.badCPs(allText).map( c =>
          s"${HmtChars.cpsToString(Vector(c))} (x${c.toHexString})"
        )
        t.analysis.editionUrn + "#" + allText + "#" + badChars.mkString(", ")
      }
      "Passage#All readings#Errors\n" + rows.mkString("\n")
    }
  */


    /** Filter a token list for tokens containing one or more
    * markup errors.
    *
    * @param tokens List of tokens.

    def badMarkup(tokens: Vector[TokenAnalysis]): Vector[TokenAnalysis] = {
      val tokenOpts = for (t <- tokens) yield {
        if (t.analysis.errors.nonEmpty) {
          Some(t)
        } else {
          None
        }
      }
      tokenOpts.flatten
    }*/

    /** Recursively merge  a list of corpora into a single corpus.
    *
    * @param v List of corpora to merge.
    * @param composite Composite corpus compiled so far.

    def mergeCorpusVector(v: Vector[Corpus], composite: Corpus):  Corpus = {
      if (v.isEmpty) {
        composite
      } else {
        val nextCorpus = composite ++ v.head
        mergeCorpusVector(v.tail, nextCorpus)
      }
    }*/
}
