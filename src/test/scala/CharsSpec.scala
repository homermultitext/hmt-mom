package edu.holycross.shot.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

class CharsSpec extends FlatSpec {

  "The HmtChars object" should "determine if a character is legal" in {
    assert(HmtChars.legal('α'))
    assert(HmtChars.legal('a') == false)
  }
}
