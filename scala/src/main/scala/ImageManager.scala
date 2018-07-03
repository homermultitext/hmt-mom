package edu.holycross.shot.hmtmom
import edu.holycross.shot.cite._

case class ImageManager(
  ictBase: String = "http://www.homermultitext.org/ict2/",
  iipBase: String = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/",

  binaryImgService: String = "http://www.homermultitext.org/hmt-digital/images?request=GetBinaryImage") {


  def ict(img: Cite2Urn) = {
    ictBase + "?urn=" + img
  }

  def binary(img: Cite2Urn) = {}

  //val urlBase = s"${imgService}&w=${imgSize}&urn="


  //val splits = img.get.objectComponent.split("@")
  //  s"![reading](${imgUrlBase}${splits(0)}.tif&RGN=${img.get.objectExtension}&WID=${imageSize}&CVT=JPEG])"


}
