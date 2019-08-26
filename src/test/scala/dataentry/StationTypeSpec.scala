package dataentry

import dataentry.StationType._
import org.scalatest.WordSpec

class StationTypeSpec extends WordSpec{
  "StationType" should{
    "return the correct station type" when {

      "receiving A" in {
        assert(fromString("A") == Highway)
        assert(fromString("a") == Highway)
      }

      "receiving R" in {
        assert(fromString("R") == Road)
        assert(fromString("R") == Road)
      }

      "receiving incorrect value" in {
        assert(fromString("incorrect") == Undefined)
        assert(fromString("U") == Undefined)
      }
    }
  }
}