#!/usr/bin/bash

lsof -t -i tcp:2181 | xargs kill -9
lsof -t -i tcp:9092 | xargs kill -9
lsof -t -i tcp:9093 | xargs kill -9
lsof -t -i tcp:9094 | xargs kill -9