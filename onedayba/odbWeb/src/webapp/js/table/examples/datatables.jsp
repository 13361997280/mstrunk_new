<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>My First Chart</title>
	<%@include file="/dw.jsp"%>

</head>
<body>
<table id="dwtable" class="display" cellspacing="0" width="100%">
</table>
<input type="button" value="click" id="btn" />
<script>
	$(function(){
		$("#example").append('<thead><tr><th>11</th><th>22</th><th>33</th><th>44</th></tr></thead>');
		$("#example").append('<tfoot><tr><th>11</th><th>22</th><th>33</th><th>44</th></tr></tfoot>');
		var table = $("#example").DataTable({
			pageLength:2,
			destroy:true,
			data:[{"total":"112","wine":0,"car":79,"ticket":33,"browser":0,"house":0,"bbs":0,"statDate":"2014-11-12"},
				{"total":"116","wine":0,"car":68,"ticket":47,"browser":0,"house":1,"bbs":0,"statDate":"2014-11-13"},
				{"total":"111","wine":0,"car":28,"ticket":76,"browser":0,"house":7,"bbs":0,"statDate":"2014-11-14"},
				{"total":"1264","wine":0,"car":29,"ticket":764,"browser":0,"house":471,"bbs":0,"statDate":"2014-11-15"},
				{"total":"1662","wine":0,"car":27,"ticket":1286,"browser":0,"house":349,"bbs":0,"statDate":"2014-11-16"},
				{"total":"30","wine":0,"car":17,"ticket":11,"browser":0,"house":2,"bbs":0,"statDate":"2014-11-17"}],
			"columns": [
				{ "data": "total"},
				{ "data": "car" },
				{ "data": "ticket" },
				{ "data": "statDate" }
			]
		});


		$('#example tfoot th').each( function () {
			var title = $(this).text();
			$(this).html( '<input type="text" placeholder="Search '+title+'" />' );
		});

		// Apply the search
//				table.columns(0).search(111).draw();
		table.columns().eq(0).each( function (index) {
			var that = table.columns(index);
//					var that = this;

			$( 'input', that.footer() ).on( 'keyup change', function () {
				if ( that.search() !== this.value ) {
					that
							.search( this.value )
							.draw();
				}
			} );
		});
//        		$("#btn").on("click", function(){
//        			$("#example thead").html('<tr><th>1</th><th>2</th><th>3</th><th>4</th></tr>');
//        			$("#example").DataTable({
//            			destroy:true,
//            			data:[{"total":112,"wine":0,"car":79,"ticket":33,"browser":0,"house":0,"bbs":0,"statDate":"2014-11-12"},{"total":116,"wine":0,"car":68,"ticket":47,"browser":0,"house":1,"bbs":0,"statDate":"2014-11-13"},{"total":111,"wine":0,"car":28,"ticket":76,"browser":0,"house":7,"bbs":0,"statDate":"2014-11-14"},{"total":1264,"wine":0,"car":29,"ticket":764,"browser":0,"house":471,"bbs":0,"statDate":"2014-11-15"},{"total":1662,"wine":0,"car":27,"ticket":1286,"browser":0,"house":349,"bbs":0,"statDate":"2014-11-16"},{"total":30,"wine":0,"car":17,"ticket":11,"browser":0,"house":2,"bbs":0,"statDate":"2014-11-17"}],
//            			"columns": [
//            			            { "data": "ticket" },
//            			            { "data": "browser" },
//            			            { "data": "bbs" },
//            			            { "data": "house", visible:false }
//            			        ]
//            		});
//        		});
	});
</script>
</body>
</html>