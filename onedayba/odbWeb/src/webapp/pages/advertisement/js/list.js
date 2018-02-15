$(function () {
    var tableSetting = {
        tableDivId: 'trendTableDiv',
        tableId: 'trendTable',
        isPaging: false,
        tableHtml: '<table class="display" cellspacing="0" width="100%"></table>',
        tableConfig: {
            serverSide: false,
            iDisplayLength:8,
            columns: [                
                {data: 'title', title: '计划主题', sWidth: '160px'},
                {data: 'time', title: '展示时间', sWidth: '250px'},
                {data: 'spendTime', title: '时长', sWidth: '40px'},
                {data: 'custName', title: '客户', sWidth: '40px'},
                {data: 'status', title: '状态',sWidth: '40px'},
                {data: 'updateStmp', title: '发布时间', sWidth: '160px'},
                {data: 'operator', title: '发布者', sWidth: '70px'},
                {data: 'id', title: '操作', sWidth: '200px'}
            ],
            columnDefs: [
                {
                    "targets": [0],
                    "render": function (data, type, full) {
                        var title = full.title;
                        if(title.length>20){
                            title = title.substring(0,20)+"...";
                        }
                        return title;
                    }
                },
                {
                    "targets": [1],
                    "render": function (data, type, full) {
                        var dataTemp = full.startTime+" 至 "+full.endTime;
                        return dataTemp;
                    }
                },
                {
                    "targets": [2],
                    "render": function (data, type, full) {
                        if(full.startTime===null||full.endTime===null) return 0;
                        var startTime = new Date(Date.parse(full.startTime.replace(/-/g,   "/"))).getTime();
                        var endTime = new Date(Date.parse(full.endTime.replace(/-/g,   "/"))).getTime();
                        var dates = parseInt(Math.abs((startTime - endTime))/(1000*60*60*24));
                        return dates+"天";
                    }
                },
                {
                    "targets": [4],
                    "data": "status",
                    "render": function (data, type, full) {
                        var releaseStr = "待发布";
                        if(data === 1){
                            releaseStr = "已发布";
                        }else if(data===2){
                            releaseStr = "已下架";
                        }
                        return releaseStr;
                    }
                },
                {
                    "title": "操作",
                    "targets": [7],
                    // "data": "id",
                    "render": function (data, type, full) {
                        var editHtml = "";
                        var startTime = new Date(Date.parse(full.startTime.replace(/-/g,   "/"))).getTime();
                        var endTime = new Date(Date.parse(full.endTime.replace(/-/g,   "/"))).getTime();
                        var nowTime = new Date().getTime();
                        var timeRang = false;
                        if(nowTime>=startTime&&nowTime<=endTime){
                            timeRang = true;
                        }
                        if (full.status == 0 || full.status == 1 ) {
                            editHtml += "<a href='#' id='edit_" + full.id + "' onclick=\"editContent(" + full.id + ")\">修改</a>";
                        }
                        if (full.status == 0) {
                            editHtml += " <a href='#' onclick=\"remove(" + full.id + ")\">删除</a>";
                        }
                        if (timeRang) {
                            editHtml += " <a href='#' onclick=\"edit(" + full.id + ",1)\">发布</a>";
                        }
                            editHtml += " <a href='#' onclick=\"edit("+full.id+",2)\">下架</a>";
                            editHtml += " <a href='#' onclick=\"view("+full.id+")\">预览</a>";

                        return editHtml;
                    }
                }
            ],
            order: [[7, "desc"]],
            ajax: {
                data: {},
                type: 'get',
                url: '/advertisement/getList.do'
            }
        }
    };
    /**
     * 绘制本页图形与表格
     */
    var drawChartAndTablePage = function (_dateParams) {
        //Render Table
        tableSetting.tableConfig.ajax.data = $.extend({}, _dateParams);
        RenderTable.render(tableSetting);
    };
    drawChartAndTablePage({});
    window.tableSetting = tableSetting;
});

function editContent(id) {
    location.href = '/pages/advertisement/edit.jsp?id='+id;
}


//发布或下架首页推广
function edit(id, status) {
    $("#dialog").text('确认本次操作!');
    $("#dialog").dialog({
        autoOpen: true,
        modal: true,
        buttons: {
            "确认": function () {
                $(this).dialog("close");
                $.ajax({
                    url: "/advertisement/" + id + "/updateStatus.do?status=" + status,
                    type: "get",
                    success: function (result) {
                        if (result.success) {
                            RenderTable.render(window.tableSetting);
                        } else {
                            $("#errorMsg").dialog({
                                buttons: {
                                    "Ok": function () {
                                        $(this).dialog("close");
                                    }
                                }
                            });

                            // $("#errorMsg").dialog("show");
                            $("#errorMsg p").text(result.data);
                        }
                    },
                    error: function (result) {
                        alert("error");
                    }
                })
            },
            "取消": function () {
                $(this).dialog("close");
            }
        }
    });
}
//预览
function view(id) {
    location.href = '/pages/advertisement/view.jsp?id='+id;
}

function remove(id) {
    $("#remove").text('确认删除？');
    $("#remove").dialog({
        autoOpen: true,
        modal: true,
        buttons: {
            "确认": function () {
                $.ajax({
                    url: "/advertisement/" + id + "/delete.do",
                    type: "DELETE",
                    success: function (result) {
                        if (result.success) {
                            RenderTable.render(window.tableSetting);
                            $("#errorMsg p").text('');
                        } else {
                            $("#errorMsg p").text(result.data);
                        }
                    },
                    error: function (result) {
                        alert("error");
                    }
                });
                $(this).dialog("close");
            },
            "取消": function () {
                $(this).dialog("close");
            }
        }
    });
}