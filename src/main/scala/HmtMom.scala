package edu.holycross.shot.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.scm._



object HmtMom {
  val library = "Editing in progress"
  val libraryUrn = Cite2Urn("urn:cite2:hmt:mom.v1:mom")
  val license = "Creative Commons Attribution Share-Alike"
  val namespaces = Vector[CiteNamespace]()


  /** Build a TextRepository from editorial source.
  */
  def texts: Option[TextRepository] = {
    None
  }

  /** Build a CiteCollectionRepository from editorial source.
  */
  def collections: Option[CiteCollectionRepository] = {
    None
  }

  /** Build ImageExtensions from editorial source.

  def images: Option[ImageExtensions] = {
    None
  }
  */

  /** Build CiteRelationSet from editorial source.
  */
  def indexes: Option[CiteRelationSet] = {
    None
  }


  /** Create a CiteLibrary for a directory following
  * HMT editors' layout conventions.
  *
  * @param dir Name of root directory of an editing project.
  */
  def citeLibrary(dir: String): CiteLibrary = {
    CiteLibrary(library,libraryUrn,license,namespaces,
      texts,collections,indexes
    )
  }



}
