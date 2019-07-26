package org.homermultitext.hmtmom
import org.scalatest.FlatSpec


import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


class EditorsRepoSpec extends FlatSpec {

  val repoDir = "src/test/resources/il10"

  "An EditorsRepo" should "be include a dse directory" in {
    val repo = EditorsRepo(repoDir)
    val expectedDse = File(repoDir + "/dse")
    assert (repo.dseDir == expectedDse)
  }


  it should "generate a CITE Library" in {
    val repo = EditorsRepo(repoDir)
    val lib = repo.library
    val expected = "Homer Multitext project, temporary library for validating work in progress"
    assert(lib.name == expected)
  }

}
