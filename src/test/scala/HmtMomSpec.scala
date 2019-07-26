package org.homermultitext.hmtmom
import org.scalatest.FlatSpec


import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


class HmtMomSpec extends FlatSpec {

  val repoDir = "src/test/resources/il10"

  "An HmtMom" should "be include a dse directory" in {
    val repo = HmtMom(repoDir)
    val expectedDse = File(repoDir + "/dse")
    assert (repo.dseDir == expectedDse)
  }


  it should "generate a CITE Library" in {
    val repo = HmtMom(repoDir)
    val lib = repo.library
    val expected = "Homer Multitext project, temporary library for validating work in progress"
    assert(lib.name == expected)
  }

  it should "compute CITE relations for scholia" in {
    val repo = HmtMom(repoDir)
    repo.scholiaComments(repo.scholia(repo.texts.corpus))
  }

}
