package dataentry

/**
 * Simple enumeration to define all the possible types of selling spot of a gas station
 */
object StationType {

  sealed case class StationTypeValue(name: String){
    override def toString: String = name
  }
  object Highway extends StationTypeValue("A")
  object Road extends StationTypeValue("R")
  object Undefined extends StationTypeValue("U")

  private final val values = Vector(Highway,Road,Undefined).map(e => (e.name.toLowerCase, e)).toMap

  /**
   * Return a StationType from a string
   * @param str string
   * @return according StationType if it exists, Undefined otherwise
   */
  def fromString(str: String): StationTypeValue = values.get(str.toLowerCase) match {
    case Some(value) => value
    case None => Undefined
  }
}
