#!/bin/bash
. env.sh

. ${bin_path}/stop.sh monitor-searchapi

ps -ef |grep monitor-searchapi
