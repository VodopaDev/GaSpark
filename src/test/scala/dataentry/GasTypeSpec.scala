package dataentry

import org.scalatest.WordSpec
import dataentry.GasType._

class GasTypeSpec extends WordSpec{
  "GasType" should{
    "return the correct gas type" when {

      "receiving SP95" in {
        assert(fromString("SP95") == SP95)
        assert(fromString("Sp95") == SP95)
      }

      "receiving SP98" in {
        assert(fromString("SP98") == SP98)
        assert(fromString("Sp98") == SP98)
      }

      "receiving Gazole" in {
        assert(fromString("Gazole") == GAZOLE)
        assert(fromString("gaZole") == GAZOLE)
      }

      "receiving E10" in {
        assert(fromString("E10") == E10)
        assert(fromString("e10") == E10)
      }

      "receiving E85" in {
        assert(fromString("E85") == E85)
        assert(fromString("e85") == E85)
      }

      "receiving GPLc" in {
        assert(fromString("GPLc") == GPLc)
        assert(fromString("gplC") == GPLc)
      }

      "receiving incorrect value" in {
        assert(fromString("incorrect") == UNDEFINED)
        assert(fromString("undef") == UNDEFINED)
      }
    }
  }
}
