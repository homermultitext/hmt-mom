package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test

// tokenization *system*
class TestTokenSystem extends GroovyTestCase {

  File inputFile = new File("testdata/tabs/Iliad-3lns.txt")
  String separatorStr = "#"

  void testTokenizing() {
    HmtGreekTokenization toker = new HmtGreekTokenization()
    ArrayList results = toker.tokenize( inputFile,  separatorStr) 
    results.each { pr ->
      println "${pr[0]} -> ${pr[1]}"
    }
  }
  
}