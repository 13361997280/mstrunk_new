#!/bin/bash

. global.sh

if ! ./check-user.sh
then
	exit $?
fi

if [ -z "$1" ]
then
	echo_t "Useage:$0 cmd,cmd maybe:engie|scraper|rebuild|job|suggestion|monitor|loganalysis|process-keyword"
	exit 1
fi

. env.sh

SERVER_KEY="$1"
case "$SERVER_KEY"  in
			
	"engine") 
		port="${port_prefix}${port_key}0"
		getPID="index_name=$index_name .*com.ctrip.search.engine.EngineServer";;
		
	"scraper") 
		port="${port_prefix}${port_key}3"
		getPID="index_name=$index_name .*com.ctrip.search.scraper.ScraperServer";;
		
	"rebuild") 
		port="${port_prefix}${port_key}2"
		getPID="index_name=$index_name .*com.ctrip.search.rebuild.RebuildServer";;
		
	"job") 
		getPID="index_name=$index_name .*com.ctrip.search.job.JobServer";;
		
	"suggestion") 
		getPID="index_name=$index_name .*com.ctrip.search.suggestion.SuggestionServer";;
		
	"monitor") 
		port="${port_prefix}${port_key}6"
		getPID="index_name=$index_name .*com.ctrip.search.monitor.MonitorServer";;

	"loganalysis") 
		port="${port_prefix}${port_key}5"
		getPID="index_name=$index_name .*com.ctrip.search.loganalysis.LogAnalysisServer";;
 
	*) 
		getPID="$1";;

	 esac;	
if [ "$SERVER_KEY" = "rebuild" -o "$SERVER_KEY" = "scraper" -o "$SERVER_KEY" = "monitor" -o "$SERVER_KEY" = "loganalysis" ]
then
	result_file="./$SERVER_KEY.stopSchedule.html"
	rm $result_file
	wget -q -t 1 -T 30 -O $result_file "http://localhost:$port/$index_name/stopSchedule?info=enforce"
fi	

if [ "$SERVER_KEY" = "engine" ]
then
	if curl --connect-timeout 60 -X put -d 15 "http://localhost:$port/$index_name/setSearchOnline"
	then
		echo_t "wait 15 seconds for loadbalance to do health check..."
	fi
fi	  

max_loop=10
for((i=0;i<$max_loop;i++))
do
	pids="$(./get-pid.sh $getPID)"
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
		echo_t "try kill process with pid:$pid and pattern:$getPID"
		if ! kill $_9 $pid
		then
			echo_t "failed kill process with pid:$pid and pattern:$getPID"
		else 
			echo_t "succeed kill process with pid:$pid and pattern:$getPID"
		fi
	done
	
	sleep 3
done

if [ $i -eq $max_loop ]
then
	echo_t "failed kill the processes $pids after try $max_loop times"
	exit 1
fi




