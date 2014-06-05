package org.homermultitext.mom



import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn

//import groovy.xml.StreamingMarkupBuilder


/** Class for verifying all material associated with a given Folio object.
*/
class Verifier {

  int debug = 0

  /** The folio to verify. */
  CiteUrn urn

  def imageToSurfaceFiles = []
  def textToImageFiles = []
  def textToSurfaceFiles = []


  Verifier() {
  }
    

  public static void main(String[] args) 
  throws Exception {
    try {
      CiteUrn urn = new CiteUrn (args[0])
      System.out.println "RUNNING VERIFIER on urn " + urn
    } catch (Exception  e) {
      System.err.println "Verifier: bad urn value " + args[0]
      throw e
    }
  }
}

