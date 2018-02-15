<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>编辑</title>
    <style type="text/css">
        #promotionTable tr td {
            line-height: 40px;
        }
    </style>
    <%@include file="/dw.jsp" %>
    <script type="text/javascript">
        <%
         String id = request.getParameter("id");
      %>
        var id = '<%=id%>'; //这里是从request获取参数，赋值给name这个变量

    </script>
</head>
<body>
<button id="callConfirm"></button>
<div id="dialog" style="display: none" title="确认">
    确认本次操作!
</div>​
<div class="main">
    <div class="data-display common-mt" style="float: left; padding-left: 100px;">
        <div class="data-tool" style=" text-align: left ;width: 800px;">
            <button id="saveDemo"> 保存</button>
            <button id="confirmRelease" style="margin-left: 20px;">保存发布</button>
            <button  style="margin-left: 20px;" onclick="javascript:location.href = '/pages/advertisement/list.jsp'">关闭</button>
            <a style="margin-left: 20px; color: red;" id="errorMsg"></a>
            <form action="/advertisement/update.do" method="post" id="addPromotionForm" enctype="multipart/form-data" target="ifm">
                <table style="margin-top: 20px;" id="promotionTable">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" id="status" name="status">
                    <input type="hidden" id="release" name="release">
                    <tr>
                        <td>计划主题:</td>
                        <td><input name="title" id="title" maxlength="50"/>(50字以内)</td>
                    </tr>
                    <tr>
                        <td>展示时间:</td>
                        <td><input type="text" id="startTime" name="startTime"/>&nbsp;
                            至&nbsp;&nbsp;<input type="text" id="endTime" name="endTime"/></td>
                    </tr>
                    <tr>
                        <td>展示位置:</td>
                        <td><input type="checkbox" name="position" value="0"> 首页
                        <input type="checkbox" name="position" value="1"> 新闻列表
                        <input type="checkbox" name="position" value="2"> 新闻详情
                        </td>
                    </tr>
                    <tr>
                        <td ></td>
                        <td>
                            <table width="100%">
                                <tr>
                                    <td>
                                        <div style="float: left">
                                            <div id="container1">
                                                  <input id="file1" type="file" style="display:none">
                                            </div>
                                        </div>
                                        <div style="width:100px;height:50px">
                                            <img id="indexPic" src="/images/u95.png"  width="100px" height="50px">
                                            <input type="hidden" id="indexImage" name="indexImage">
                                        </div>
                                        (首页360*750px的图片)<br>
                                        <a id="uploadImage1" href="#">上传</a>
                                    </td>
                                    <td>
                                        <div style="float: left">
                                            <div id="container">
                                                <input id="file2" type="file" style="display:none"></div>
                                            </div>
                                        <div  style="width:100px;height:50px">
                                            <img id="newsPic" src="/images/u95.png" width="100px" height="50px">
                                            <input type="hidden" id="newsImage" name="newsImage">
                                        </div>
                                        <input type="hidden" value="https://qn-message.qbcdn.com" id="domain">
                                        <input type="hidden" value="/qiniu/getToken.do" id="uptoken_url">
                                        (新闻380*690px的图片)<br>
                                        <a id="uploadImage" href="#">上传</a>

                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>推广关键字:</td>
                        <td><input id="keyword" name="keyword" maxlength="50"/>多个关键词之间以","分割</td>
                    </tr>
                    <tr>
                        <td>超链:</td>
                        <td><input id="activityUrl" name="activityUrl" maxlength="200"/></td>
                    </tr>
                    <tr>
                        <td>投放客户:</td>
                        <td>
                            <select name="custIds" id="custIds" style="width:100px;">
                                <option value="">--请选择--</option>
                            </select></td>
                    </tr>
                    <tr>
                        <td>结算方式:</td>
                        <td>
                            <select name="feeType" id="feeType" style="width:100px;">
                                <option value="">--请选择--</option>
                                <option value="0">CPS</option>
                                <option value="1">CPC</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>描述:</td>
                        <td><textarea rows="3" cols="20" name="description" id="description" style="width: 300px;height: 50px;max-width: 300px;max-height: 50px;"  maxlength="120"></textarea></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

</div>

<script type="text/javascript" src="/js/qiniu/main.js"></script>
<script type="text/javascript" src="/js/qiniu/ui.js"></script>
<script type="text/javascript" src="/js/qiniu/multiple.js"></script>
<script type="text/javascript" src="/js/qiniu/qiniu.js"></script>
<script type="text/javascript" src="/js/qiniu/plupload.min.js"></script>
<script type="text/javascript" src="/js/qiniu/plupload.full.min.js"></script>
<script type="text/javascript" src="/pages/advertisement/js/edit.js"></script>

