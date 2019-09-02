package query

import configuration.{DefaultValues, StationTypeCompareConf}
import dataentry.GasTypeEnum
import dataentry.StationTypeEnum.{HIGHWAY, ROAD, StationType}
import org.apache.spark.rdd.RDD
import query.utils.GranularityEnum
import org.apache.spark.sql.DataFrame
import query.utils.{GranularityEnum, ExtendedDataFrame}
import rdd.DataSetLoader
import rdd.SparkConfiguration.ss.implicits._

import query.utils.ExtendedDataFrame.Implicits

object StationTypeComparisonQuery extends BaseQuery[StationTypeCompareConf] {

  private def filterMapAndSortOnDate(rdd: RDD[((StationType, String), Double)], stationType: StationType) =
    rdd.filter { case ((station, _), _) => station == stationType }
      .map { case ((_, date), avg) => (date, avg) }
      .sortBy(_._2)
      .toDF("Date", stationType.toString)

  def computeWithConfig(conf: StationTypeCompareConf): Unit = {
    val all = loadInitialRdd(conf)
    val gran = GranularityEnum.fromString(conf.granularity.getOrElse(DefaultValues.defaultGranularity.toString))

    val gasFilter = conf.gasType.toOption match {
      case Some(value) => all.filter(e => e.gasType == GasTypeEnum.fromString(value))
      case None => all
    }

    val avgMap = gasFilter.map(e => {
        val dateStr = granularDateString(gran, e)
        ((e.stationType, dateStr), (e.price.toLong, 1))
      })
      .reduceByKey { case ((t1, c1), (t2, c2)) => (t1 + t2, c1 + c2) }
      .mapValues { case (t, c) => t / c.toDouble }
      .cache()

    val avgHighway = filterMapAndSortOnDate(avgMap, HIGHWAY)
    val avgRoad = filterMapAndSortOnDate(avgMap, ROAD)
    avgHighway.natjoin(avgRoad).saveWithConf(conf)
  }

}
