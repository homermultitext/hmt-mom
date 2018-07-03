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



}
