package org.homermultitext.hmtmom

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.citerelation._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.dse._
import org.homermultitext.edmodel._
import org.homermultitext.hmtcexbuilder._


import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._

import scala.io.Source



case class AuthorityValue(urn: Cite2Urn, label: String)
/**
*/
case class NamedEntityReporter(pg: Cite2Urn, tkns :  Vector[TokenAnalysis]) {


  val people = tkns.filter(_.analysis.lexicalDisambiguation ~~ Cite2Urn("urn:cite2:hmt:pers:"))
  val places = tkns.filter(_.analysis.lexicalDisambiguation ~~ Cite2Urn("urn:cite2:hmt:place:"))

  val namesList = namesAuthority.toVector

  def namesAuthority = {//}: Array[AuthorityValue] = {
    val lines = Source.fromURL("https://raw.githubusercontent.com/homermultitext/hmt-authlists/master/data/hmtnames.cex").getLines.toVector.drop(2)

    val auths = for (ln <- lines) yield {
      val cols = ln.split("#")

    }
    auths
  }

  /** Add "s" to plurals referring to a collection.
  *
  * @param v Vector to check for singular or plural.
  */
  def plural[T](v: Vector[T]): String = {
    if (v.size == 1) {
      ""
    } else{
      "s"
    }
  }

  /** Compose markdown for report on validation.
  */
  def validation: String = {""}

  /** Compose markdown for verification.
  */
  def verification: String ={
    val bldr = StringBuilder.newBuilder
    bldr.append(s"\n\n## Human verification of named entity identification for ${pg.collection}, page ${pg.objectComponent}\n\n## Persons\n\n")

    val dudes = people.map(tk => (tk.analysis.lexicalDisambiguation, Occurrence(tk.analysis.editionUrn, tk.analysis.readWithDiplomatic)))
    val grouped = dudes.groupBy(_._1)
    for (k <- grouped.keySet) {
      val groupedDudes = grouped(k).map(_._2)
      bldr.append(s"\n**${k}** - ${groupedDudes.size} passage${plural(groupedDudes)}\n\n")

      for (occurrence <- groupedDudes) {
        println(occurrence)
        bldr.append(s"-  ${occurrence.something} (${occurrence.urn})\n")
      }
    }
    bldr.toString
  }

}
