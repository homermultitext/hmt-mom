package edu.holycross.shot.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._


/**
*/
class ImageManagerSpec extends FlatSpec {

  val asterisk = Cite2Urn("urn:cite2:hmt:vaimg.2017a:VA012RN_0013@0.1478,0.4788,0.01279,0.008458")

  "An ImageManager" should "do things" in {
    val im = ImageManager()
    val expected = "http://www.homermultitext.org/ict2/?urn=urn:cite2:hmt:vaimg.2017a:VA012RN_0013@0.1478,0.4788,0.01279,0.008458"

    assert(im.ict(asterisk) == expected)
  }
  //
}
