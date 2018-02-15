#!/bin/bash

if readlink -f "$0" > /dev/null 2>&1
then
  script_name=`readlink -f "$0"`
else
  script_name="$0"
fi
local_bin_dir=`dirname "$script_name"`
cd $local_bin_dir
cd ..
index_name=`pwd`
index_name=${index_name%/}
index_name=${index_name##/*/}

LOGS_DIR=$local_bin_dir/../logs
BIN_DIR==$local_bin_dir

ROOT_UID=0
E_XCD=66
E_NOTROOT=67

LOGS_Name=*.log.*
 
if [ "$UID" -ne "$ROOT_UID" ]
then
 echo "Must be root to run this script."
 exit $E_NOTROOT
fi

for logfile in $( find $LOGS_DIR -type f -name "$LOGS_Name" -mtime +7)
do
  rm -f "$logfile"
done

#for logfile in $( find $BIN_DIR -type f -name "*.log" -mtime +14)
#do
#   rm -f "$logfile"
#done

#for logfile in $( find $BIN_DIR -type f -name "*.info" -mtime +14)
#do
#   rm -f "$logfile"
#done

#for logfile in $( find $BIN_DIR -type f -name "*.resp" -mtime +14)
#do
#   rm -f "$logfile"
#done

exit 0