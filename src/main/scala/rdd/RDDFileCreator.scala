package rdd
import java.io.PrintWriter

import dataentry.{GasDataEntry, GasType, StationType}
import org.apache.spark.{SparkConf, SparkContext}

import scala.xml.{Elem, Node, XML}

private object RDDFileCreator extends App{

  def createYearlyRDD(year: Int): Unit = {
    val start = System.currentTimeMillis()
    val dst = new PrintWriter(s"resources/rdd/$year")

    def isValid(e: GasDataEntry): Boolean = {
      (e.gasType != GasType.UNDEFINED) &&
        (e.stationType != StationType.UNDEFINED) &&
        (e.price >= 300)
    }

    (XML.loadFile(s"resources/sanitized/$year.xml") \ "pdv").toIterator
      .foreach(n => {
        val department = (n \@ "cp")
        val seller = (n \@ "id")
        val roadType = (n \@ "pop")
        (n \ "prix").foreach(p => {
          val entry = GasDataEntry.fromStringArguments(
            seller,
            department,
            roadType,
            (p \@ "nom"),
            (p \@ "valeur"),
            (p \@ "maj"))
          if (isValid(entry)) dst.println(entry)
        })

    })
    println(s"Year $year took " + (System.currentTimeMillis() - start) + "ms to complete")
  }

  (2018 to 2018).foreach(createYearlyRDD)

}
