name := "GaSpark"
version := "0.1"
scalaVersion := "2.12.7"

libraryDependencies += "org.rogach" %% "scallop" % "3.3.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"
libraryDependencies += "com.databricks" %% "spark-xml" % "0.6.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"