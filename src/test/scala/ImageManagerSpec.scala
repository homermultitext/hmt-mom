package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._


/**
*/
class ImageManagerSpec extends FlatSpec {

  val asterisk = Cite2Urn("urn:cite2:hmt:vaimg.2017a:VA012RN_0013@0.1478,0.4788,0.01279,0.008458")

  "An ImageManager" should "form URLs for using an image in ICT" in {
    val im = ImageManager()
    val expected = "http://www.homermultitext.org/ict2/?urn=urn:cite2:hmt:vaimg.2017a:VA012RN_0013@0.1478,0.4788,0.01279,0.008458"

    assert(im.ict(asterisk) == expected)
  }
  it should "form URLs for retrieving binary data for an image with no RoI" in {
    val im = ImageManager()
    val noRoi = asterisk.dropExtensions
    val expected = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vaimg/2017a/VA012RN_0013.tif&WID=100&CVT=JPEG"
    val actual = im.binary(noRoi)

    assert(actual == expected)
  }
  it should "form URLs for retrieving binary data for an image including RoI" in {
    val im = ImageManager()
    val expected = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vaimg/2017a/VA012RN_0013.tif&RGN=0.1478,0.4788,0.01279,0.008458&WID=100&CVT=JPEG"
    assert(im.binary(asterisk) == expected)
  }
  it should "allow specification of image size in retrieving binary data" in {
    val im = ImageManager()
    val expected = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vaimg/2017a/VA012RN_0013.tif&RGN=0.1478,0.4788,0.01279,0.008458&WID=500&CVT=JPEG"

    val actual = im.binary(asterisk, 500)
    assert( actual == expected)
  }

  it should "compose Markdown for an embedded image linked to ICT" in {
    val im = ImageManager()
    val expected = "[![image](http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vaimg/2017a/VA012RN_0013.tif&RGN=0.1478,0.4788,0.01279,0.008458&WID=100&CVT=JPEG)](http://www.homermultitext.org/ict2/?urn=urn:cite2:hmt:vaimg.2017a:VA012RN_0013@0.1478,0.4788,0.01279,0.008458)"
    assert(im.markdown(asterisk) == expected)
  }
}
