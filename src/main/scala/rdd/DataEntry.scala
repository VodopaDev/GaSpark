package rdd


import rdd.DataEntry.GasType.GasType
import rdd.DataEntry.LocationType.LocationType

object DataEntry {
  def dataEntryFromString(s: String): FullDataEntry = {
    s.split(' ').toList match {
      case (postal +: location +: price +: gasType +: date +: tail) =>
        FullDataEntry(
          postal.toInt,
          LocationType.fromString(location),
          price.toInt,
          GasType.fromString(gasType),
          dateFromTextFile(date)
        )
    }
  }
  case class FullDataEntry(postalCode: PostalCode,
                           locationType: LocationType,
                           price: Price,
                           gasType: GasType,
                           date: Date)

  type Price = Int
  type PostalCode = Int

  case class Date(year: Int, month: Int, day: Int){
    override def toString: String = s"$year-$month-$day"
  }

  def dateFromXML(xml: String): Date = {
    val split = xml.split(' ')(0).split('-')
    Date(split(0).toInt, split(1).toInt, split(2).toInt)
  }

  def dateFromTextFile(s: String): Date = {
    val split = s.split('-')
    Date(split(0).toInt, split(1).toInt, split(2).toInt)
  }

  object GasType extends Enumeration {
    type GasType = Value
    val GAZOLE: GasType = Value("Gazole")
    val GPLc: GasType = Value("GPLc")
    val E10: GasType = Value("E10")
    val E85: GasType = Value("E85")
    val SP95: GasType = Value("SP95")
    val SP98: GasType = Value("SP98")
    val UNDEFINED: GasType = Value("undefined")

    private val enums = this.values.toVector.map(t => (t.toString.toLowerCase, t)).toMap
    def fromString(str: String): GasType = enums.get(str.toLowerCase) match {
      case Some(value) => value
      case None => UNDEFINED
    }
  }

  object LocationType extends Enumeration {
    type LocationType = Value
    val AUTOROUTE: LocationType = Value("Autoroute")
    val ROUTE: LocationType = Value("Route")
    val UNDEFINED: LocationType = Value("undefined")

    def fromString(str: String): LocationType = str match {
      case "A" | "Autoroute" => AUTOROUTE
      case "R" | "Route" => ROUTE
      case _ => UNDEFINED
    }
  }
}