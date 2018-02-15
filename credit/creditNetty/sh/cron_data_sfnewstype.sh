#!/bin/bash
#-------------------------start全局配置项-------------------------
#实体名称定义
entityName="sfnewstype"
entityCName="新闻类型"
tableName="t_result_oneday_news_type_d"
indexKey="user_id,news_type"
timeField="stat_date"
#-------------------------end 全局配置项-------------------------
dt=`date '+%Y-%m-%d'`

if [ -z $1 ]
then
	echo "Usage: $0 192.168.14.246:8002" >&2
	exit 1
fi
crondir=`pwd`
echo ${crondir}
#$1=192.168.14.246:8002
echo  `curl "http://$1/command?type=indexupdate&date=${dt}&table=${tableName}&index=${entityName}&key=${indexKey}&dt=${timeField}"` > ${crondir}/result_${entityName}.html
echo `cat ${crondir}/result_${entityName}.html` >> ${crondir}/results_${entityName}.html
count=`awk -F = '{print   $3 }' ${crondir}/result_${entityName}.html | awk -F \< '{print $1}'`
rm  ${crondir}/result_${entityName}.html
if [  $count -lt 700000 ]
then
 echo "Sendmail ...."
 exit 0
fi
exit 0
