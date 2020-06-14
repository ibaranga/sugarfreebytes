#!/bin/bash

mvn clean package -q -B -T4 -DskipTests

# warmup
(sleep 5 && wrk -t8 -c8 -d30s http://localhost:8004/demo/xl.json) &

java -jar target/*.jar
