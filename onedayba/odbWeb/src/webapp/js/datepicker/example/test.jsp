<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>My month picker</title>
       	<%@include file="/dw.jsp"%>
    </head>
    <body>
                <p>See it: <input type="text" id="default_widget" value='2014-01 - 2014-11' /> <small>( click on the input )</small></p>
 
        <script>
       		 $('#default_widget').monthpicker({
       			mode : 'range'
       		 });
        </script>
    </body>
</html>