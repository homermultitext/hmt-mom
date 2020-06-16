package org.homermultitext.hmtmom



import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.scm._
import edu.holycross.shot.cex._
import edu.holycross.shot.dse._
import edu.holycross.shot.citerelation._

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.mid.orthography._
import edu.holycross.shot.mid.markupreader._
import edu.holycross.shot.citevalidator._

import scala.xml._

import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


/** Mandatory Ongoing Maintenance for HMT project editorial repositories.
*
* @param baseDir Name of Root directory of an HMT editorial repository
* laid out according to 2018 standards..
*/
case class HmtMom(baseDir: String)  {




  //val midRepo = EditorsRepo(baseDir)

  //val dse = midValidator.dse




  /** Create new corpus by extracting all scholia from a
  * given corpus.
  *
  * @param c Corpus to extact scholia from.
  */
  def scholia(c: Corpus) : Corpus = {
    val scholiaUrn = CtsUrn("urn:cts:greekLit:tlg5026:")
    c ~~ scholiaUrn
  }

  /** Create new corpus by extracting all Iliad passages from a
  * given corpus.
  *
  * @param c Corpus to extact scholia from.
  */
  def iliads(c: Corpus): Corpus = {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    c ~~ iliadUrn
  }

  /** From a corpus citing scholia at leaf node (lemma, ref, comment),
  * drop all "ref" elements to create a new leaf-node corpus containing
  * only text content of the edition.
  *
  * @param scholia Corpus of scholia cited at leaf node.
  */
  def scholiaTextCorpus (c: Corpus): Corpus = {
    val scholiaNodes = c.nodes.filter(_.urn ~~ CtsUrn("urn:cts:greekLit:tlg5026:"))
    Corpus(scholiaNodes.filterNot(_.urn.toString.endsWith(".ref")))
  }

  /** Given a corpus of scholia in HMT archival XML cited at leaf node,
  * create a corpus of well-formed XML texts cited at the level of
  * the whole scholion.  The function first drops any ".ref" nodes,
  * then combines any remaining lemma and comment nodes in a well-formed
  * XML block with URN collapsed by 1 level.
  *
  * @param scholiaXml Corpus of scholia in archival XML cited at leaf node.
  */
  def scholiaCollapsedXml(scholiaXml: Corpus) = {
    val textOnly = scholiaTextCorpus(scholiaXml)
    // There should be 2 citable XML nodes per scholion  in the sequence
    // lemma, comment, so we walk through the nodes 2 at a time.
    val collapsedNodes = for (i <- 0 until (textOnly.size - 1) by 2 ) yield {
      val u = textOnly.nodes(i).urn.collapsePassageBy(1)
      val txt = "<div>" + textOnly.nodes(i).text + " " + textOnly.nodes(i+1).text + "</div>"
      CitableNode(u,txt)
    }
    Corpus(collapsedNodes.toVector)
  }


  /** Given a corpus of scholia in HMT cited at leaf node,
  * create a corpus of well-formed XML texts cited at the level of
  * the whole scholion.  The function first drops any ".ref" nodes,
  * then combines any remaining lemma and comment nodes in text string
  * with URN collapsed by 1 level.
  *
  * @param scholiaXml Corpus of scholia in archival XML cited at leaf node.
  */
  def scholiaCollapsedText(scholiaXml: Corpus) = {
    val textOnly = scholiaTextCorpus(scholiaXml)
    // There should now be 2 citable XML nodes per scholion  in the sequence
    // lemma, comment, so we walk through the nodes 3 at a time.
    val collapsedNodes = for (i <- 0 until (textOnly.size - 1) by 2 ) yield {
      val u = textOnly.nodes(i).urn.collapsePassageBy(1)
      val txt = textOnly.nodes(i).text + " " + textOnly.nodes(i+1).text
      CitableNode(u,txt)
    }
    Corpus(collapsedNodes.toVector)
  }



  /** Given a corpus of scholia in HMT archival XML cited at leaf node,
  * create CiteTriples.
  *
  * @param scholiaXml Corpus of scholia in archival XML cited at leaf node.
  */
  def scholiaComments(scholiaXml: Corpus)  : Vector[Option[CiteTriple]]= {
    val verb = Cite2Urn("urn:cite2:cite:verbs.v1:commentsOn")
    val scholiaNodes = scholiaXml.nodes
    // There should be 3 citable XML nodes per scholion  in the sequence
    // lemma, ref, comment, so we walk through the nodes 3 at a time.
    val commentRelations = for (i <- 0 until (scholiaXml.size - 2) by 3) yield {
      val xref = XML.loadString(scholiaNodes(i+1).text)
      val nList = xref.attribute("n").get
      val scholionUrn = scholiaNodes(i).urn.collapsePassageBy(1)
      val iliadUrnString = xref.text.trim
      try {
        val iliadUrn = CtsUrn(iliadUrnString)
        Some(CiteTriple(scholionUrn, verb, iliadUrn))
      } catch {
        case t: Throwable => {
          println(s"Failed to create CiteTriple for scholion ${scholionUrn}.  Iliad Urn string was '" + iliadUrnString +  "'" + "\n\t===>" + t)
          None
        }
      }
    }
    commentRelations.toVector
  }



// THIS IS MESSED UP
  def commentsCex(corpus: Corpus): String = {
    val scholiaNodes = corpus.nodes.filter(_.urn ~~ CtsUrn("urn:cts:greekLit:tlg5026:"))

    val comments = scholiaComments(Corpus(scholiaNodes)).flatten.toSet
    CiteRelationSet(comments).cex()
  }


  /** Construct mappings, as configurred for this repository, of CtsUrns to
  * classes implementing the MidMarkupReader trait.
  */
  def readerMappings :  Vector[ReadersPairing]= {
    val readersConf = File(baseDir + "/editions/readers.csv")
    val readersList = readersConf.lines.tail
    val pairs = for (row <- readersList) yield {
      val parts = row.split(",").toVector
      ReadersPairing(CtsUrn(parts(0)), HmtValidator.readersForString(parts(1)))
    }
    pairs.toVector
    //Vector.empty[ReadersPairing]
  }


  /** Construct mappings, as configurred for this repository, of CtsUrns to
  * classes implementing the MidOrthography trait.
  */
  def orthoMappings : Vector[OrthoPairing] = {
    val orthoConf = File(baseDir + "/editions/orthographies.csv")
    val orthoList = orthoConf.lines.tail
    val pairs = for (row <- orthoList) yield {
      val parts = row.split(",").toVector
      OrthoPairing(CtsUrn(parts(0)), HmtValidator.orthoForString(parts(1)))
    }
    pairs.toVector
  }

  /** HCMID project validator to check on layout or repository.*/
  //val midValidator = Validator(midRepo, readerMappings, orthoMappings)

  def validate(uString : String) = {
    println("Validating DSE consistency:")
    //val dse = midValidator.dse
    //val reporter = ValidationReporter(midValidator)
    //reporter.validate(uString)
    //val hmtValidator = HmtValidator(library)
  }


}
