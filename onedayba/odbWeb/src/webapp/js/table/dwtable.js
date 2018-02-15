var DATE_FORMAT = 'yyyy/MM/dd';
var DATEPICKER_FORMAT = 'yy/mm/dd';

function GetDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    return y+"/"+m+"/"+d;
}

$.fn.dataTable.Api.register( 'sum()', function () {
    return this.reduce( function (a, b) {
        var x = parseFloat( a ) || 0;
        var y = parseFloat( b ) || 0;
        return x + y;
    } );
});

var REG_INT = /^-?\d+$/;

function testReg(re, s){
	return re.test(s);
}

//$.fn.dataTable.ext.legacy.ajax = true;

Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

(function(window,$,undefined){
	$.fn.extend({
		dwtable:function(){
			var render = {
				tableId:"",
				_table:null,
				startDate:"",
				endDate:"",
				otherConditions:{},
				tableConfig:{
					paging:false,
					pageLength:10,
					lengthChange: true,
					searching:false,
					processing:false,
					ordering:true,
					info:false,
					isSummary:true,
					isAllSummary:false,
					deferRender:false,
					ajax:null,
					serverSide:false,
					columnDefs:[],
					sumLabel:null
				},//columns,columnDefs
				fdataCnt:-1,
				fdata:[],
				fdataCntUrl:"",
				fdataUrl:"",	
				init : function(settings){
					var deep = true;
					if(settings && settings.deep != undefined && !settings.deep){
						deep = false;
					}
					$.extend(true, this, settings);
					if(!deep){
						this.tableConfig = {
								paging:false,
								lengthChange: true,
								ordering:true,
								processing:false,
								info:false,
								isSummary:true,
								isAllSummary:false,
								deferRender:false,
								ajax:null,
								serverSide:false,
								columnDefs:[]
							};
						$.extend(this.tableConfig, settings.tableConfig);
					}
					this.initData();
				},
				initData : function(){
					this.fdataCnt = -1;
					this.fdata = [];
					this.sdata = [];
					this.loadFdataCnt();
					if(this.fdataCnt > 0){
						this.loadFdata();
						this.formatPlaceholderForData(this.fdata, _.pluck(this.tableConfig.columns, "data"));
						if(this.isComp){
							this.loadSdata();
							if(this.sdataCnt > 0){
								this.formatPlaceholderForData(this.sdata, _.pluck(this.tableConfig.columns, "data"));
							}
						}
					}
				},
				loadFdataCnt : function(){
					var _this = this;
					$.ajax({
						url:_this.fdataCntUrl,
						async:false,
						data:$.extend({},{
								startDate:_this.startDate,
								endDate:_this.endDate
							},_this.otherConditions),
						success:function(ret){
								_this.fdataCnt = ret.data;
							}
					});
				},
				loadFdata : function(){
					var _this = this;
					$.ajax({
						url:_this.fdataUrl,
						async:false,
						data:$.extend({},{
								startDate:_this.startDate,
								endDate:_this.endDate
							},_this.otherConditions),
						success:function(ret){
							_this.fdata = ret.data;
							}
					});
				},
				getData : function(options){
					var data = null;
					$.ajax({
						url:options.url,
						async:false,
						data:options.params,
						success:function(ret){
							data = ret.data;
							}
					});
					return data;
				},
				formatPlaceholderForData : function(data, names){
					var dataItemSize = data && data[0] && _.size(data[0]) || 0;
					if(dataItemSize == names.length)return data;
					_.each(data,function(item){
						_.each(_.rest(names, dataItemSize),function(ele){
							item[ele]="";
						});
					});
				},	
				renderTable : function (){
					var _this = this;
					if(!_this.isComp){
						var t = $("#" + _this.tableId).DataTable({
							destroy: true,
							"scrollX": true,
							"paging": _this.tableConfig["paging"],
							"pageLength": _this.tableConfig["pageLength"],
							searching:_this.tableConfig["searching"],
							processing:_this.tableConfig["processing"],
							info:_this.tableConfig["info"],
							"order": _this.tableConfig["order"],//[[ 1, 'desc' ]],
							ordering:_this.tableConfig["ordering"],
							"data": _this.fdata,
							"lengthChange": _this.tableConfig["lengthChange"],
							//"deferRender":_this.tableConfig["deferRender"],
							ajax:_this.tableConfig["ajax"]?{
								url:_this.fdataUrl,
								data:$.extend({},{
									startDate:_this.startDate,
									endDate:_this.endDate
								},_this.otherConditions),
							}:null,
							serverSide:_this.tableConfig["serverSide"],
							"deferLoading": _this.fdataCnt,
							"columns": _this.tableConfig["columns"],
				            "columnDefs": _this.tableConfig["columnDefs"],
							"drawCallback": function ( settings ) {
								var api = this.api();
					            var rows = api.rows( {page:'current'} ).nodes();

								if($(rows).length > 0 && $($(rows).get(0)).is("tr")){
									if(_this.tableConfig.columnStyle){
										$(rows).each(function(ele){
											var __this = this;
											if($(__this).has("td")){
												_.each(_this.tableConfig.columnStyle.index,function(el){
													_.each(_this.tableConfig.columnStyle.style,function(e,i){
														$(__this).find("td:eq("+el+")").css(i,e);
													});
												});
											}
										});
									}
								}

					            if(_this.tableConfig.isSummary){
						            if($(rows).length > 0 && $($(rows).get(0)).is("tr")){
					            		var summaryTr = $($(rows).last()).after(
					            			function(){
						                        var html = '<tr style="font-weight:bold;" class="group"><td>'+(_this.tableConfig.sumLabel||'当页汇总')+'</td>';
						                        for(var i = 1; i < api.columns().dataSrc().length; i++){
						                        	if(api.column(i).visible()){
						                        		if(_.indexOf(_this.tableConfig.calcColumns, api.column(i).dataSrc()) != -1) {
							                        		var ret = api.column(i).data().sum();
							                        		ret = $.isNumeric(ret) ? (testReg(REG_INT, ret) ? ret : ret.toFixed(2)) : '--';
							                        		var endWithPercent = _.every(api.column(i).data(),function(ele){
							                        			return ele && ele.toString().indexOf("%") != -1;
							                        		});
							                        		if(endWithPercent){
							                        			ret += "%";
							                        		} else {
							                        			ret = d3.format(",")(ret);
							                        		}

							                        		html += '<td>'+ret+'</td>';
							                        	} else {
							                        		html += '<td></td>';
							                        	}
						                        	}
						                        }
						                        html += '</tr>';
						                        return html;
					            			}
					                    );

										if(_this.tableConfig.columnStyle){
											if($(summaryTr).next().has("td")){
												_.each(_this.tableConfig.columnStyle.index,function(el){
													_.each(_this.tableConfig.columnStyle.style,function(e,i){
														$(summaryTr).next().find("td:eq("+el+")").css(i,e);
													});
												});
											}
										}
						            }
					            } else if(_this.tableConfig.isAllSummary){
						            if($(rows).length > 0 && $($(rows).get(0)).is("tr")){
						            	var _data = _this.getData({
		                    				url:_this.allSUmmaryUrl,
		                    				params:$.extend({},{
												startDate:_this.startDate,
												endDate:_this.endDate
											},_this.otherConditions)
		                    			});
						            	if(_data&&_data[0]){
						            		$($(rows).last()).after(
						            			function(){
							                        var html = '<tr style="font-weight:bold;" class="group"><td>'+(_this.tableConfig.sumLabel)+'</td>';
							                        for(var i = 1; i < api.columns().dataSrc().length; i++){
							                        	if(api.column(i).visible()){
							                        		if(_.indexOf(_this.tableConfig.calcColumns, api.column(i).dataSrc()) != -1) {
			//					                        		var ret = api.column(i).data().sum();
								                        		var ret = _data[0][api.column(i).dataSrc()];
								                        		ret = $.isNumeric(ret) ? (testReg(REG_INT, ret) ? ret : ret.toFixed(2)) : '--';
								                        		var endWithPercent = _.every(api.column(i).data(),function(ele){
								                        			return ele && ele.toString().indexOf("%") != -1;
								                        		});
								                        		if(endWithPercent){
								                        			ret += "%";
								                        		} else {
								                        			ret = d3.format(",")(ret);
								                        		}

								                        		html += '<td>'+ret+'</td>';

								                        	} else {
								                        		html += '<td></td>';
								                        	}
							                        	}
							                        }
							                        html += '</tr>';
							                        return html;
						            			}
						                    );
						            	}
						            }
					            }
							},
							"rowCallback":function(row,data){
								var api = this.api();
								_this.formatData(api,row,data);
								_this.formatTag(api,row,data);
								_this.onTblChange(api,row,data);
								if(_this.tableConfig.customRowCallback){
									_this.tableConfig.customRowCallback(this,row, data);
								}
							}
						});
						_this._table = t;
						_this.renderChildTable();

						_this.renderSearch();
						
						_this.onTblSubmit();
						
						// 图形为线图时，加载图表即触发第一行线图加载
						if(_.indexOf(["line", "linePlusLine"], _this.chartType) != -1){
							// 首次加载
							window.onload = function(){
								$('#' + _this.tableId  + ' tbody tr:eq(0)').trigger( 'click');
							}
							// 其他情况加载
							$('#' + _this.tableId  + ' tbody tr:eq(0)').trigger( 'click');
						}
						
						/*
						t.on( 'order.dt search.dt', function () {
					        t.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
					            cell.innerHTML = i+1;
					        } );
					    } ).draw();*/
					}
				},
				formatData : function(api,row,data){ 
					var _this = this;
					var abbColumns = _.chain(_this.tableConfig.columns)
										.filter(function(ele){
											return _.has(ele,"abbLen");
										})
										.pluck("data")
										.value();
					for(var i=0;i<api.columns().dataSrc().length;i++){
						var dataSrc = api.column(i).dataSrc();
						var td = $("td", row).eq(i);
						if($.inArray(dataSrc, _this.tableConfig.calcColumns) != -1){// NaN->'--';'1.2345%'->'1.23%'
							if(!isFinite(parseFloat($.trim(td.html())))){
								td.html('--');
							} else {
								if($.trim(td.html()).indexOf("%") != -1){
									td.html(parseFloat($.trim(td.html())).toFixed(2) + "%");
								}
							}
						}
						if($.inArray(dataSrc,abbColumns) != -1){//abbfdaffdafda->abb...
							var currColumn = _.find(_this.tableConfig.columns,{"data":dataSrc});
							var content = data[dataSrc];//$.trim(td.html());
							var content_abb = content.length > currColumn.abbLen ? (content.slice(0,currColumn.abbLen) + "...") : content;
							td.html(content_abb);
							td.attr("title",content);
						}
					};
				},
				formatTag : function(api,row,data){
					var _this = this;
					for(var i=0;i<api.columns().dataSrc().length;i++){
						var dataSrc = api.column(i).dataSrc();
						if(_.indexOf(_.pluck(_this.tableConfig.formatTags, "name"), dataSrc) != -1){
							var td = $("td", row).eq(i);
							var text = $.trim(td.text());
							var type = _.findWhere(_this.tableConfig.formatTags, {"name":dataSrc})["type"];
							switch(type){
							case "text":
								td.html('<input type="text" name="' + dataSrc + "_" + $.trim($("td", row).eq(0).text()) + '" value="' + text + '" />');
								break;
							case "title":
								td.attr("title",data[_.findWhere(_this.tableConfig.formatTags, {"name":dataSrc})["col"]]);
								break;
							case "select":
								var options = _.findWhere(_this.tableConfig.formatTags, {"name":dataSrc})["options"];
								var html = '<select name="' + dataSrc + "_" + $.trim($("td", row).eq(0).text()) + '">';
								_.each(options,function(txt, val){
									html += '<option value="'+val+'"'+(text==val?' selected':'')+'>'+txt+'</option>';
								});
								html += '</select>';
								td.html(html);
								break;
							case "checkbox":
								var options = _.findWhere(_this.tableConfig.formatTags, {"name":dataSrc})["options"];
								var html = '';
								var j = 0;
								var items = text.split(",");
								var rowSize = _.findWhere(_this.tableConfig.formatTags, {"name":dataSrc})["rowSize"];
								_.each(options,function(txt, val){
									html += '<input type="checkbox" name="' + dataSrc + "_" + $.trim($("td", row).eq(0).text()) + '" value="'+val+'"'+(_.indexOf(items,val)!=-1?' checked':'')+' />'+txt;
									if(++j>=rowSize){
										html+="<br>";
										j=0;
									}
								});
								td.html(html);
								break;
							default:
								console.err("unsupported html tag type(" + type + ")!");
							}
						}
					};
				},
				renderGroup : function(api){
					var _this = this;
					var rows = api.rows( {page:'current'} ).nodes();
		            var last=null;
		            api.column(_this.tableConfig.columnCompGroup, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                    $(rows).eq( i ).before(
		                        '<tr class="group"><td colspan="'+_.compact(api.columns().visible()).length+'">'+group+'</td></tr>'
		                    );
		                    last = group;
		                }
		            });
		            
				},
				orderByGroup : function(){
					var _this = this;
					$('#'+_this.tableId+' tbody').off( 'click', 'tr.group');//TODO
					$('#'+_this.tableId+' tbody').on( 'click', 'tr.group', function () {
						var t = _this._table;
						var currentOrder = t.order()[0];
						if ( currentOrder[0] === _this.tableConfig.columnCompGroup && currentOrder[1] === 'asc' ) {
							t.order( [ _this.tableConfig.columnCompGroup, 'desc' ] ).draw();
						}
						else {
							t.order( [ _this.tableConfig.columnCompGroup, 'asc' ] ).draw();
						}
					} );			
				},
				renderSum : function(api){
					var _this = this;
					var size = _this.tableConfig.calcColumns.length;
		        	var sum1 = new Array(size), 
		        		sum2 = new Array(size),
		        		changeRatio = new Array(size);
		        	$.each(sum1,function(i,n){sum1[i] = 0});
		        	$.each(sum2,function(i,n){sum2[i] = 0});
		        	$.each(changeRatio,function(i,n){changeRatio[i] = 0});
		        	api.rows(function(idx, data, node){
		        		if(data.dateRange == (_this.startDate + " - " + _this.endDate)){
		        			for(var index in _this.tableConfig.calcColumns){
		        				sum1[index] += parseFloat(data[_this.tableConfig.calcColumns[index]]);
		        			}
		            	}
		            	else if(data.dateRange == (_this.startDateComp + " - " + _this.endDateComp)){
		            		for(var index in _this.tableConfig.calcColumns){
		        				sum2[index] += parseFloat(data[_this.tableConfig.calcColumns[index]]);
		        			}
		            	}
		        	})
		        	
		        	if((_this.startDate + " - " + _this.endDate) == (_this.startDateComp + " - " + _this.endDateComp)){
		        		sum1 = _.map(sum1, function(num){
		        			return num / 2;
		        		});
		        		sum2 = sum1;
		        	}
		        	
		        	for(var index in _this.tableConfig.calcColumns){
		        		changeRatio[index] = ((sum1[index] - sum2[index]) / sum2[index] * 100).toFixed(2);
					}
		        	var colspan = _.compact(api.columns().visible()).length - _this.tableConfig.calcColumns.length;
		        	var trs = '<tr style="font-weight:bold;" class="group"><td colspan="'+_.compact(api.columns().visible()).length+'">'+(_this.tableConfig.sumLabel||'当页汇总')+'</td></tr>';
		        	var tr1 = '<tr style="font-weight:bold;" class="group"><td colspan="'+colspan+'"></td<td>'+(_this.startDate + " - " + _this.endDate)+'</td>';
		        	var tr2 = '<tr style="font-weight:bold;" class="group"><td colspan="'+colspan+'"></td<td>'+(_this.startDate + " - " + _this.endDate)+'</td>';
		        	var tr3 = '<tr style="font-weight:bold;" class="group"><td colspan="'+colspan+'"></td<td>变化率</td>';
		        	for(var index in _this.tableConfig.calcColumns){
		        		var data1 = (!isFinite(sum1[index]) ? '--' : (testReg(REG_INT, sum1[index]) ? sum1[index] : sum1[index].toFixed(2)));
		        		var data2 = (!isFinite(sum2[index]) ? '--' : (testReg(REG_INT, sum2[index]) ? sum2[index] : sum2[index].toFixed(2)));
		        		var endWithPercent = _.every(api.column(_this.tableConfig.calcColumns[index] + ":name").data(),function(ele){
		        			return !ele || ele.toString().indexOf("%") != -1;
		        		});
		        		if(endWithPercent){
		        			data1 = (data1 == "--" ? d3.format(",")(data1) : (data1+"%"));
		        			data2 = (data2 == "--" ? d3.format(",")(data2) : (data2+"%"));
		        		}
		        		tr1 += '<td>' + data1 +'</td>';
		        		tr2 += '<td>' + data2 +'</td>';
		        		tr3 += '<td>' + (!isFinite(changeRatio[index]) ? '--' : (changeRatio[index]) + '%') +'</td>';
					}
		        	tr1 += '</tr>'
		        	tr2 += '</tr>'
		        	tr3 += '</tr>'
		        	trs += tr1;
		        	trs += tr2;
		        	trs += tr3;     	
		    		$(api.rows().nodes()).last().after(trs);
				},
				renderChangeRate:function(api){
					var _this = this;
					var prevData = {};
		    		api.rows(function(idx, data, node){
		    			if(data.dateRange == (_this.startDate + " - " + _this.endDate)){
		    				prevData = data;
		    			}
		    			else if(data.dateRange == (_this.startDateComp + " - " + _this.endDateComp)){
		    				var colspan = _.compact(api.columns().visible()).length - _this.tableConfig.calcColumns.length;
		    				var tr = '<tr class="group"><td colspan="'+ colspan +'">变化率</td>';
		    				for(var key in _this.tableConfig.calcColumns){
		    					var num1 = parseFloat(prevData[_this.tableConfig.calcColumns[key]]), 
		    						num2 = parseFloat(data[_this.tableConfig.calcColumns[key]]);
		        				var numChange = (!isFinite(num1) || !isFinite(num2)) ? '--' : ((num1 - num2)/num2 * 100).toFixed(2) + '%';
		    					var td = '<td>' + numChange + '</td>';
		    					tr += td;
		    				}
		    				$(node).after(tr);
		    			};
		    			return true;
		    		});
				},
				renderChildTable:function(){
					var _this = this;
					var t = _this._table;
					if(!_this.tableConfig.childRow){
						return;
					}
					// Add event listener for opening and closing details
					t.off('click', 'td.details-control');
				    t.on('click', 'td.details-control', function () {
				    	var t = _this._table;
				        var tr = $(this).closest('tr');
				        var row = t.row( tr );
				        var parentKey = row.data()[_this.tableConfig.parentKey];
				 
				        if ( row.child.isShown() ) {
				            row.child.hide();
				            tr.removeClass('shown');
				        }
				        else {
				        	var dateRange = row.data()["dateRange"];
				        	var data = _this.getData({
				        		url:_this.tableConfig.childUrl,
				        		params:$.extend({
				        			startDate: dateRange.split(" - ")[0],
				        			endDate: dateRange.split(" - ")[1],
				        			parentKey:parentKey
				        		},_this.tableConfig.childParams)
				        	});
				        	var trs = "";
				        	for(var e in data){
				        		var html = '<tr>';
		                        for(var i = 0, dataSrc = t.columns().dataSrc(); i < dataSrc.length; i++){
		                        	if(t.column(i).visible()) {
		                        		if(dataSrc[i] == null){
		                        			html += "<td></td>";	                        		
		                        		} else {
											var conditionObj = {};
											conditionObj.data = dataSrc[i];
											var format = _.findWhere(_this.tableConfig.columns,conditionObj).render||function(_data){return _data};
		                        			html += '<td>'+format(data[e][dataSrc[i]])+'</td>';
		                        		}
		                        	}
		                        }
		                        html += '</tr>';
		                        trs += html;
				        	}
				            row.child($(trs)).show();
				            tr.addClass('shown');
				        }
				    } );
				},
				renderSearch: function(){
					var _this = this;
					if(!_this.tableConfig.searchColumns)return;
					var tfoot = [];
					tfoot.push("<tfoot><tr>");
					_.each(_.pluck(_this.tableConfig.columns, 'title'),function(i,e){
						tfoot.push('<th>'+i+'</th>');
					});
					tfoot.push("</tr></tfoot>");
					$('#'+_this.tableId).append(tfoot.join(""));
					$('#'+_this.tableId+' tfoot th').each( function (index) {
						var title = $(this).text();
						$(this).html( '<input type="text" style="width:'+$($('#'+_this.tableId+' thead th').eq(index)).width()+'px" placeholder="'+title+'" />' );
					});
					_this._table.columns().eq(0).each( function (index) {
						var that = _this._table.columns(index);

						$( 'input', that.footer() ).on( 'keyup change', function () {
							if ( that.search() !== this.value ) {
								that
									.search( this.value )
									.draw();
							}
						} );
					});
				},
		//		onTblChange : function(){
		//			var _this = this;
		//			if(!_this.tableConfig.submitBtnId)return;
		//			$('#' + _this.tableId + ' tbody tr input,select').on('keyup', function () {
		//				this.value=this.value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'');
		//			});
		//			$('#' + _this.tableId + ' tbody tr input,select').on('change', function () {
		//				var tags = $('#' + _this.tableId).data("changedTags") || {};
		//				tags[this.name] = this.value;
		//				$('#' + _this.tableId).data("changedTags", tags);
		//				if(_this.tableConfig.autoSubmit){
		//					$("#" + _this.tableConfig.submitBtnId).trigger("click");
		//				}
		//			});
		//		},
				onTblChange : function(api,row,data){
					var _this = this;
					if(!_this.tableConfig.submitBtnId)return;
					$('input,select',row).on('keyup', function () {
						this.value=this.value.replace(/[^-|\d{1,}\.\d{1,}|\d{1,}]/g,'');
					});
					$('input,select',row).on('change', function () {
						var tags = $('#' + _this.tableId).data("changedTags") || {};
						if(this.type=="checkbox" || this.type=="radio"){							
							tags[this.name] = _.map($('input[name="'+this.name+'"]:checked'),function(ele){return $(ele).val()}).join(",");
						}else{							
							tags[this.name] = this.value;
						}
						$('#' + _this.tableId).data("changedTags", tags);
						if(_this.tableConfig.autoSubmit){
							var msgObj = $("<label style='color:red'>数据提交成功！</label>");
							$(this).after(msgObj);
							msgObj.fadeOut(1500, function(){
								msgObj.remove();
							});
							$("#" + _this.tableConfig.submitBtnId).trigger("click");
						}
					});
				},
				onTblSubmit : function(){
					var _this = this;
					if(!_this.tableConfig.submitBtnId)return;
					$("#" + _this.tableConfig.submitBtnId).on("click", function(){
						var tags = $('#' + _this.tableId).data("changedTags");
						if(!tags)return false;
						var params = $.param(tags);
						$('#' + _this.tableId).removeData("changedTags");
						$.post(_this.tableConfig.submitUrl, params, function(ret){
							if(!_this.tableConfig.autoSubmit){
								var msgObj = $("<label style='color:red'>数据提交成功！</label>");
								$("#" + _this.tableConfig.submitBtnId).after(msgObj);
								msgObj.fadeOut(3000, function(){
									msgObj.remove();
								});
								_this.initData();
								_this.renderTable();
							}
						});
						return false;
					});
				}
			};
	
			var options = $.extend({},{tableId:$(this).attr("id")},arguments.length>0?arguments[0]:{});
			render.init(options);
			render.renderTable();
			return {
				refresh:function(settings){
					render.init(settings);
					render.renderTable();
				}
			};
		}
	});
})(window,jQuery);