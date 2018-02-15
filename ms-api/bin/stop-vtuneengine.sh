#!/bin/bash

. global.sh

if ! ./check-user.sh
then
	exit $?
fi

. env.sh

SERVER_KEY="engine"
case $SERVER_KEY  in
			
	"engine") 
		port="${port_prefix}${port_key}0"
		MAIN_CLASS="com.ctrip.search.engine.EngineServer";;
		
	"scraper") 
		port="${port_prefix}${port_key}3"
		MAIN_CLASS="com.ctrip.search.scraper.ScraperServer";;
		
	"rebuild") 
		port="${port_prefix}${port_key}2"
		MAIN_CLASS="com.ctrip.search.rebuild.RebuildServer";;
		
	"job") 
		MAIN_CLASS="com.ctrip.search.job.JobServer";;
		
	"suggestion") 
		MAIN_CLASS="com.ctrip.search.suggestion.SuggestionServer";;
		
	"monitor") 
		port="${port_prefix}${port_key}6"
		MAIN_CLASS="com.ctrip.search.monitor.MonitorServer";;

	"loganalysis") 
		port="${port_prefix}${port_key}5"
		MAIN_CLASS="com.ctrip.search.loganalysis.LogAnalysisServer";;
 
	*) 
		MAIN_CLASS="$1";;

	 esac;	

MAIN_CLASS="$index_name.*$MAIN_CLASS"
max_loop=10
vtune_pid=$(./get-pid.sh "amplxe-cl")
for((i=0;i<$max_loop;i++))
do
	pids="$(./get-pid.sh $MAIN_CLASS)"
	if [ -z "$pids" ]
	then
		break;
	fi
	
	if [ $i -gt 5 ]
	then
		_9="-9"
	fi
	
	for pid in $pids
	do
		if [ $pid -eq $vtune_pid ]
		then
			echo "get vtune pid:$pid,skip it" 
			continue;	
		else
			echo_t "try kill process with pid:$pid and pattern:$MAIN_CLASS"
			if ! sudo kill $_9 $pid
			then
				echo_t "failed kill process with pid:$pid and pattern:$MAIN_CLASS"
			else 
				echo_t "succeed kill process with pid:$pid and pattern:$MAIN_CLASS"
			fi
	        fi
	done
	
	sleep 3
done

if [ $i -eq $max_loop ]
then
	echo_t "failed kill the processes $pids after try $max_loop times"
	exit 1
fi
