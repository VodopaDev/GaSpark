package query

import java.io.{File, PrintWriter}

import configuration.{BaseConf, DefaultValues}
import dataentry.GasDataEntry
import org.apache.spark.rdd.RDD
import query.utils.GranularityEnum
import query.utils.GranularityEnum.Granularity
import rdd.DataSetLoader

trait BaseQuery[Conf <: BaseConf] {
  def computeWithConfig(conf: Conf): Unit

  protected def loadInitialRdd(conf: Conf): RDD[GasDataEntry] = {
    val range = conf.from.getOrElse(DefaultValues.defaultYearFrom) to conf.to.getOrElse(DefaultValues.defaultYearTo)
    DataSetLoader.getRangeDataset(range).rdd
  }

  protected def granularDateString(gran: Granularity, e: GasDataEntry): String =
    gran match {
      case GranularityEnum.YEAR => f"${e.date.year}%04d"
      case GranularityEnum.MONTH => f"${e.date.year}%04d-${e.date.month}%02d"
      case GranularityEnum.DAY => e.date.toString
      case GranularityEnum.ALL | _ => "ALL"
    }
}
