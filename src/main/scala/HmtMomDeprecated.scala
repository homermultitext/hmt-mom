package org.homermultitext.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.dse._
import org.homermultitext.edmodel._
import edu.holycross.shot.cex._


/** HmtMom helps you manage and maintain the contents of a Homer Multitext
*  project repository.
*
* @param repo Root directory of a repository laid out according to conventions
* of HMT project in 2018.
*/
case class HmtMomDeprecated(repo: HmtMom) {

  /** Create a corpus of XML archival editions.
  */
  def raw:  Corpus = repo.texts.corpus

  /** Create corpus of XML source of Iliad with all nodes renamed to
  * diplomatic version.
  */
  def iliads: Corpus = repo.iliads() /*{
    val iliadXml = raw ~~ CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
    val iliadNodes = iliadXml.nodes.map( n => {
      val diplo = n.urn.version.replaceAll("_xml","")
      CitableNode(n.urn.dropVersion.addVersion(diplo) ,n.text)
    })
    Corpus (iliadNodes)
  }*/


  /** Create corpus of XML source of scholia with all nodes renamed to
  * diplomatic version.
  */
  def scholia : Corpus = repo.scholia() /*{
    val scholiaXml = raw ~~ CtsUrn("urn:cts:greekLit:tlg5026:")
    val noReff = Corpus(scholiaXml.nodes.filterNot(_.urn.toString.contains(".ref")))
    val collapsed = for (i <- 0 until (noReff.size - 1) by 2 ) yield {
      val u = noReff.nodes(i ).urn.collapsePassageBy(1)
      val txt = "<div>" + noReff.nodes(i).text + " " + noReff.nodes(i+1).text + "</div>"
      CitableNode(u,txt)
    }
    // For
    Corpus(collapsed.toVector.map( n => {
      CitableNode(n.urn.dropVersion.addVersion("dipl") ,n.text)
    }))
  }
*/
  /** Create corpus of XML source of Iliads and scholia.
  */
  def corpus = raw // iliads ++ scholia

  /** CEX records for paleographic observations.
  *  We want data lines only, so drop 1 header line.
  */
  // def paleoCex = DataCollector.compositeFiles(repo.paleographyDir.toString, "cex", 1)


  /** Complete tokenization of the corpus. */
  def tokens = TeiReader.analyzeCorpus(corpus)



}

object HmtMomDeprecated {

  /** Construct an HmtMom object from the path to an
  * editorial repository.
  *
  * @param repoPath Path to repository.
  */
  def apply(repoPath: String) : HmtMomDeprecated = {
    HmtMomDeprecated(HmtMom(repoPath))
  }


}
