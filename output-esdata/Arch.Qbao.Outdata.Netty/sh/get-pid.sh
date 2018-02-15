#!/bin/bash
if [ -z $1 ]
then
	echo "Usage: $0 process-pattern" >&2
	exit 1
fi

exc_pids="^$$$"
 
curr_pid=$$
 
while [ $curr_pid -gt 0 ]
do
	curr_pid=`ps -fwwp $curr_pid|grep -v PPID|awk '{print $3}'`
 
	exc_pids="$exc_pids|^$curr_pid$"
 
	
done
curr_script=$0
 
curr_script=${curr_script#.}
 
curr_script=${curr_script//./\\.}
 
ps -efww|grep $1|grep -Ev "grep|$curr_script"|awk '{print $2}'|grep -Ev $exc_pids






