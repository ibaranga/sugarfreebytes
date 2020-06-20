#!/bin/bash

now=$(date +"%s")

apps=(quarkus-resteasy quarkus-vertx-web spring-boot spring-boot-webflux micronaut micronaut-reactive)
sizes=(xxs xs md xl xxl)
connection_nums=(10 100 500 1000)
thread_nums=(1 5 10)

host=localhost

warmup_duration=60s
test_duration=60s

function test() {
  app=$1
  threads=$2
  conns=$3
  duration=$4
  script=$5
  size=$6
  log_file=$7

  wrk -t$threads -c$conns -d$duration -s$script http://$host:18080/$app/$size.json >>$log_file
}

function checkReadiness() {
  app=$1
  echo "Checking $app readiness ..."
  (cd ../mfdemo-common && mvn clean test -B -q -Dtest=ReadinessIT -Dapp=$app)
}

function start() {
  app=$1
  echo "Starting $app ..."
  java -jar ../$app/target/$app*.jar -DwrkPerfTestCustomLabel >run.log &
  app_pid=$!
  echo "Started $app ($app_pid)"
}

function afterTest() {
  app=$1
  app_pid=$2
  echo "Stopping $app ($app_pid) ..."
  kill -SIGKILL $app_pid
}

function beforeTest() {
  app=$1
  threads=$2
  conns=$3
  size=$4

  start $app
  sleep 10
  checkReadiness $app || exit 1

  echo "Warming up ..."
  test $app $threads $conns $warmup_duration ../wrk_scripts/default.lua $size logs/$now.warmup.log
  echo "Warm-up completed"
}

function buildApps() {
  (cd ../mfdemo-common && mvn -q -B clean install -DskipTests -T4)
  for app in ${apps[@]}; do
    (cd ../$app && mvn -q -B clean install -DskipTests -T4)
  done
}

function testApps() {
  for app in ${apps[@]}; do
    beforeTest $app 10 100 md

    for size in ${sizes[@]}; do
      test $app 10 100 $test_duration ../wrk_scripts/default.lua $size logs/$now.$app.get.log
      test $app 10 100 $test_duration ../wrk_scripts/post_$size.lua $size logs/$now.$app.post.log
    done

    afterTest $app $app_pid
  done
}

buildApps
testApps
