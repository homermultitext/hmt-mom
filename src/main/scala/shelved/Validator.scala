package org.homermultitext.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.scm._



case class Validator(editorsRepo: EditorsRepo) {
/*
  def repo: TestReport = {
    val filesTestSuite = TestIdentifier(
      Cite2Urn("urn:cite2:hmt:editorstests.2017a:repoformat"),
      "Test for correctly configured repositories of data",
      "repository/ies")

    val textRepoResult =  try {
      val textRepo = TextRepositorySource.fromFiles(editorsRepo.textInventory,editorsRepo.textConfig,editorsRepo.baseDir)
      TestResult(s"Repository configured in ${editorsRepo.baseDir}", s"Created repository with ${textRepo.corpus.size} citable nodes",true)

    } catch {
      case e: Exception => TestResult(s"Repository configured in ${editorsRepo.baseDir}", s"Failed for baseDir ${editorsRepo.baseDir}: ${e.getMessage()}", false)
    }

    val collRepoResult =
      editorsRepo.collRepoOption match {
        case collRepo: Some[CiteCollectionRepository] =>  TestResult(s"Repository configured in ${editorsRepo.baseDir}", s"Created collection repository with ${collRepo.get} citable objects",true)
      case None =>     TestResult(s"Repository configured in ${editorsRepo.baseDir}", s"Failed to create collection repository in ${editorsRepo.baseDir}",false)
    }

    TestReport(filesTestSuite,Vector(textRepoResult, collRepoResult))

  }
*/

}
