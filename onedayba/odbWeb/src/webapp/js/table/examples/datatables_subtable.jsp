<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>My First Chart</title>
       	<%@include file="/dw.jsp"%>
       	
    </head>
    <body>
    	<table id="example" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th></th>
                <th>Name</th>
                <th>Position</th>
                <th>Office</th>
                <th>Salary</th>
            </tr>
        </thead>
 
        <tfoot>
            <tr>
                <th></th>
                <th>Name</th>
                <th>Position</th>
                <th>Office</th>
                <th>Salary</th>
            </tr>
        </tfoot>
    </table>
	    <input type="button" value="click" id="btn" />
        <script>
        /* Formatting function for row details - modify as you need */
        function format ( d ) {
            // `d` is the original data object for the row
            return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
                '<tr>'+
                    '<td>Full name:</td>'+
                    '<td>'+d.name+'</td>'+
                '</tr>'+
                '<tr>'+
                    '<td>Extension number:</td>'+
                    '<td>'+d.extn+'</td>'+
                '</tr>'+
                '<tr>'+
                    '<td>Extra info:</td>'+
                    '<td>And any further details here (images etc)...</td>'+
                '</tr>'+
            '</table>';
        }
         
        $(document).ready(function() {
            var table = $('#example').DataTable( {
                "ajax": "/user/getCUser.do?presenterId=206962",
                serverSide:true,
                "columns": [
                    {
                        "className":      'details-control',
                        "orderable":      false,
                        "data":           null,
                        "defaultContent": ''
                    },
                    {data:'cUserId',title:'当cUserId留存'},
					  {data:'d3',title:'3日留存'},
					  {data:'d7',title:'7日留存'},
					  {data:'d17',title:'认证时留存(17日)'}
                ],
                "order": [[1, 'asc']]
                /*"initComplete": function(settings, json) {
                	// Add event listener for opening and closing details
                    this.api().$('td.details-control').on('click', function () {
                        var tr = $(this).closest('tr');
                        var row = table.row( tr );
                 
                        if ( row.child.isShown() ) {
                            // This row is already open - close it
                            row.child.hide();
                            tr.removeClass('shown');
                        }
                        else {
                            // Open this row
                            row.child( format(row.data()) ).show();
                            tr.addClass('shown');
                        }
                    } );
                  }*/
            } );
            
         // Add event listener for opening and closing details
            $('#example tbody').on('click', 'td.details-control', function () {
                var tr = $(this).closest('tr');
                var row = table.row( tr );
         
                if ( row.child.isShown() ) {
                    // This row is already open - close it
                    row.child.hide();
                    tr.removeClass('shown');
                }
                else {
                    // Open this row
                    row.child( format(row.data()) ).show();
                    tr.addClass('shown');
                }
            } );
             
            
        } );
        </script>
    </body>
</html>