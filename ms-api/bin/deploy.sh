#!/bin/bash

. global.sh

echo_t "start deploy with params $@" >&2

if ! ./check-user.sh
then
	exit $?
fi

local_bin_dir=`pwd`
local_search_home=${local_bin_dir%/}
local_search_home=${local_search_home%/*}
remote_search_home=${local_search_home%%_deploy*}
remote_bin_dir="$remote_search_home/bin"
dt=`date +"%Y-%m-%d-%H-%M-%S"`
echo "date key:$dt"

usage="Usage: $0 [-e{online|fat|lpt|test|uat|dev} --default:online] [-f{all|common|none} --defalut:common] [-d --debug mode] [-nr --don't revoer,for engine] [-step {^all$|^deployserver$|^stopserver$|^deployengine$}]"

left_params=""
env_type="online"
files_type="common"
deploy_step="all"
while [ ! -z "$1" ]
do
	case "$1"  in
	
	"-e") 		
		if printf "$2"|grep -E '^online$|^test$|^uat$|^fat$|^lpt$|^dev$' >/dev/null
		then
			env_type=$2
			shift 2
		else
			echo_t $usage >&2
			exit 1
		fi
	;;
			
	"-f") 		
		if printf "$2"|grep -E '^all$|^common$|^none$' >/dev/null
		then
			files_type=$2
			shift 2
		else
			echo_t $usage >&2
			exit 1
		fi
	;;
	
	"-step")
		if printf "$2"|grep -E '^all$|^deployserver$|^stopserver$|^deployengine$' >/dev/null
		then
			deploy_step=$2
			shift 2		
		else
			echo_t $usage >&2
			exit 1
		fi
	;;
	
	"-ip") 
		deploy_engine_ips=$(printf $2 | tr ',' ' ')
		shift 2	 
	;;

	
	*)
		left_params="$left_params $1"
		shift
	;;
	esac;
	
done
left_params="$left_params -t $dt"


ssh_port=1022
echo ${local_search_home##*/} > ../deploy.version
echo $env_type > env.type

. env.sh

#stop server
stopped_servers=""
bin_copied_ips=""
function stop_server(){
	tag=$1
	server_ip=$2
	
	if printf "$stopped_servers "|grep " $tag$server_ip " >/dev/null
	then
		echo_t "$tag server $server_ip has been stopped,just skip" >&2
		return 0
	fi
	
	#update bin files
	if printf "$bin_copied_ips "|grep " $server_ip " >/dev/null
	then
		echo_t "server $server_ip bin files has been copied,just skip" >&2
	else
		src=$local_bin_dir
		dst="$server_ip:$remote_search_home"
		ssh -p $ssh_port $server_ip "if ! [ -a $remote_search_home ];then mkdir -p $remote_search_home; fi"
		if ! scp -P $ssh_port -r $local_bin_dir $dst
		then
			echo_t "failed to scp $src to $tag server $dst!!!" >&2
			cd $local_bin_dir
			return 1	
		else
			ssh -p $ssh_port $server_ip "cd $remote_bin_dir;chmod +x ./*"
			echo_t "succeed scp $src to $tag server $dst..." >&2
		fi
		bin_copied_ips="$bin_copied_ips $server_ip"
	fi

	echo_t "start stop $tag server $server_ip..." >&2
	if ! ssh -p $ssh_port $server_ip "if [ -a $remote_bin_dir/stop-$tag.sh ];then cd $remote_bin_dir;./stop-$tag.sh; fi"
	then
		echo_t "failed to stop $tag server $server_ip" >&2
		return 1
	else
		echo_t "succeed stop $tag server $server_ip!!!" >&2
	fi
	stopped_servers="$stopped_servers $tag$server_ip"
}

