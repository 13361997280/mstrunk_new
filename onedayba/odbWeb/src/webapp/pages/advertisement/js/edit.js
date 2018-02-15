$(function () {

    $("#startTime").datetimepicker({
        timeFormat: "HH:mm:ss",
        dateFormat: "yy-mm-dd"
    });
    $("#endTime").datetimepicker({
        timeFormat: "HH:mm:ss",
        dateFormat: "yy-mm-dd"
    });
    //保存发布
    $("#confirmRelease").click(function () {
        $('#release').val(1);
        $('#status').val(1);
        save();
    });

    //保存
    $("#saveDemo").click(function () {
        $('#release').val(0);
        save();
    });
    var promotion = {
        initFromData: function(id) {
            $.ajax({
                url: "/advertisement/" + id + '/get.do',
                type: 'get',
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        var data = result.data;
                        $('#title').val(data.title);
                        $('#startTime').val(data.startTime);
                        $('#endTime').val(data.endTime);
                        $('#status').val(data.status);
                        var position = data.position;
                        if(position!=null) {
                            var cbArray = new Array;
                            cbArray = position.split(",");
                            var str = document.getElementsByName("position");
                            var objarray = str.length;
                            for (i = 0; i < cbArray.length; i++) {
                                for (j = 0; j < objarray; j++) {
                                    if (str[j].value == cbArray[i]) {
                                        str[j].checked = true;
                                    }
                                }
                            }
                        }
                        $('#keyword').val(data.keyword);
                        $('#activityUrl').val(data.activityUrl);
                        $('#indexPic').attr('src',data.indexImage);
                        $('#indexImage').val(data.indexImage);
                        $('#newsPic').attr('src',data.newsImage);
                        $('#newsImage').val(data.newsImage);
                        $('#custIds').val(data.custIds);

                        $('#feeType').val(data.feeType);

                        $('#description').val(data.description);
                        $('#id').val(data.id);
                    }
                },
                error: function (result) {
                    alert(result);
                }
            });
        }
    };
    var customer = {
        initFromData: function() {
            $.ajax({
                url: "/advertisement/getCust.do",
                type: 'get',
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        var data = result.data;
                        $.each(data, function(index, array) {
                            $("#custIds").append("<option value='"+array.id+"'>"+array.custName+"</option>");
                        });
                        if(id>0) {
                            promotion.initFromData(id);
                        }
                    }
                },
                error: function (result) {
                    alert(result);
                }
            });
        }
    };
    customer.initFromData();

    var uploader = Qiniu.uploader({
        runtimes: 'html5,flash,html4',
        browse_button: 'uploadImage',
        container: 'container',
        drop_element: 'container',
        max_file_size: '10mb',
        flash_swf_url: 'bower_components/plupload/js/Moxie.swf',
        dragdrop: true,
        chunk_size: '4mb',
        multi_selection: !(mOxie.Env.OS.toLowerCase() === "ios"),
        uptoken_url: '/qiniu/getToken.do',
        domain: 'https://qn-message.qbcdn.com',
        get_new_uptoken: false,
        unique_names: true,
        auto_start: true,
        log_level: 5,
        filters: {
            mime_types: [
                {
                    title: 'images',
                    extensions: 'jpg,png,jpeg',
                }
            ],
        },
        init: {
            'FilesAdded': function (up, files) {
                $('table').show();
                $('#success').hide();
                plupload.each(files, function (file) {
                    var progress = new FileProgress(file, 'fsUploadProgress');
                    progress.setStatus("等待...");
                    progress.bindUploadCancel(up);
                });
            },
            'BeforeUpload': function (up, file) {
                var progress = new FileProgress(file, 'fsUploadProgress');
                var chunk_size = plupload.parseSize(this.getOption('chunk_size'));
                if (up.runtime === 'html5' && chunk_size) {
                    progress.setChunkProgess(chunk_size);
                }
            },
            'UploadProgress': function (up, file) {
                var progress = new FileProgress(file, 'fsUploadProgress');
                var chunk_size = plupload.parseSize(this.getOption('chunk_size'));
                progress.setProgress(file.percent + "%", file.speed, chunk_size);
            },
            'UploadComplete': function () {
                $('#success').show();
            },
            'FileUploaded': function (up, file, info) {
                var domain = up.getOption('domain');
                var res = $.parseJSON(info);
                var sourceLink = domain + "/" + res.key; //获取上传成功后的文件
                $("#newsPic").attr('src', sourceLink);
                $("#newsImage").val(sourceLink);
            },
            'Error': function (up, err, errTip) {
                $('table').show();
                var progress = new FileProgress(err.file, 'fsUploadProgress');
                progress.setError();
                progress.setStatus(errTip);
            }
        }
    });
    var uploader1 = Qiniu.uploader({
        runtimes: 'html5,flash,html4',
        browse_button: 'uploadImage1',
        container: 'container1',
        drop_element: 'container1',
        max_file_size: '10mb',
        flash_swf_url: 'bower_components/plupload/js/Moxie.swf',
        dragdrop: true,
        chunk_size: '4mb',
        multi_selection: !(mOxie.Env.OS.toLowerCase() === "ios"),
        uptoken_url: '/qiniu/getToken.do',
        domain: 'https://qn-message.qbcdn.com',
        get_new_uptoken: false,
        unique_names: true,
        auto_start: true,
        log_level: 5,
        filters: {
            mime_types: [
                {
                    title: 'images',
                    extensions: 'jpg,png,jpeg',
                }
            ],
        },
        init: {
            'FilesAdded': function (up, files) {
                $('table').show();
                $('#success').hide();
                plupload.each(files, function (file) {
                    var progress = new FileProgress(file, 'fsUploadProgress');
                    progress.setStatus("等待...");
                    progress.bindUploadCancel(up);
                });
            },
            'BeforeUpload': function (up, file) {
                var progress = new FileProgress(file, 'fsUploadProgress');
                var chunk_size = plupload.parseSize(this.getOption('chunk_size'));
                if (up.runtime === 'html5' && chunk_size) {
                    progress.setChunkProgess(chunk_size);
                }
            },
            'UploadProgress': function (up, file) {
                var progress = new FileProgress(file, 'fsUploadProgress');
                var chunk_size = plupload.parseSize(this.getOption('chunk_size'));
                progress.setProgress(file.percent + "%", file.speed, chunk_size);
            },
            'UploadComplete': function () {
                $('#success').show();
            },
            'FileUploaded': function (up, file, info) {
                var domain = up.getOption('domain');
                var res = $.parseJSON(info);
                var sourceLink = domain + "/" + res.key; //获取上传成功后的文件
                $("#indexPic").attr('src', sourceLink);
                $("#indexImage").val(sourceLink);
            },
            'Error': function (up, err, errTip) {
                $('table').show();
                var progress = new FileProgress(err.file, 'fsUploadProgress');
                progress.setError();
                progress.setStatus(errTip);
            }
        }
    });

    $("#dialog").dialog({
        autoOpen: false,
        modal: true,
        buttons : {
            "确认" : function() {
                $.ajax({
                    url: '/advertisement/update.do',
                    data: $("#addPromotionForm").serializeArray(),
                    type: 'post',
                    dataType: 'json',
                    success: function (result) {
                        // alert(result.success);
                        if (result.success){
                            location.href = '/pages/advertisement/list.jsp';
                        }else {
                            $('#errorMsg').text(result.data);
                        }

                    },
                    error: function (result) {
                        // alert(result);
                    }
                });
                $(this).dialog("close");
            },
            "取消" : function() {
                $(this).dialog("close");
            }
        }
    });

});

function save() {
    $("#dialog").dialog("open");
}
