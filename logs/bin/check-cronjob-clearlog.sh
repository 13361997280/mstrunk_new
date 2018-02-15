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



sudo /usr/bin/crontab -l > mycron
if grep -q "$local_bin_dir/searchlog-clear.sh" mycron
then
    echo "$index_name cronjob of clearlog is ok!"
else
    echo "* 5 * * * $local_bin_dir/searchlog-clear.sh" >> mycron
    sudo /usr/bin/crontab mycron
    rm mycron
fi