package query.compare

import dataentry.GasTypeEnum
import dataentry.StationTypeEnum.{HIGHWAY, ROAD, StationType}
import org.apache.spark.rdd.RDD
import query.configuration.StationTypeCompareConf
import query.utils.GranularityEnum
import rdd.DataSetLoader
import query.utils.DefaultArgs._

object StationTypeCompare extends BaseCompare[StationTypeCompareConf] {

  private def filterMapAndSortOnFinalValue(rdd: RDD[((StationType, String), Double)], stationType: StationType) =
    rdd.filter { case ((station, _), _) => station == stationType }
      .map { case ((stationType, date), avg) => (stationType, date, avg) }
      .sortBy(_._2)

  def computeWithConfig(conf: StationTypeCompareConf): Int = {
    val from = getFromOrDefault(conf.from)
    val to = getToOrDefault(conf.to)
    val granularity = getGranularityOrDefault(conf.granularity)
    val gasType = getGasTypeOrDefault(conf.gasType)

    val all: RDD[((StationType, String), Double)] =
      DataSetLoader.getRangeDataset(from to to)
        .rdd
        .filter(e => e.gasType == gasType)
        .map(e => {
          val dateStr = granularity match {
            case GranularityEnum.YEAR => f"${e.date.year}%04d"
            case GranularityEnum.MONTH => f"${e.date.year}%04d-${e.date.month}%02d"
            case GranularityEnum.DAY => e.date.toString
            case GranularityEnum.ALL | _ => "ALL"
          }
          ((e.stationType, dateStr), (e.price.toLong, 1))
        })
        .reduceByKey { case ((t1, c1), (t2, c2)) => (t1 + t2, c1 + c2) }
        .mapValues { case (t, c) => t / c.toDouble }
        .cache()

    val highway = filterMapAndSortOnFinalValue(all, HIGHWAY)
    val road = filterMapAndSortOnFinalValue(all, ROAD)

    highway.zip(road).collect().foreach(t => println(gasType.name + t))
    1
  }


}
