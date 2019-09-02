package configuration

import dataentry.GasTypeEnum.{GAZOLE, GasType}
import dataentry.StationTypeEnum.{ROAD, StationType}
import query.utils.GranularityEnum.{ALL, Granularity}

object DefaultValues {
  val defaultGasType: GasType = GAZOLE
  val defaultStationType: StationType = ROAD
  val defaultGranularity: Granularity = ALL

  val defaultYearFrom = 2007
  val defaultYearTo = 2019
  val defaultYearRange: Range.Inclusive = defaultYearFrom to defaultYearTo

  val defaultVerbose = false
  val defaultSavePath = "result/"
  val defaultFileName = "result.txt"
}
