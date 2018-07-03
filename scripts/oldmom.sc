/*
Mandatory On-going Maintenance.

Two imortant objects:

1. Validator
2. Verifier

*/


// Settings

/** Size of image in paleography displays.
*/
val imageSize = 50
/** Binary image service
*/
val imgService = "http://www.homermultitext.org/hmt-digital/images?request=GetBinaryImage"


val baseDir = "src/test/resources"



//import com.github.tototoshi.csv._
//import java.io.PrintWriter


import edu.holycross.shot.cite._
import edu.holycross.shot.hmtmom._
import edu.holycross.shot.scm._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citeobj._

import org.homermultitext.edmodel._

import scala.io.Source
import java.io.File

println("\nCreating library from repository data.  Please be patient...")

// Process big static files ahead of time.
val coll =  {
  val collInventory = baseDir + "/collections/catalog/citecatalog.cex"
  val vaFolio  = baseDir + "/collections/venetusA.cex"
  val vaImages = baseDir + "/collections/vaimages.cex"

  try {
    val inventoryString = Source.fromFile(collInventory).getLines.mkString("\n")
    val dataString = Source.fromFile(vaFolio).getLines.mkString("\n") +   Source.fromFile(vaImages).getLines.mkString("\n")
    val ccr = CiteCollectionRepository(s"${inventoryString}\n${dataString}")
    Some(ccr)
  } catch {
   case e: Exception => {
    println(s"==>Failed to make collection repository configured in ${baseDir}: ${e.getMessage}")
    None
   }
  }
}





val repo = EditorsRepo(baseDir,coll)

val validator = Validator(repo)


println("\n\nValidator ready to use.\n")


val tr = TextRepositorySource.fromFiles(repo.textInventory,repo.textConfig,repo.baseDir)
val tokens = TeiReader.fromCorpus(tr.corpus)
val urnList = tokens.map(_.textNode).distinct
val vectvect = for (u <- tokens.map(_.textNode)) yield { tokens.filter(_.textNode == u).map(_.readWithDiplomatic.text) }.distinct

val dipl =  vectvect.map(_.mkString(" ")).mkString("\n")

/*


object Paleography {


  def view = {
    scholia
    iliad
  }

  def scholia = {
    val urlBase = s"${imgService}&w=${imageSize}&urn="
    val report =  StringBuilder.newBuilder
    report.append("# Paleographic verification: *scholia*\n\n")
    report .append("| Reading     | Image     |\n| :------------- | :------------- |\n")

    val reader = CSVReader.open(new File(scholiaDataFile))
    val entries = reader.allWithHeaders()

    for (entry <- entries) {
      val img = entry("Image")
      try {
        val txt = CtsUrn(entry("TextUrn"))
        val reading = txt.passageNodeSubrefText

        report.append(s"| ${reading} | ![${txt}](${urlBase}${img}) |\n")
      } catch {
        case t: Throwable => report.append(s"| ${t.getMessage()} | Text reference: " + entry("TextUrn") + " |\n")
      }
    }
    report.append("\n\n")
    new PrintWriter(scholiaReportFile) { write(report.toString); close }
  }

  def iliad = {
    val urlBase = s"${imgService}&w=${imageSize}&urn="
    val report =  StringBuilder.newBuilder
    report.append("# Paleographic verification: *Iliad* text\n\n")
    report .append("| Reading     | Image     |\n| :------------- | :------------- |\n")

    val reader = CSVReader.open(new File(iliadDataFile))
    val iliadEntries = reader.allWithHeaders()

    for (entry <- iliadEntries) {
      val img = entry("Image")
      try {
        val txt = CtsUrn(entry("TextUrn"))
        val reading = txt.passageNodeSubrefText

        report.append(s"| ${reading} | ![${txt}](${urlBase}${img}) |\n")
      } catch {
        case t: Throwable => report.append(s"| ${t.getMessage()} | Text reference: " + entry("TextUrn") + " |\n")
      }
    }
    report.append("\n\n")
    new PrintWriter(iliadReportFile) { write(report.toString); close }
  }

}
*/
