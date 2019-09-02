package query.utils

import java.io.{File, PrintWriter}

import configuration.{BaseConf, DefaultValues}
import org.apache.spark.sql.DataFrame

import scala.language.implicitConversions

object ExtendedDataFrame {

  implicit class Implicits(df: DataFrame) {

    def saveWithConf(conf: BaseConf, separator: String = ";"): DataFrame = {
      val cached = df.cache()
      val result = cached.collect().sortBy(r => r.getString(0))
      result.foreach(println(_))

      conf.savePath.toOption match {
        case Some(path) =>
          val writer = new PrintWriter(new File(DefaultValues.defaultSavePath + path))

          writer.println(cached.schema.names.mkString(separator))
          result.foreach(line => writer.println(line.mkString(separator)))
          writer.close()
        case None =>
      }
      df
    }

    /**
     * Performs a natural join of two data frames.
     * The frames are joined by equality on all of the columns they have in common.
     * @param other other data frame to join
     * @return resulting frame has the common columns (in the order they appeared in <code>left</code>),
     *         * followed by the columns that only exist in <code>left</code>, followed by the columns that
     *         * only exist in <code>right</code>.
     */
    def natjoin(other: DataFrame): DataFrame = {
      val leftCols = df.columns
      val rightCols = other.columns

      val commonCols = leftCols.toSet intersect rightCols.toSet

      if(commonCols.isEmpty)
        df.limit(0).join(other.limit(0))
      else
        df
          .join(other, commonCols.map { col => df(col) === other(col) }.reduce(_ && _))
          .select(leftCols.collect { case c if commonCols.contains(c) => df(c) } ++
            leftCols.collect { case c if !commonCols.contains(c) => df(c) } ++
            rightCols.collect { case c if !commonCols.contains(c) => other(c) } : _*)
    }
  }


}


