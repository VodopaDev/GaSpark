package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import rdd.DataEntry.FullDataEntry

object RDDLoader {
  private val appName = "GaSpark"
  private val numberOfThreads = Runtime.getRuntime.availableProcessors()
  private val conf = new SparkConf()
    .setAppName(appName)
    .setMaster(s"local[$numberOfThreads]")
    .set("spark.executor.memory", "2g")
  val sc: SparkContext = SparkContext.getOrCreate(conf)

  def main(args: Array[String]): Unit = {
    //prefer txt
  }

}
