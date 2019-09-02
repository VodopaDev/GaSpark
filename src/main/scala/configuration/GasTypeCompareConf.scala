package configuration
import org.rogach.scallop.ScallopOption

class GasTypeCompareConf(args: Seq[String]) extends BaseConf(args) {
  val stationType: ScallopOption[String] = opt[String](default = None)
  val gasTypes: ScallopOption[List[String]] = opt[List[String]](default = None, name = "gt")
  verify()
}
