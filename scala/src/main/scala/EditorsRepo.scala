package edu.holycross.shot.hmtmom

import java.io.File
import scala.io.Source
import edu.holycross.shot.citeobj._



case class EditorsRepo(baseDir: String, collRepoOption: Option[CiteCollectionRepository])  {
  val textInventory = baseDir + "/editions/catalog/textinventory.xml"
  val textInvFile = new File(textInventory)
  require(textInvFile.exists(), s"==>Could not find text inventory file ${textInventory} ")

  val textConfig = baseDir + "/editions/catalog/textconfig.xml"
  val textConfigFile = new File(textConfig)
  require(textConfigFile.exists(), s"==>Could not find text configuration file ${textConfig} ")

  val collInventory = baseDir + "/collections/catalog/citecatalog.cex"
  val collInventoryFile = new File(collInventory)
  require(collInventoryFile.exists(), s"==>Could not find collections inventory file ${collInventory} ")


  val vaFolio  = baseDir + "/collections/venetusA.cex"
  val vaFolioFile = new File(vaFolio)
  require(vaFolioFile.exists(), s"==>Could not find Venetus A folio data ${vaFolio} ")


  val vaImages = baseDir + "/collections/vaimages.cex"
  val vaImagesFile = new File(vaImages)
  require(vaImagesFile.exists(), s"==>Could not find Venetus A image data ${vaImages} ")

  val inventoryString = Source.fromFile(collInventory).getLines.mkString("\n")
  val dataString = Source.fromFile(vaFolio).getLines.mkString("\n") +   Source.fromFile(vaImages).getLines.mkString("\n")

  def collRepoOk: Boolean = {
   collRepoOption match {
     case None => false
     case repo: Some[CiteCollectionRepository] => true
   }
  }
  require(collRepoOk, s"==>Failed to make collection repository from base dir. ${baseDir}")


 }
