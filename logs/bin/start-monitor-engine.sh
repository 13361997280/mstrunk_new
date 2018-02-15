#!/bin/bash
. env.sh
left_params=""
while [ ! -z "$1" ]
do
	case "$1" in
	
	"-t") 
		dt="$2"
		shift 2
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

nohup ./monitor-engine.sh $left_params $index_name monitor-engine > ./monitor-engine.$dt.log 2>&1 &

