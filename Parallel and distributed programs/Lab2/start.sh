#!/bin/sh
hadoop fs -rmr output
# rm -rf output/
mvn package
hadoop fs -copyFromLocal 664600583_T_ONTIME_sample.csv
export HADOOP_CLASSPATH=target/hadoop-examples-1.0-SNAPSHOT.jar
hadoop bmstu.iu9.SortApp 664600583_T_ONTIME_sample.csv output
hadoop fs -copyToLocal output
exit 0
