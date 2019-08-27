package query.configuration

class StationTypeCompareConf(args: Seq[String]) extends BaseConf(args){
  private val _granularity = opt[String](default = None)
  private val _gasType = opt[String](default = None)
  val postalCodes = opt[List[Int]](default = None)

  lazy val granularity = DefaultArgsBehaviour.getGranularityOrDefault(_granularity)
  lazy val gasType = DefaultArgsBehaviour.getGasTypeOrDefault(_gasType)
  verify()
}