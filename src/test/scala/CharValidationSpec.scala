package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

class CharValidationSpec extends FlatSpec {

    "An HmtMom" should "find bad chars in a String" in {
      val s = "ἄλφα beta"
      val tokens = TeiReader(s"urn:cts:greekLit:tlg0012.tlg001.msA:10.madeup#<p>${s}</p>").tokens
      println("Start from " + tokens)
      println(HmtMom.badCharTokens(tokens))
    }
}
