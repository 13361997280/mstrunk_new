$(function () {

    $.ajax({
        url: "/advertisement/" + id + '/get.do',
        type: 'get',
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                var data = result.data;
                document.getElementById('title1').innerHTML = data.title;
                document.getElementById('time1').innerHTML = data.startTime + "至" + data.endTime;
                var position = data.position;
                var chestr = "";
                if (position != null){
                    var cbArray = new Array;
                    cbArray = position.split(",");
                    for (var i = 0; i < cbArray.length; i++) {
                        if (cbArray[i] == 0) {
                            chestr += "首页" + ",";
                        } else if (cbArray[i] == 1) {
                            chestr += "新闻列表" + ",";
                        } else if (cbArray[i] == 2) {
                            chestr += "新闻详情" + ",";
                        }
                    }
                    if (chestr.length > 1) {
                        chestr = chestr.substr(0, chestr.length - 1);
                    }
                 }
                document.getElementById('position1').innerHTML=chestr;
                document.getElementById('custName').innerHTML=data.custName;
                $.ajax({
                        url: "//logs.qbao.com/qbaolog/search?pageId=" + id,
                        type: 'get',
                        dataType: 'json',
                        success: function (result) {
                            var data = result.data;
                            document.getElementById('clickNum').innerHTML=data.total;
                            if(data.total > 0) {
                                // 路径配置
                                require.config({
                                    paths: {
                                        echarts: '/js/echarts'
                                    }
                                });
                                // 使用
                                require(
                                    [
                                        'echarts',
                                        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
                                    ],
                                    function (ec) {
                                        // 基于准备好的dom，初始化echarts图表
                                        var myChart = ec.init(document.getElementById('echart'));

                                        var option = {
                                            tooltip: {
                                                show: true
                                            },
                                            xAxis: [
                                                {
                                                    type: 'category',
                                                    data: data.detail.x
                                                }
                                            ],
                                            yAxis: [
                                                {
                                                    type: 'value'
                                                }
                                            ],
                                            series: [
                                                {
                                                    "name": "点击量",
                                                    "type": "bar",
                                                    "data": data.detail.y
                                                }
                                            ]
                                        };

                                        // 为echarts对象加载数据
                                        myChart.setOption(option);
                                    }
                                );
                            }
                        },
                        error: function (result) {
                            alert(result);
                        }
                });

            }
        },
        error: function (result) {
            alert(result);
        }
    });
});

