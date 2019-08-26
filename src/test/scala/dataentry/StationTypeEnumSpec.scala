package dataentry

import dataentry.StationTypeEnum._
import org.scalatest.WordSpec

class StationTypeEnumSpec extends WordSpec{
  "StationType" should{
    "return the correct station type" when {

      "receiving A" in {
        assert(fromString("A") == HIGHWAY)
        assert(fromString("a") == HIGHWAY)
      }

      "receiving R" in {
        assert(fromString("R") == ROAD)
        assert(fromString("R") == ROAD)
      }

      "receiving incorrect value" in {
        assert(fromString("incorrect") == UNDEFINED)
        assert(fromString("U") == UNDEFINED)
      }
    }
  }
}