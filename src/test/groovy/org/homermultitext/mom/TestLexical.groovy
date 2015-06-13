package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test


class TestLexical extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File byz = new File("testdata/authlists/orthoequivs.csv")
  
  String morphCmd = "../morpheus/bin/morpheus"
  
  Integer expectedCount = 904
  Integer expectedFails = 241
  Integer expectedSuccesses = 663
  
  void testLexicalValidation() {
    LexicalValidation lexicalv = new LexicalValidation(tokens, byz, morphCmd)
    
    assertFalse lexicalv.validates()
    assert lexicalv.tokensCount() == expectedCount
    // all tokens accounted for:
    assert lexicalv.successCount() + lexicalv.failureCount() == lexicalv.tokensCount()    
    // break down as expected:
    assert lexicalv.successCount() == expectedSuccesses
    assert lexicalv.failureCount() == expectedFails
  }
  
}