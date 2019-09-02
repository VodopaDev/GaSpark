package configuration
import org.rogach.scallop.ScallopOption

class SimpleConf(args: Seq[String]) extends BaseConf(args){
  val stationType: ScallopOption[String] = opt[String](default = None)
  val gasType: ScallopOption[String] = opt[String](default = None)
}
