#!/bin/bash

. global.sh

LOCAL_IP=$(/sbin/ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|awk '{print $2}'|tr -d "addr:"|head -1)

#current bin dir
index_name=`pwd`
#delete the last / if exists
index_name=${index_name%/}
#to the build dir
index_name=${index_name%/*}
#get the name of the build dir
index_name=${index_name##/*/}
#delete the version info if exists
index_name=${index_name%%_deploy*}
Index_name=`echo $index_name | sed 's/^[[:lower:]]/\u&/'`

env_type=`cat env.type`

case $index_name in
	
	"vacation")
		port_prefix="80"
		port_key="9"

		#default except online
		engine_mem_info="-Xms128m -Xmx1024m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx512m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx512m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev")
				engine_ips="192.168.81.143 192.168.81.142"
				rebuild_ip="192.168.81.143"
				scraper_ip="192.168.81.143"
				job_ip="192.168.81.143"
				monitor_ip="192.168.81.143"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.81.64"
				scraper_ip="192.168.81.64"
				job_ip="192.168.81.64"
				monitor_ip="192.168.81.64"
			;;
			
			"uat") 
				engine_ips="192.168.82.221 192.168.83.125"
				rebuild_ip="192.168.81.63"
				scraper_ip="192.168.81.63"
				job_ip="192.168.81.63"
				monitor_ip="192.168.81.63"
			;;
			
			"fat") 
				engine_ips="10.2.6.15 10.2.6.16"
				rebuild_ip="10.2.6.21"
				scraper_ip="10.2.6.21"
				job_ip="10.2.6.21"
				monitor_ip="10.2.6.21"
				ssh_port=22
			;;
			
			"lpt") 
				engine_ips="10.2.4.35 10.2.4.36"
				rebuild_ip="10.2.4.41"
				scraper_ip="10.2.4.41"
				job_ip="10.2.4.41"
				monitor_ip="10.2.4.41"
				ssh_port=22
			;;
			
			"online") 
				engine_ips="192.168.86.250 192.168.86.251 192.168.86.252 192.168.86.123 192.168.86.124 192.168.86.166 192.168.86.167 192.168.86.168"
				rebuild_ip="192.168.86.253"
				scraper_ip="192.168.86.253"
				job_ip="192.168.86.253"
				monitor_ip="192.168.86.253"
				
				engine_mem_info="-Xms1G -Xmx6G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1G -Xmn200m -Xss128k"
				job_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;			
		esac
	;;
	
	"markland")
		port_prefix="80"
		port_key="7"	

		#default except online
		engine_mem_info="-Xms128m -Xmx1536m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev") 
				engine_ips="192.168.81.143 192.168.81.142"
				#engine_ips="192.168.83.100"
				rebuild_ip="192.168.81.142"
				monitor_ip="192.168.81.142"
				engine_mem_info="-Xms128m -Xmx1536m -Xmn64m -Xss512k"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.81.223"
				monitor_ip="192.168.81.223"
			;;
			
			"uat") 
				engine_ips="192.168.82.221 192.168.83.125"
				rebuild_ip="192.168.81.65"
				monitor_ip="192.168.81.65"
			;;
			
			"fat") 
				engine_ips="10.2.6.15 10.2.6.16"
				rebuild_ip="10.2.6.21"
				monitor_ip="10.2.6.21"
				ssh_port=22
			;;	
			
			"lpt") 
				engine_ips="10.2.4.35 10.2.4.36"
				rebuild_ip="10.2.4.41"
				monitor_ip="10.2.4.41"
				ssh_port=22
			;;
			
			"online") 
				engine_ips="192.168.79.36 192.168.79.37 192.168.79.71 192.168.79.72 192.168.79.73 192.168.79.74 192.168.79.75"
				rebuild_ip="192.168.86.253"
				monitor_ip="192.168.86.253"
				
				engine_mem_info="-Xms1G -Xmx4G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1G -Xmn200m -Xss128k"
				job_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;			
		esac
	;;

	"guide")
		port_prefix="80"
		port_key="6"	
		
		#default except online
		engine_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev") 
				engine_ips="192.168.81.145"
				rebuild_ip="192.168.81.142"
				#scraper_ip="192.168.81.142"
				job_ip="192.168.81.142"
				monitor_ip="192.168.81.142"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.83.222"
				#scraper_ip="192.168.83.222"
				job_ip="192.168.83.222"
				monitor_ip="192.168.83.222"
			;;
			
			"uat") 
				engine_ips="192.168.82.221 192.168.83.125"
				rebuild_ip="192.168.83.125"
				#scraper_ip="192.168.83.125"
				job_ip="192.168.83.125"
				monitor_ip="192.168.83.125"
			;;
			
			"fat") 
				engine_ips="10.2.6.15 10.2.6.16"
				rebuild_ip="10.2.6.21"
				#scraper_ip="10.2.6.21"
				job_ip="10.2.6.21"
				monitor_ip="10.2.6.21"
				ssh_port=22
			;;
			
			"online") 
				engine_ips="192.168.79.72 192.168.79.73 192.168.79.74 192.168.79.75"
				rebuild_ip="192.168.86.253"
				#scraper_ip="192.168.83.222"
				job_ip="192.168.86.253"
				monitor_ip="192.168.86.253"
				
				engine_mem_info="-Xms1G -Xmx3G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1G -Xmn200m -Xss128k"
				job_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;			
		esac
	;;	
	
	"scenicspot")
		port_prefix="80"
		port_key="5"	
		
		#default except online
		engine_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev") 
				engine_ips="192.168.81.144 192.168.81.145"
				rebuild_ip="192.168.81.145"
				scraper_ip="192.168.81.145"
				monitor_ip="192.168.81.145"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.81.64"
				scraper_ip="192.168.81.64"
				monitor_ip="192.168.81.64"
			;;
			
			"uat") 
				engine_ips="192.168.82.15 192.168.81.65"
				rebuild_ip="192.168.81.65"
				scraper_ip="192.168.81.65"
				monitor_ip="192.168.81.65"
			;;
			
			"fat") 
				engine_ips="10.2.6.15 10.2.6.16"
				rebuild_ip="10.2.6.21"
				scraper_ip="10.2.6.21"
				monitor_ip="10.2.6.21"
				ssh_port=22
			;;
			
			"lpt") 
				engine_ips="10.2.4.35 10.2.4.36"
				rebuild_ip="10.2.4.41"
				scraper_ip="10.2.4.41"
				monitor_ip="10.2.4.41"
				ssh_port=22
			;;
			
			"online") 
				engine_ips="192.168.79.72 192.168.79.73 192.168.79.74 192.168.79.75"
				rebuild_ip="192.168.86.253"
				scraper_ip="192.168.86.253"
				monitor_ip="192.168.86.253"
				
				engine_mem_info="-Xms1G -Xmx3G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1G -Xmn200m -Xss128k"
				job_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;			
		esac
	;;	
	"shx")
		port_prefix="80"
		port_key="8"	
		
		#default except online
		engine_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev") 
				engine_ips="192.168.81.144 192.168.81.145"
				rebuild_ip="192.168.81.145"
				scraper_ip="192.168.81.145"
				monitor_ip="192.168.81.145"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.81.64"
				scraper_ip="192.168.81.64"
				monitor_ip="192.168.81.64"
			;;
			
			"uat") 
				engine_ips="192.168.82.15 192.168.81.65"
				rebuild_ip="192.168.81.65"
				scraper_ip="192.168.81.65"
				monitor_ip="192.168.81.65"
			;;
			
			"fat") 
				engine_ips="10.2.6.15 10.2.6.16"
				rebuild_ip="10.2.6.21"
				scraper_ip="10.2.6.21"
				monitor_ip="10.2.6.21"
				ssh_port=22
			;;
			
			"lpt") 
				engine_ips="10.2.4.35 10.2.4.36"
				rebuild_ip="10.2.4.41"
				scraper_ip="10.2.4.41"
				monitor_ip="10.2.4.41"
				ssh_port=22
			;;
			
			"online") 
				engine_ips="10.8.91.56 10.8.91.57 10.8.91.58"
				rebuild_ip="192.168.86.253"
				scraper_ip="192.168.86.253"
				monitor_ip="192.168.86.253"
				
				engine_mem_info="-Xms1G -Xmx3G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1G -Xmn200m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;		
		esac
	;;	
	
	"expansion")
		port_prefix="80"
		port_key="4"	

		#default except online
		engine_mem_info="-Xms128m -Xmx512m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev") 
				engine_ips="192.168.81.143 192.168.81.142"
				#engine_ips="192.168.83.100"
				rebuild_ip="192.168.81.142"
				monitor_ip="192.168.81.142"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.81.223"
				monitor_ip="192.168.81.223"
			;;		
			
			"online") 
				engine_ips="10.8.91.113 10.8.91.114"
				rebuild_ip="10.8.91.112"
				monitor_ip="10.8.91.112"
				
				engine_mem_info="-Xms1G -Xmx4G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1G -Xmn200m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;
		esac
	;;
	
	"global")
		port_prefix="80"
		port_key="2"	

		#default except online
		engine_mem_info="-Xms128m -Xmx2048m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx1024m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev") 
				engine_ips="192.168.81.143 192.168.81.142"
				rebuild_ip="192.168.81.142"
				monitor_ip="192.168.81.142"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.81.223"
				monitor_ip="192.168.81.223"
			;;		
			
			"online") 
				engine_ips="10.8.91.113 10.8.91.114 10.8.91.115  10.8.91.116  10.8.91.117"
				rebuild_ip="10.8.91.112"
				monitor_ip="10.8.91.112"
				
				engine_mem_info="-Xms1G -Xmx4G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1G -Xmn200m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;
		
		esac
	;;
	"globalautocomplete")
		port_prefix="80"
		port_key="1"	

		#default except online
		engine_mem_info="-Xms128m -Xmx1536m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx1024m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev") 
				engine_ips="192.168.81.143 192.168.81.142"
				rebuild_ip="192.168.81.142"
				monitor_ip="192.168.81.142"
				engine_mem_info="-Xms128m -Xmx1536m -Xmn64m -Xss512k"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.81.223"
				monitor_ip="192.168.81.223"
			;;
			
			"uat") 
				engine_ips="192.168.82.221 192.168.83.125"
				rebuild_ip="192.168.81.65"
				monitor_ip="192.168.81.65"
			;;
			
			"fat") 
				engine_ips="10.2.6.15 10.2.6.16"
				rebuild_ip="10.2.6.21"
				monitor_ip="10.2.6.21"
				ssh_port=22
			;;	
			
			"lpt") 
				engine_ips="10.2.4.35 10.2.4.36"
				rebuild_ip="10.2.4.41"
				monitor_ip="10.2.4.41"
				ssh_port=22
			;;
			
			"online") 
				engine_ips="10.8.91.113 10.8.91.114 10.8.91.115  10.8.91.116  10.8.91.117"
				rebuild_ip="10.8.91.112"
				monitor_ip="10.8.91.112"
				
				engine_mem_info="-Xms1G -Xmx4G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1536m -Xmn200m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;
		esac
	;;
	"autocomplete")
		port_prefix="80"
		port_key="3"	

		#default except online
		engine_mem_info="-Xms128m -Xmx1536m -Xmn64m -Xss512k"
		scraper_mem_info="-Xms128m -Xmx512m -Xmn64m -Xss128k"
		rebuild_mem_info="-Xms128m -Xmx512m -Xmn64m -Xss128k"
		job_mem_info="-Xms128m -Xmx256m -Xmn64m -Xss128k"
		monitor_mem_info="-Xms128m -Xmx128m -Xmn64m -Xss128k"		
		case $env_type  in
			"dev") 
				engine_ips="192.168.81.143 192.168.81.142"
				rebuild_ip="192.168.81.143"
				monitor_ip="192.168.81.143"
				engine_mem_info="-Xms128m -Xmx1536m -Xmn64m -Xss512k"
			;;
			
			"test") 
				engine_ips="192.168.81.222 192.168.83.222"
				rebuild_ip="192.168.81.223"
				monitor_ip="192.168.81.223"
			;;
			
			"uat") 
				engine_ips="192.168.82.221 192.168.83.125"
				rebuild_ip="192.168.81.65"
				monitor_ip="192.168.81.65"
			;;
			
			"fat") 
				engine_ips="10.2.6.15 10.2.6.16"
				rebuild_ip="10.2.6.21"
				monitor_ip="10.2.6.21"
				ssh_port=22
			;;	
			
			"lpt") 
				engine_ips="10.2.4.35 10.2.4.36"
				rebuild_ip="10.2.4.41"
				monitor_ip="10.2.4.41"
				ssh_port=22
			;;
			
			"online") 
				engine_ips="10.8.91.113 10.8.91.114"
				rebuild_ip="10.8.91.112"
				monitor_ip="10.8.91.112"
				
				engine_mem_info="-Xms1G -Xmx4G -Xmn700m -Xss512k"
				scraper_mem_info="-Xms500m -Xmx500m -Xmn100m -Xss128k"
				rebuild_mem_info="-Xms1G -Xmx1536m -Xmn200m -Xss128k"
				monitor_mem_info="-Xms200m -Xmx200m -Xmn100m -Xss128k"				
			;;			
		esac
	;;	
esac