#update(stop server->update files->start server) engine server one by one
#so that the clients can always search
files_copied_ips=""
function update(){
	tag=$1
	server_ip=$2
	#echo_t "start update $tag server $server_ip..." >&2
	
	#stop server
	if ! stop_server $tag $server_ip
	then
		return 1
	fi
	
	#scp files
	if [ $files_type = "all" -o $files_type = "common" ]
	then
		if printf "$files_copied_ips "|grep " $server_ip " >/dev/null
		then
			echo_t "the files of $server_ip has been updated,just skip" >&2
		else
			#mkdirs
			if ! ssh -p $ssh_port $server_ip mkdir -p $remote_search_home
			then
				echo_t "failed to mkdir $remote_search_home recursive" >&2
				return 1	
			else
				echo_t "succeed mkdir $remote_search_home recursive" >&2
			fi
		
			#scp files
			files_pattern='^Arch.Search.*.jar$|^bin$|^conf$|^lib$|^pinyin.txt$|^playground.htm$|^deploy.version$|^env.type$|^template$|^zh2Hans.properties$'
			if [ $files_type = "common" ]
			then
				src=$(ls $local_search_home|grep -E $files_pattern|grep -Ev '^conf$|^Arch.Search.KeyGen.jar$')
			else
				echo_t "all file!!!"
				src=$(ls $local_search_home|grep -E $files_pattern)
			fi
			dst="$server_ip:$remote_search_home"

			echo_t "start scp files to $tag server $dst..." >&2
			cd ../
			if ! scp -P $ssh_port -r $src $dst
			then
				echo_t "failed to scp files to $tag server $dst!!!" >&2
				cd $local_bin_dir
				return 1	
			else
				ssh -p $ssh_port $server_ip "cd $remote_search_home;chmod -R 755 ./*;chmod 750 ./Arch.Search.KeyGen.jar ./conf/$index_name/loadConfig.properties;chmod 754 ./*.jar ./bin/*.sh"
				echo_t "succeed scp files to $tag server $dst..." >&2
			fi
			cd $local_bin_dir
						
			files_copied_ips="$files_copied_ips $server_ip"
		fi	
	fi
	
	#start server
	echo_t "start start $tag server $server_ip..." >&2
	if ! ssh -p $ssh_port $server_ip "cd $remote_bin_dir;./start-$tag.sh $left_params"
	then
		echo_t "failed to start $tag server $server_ip!!!" >&2
		return 1
	else
		echo_t "succeed start $tag server $server_ip!!!" >&2
	fi
	
	#echo_t "finish update $tag server $server_ip!!!" >&2
	return 0
}

if [ $deploy_step = "all" -o $deploy_step = "stopserver" ] 
then
	#stop monitor server
	echo_t ""
	if ! [ -z "$monitor_ip" ] && ! stop_server monitor $monitor_ip
	then
		return 1
	fi
fi

server_tags="rebuild scraper job"
if [ $deploy_step = "all" -o $deploy_step = "stopserver" ] 
then
	#stop all background servers
	for tag in $server_tags
	do
		echo_t ""
		eval server_ip="\$${tag}_ip"
		if ! [ -z $server_ip ] && ! stop_server $tag $server_ip
		then
			return 1
		fi
	done
fi

if [ $deploy_step = "all" -o $deploy_step = "deployengine" ] 
then
	if [ $deploy_step = "deployengine" ] 
	then
		engine_ips=$deploy_engine_ips
	fi
	
	#update all engine servers
	for server_ip in $engine_ips
	do
		echo_t ""
		#stop monitor-engine.sh
		stop_server monitor-engine $server_ip
		#ssh -p $ssh_port $server_ip "cd $remote_bin_dir;./stop-monitor-engine.sh"
		if ! update "engine" $server_ip
		then
			exit 1
		else 
			#start monitor-engine.sh
			if ssh -p $ssh_port $server_ip "cd $remote_bin_dir;./start-monitor-engine.sh $left_params"
			then
				echo_t "succeed start ./monitor-engine.sh on engine server $server_ip" >&2
			else
				echo_t "failed start ./monitor-engine.sh on engine server $server_ip" >&2
				exit 1
			fi
		fi		
	done
fi


if [ $deploy_step = "all" -o $deploy_step = "deployserver" ] 
then
	#update all background servers
	for tag in $server_tags
	do
		echo_t ""
		eval server_ip="\$${tag}_ip"
		if ! [ -z $server_ip ] && ! update $tag $server_ip
		then
			exit 1
		fi	
	done
fi

if [ $deploy_step = "all" -o $deploy_step = "deployserver" ] 
then
	#update monitor server
	echo_t ""
	if ! [ -z $monitor_ip ] && ! update monitor $monitor_ip
	then
		return 1
	fi
fi

echo_t "finish deploy" >&2

