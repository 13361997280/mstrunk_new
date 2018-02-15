#!/bin/bash
#-------------------------start全局配置项-------------------------
#实体名称定义
entityName="sftimeall"
#发送短信接口地址
sendMsgUrl="http://192.168.132.162:8081/sms/sendsmsByMobile"
#搜索引擎地址及端口
esAddress="192.168.14.107:9200"
#接收异常信息手机号
mobileArray=("13472879099" "13361997280" "13770675272" "13262872357")
#接收异常信息邮箱
emailArray=("guoxiaobing@qbao.com" "huangzhi@qbao.com" "liuwang@qbao.com" "songjie@qbao.com")
#-------------------------end 全局配置项-------------------------

#日志文件
logPath="cron_monitor_${entityName}.txt"
today=`date +%e`
shname="cron_monitor_${entityName}.sh"
awk '/${shname}/ {print $2 }' /var/log/cron > /opt/monitor/sftimeall.log

runDay=`awk 'END {print}' /opt/monitor/sftimeall.log`

echo "当前日期：${today},最后一次运行日:"${runDay}  >>  ${logPath}


if [ -z "${runDay}" ]; then
  echo "-------->> `date +%Y%m%d%H%M%S` 监控脚本 ${shname} 未运行" >> ${logPath}
  #发送短信
  for mobile in ${mobileArray[*]}
  do
    echo  "短信发送结果->${mobile}:"`curl "${sendMsgUrl}?mobile=${mobile}&content=${shname}未运行"`  >>  ${logPath}
  done
  #发送邮件
  for email in ${emailArray[*]}
  do
    echo "监控脚本 ${shname}" | mail -s "esdata.sh 未运行" ${email}
  done
  exit 0
fi

if [ ${today} -ne ${runDay} ]; then
  echo "-------->> `date +%Y%m%d%H%M%S` 监控脚本 ${shname} 未运行" >> ${logPath}
  #发送短信
  for mobile in ${mobileArray[*]}
  do
    echo  "短信发送结果->${mobile}:"`curl "${sendMsgUrl}?mobile=${mobile}&content=${shname}未运行"`  >>  ${logPath}
  done
  #发送邮件
  for email in ${emailArray[*]}
  do
    echo "监控脚本 ${shname}" | mail -s "${shname} 未运行" ${email}
  done
  exit 0
else
  echo "-------->> `date +%Y%m%d%H%M%S` 监控脚本 ${shname} 正常运行中" >> ${logPath}
  exit 1
fi


