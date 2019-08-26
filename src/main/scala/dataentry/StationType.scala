package dataentry

/**
 * Simple enumeration to define all the possible types of selling spot of a gas station
 */
object StationType extends Enumeration {
  type StationType = Value
  val AUTOROUTE: StationType = Value("A")
  val ROUTE: StationType = Value("R")
  val UNDEFINED: StationType = Value("U")

  private final val enums = this.values.toVector.map(t => (t.toString.toLowerCase, t)).toMap
  def fromString(str: String): StationType = enums.get(str.toLowerCase) match {
    case Some(value) => value
    case None => UNDEFINED
  }
}
