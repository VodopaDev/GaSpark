package dataentry

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DataType, StructField}
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

      // TODO: Make this test row testing
      "created from a DataFrame row" in {
        /*
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
         */
        val date = Date(2018,1,31)
        val entry = GasDataEntry(1, 2, StationTypeEnum.HIGHWAY, GasTypeEnum.E10, 100, Date("2018-01-31"))
        assert(entry.sellerId == 1)
        assert(entry.postalCode == 2)
        assert(entry.stationType == StationTypeEnum.HIGHWAY)
        assert(entry.gasType == GasTypeEnum.E10)
        assert(entry.price == 100)
        assert(entry.date == date)
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
