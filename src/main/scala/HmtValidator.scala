package org.homermultitext.hmtmom

import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.dse._
import org.homermultitext.edmodel._


/** A class for validating a CiteLibrary with HMT project content.
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
}
