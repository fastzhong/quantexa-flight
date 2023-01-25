package com.quantexa

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import java.sql.Date

/**
 * @author Zhong Lun
 * @date 23/12/22
 *       apiNote
 */
object Main extends App {

  // Spark session
  val spark = SparkSession.builder()
    .appName("Quantexa Flight")
    .config("spark.master", "local")
    .getOrCreate()

  def readCsv(filename: String) = spark.read
    .format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load(s"src/main/resources/data/$filename")

  // flight case class
  case class Flight(
                     passengerId: Long,
                     flightId: Long,
                     from: String,
                     to: String,
                     date: Date
                   )

  // passenger case class
  case class Passenger(
                        passengerId: Long,
                        firstname: String,
                        lastname: String
                      )

  val flightDF = readCsv("flightData.csv")
  val passengerDF = readCsv("passengers.csv")

  import spark.implicits._
  val flightDS = flightDF.withColumn("date", to_date(col("date"), "YYYY-MM-dd")).as[Flight]
  val passengerDS = passengerDF.as[Passenger]

  // Q1 solution
  def flightOfEachMonth() = flightDS.withColumn("Month", month($"date"))
    .groupBy($"Month")
    .agg(count("*").as("Number of Flights"))
    .orderBy(col("Month").asc)
    .show(12)

  // Q2 solution
  def frequentFlyers(topN: Int): Unit = {
    val flightByPassenger = flightDS.groupBy($"passengerId")
      .agg(count("*").as("Number of Flights"))
      .orderBy($"Number of Flights".desc)
      .limit(topN)
    flightByPassenger.as("f")
      .join(passengerDS.as("p"),
        $"f.passengerId" === $"p.passengerId",
        "left_outer")
      .drop($"p.passengerId")
      .withColumnRenamed("passengerId", "Passenger ID")
      .withColumnRenamed("firstName", "First Name")
      .withColumnRenamed("lastName", "Last Name")
      .show(topN)
  }


  // Q3 solution

  // count the number of countries, i.e. UK -> FR -> US -> CN -> UK -> DE -> UK returns 3
  def getMaxNumberOfCountries(start: String, countries: Seq[String]): Int = {
    var maxCount = 0
    var startCount = false
    var count = 0
    var preCountry = ""
    for (c <- countries) {
      if (c.equals(start)) {
        if (startCount) {
          if (count > maxCount) {
            maxCount = count
          }
        }
        startCount = true
        count = 0
      } else {
        if (startCount && !c.equals(preCountry)) {
          count = count + 1
        }
      }
      preCountry = c
    }
    return maxCount
  }

  def flyCountries(start: String): Unit = {
    val flightByPassenger = flightDS.groupBy($"passengerId")
      .agg(concat(collect_list($"from"), collect_list($"to")).as("countries"))
    val longestRunUdf = udf(getMaxNumberOfCountries _)
    flightByPassenger.withColumn("Longest Run", longestRunUdf(lit(start), $"countries"))
      .orderBy($"Longest Run".desc)
      .select($"passengerId".alias("Passenger ID"), $"Longest Run")
      .show()
  }

  // Q4 solution
  def flyTogether(minTimes: Int, fromDate: Date, toDate: Date): Unit = {
    val flightWithRange = flightDS.filter(f => f.date.compareTo(fromDate) >= 0 && f.date.compareTo(toDate) <= 0)
    flightWithRange.withColumnRenamed("passengerId", "passengerId1").as("f1")
      .join(flightWithRange.withColumnRenamed("passengerId", "passengerId2").as("f2"),
        $"f1.flightId" === $"f2.flightId" && $"f1.date" === $"f2.date" && $"f1.passengerId1" < $"f2.passengerId2",
        "inner")
      .groupBy($"f1.passengerId1", $"f2.passengerId2")
      .agg(count("*").as("Number of Flights Together"), min($"f1.date").as("From"), max($"f1.date").as("To"))
      .where($"Number of Flights Together" > minTimes)
      .orderBy($"Number of Flights Together".desc, $"passengerId1".asc)
      .withColumnRenamed("passengerId1", "Passenger 1 ID")
      .withColumnRenamed("passengerId2", "Passenger 2 ID")
      .show()
  }

  if (args.length != 1) {
    println("Warn: pls give a question number (1~4)")
  } else {
    val question = util.Try(args(0).toInt).getOrElse(0)
    question match {
      case 1 => flightOfEachMonth()
      case 2 => frequentFlyers(100)
      case 3 => flyCountries("uk")
      case 4 => flyTogether(3, Date.valueOf("2017-01-01"), Date.valueOf("2017-12-31"))
      case _ => println("Error: invalid question number (1~4 only)")
    }
  }

}