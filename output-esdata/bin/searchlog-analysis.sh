#!/bin/bash

LOG_DIR=../logs

E_XCD=66

FILE="engine.access.log."
BACKUP="Y"
FILEDATE=$(date -d'-1 day' "+%Y-%m-%d")
TAG=$HOSTNAME
#check dir
cd $LOG_DIR
if [ "$PWD" != "$LOG_DIR" ]
then
        echo "Can't change to $LOG_DIR."
        exit $E_XCD
fi

usage="Usage: $0 [-d{yyyy-mm-dd}] [-bk{Y|N} --default:N]"
while [ ! -z "$1" ]
do
	case $1 in
	"-d")
		if  echo "$2"|grep -E "^[0-9]{4}\-[0-9]{2}\-[0-9]{2}$" >/dev/null
		then
			FILEDATE=$2
			shift
		else
			echo $usage >&2
			exit 1
		fi
	;;
	"-bk")
		if echo "$2"|grep -E '^Y$|^N$' >/dev/null
		then
			BACKUP=$2
		else
			echo $usage >&2
			exit 1
		fi
	;;
	*)
		echo $usage >&2
		exit 1
	;;
	esac;

	shift
done

FILENAME=$FILE$FILEDATE

if [[ ! -f $FILENAME ]]
then
        echo "FILE $FILENAME IS NOT EXISTS!"
        exit 1
fi
#analysis
awk 'BEGIN {FS="\t";RS="#";total=0;}
	function ut(time)
	{
		if ( time <10 )	return "0ms--10ms"
		else if( 10 <= time&& time < 20) return "10ms--20ms"
		else if( 20 <= time&& time < 50) return "20ms--50ms"
		else if( 50 <= time&& time < 100) return "50ms--100ms"
		else if( 100 <= time&& time < 1000) return "100ms--1000ms"
		else if( 1000 <= time) 	return "1000ms--"
	}
	{
		if($1~/^([0-9 \.\-:])+$/){		 	 
			hourtotal[substr($1,12,2)]++;
			if (match($3,/[0-9.]+/)){
				ip=substr($3,RSTART,RLENGTH);
				uip[ip]++;
			} ;
			usedtime[ut($6)]++;
			total++
		}
	}
	END {
		print "=====begin===== "		
			print "request_day_total="total"; ";
		for(i in hourtotal)
			print "request_hour_"i"="hourtotal[i]"; ";
		for(i in uip)
			print "clientip_"i"="uip[i]"; ";
		for(i in usedtime)
			print "usedtime_"i"="usedtime[i]"; ";			
		print "=====end====="
		}
		' "$FILENAME" >> engine.access.log.$FILEDATE.analysisresult

#java
MAIN_CLASS="com.ctrip.search.loganalysis.LogAnalysisServer"
CLASS_PATH="../Arch.Search.LogAnalysisServer.jar"
CLASS_PATH="../conf:../*:../lib/*:../lib/SQL/*:../Arch.Search.Common.jar:../Arch.Search.KeyGen.jar:$CLASS_PATH"
nohup java -classpath $CLASS_PATH $MAIN_CLASS $FILEDATE $TAG > "start.search.analysis.log" 2>&1 &

if [ "$BACKUP" = "Y" ]
then
	gzip $FILENAME
fi