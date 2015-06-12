package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test


class TestValidator extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File authSrc = new File("testdata/authlists")
  
  
  
  void testValidator() {
    Validator v = new Validator(tokens,authSrc)
    assert v.persv.validates()
  }
  
}