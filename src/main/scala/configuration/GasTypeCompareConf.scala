package configuration

class GasTypeCompareConf(args: Seq[String]) extends BaseConf(args) {
  val stationType = opt[String](default = None)
  verify()
}
