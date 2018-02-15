#!/bin/bash

if [ -z $1 ]
then
	echo "Useage:$0 cmd,cmd maybe:hotspots|locksandwaits"
	exit 1
fi
collect_type="$1"
dt=`date +"%Y-%m-%d"`
shift
case $collect_type  in
			
	"hotspots") 
		report_dir="hs$dt";;
		
	"locksandwaits") 
		report_dir="lw$dt";;
	esac;	
sudo rm -fr $report_dir
sudo AMPLXE_EXPERIMENTAL=1 /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -collect $collect_type -result-dir $report_dir java -server -Xms1G -Xmx3G -Xmn700m -Xss512k -Dcom.sun.management.jmxremote.port=8891 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.io.tmpdir=../tmp -DLOG=engine -Dindex_name=vacation -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/java_pid.hprof -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSInitiatingOccupancyFraction=60 -XX:CMSTriggerRatio=70 -XX:CMSTriggerPermRatio=70 -classpath ../conf:../*:../lib/*:../Arch.Search.Common.jar:../Arch.Search.KeyGen.jar:../Arch.Search.jar:../Arch.Search.Vacation.jar:../Arch.Search.Analysis.jar:../Arch.Search.Spellcheck.jar com.ctrip.search.engine.EngineServer recover > vtune.log 2>&1 &
