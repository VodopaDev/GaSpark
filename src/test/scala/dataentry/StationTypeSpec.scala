package dataentry

import dataentry.StationType._
import org.scalatest.WordSpec

class StationTypeSpec extends WordSpec{
  "StationType" should{
    "return the correct station type" when {

      "receiving A" in {
        assert(fromString("A") == AUTOROUTE)
        assert(fromString("a") == AUTOROUTE)
      }

      "receiving R" in {
        assert(fromString("R") == ROUTE)
        assert(fromString("R") == ROUTE)
      }

      "receiving incorrect value" in {
        assert(fromString("incorrect") == UNDEFINED)
        assert(fromString("U") == UNDEFINED)
      }
    }
  }
}