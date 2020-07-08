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

import java.text.Normalizer

import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.annotation.tailrec

/** Implementation of the MidOrthography trait for orthography of
* Strings the HmtIliadString class.
*/
object HmtIliadOrthography  extends MidOrthography with LogSupport  {
  Logger.setDefaultLogLevel(LogLevel.WARN)
  // 5 methods required by MidOrthography
  //
  // 1. required by MidOrthography trait
  /** Label for orthographic system.*/
  def orthography: String = "Conventional modern orthography of literary Greek"

  // 2. required by MidOrthography trait
  /** Test if cp is a valid code point.
  *
  * @param cp Code point to test.
  */
  def validCP(cp: Int): Boolean = {
    val s = Character.toChars(cp.toInt).toVector.mkString
    val ascii = literaryAsciiOf(s)
    if (ascii.isEmpty){
      warn("NO LITERARY ASCII found for " + s)
      false
    } else {
      val asciiCP = ascii(0).toInt
      validAsciiCP(asciiCP)
    }
  }

  // 3. required by MidOrthography trait
  /** Complete enumeration of MidTokenCategory values. */
  def tokenCategories : Vector[MidTokenCategory] = Vector(
    PunctuationToken, LexicalToken, NumericToken
  )

  // 4. required by MidOrthography trait
  /** Tokenize a citable node.
  *
  * @param n Node to tokenize.
  */
  def tokenizeNode(n: CitableNode): Vector[MidToken] = {
    val urn = n.urn
    // initial chunking on white space
    val lgs = HmtIliadString(n.text)
    val units = lgs.ascii.split("[\\s]+").filter(_.nonEmpty).toVector
    debug("TOKENIZE LGS WITH u " + lgs.ucode)
    debug("ascii " + lgs.ascii)
    debug ("units " + units)

    val classified = for ( (unit, idx) <- units.zipWithIndex) yield {
      val newPassage = urn.passageComponent + "." + idx
      val newVersion = urn.addVersion(urn.versionOption.getOrElse("") + HmtIliadOrthography.exemplarId)
      val newUrn = CtsUrn(newVersion.dropPassage.toString + newPassage)

      debug(s"Look at unit ${idx}: " + unit)
      val trimmed = unit.trim

      if (trimmed.isEmpty) {
        n.text.size match {
          case 0 => {
            warn("HmtIliadString: empty text citable node " + n)
            Vector.empty[MidToken]
          }
          case _ => {
            warn (s"HmtIliadString: evil unicode detected.  Text node of length ${n.text.size} has no tokens.")
            warn("Code points were:")
            warn(CodePointTranscoder.sideBySide(n.text).mkString("\n"))
            Vector.empty[MidToken]
          }
        }
        Vector.empty[MidToken]

      } else {
        // Catch leading quotation?
        val tokensClassified: Vector[MidToken] = if (trimmed(0) == '"') {
            Vector(MidToken(newUrn, "\"", Some(PunctuationToken)))

        } else {
          val depunctuated = depunctuate(unit)
          val first =  MidToken(newUrn, depunctuated.head, lexicalCategory(depunctuated.head))

          val trailingPunct = for (punct <- depunctuated.tail zipWithIndex) yield {
            MidToken(CtsUrn(newUrn + "_" + punct._2), punct._1, Some(PunctuationToken))
          }
          first +: trailingPunct
        }
        tokensClassified
      }
    }
    classified.toVector.flatten
  }
  // 5. required by MidOrthography trait
  /** String value to appendin in forming exemplar ID values.*/
  def exemplarId: String = "_hmtiltkn"


  /** Recursively strips punctuation tokens off the end of a String,
  * to build list of tokens.
  *
  * @param s String to depunctuate.
  * @param depunctVector List of result tokens.
  * @param punctuation String containing all punctuation characters.
  */
  @tailrec def depunctuate (s: String, depunctVector: Vector[String] = Vector.empty, punctuationChars: String = punctuationString): Vector[String] = {
    val trimmed = s.trim
    val trailChar = s"${trimmed.last}"
    if (punctuationChars.contains(trailChar)) {
      val dropLast = trimmed.reverse.tail.reverse
      if (dropLast.nonEmpty) {
        depunctuate(dropLast, trailChar +: depunctVector)
      } else {
        s +: depunctVector
      }

    } else {
      s +: depunctVector
    }
  }

