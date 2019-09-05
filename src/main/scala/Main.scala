import configuration.{GasTypeCompareConf, StationTypeCompareConf}
import query.{GasTypeComparisonQuery, StationTypeComparisonQuery}

object Main {
  def main(args: Array[String]): Unit = {
    args.toList match {
      case "station" +: tail => StationTypeComparisonQuery.computeWithConfig(new StationTypeCompareConf(tail))
      case "gas" +: tail => GasTypeComparisonQuery.computeWithConfig(new GasTypeCompareConf(tail))
      case _ => println("Use station or gas as your first parameter (use <command> --help for more details)")
    }
  }
}
