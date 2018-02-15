var nav = {


    treeList: [
        {
            "name": "首页",
            "url": "main/body.html",
            "type": "node",
            "isShow" : true
        }, {
            "name": "树",
            "type": "tree",
            "children": [
                {
                    "name": "树儿子1",
                    "url": "main/body.html",
                    "type": "node"
                },
                {
                    "name": "树儿子2",
                    "url": "main/body.html",
                    "type": "node"
                },{
                    "name": "树儿子3",
                    "url": "main/body.html",
                    "type": "node"
                }
            ]
        }
    ],

    load: function () {

        var tree = $("#side-menu");

        var list = this.treeList;

        var sb = "";
        for (var i = 0; i < list.length; i++) {
            var node = list[i];
            if (node.type == "tree") {
                sb += this.liHtml(node.isShow,node.name,null);
                sb += this.ulHtml();
                for (var j= 0; j<node.children.length;j++){
                    sb += this.liHtml(node.children[j].isShow,node.children[j].name,node.children[j].url);
                    sb += this.liEndHtml();
                }
                sb += this.ulEndHtml();
                sb += this.liEndHtml();

            } else  if (node.type = "node "){
                sb += this.liHtml(node.isShow,node.name,node.url);
                sb += this.liEndHtml();
            }
        }
        tree.append(sb);
    },

    ulHtml : function(){
        var a = "<ul class=\"nav nav-second-level collapse\">";
        return a;
    },

    ulEndHtml:function () {
        return "</ul>";
    },

    liHtml:function (isShow,name,url) {
        var active = isShow ? "active" : "";
        var a = "<li class=\"" + active + "\"> <a onclick='nav.nodeClick(\"" + url + "\",this)'>" + name + "</a>";
        return a;
    },
    liEndHtml:function () {
        return "</li>";
    },

    nodeClick:function (url,elm) {
        console.info(url);
        console.info(elm);
        this.hideAll();
        if (url == null || url == 'null'){
            $(elm).next().removeClass('collapse');
        }
        $(elm).parent().addClass('active')
    },
    hideAll:function () {
        console.info($("#side-menu"));
        console.info($("#side-menu").children("li"));
        $("#side-menu").children("li").removeClass("active");
    }


}

$(document).ready(function () {
    console.info(1);
    nav.load();
})

//
// <li class="active">
//     <a href="index.html"><span class="nav-label">Dashboards</span> <span
// class="fa arrow"></span></a>
//     <ul class="nav nav-second-level">
//     <li class="active"><a href="index.html">Dashboard v.1</a></li>
// <li><a href="dashboard_2.html">Dashboard v.2</a></li>
// <li><a href="dashboard_3.html">Dashboard v.3</a></li>
// <li><a href="dashboard_4_1.html">Dashboard v.4</a></li>
// <li><a href="dashboard_5.html">Dashboard v.5 <span class="label label-primary pull-right">NEW</span></a>
//     </li>
//     </ul>
//     </li>
//     <!--<li>-->
//     <!--<a href="layouts.html"><i class="fa fa-diamond"></i> <span class="nav-label">Layouts</span></a>-->
//     <!--</li>-->
//     <li>
//     <a href="nav.html#">Graphs</a>
//     <ul class="nav nav-second-level collapse">
//     <li><a href="graph_flot.html">Flot Charts</a></li>
// <li><a href="graph_morris.html">Morris.js Charts</a></li>
// <li><a href="graph_rickshaw.html">Rickshaw Charts</a></li>
// <li><a href="graph_chartjs.html">Chart.js</a></li>
//     <li><a href="graph_chartist.html">Chartist</a></li>
//     <li><a href="c3.html">c3 charts</a></li>
// <li><a href="graph_peity.html">Peity Charts</a></li>
// <li><a href="graph_sparkline.html">Sparkline Charts</a></li>
// </ul>
// </li>