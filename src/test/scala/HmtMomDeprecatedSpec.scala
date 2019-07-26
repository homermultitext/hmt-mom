package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

class HmtMomDeprecatedSpec extends FlatSpec {

  "An HmtMom" should "create a CTS corpus of raw XML source" in {
    val mom = HmtMomDeprecated("src/test/resources/il10")
    assert(mom.raw.isInstanceOf[Corpus])
  }

  it should "create an Iliad corpus with XML source for diplomatic edition" in pending /*{
    val mom = HmtMomDeprecated("src/test/resources/il10")
    assert(mom.iliads.isInstanceOf[Corpus])

    val expectedIliads = Set("msA", "msB", "e3")
    val actualIliads = mom.iliads.nodes.map(_.urn.version).toSet
    assert(actualIliads == expectedIliads)
  }*/


  it should "create a scholia corpus with XML source for diplomatic edition" in {
    val mom = HmtMomDeprecated("src/test/resources/il10")
    assert(mom.scholia.isInstanceOf[Corpus])

    val expectedScholia = Set("msAext", "msA", "msAil", "msAint", "msAim")
    val actualScholia = mom.scholia.nodes.map(_.urn.work).toSet
    assert(actualScholia == expectedScholia)
  }
  it should "create a comprehensive corpus with XML source for diplomatic edition" in {
    val mom = HmtMomDeprecated("src/test/resources/il10")
    assert(mom.corpus.isInstanceOf[Corpus])
  }
  it should "create a vector of HmtTokens for the whole corpus" in pending /*{
    val mom = HmtMomDeprecated("src/test/resources/il10")
    assert(mom.tokens.isInstanceOf[Vector[HmtToken]])
  }*/



}
