<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>My First Chart</title>
       	<%@include file="/dw.jsp"%>
    </head>
    <body>
        <p><a href="#" title="That&apos;s what this widget is">Tooltips</a> can be attached to any element. When you hover
		the element with your mouse, the title attribute is displayed in a little box next to the element, just like a native tooltip.</p>
		<p>But as it's not a native tooltip, it can be styled. Any themes built with
		<a href="http://jqueryui.com/themeroller/" tip="ThemeRoller: jQuery UI&apos;s theme builder application">ThemeRoller</a>
		will also style tooltips accordingly.</p>
		<p>Tooltips are also useful for form elements, to show some additional information in the context of each field.</p>
		<p><label for="age">Your age:</label><input id="age" tip="age_tip"/></p>
		<p>Hover the field to see the tooltip.</p>
    </body>
</html>