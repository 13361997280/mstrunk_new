/**
 * 报表-公共方法
 */
var inheritPrototype = function(subType, superType) {
    var prototype = Object.create(superType.prototype);
    prototype.constructor = subType;
    subType.prototype = prototype;
}

/*============================================================ DataTable ============================================================*/
function BasicDataTable(){
	this.isSub = false,
	this.table = null;
	this.tableId = '';
	this.tableDivId = '';
	this.isPaging = false;
	this.tableHtml = '';
	this.tableConfig = null;
	this.pagingTableConfig = null;
}
BasicDataTable.prototype.render = function(){
	if(this.isSub){
		//do nothing.
	}else{
		$('#' + this.tableDivId).empty().append($(this.tableHtml).attr('id', this.tableId));
	}
	this.table = $('#' + this.tableId).dataTable(this.tableConfig);
};
BasicDataTable.prototype.init = function(_config){
	this.isSub = _config.isSub;
	this.tableId = _config.tableId;
	this.tableDivId = _config.tableDivId;
	this.tableHtml = _config.tableHtml;
	this.tableConfig = {
		destory: true,
		processing: true,
		searching: false,
		paginate: false
	};
	this.pagingTableConfig = {
		pagingType: 'full_numbers',
		paginate: true,
		lengthChange: false
	};
	if(_config.isPaging){
		$.extend(this.tableConfig, this.pagingTableConfig);
	}
	$.extend(this.tableConfig, _config.tableConfig);
};

function NestedDataTable(){
	BasicDataTable.call(this);
	this.initCallBack = null;
}
inheritPrototype(NestedDataTable, BasicDataTable);


/**
 * 渲染DataTable
 */
var RenderTable = (function($,window){
	var nestedDataTable = new NestedDataTable();//嵌套Table
	var dataTable;
	var render = function(_config){
		switch(_config.tableType){
		case '':
			break;
		case '':
			break;
		default:
			dataTable = nestedDataTable;
		}
		dataTable.init(_config);
		dataTable.render();
		return dataTable.table;
	};
	return {
		render: function(_config){
			render(_config);
		}
	};
})(jQuery,window);

/*============================================================ NVD3 Chart ============================================================*/
function BasicChart(){
	this.chart = null;
	this.svgId = '';
	this.chartData = '';
	this.initCompleteCallBack = null;
	this.chartConfig = null;
}
BasicChart.prototype.chartDataConvert = function(_rawData,_demin){
	var dealDatas = [];
	$.each(_demin.yItems,function(index, yItem){
		var series = [];
		dealDatas.push({
			key: yItem.name,
			area: yItem.area,
			bar: yItem.bar,
			color: yItem.color,
			disabled: yItem.disabled,
			values : series
		});
		$.each(_rawData, function(_idx, _data){
			var xvalue = _data[_demin.xItem];
			if(_demin.xtype == 'date')
				xvalue = moment(xvalue)._d;
			series.push({
				x: xvalue,
				y: (function(_value){
					return _value?_value:0;
				})(_data[yItem.field])
			});
		});
	});
	return dealDatas;
};

BasicChart.prototype.init = function(_config){
	this.svgId = _config.svgId;
	this.svgConfig = _config.svgConfig;
	if($.isFunction(_config.initCompleteCallBack)){
		this.initCompleteCallBack = _config.initCompleteCallBack;
	}
	this.chartConfig = {
		margin:{},
		tickFormat:{
			xAxisFormat: '%Y/%m/%d',
			yAxisFormat: ','
		},
		dataConfig: {
			demin: {},
			url: '',
			params: {},
			convert: null
		}
	};
	$.extend(true, this.chartConfig, _config);
	
	/**
	 * Ajax数据请求
	 */
	var requireAjaxData = function(_url, _params){
		var _callBackRawData;
		$.ajax({
			url: _url,
			data: _params,
			type: 'post',
			async: false,
			success: function(_data){
				if(_data.success){
					_callBackRawData = _data.data;
				}
			}
		});
		return _callBackRawData;
	};
	
	this.chartData = (function(_this){
		var data;
		var _dataConfig = _this.chartConfig.dataConfig;
		
		var _rawData = requireAjaxData(_dataConfig.url,_dataConfig.params);
		if(_dataConfig.params.isComp){//比较
			var _cParams = $.extend(true, {}, _dataConfig.params);
			_cParams.startDate = _dataConfig.params.startDateComp;
			_cParams.endDate = _dataConfig.params.endDateComp;
			var _cRawData = requireAjaxData(_dataConfig.url,_cParams);
			data = dw.chart.lineCompDataConvert(_rawData,_cRawData,_dataConfig.demin);
		}else{//不比较
			if(_dataConfig.demin){
				var _demin = _dataConfig.demin;
				if($.isFunction(_dataConfig.convert)){//如果自定义数据转换函数有效
					data = _dataConfig.convert(_rawData, _demin);	
				}else{//也可以通过在子类中重写chratDataConvert方法
					data = _this.chartDataConvert(_rawData, _demin);
				}
			}
		}
		return data;
	})(this);
};

BasicChart.prototype.render = function(){
	$('#' + this.svgId).parent()
		.empty()
		.append($('<svg id="' + this.svgId + '"></svg>').attr(this.svgConfig));
	nv.addGraph(this.chart);
};

