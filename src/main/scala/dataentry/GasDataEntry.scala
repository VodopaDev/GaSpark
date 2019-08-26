package dataentry

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

import dataentry.GasType.GasType
import dataentry.GasDataEntry.{Department, Price, SellerID}
import dataentry.StationType.StationType

/**
 * Case class to represent a selling entry in some gas station
 * @param sellerId seller id in the provided data
 * @param department department where the gas is sold
 * @param stationType road type on which the gas station sells gas
 * @param gasType gas type sold
 * @param price price of the gas type
 * @param date date at which the gas price was updated
 */
case class GasDataEntry(sellerId: SellerID, department: Department, stationType: StationType, gasType: GasType, price: Price, date: Date){

  /**
   * Return a String representation of a GasDataEntry as a csv line
   * @return csv line with all the case class values
   */
  override def toString: String = {
    val datePrinter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    s"$sellerId;$department;$stationType;$gasType;$price;" + datePrinter.format(date)
  }

}

/**
 * Object to describe the GasDataEntry class' member types
 * Define two parsers to get a GasDataEntry from Strings
 */
object GasDataEntry{
  type SellerID = Int
  type Department = Int
  type Price = Int

  val datePrinter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")

  /**
   * Creates a GasDataEntry from multiples String arguments
   * @param sellerId sellerId in String form
   * @param department department in String form
   * @param stationType road type in String form (A or R)
   * @param gasType gas type in String form
   * @param price price in String form
   * @param date date in String form (formatted as "yyyy-MM-dd")
   * @return GasDataEntry with these arguments as its values
   */
  def fromStringArguments(sellerId: String, department: String, stationType: String, gasType: String, price: String, date: String): GasDataEntry =
    GasDataEntry(
      sellerId.toInt,
      department.toInt,
      StationType.fromString(stationType),
      GasType.fromString(gasType),
      price.toInt,
      datePrinter.clone().asInstanceOf[SimpleDateFormat].parse(date)
    )

  /**
   * Creates a GasDataEntry from the line of a txt-form RDD
   * This line must have all values separated by semicolons (;)
   * @param line line containing the GasDataEntry (all members separated by semicolons)
   * @return GasDataEntry with corresponding values
   */
  def fromRDDLine(line: String): GasDataEntry = {
    line.split(";").toVector match {
      case sellerIdStr +: countyStr +: stationTypeStr +: gasTypeStr +: priceStr +: dateStr +: _ =>
        GasDataEntry(
          sellerIdStr.toInt,
          countyStr.toInt,
          StationType.fromString(stationTypeStr),
          GasType.fromString(gasTypeStr),
          priceStr.toInt,
          datePrinter.clone().asInstanceOf[SimpleDateFormat].parse(dateStr)
        )
    }
  }
}


