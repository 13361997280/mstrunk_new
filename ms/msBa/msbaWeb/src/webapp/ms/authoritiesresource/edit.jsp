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
            var key = document.getElementsByName("key")[0].value;
            var value = document.getElementsByName("value")[0].value;
            var enable = getRadioBoxValue("enable");
            if(name == ""  ){
                alert("资源名称不能为空");
                return false;
            }
            if(key == ""  ){
                alert("资源健不能为空");
                return false;
            }
            if(value == ""  ){
                alert("资源值不能为空");
                return false;
            }
            if(enable == ""  ){
                alert("是否激活不能为空");
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

                            <form role="form" id="formid" action="../authoritiesresource/save.do" method="post">
                                <div class="form-group"><label>资源名称</label>
                                    <input type="text" name="name" autocomplete="off" class="form-control" value="${entity.name}"></div>

                                <div class="form-group"><label>资源备注</label>
                                    <input type="text" name="remark" autocomplete="off" class="form-control" value="${entity.remark}"></div>

                                <div class="form-group"><label>资源健</label>
                                    <input type="text" name="key" autocomplete="off" class="form-control" value="${entity.key}"></div>

                                <div class="form-group"><label>资源值</label>
                                    <input type="text" name="value" autocomplete="off" class="form-control" value="${entity.value}"></div>

                                <div class="form-group"><label>是否激活</label>
                                    &nbsp;&nbsp;<label><input type="radio" name="enable" value="1" <c:if  test="${entity.enable  eq  '1'}"> checked </c:if>>是</label>
                                    &nbsp;&nbsp;<label><input type="radio" name="enable" value="0" <c:if  test="${entity.enable  eq  '0'}"> checked </c:if>>否</label>
                                <div>
                                    <input type="hidden" name="id" value="${entity.id}">
                                    <button class="btn btn-sm btn-primary" type="button" onclick = "checkForm();" ><strong>Save</strong></button>
                                    <button class="btn btn-sm btn-primary" type="button"
                                            onclick="javascript:location.href = '../authoritiesresource/list.do'">
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
