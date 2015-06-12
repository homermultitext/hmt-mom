package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test


class TestPlaceName extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File namesList = new File("testdata/authlists/hmtplaces.csv")
  
  
  Integer expectedCount = 2
  Integer expectedFails = 0
  
  void testPlaceNameValidation() {
    PlaceNameValidation pnv = new PlaceNameValidation(tokens, namesList)


    // All valid tokens in this sampe, so:
    assert pnv.validates()
    assert pnv.tokensCount() == expectedCount
    assert pnv.successCount() == expectedCount
    assert pnv.failureCount() == expectedFails

    
  }
  
}