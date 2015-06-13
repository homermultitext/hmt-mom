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
  
  void computeScores(File srcFile, String parserCmd) {

    def lextokens = srcFile.readLines().findAll { l -> l ==~ /.+,urn:cite:hmt:tokentypes.lexical/}
    this.total = lextokens.size()

    TransCoder tobeta = new TransCoder()
    tobeta.setConverter("BetaCode")
    tobeta.setParser("Unicode")

    Integer lexCount = 0
    lextokens.each { lex ->
      boolean urnOk = false
      lexCount++
    
      Integer good = 0
      Integer bad = 0
      def scoreMap = [:]
      def cols = lex.split(/,/)

      CtsUrn tokenUrn
      String token
      String betaToken
      try {
	tokenUrn = new CtsUrn(cols[0])
	token = tokenUrn.getSubref()
	betaToken = tobeta.getString(token.toLowerCase())
	urnOk = true
	
      } catch (Exception e) {
	System.err.println ("LexicalValidation: failed to make CtsUrn: " + e)
      }


      // USE GREEKLANG TO CHECK ISPUCNT
      //

      // report chain:
      if (! urnOk) {
	System.err.println "${lexCount}: invalid URN value " + tokenUrn
	
      } else if ((token.size() < 1) || (token == "⁑")) {
	System.err.println "${lexCount}: need to create punctuation token ${token}"
	// NO: make this an punctuation token!

	
      } else if (authList.contains(betaToken)) {
	System.err.println "${lexCount}: Byzantine orthography ok: " + token
	validationMap[tokenUrn] = "byz"

      } else {

	def command = "${parserCmd} ${betaToken}"
	System.err.print "${lexCount}: Analyzing ${token} with ${command}..."
	def proc = command.execute()
	proc.waitFor()
	def reply = proc.in.text.readLines()

	if (reply[1] ==~ /.*unknown.+/) {
	  validationMap[tokenUrn]  = "fail"
	  failures = failures + 1
	  System.err.println " fail."
	} else {
	  validationMap[tokenUrn]  = "success"
	  successes = successes + 1
	  System.err.println " success."
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

  
}
