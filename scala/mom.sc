/*
Mandatory On-going Maintenance.
*/

// Settings

/** Size of image in paleography displays.
*/
val imageSize = 50
/** Binary image service
*/
val imgService = "http://www.homermultitext.org/hmt-digital/images?request=GetBinaryImage"


val baseDir = "src/test/resources"


/*
// Location of data files:
val iliadDataFile = "collections/paleography-iliad.csv"
val scholiaDataFile = "collections/paleography-scholia.csv"
val iliadReportFile = "validation/paleography-iliad.md"
val scholiaReportFile = "validation/paleography-scholia.md"





import com.github.tototoshi.csv._
import java.io.File
import java.io.PrintWriter
*/

import edu.holycross.shot.cite._
import edu.holycross.shot.hmtmom._
import edu.holycross.shot.scm._
import edu.holycross.shot.ohco2._

object Validator {

  def repo: TestReport = {
    println("Check on " + baseDir )
    val inventory = baseDir + "/editions/catalog/textinventory.xml"
    val configFile = baseDir + "/editions/catalog/textconfig.xml"


    val repoTestSuite = TestIdentifier(Cite2Urn("urn:cite2:hmt:editorstests.2017a:textrepo"), "Test for correctly configured repositories of data", "repository/ies")
    val textRepoResult =  try {
      val textRepo = TextRepositorySource.fromFiles(inventory,configFile,baseDir)
      TestResult(s"Repository configured in ${baseDir}", s"Created repository with ${textRepo.corpus.size} citable nodes",true)

    } catch {
      case e: Exception => TestResult(s"Repository configured in ${baseDir}", s"Failed for baseDir ${baseDir}: ${e.getMessage()}", false)
    }

    TestReport(repoTestSuite,Vector(textRepoResult))

  }

}



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