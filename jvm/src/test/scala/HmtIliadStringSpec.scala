package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._




class HmtIliadStringSpec extends FlatSpec {
  "A HmtIliadString" should "produce equivalent ascii and Unicode views" in {
    val iliadString = HmtIliadString("Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος")
    val expectedAscii = "*mh=nin a)/eide qea\\ *phlhi+a/dew *a)xilh=os"
    assert(iliadString.ascii == expectedAscii)
    val fromAscii = HmtIliadString(expectedAscii)

    val expectedUcode = "Μῆνιν ἄειδε θεὰ Πηληϊάδεω Ἀχιλῆος"
    assert(fromAscii.ucode == expectedUcode)
  }

  it should "transliterate for pretty display" in {
    val iliadString = HmtIliadString("Μῆνιν")
    val expected = "Mênin"
    assert(iliadString.xlit == expected)
  }

  it should "bracket bad characters between hash tags in ascii display" in {
    val badHighStop = HmtIliadString("οὐλομένην· ἡ μυρί' Ἀχαιοῖς ἄλγε' ἔθηκεν")
    val expected = "ou)lome/nhn#·# h( muri/' *a)xaioi=s a)/lge' e)/qhken"
    assert(badHighStop.ascii == expected)
    println(badHighStop.ucode)
  }

  it should "accept tilde as transcription of high stop" in {
    val goodHighStop = HmtIliadString("οὐλομένην~ ἡ μυρί' Ἀχαιοῖς ἄλγε' ἔθηκεν")
    val expected = "ou)lome/nhn~ h( muri/' *a)xaioi=s a)/lge' e)/qhken"
    assert(goodHighStop.ascii == expected)
    println(goodHighStop.ucode)
  }
}
