package rdd

import dataentry.{GasDataEntry, GasTypeEnum, StationTypeEnum}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object RDDLoader {

  private val baseRange = 2007 to 2019
  private val appName = "GaSpark"
  private val numberOfThreads = Runtime.getRuntime.availableProcessors()
  private val conf = new SparkConf()
    .setAppName(appName)
    .setMaster(s"local[$numberOfThreads]")
    .set("spark.executor.memory", "4g")
  private val ss = SparkSession.builder().config(conf).getOrCreate()
  import ss.implicits._

  def main(args: Array[String]): Unit = {
    val b4 = System.currentTimeMillis()

    val gazole = yearlyRdd(2007)
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

    println("It took " + (System.currentTimeMillis() - b4) + "ms")
  }

  /**
   * Get the RDD of a specific year
   * @param year year of the record
   * @return RDD with the specific year values
   */
  def yearlyRdd(year: Int): RDD[GasDataEntry] = ss.read.parquet(year.toString).map(GasDataEntry.apply).rdd

  /**
   * Get the union of RDDs from the given year to the end of the records
   * @param from lower bound for the record's year
   * @return RDD starting from the specific year
   */
  def fromYearRdd(from: Int): RDD[GasDataEntry] = getRangeRdd(clampBound(from) to baseRange.end)

  /**
   * Get the union of RDDs from the beginning of the records to the given year
   * @param to upper bound for the record's year
   * @return RDD ending to the specific year
   */
  def toYearRdd(to: Int): RDD[GasDataEntry] = getRangeRdd(baseRange.start to clampBound(to))

  /**
   * Get the union of RDDs between two years
   * @param range range of years needed
   * @return RDD contained in the given year range
   */
  def getRangeRdd(range: Range = baseRange): RDD[GasDataEntry] =
    clampRange(range)
    .map(yearlyRdd)
    .reduce((acc, rdd) => acc.union(rdd))

  /**
   * Clamped a given year so its correspond to a valid RDD
   * @param year year
   * @return valid year to query a RDD
   */
  private def clampBound(year: Int) = {
    if (year < baseRange.start) baseRange.start
    else if (year > baseRange.end) baseRange.end
    else year
  }

  /**
   * Clamped a given range so its correspond to a valid range of RDD years
   * @param yearRange range
   * @return valid range to query RDDs
   */
  private def clampRange(yearRange: Range): Range = clampBound(yearRange.start) to clampBound(yearRange.end)
}
