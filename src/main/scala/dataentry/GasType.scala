package dataentry

/**
 * Simple enumeration to define all the possible types of gas sold in France
 */
object GasType extends Enumeration {
  type GasType = Value
  val GAZOLE: GasType = Value("Gazole")
  val GPLc: GasType = Value("GPLc")
  val E10: GasType = Value("E10")
  val E85: GasType = Value("E85")
  val SP95: GasType = Value("SP95")
  val SP98: GasType = Value("SP98")
  val UNDEFINED: GasType = Value("Undef")

  private final val enums = this.values.toVector.map(t => (t.toString.toLowerCase, t)).toMap
  def fromString(str: String): GasType = enums.get(str.toLowerCase) match {
    case Some(value) => value
    case None => UNDEFINED
  }
}