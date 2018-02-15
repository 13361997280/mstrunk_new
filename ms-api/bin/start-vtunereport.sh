#!/bin/bash

if [ -z $1 ]
then
	echo "Useage:$0 cmd,cmd maybe:hotspots|locksandwaits"
	exit 1
fi
collect_type="$1"
dt=`date +"%Y-%m-%d"`
shift

hs_dir="hs$dt"
lw_dir="lw$dt"
report_dir=/usr/arch/search/vacation/bin/report
hs_report_output=/usr/arch/search/vacation/bin/report/hsreport
lw_report_output=/usr/arch/search/vacation/bin/report/lwreport
	
if [ ! -d $report_dir ]
then
	sudo mkdir -p $report_dir
fi
if [ ! -d $hs_report_output ]
then
	sudo mkdir -p $hs_report_output
fi
if [ ! -d $lw_report_output ]
then
	sudo mkdir -p $lw_report_output
fi

if [ -d $hs_dir ]
then 
	#sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report summary -result-dir $hs_dir -report-output="$hs_report_output/hs1_report$dt.csv" -format=csv -csv-delimiter=comma
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report hotspots -result-dir $hs_dir -report-output="$hs_report_output/hs2_report$dt.csv" -format=csv -csv-delimiter=comma --call-stack-mode user-only
	#sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report wait-time -result-dir $hs_dir -report-output="$hs_report_output/hs3_report$dt.csv" -format=csv -csv-delimiter=comma
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report callstacks -result-dir $hs_dir -report-output="$hs_report_output/hs4_report$dt.csv" -format=csv -csv-delimiter=comma
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report top-down -result-dir $hs_dir -report-output="$hs_report_output/hs5_report$dt.csv" -format=csv -csv-delimiter=comma
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report perf-detail -result-dir $hs_dir -report-output="$hs_report_output/hs6_report$dt.csv" -format=csv -csv-delimiter=comma
	#sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -R perf -r hs2013-01-09 -r hs2013-01-08 -cumulative-threshold-percent 50
fi
if [ -d $lw_dir ]
then 
	#sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report summary -result-dir $lw_dir -report-output="$lw_report_output/lw1_report$dt.csv" -format=csv -csv-delimiter=comma
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report hotspots -result-dir $lw_dir -report-output="$lw_report_output/lw2_report$dt.csv" -format=csv -csv-delimiter=comma
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report wait-time -result-dir $lw_dir -report-output="$lw_report_output/lw3_report$dt.csv" -format=csv -csv-delimiter=comma --call-stack-mode user-only
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report callstacks -result-dir $lw_dir -report-output="$lw_report_output/lw4_report$dt.csv" -format=csv -csv-delimiter=comma
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report top-down -result-dir $lw_dir -report-output="$lw_report_output/lw5_report$dt.csv" -format=csv -csv-delimiter=comma
	sudo /opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -report perf-detail -result-dir $lw_dir -report-output="$lw_report_output/lw6_report$dt.csv" -format=csv -csv-delimiter=comma
fi

