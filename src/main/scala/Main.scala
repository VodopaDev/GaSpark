import configuration.{GasTypeCompareConf, StationTypeCompareConf}
import query.{GasTypeComparisonQuery, StationTypeComparisonQuery}

object Main extends App {

  val conf = new StationTypeCompareConf(args)
  StationTypeComparisonQuery.computeWithConfig(conf)

}
