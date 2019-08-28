package rdd

import dataentry.GasDataEntry
import SparkConfiguration.ss
import SparkConfiguration.ss.implicits._
import org.apache.spark.sql.Dataset
import configuration.DefaultValues.{defaultYearFrom, defaultYearTo, defaultYearRange}

object DataSetLoader {

  /**
   * Get the Dataset of a specific year
   * @param year year of the record
   * @return Dataset with the specific year values
   */
  def yearlyDataset(year: Int): Dataset[GasDataEntry] = getRangeDataset(year to year)

  /**
   * Get the union of Dataset from the given year to the end of the records
   * @param from lower bound for the record's year
   * @return Dataset starting from the specific year
   */
  def fromYearDataset(from: Int): Dataset[GasDataEntry] = getRangeDataset(clampBound(from) to defaultYearTo)

  /**
   * Get the union of Dataset from the beginning of the records to the given year
   * @param to upper bound for the record's year
   * @return Dataset ending to the specific year
   */
  def toYearDataset(to: Int): Dataset[GasDataEntry] = getRangeDataset(defaultYearFrom to clampBound(to))

  /**
   * Get the union of Dataset between two years
   * @param range range of years needed
   * @return Dataset contained in the given year range
   */
  def getRangeDataset(range: Range = defaultYearRange): Dataset[GasDataEntry] =
    clampRange(range)
      .map(y => ss.read.parquet(s"dataset/$y"))
      .reduce((a1,a2) => a1.union(a2))
      .map(GasDataEntry.apply)

  /**
   * Clamped a given year so its correspond to a valid Dataset
   * @param year year
   * @return valid year to query a Dataset
   */
  private def clampBound(year: Int) = {
    if (year < defaultYearFrom) defaultYearFrom
    else if (year > defaultYearTo) defaultYearTo
    else year
  }

  /**
   * Clamped a given range so its correspond to a valid range of Dataset years
   * @param yearRange range
   * @return valid range to query a Dataset
   */
  private def clampRange(yearRange: Range): Range = clampBound(yearRange.start) to clampBound(yearRange.end)
}