/**
 * 堆栈或多柱图-MultiBar
 */
function MultiBarChart(){
	BasicChart.call(this);
};
inheritPrototype(MultiBarChart, BasicChart);
MultiBarChart.prototype.render = function(){
	var me = this;
	this.chart = function() {
		var _chart = nv.models.multiBarChart()
//		.barColor(d3.scale.category10().range())
		.transitionDuration(300)
		.delay(0)
		.groupSpacing(0.1)
		.rotateLabels(me.chartConfig.rotateAngle)
		.margin(me.chartConfig.margin)
		.stacked(me.chartConfig.stacked)
      ;
		_chart.multibar.hideable(true);
		_chart.reduceXTicks(false).staggerLabels(true);
		_chart.xAxis
	        .showMaxMin(true)
	        .tickFormat(function(d){
	        	var _defaultXFormat = '%m/%d'; //默认横坐标-日期
	        	if(me.chartConfig.axisConfig && me.chartConfig.axisConfig.xAxisFormat){
	        		_defaultXFormat = me.chartConfig.axisConfig.xAxisFormat;
	        	}
	        	return d3.time.format(_defaultXFormat)(new Date(d));
	        });
		_chart.yAxis
	        .tickFormat(function(d){
	        	var _defaultYFormat = ','; //默认纵坐标-数字
	        	if(me.chartConfig.axisConfig && me.chartConfig.axisConfig.yAxisFormat){
	        		_defaultYFormat = me.chartConfig.axisConfig.yAxisFormat;
	        	}
	        	return d3.format(_defaultYFormat)(d);
	        });
	
	    d3.select('#' + me.svgId)
	      .datum(function(_data){
	    	  var colors = d3.scale.category10().range();
	    	  $.each(_data, function(_index, _item){
	    		  _item.color = colors[_index];
	    	  });
	    	  return _data;
	      }(me.chartData))
	      .call(_chart);
	    //Chart Element Click
	    if($.isFunction(me.chartConfig.elementClick)){
	    	_chart.multibar.dispatch.on('elementClick',function(e){
	    		me.chartConfig.elementClick({statDate:e.point.x.format('yyyy-MM-dd')});
            });
	    }
	    nv.utils.windowResize(_chart.update);
	    _chart.dispatch.on('stateChange', function(e) { nv.log('New State:', JSON.stringify(e)); });
	    return _chart;
	};
	BasicChart.prototype.render.apply(this, arguments);
};
/**
 * 线图-Line
 */
function LineChart(){
	BasicChart.call(this);
}
inheritPrototype(LineChart, BasicChart);
LineChart.prototype.render = function(){
	var me = this;
	this.chart = function() {
		var _chart = nv.models.lineChart()
		.useInteractiveGuideline(true)
		.margin(me.chartConfig.margin);
		_chart.xAxis.tickFormat(function(d) {
			var _defaultXFormat = '%m/%d';
			if(me.chartConfig.axisConfig && me.chartConfig.axisConfig.xAxisFormat){
				_defaultXFormat = me.chartConfig.axisConfig.xAxisFormat;
			}
			return d3.time.format(_defaultXFormat)(new Date(d));
		});
		_chart.yAxis.tickFormat(function(d){
			var _defaultYFormat = ',';
			if(me.chartConfig.axisConfig && me.chartConfig.axisConfig.yAxisFormat){
				_defaultYFormat = me.chartConfig.axisConfig.yAxisFormat;
			}
			return d3.format(_defaultYFormat)(d);
		});
		d3.select('#' + me.svgId)
		  .datum(me.chartData)
		  .transition()
		  .duration(500)
		  .call(_chart);
		//Chart Element Click
		if($.isFunction(me.chartConfig.elementClick)){
			_chart.interactiveLayer.dispatch.on('elementDblclick',function(e){
				var _clickedParam = {},dateFormat = 'MM/DD/YYYY',standDateFormat='yyyy-MM-dd';
				var clickedX = moment(e.pointXValue).format(dateFormat);
				_clickedParam.currentDate = new Date(clickedX).format(standDateFormat);
				if(me.chartData.length>1){
					$.each(me.chartData[1].values,function(_i,_value){
						var _x = moment(_value.x).format(dateFormat);
						if(_x == clickedX){
							_clickedParam.compareDate = new Date(_value.c).format(standDateFormat);
						}
					});
				}
				me.chartConfig.elementClick(_clickedParam);
			});
		}
		nv.utils.windowResize(_chart.update);
		return _chart;
	};
	BasicChart.prototype.render.apply(this, arguments);
};

var RenderChart = (function($,window){
	var multiBarChart = new MultiBarChart();//堆栈或多柱图
	var lineChart = new LineChart();//线图
	
	var chartRender;
	var render = function(_config){
		switch(_config.chartType){
		case 'MultiBarChart':
			chartRender = multiBarChart;
			break;
		case 'LineChart':
			chartRender = lineChart;
			break;
		default:
			console.log('unknown chart type.');
		}
		chartRender.init(_config);
//		console.log(chartRender);
		chartRender.render();
	};
	return {
		render: function(_config){
			render(_config);
		}
	};
})(jQuery,window);