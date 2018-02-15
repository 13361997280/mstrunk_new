<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>资源组管理</title>

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
                            <h5>资源组管理</h5>
                            <div class="ibox-tools">
                                <a href="../authoritiesresource/add.do">新增</a>
                            </div>
                        </div>
                        <div class="ibox-content">

                            <table class="table">
                                <thead>
                                <tr>
                                    <th>资源ID</th>
                                    <th>资源名称</th>
                                    <th>资源描述</th>
                                    <th>唯一标识</th>
                                    <th>资源路径</th>
                                    <th>是否激活</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list}" var="item">
                                    <tr>
                                        <td>${item.id}</td>
                                        <td>${item.name}</td>
                                        <td>${item.remark}</td>
                                        <td>${item.key}</td>
                                        <td>${item.value}</td>
                                        <td><c:choose><c:when  test="${item.enable  eq  '1'}"> 是</c:when><c:otherwise>否</c:otherwise></c:choose></td>
                                        <td><a href="../authoritiesresource/edit.do?id=${item.id}">编辑</a>
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="../authoritiesresource/del.do?id=${item.id}">删除</a></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
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