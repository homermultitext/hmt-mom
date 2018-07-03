package edu.holycross.shot.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.scm._
import org.homermultitext.edmodel._


/** Definitions of allowed characters in HMT editions.
*
*
*/
object HmtChars {

  /** Map of modern Greek vowels + tonos to ancient Greek
  * vowel + acute, plus elision mark to plain single quote.
  */
  val greekCpMap: Map[Int, Char] = Map (
    // tonos:
    '\u03ac'.toInt -> '\u1f71', // alpha
    '\u03ad'.toInt -> '\u1f73',//epsilon
    '\u03cc'.toInt -> '\u1f79', // omicron
    '\u03cd'.toInt -> '\u1f7b', // upsilon
    '\u03ce'.toInt -> '\u1f7d', // omega
    // elision
    '\u1fbd'.toInt -> '\u0027'
  )

  // Names for referring to awkward characters:
  val elision = '\u0027'
  val fishtail = '\u2051'
  val cross = '\u2021'
  val backslash = '\\'

  /** Punctuation characters. */
  val punctCPs:  Vector[Char] = Vector('\u003a', '\u003b', '\u002c' , '\u002e', elision, fishtail, cross, '-')

  /** Quantity markers.  */
  val quants:  Vector[Char] = Vector(
    '_', // macron
    '^' // breve
  )

  /** ASCII representations of additional "floating" characters.
  */
  val floatChars:  Vector[Char] = Vector(
    backslash, // grave
    '+',  // diaeresis
    '/',  // acute
    '_', // macron
    '^' // breve
  )

  // Remeber that Vector.range() yields values up to but not including
  // second param, e.g., Vector.range(1,3) == Vector(1,2)
  /** Codepoints for basic alphabetic characters. */
  val basicAlphabetCPs:  Vector[Char] = Vector.range('\u0391', '\u03a2') ++   Vector.range('\u03a3', '\u03aa') ++  Vector.range('\u0381', '\u03ca') ++ Vector('\u03ca', '\u03cb')

  /** Codepoints in thbe extended Greek range omitting undefined codepoints.*/
  val combinedFormCPs:  Vector[Char] =   Vector.range('\u1f00','\u1f16') ++  Vector.range('\u1f18','\u1f1e') ++ Vector.range('\u1f20', '\u1f46') ++ Vector.range('\u1f48', '\u1f4e') ++ Vector.range('\u1f50', '\u1f58') ++ Vector('\u1f59', '\u1f5b', '\u1f5d') ++ Vector.range('\u1f5f', '\u1f7e') ++ Vector.range('\u1f80', '\u1fb5') ++ Vector.range('\u1fb6','\u1fbd') ++ Vector.range('\u1fc2', '\u1fc5') ++ Vector.range('\u1fc6','\u1fcd') ++ Vector.range('\u1fd0', '\u1fd4') ++ Vector.range('\u1fd6', '\u1fdc') ++ Vector.range('\u1fe0','\u1fed') ++ Vector.range('\u1ff2','\u1ff5') ++ Vector.range('\u1ff6','\u1ffd')

  val allowedCPs = basicAlphabetCPs ++  punctCPs ++  combinedFormCPs ++ floatChars


  /** True if codepoint is allowed in HMT editions.
  *
  * @param cp Codepoint to check.
  */
  def legal(cp: Int): Boolean = {
    allowedCPs.contains(cp)
  }

    /** True if character is allowed in HMT editions.
    *
    * @param ch Character to check.
    */
    def legal(ch: Char): Boolean = {
      allowedCPs.contains(ch)
    }
}
