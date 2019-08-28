package configuration

class StationTypeCompareConf(args: Seq[String]) extends BaseConf(args){
  val gasType = opt[String](default = None)
  verify()
}