  /** Identify token type of a string.  Numeric tokens are flagged by
  * the trailing numeric tick character.  Punctuation tokens are those
  * found in an list that defaults to the punctuationString.  Other valid
  * strings are lexical tokens.
  *
  * @param s String to classify.
  * @param punctuationChars List of punctuation characters.
  */
  def lexicalCategory(s: String, punctuationChars: String = punctuationString): Option[MidTokenCategory] = {
    if (s.last == numericTick) {
      Some(NumericToken)

    } else if (punctuationChars.contains(s)) {
      Some(PunctuationToken)

    } else if (HmtIliadString(s).valid) {
      Some(LexicalToken)

    } else {
      None
    }
  }


  /** Extract first series of characters from an ascii String
  * forming a single Unicode code point by recursively looking ahead
  * as long as following character is a combining character.
  *
  * @param s String to extract code point from.
  * @param accumulator String accumulasted so far.
  *
  */
  @tailrec def peekAhead(s: String, accumulator: String): String = {
    if (s.size < 2) {
      accumulator + s
    } else {
      if (s(0) == '*') {
        if (s.size == 2) {
          accumulator + s
        } else if (isCombining(s(2))) {
          peekAhead(s.drop(2), accumulator + s.take(2))
        } else {
          accumulator + s.take(2)
        }

      } else if (isCombining(s(1))) {
        peekAhead(s.drop(1), accumulator + s.head)
      } else {
        accumulator + s.head.toString
      }
    }
  }


  ///////////// ORTHOGRAPHIC DEFINITION
  //
  /** All valid characters in the ASCII representation of this system
  * in their alphabetic order in Greek, used in compare
  * method to sort HmtIliad strings.
  */
  val alphabetString = "*abgdezhqiklmncoprstufxyw'|()/\\=+,~.;⁑— \n\r"
  //val alphabetString = "*abgdezhqiklmncoprsΣtufxyw'.|()/\\=+,:;.“”— \n\r"

  /** Alphabetically ordered Vector of vowel characters in `ascii` view.*/
  val vowels = Vector('a','e','h','i','o','u','w')

  /** Alphabetically ordered Vector of consonant characters in `ascii` view.*/
  val consonants = Vector('b','g','d','z','q','k','l','m','n','c','p',
    'r','s','t','f','x','y') //,'Σ')

  /** Breathing characters.*/
  val breathings = Vector(')', '(')

  /** Accent characters.*/
  val accents = Vector('=', '/', '\\')


  /** Characters in addition to breathings and accents that combine with
  * other characters in `ucode` view.*/
  val comboChars = Vector('|','+')

  val punctuationString: String = """,~".;⁑—"""


  val whiteSpace = Vector(' ','\t', '\n', '\r' )

  val numericTick: Character = 'ʹ'
  val typography = Vector('\'',  '*', numericTick)

  val validList = vowels.mkString("") + consonants.mkString("") + breathings.mkString("") + accents.mkString("") + comboChars.mkString("") + punctuationString.mkString("") + whiteSpace.mkString("") + typography.mkString("")


  def validAsciiCP(cp: Int): Boolean = {
    val cArray = Character.toChars(cp)
    alphabetString.contains(cArray(0))
  }


  /** True if given character is a vowel.
  *
  * @param c Character to check.
  */
  def isVowel (c: Character): Boolean = {vowels.contains(c)}

  /** True if given character is a consonant.
  *
  * @param c Character to check.
  */
  def isConsonant (c: Character): Boolean = {consonants.contains(c)}

  /** True if given character is alphabetic.
  *
  * @param c Character to check.
  */
  def isAlpha(c: Character): Boolean = (isVowel(c) || isConsonant(c))

  /** True if given character is an accent.
  *
  * @param c Character to check.
  */
  def isAccent(c: Character): Boolean = {accents.contains(c)}


  /** True if given character is a breathing.
  *
  * @param c Character to check.
  */
  def isBreathing(c: Character): Boolean = {breathings.contains(c)}

