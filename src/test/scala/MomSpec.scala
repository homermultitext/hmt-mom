package edu.holycross.shot.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

class MomSpec extends FlatSpec {

  "An HmtMom" should "create a CTS corpus of raw XML source" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    assert(mom.raw.isInstanceOf[Corpus])
  }

  it should "create a corpus with XML source for diplomatic edition" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    assert(mom.iliads.isInstanceOf[Corpus])

    val expectedIliads = Set("msA", "msB", "e3")
    val actualIliads = mom.iliads.nodes.map(_.urn.version).toSet
    assert(actualIliads == expectedIliads)
  }

}
