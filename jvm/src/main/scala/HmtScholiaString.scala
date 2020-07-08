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
* @param str A string in either the ascii or ucode representation of the [[HmtScholiaString]]
* system.
*/
case class HmtScholiaString(str: String)  extends LGSTrait with GreekString with Ordered[GreekString] with LogSupport  {

  require(str.nonEmpty, "Cannot create HmtScholiaString from empty String")

  //////////////////////////////////////////////
  /////////////// REQUIRED BY LGSTrait
  //
  /** All valid characters in the ASCII representation of this system
  * in their alphabetic order in Greek.
  */
  def alphabetString =  HmtScholiaOrthography.alphabetString  //"*abgdezhqiklmncoprstufxyw'.|()/\\=+,:;.— \n\r"

  def punctuationString: String = HmtScholiaOrthography.punctuationString

  /** Alphabetically ordered Vector of vowel characters in `ascii` view.*/
  def vowels = HmtScholiaOrthography.vowels //Vector('a','e','h','i','o','u','w')

  /** Alphabetically ordered Vector of consonant characters in `ascii` view.*/
  def consonants = HmtScholiaOrthography.consonants // ('b','g','d','z','q','k','l','m','n','c','p',
  //'r','s','t','f','x','y') //,'Σ')

  def accents =  HmtScholiaOrthography.accents

  /** Breathing characters. */
  def breathings = HmtScholiaOrthography.breathings // Vector(')', '(')
  /** Accent characters. */
  Vector('=', '/', '\\')

  /** */
  def comboChars = HmtScholiaOrthography.comboChars //Vector('|','+')

  // use concrete encoding in LGSTrait
  def combos: String = fixedCombos(str)


  /////////////// REQUIRED BY GreekString trait
  //
  def stripAccent: HmtScholiaString = HmtScholiaString(stripAccentString)

  def stripBreathing: HmtScholiaString = HmtScholiaString(stripBreathingString)

  def stripBreathingAccent: HmtScholiaString = HmtScholiaString(stripBreathingAccentString)

  def flipGrave: HmtScholiaString = HmtScholiaString(flipGraveString)

  def toLower: HmtScholiaString = HmtScholiaString(lowerCase)

  def toUpper: HmtScholiaString = HmtScholiaString(upperCase)


  //// OTHER FORMATTING METHODS
  def capitalize: HmtScholiaString = HmtScholiaString(capitalizeString)

  def depunctuate: HmtScholiaString = HmtScholiaString(HmtScholiaOrthography.depunctuate(ascii).mkString(" "))

}
