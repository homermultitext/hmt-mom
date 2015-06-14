package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test

import org.homermultitext.citemanager.DseManager

class TestValidator extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File authSrc = new File("testdata/authlists")
  File byz = new File("testdata/authlists/orthoequivs.csv")
  String morphCmd = "../morpheus/bin/morpheus"
    
  
  
  void testValidator() {
    HmtValidator v = new HmtValidator(tokens,authSrc, byz, morphCmd)
    assert v.persv.validates()
    assert v.placev.validates()
    assert v.ethnicv.validates()

    assertFalse v.lexv.validates()

    Integer expectedSuccesses = 390
    Integer expectedFailures = 111
    assert v.lexv.successCount() == expectedSuccesses
    assert v.lexv.failureCount() == expectedFailures
    
  }
  
}