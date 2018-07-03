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

  it should "convert allowed input characters to required output characters" in pending

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

  it should "compile a complete index of token occurrences" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val mom = HmtMom(repo)
    val wordHisto = HmtMom.wordHisto(mom.tokens)

  }
  it should "compute a sequence of StringOccurences from a sequence of tokens" in {
    val repo = EditorsRepo("src/test/resources/il10")
    val tkns = HmtMom(repo).tokens
    val stringOccurences = HmtMom.stringSeq(tkns)
    assert(stringOccurences.size == tkns.size)
    assert(stringOccurences.isInstanceOf[Vector[StringOccurrence]])
  }

  it should "compute a histogram of characters in a list of tokens" in pending
  it should "report on all character-set errors in a list of tokens" in pending
  it should "report on all markup errors in a list of tokens" in pending

}
