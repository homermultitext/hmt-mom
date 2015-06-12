package org.homermultitext.mom



  /* Needs:
   * 1. Map of features (or tokens) to occurrences in a text.
   * The map's keys will be CITE URNs, and the values will be CTS URNs, 
   * normally with subref values. 
   * 2. counts good/bad/total
   * 3. boolean allValid
   * 4. map of tokens to validation
   */


/** Trait for working with a HMT-MOM validation test. */


public interface Validation {

  LinkedHashMap getValidationResults()
  boolean validates()
  Integer successCount()
  Integer failureCount()
  Integer tokensCount()
  
}
