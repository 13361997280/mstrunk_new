#!/bin/bash
#-------------------------start全局配置项-------------------------
#实体名称定义
entityName="sfnewtype"
entityCName="新闻类型"
#发送短信接口地址
sendMsgUrl="http://192.168.132.162:8081/sms/sendsmsByMobile"
#搜索引擎地址及端口
esAddress="192.168.14.107:9200"
#接收异常信息手机号
mobileArray=("13472879099" "13361997280" "13770675272" "13262872357")
#接收异常信息邮箱
emailArray=("guoxiaobing@qbao.com" "huangzhi@qbao.com" "liuwang@qbao.com" "songjie@qbao.com")
#-------------------------end 全局配置项-------------------------


sleepSeconds=1800
latestTotal=""
#保存es查询结果
result="monitor_result_${entityName}.txt"
#日志文件
logPath="monitor_log_${entityName}.txt"

while [ true ];
do

    hh=`date +%H`

    if [ ${hh} -ge 3 ] && [ ${hh} -le 7 ]; then
         echo "=======03-07时间段不监控=======" >>  ${logPath}
         sleep ${sleepSeconds}
        continue
    fi
    
    echo "=======`date +%Y%m%d%H%M%S` 监控ES-${entityName}:${esAddress}=======" >>  ${logPath}

    oldTotlal=`cat ${result}`

    statusCode=`curl -I -m 10 -o /dev/null -s -w %{http_code} "http://${esAddress}"`

    if [ ${statusCode} -ne 200 ]; then
          echo "发送ES-${entityName}异常提醒 状态码:${statusCode}" >> ${logPath}
          #发送短信
          for mobile in ${mobileArray[*]}
             do
             echo  "短信发送结果->${mobile}:"`curl "${sendMsgUrl}?mobile=${mobile}&content=ES-${entityName}:${esAddress},返回状态:${statusCode}"`  >>  ${logPath}
          done
          #发送邮件
          for email in ${emailArray[*]}
            do
            echo "${esAddress} 返回状态：${statusCode}" | mail -s "ES-${entityName}:${esAddress}状态异常" ${email}
          done
         sleep ${sleepSeconds}
      continue
    fi

    echo `curl -H "Content-Type: application/json" -X POST  --data '{"query":{"bool":{"must":[{"match_all":{}}],"must_not":[],"should":[]}},"from":0,"size":1,"sort":[],"aggs":{}}' "http://${esAddress}/${entityName}/_search" `  > ${result}

   # cat ${result} >> ${logPath}

    #获取总数
    total=`awk -F[,:\"] '{print $30 }' ${result}`
    times=`awk -F[,:\"] '{print $4 }' ${result}`
    echo "${entityName}/_search返回结果：times=${times}毫秒,total=${total} " >>  ${logPath}

    #超时提醒
    if [ ${times} -gt 5000 ]; then
      echo "ES:${esAddress},查询结果耗时:${times}毫秒,超出阈值5000" >> ${logPath}
      #发送短信
       for mobile in ${mobileArray[*]}
       do
        echo "短信发送结果->${mobile}:"`curl "${sendMsgUrl}?mobile=${mobile}&content=ES:${esAddress}查询结果耗时:${times}毫秒,超出阈值5000"` >> ${logPath}
       done
      #发送邮件
       for email in ${emailArray[*]}
       do
         echo "ES:${esAddress}查询结果耗时:${times}毫秒,超出阈值5000" | mail -s "ES:${esAddress}状态异常" ${email} >> ${logPath}
       done
    fi

    #保存查询结果到文件，用于下次检查比对类型数变化
    echo ${total} > ${result}

    if [ -z "${total}" ]; then
          echo "发送ES-${entityName}异常提醒 调用接口http://${esAddress}/${entityName}/_search异常" >> ${logPath}
          #发送短信
          for mobile in ${mobileArray[*]}
            do
            echo  "短信发送结果->${mobile}:"`curl "${sendMsgUrl}?mobile=${mobile}&content=ES-${entityName}:接口/${entityName}/_search,返回异常"`  >>  ${logPath}
          done
          #发送邮件
          for email in ${emailArray[*]}
            do
            echo "ES-${entityName}:接口${esAddress}/${entityName}/_search 返回异常" | mail -s "ES-${entityName}:${esAddress}异常" ${email}
          done
          sleep ${sleepSeconds}
       continue
    fi

    if [ ! -z ${oldTotlal} ]; then
        if [ ${total} -le ${oldTotlal} ];then
          echo "ES-${entityName}:${esAddress} ${entityCName}总数异常,latestTotal：${oldTotlal},total：${total}" >>  ${logPath}
          echo "发送ES-${entityName}异常提醒" >>  ${logPath}
          #发送短信
          for mobile in ${mobileArray[*]}
            do
            echo  "短信发送结果->${mobile}:"`curl "${sendMsgUrl}?mobile=${mobile}&content=ES-${entityName}:${esAddress}，${entityCName}总数无变化"`  >>  ${logPath}
          done
          #发送邮件
          for email in ${emailArray[*]}
            do
            echo " ${entityCName}总数异常:latestTotal：${oldTotlal}，total：${total} " | mail -s "ES-${entityName}:${esAddress}异常，${entityCName}总数无变化" ${email}
          done
        else
            echo "=======ES-${entityName}:${esAddress} 运行正常，latestTotal：${oldTotlal}，total：${total}=======" >>  ${logPath}
        fi
      else
        echo "=======ES-${entityName}:${esAddress} 运行正常，latestTotal：${oldTotlal}，total：${total}=======" >>  ${logPath}
    fi

    echo "\n">>${logPath}

 sleep ${sleepSeconds}

done


