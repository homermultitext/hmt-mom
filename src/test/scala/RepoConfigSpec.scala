package org.homermultitext.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._


/**
*/
class RepoConfigSpec extends FlatSpec {

  "An HMT MOM repository" should "fail if missing dse dir" in {
    val badSrc = "src/test/resources/nodse"
    try {
      val repo = HmtMom(badSrc)
      fail("Should not have created repo.")
    } catch {
      case err: Throwable => assert(true)
    }
  }
  it  should "fail if missing editing dir" in {
    val badSrc = "src/test/resources/noeditions"
    try {
      val repo = HmtMom(badSrc)
      fail("Should not have created repo.")
    } catch {
      case err: Throwable => assert(true)
    }
  }
  it  should "fail if missing validation dir" in {
    val badSrc = "src/test/resources/novalidation"
    try {
      val repo = HmtMom(badSrc)
      fail("Should not have created repo.")
    } catch {
      case err: Throwable => assert(true)
    }
  }
  it  should "fail if missing paleography dir" in {
    val badSrc = "src/test/resources/nopaleography"
    try {
      val repo = HmtMom(badSrc)
      fail("Should not have created repo.")
    } catch {
      case err: Throwable => assert(true)
    }
  }

  it  should "fail if missing CTS catalog" in {
    val badSrc = "src/test/resources/noctscatalog"
    try {
      val repo = HmtMom(badSrc)
      fail("Should not have created repo.")
    } catch {
      case err: Throwable => assert(true)
    }
  }

  it  should "fail if missing CTS citation configuration" in {
    val badSrc = "src/test/resources/noctscitation"
    try {
      val repo = HmtMom(badSrc)
      fail("Should not have created repo.")
    } catch {
      case err: Throwable => assert(true)
    }
  }




}
