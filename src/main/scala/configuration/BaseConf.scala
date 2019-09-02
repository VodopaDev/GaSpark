package configuration

import org.rogach.scallop.{ScallopConf, ScallopOption}

abstract class BaseConf(args: Seq[String]) extends ScallopConf(args){
  val from: ScallopOption[Int] = opt[Int](name = "from", short = 'f', descr = "lower bound for the data analysis (default 2007)", argName = "year", default = Some(DefaultValues.defaultYearFrom))
  val to: ScallopOption[Int] = opt[Int](name = "to", short = 't', descr = "upper bound for the data analysis (default 2019)", argName = "year", default = Some(DefaultValues.defaultYearTo))
  val granularity: ScallopOption[String] = opt[String](short = 'g', default = None)
  val verbose: ScallopOption[Boolean] = toggle(name = "verbose", short = 'v' ,default = Some(DefaultValues.defaultVerbose))
  val savePath: ScallopOption[String] = opt[String](name = "save",short = 's', descr = "path to save the result", argName = "path+name", default = None)
}
