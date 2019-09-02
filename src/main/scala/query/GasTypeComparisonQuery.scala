package query

import configuration.{DefaultValues, GasTypeCompareConf}
import dataentry.GasTypeEnum.UNDEFINED
import dataentry.{GasTypeEnum, StationTypeEnum}
import query.utils.ExtendedDataFrame.Implicits
import query.utils.{GranularityEnum}
import rdd.DataSetLoader
import rdd.SparkConfiguration.ss.implicits._


object GasTypeComparisonQuery extends BaseQuery[GasTypeCompareConf] {

  override def computeWithConfig(conf: GasTypeCompareConf): Unit = {
    val range = conf.from.getOrElse(DefaultValues.defaultYearFrom) to conf.to.getOrElse(DefaultValues.defaultYearTo)
    val gran = GranularityEnum.fromString(conf.granularity.getOrElse(DefaultValues.defaultGranularity.toString))
    val gasTypes = conf.gasTypes.toOption match {
      case Some(value) => value.map(GasTypeEnum.fromString).filter(_ != UNDEFINED).distinct
      case None => GasTypeEnum.values
    }

    val all = DataSetLoader.getRangeDataset(range).rdd

    val roadFilter = conf.stationType.toOption match {
      case Some(value) => all.filter(e => e.stationType == StationTypeEnum.fromString(value))
      case None => all
    }

    val avgMap = roadFilter.map(e => {
        val dateStr = granularDateString(gran, e)
        ((e.gasType, dateStr), (e.price.toLong, 1))
      })
      .reduceByKey { case ((t1, c1), (t2, c2)) => (t1 + t2, c1 + c2) }
      .mapValues { case (t, c) => t / c.toDouble }
      .cache()


    val joinResult = gasTypes.map(gt => avgMap.filter(_._1._1 == gt)
      .map(e => (e._1._2, e._2)).toDF("Date", gt.toString))
      .reduce((df1, df2) => df1.natjoin(df2)).cache()
    joinResult.saveWithConf(conf)
  }

}
