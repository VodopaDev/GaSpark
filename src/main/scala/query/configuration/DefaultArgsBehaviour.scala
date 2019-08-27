package query.configuration

import dataentry.GasTypeEnum
import dataentry.GasTypeEnum.GasType
import org.rogach.scallop.ScallopOption
import query.utils.GranularityEnum
import query.utils.GranularityEnum.Granularity

object DefaultArgsBehaviour {

  def getGranularityOrDefault(opt: ScallopOption[String]): Granularity = opt.get match {
    case Some(value) => GranularityEnum.fromString(value)
    case None => GranularityEnum.ALL
  }

  def getFromOrDefault(opt: ScallopOption[Int]): Int = opt.get match {
    case Some(value) => value
    case None => 2007
  }

  def getToOrDefault(opt: ScallopOption[Int]): Int = opt.get match {
    case Some(value) => value
    case None => 2019
  }

  def getGasTypeOrDefault(opt: ScallopOption[String]): GasType  = opt.get match {
    case Some(value) => GasTypeEnum.fromString(value)
    case None => GasTypeEnum.GAZOLE
  }
}
