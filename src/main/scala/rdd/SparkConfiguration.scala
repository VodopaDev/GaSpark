package rdd

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkConfiguration {
  private val appName = "GaSpark"
  private val numberOfThreads = Runtime.getRuntime.availableProcessors()
  val sc: SparkConf = new SparkConf()
    .setAppName(appName)
    .setMaster(s"local[$numberOfThreads]")
    .set("spark.executor.memory", "4g")
  val ss: SparkSession = SparkSession.builder().config(sc).getOrCreate()
}
