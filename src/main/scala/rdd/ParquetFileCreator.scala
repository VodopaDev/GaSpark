package rdd

import java.text.SimpleDateFormat

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.explode
import com.databricks.spark.xml._
import dataentry.{Date, GasDataEntry, GasTypeEnum, StationTypeEnum}
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}
import org.apache.spark.sql.types._

/**
 * Main object to parse the sanitized XML files into an exploitable RDD
 */
//noinspection SpellCheckingInspection
private object ParquetFileCreator extends App{

  val appName = "GaSpark-RDD-Creator"
  val numberOfThreads = Runtime.getRuntime.availableProcessors()
  val conf = new SparkConf()
    .setAppName(appName)
    .setMaster(s"local[$numberOfThreads]")
    .set("spark.executor.memory", "4g")
  val sp = SparkSession.builder().config(conf).getOrCreate()
  import sp.implicits._

  (2007 to 2019).foreach(createYearlyDataSet(_))

  /**
   * Parse the XML file containing yearly data into an easily parsable DataSet
   * @param year year data to parse
   * @param xmlPath path of the XML files
   * @param dsPath destination path of the RDD
   */
  def createYearlyDataSet(year: Int, xmlPath: String = "resources/sanitized/", dsPath: String = "resources/dataset/"): Unit = {
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
      .map { r =>
        GasDataEntry(
          r.getLong(r.fieldIndex("_cp")).toInt,
          r.getLong(r.fieldIndex("_id")).toInt,
          StationTypeEnum.fromString(r.getString(r.fieldIndex("_pop"))),
          GasTypeEnum.fromString(r.getString(r.fieldIndex("_nom"))),
          r.getLong(r.fieldIndex("_valeur")).toInt,
          Date(r.getString(r.fieldIndex("_maj")))
        )
      }
      .filter(e => isValidGasEntry(e))
      .write
      .parquet(dsPath + year)
    val time = System.currentTimeMillis() - begin
    println(s"Creating RDD-$year took ${time}ms")
  }

  /**
   * Check if a GasDataEntry is valid
   * @param e entry to check
   * @return true if it is valid, false otherwise
   */
  private def isValidGasEntry(e: GasDataEntry): Boolean = {
    (e.date < Date(2020,0,0) && e.date > Date(2006,12,31)) &&
      e.price > 300 &&
      e.gasType != GasTypeEnum.UNDEFINED&&
      e.stationType != StationTypeEnum.UNDEFINED &&
      e.postalCode > 0 &&
      e.sellerId > 0
  }

}
