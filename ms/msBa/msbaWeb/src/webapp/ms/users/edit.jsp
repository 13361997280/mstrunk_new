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
            var oldusername = '${entity.username}';
            var username = document.getElementsByName("username")[0].value;
            var enabled = getRadioBoxValue("enabled");
            var id = document.getElementsByName("id")[0].value;
            var openapiStatus = getRadioBoxValue("openapiStatus");
            if(username.replace(/(^s*)|(s*$)/g, "").length ==0 ){
                alert("用户名不能为空");
                return false;
            }else{
                if(oldusername!=username) {
                    var htmlobj = $.ajax({url: "../users/exist.do?username=" + username, async: false});
                    var result = htmlobj.responseJSON;
                    if (result) {
                        alert("用户名已存在");
                        return false;
                    }
                }
            }
            //对用户名邮箱格式的验证
            var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            if(!myreg.test(username)){
                alert('请输入有效的用户名格式!');
                return false;
            }
            if(enabled == ""  ){
                alert("有效性不能为空");
                return false;
            }
            if(openapiStatus == ""  ){
                alert("是否有权限不能为空");
                return false;
            }
            document.getElementById("formid").submit();
        }
        function getRadioBoxValue(radioName){
            var obj = document.getElementsByName(radioName);  //这个是以标签的name来取控件
            for(var i=0; i<obj.length;i++) {
                if(obj[i].checked)    {
                    return   obj[i].value;
                }
            }
            return "";
        }
        function validateMail(email){
            var temp = document.getElementById("text1");
            //对电子邮件的验证
            var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            if(!myreg.test(temp.value)){
                alert('请输入有效的用户名格式!');
                myreg.focus();
                return false;
            }
            return true;
        }
    </script>
</head>

<body>

<div id="wrapper">
    <div id="page-wrapper" class="gray-bg"  style="margin:0 0 0 0">
        <div class="wrapper wrapper-content">
            <div class="row">
                <div class="col-lg-6">
                    <div class="ibox float-e-margins">

                        <div class="ibox-content">

                            <form role="form" id="formid" action="../users/save.do" method="post">
                                <div class="form-group"><label>用户名</label>
                                    <input type="text" name="username" class="form-control" autocomplete="off" value="${entity.username}"></div>

                                <div class="form-group"><label>有效性</label>
                                    &nbsp;&nbsp;<label><input type="radio" name="enabled" value="true" <c:if  test="${entity.enabled  eq  'true'}"> checked </c:if>>有效</label>
                                        &nbsp;&nbsp;<label><input type="radio" name="enabled" value="false" <c:if  test="${entity.enabled  eq  'false'}"> checked </c:if>>无效</label>
                                </div>
                                <%--<div class="form-group"><label>手机号</label>
                                    <input type="text" name="mobile" class="form-control" value="${entity.mobile}"></div>
--%>
                                <%--<div class="form-group"><label>组id</label>
                                    <input type="text" name="groupIds" class="form-control" value="${entity.groupIds}"></div>--%>
                                <%--<div class="form-group"><label>邮箱</label>
                                    <input type="text" name="email" class="form-control" value="${entity.email}"></div>--%>
                                <div class="form-group"><label>是否有权限</label>
                                    &nbsp;&nbsp;<label><input type="radio" name="openapiStatus" value="1" <c:if  test="${entity.openapiStatus  eq  '1'}"> checked </c:if>>是</label>
                                    &nbsp;&nbsp;<label><input type="radio" name="openapiStatus" value="0" <c:if  test="${entity.openapiStatus  eq  '0'}"> checked </c:if>>否</label>
                                </div>
                                <div>
                                    <input type="hidden" name="id" value="${entity.id}">
                                    <button class="btn btn-sm btn-primary" type="button"  onclick = "checkForm();"><strong>Save</strong></button>
                                    <button class="btn btn-sm btn-primary" type="button"
                                            onclick="javascript:location.href = '../users/list.do'">
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
