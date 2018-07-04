package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

class MomDseSpec extends FlatSpec {

  "An HmtMom" should "build a DseVector for the repository" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val dseV = mom.dse
    assert(dseV.passages.size > 0)
  }

}
