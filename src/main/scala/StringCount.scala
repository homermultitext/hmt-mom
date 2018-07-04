package org.homermultitext.hmtmom

/** Convenience class for counts of string occurrences.
*
* @param str String.
* @param count: Number of occurrences.
*/
case class StringCount(str: String, count: Int) {

  /** Format pair for CEX. */
  def cex :  String = {
    str + "#" + count
  }
}
