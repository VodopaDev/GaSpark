package query.compare

import dataentry.StationTypeEnum.{HIGHWAY, ROAD, StationType}
import org.apache.spark.rdd.RDD
import query.configuration.StationTypeCompareConf
import query.utils.GranularityEnum
import rdd.DataSetLoader

object StationTypeCompare extends BaseCompare[StationTypeCompareConf] {

  private def filterMapAndSortOnFinalValue(rdd: RDD[((StationType, String), Double)], stationType: StationType) =
    rdd.filter { case ((station, _), _) => station == stationType }
      .map { case ((stationType, date), avg) => (stationType, date, avg) }
      .sortBy(_._2)

  def computeWithConfig(conf: StationTypeCompareConf): Int = {
    val all: RDD[((StationType, String), Double)] =
      DataSetLoader.getRangeDataset(conf.from to conf.to)
        .rdd
        .filter(e => e.gasType == conf.gasType)
        .map(e => {
          val dateStr = conf.granularity match {
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

    highway.zip(road).collect().foreach(t => println(conf.gasType.name + t))
    road.count().toInt
  }


}
