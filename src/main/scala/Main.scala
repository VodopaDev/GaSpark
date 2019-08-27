import query.compare.StationTypeCompare
import query.configuration.StationTypeCompareConf

object Main extends App {

  val conf = new StationTypeCompareConf(args)
  println(StationTypeCompare.computeWithConfig(conf))

}
