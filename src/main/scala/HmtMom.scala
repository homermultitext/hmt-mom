package org.homermultitext.hmtmom



import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.scm._
import edu.holycross.shot.cex._
import edu.holycross.shot.dse._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.mid.validator._

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


  /** Build an HMT library from the library. */
  def library: CiteLibrary = {
    // required components:
    // text repo, dse,
    // relations dervied from cross-references in scholia editions
    CiteLibrary(libHeader + dseCex + textsCex + commentsCex)
  }

  /** Construct DseVector for this repository's records. */
  def dse:  DseVector = {
    val records = dseCex.split("\n").filter(_.nonEmpty).toVector

    // This value must agree with header data in header/1.dse-prolog.cex.
    val baseUrn = "urn:cite2:validate:tempDse.temp:"
    val dseRecords = for ((record, count) <- records.zipWithIndex) yield {
      s"${baseUrn}validate_${count}#Temporary DSE record ${count}#${record}"
    }

    if (records.isEmpty) {
      DseVector(Vector.empty[DsePassage])
    } else {
      val srcAll = libHeader + dseRecords.mkString("\n")
      DseVector(srcAll)
    }
  }

  /** Construct TextRepository. */
  def texts : TextRepository = {
    TextRepositorySource.fromFiles(ctsCatalog.toString, ctsCitation.toString, editionsDir.toString)
  }


  /** Create new corpus by extracting all scholia from a
  * given corpus.
  *
  * @param c Corpus to extact scholia from.
  */
  def scholia(c: Corpus = texts.corpus) : Corpus = {
    val scholiaUrn = CtsUrn("urn:cts:greekLit:tlg5026:")
    c ~~ scholiaUrn
  }

  /** Create new corpus by extracting all Iliad passages from a
  * given corpus.
  *
  * @param c Corpus to extact scholia from.
  */
  def iliads(c: Corpus = texts.corpus): Corpus = {
    val iliadUrn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    c ~~ iliadUrn
  }

  /** From a corpus citing scholia at leaf node (lemma, ref, comment),
  * drop all "ref" elements to create a new leaf-node corpus containing
  * only text content of the edition.
  *
  * @param scholia Corpus of scholia cited at leaf node.
  */
  def scholiaTextCorpus (scholia: Corpus): Corpus = {
    Corpus(scholia.nodes.filterNot(_.urn.toString.endsWith(".ref")))
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

  /** CEX library header data.*/
  def libHeader:  String = DataCollector.compositeFiles(libHeadersDir.toString, "cex")

  /** CEX data for DSE relations.*/
  def dseCex:  String = {
    val triplesCex = DataCollector.compositeFiles(dseDir.toString, "cex", dropLines = 1)
    val tempCollection = Cite2Urn("urn:cite2:validate:tempDse.temp:")
    val dseV = DseVector.fromTextTriples(triplesCex, tempCollection)
    //dseV.cex
    val rows = dseV.passages.map(_.cex())
    rows.mkString("\n")
  }

  /** CEX data for text editions.*/
  def textsCex: String = {
    texts.cex()
  }

  def commentsCex: String = {
    val scholia = scholiaTextCorpus(texts.corpus)
    val comments = scholiaComments(scholia).flatten.toSet
    CiteRelationSet(comments).cex()
  }


  val dseDir = File(baseDir + "/dse")
  val validationDir = File(baseDir + "/validation")
  val editionsDir = File(baseDir + "/editions")
  val paleographyDir = File(baseDir + "/paleography") // allow this to be optional
  val libHeadersDir = File(baseDir + "/header")
  val dirs = Vector(dseDir, editionsDir, validationDir, paleographyDir, libHeadersDir)

  /** Construct mappings, as configurred for this repository, of CtsUrns to
  * classes implementing the MidMarkupReader trait.
  */
  def readerMappings :  Vector[ReadersPairing]= {
    val readersList = readersConfig.lines.tail
    val pairs = for (row <- readersList) yield {
      val parts = row.split(",").toVector
      ReadersPairing(CtsUrn(parts(0)), HmtValidator.readersForString(parts(1)))
    }
    pairs.toVector
  }


  /** Construct mappings, as configurred for this repository, of CtsUrns to
  * classes implementing the MidOrthography trait.
  */
  def orthoMappings : Vector[OrthoPairing] = {
    Vector.empty[OrthoPairing]
  }
/*

def orthoMappings(csvSource : String = "editions/orthographies.csv") = {
  val csvRows = scala.io.Source.fromFile(csvSource).getLines.toVector.tail
  val pairs = for (row <- csvRows) yield {
    val parts = row.split(",").toVector
    OrthoPairing(CtsUrn(parts(0)), orthoForString(parts(1)))
  }
  pairs.toVector
}

*/
  // Text repo configuration
  val ctsCatalog = editionsDir/"catalog.cex"
  val ctsCitation = editionsDir/"citation.cex"
  val readersConfig = editionsDir/"readers.csv"
  val orthoConfig = editionsDir/"orthographies.csv"
  constructed

  def constructed: Unit = {
    for (d <- dirs) {
      require(d.exists, "Repository not correctly laid out: missing directory " + d)
    }
    for (conf <- Seq(ctsCatalog, ctsCitation, readersConfig, orthoConfig)) {
      require(conf.exists,"Missing required configuration file: " + conf)
    }
  }
}
