package query.compare

import query.configuration.BaseConf

trait BaseCompare[Conf <: BaseConf] {
  def computeWithConfig(conf: Conf): Int
  def computeWithConfigAndSave(conf: Conf): Unit = {

  }
}
