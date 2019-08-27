package conftypes

import org.rogach.scallop.ScallopConf

class StationTypeCompareConf(args: Seq[String]) extends ScallopConf(args){
  val from = opt[Int](default = Some(2007))
  val to = opt[Int](default = Some(2019))
  val postalCodes = None
}