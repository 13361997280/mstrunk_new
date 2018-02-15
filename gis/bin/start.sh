#!/bin/bash

. global.sh

#-------------------------------------------------------------------
#    Search Bootstrap Script 
#	li_yao
#-------------------------------------------------------------------
if ! ./check-user.sh
then
	exit $?
fi

. env.sh
./check-cronjob-clearlog.sh
BIN_DIR=`dirname "$0"`
cd $BIN_DIR

left_params=""
while [ ! -z "$1" ]
do
	case "$1" in
	
	"-k") 
		SERVER_KEY="$2"
		shift 2
	;;
	
	"-t") 
		dt="$2"
		shift 2
	;;
	
	"-d") 
		debug="debug"
		shift
	;;
	
	*)
		left_params="$left_params $1"
		shift
	;;
	esac;	
done
if [ -z "$dt" ]
then
	dt=`date +"%Y-%m-%d-%H-%M-%S"`
fi
left_params="$left_params -t $dt"


shift
case $SERVER_KEY  in
			
	"engine") 
		SERVER_NO=1;
		SERVER_PORT="${port_prefix}${port_key}0";
		MAIN_CLASS="com.ctrip.search.engine.EngineServer"; 
		MEM_INFO=$engine_mem_info;
		CLASS_PATH="../Arch.Search.jar:../Arch.Search.$Index_name.jar:../Arch.Search.Analysis.jar:../Arch.Search.Spellcheck.jar";;
		
	"scraper") 
		SERVER_NO=2;
		SERVER_PORT="${port_prefix}${port_key}3";
		MAIN_CLASS="com.ctrip.search.scraper.ScraperServer";
		MEM_INFO=$scraper_mem_info;
		CLASS_PATH="../Arch.Search.jar:../Arch.Search.$Index_name.jar";;
		
	"rebuild") 
		SERVER_NO=3;
		SERVER_PORT="${port_prefix}${port_key}2";
		MAIN_CLASS="com.ctrip.search.rebuild.RebuildServer";
		MEM_INFO=$rebuild_mem_info;
		CLASS_PATH="../Arch.Search.jar:../Arch.Search.$Index_name.jar:../Arch.Search.Analysis.jar";;
		
	"job") 
		SERVER_NO=4;
		SERVER_PORT="${port_prefix}${port_key}4";
		MAIN_CLASS="com.ctrip.search.job.JobServer";
		MEM_INFO=$job_mem_info;
		CLASS_PATH="../Arch.Search.DataSync.jar:../Arch.Search.$Index_name.jar";;
		
#	"suggestion") 
#		SERVER_NO=6;
#		SERVER_PORT="${port_prefix}${port_key}0";
#		MAIN_CLASS="com.ctrip.search.suggestion.SuggestionServer"; 
#		MEM_INFO="-Xms1G -Xmx1G -Xmn300m -Xss128k";
#		CLASS_PATH="../Arch.Search.Suggestion.jar:../Arch.Search.Suggestion.$Index_name.jar:../Arch.Search.Analysis.jar";;
		
	"monitor") 
		SERVER_NO=7;
		SERVER_PORT="${port_prefix}${port_key}6";
		MAIN_CLASS="com.ctrip.search.monitor.MonitorServer"; 
		MEM_INFO=$monitor_mem_info;
		CLASS_PATH="../Arch.Search.Monitor.jar";;


#	"loganalysis") 
#		SERVER_NO=5;
#		SERVER_PORT="${port_prefix}${port_key}5";
#		MAIN_CLASS="com.ctrip.search.loganalysis.LogAnalysisServer"; 
#		MEM_INFO="-Xms200m -Xmx200m -Xmn100m -Xss128k";
#		CLASS_PATH="../Arch.Search.LogAnalysis.jar";;

	 
	*) 
		echo_t "Useage:$0  cmd,cmd maybe:engie[ recover]|scraper|rebuild|job|monitor [debug]";
		exit 1;;

	 esac;	 

if ! ./stop.sh $SERVER_KEY
then 
	exit 1
fi



if [ "$debug" == "debug" ]
then
	DEBUG_INFO=" -Xdebug -Xrunjdwp:transport=dt_socket,address=152$port_key$SERVER_NO,server=y,suspend=n"
fi

DEBUG_INFO="$DEBUG_INFO -Dcom.sun.management.jmxremote.port=88$port_key$SERVER_NO"
DEBUG_INFO="$DEBUG_INFO -Dcom.sun.management.jmxremote.authenticate=false"
DEBUG_INFO="$DEBUG_INFO -Dcom.sun.management.jmxremote.ssl=false"
START_OPTS="$START_OPTS -XX:+AggressiveOpts -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:NewSize=64m"
START_OPTS="-server $MEM_INFO $DEBUG_INFO"
START_OPTS="$START_OPTS -Djava.io.tmpdir=../tmp"
START_OPTS="$START_OPTS -DLOG=$SERVER_KEY"
START_OPTS="$START_OPTS -Dindex_name=$index_name"
START_OPTS="$START_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump_${index_name}_${SERVER_KEY}_${LOCAL_IP}.hprof -XX:+UseConcMarkSweepGC -XX:+UseParNewGC  -XX:CMSInitiatingOccupancyFraction=60 -XX:CMSTriggerRatio=70 -XX:CMSTriggerPermRatio=70"
#START_OPTS="$START_OPTS -Xloggc:../logs/${SERVER_KEY}_gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails"

CLASS_PATH="../conf:../*:../lib/*:../Arch.Search.Common.jar:../Arch.Search.KeyGen.jar:$CLASS_PATH"
nohup java $START_OPTS -classpath $CLASS_PATH $MAIN_CLASS $left_params > "./start.$SERVER_KEY.$dt.log" 2>&1 &
echo_t "java-pid $!"
sleep 5

#wait for completly started
max_loop=1000
for((i=0;i<=$max_loop;i++))
do	
	#check wether the $SERVER_KEY server with pid is alive
	if [ -z "$(./get-pid.sh $index_name.*$MAIN_CLASS)" ]
	then
		echo_t "failed to start the $SERVER_KEY server"
		exit 1
	fi

	#check more exactly
	rm favicon.ico
	if wget -O favicon.ico "http://localhost:$SERVER_PORT/favicon.ico"
	then
		echo_t "$SERVER_KEY server has been restart successfully";
		break;
	fi
	
	sleep 3
done

if [ $i -gt $max_loop ]
then
	echo_t "failed to restart $SERVER_KEY server";
	exit 1;
fi

echo_t "finish $0"