package org.homermultitext.mom

import edu.harvard.chs.cite.CtsUrn
import edu.unc.epidoc.transcoder.TransCoder

class LexicalValidation implements HmtValidation {

  Integer debug = 1


  // map of token URNs to CTS URNs w subref (occurrences)
  //LinkedHashMap tokensMap = [:]
  // map of the same token URNs to boolean (t = valid)
  LinkedHashMap validationMap = [:]
  Integer successes = 0
  Integer failures = 0
  Integer total = 0


  ArrayList authList = []

  
  LexicalValidation(File tokensFile, File authListFile, String morphCmd) {
    //tokensMap = populateTokensMap(tokensFile)
    authList = populateAuthorityList(authListFile)
    computeScores(tokensFile, morphCmd)
  }


  /// methods required to implement interface
  
  boolean validates() {
    return (total == successes)
  }

  Integer successCount() {
    return successes
  }

  Integer failureCount() {
    return failures
  }

  Integer tokensCount() {
    return total
  }

  LinkedHashMap getValidationResults() {
    return validationMap
  }


  /// methods doing the validation work:
  
  void computeScores(File srcFile, String parserCmd)
  throws Exception {
    def lextokens = srcFile.readLines().findAll { l -> l ==~ /.+,urn:cite:hmt:tokentypes.lexical/}
    
    this.total = lextokens.size()

    TransCoder tobeta = new TransCoder()
    tobeta.setConverter("BetaCode")
    tobeta.setParser("Unicode")

    Integer lexCount = 0
    lextokens.each { lex ->
      lexCount++
    
      Integer good = 0
      Integer bad = 0
      def scoreMap = [:]
      def cols = lex.split(/,/)
      if (debug > 0) {println "CtsUrn column[0]: " + cols[0]}
      CtsUrn tokenUrn
      try {
	tokenUrn = new CtsUrn(cols[0])
      } catch (Exception e) {
	System.err.println ("LexicalValidation: failed to make CtsUrn: " + e)
	throw e
      }
      String token = tokenUrn.getSubref()
      String betaToken = tobeta.getString(token.toLowerCase())



      // USE GREEKLANG TO CHECK ISPUCNT
      if ((token.size() < 1) || (token == "⁑")){
	println "${lexCount}: omit punctuation ${token}"
	// NO: make this an punctuation token!

	
      } else if (authList.contains(betaToken)) {

	println "${lexCount}: Byzantine orthography ok: " + token
	validationMap[tokenUrn] = "byz"

      } else {

	def command = "${parserCmd} ${betaToken}"
	print "${lexCount}: Analyzing ${token} with ${command}..."
	def proc = command.execute()
	proc.waitFor()
	def reply = proc.in.text.readLines()

	if (reply[1] ==~ /.*unknown.+/) {
	  validationMap[tokenUrn]  = "fail"
	  fail = fail + 1
	  println " fail."
	} else {
	  validationMap[tokenUrn]  = "success"
	  success = success + 1
	  println " success."
	}
      }

    }
    
  }


  // add error checking...
  ArrayList populateAuthorityList(File srcFile) {
    ArrayList validList = []
    srcFile.eachLine {
      def cols = it.split(/,/)
      validList.add(cols[0])
    }
    return validList
  }
  

  /*
  // read file, return contents as a map
  LinkedHashMap populateTokensMap(File srcFile) {
    LinkedHashMap occurrences = [:]
    def lextokens = srcFile.readLines().findAll { l -> l ==~ /.+,urn:cite:hmt:tokentypes.lexical/}
     lextokens.each { p ->
      def cols = p.split(/,/)
      def lex = cols[1]
      def psg = cols[0]
      if (occurrences[lex]) {
	def psgs = occurrences[lex]
	psgs.add(psg)
	occurrences[lex] =  psgs
      } else {
	occurrences[lex] = [psg]
      }

      if (debug > 0) {
       System.err.println "Lex ${lex} in ${psg} "
       System.err.println "so entry is now " + occurrences[lex]
     }

    }
     return occurrences
  }*/

  
}
