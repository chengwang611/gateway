/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package com.paytmlabs.weblog
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.count
import org.apache.spark.sql.functions.lag
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.functions.max
import org.apache.spark.sql.functions.min
import org.apache.spark.sql.functions.sum
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.functions.countDistinct
import org.apache.spark.sql.functions.unix_timestamp
import org.apache.spark.sql.functions.when

/**
 * Usage: Aggregation [partitions] [numElem] [blockSize]
 */


case class Session(userId: String, startTime: Long, endTime: Long, count: Long)

object SparkSessionization {

  def fetIp = udf { x: String => x.split(":")(0) }

  def main(args: Array[String]) {

    val spark = SparkSession
      .builder()
      .appName("Broadcast Test").master("local[2]")
      .getOrCreate()
    val path = "C://Users//chenwang2017//git//WeblogChallenge//data//2015_07_22_mktplace_shop_web_log_sample.log.gz"
    val colFilter = Seq("timestamp", "userId", "targetIP", "targetUrl", "browser").map(x => col(x))
    val logs = spark.read.format("csv").option("header", "false").option("sep", " ").option("inferSchema", "true").load(path)
      .withColumnRenamed("_c0", "timestamp")
      .withColumnRenamed("_c2", "userId")
      .withColumnRenamed("_c3", "targetIP")
      .withColumnRenamed("_c11", "targetUrl")
      .withColumnRenamed("_c12", "browser").select(colFilter: _*)
      .withColumn("timestamp", unix_timestamp(col("timestamp"), "yyyy-MM-dd HH:mm:ss.nnn").cast("long"))
      .withColumn("userId", fetIp(col("userId")))
    logs.printSchema()
    logs.show(false)
    println(logs.count)

    import spark.implicits._

    val logsSessionIds = logs
      .select('userId, 'timestamp,'targetUrl,
        lag('timestamp, 1)
          .over(Window.partitionBy('userId).orderBy('timestamp))
          .as('prevTimestamp))
      .select('userId, 'timestamp,'targetUrl,
        when('timestamp.minus('prevTimestamp) < lit(15 * 60), lit(0)).otherwise(lit(1))
          .as('isNewSession))
      .select('userId, 'timestamp,'targetUrl,
        sum('isNewSession)
          .over(Window.partitionBy('userId).orderBy('userId, 'timestamp))
          .as('sessionId))
    logsSessionIds.registerTempTable("logsSessionIds_t")
    spark.sql("select * from logsSessionIds_t where userId ='103.16.71.9' ").show(500,false)
    val sessionsDF = logsSessionIds
      .groupBy("userId", "sessionId")
      .agg(min("timestamp").as("startTime"), max("timestamp").as("endTime"), count("*").as("count"), countDistinct("targetUrl").as("url_count"))
      .as[Session].withColumnRenamed("count", "log_count_per_session")
    sessionsDF.show(400)
    sessionsDF.registerTempTable("session_table")
    spark.sql("select * from session_table where userId ='103.16.71.9' ").show(100)
    // spark.sql("select * from session_table where sessionId  >2 and userId ='103.16.71.9'").show(100)
    spark.stop()
  }
}
// scalastyle:on println
