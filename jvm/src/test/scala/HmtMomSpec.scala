package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._

import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


class HmtMomSpec extends FlatSpec {

  val repoDir = "./src/test/resources/il10"

  "An HmtMom" should "create an MID EditorsRepo" in {
    val mom = HmtMom(repoDir)
    assert(mom.midRepo.baseDir == repoDir)
  }

  it should "include a dse directory" in {
    val mom = HmtMom(repoDir)
    val expectedDse = File(repoDir + "/dse")
    assert (mom.midRepo.dseDir == expectedDse)
  }

  it should "do MID DSE consistency checking" in {
    val mom = HmtMom(repoDir)
  }


  it should "generate a CITE Library" in {
    val mom = HmtMom(repoDir)
    val lib = mom.library
    val expected = "Homer Multitext project, temporary library for validating work in progress"
    assert(lib.name == expected)
  }

  it should "compute CITE relations for scholia" in {
    val mom = HmtMom(repoDir)
    mom.scholiaComments(mom.scholia(mom.texts.corpus))
  }

  it should "map texts String to scala classes implementing MidMarkupReader" in {
    val mom = HmtMom(repoDir)
    val readers = mom.readerMappings

    // Entries configured for 3 distinct URNs
    val expectedSize = 3
    assert(readers.size == expectedSize)

    // Only 1 entry for homeric poetry
    val george = CtsUrn("urn:cts:greekLit:tlg0012:")
    val georgeMapping = readers.filter(_.urn ~~ george)
    assert(georgeMapping.size == 1)
  }

  it should "map texts String to scala classes implementing MidOrthography" in {
    val mom = HmtMom(repoDir)
    val orthos = mom.orthoMappings
    val expectedSize = 3
    assert(orthos.size == expectedSize)

    // Only 1 entry for homeric poetry
    val george = CtsUrn("urn:cts:greekLit:tlg0012:")
    val georgeMapping = orthos.filter(_.urn ~~ george)
    assert(georgeMapping.size == 1)
  }

  it should "support MID validation" in {
    
  }
}
