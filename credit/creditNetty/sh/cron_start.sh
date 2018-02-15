#!/bin/bash
crondir=`pwd`
echo "0 7 * * * * /bin/sh ${crondir}/cron_data_sftimeall.sh 192.168.14.246:8002" >> mycron
echo "0 7 * * * * /bin/sh ${crondir}/cron_data_sfnewstype.sh 192.168.14.246:8002" >> mycron
echo "10 8 * * * /bin/sh ${crondir}/cron_monitor_sftimeall.sh" >> mycron
echo "10 8 * * * /bin/sh ${crondir}/cron_monitor_sfnewstype.sh" >> mycron
crontab mycron
rm mycron
exit 0