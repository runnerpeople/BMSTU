#!/usr/bin/env bash

mvn package
mvn compile exec:java -Dexec.mainClass=ru.bmstu.iu9.HBaseFillTable
stop-hbase.sh
start-hbase.sh
mvn compile exec:java -Dexec.mainClass=ru.bmstu.iu9.HBaseFilter