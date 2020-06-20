#!/bin/bash

ps ax | grep wrkTestLabel | grep target/quarkus | cut -d' ' -f1 | xargs kill -9