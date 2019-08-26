package rdd

import java.text.SimpleDateFormat

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.explode
import com.databricks.spark.xml._
import dataentry.{GasDataEntry, GasType, StationType}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

/**
 * Main object to parse the sanitized XML files into an exploitable RDD
 */
private object RDDFileCreator extends App{

  val appName = "GaSpark-RDD-Creator"
  val numberOfThreads = Runtime.getRuntime.availableProcessors()
  val conf = new SparkConf()
    .setAppName(appName)
    .setMaster(s"local[$numberOfThreads]")
    .set("spark.executor.memory", "4g")
  val sp = SparkSession.builder().config(conf).getOrCreate()
  import sp.implicits._

  /**
   * Parse the XML file containing yearly data into an exploitable RDD
   * @param year year data to parse
   * @param xmlPath path of the XML files
   * @param rddPath destination path of the RDD
   */
  def createYearlyRDD(year: Int, xmlPath: String = "resources/sanitized/", rddPath: String = "resources/rdd/"): Unit = {
    val begin = System.currentTimeMillis()
    sp.read
      .option("rowTag", "pdv")
      .xml(xmlPath + year + ".xml")
      .select("_cp", "_id", "_pop", "prix")
      .withColumn("prix", explode($"prix"))
      .select("_cp", "_id", "_pop", "prix._nom", "prix._valeur", "prix._maj")
      .withColumn("_majtmp", $"_maj".cast(StringType))
      .drop("_maj")
      .withColumnRenamed("_majtmp", "_maj")
      .rdd
      .map { r =>
        val datePrinter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val dateStr = r.getString(r.fieldIndex("_maj"))
        val cutDate = if (dateStr contains 'T') dateStr.split('T')(0)
            else if (dateStr contains ' ') dateStr.split(' ')(0)
            else dateStr

        GasDataEntry(
          r.getLong(r.fieldIndex("_cp")).toInt,
          r.getLong(r.fieldIndex("_id")).toInt,
          StationType.fromString(r.getString(r.fieldIndex("_pop"))),
          GasType.fromString(r.getString(r.fieldIndex("_nom"))),
          r.getLong(r.fieldIndex("_valeur")).toInt,
          datePrinter.parse(cutDate)
        )
      }.saveAsTextFile(rddPath + year)
    val time = System.currentTimeMillis() - begin
    println(s"Creating RDD-$year took ${time}ms")
  }

  (2007 to 2019).foreach(createYearlyRDD(_))
}
