<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>用户管理</title>

    <link href="../ms/css/bootstrap.min.css" rel="stylesheet">
    <link href="../ms/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="../ms/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="../ms/css/animate.css" rel="stylesheet">
    <link href="../ms/css/style.css" rel="stylesheet">

</head>

<body style="background-color: white">
<div id="wrapper">
    <div id="page-wrapper" style="margin:0 0 0 0">
        <div class="wrapper wrapper-content">
            <div class="row">
                <div class="col-lg-6">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>用户管理</h5>
                            <div class="ibox-tools">
                                <a href="../users/add.do">新增</a>
                            </div>
                        </div>
                        <div class="ibox-content">
                        <table class="table" style="width:1024px;" border="0">
                            <thead>
                            <tr>
                                <th>用户ID</th>
                                <th>用户名</th>
                                <th>有效性</th>
                                <th>修改时间</th>
                                <th>凭证</th>
                                <th>密钥</th>
                                <th>是否有权限</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${list}" var="item">
                                <tr>
                                    <td>${item.id}</td>
                                    <td>${item.username}</td>
                                    <td><c:choose><c:when  test="${item.enabled  eq  'true'}"> 有效</c:when><c:otherwise>无效</c:otherwise></c:choose></td>
                                    <td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd" /></td>
                                    <td>${item.appid}</td>
                                    <td>${item.secret}</td>
                                    <td><c:choose><c:when  test="${item.openapiStatus  eq  '1'}"> 是</c:when><c:otherwise>否</c:otherwise></c:choose></td>
                                    <td><a href="../users/edit.do?id=${item.id}">编辑</a>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <a href="../users/del.do?id=${item.id}">删除</a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tr>
                                <td>记录总数:${count}</td>
                                <td>当前页号:${pageNo}</td>
                                <td>总页数:${pageSum}</td>
                                <td><a href="../users/list.do?pageNo=${pageNo+1}">下一页</a></td>
                                <td><a href="../users/list.do?pageNo=${pageNo-1}">上一页</a></td>
                                <td><a href="../users/list.do?pageNo=1">第一页</a></td>
                                <td><a href="../users/list.do?pageNo=${pageSum}">最后页</a></td>
                                <td></td>
                            </tr>
                        </table>

                        </div>
                    </div>
                </div>

            </div>


        </div>

    </div>
</div>

<!-- Mainly scripts -->
<script src="../ms/js/jquery-2.1.1.js"></script>
<script src="../ms/js/bootstrap.min.js"></script>
<script src="../ms/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="../ms/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>


</body>

</html>

