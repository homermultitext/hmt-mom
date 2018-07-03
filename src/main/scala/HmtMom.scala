package edu.holycross.shot.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.scm._



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

  def corpus = iliads ++ scholia


}
