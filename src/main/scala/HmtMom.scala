package edu.holycross.shot.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.scm._
import org.homermultitext.edmodel._


case class HmtMom(repo: EditorsRepo) {

  /** Create a corpus of XML archival editions.
  */
  def raw:  Corpus = {
    TextRepositorySource.fromFiles(repo.ctsCatalog.toString, repo.ctsCitation.toString, repo.editionsDir.toString).corpus
  }

  /** Create corpus of XML source of Iliad with all nodes renamed to
  * diplomatic version.
  */
  def iliads: Corpus = {
    val iliadXml = raw ~~ CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    val iliadNodes = iliadXml.nodes.map( n => {
      val diplo = n.urn.version.replaceAll("_xml","")
      CitableNode(n.urn.dropVersion.addVersion(diplo) ,n.text)
    })
    Corpus (iliadNodes)
  }


  /** Create corpus of XML source of scholia with all nodes renamed to
  * diplomatic version.
  */
  def scholia : Corpus = {
    val scholiaXml = raw ~~ CtsUrn("urn:cts:greekLit:tlg5026:")
    val noReff = Corpus(scholiaXml.nodes.filterNot(_.urn.toString.contains(".ref")))
    val collapsed = for (i <- 0 until (noReff.size - 1) by 2 ) yield {
      val u = noReff.nodes(i).urn.collapsePassageBy(1)
      val txt = "<div>" + noReff.nodes(i).text + " " + noReff.nodes(i+1).text + "</div>"
      CitableNode(u,txt)
    }
    Corpus(collapsed.toVector.map( n => {
      CitableNode(n.urn.dropVersion.addVersion("hmt") ,n.text)
    }))
  }

  /** Create corpus of XML source of Iliads and scholia.
  */
  def corpus = iliads ++ scholia

  /** Complete tokenization of the corpus. */
  def tokens = TeiReader.fromCorpus(corpus)



}

object HmtMom {

  /** Construct an HmtMom object from the path to an
  * editorial repository.
  *
  * @param repoPath Path to repository.
  */
  def apply(repoPath: String) : HmtMom = {
    HmtMom(EditorsRepo(repoPath))
  }


  /** Count number of tokens occurring for each token type.
  *
  * @param tokens Tokens to profile.
  */
  def profileTokens(tokens: Vector[TokenAnalysis]): Vector[(LexicalCategory, Int, Int)] = {
    val tokenTypes = tokens.map(_.analysis.lexicalCategory).distinct

    val profile = for (ttype <- tokenTypes) yield {
      val typeTokens = tokens.filter(_.analysis.lexicalCategory == ttype)
      (ttype,typeTokens.size,typeTokens.map(_.analysis.readWithAlternate).distinct.size )
    }
    profile
  }

  /** Extract list of unique lexical words from tokens.
  *
  * @param tokens List of tokens to analyze.
  */
  def wordList(tokens: Vector[TokenAnalysis]): Vector[String] = {
    tokens.map(_.analysis.readWithAlternate).distinct
  }

  /** Compute histogram of words for a list of tokens.
  *
  * @param tokens List of tokens to analyze.
  */
  def wordHisto(tokens: Vector[TokenAnalysis]): Vector[StringCount] = {
    val lexTokens = tokens.filter(_.analysis.lexicalCategory == LexicalToken)
    val strs = lexTokens.map(_.analysis.readWithAlternate)
    val grouped = strs.groupBy(w => w).toVector
    val counted =  grouped.map{ case (k,v) => StringCount(k,v.size) }
    counted.sortBy(_.count).reverse
  }
}
