package edu.holycross.shot.hmtmom

/** Convenience class for counts of string occurrences.
*
* @param s String.
* @param count: Number of occurrences.
*/
case class StringCount(s: String, count: Int) {
  def cex :  String = {
    s + "#" + count
  }
}
