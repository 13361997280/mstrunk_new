<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
            var name = document.getElementsByName("name")[0].value;
            var resouceIds = document.getElementsByName("resouceIds")[0].value;
            if(name == ""  ){
                alert("权限名称不能为空");
                return false;
            }
            if(resouceIds == ""  ){
                alert("资源不能为空");
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

                            <form role="form" id="formid" action="../authoritiesgroup/save.do" method="post">
                                <div class="form-group"><label>权限名称</label>
                                    <input type="text" name="name" class="form-control" autocomplete="off" value="${entity.name}"></div>
                                <div class="form-group"><label>资源</label>
                                    <c:forEach items="${list}" var="item">
                                        <input type="checkbox" name="resouceIds" value="${item.id}"
                                         <c:if  test="${fn:containsIgnoreCase(entity.resouceIds, item.id)}"> checked </c:if>
                                        >${item.name}&nbsp;&nbsp;
                                    </c:forEach>
                                    </select>
                                </div>
                                <div>
                                    <input type="hidden" name="id" value="${entity.id}">
                                    <button class="btn btn-sm btn-primary" type="button"  onclick = "checkForm();"><strong>Save</strong></button>
                                    <button class="btn btn-sm btn-primary" type="button"
                                            onclick="javascript:location.href = '../authoritiesgroup/list.do'">
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
