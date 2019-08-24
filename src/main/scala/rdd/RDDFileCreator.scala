package rdd
import DataEntry.{FullDataEntry, GasType, LocationType}
import org.apache.spark.{SparkConf, SparkContext}

import scala.xml.XML

private object RDDFileCreator {

  private val appName = "GaSpark-Create"
  private val numberOfThreads = Runtime.getRuntime.availableProcessors()
  private lazy val conf = new SparkConf()
    .setAppName(appName)
    .setMaster(s"local[$numberOfThreads]")
    .set("spark.executor.memory", "2g")
  val sc = SparkContext.getOrCreate(conf)

  private def createYearlyRDD(year: Int) = {
    val xml = XML.loadFile(s"resources/sanitized/$year.xml")
    val entries = (xml\"pdv").par.flatMap(n =>
      (n\"prix").map(p =>
        FullDataEntry(
          postalCode = (n \@ "cp").toInt,
          locationType = LocationType.fromString(n \@ "pop"),
          price = (p \@ "valeur").toInt,
          gasType = GasType.fromString(p \@ "nom"),
          date = DataEntry.dateFromXML(p \@ "maj"))
      )
    )
    val newRdd = sc.parallelize(entries.seq).cache()
    newRdd.saveAsObjectFile(s"resources/rdd/$year-obj")
    newRdd.saveAsTextFile(s"resources/rdd/$year-txt")
  }

  def main(args: Array[String]): Unit = {
    (2007 to 2019).foreach(createYearlyRDD)
  }
}
