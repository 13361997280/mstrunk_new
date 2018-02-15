#!/bin/bash

. env.sh


. ${bin_path}/monitor-searchapi-halt-B.sh


echo  ++++++start monitor dyonline  ++++++;

.   ${bin_path}/monitor-searchapi-B.sh >> ${bin_path}/monitor-searchapi-status.log  2>&1&
