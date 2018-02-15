#!/bin/bash
#发送短信接口地址
#sendMsgUrl="http://localhost:8080/sms/sendsmsByMobile"
sendMsgUrl="http://192.168.132.162:8081/sms/sendsmsByMobile"

#接收异常信息手机号
mobileArray=("13472879099")
#接收异常信息邮箱
emailArray=("guoxiaobing@qbao.com")

#日志文件
logPath="monitor.txt"
processsfnewstype="monitor_sfnewstype.sh"

while [ true ];
do

  pidsfnewstype=$(ps -ef | grep ${processsfnewstype} | grep -v grep | awk '{print $2}')

    if [ -z "${pidsfnewstype}" ]; then
      echo "-------->> `date +%Y%m%d%H%M%S` 监控脚本 monitor_sfnewstype.sh 进程中止" >> ${logPath}
      echo "启动monitor_sfnewstype.sh" >> ${logPath}
      sh monitor_sfnewstype.sh &
      echo "发送异常提醒" >> ${logPath}
      #发送短信
      for mobile in ${mobileArray[*]}
          do
          echo  "短信发送结果->${mobile}:"`curl "${sendMsgUrl}?mobile=${mobile}&content=监控脚本monitor_sfnewstype.sh停止,已重新启动"`  >>  ${logPath}
      done
      #发送邮件
      for email in ${emailArray[*]}
            do
            echo "监控脚本monitor_sfnewstype.sh 进程中止了" | mail -s "脚本monitor_sfnewstype.sh停止,已重新启动" ${email}
      done
    else
      echo "-------->> `date +%Y%m%d%H%M%S` 监控脚本 monitor_sfnewstype.sh 正常运行中" >> ${logPath}
    fi
   sleep 600
done
