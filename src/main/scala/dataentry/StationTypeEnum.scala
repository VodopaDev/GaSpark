package dataentry

/**
 * Simple enumeration to define all the possible types of selling spot of a gas station
 */
object StationTypeEnum {

  sealed case class StationType(name: String){
    override def toString: String = name
  }
  object HIGHWAY extends StationType("A")
  object ROAD extends StationType("R")
  object UNDEFINED extends StationType("U")

  private final val values = Vector(HIGHWAY,ROAD).map(e => (e.name.toLowerCase, e)).toMap

  /**
   * Return a StationType from a string
   * @param str string
   * @return according StationType if it exists, Undefined otherwise
   */
  def fromString(str: String): StationType = values.get(str.toLowerCase) match {
    case Some(value) => value
    case None => UNDEFINED
  }
}
