#!/bin/bash

mvn clean package -q -B -T4 -DskipTests
java -jar target/*-runner.jar -DwrkTestLabel
