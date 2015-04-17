package org.homermultitext.mom


import static org.junit.Assert.*
import org.junit.Test

class TestTokenizer extends GroovyTestCase {

  File tabsDir = new File("testdata/tabs")
  File outputFile = new File("testdata/out/tokens.txt")
  String separatorStr = "#"

  
  void testFullCorpusTokenizer() {
    //    HmtTokenizer tokenizer = new HmtTokenizer(tabsDir, outputFile, separatorStr)
    // tokenizer.tokenizeTabs()
  }
  
}