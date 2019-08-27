package query.configuration

class StationTypeCompareConf(args: Seq[String]) extends BaseConf(args){
  val granularity = opt[String](default = None)
  val gasType = opt[String](default = None)
  val postalCodes = opt[List[Int]](default = None)
  verify()
}