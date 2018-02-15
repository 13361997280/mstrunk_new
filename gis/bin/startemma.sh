#!/bin/bash

if [ -z $1 ]
then
  echo "Useage:$0 cmd,cmd maybe:vacation|markland"
  exit 1
fi
type="$1"
dt=`date +"%Y-%m-%d"`
shift

if [ "$type" == "vacation" ]
then
  if [ -f "coveragevacation$dt.em" ]
  then
	mv coveragevacation$dt.em coveragevacation$dt.em.bak
  fi
  if [ -d ../jarbak ]
  then
	rm -f ../jarbak
  else
	mkdir ../jarbak
  fi
  cp ../Arch.Search.jar ../jarbak/
  cp ../Arch.Search.Vacation.jar ../jarbak/
  java emma instr -m overwrite -cp ../Arch.Search.jar -ix -com.ctrip.search.rebuild.*,-com.ctrip.search.scraper.*,-org.apache.lucene.search.*,-com.ctrip.search.engine.query.filter.* -Dmetadata.out.file=coveragevacation$dt.em
  java emma instr -m overwrite -cp ../Arch.Search.Vacation.jar -ix -com.ctrip.search.vacation.rebuild.*,-com.ctrip.search.vacation.scraper.*,-com.ctrip.search.vacation.analysis.*,-com.ctrip.search.vacation.job.*,-com.ctrip.search.vacation.collector.* -Dmetadata.out.file=coveragevacation$dt.em

else
  if [ -f "coveragemarkland$dt.em" ]
  then
	mv coveragemarkland$dt.em coveragemarkland$dt.em.bak
  fi  
  if [ -d ../jarbak ]
  then
	rm -f ../jarbak
  else
	mkdir ../jarbak
  fi
  cp ../Arch.Search.jar ../jarbak/
  cp ../Arch.Search.Markland.jar ../jarbak/
  java emma instr -m overwrite -cp ../Arch.Search.jar -ix -com.ctrip.search.rebuild.*,-com.ctrip.search.scraper.*,-org.apache.lucene.search.*,-com.ctrip.search.engine.query.filter.* -Dmetadata.out.file=coveragemarkland$dt.em
  java emma instr -m overwrite -cp ../Arch.Search.Markland.jar -ix -com.ctrip.search.markland.rebuild.*,-com.ctrip.search.markland.* -Dmetadata.out.file=coveragemarkland$dt.em
fi