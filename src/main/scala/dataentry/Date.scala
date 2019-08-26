package dataentry

/**
 * Simple class to represent a date in the year-month-day format
 * Supports comparison but has no date-correctness check nor any timezone
 * @param year year
 * @param month month
 * @param day day
 */
case class Date(year: Short, month: Short, day: Short) extends Ordered[Date]{

  /**
   * Compare two dates together
   * @param other other date
   * @return 0 if they are equal, 1 if "this" is after "other" and -1 if "other" is after "this"
   */
  override def compare(other: Date): Int = {
    if (this == other) 0
    else if (year > other.year ||
      (year == other.year && month > other.month) ||
      (year == other.year && month == other.month && day > other.day)) 1
    else -1
  }

  /**
   * Print the date to a parsable string in the yyyy-MM-dd
   * @return date in the yyyy-MM-dd format
   */
  override def toString: String = f"$year%04d-$month%02d-$day%02d"
}

/**
 * Date object to define alternative constructors
 */
object Date{

  /**
   * Create a Date from a string-formatted date
   * Must be in the format "yyyy-MM-dd" or "yyyy-MM-ddThh:mm:ss" or "yyyy-MM-dd hh:mm:ss"
   * @param dateStr string-formatted date
   * @return Date corresponding to the given string
   */
  def apply(dateStr: String): Date = {
    val newStr = (if (dateStr contains ' ') dateStr.split(' ')(0)
    else if (dateStr contains 'T') dateStr.split('T')(0)
    else dateStr).split('-')
    assert(newStr.length == 3)
    Date(newStr(0).toShort, newStr(1).toShort, newStr(2).toShort)
  }
}
