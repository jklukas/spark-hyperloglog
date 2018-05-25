/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mozilla.spark.sql.hyperloglog

import com.twitter.algebird.{HyperLogLog, HyperLogLogMonoid}
import com.mozilla.spark.sql.hyperloglog.aggregates.HyperLogLogMerge

package object functions {
  def hllCardinality(hll: Array[Byte]): Long = {
    HyperLogLog.fromBytes(hll).approximateSize.estimate
  }

  // See algebird-core/src/main/scala/com/twitter/algebird/HyperLogLog.scala#L194-L210
  // E.g. 12 bits corresponds to an error of 0.0163
  def hllCreate(x: String, bits: Int): Array[Byte] = {
    x match {
      case null => null
      case _ =>
        val monoid = new HyperLogLogMonoid(bits)
        HyperLogLog.toBytes(monoid.toHLL(x))
    }
  }

  def registerUdf() {
    import org.apache.spark.sql.SparkSession
    val spark = SparkSession.builder().getOrCreate()

    val merge_udf = "hll_merge"
    val create_udf = "hll_create"
    val cardinality_udf = "hll_cardinality"

    spark.udf.register(merge_udf, new HyperLogLogMerge)
    spark.udf.register(create_udf, hllCreate _)
    spark.udf.register(cardinality_udf, hllCardinality _)
  }
}
