package query.configuration

import org.rogach.scallop.ScallopConf

abstract class BaseConf(args: Seq[String]) extends ScallopConf(args){
  val from = opt[Int](name = "from", short = 'f', descr = "lower bound for the data analysis (default 2007)", argName = "year")
  val to = opt[Int](name = "to", short = 't', descr = "upper bound for the data analysis (default 2019)", argName = "year")
  val resultPath = opt[String](name = "save",short = 's', descr = "path to save the result", argName = "path")
}
