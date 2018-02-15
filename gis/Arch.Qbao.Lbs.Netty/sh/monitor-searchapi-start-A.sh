#!/bin/bash

. env.sh


. ${bin_path}/monitor-searchapi-halt-A.sh


echo  ++++++start monitor dyonline  ++++++;

.   ${bin_path}/monitor-searchapi-A.sh >> ${bin_path}/monitor-searchapi-status.log  2>&1&
