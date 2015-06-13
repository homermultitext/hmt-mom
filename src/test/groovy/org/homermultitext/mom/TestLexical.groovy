package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test


class TestLexical extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File byz = new File("testdata/authlists/orthoequivs.csv")
  
  String morphCmd = "../morpheus/bin/morpheus"
  
  Integer expectedCount = 3
  Integer expectedFails = 0
  
  void testLexicalValidation() {
    LexicalValidation lexicalv = new LexicalValidation(tokens, byz, morphCmd)

    /*
    // All valid tokens in this sampe, so:
    assert ethnicv.validates()
    assert ethnicv.tokensCount() == expectedCount
    assert ethnicv.successCount() == expectedCount
    assert ethnicv.failureCount() == expectedFails
    */
    
  }
  
}