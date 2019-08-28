package query

import java.io.{File, PrintWriter}

import configuration.{DefaultValues, StationTypeCompareConf}
import dataentry.GasTypeEnum
import dataentry.StationTypeEnum.{HIGHWAY, ROAD, StationType}
import org.apache.spark.rdd.RDD
import query.utils.GranularityEnum
import rdd.DataSetLoader

object StationTypeComparisonQuery extends BaseQuery[StationTypeCompareConf] {

  private def filterMapAndSortOnFinalValue(rdd: RDD[((StationType, String), Double)], stationType: StationType) =
    rdd.filter { case ((station, _), _) => station == stationType }
      .map { case ((stationType, date), avg) => (stationType, date, avg) }
      .sortBy(_._2)

  def computeWithConfig(conf: StationTypeCompareConf): Unit = {
    val range = conf.from.getOrElse(DefaultValues.defaultYearFrom) to conf.to.getOrElse(DefaultValues.defaultYearTo)
    val granularity = GranularityEnum.fromString(conf.granularity.getOrElse(DefaultValues.defaultGranularity.toString))

    val all = DataSetLoader.getRangeDataset(range).rdd

    val gasFilter = conf.gasType.get match {
      case Some(value) => all.filter(e => e.gasType == GasTypeEnum.fromString(value))
      case None => all
    }

    val avgMap = gasFilter.map(e => {
            val dateStr = granularity match {
            case GranularityEnum.YEAR => f"${e.date.year}%04d"
            case GranularityEnum.MONTH => f"${e.date.year}%04d-${e.date.month}%02d"
            case GranularityEnum.DAY => e.date.toString
            case GranularityEnum.ALL | _ => "ALL"
          }
          ((e.stationType, dateStr), (e.price.toLong, 1))})
      .reduceByKey { case ((t1, c1), (t2, c2)) => (t1 + t2, c1 + c2) }
      .mapValues { case (t, c) => t / c.toDouble }
      .cache()

    val avgHighway = filterMapAndSortOnFinalValue(avgMap, HIGHWAY)
    val avgRoad = filterMapAndSortOnFinalValue(avgMap, ROAD)
    val avgHighwayVsRoad = avgHighway.zip(avgRoad)
      .map{ case (h, r) => s"${h._2} ${h._1} ${h._3} ${r._1} ${r._3}"}
      .collect()

    avgHighwayVsRoad.foreach(println)

    conf.savePath.get match {
      case Some(path) =>
        val writer = new PrintWriter(new File(path))
        avgHighwayVsRoad.foreach(line => writer.println(line))
        writer.close()
      case None =>
    }
  }

}
