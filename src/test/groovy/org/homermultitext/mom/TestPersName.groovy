package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test


class TestPersName extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File namesList = new File("testdata/authlists/hmtnames.csv")
  
  
  Integer expectedCount = 17
  Integer expectedFails = 0
  
  void testPersNameValidation() {
    PersNameValidation pnv = new PersNameValidation(tokens, namesList)

    // All valid tokens in this sampe, so:
    assert pnv.validates()
    assert pnv.tokensCount() == expectedCount
    assert pnv.successCount() == expectedCount
    assert pnv.failureCount() == expectedFails

    
  }
  
}