#!/bin/bash

MAIN_CLASS="com.dooioo.search.engine.DooiooServer"
 
max_loop=5

for((i=0;i<$max_loop;i++))
do
	pids="$(./get-pid.sh $MAIN_CLASS)"
	if [ -z "$pids" ]
	then
		break;
	fi
	
	if [ $i -gt 1 ]
	then
		_9="-9"
	fi
	
	for pid in $pids
	do
		echo "try kill process with pid:$pid and pattern:$MAIN_CLASS"
		if !  kill $_9 $pid
		then
			echo "failed kill process with pid:$pid and pattern:$MAIN_CLASS"
		else 
			echo "succeed kill process with pid:$pid and pattern:$MAIN_CLASS"
		fi
	done
	
	sleep 0.5
done

if [ $i -eq $max_loop ]
then
	echo "failed kill the processes $pids after try $max_loop times"
	exit 1
fi




