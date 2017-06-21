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

import scala.io.Source
import java.io.File


val repo = EditorsRepo(baseDir)

/*



case class Validator(repo: EditorsRepo) = {

  def repo: TestReport = {
    val filesTestSuite = TestIdentifier(
      Cite2Urn("urn:cite2:hmt:editorstests.2017a:repoformat"),
      "Test for correctly configured repositories of data",
      "repository/ies")

    val textRepoResult =  try {
      val textRepo = TextRepositorySource.fromFiles(repo.textInventory,repo.textConfig,baseDir)
      TestResult(s"Repository configured in ${baseDir}", s"Created repository with ${textRepo.corpus.size} citable nodes",true)

    } catch {
      case e: Exception => TestResult(s"Repository configured in ${baseDir}", s"Failed for baseDir ${baseDir}: ${e.getMessage()}", false)
    }

    val collRepoResult =
      repo.collRepoOption match {
        case collRepo: Some[CiteCollectionRepository] =>  TestResult(s"Repository configured in ${baseDir}", s"Created collection repository with ${collRepo.get} citable objects",true)
      case None =>     TestResult(s"Repository configured in ${baseDir}", s"Failed to create collection repository in ${baseDir}",false)
    }

    TestReport(filesTestSuite,Vector(textRepoResult, collRepoResult))

  }
}

val repo = new EditorsRepo(baseDir)
val validator = Validator(repo)

object Verifier {
  def paleography = {
  }
  def namedEntities = {}
  def textMapping = {} // two ways: by text passage (extracted image), by TBS (overlay all)


}
*/
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
