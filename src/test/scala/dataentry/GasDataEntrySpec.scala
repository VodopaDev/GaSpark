package dataentry

import org.scalatest.WordSpec

class GasDataEntrySpec extends WordSpec{

  "GasDataEntry" should {
    "have correct values" when {

      "created from class constructor" in {
        val date = Date(2018,1,31)
        val entry = GasDataEntry(1, 2, StationTypeEnum.HIGHWAY, GasTypeEnum.E10, 100, Date("2018-01-31"))
        assert(entry.sellerId == 1)
        assert(entry.postalCode == 2)
        assert(entry.stationType == StationTypeEnum.HIGHWAY)
        assert(entry.gasType == GasTypeEnum.E10)
        assert(entry.price == 100)
        assert(entry.date == date)
      }

      "created from a RDD string" in {
        val entry = GasDataEntry("1;2;A;E10;100;2018-01-31")
        assert(entry.sellerId == 1)
        assert(entry.postalCode == 2)
        assert(entry.stationType == StationTypeEnum.HIGHWAY)
        assert(entry.gasType == GasTypeEnum.E10)
        assert(entry.price == 100)
        assert(entry.date == Date(2018,1,31))
      }

      "created from a many strings" in {
        val entry = GasDataEntry("1","2","A","E10","100","2018-01-31")
        assert(entry.sellerId == 1)
        assert(entry.postalCode == 2)
        assert(entry.stationType == StationTypeEnum.HIGHWAY)
        assert(entry.gasType == GasTypeEnum.E10)
        assert(entry.price == 100)
        assert(entry.date == Date(2018,1,31))
      }
    }

    "have a correct toString method" in {
      val entry = GasDataEntry("1","2","A","E10","100","2018-01-31")
      assert(entry.toString == "1;2;A;E10;100;2018-01-31")
    }
  }
}
