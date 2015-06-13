package org.homermultitext.mom



import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import org.homermultitext.citemanager.DseManager

//import groovy.xml.StreamingMarkupBuilder


/** Class for validating all material associated with a given text-bearing surface (page of a MS, column of a papyrus...)
*/
class HmtValidator  {

  int debug = 0


  // Allow a range value?
  /** The folio to validate. */
  CiteUrn urn


  PersNameValidation persv
  PlaceNameValidation placev
  EthnicNameValidation ethnicv
  LexicalValidation lexv



  // need to know:
  // authlists dir
  // tokens
  HmtValidator(File tokens, File authListsDir, File byzOrtho, String morphCmd) {
    persv = new PersNameValidation(tokens, new File(authListsDir, "hmtnames.csv"))
    placev = new PlaceNameValidation(tokens, new File(authListsDir, "hmtplaces.csv"))
    ethnicv = new EthnicNameValidation(tokens, new File(authListsDir, "hmtplaces.csv"))
    lexv = new LexicalValidation(tokens, byzOrtho, morphCmd)
  }



  
  /*
  public static void main(String[] args) 
  throws Exception {
    DseManager dsemgr = new DseManager()
    try {
      CiteUrn urn = new CiteUrn (args[0])

      String imgFolioStr = args[1].replaceFirst(/^\[/, "")
      imgFolioStr = imgFolioStr.replaceFirst(/\]$/, "")
      def imgFolioIndices = imgFolioStr.split(/,/)
      def imgFolioFiles = []
      imgFolioIndices.each {  
	imgFolioFiles.add(new File(it))
      }
      dsemgr.tbsImageIndexFiles = imgFolioFiles


      String txtImgStr =  args[2].replaceFirst(/^\[/, "")
      txtImgStr = txtImgStr.replaceFirst(/\]$/, "")
      def txtImgIndices = txtImgStr.split(/,/)
      def txtImgFiles = []
      txtImgIndices.each {  
	txtImgFiles.add(new File(it))
      }
      dsemgr.textImageIndexFiles = txtImgFiles



      String txtFolioStr =  args[3].replaceFirst(/^\[/, "")
      txtFolioStr = txtFolioStr.replaceFirst(/\]$/, "")
      def txtFolioIndices = txtFolioStr.split(/,/)
      def txtFolioFiles = []
      txtFolioIndices.each {  
	txtFolioFiles.add(new File(it))
      }
      dsemgr.textTbsIndexFiles = txtFolioFiles


      File buildDir = new File(args[4])
      //System.out.println "Write reports in " + buildDir



      if (dsemgr.verifyTbs(urn)) {
	System.out.println "All DSE relations are valid."
      } else {
	System.out.println "Errors."
      }




    } catch (Exception  e) {
      System.err.println "Verifier: bad urn value " + args[0]
      throw e
    }
  }
  */
}

