<%--
  Created by IntelliJ IDEA.
  User: songjie
  Date: 16/10/12
  Time: 上午11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <style type="text/css">
        #promotionTable tr td {
            line-height: 30px;
        }
    </style>
    <%@include file="/dw.jsp" %>
</head>
<body>

<div class="main">
    <!-- 表格数据区域 start -->
    <div class="table-column common-mt">
        <div class="dataTitle">
            <h3>广告管理</h3>
            <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/pages/advertisement/edit.jsp">新增</a> </span>
        </div>
        <div class="dataTable">
        </div>
        <a></a>
    </div>
    <div id="trendTableDiv"></div>
    <button id="callConfirm"></button>
    <div id="dialog" style="display: none" title="确认框">
    </div>
    <div id="errorMsg" title="消息框">
        <p style="color: red">
        </p>
    </div>
    <input type="hidden" value="https://qn-message.qbcdn.com" id="domain">
    <input type="hidden" value="/qiniu/getToken.do" id="uptoken_url">
    <div id="remove" title="确认框">
    </div>

    <!-- 表格数据区域 end -->
</div>
<div id="dialog-confirm" title="警告" style="display: none"></div>

<script type="text/javascript" src="/js/qiniu/main.js"></script>
<script type="text/javascript" src="/js/qiniu/ui.js"></script>
<script type="text/javascript" src="/js/qiniu/multiple.js"></script>
<script type="text/javascript" src="/js/qiniu/qiniu.js"></script>
<script type="text/javascript" src="/js/qiniu/plupload.min.js"></script>
<script type="text/javascript" src="/js/qiniu/plupload.full.min.js"></script>
<script type="text/javascript" src="/pages/report_render.js"></script>
<script type="text/javascript" src="/pages/advertisement/js/list.js"></script>
</body>
</html>
