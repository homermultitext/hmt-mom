package org.homermultitext

/** Provides classes for validating editorial work on the Homer Multitext
project.
* See https://homermultext.github.io/ for documentation.
*
* ==Overview==
* The class  [[HmtMom]] manages contents of an [[EditorsRepo]].  It
* can be constructucted from a string path for the root of the
* repository.  For example:
*
* {{{
* val mom = HmtMom("REPOROOT")
* }}}
*
*
*/
package object hmtmom {

  /** Object selectors for MSS illustrated in bifolio spreads.*/
  val bifolios = Seq("e3", "venB")

  /** Base URL for Image Citation Tools.*/
  val ictBase= "http://www.homermultitext.org/ict2/"

  /** Icon image for good results.*/
  val okImg = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vaimg/2017a/VA311RN_0481.tif&RGN=0.6043,0.2275,0.01013,0.008714&WID=50&CVT=JPEG"

  val sadImg = "http://www.homermultitext.org/iipsrv?OBJ=IIP,1.0&FIF=/project/homer/pyramidal/deepzoom/hmt/vbimg/2017a/VB216VN_0316.tif&RGN=0.4788,0.7559,0.01419,0.007746&WID=50&CVT=JPEG"


}
