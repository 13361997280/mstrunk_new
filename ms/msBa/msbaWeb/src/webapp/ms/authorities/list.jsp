<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Static Tables</title>

    <link href="../ms/css/bootstrap.min.css" rel="stylesheet">
    <link href="../ms/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="../ms/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="../ms/css/animate.css" rel="stylesheet">
    <link href="../ms/css/style.css" rel="stylesheet">

</head>

<body style="background-color: white">

<div id="wrapper">
    <div id="page-wrapper" style="margin-left: 0px;padding-left: 0px;padding-right: 0px;">
        <div class="wrapper wrapper-content">
            <div class="row">
                <div class="col-lg-6">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>权限分配</h5>
                            <div class="ibox-tools">
                                <a href="../authorities/add.do">新增</a>
                            </div>
                        </div>
                        <div class="ibox-content">

                            <table class="table">
                                <thead>
                                <tr>
                                    <th>id</th>
                                    <th>用户名</th>
                                    <th>权限</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list}" var="item">
                                    <tr>
                                        <td>${item.id}</td>
                                        <td>${item.username}</td>
                                        <td>${item.authorityName}</td>
                                        <td><a href="../authorities/edit.do?id=${item.id}">编辑</a>
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="../authorities/del.do?id=${item.id}">删除</a></td>
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
