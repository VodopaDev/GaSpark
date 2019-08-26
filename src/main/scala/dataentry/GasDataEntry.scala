package dataentry
import dataentry.GasDataEntry.{PostalCode, Price, SellerID}
import dataentry.GasTypeEnum.GasType
import dataentry.StationTypeEnum.StationType
import org.apache.spark.sql.Row

/**
 * Case class to represent a selling entry in some gas station
 * @param sellerId seller id in the provided data
 * @param postalCode department where the gas is sold
 * @param stationType road type on which the gas station sells gas
 * @param gasType gas type sold
 * @param price price of the gas type
 * @param date date at which the gas price was updated
 */
case class GasDataEntry(sellerId: SellerID, postalCode: PostalCode, stationType: StationType, gasType: GasType, price: Price, date: Date){

  /**
   * Return a String representation of a GasDataEntry as a csv line
   * @return csv line with all the case class values
   */
  override def toString: String = s"$sellerId;$postalCode;$stationType;$gasType;$price;$date"

}

/**
 * Object to describe the GasDataEntry class' member types
 * Define two parsers to get a GasDataEntry from Strings
 */
object GasDataEntry{
  type SellerID = Int
  type PostalCode = Int
  type Price = Int

  /**
   * Creates a GasDataEntry from multiples String arguments
   * @param sellerId sellerId in String form
   * @param postalCode department in String form
   * @param stationType road type in String form (A or R)
   * @param gasType gas type in String form
   * @param price price in String form
   * @param date date in String form (formatted as "yyyy-MM-dd")
   * @return GasDataEntry with these arguments as its values
   */
  def apply(sellerId: String, postalCode: String, stationType: String, gasType: String, price: String, date: String): GasDataEntry =
    this(
      sellerId.toInt,
      postalCode.toInt,
      StationTypeEnum.fromString(stationType),
      GasTypeEnum.fromString(gasType),
      price.toInt,
      Date(date)
    )

  def apply(dfRow: Row): GasDataEntry = {
    val date = dfRow.getStruct(dfRow.fieldIndex("date"))
    val gas = dfRow.getStruct(dfRow.fieldIndex("gasType"))
    val station = dfRow.getStruct(dfRow.fieldIndex("stationType"))
    this(
      dfRow.getInt(dfRow.fieldIndex("sellerId")),
      dfRow.getInt(dfRow.fieldIndex("postalCode")),
      StationTypeEnum.fromString(station.getString(0)),
      GasTypeEnum.fromString(gas.getString(0)),
      dfRow.getInt(dfRow.fieldIndex("price")),
      Date(date.getShort(0), date.getShort(1), date.getShort(2))
    )
  }

  /**
   * Creates a GasDataEntry from the line of a txt-form RDD
   * This line must have all values separated by semicolons (;)
   * @param line line containing the GasDataEntry (all members separated by semicolons)
   * @return GasDataEntry with corresponding values
   */
  def apply(line: String): GasDataEntry = {
    line.split(";").toVector match {
      case sellerIdStr +: countyStr +: stationTypeStr +: gasTypeStr +: priceStr +: dateStr +: _ =>
        GasDataEntry(
          sellerIdStr.toInt,
          countyStr.toInt,
          StationTypeEnum.fromString(stationTypeStr),
          GasTypeEnum.fromString(gasTypeStr),
          priceStr.toInt,
          Date(dateStr)
        )
    }
  }
}


