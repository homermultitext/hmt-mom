package edu.holycross.shot.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.scm._



object HmtMom {


  /** Create a CiteLibrary for a directory following
  * HMT editors' layout conventions.
  *
  * @param dir Name of root directory of an editing project.
  */
  def citeLibrary(dir: String): CiteLibrary = {
    CiteLibrary("","","")
  }
}
