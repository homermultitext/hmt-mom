package org.homermultitext.hmtmom

import edu.holycross.shot.cite._


/** Convenience class for occurrence of a string in a passage.
*
* @param urn Containing passage.
* @param s String.
*/
case class Occurrence[T](urn: CtsUrn, something: T)
