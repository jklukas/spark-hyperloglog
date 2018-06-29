from pyspark.sql import SparkSession, Row
from pyspark.sql.functions import expr

def register():
    spark = SparkSession.builder.getOrCreate()
    # NOTE: at least one dataframe should be created before registration
    spark.createDataFrame([Row(a=1)]).count()
    sc = spark.sparkContext
    sc._jvm.com.mozilla.spark.sql.hyperloglog.functions.package.registerUdf()


def merge(col):
    return expr("hll_merge({0})".format(col))


def cardinality(col):
    return expr("hll_cardinality({0})".format(col))


def create(col, bits=12):
    return expr("hll_create({}, {})".format(col, bits))
