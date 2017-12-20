package edu.holycross.shot.hmtmom

import java.io.File
import scala.io.Source
import edu.holycross.shot.citeobj._
import edu.holycross.shot.cite._
import edu.holycross.shot.scm._



/** Editoiral repository.  Because building a collection repository is so expensive,
* we ask for a precomputed option on that.
*
* @param baseDir Root diretory of HMT editor's repository.
* @param collRepOption Some prebuilt CiteCollectionRepository, or None if build failed.
*/
case class EditorsRepo(baseDir: String, collRepoOption: Option[CiteCollectionRepository])  {

  // Names of required files
  def textInventory = baseDir + "/editions/catalog/textinventory.xml"
  def textConfig = baseDir + "/editions/catalog/textconfig.xml"

  def collInventory = baseDir + "/collections/catalog/citecatalog.cex"
  def vaFolio  = baseDir + "/collections/venetusA.cex"
  def vaImages = baseDir + "/collections/vaimages.cex"

  def mainScholia = baseDir + "/dse-models/venA-mainScholia.csv"
  def intermargScholia = baseDir + "/dse-models/venA-intermarginal.csv"
  def intScholia = baseDir + "/dse-models/venA-interior.csv"
  def extScholia = baseDir + "/dse-models/venA-exterior.csv"
  def interlinScholia = baseDir + "/dse-models/venA-interlinear.csv"


  /** Files required for layout of HMT editors' repository.
  */
  val requiredFiles = Vector(
    textInventory, textConfig,
    collInventory,vaFolio,vaImages,
    mainScholia, intermargScholia,  intScholia, extScholia, interlinScholia
  )

  /** True if all required files exist.
  */
  def fileLayoutOk: Boolean = {
    for (fName <- requiredFiles) {
      val f = new File(fName)
      require(f.exists(), s"==>Could not find required file ${f} ")
    }
    true
  }

  /** True if CiteCollectionRepository exists.
  */
  def collRepoOk: Boolean = {
    collRepoOption match {
     case None => false
     case repo: Some[CiteCollectionRepository] => true
    }
  }


  /** True if layout and contents of this repository are valid.
  */
  def validRepo: Boolean = {
    require(fileLayoutOk)
    require(collRepoOk, s"==>Failed to make collection repository from base dir. ${baseDir}")
    true
  }

  require(validRepo)


  /** Contents of editorial repository as a CiteLibrary.
  */
  def citeLibrary = {
    val library = "Editing in progress"
    val libraryUrn = Cite2Urn("urn:cite2:hmt:mom.2017a:mom")
    val license = "Creative Commons Attribution Share-Alike"
    val namespaces = Vector[CiteNamespace]()

  }

 }
