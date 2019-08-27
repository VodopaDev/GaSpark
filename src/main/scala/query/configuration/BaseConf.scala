package query.configuration

import org.rogach.scallop.ScallopConf

abstract class BaseConf(args: Seq[String]) extends ScallopConf(args){
  private val _from = opt[Int](name = "from", short = 'f', descr = "lower bound for the data analysis (default 2007)", argName = "year")
  private val _to = opt[Int](name = "to", short = 't', descr = "upper bound for the data analysis (default 2019)", argName = "year")
  val resultPath = opt[String](name = "save",short = 's', descr = "path to save the result", argName = "path", default = None)

  lazy val from = DefaultArgsBehaviour.getFromOrDefault(_from)
  lazy val to = DefaultArgsBehaviour.getToOrDefault(_to)
}
