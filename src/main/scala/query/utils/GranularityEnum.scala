package query.utils

/**
 * Simple enumeration to define all the possible granularity types
 */
object GranularityEnum {

  sealed case class Granularity(name: String){
    override def toString: String = name
  }
  object YEAR extends Granularity("year")
  object MONTH extends Granularity("month")
  object DAY extends Granularity("day")
  object ALL extends Granularity("all")

  private final val values = Vector(YEAR,MONTH,DAY,ALL).map(e => (e.name.toLowerCase, e)).toMap

  /**
   * Return a granularity from a string
   * @param str string
   * @return according granularity if it exists, ALL otherwise
   */
  def fromString(str: String): Granularity = values.get(str.toLowerCase) match {
    case Some(value) => value
    case None => ALL
  }
}