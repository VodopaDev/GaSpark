package configuration

class SimpleConf(args: Seq[String]) extends BaseConf(args){
  val stationType = opt[String](default = None)
  val gasType = opt[String](default = None)
}
