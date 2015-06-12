package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test


class TestEthnicName extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File namesList = new File("testdata/authlists/hmtplaces.csv")
  
  
  Integer expectedCount = 3
  Integer expectedFails = 0
  
  void testEthnicNameValidation() {
    EthnicNameValidation ethnicv = new EthnicNameValidation(tokens, namesList)


    // All valid tokens in this sampe, so:
    assert ethnicv.validates()
    assert ethnicv.tokensCount() == expectedCount
    assert ethnicv.successCount() == expectedCount
    assert ethnicv.failureCount() == expectedFails

    
  }
  
}