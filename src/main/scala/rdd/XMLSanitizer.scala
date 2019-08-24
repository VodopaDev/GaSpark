package rdd

import java.io.PrintWriter

import scala.io.Source

object XMLSanitizer extends App{
  def deleteRoguePriceTags(year: Int) = {
    val start = System.currentTimeMillis()
    val src = Source.fromFile(s"resources/unsanitized/$year.xml")
    val dst = new PrintWriter("resources/sanitized/" + year + ".xml")
    src.getLines().filter(s => !s.contains("<prix/>")).foreach(dst.println)
    src.close()
    dst.close()
    val total = System.currentTimeMillis() - start
    println(s"Sanitizing $year took $total ms")
  }

  (2007 to 2019).par.foreach(deleteRoguePriceTags)
}
