package configuration
import org.rogach.scallop.ScallopOption

class StationTypeCompareConf(args: Seq[String]) extends BaseConf(args){
  val gasType: ScallopOption[String] = opt[String](default = None)
  verify()
}