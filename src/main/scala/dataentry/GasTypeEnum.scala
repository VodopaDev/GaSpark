package dataentry

/**
 * Simple enumeration to define all the possible types of gas sold in France
 */
object GasTypeEnum {

  sealed case class GasType(name: String){
    override def toString: String = name
  }
  object GAZOLE extends GasType("Gazole")
  object GPLc extends GasType("GPLc")
  object E10 extends GasType("E10")
  object E85 extends GasType("E85")
  object SP95 extends GasType("SP95")
  object SP98 extends GasType("SP98")
  object UNDEFINED extends GasType("U")

  private final val values = Vector(GAZOLE,GPLc,E10,E85,SP95,SP98).map(e => (e.name.toLowerCase, e)).toMap

  /**
   * Return a GasType from a string
   * @param str string
   * @return according GasType if it exists, UNDEFINED otherwise
   */
  def fromString(str: String): GasType = values.get(str.toLowerCase) match {
    case Some(value) => value
    case None => UNDEFINED
  }
}