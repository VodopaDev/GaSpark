package dataentry

import org.scalatest.WordSpec

class DateSpec extends WordSpec{
  "Date" should{

    "be correctly instantiated" when {

      def assertMatches(date: Date, year: Short, month: Short, day: Short) = {
        assert(date.year == year)
        assert(date.month == month)
        assert(date.day == day)
      }

      "using its base constructor" in {
        assertMatches(Date(3,2,1), 3, 2, 1)
      }

      "using its string-formatted date constructor" in {
        assertMatches(Date("03-02-01"), 3, 2, 1)
        assertMatches(Date("03-02-01 12:21:27"), 3, 2, 1)
        assertMatches(Date("03-02-01T12:21:27"), 3, 2, 1)
      }
    }

    "compare correctly" when {
      val date1 = Date(3,3,3)
      val date2 = Date(2,3,3)
      val date3 = Date(3,2,3)
      val date4 = Date(3,3,2)

      "matched with a previous date" in {
        assert((date1 compare date2) > 0)
        assert((date1 compare date3) > 0)
        assert((date1 compare date4) > 0)
      }

      "matched with a posterior date" in {
        assert((date2 compare date1) < 0)
      }

      "matched with an equal date" in {
        assert((date1 compare date1) == 0)
      }
    }

    "print its values as a string-formatted date" in {
      assert(Date(3,2,1).toString == "0003-02-01")
    }
  }
}
