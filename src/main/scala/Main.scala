import dataentry.{GasTypeEnum, StationTypeEnum}
import org.apache.spark.api.java.function.ReduceFunction
import rdd.DataSetLoader._

object Main extends App {
  val b4 = System.currentTimeMillis()

  var fromYear = Int.MinValue
  var toYear = Int.MaxValue

  def parseArgs(args: List[String]): Int = args match {
    case Nil => 0
    case "--from" :: start :: tail =>
      if (start.toInt > toYear) toYear = start.toInt
      parseArgs(tail)
    case "--to" :: to :: tail =>
      if (toYear > fromYear) toYear = to.toInt
      parseArgs(tail)

  }

  def rddMap = {
    val gazole = getRangeDataset(2007 to 2019)
      .rdd
      .filter(_.gasType == GasTypeEnum.GAZOLE)
      .map(e => ((e.stationType, (e.date.year, e.date.month)), (1, e.price.toLong)))
      .reduceByKey((t1, t2) => (t1._1 + t2._1, t1._2 + t2._2))
      .mapValues {case (count, total) => total / count}
      .map{ case ((station, date), avg) => (date, (station, avg))}
      .cache()

    val highwayGazole = gazole.filter{ case (_, (station,_)) => station == StationTypeEnum.HIGHWAY}.sortBy(_._1)
    val roadGazole = gazole.filter{ case (_, (station,_)) => station == StationTypeEnum.ROAD}.sortBy(_._1)
    val zip = highwayGazole.zip(roadGazole)
      .map{ case ( (year, (_, priceHighway)), (_, (_,priceRoad))) => (year, priceHighway, priceRoad)}

    zip.collect().foreach{ case (year, pH, pR) =>
      val diff = pH/1000.toDouble - pR/1000.toDouble
      val perD = (pH/pR.toDouble - 1.0d) * 100
      val perS = (if (perD >= 0) f"+$perD%1.1f" else f"+$perD%1.1f") + "%"
      println(f"In $year, gasoline cost on average ${pH/1000.toDouble}%1.3f€/L on highways vs ${pR/1000.toDouble}%1.3f€/L on basic roads. " +
        f"Highways are on average $diff%1.3f€/L ($perS) more expensive.")
    }
  }
  println("It took " + (System.currentTimeMillis() - b4) + "ms")
}
