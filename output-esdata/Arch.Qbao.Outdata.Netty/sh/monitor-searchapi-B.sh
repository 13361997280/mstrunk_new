#!/bin/bash

. env.sh
#================监控报警次数变量====================
warn_parms_null_num=0
warn_Qtime_num=0
warn_indexNum_num=0
warn_status_num=0




while [ true ]; 
do
   hh=`date '+%k'` 
   mm=`date '+%M'` 
   if [ ${mm:0:1} -eq 0 ];then 
       mm=${mm:1:1} 
   fi  


#===================================================solr接口检查==================================================================================

echo  `curl "http://10.168.252.77:29010/search?keyword=%E6%9B%B9%E5%AE%89%E8%B7%AF1111&type=recommend"` > monitorsearchapi.html
status_dyonline=`awk -F[,:\"] '{print   $2 }' monitorsearchapi.html`
Qtime_dyonline=`awk -F[,:\"] '{print   $11 }' monitorsearchapi.html`
numFound_dyonline=`awk -F[,:\"] '{print   $16 }' monitorsearchapi.html`
Qtime_dyonline=${Qtime_dyonline/ ms/}
echo `date`  searchApi 搜索参数  ${status_dyonline}  ${Qtime_dyonline} ${numFound_dyonline}


   #solr关闭或异常时三个值为空判断
   if [ "${numFound_dyonline}" == "" ];then


        warn_parms_null_num=`expr $warn_parms_null_num + 1`
        echo 监控参数为空报警循环数: $warn_parms_null_num

        #报警三次后停一个小时
        if [ $warn_parms_null_num -lt 4 ];then

        echo "`date` 10.168.252.77 solr监控参数为空!  numFound_dyonline=${numFound_dyonline}  "
        content="`date "+%Y-%m-%d_%H:%M:%S"`-10.168.252.77-搜索API监控参数为空!-numFound_dyonline=${numFound_dyonline}"

	. ${bin_path}/sendmail.sh  405192832@qq.com "${content}"  "${content}"

        . ${bin_path}/sendmsg.sh 13361997280  "${content}"
        
        fi

         #一个小时后报警数归0 ，重新开始
         if [ $warn_parms_null_num -gt 60 ];then
         warn_parms_null_num=0
         fi

   else
        #正常则报警归零
        warn_parms_null_num=0
   fi



#============================================= 7 - 24 点 监控Qtime_dyonline返回时间<100ms==================================================================

if [ $hh -le 23 ]&&[ $hh -ge 7 ];then



   if [ ${Qtime_dyonline} -gt 100 ];then

	warn_Qtime_num=`expr $warn_Qtime_num + 1` 
        echo dyonline报警循环数: $warn_Qtime_num

        #报警三次后停一个小时
        if [ $warn_Qtime_num -lt 3 ];then

       content="`date "+%Y-%m-%d_%H:%M:%S"`_10.168.252.77_搜索API返回时间太长_数量${numFound_dyonline}_响应时间${Qtime_dyonline}ms"
        
        . ${bin_path}/sendmail.sh  405192832@qq.com "${content}"  "${content}"

        . ${bin_path}/sendmsg.sh 13361997280  "${content}"
  
        fi
	
         #一个小时后报警数归0 ，重新开始
	 if [ $warn_Qtime_num -gt 60 ];then
         warn_Qtime_num=0
	 fi

   else
	   #只要有一次成功，报警数清零
   	   warn_Qtime_num=0
   fi






   #22:59分退出前清零warn_indexNum_num清零
   if [ $hh -eq 22 ]&&[ $mm -eq 59 ];then
	   warn_Qtime_num=0
         
	 
   fi

fi



sleep 60

done