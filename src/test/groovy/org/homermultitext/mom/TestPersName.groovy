package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test


class TestPersName extends GroovyTestCase {

  File tokens = new File("testdata/tokens/tokens.csv")
  File namesList = new File("testdata/authlists/hmtnames.csv")
  
  

  
  void testPersNameValidator() {
    PersNameValidation pnv = new PersNameValidation(tokens, namesList)

    assert pnv.validates()
  }
  
}