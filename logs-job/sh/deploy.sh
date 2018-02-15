#!/usr/bin/env bash

cd ../

mvn clean install

scp /Users/songjie/IdeaProjects/qbao/userMirror/reportJob/target/report-job-jar-with-dependencies.jar 192.168.132.162:/root/report-job/

