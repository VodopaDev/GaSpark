package dataentry

import dataentry.GasDataEntry._
import org.scalatest.WordSpec

class GasDataEntrySpec extends WordSpec{

  "GasDataEntry" should {
    "have correct values" when {

      "created from class constructor" in {
        val date =  GasDataEntry.datePrinter.parse("2018-01-31")
        val entry = new GasDataEntry(1, 2, StationType.AUTOROUTE, GasType.E10, 100, date)
        assert(entry.sellerId == 1)
        assert(entry.department == 2)
        assert(entry.stationType == StationType.AUTOROUTE)
        assert(entry.gasType == GasType.E10)
        assert(entry.price == 100)
        assert(entry.date == date)
      }

      "created from a RDD string" in {
        val entry = fromRDDLine("1;2;A;E10;100;2018-01-31")
        assert(entry.sellerId == 1)
        assert(entry.department == 2)
        assert(entry.stationType == StationType.AUTOROUTE)
        assert(entry.gasType == GasType.E10)
        assert(entry.price == 100)
        assert(entry.date == GasDataEntry.datePrinter.parse("2018-01-31"))
      }

      "created from a many strings" in {
        val entry = fromStringArguments("1","2","A","E10","100","2018-01-31")
        assert(entry.sellerId == 1)
        assert(entry.department == 2)
        assert(entry.stationType == StationType.AUTOROUTE)
        assert(entry.gasType == GasType.E10)
        assert(entry.price == 100)
        assert(entry.date == GasDataEntry.datePrinter.parse("2018-01-31"))
      }
    }

    "have a correct toString method" in {
      val entry = fromStringArguments("1","2","A","E10","100","2018-01-31")
      assert(entry.toString == "1;2;A;E10;100;2018-01-31")
    }
  }
}