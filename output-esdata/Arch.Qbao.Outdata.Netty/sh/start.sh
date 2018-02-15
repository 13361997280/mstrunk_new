#!/bin/bash
export JAVA_HOME=/usr/local/java/jdk1.7.0_45
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

. env.sh

# dooioo site start

BIN_DIR=`dirname "$0"`
cd $BIN_DIR

 
JMX_PORT="29020"
DEBUG_PORT="29030"
SERVER_KEY="dooioo"
MAIN_CLASS="com.dooioo.search.engine.DooiooServer"
MEM_INFO="-Xms2G -Xmx2G -Xmn100m -Xss512k"
if ! ./stop.sh 
then 
	exit 1
fi 

while [ ! -z "$1" ]
do
	case "$1" in
		
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

if [ "$debug" == "debug" ]
then
	DEBUG_INFO=" -Xdebug -Xrunjdwp:transport=dt_socket,address=DEBUG_PORT,server=y,suspend=n"
fi
DEBUG_INFO="$DEBUG_INFO -Dcom.sun.management.jmxremote.port=$JMX_PORT"
DEBUG_INFO="$DEBUG_INFO -Dcom.sun.management.jmxremote.authenticate=false"
DEBUG_INFO="$DEBUG_INFO -Dcom.sun.management.jmxremote.ssl=false"
# START_OPTS="$START_OPTS -XX:+AggressiveOpts -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:NewSize=64m"
START_OPTS="$DEBUG_INFO -server $MEM_INFO"
START_OPTS="$START_OPTS -Djava.io.tmpdir=../tmp"
START_OPTS="$START_OPTS -DLOG=$SERVER_KEY"
START_OPTS="$START_OPTS -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+UseParNewGC  -XX:CMSInitiatingOccupancyFraction=60"
START_OPTS="$START_OPTS -Xloggc:../logs/${SERVER_KEY}_gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails"

CLASS_PATH="../*:../lib/*"
dt=`date +"%Y-%m-%d-%H-%M-%S"`
nohup java $START_OPTS -classpath $CLASS_PATH $MAIN_CLASS $@ > "start.$SERVER_KEY.$dt.log" 2>&1 &
echo "java-pid $!"
sleep 1



echo "finish $SERVER_KEY"