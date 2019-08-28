import configuration.StationTypeCompareConf
import query.StationTypeComparisonQuery

object Main extends App {

  val conf = new StationTypeCompareConf(args)
  StationTypeComparisonQuery.computeWithConfig(conf)

}
