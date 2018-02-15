#!/bin/bash

. global.sh

if ! ./check-user.sh
then
	exit $?
fi

left_params=""
while [ ! -z "$1" ]
do
	case "$1" in
	
	"-d") 
		left_params="$left_params $1"
		shift
	;;
	
	"-nr") 
		left_params="$left_params $1"
		shift
	;;
	
	"-t") 
		left_params="$left_params $1 $2"
		shift 2
	;;
	
	*)
		shift
	;;
	esac;	
done

if ! ./stop.sh monitor-engine
then
	echo_t "failed to stop monitor-engine.sh"
	exit 0
fi
. env.sh

while [ true ]
do
	sleep 30
	
	max_loop=5
	for((i=0;i<$max_loop;i++))
	do	
		pids="$(./get-pid.sh $index_name.*EngineServer)"
		if ! [ -z "$pids" ]
		then
			break
		fi
		sleep 1
	done
	
	if [ -z "$pids" ]
	then
		echo_t "engine server process does not exist,start it"
		./start-engine.sh $left_params
	else
		max_loop=10
		for((i=0;i<$max_loop;i++))
		do
			tmp="monitor.engine.`date +"%Y-%m-%d-%H-%M-%S"`"
			rf1="$tmp.resp"
			rf2="$tmp.info"
			if wget -t 1 -T 3 -O $rf1 "http://localhost:${port_prefix}${port_key}0/favicon.ico" > $rf2 2>&1
			then
				rm $rf1
				rm $rf2
				break
			fi
			
			sleep 2
		done
		
		if [ $i -eq $max_loop ]
		then
			echo_t "engine server is not normal"
			dump_files=$(ls -t |grep ${index_name}_engine_${LOCAL_IP}_|grep dump)
			k=0
			for dump_file in $dump_files
			do
				((k+=1))
				if [ $k -gt 6 ]
				then
					rm -fr $dump_file
				fi
			done
			#ts=`date +"%Y-%m-%d-%H-%M-%S"`
			#for pid in $pids
			#do
				#echo_t "dump thread..."
				#jstack -F $pid > ${index_name}_engine_${LOCAL_IP}_thread_dump_$ts.txt
				#echo_t "dump heap..."
				#jmap -dump:live,format=b,file=${index_name}_engine_${LOCAL_IP}_heap_dump_$ts.hprof $pid
			#done
			echo_t "restart it..."
			./start-engine.sh $left_params
		fi			
	fi
	
done
