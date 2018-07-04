package org.homermultitext.hmtmom
import edu.holycross.shot.cite._


/** A class for working with image services recognizing URNs.
*
* @param ictBase Base URL for HMT Image Citation Service, version 2.
* @param iipBase Base URL for IIP image service.
*/
case class ImageManager(
  ictBase: String = "http://www.homermultitext.org/ict2/",
  iipBase: String = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/"
  ) {

  /**  Form URL for using an image in HMT Image Citation Tool.
  *
  * @param img Image to open in ICT.
  */
  def ict(img: Cite2Urn): String = {
    ictBase + "?urn=" + img
  }

  /**  Form URL for image binary data.
  *
  *  @param img Image to retrieve.
  *  @param imgSize Size of binary image, in pixels.
  */
  def binary(img: Cite2Urn, imgSize: Int = 100): String = {
    val imgPath = iipBase + s"${img.namespace}/${img.collection}/${img.version}/${img.dropExtensions.objectComponent}"

    img.objectExtensionOption match {
      case roi: Some[String] =>        s"${imgPath}.tif&RGN=${roi.get}&WID=${imgSize}&CVT=JPEG"
      case None => s"${imgPath}.tif&WID=${imgSize}&CVT=JPEG"
    }
  }


  /**  Compose markdown for binary image linked to ICT.
  *
  * @param img Image to display in markdown.
  * @param imgSize Size of binary image, in pixels.
  */
  def markdown(img: Cite2Urn, imgSize: Int = 100): String = {
    s"[![image](${binary(img, imgSize)})](${ict(img)})"
  }

}