  /** True if given character combines with other characters in `ucode` view.
  *
  * @param c Character to check.
  */
  def isCombining(c: Character): Boolean = {
    (comboChars.contains(c) || isAccent(c) || isBreathing(c))
  }

  /** String label for class of a character.
  *
  * @param c Character to classify.
  */
  def classOfChar(c: Character): String = {
    if (vowels.contains(c)) {
      "vowel"
    } else if (consonants.contains(c)) {
      "consonant"
    } else if (breathings.contains(c)) {
      "breathing"
    } else if (accents.contains(c)) {
      "accent"
    } else if (comboChars.contains(c)) {
      "combining"
    } else {
      "invalid"
    }
  }


  /** Use the [[CodePointTranscoder]] object to recursively
  * convert code points represented in `ascii` view to
  * `ucode` code points.
  *
  * @param ascii String to convert to `ucode` view.
  * @param ucode Accumluated string of Unicode code  points
  * in `ucode` view's encoding.
  */
  @tailrec def asciiToUcode(ascii: String, ucode: String = ""): String = {
    //Logger.setDefaultLogLevel(LogLevel.INFO)
    //debug("asciiToUcode: a vs u " + ascii + " vs " + ucode)
    if (ascii.size == 0 ) {
      ucode

    } else if (ascii.size == 1) {
      ucode + CodePointTranscoder.ucodeCodePoint(ascii)

    } else {
      val chunk = peekAhead(ascii,"")
      val newUcode = ucode + CodePointTranscoder.ucodeCodePoint(chunk)
      val newAscii = ascii.drop(chunk.size)
      asciiToUcode(newAscii, newUcode)
    }
  }

  /** Recursively converts code points in a Unicode string in form NFC to
  * equivalent characters in `ascii` view.
  *
  * @param ucode String to convert.  Note that the String must be in
  * Unicode Form NFC.
  * @param ascii String of `ascii` view accumulated so far.
  */
  def nfcToAscii(ucode: String, ascii: String = ""): String = {
    if (ucode.size == 0 ) {
      debug("going with provided ascii " + ascii)
      ascii

    } else if (ucode.size == 1) {
      debug("transcoding ucode " + ucode)
      ascii +  CodePointTranscoder.asciiCodePoint(ucode)

    } else {
      val newUcode = ucode.drop(1)
      val newAscii = ascii + CodePointTranscoder.asciiCodePoint(ucode.head.toString)
      nfcToAscii(newUcode,newAscii )
    }
  }

  def hmtIliadAsciiOf (s: String): String = {
    val checkFirst = if (s.head == '“') {
      s(1)
    } else {
      s.head
    }

    if (checkFirst.toInt > 127) {
      val normalized = Normalizer.normalize(s, Normalizer.Form.NFC)
      HmtIliadOrthography.nfcToAscii(normalized,"")

    } else {
      // handle terminal sigma
      Normalizer.normalize(s, Normalizer.Form.NFC)
    }
  }



  /** Create [[HmtIliadOrthography]]'s `ucode` view of a String.
  * If the first character is <= 127, assume that the String
  * is in `ascii` view and convert it; otherwise, normalize to Unicode
  * form NFC.
  *
  * @param s String to create `ucode` view for.
  */
  def hmtIliadUcodeOf(s: String) : String = {
      val checkFirst = if (s.head == '“') {
        s(1)
      } else {
        s.head
      }

    if (checkFirst.toInt > 127) {
      Normalizer.normalize(s, Normalizer.Form.NFC)


      // waht was all this about?
      /*
      if (s.head.toInt == 787){
        val asciified = CodePointTranscoder.asciiCodePoint(s.tail.head.toString) + ")" + literaryAsciiOf(s.tail.tail)
        val asciiLeader = CodePointTranscoder.asciiCodePoint(s.tail.head.toString)
        val leader = CodePointTranscoder.ucodeCodePoint(asciiLeader + ")")
        Normalizer.normalize(leader + s.tail.tail, Normalizer.Form.NFC)

      } else if (s.tail.head == 787 ) {
        //smoothUpperCase(s.tail.tail)
        Normalizer.normalize(s, Normalizer.Form.NFC)
      } else {*/


    } else {
      HmtIliadOrthography.asciiToUcode(s,"")
    }
  }

}
