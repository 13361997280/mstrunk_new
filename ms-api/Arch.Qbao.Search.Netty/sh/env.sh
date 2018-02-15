
#!/bin/bash

#×Ö·û±àÂë
source /etc/sysconfig/i18n

# export MAVEN_OPTS =-Xms256m -Xmx512m -Dfile.encoding=UTF-8
source /etc/profile

#ip
host_ip=$(/sbin/ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|awk '{print $2}'|tr -d "addr:"|head -1)
echo   ============================  ${host_ip} =============================================================

#current node
bin_path=`pwd`
echo bin_path ${bin_path}

node_path=${bin_path%bin}
echo node_path ${node_path}


logs_path=${node_path}logs/
echo logs_path ${logs_path}

core_path=${node_path}solr/
echo core_path ${core_path}


solr_lib_path=${node_path}solr-webapp/webapp/WEB-INF/lib/
echo solr_lib_path ${solr_lib_path}

zkp_update_path=${node_path}cloud-scripts/
echo zkp_updata_path ${zkp_update_path}

node_No=${node_path:(-6):1}
echo node_No : ${node_No}


zkp_root=${node_path}zkp/
echo zkp_root ${zkp_root}

zkp_conf=${zkp_root}conf/
echo zkp_conf ${zkp_conf}


zkp_data=${zkp_root}data/
echo zkp_data ${zkp_data}


echo   ============================  ${host_ip} ==========${node_No}node===================================================
