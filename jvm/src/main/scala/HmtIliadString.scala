package org.homermultitext.hmtmom


import edu.holycross.shot.greek._
import edu.holycross.shot.mid.orthography._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cite._
import edu.holycross.shot.citevalidator._
import edu.holycross.shot.scm._
import edu.holycross.shot.dse._

import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.annotation.tailrec

/** Representation of a Greek string written in conventional literary orthography.
*
* @param str A string in either the ascii or ucode representation of the [[HmtIliadString]]
* system.
*/
case class HmtIliadString(str: String)  extends LGSTrait with GreekString with Ordered[GreekString] with LogSupport  {

  require(str.nonEmpty, "Cannot create HmtIliadString from empty String")

  /** The ASCII representation of this string.
  */
  override def ascii = HmtIliadOrthography.hmtIliadAsciiOf(combos.replaceAll("ς","σ"))

  //val ucode =  literaryUcodeOf(fixedCombos.replace("s ","Σ ").replaceAll("s$","Σ").replaceAll("σ ", "ς ").replaceAll("σ$", "ς"))
  /** The representation of this string with glyphs in the "Greek and Coptic"
  * and "Extended Greek" blocks of Unicode.
  */
  override def ucode =  HmtIliadOrthography.hmtIliadUcodeOf(combos).replaceAll("σ ", "ς ").replaceAll("σ$", "ς")

  //////////////////////////////////////////////
  /////////////// REQUIRED BY LGSTrait
  //
  /** All valid characters in the ASCII representation of this system
  * in their alphabetic order in Greek.
  */
  def alphabetString =  HmtIliadOrthography.alphabetString  //"*abgdezhqiklmncoprstufxyw'|()/\\=+,~;.— \n\r"

  def punctuationString: String = HmtIliadOrthography.punctuationString

  /** Alphabetically ordered Vector of vowel characters in `ascii` view.*/
  def vowels = HmtIliadOrthography.vowels //Vector('a','e','h','i','o','u','w')

  /** Alphabetically ordered Vector of consonant characters in `ascii` view.*/
  def consonants = HmtIliadOrthography.consonants

  /** Accent characters in ascii view. */
  def accents =  HmtIliadOrthography.accents

  /** Breathing characters. */
  def breathings = HmtIliadOrthography.breathings

  /** Combining characters (iota subscript, diaeresis) in ascii view.*/
  def comboChars = HmtIliadOrthography.comboChars

  /** Use LGSTrait's concrete fixedCombos method to
  *
  */
  def combos: String = fixedCombos(str)


  /////////////// REQUIRED BY GreekString trait
  //
  def stripAccent: HmtIliadString = HmtIliadString(stripAccentString)

  def stripBreathing: HmtIliadString = HmtIliadString(stripBreathingString)

  def stripBreathingAccent: HmtIliadString = HmtIliadString(stripBreathingAccentString)

  def flipGrave: HmtIliadString = HmtIliadString(flipGraveString)

  def toLower: HmtIliadString = HmtIliadString(lowerCase)

  def toUpper: HmtIliadString = HmtIliadString(upperCase)


  //// OTHER FORMATTING METHODS
  def capitalize: HmtIliadString = HmtIliadString(capitalizeString)

  def depunctuate: HmtIliadString = HmtIliadString(HmtIliadOrthography.depunctuate(ascii).mkString(" "))

}
