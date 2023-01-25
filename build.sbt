name := "quantexa-flight"

version := "1.0"

scalaVersion := "2.12.10"

val sparkVersion = "2.4.8"

resolvers ++= Seq(
  "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven",
  "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases",
  "MavenRepository" at "https://mvnrepository.com"
)

libraryDependencies ++= Seq(
  // Spark
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  // logging
  "org.apache.logging.log4j" % "log4j-api-scala_2.13" % "12.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.19.0" % Runtime
)

