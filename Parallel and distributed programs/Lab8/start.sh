#!/usr/bin/env bash

mvn package
mvn compile exec:java -Dexec.mainClass=ru.bmstu.iu9.TridentApp
