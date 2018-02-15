#!/bin/bash

if [ -z $1 ]
then
  echo "Useage:$0 cmd,cmd maybe:vacation|markland"
  exit 1
fi
type="$1"
dt=`date +"%Y-%m-%d"`
dt1=`date +"%Y-%m-%d-%H-%M-%S"`
shift

if [ "$type" == "vacation" ]
	then
	if [ -f "coveragevacation$dt.em" ]
	then
		if [ -f "coveragevacation$dt.ec" ]
		then
			mv coveragevacation$dt.ec coveragevacation$dt.ec.bak
		fi
	    java emma ctl -connect localhost:47653 -command coverage.get,coveragevacation$dt.ec
		if [ -d coverage ]
		then
			if [ -d coveragehis ]
			then
				rm -rf coveragehis
			fi
			mv coverage coveragehis
		fi
	    java -cp emma.jar emma report -r html -in coveragevacation$dt.em,coveragevacation$dt.ec  -Dreport.html.out.file=coveragevacation.html
		./stop-engine.sh
		cp ../jarbak/Arch.Search.jar ../
		cp ../jarbak/Arch.Search.Vacation.jar ../
		./start.sh engine recover
		nohup ./monitor-engine.sh debug vacation monitor-engine > monitor-engine.$dt1.log &
	else
		echo "coveragevacation$dt.em is null."
	fi
else
	if [ -f "coveragemarkland$dt.em" ]
	then
		if [ -f "coveragemarkland$dt.ec" ]
		then
			mv coveragemarkland$dt.ec coveragemarkland$dt.ec.bak
		fi
		java emma ctl -connect localhost:47653 -command coverage.get,coveragemarkland$dt.ec
		if [ -d coverage ]
		then
			if [ -d coveragehis ]
			then
				rm -rf coveragehis
			fi
			mv coverage coveragehis
		fi
		java -cp emma.jar emma report -r html -in coveragemarkland$dt.em,coveragemarkland$dt.ec  -Dreport.html.out.file=coveragemarkland.html
		./stop-engine.sh
		cp ../jarbak/Arch.Search.jar ../
		cp ../jarbak/Arch.Search.Markland.jar ../
		./start.sh engine recover
		nohup ./monitor-engine.sh debug markland monitor-engine > monitor-engine.$dt1.log &		
	else
		echo "coveragemarkland$dt.em is null."
	fi
fi
