package org.homermultitext.mom

import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.hocuspocus.Corpus

class HmtTokenizer {

    def debug = 2

       
    File tabulatedDir
    File tokensFile
    String separator


    // Static string definitions
    /** String value of URN for lexical token collection.*/
    String lexurnbase = "urn:cite:perseus:lextoken"
    /** String value of URN for numeric token collection.*/
    String numurnbase = "urn:cite:hmt:numerictoken"
    /** String value of URN for HMT named entity collection.*/
    String nameurnbase = "urn:cite:hmt:namedentitytoken"
    /** String value of URN for HMT  literal string collection.*/
    String literalurnbase = "urn:cite:hmt:literaltoken"
    /** String value of URN for HMT  label token collection.*/
    String labelurnbase = "urn:cite:hmt:labeltoken"



    /** Constructor with explicit values for all required settings.
    *  @param tabsDir Directory with .txt files in hocuspocus tabulated format.
    *  @param outputFile Writable file where RDF statements in TTL format will be
    *  written.
    *  @param fieldBreak String value used as separator for fields of hocuspocus
    * tabulated file.
    */
    HmtTokenizer(File tabsDir, File outputFile, String fieldBreak) {
        this.tabulatedDir = tabsDir
        this.tokensFile = outputFile
        this.separator = fieldBreak
    }



    /**
    */
    void tokenizeTabs() {
        HmtGreekTokenization tokenSystem = new HmtGreekTokenization()
        tokensFile.setText("")

        def tabList = tabulatedDir.list({d, f-> f ==~ /.*.txt/ } as FilenameFilter )?.toList() 
        tabList.each { f ->
            File tabFile = new File(tabulatedDir,f)
            if (debug > 0) { System.err.println "TOKENIZE TAB FILE " + tabFile }
            tokenSystem.tokenize(tabFile, separator).each { tokenPair ->
	      
	      String rawCts = tokenPair[0]
	      String analysis = tokenPair[1]

	      CtsUrn urn
	      String ctsval =  rawCts.replaceAll(/\n/,"")
	      try {
		urn = new CtsUrn(ctsval)
	      } catch (Exception e) {
		System.err.println "HmtTokenzier, tokenizeTabs:  unable to make URN from ${ctsval} in pair ${tokenPair}"
	      }


	      def checkVal = "${urn.getUrnWithoutPassage()}:${urn.getRef()}"
	      String subref = urn.getSubref1()

	      //  HmtGreekTokenization is a white-space tokenizer that 
	      //  keeps punctuation.  For analysis, we will throw out punctuation 
	      //  characters.  ·
		if (subref)  {
		  subref = subref.replaceAll(/^[\(\[]/,"")
		  subref = subref.replaceAll(/[.,;?·]$/, "")
		  String trimmed = trimWord(subref)
                        
		  // some analyses carry an id as well as a type:
		  def parts = analysis.split(":")
		  System.err.println "${checkVal}, ${subref}, ${analysis}"
		  System.err.println "\tyields ${parts}"
		  /*
		  switch (tokenPair[1]) {
                            case "urn:cite:hmt:tokentypes.lexical":
                                tokensFile.append("${lexurnbase}.${trimmed} lex:occursIn ${ctsval} .\n", "UTF-8")
                            break
                        
                            case "urn:cite:hmt:tokentypes.numeric":
                                tokensFile.append("${numurnbase}.${trimmed} lex:occursIn ${ctsval} .\n", "UTF-8")
                            break

                            case "urn:cite:hmt:tokentypes.namedEntity":
                                tokensFile.append("${nameurnbase}.${parts[1]} lex:occursIn ${ctsval} .\n", "UTF-8")
                            break

                        
                            case ":cite:hmt:tokentypes.waw":
                                tokensFile.append("${literalurnbase}.${trimmed} lex:occursIn ${ctsval} .\n", "UTF-8")
                            break
                        
                            default : 
                                System.err.println "Unrecognized token type: ${tokenPair[1]} of class ${tokenPair[1].getClass()}"
                            break
                        }


	      */
		
		} else {
		  System.err.println "HmtTokenizer, tokenizeTabs:  null subref in pair ${tokenPair}"
		}
            } 
        }
    }



    /** Removes 'ancient markup' from a StringBuffer, 
    * that is, non-whitespace characters that are valid in 
   * an HMT text, but not part of tokens (such as
    * puncutation.
    * @param s The text to trim.
    * @returns The content of the StringBuffer,
    * as a String, with inappropriate Unicode codepoints removed, 
    */
    String trimWord(String s) {
        // convert String to UTF-8 StringBuffer
        StringBuffer buff = new StringBuffer()
        if (s == null) {
            return null
        }

        byte[] byteArray = s.getBytes("UTF-8")
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray)
        InputStreamReader isr = new InputStreamReader(bais,"UTF8")
        Reader inputReader = new BufferedReader(isr)
        int ch
        while ((ch = inputReader.read()) > -1) {
            buff.append((char) ch);
        }
        inputReader.close()

        StringBuffer trimmed = new StringBuffer()
        if (buff.size() < 1) {
            return ""
        }
        int max = buff.codePointCount(0, buff.size() - 1)
        int idx = 0
        while (idx < max) {
            int cp = buff.codePointAt(idx)
            if (cp != null) {
                if (cp in HmtDefs.puncChars) {
                } else {
                    trimmed.append( new String(Character.toChars(cp)))
                }
            }
            idx = buff.offsetByCodePoints(idx,1)	
        }
        // get last char:
        int cp = buff.codePointAt(max)
        if (cp in HmtDefs.puncChars) {
        } else {
            trimmed.append( new String(Character.toChars(cp)))
        }
        return trimmed.toString()
    }

}

