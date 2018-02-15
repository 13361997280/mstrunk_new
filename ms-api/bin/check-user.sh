#!/bin/bash

. global.sh

if [ $1 ]
then
	USER=$1
else
	USER="op1"
fi
