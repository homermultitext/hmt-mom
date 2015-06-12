package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test

import org.homermultitext.citemanager.DseManager

class TestValidator extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File authSrc = new File("testdata/authlists")
  
  
  
  void testValidator() {
    HmtValidator v = new HmtValidator(tokens,authSrc)
    assert v.persv.validates()
    assert v.placev.validates()
    assert v.ethnicv.validates()
  }
  
}