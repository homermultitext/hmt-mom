package edu.holycross.shot.hmtmom
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import org.homermultitext.edmodel._

class MomObjectSpec extends FlatSpec {

  "The HmtMom object" should "profile tokens in a corpus" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val profile = HmtMom.profileTokens(mom.tokens)
    val expectedCategories = Set(LiteralToken, LexicalToken, Unintelligible, Punctuation, NumericToken)
    val actualCategories = profile.map(_._1).toSet

    assert (actualCategories == expectedCategories)
  }

  it should "extract a list of unique lexical words from a list of tokens" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val words = HmtMom.wordList(mom.tokens)
    assert(words.size > 100)
  }

  it should "compute a word histogram for a group of tokens" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val wordHisto = HmtMom.wordHisto(mom.tokens)
    val expectedMostFrequent =  "καὶ"
    val actualMostFrequent = wordHisto(0).str
    assert (actualMostFrequent == expectedMostFrequent)
  }


}
