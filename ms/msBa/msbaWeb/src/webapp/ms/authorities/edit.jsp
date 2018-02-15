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
    <script type="text/javascript">
        function checkForm(){
            var result = document.getElementsByName("userid")[0].value;
            var authority = document.getElementsByName("authority")[0].value;
            if(result == ""  ){
                alert("用户名不能为空");
                return false;
            }
            if(authority == ""  ){
                alert("权限不能为空");
                return false;
            }
            document.getElementById("formid").submit();
        }
    </script>
</head>

<body style="background-color: white">

<div id="wrapper">
    <div id="page-wrapper" class="gray-bg" style="margin-left: 0px;padding-left: 0px;padding-right: 0px;">
        <div class="wrapper wrapper-content">
            <div class="row">
                <div class="col-lg-6">
                    <div class="ibox float-e-margins">

                        <div class="ibox-content">

                            <form role="form" id="formid" action="../authorities/save.do" method="post" >
                                <div class="form-group"><label>用户名</label>
                                    <select class="input-sm form-control input-s-sm inline"  style="height: 36px;" name="userid">
                                        <option value="">--请选择--</option>
                                        <c:forEach items="${userList}" var="item">
                                            <option value="${item.id}" <c:if  test="${entity.userid  eq  item.id}"> selected </c:if> >${item.username}</option>
                                        </c:forEach>
                                    </select>
                                <div class="form-group"><label>权限</label>
                                    <select class="input-sm form-control input-s-sm inline" style="height: 36px;" name="authority">
                                        <option value="">--请选择--</option>
                                        <c:forEach items="${list}" var="item">
                                            <option value="${item.id}" <c:if  test="${entity.authority  eq  item.id}"> selected </c:if> >${item.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div>
                                    <input type="hidden" name="id" value="${entity.id}">
                                    <button class="btn btn-sm btn-primary" type="button" onclick = "checkForm();"><strong>Save</strong></button>
                                    <button class="btn btn-sm btn-primary" type="button"
                                            onclick="javascript:location.href = '../authorities/list.do'">
                                        <strong>Cancel</strong></button>
                                </div>
                            </form>

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
