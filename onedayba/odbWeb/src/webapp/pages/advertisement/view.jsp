<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>预览</title>
    <%@include file="/dw.jsp" %>
    <script type="text/javascript">
        <%
         String id = request.getParameter("id");
      %>
        var id = '<%=id%>' //这里是从request获取参数，赋值给name这个变量

    </script>
</head>
<body>
<div class="main">
    <div class="data-display common-mt" style="float: left; padding-left: 100px;">
        <%--<div class="dataTitle">--%>
        <div class="data-tool" style=" text-align: left ;width: 800px;">
            <button onclick="javascript:location.href = '/pages/advertisement/list.jsp'">关闭</button>
            <table style="margin-top: 20px;">
                <tr>
                    <td>计划主题:</td>
                    <td id="title1"></td>
                </tr>
                <tr>
                    <td>展示时间:</td>
                    <td id="time1"></td>
                </tr>
                <tr>
                    <td>展示位置:</td>
                    <td id="position1"></td>
                </tr>
                <tr>
                    <td>投放客户:</td>
                    <td id="custName"></td>
                </tr>
                <tr>
                    <td>总的点击量:</td>
                    <td id="clickNum"></td>
                </tr>
            </table>
            <div id="echart" style="height:400px"></div>
        </div>
    </div>

</div>
<script type="text/javascript" src="/pages/advertisement/js/view.js"></script>

