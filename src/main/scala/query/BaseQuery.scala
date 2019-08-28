package query

import configuration.BaseConf

trait BaseQuery[Conf <: BaseConf] {
  def computeWithConfig(conf: Conf): Unit
}
