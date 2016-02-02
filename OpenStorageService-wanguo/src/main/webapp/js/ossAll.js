//var osshttp = 'http://192.168.2.32:8080';
var osshttp = '';
Handlebars.registerHelper('descNull',function(data){
    if(data==null){
        return('暂无描述');
    }else if(data==undefined){
        return(' ');
    }else{
        return(data);
    }
});
$(".mytable tr").live({
    mouseover: function () {
        $(this).find(".update_pic").css({visibility: "visible"});
    }, mouseout: function () {
        $(this).find(".update_pic").css({visibility: "hidden"});
    }
});
//进入oss主页面
$(function () {
    $.ajax({
        url: osshttp + '/oss/account/getUserFile',
        type: 'post',
        data: {},
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
                var userName = data.data[0].email;//用户名
                $('#username').text(data.data[0].name);
                var userId = data.data[0].userid;//用户名
                $('#userId').text(userId);//用户id
                $('#user').val(userName);//存用户名
                var usedStor = parseInt(data.data[0].useSto);//取得已用和未用容量，到时候应该会改结构
                var allStor = parseInt(data.data[0].allSto);
                $.ajax({
                    url: osshttp + '/oss/hdfs/getNextFile',
                    type: 'post',
                    data: {'email': userName, 'type': 'user'},
                    dataType: 'json',
                    success: function (data) {
                        if (data.state == true) {
                            for (var i = 0; i < data.data.length; i++) {
                                if (data.data[i].type == '文件夹') {
                                    data.data[i].url = '/oss/img/file.png';
                                }
                                if (data.data[i].type == '文本') {
                                    data.data[i].url = '/oss/img/txt.gif';
                                }
                                if (data.data[i].type == '办公文档') {
                                    data.data[i].url = '/oss/img/doc.gif';
                                }
                                if (data.data[i].type == '图片') {
                                    data.data[i].url = '/oss/img/jpg.gif';
                                }
                                if (data.data[i].type == '视频') {
                                    data.data[i].url = '/oss/img/mov.gif';
                                }
                                if (data.data[i].type == '压缩文件') {
                                    data.data[i].url = '/oss/img/zip.gif';
                                }
                                if (data.data[i].type == '演示文稿') {
                                    data.data[i].url = '/oss/img/ppt.gif';
                                }
                                if (data.data[i].type == '表格') {
                                    data.data[i].url = '/oss/img/excel.gif';
                                }
                                if (data.data[i].type == '电子文档') {
                                    data.data[i].url = '/oss/img/pdf.gif';
                                }
                                if (data.data[i].type == '未知文件') {
                                    data.data[i].url = '/oss/img/undefined.png';
                                }
                            }
                            var source = $("#oss_total").html();
                            var template = Handlebars.compile(source);
                            $(".mymain").html(template(data));
                            if (usedStor >= 1000) {
                               // usedStor = Math.ceil(usedStor / 1000);
				var storUsed = parseInt(usedStor/1000)+'.'+parseInt(usedStor%1000/100)
                                $('.oss_contain span').eq(0).text(storUsed + 'G');
                            } else {
				var storUsed = usedStor
                                $('.oss_contain span').eq(0).text(storUsed + 'M');
                            }

                            $('.oss_contain span').eq(1).text(Math.ceil(allStor / 1000) + 'G');
                            var oDiv = Math.floor((usedStor / allStor)*350);
console.log(oDiv)
console.log(Math.floor(usedStor / allStor));
                            if (oDiv < 10) {
                                oDiv = 10;
                            }
                            $('#used_num').css({width: oDiv + 'px'});
                            if (data.data.length == 0) {
                                $('tbody').append('<tr><td colspan="8">暂无数据</td></tr>');
                            }
                            //加载树形插件
                            $("#browser11").treeview({
                                persist: "location",
                                collapsed: true,
                                unique: true,
                                animate: 'fast'
                            });
                            //页面加载完毕才初始化上传插件       上传文件
                            //myupload_file();
                        } else {
                            alert(data.msg);
                        }
                    }
                });
            } else {
                alert(data.msg);
            }
        }
    });
});
var myszyUpload = null;
//上传变色
function changeColo() {
    $('.footWords').css({color: '#bababa'});
    if (myszyUpload != null) {
        myszyUpload.clean();
    }
    myupload_file();
}
//取消变色
function reWord() {
    $('.footWords').css({color: '#fff'});
}
//上传函数
function myupload_file() {
    var upload_btn = $('#uBtn3');
    var user = $('#user').val();
    var btnszy = $('.table_box').attr('btn');
    var pathLength = $('.allPath').length;
    var pathAll = '';
    var aPath = [];
    if (btnszy == 1) {//1级页面上传
        var upPath = '/oss/hdfs/uploadFile?email=' + user;
    } else if (btnszy == 2) {//2级页面上传
        for (var l = 0; l < pathLength; l++) {
            pathAll += '/' + $('.allPath').eq(l).text();//面包屑全路径
            aPath.push($('.allPath').eq(l).text());
        }
        var upPath = '/oss/hdfs/uploadFile?email=' + user + '&dirUrl=' + pathAll;
    }
    myszyUpload = new AjaxUpload(upload_btn, {
        action: osshttp + upPath,
        name: 'myUpFile',   //这相当于<input type = "file" name = "shanghaiFile"/>填name值，不是id值
        onSubmit: function (file, ext) {//上传中的状态
            $('#upload_div').css({display: 'block'});
            this.disable();
        },
        onComplete: function (file, response) { //上传完毕后的操作
            console.log(this)
            $('#res_hide')[0].innerHTML = response;//用隐藏div来接收返回文本
            var mystate = JSON.parse($('#res_hide').text());
            if (mystate.state == true) {
                alert('上传成功');
                this.enable();
                $('#upload_div').css({display: 'none'});
                $('.footWords').css({color: '#fff'});
                if (btnszy == 1) {//1级页面直接刷新
                    window.location.reload();
                } else if (btnszy == 2) {//二级页面重构
                    $.ajax({
                        url: osshttp + '/oss/hdfs/getNextFile',
                        type: 'post',
                        data: {'dirUrl': pathAll, 'email': user, 'type': 'user'},
                        dataType: 'json',
                        success: function (data) {
                            if (data.state == true) {
                                for (var i = 0; i < data.data.length; i++) {
                                    if (data.data[i].type == '文件夹') {
                                        data.data[i].url = '/oss/img/file.png';
                                    }
                                    if (data.data[i].type == '文本') {
                                        data.data[i].url = '/oss/img/txt.gif';
                                    }
                                    if (data.data[i].type == '办公文档') {
                                        data.data[i].url = '/oss/img/doc.gif';
                                    }
                                    if (data.data[i].type == '图片') {
                                        data.data[i].url = '/oss/img/jpg.gif';
                                    }
                                    if (data.data[i].type == '视频') {
                                        data.data[i].url = '/oss/img/mov.gif';
                                    }
                                    if (data.data[i].type == '压缩文件') {
                                        data.data[i].url = '/oss/img/zip.gif';
                                    }
                                    if (data.data[i].type == '演示文稿') {
                                        data.data[i].url = '/oss/img/ppt.gif';
                                    }
                                    if (data.data[i].type == '表格') {
                                        data.data[i].url = '/oss/img/excel.gif';
                                    }
                                    if (data.data[i].type == '电子文档') {
                                        data.data[i].url = '/oss/img/pdf.gif';
                                    }
                                    if (data.data[i].type == '未知文件') {
                                        data.data[i].url = '/oss/img/undefined.png';
                                    }
                                }
                                $('#myUpdiv').modal('hide');
                                var source = $("#oss_child").html();
                                var template = Handlebars.compile(source);
                                $(".mymain").html(template(data));
                                if (data.data.length == 0) {
                                    $('tbody').append('<tr><td colspan="8">暂无数据</td></tr>');
                                }
                                var str = '';
                                for (var j = 0; j < aPath.length - 1; j++) {//面包屑导航
                                    str += '<span> &gt; </span>' + '<span class="navback allPath" onclick="go_backParent1(this);">' + aPath[j] + '</span>';
                                }
                                str += '<span> &gt; </span>' + '<span class="allPath">' + aPath[aPath.length - 1] + '</span>';//最后一个面包屑不能点
                                $('.list_nav').append(str);

                                //myupload_file();
                            } else {
                                alert(data.msg);
                            }
                        }
                    });
                }
            } else {
                $('#upload_div').css({display: 'none'});
                $('.footWords').css({color: '#fff'});
                alert("上传失败");
            }
            //有了response我们能做任何事了,返回的文件名称,文件路径等我们可以随意操作的!
        }
    });
}
//新建文件夹  清空
function add_new_file() {
    $("#file_name").val("");
    var path = '';
    for (var i = 0; i < $('.filePath').length; i++) {
        path += '/' + $('.filePath').eq(i).text();
    }
    $('#filePath').val(path);
}
//确定新建
function do_newFile() {
    var btn = $('.table_box').attr('btn');
    var nowPath = $('#file_name').val();//新建文件夹名
    var user = $('#user').val();
    var str1 = /^[a-zA-Z0-9_\.\-\u4e00-\u9fa5]+$/;
    var str2 = /^\S+$/;
    var str3 = /^[a-zA-Z0-9\u4e00-\u9fa5]/;
    for (var x = 0; x < $('.name_box').length; x++) {
        if ($('.name_box').eq(x).text() == nowPath) {
            alert('当前文件夹中的文件名不能重复');
            return;
        }
    }
    if (btn == 1) {//第一级页面新建
        if (str1.test(nowPath) && str2.test(nowPath) && str3.test(nowPath)) {
            $.ajax({
                url: osshttp + '/oss/hdfs/mkdir',
                type: 'post',
                data: {'dirUrl': '/' + nowPath, 'email': user, 'type': 'user'},
                dataType: 'json',
                success: function (data) {
                    if (data.state == true) {
                        alert('操作成功');
                        window.location.reload();//一级页面刷新直接reload就行
                    } else {
                        alert(data.msg);
                    }
                }
            });
        } else {
            alert('请注意文件命名规则');
        }
    }
    if (btn == 2) {//第二级页面新建
        //var pathAll = '';
        //var pathLength = $('.allPath').length;//当前文件夹路径
        var file_name = $('#file_name').val();//新建文件夹名
        if (str1.test(file_name) && str2.test(file_name) && str3.test(file_name)) {
            var pathAll = '';
            var pathLength = $('.allPath').length;//当前文件夹路径
            var aPath = '';//用于重构面包屑
            for (var j = 0; j < pathLength; j++) {
                pathAll += '/' + $('.allPath').eq(j).text();
                aPath += '/' + $('.allPath').eq(j).text();//当前文件夹路径
            }
            pathAll += '/' + file_name;//新建文件夹路径
            $.ajax({
                url: osshttp + '/oss/hdfs/mkdir',
                type: 'post',
                data: {'dirUrl': pathAll, 'email': user, 'type': 'user'},
                dataType: 'json',
                success: function (data) {
                    if (data.state == true) {//页面重绘
                        alert('操作成功');
                        $('#add_file').modal('hide');
                        $.ajax({//改名成功二级页面重绘
                            url: osshttp + '/oss/hdfs/getNextFile',
                            type: 'post',
                            data: {'dirUrl': aPath, 'email': user, 'type': 'user'},
                            dataType: 'json',
                            success: function (data) {
                                if (data.state == true) {
                                    for (var i = 0; i < data.data.length; i++) {
                                        if (data.data[i].type == '文件夹') {
                                            data.data[i].url = '/oss/img/file.png';
                                        }
                                        if (data.data[i].type == '文本') {
                                            data.data[i].url = '/oss/img/txt.gif';
                                        }
                                        if (data.data[i].type == '办公文档') {
                                            data.data[i].url = '/oss/img/doc.gif';
                                        }
                                        if (data.data[i].type == '图片') {
                                            data.data[i].url = '/oss/img/jpg.gif';
                                        }
                                        if (data.data[i].type == '视频') {
                                            data.data[i].url = '/oss/img/mov.gif';
                                        }
                                        if (data.data[i].type == '压缩文件') {
                                            data.data[i].url = '/oss/img/zip.gif';
                                        }
                                        if (data.data[i].type == '演示文稿') {
                                            data.data[i].url = '/oss/img/ppt.gif';
                                        }
                                        if (data.data[i].type == '表格') {
                                            data.data[i].url = '/oss/img/excel.gif';
                                        }
                                        if (data.data[i].type == '电子文档') {
                                            data.data[i].url = '/oss/img/pdf.gif';
                                        }
                                        if (data.data[i].type == '未知文件') {
                                            data.data[i].url = '/oss/img/undefined.png';
                                        }
                                    }
                                    var source = $("#oss_child").html();
                                    var template = Handlebars.compile(source);
                                    $(".mymain").html(template(data));
                                    var str = '';
                                    var anPath = aPath.split('/');
                                    for (var j = 1; j < anPath.length - 1; j++) {//面包屑导航
                                        str += '<span> &gt; </span>' + '<span class="navback allPath" onclick="go_backParent1(this);">' + anPath[j] + '</span>';
                                    }
                                    str += '<span> &gt; </span>' + '<span class="allPath">' + anPath[anPath.length - 1] + '</span>';//最后一个面包屑不能点
                                    $('.list_nav').append(str);
//                                myupload_file();
                                } else {
                                    alert(data.msg);
                                }
                            }
                        });
                    } else {
                        alert(data.msg);
                    }
                }
            });
        } else {
            alert('请注意文件命名规则');
        }
    }
}
//修改文件名称  预处理
function upd_FileName(obj) {
    var iFileName = $(obj).prev().prev().text();
    var iFilePath = $(obj).prev().text();
    var iFileType = $(obj).parent().next().next().next().text();
    $("#iFileName").val(iFileName);
    $("#filePath").val(iFilePath);//存路径
    $('#file_type').val(iFileType);//存类型
    $("#FileNewName").val("");//清空输入框
    $('.checkbox_class').attr('checked', false);
    $(obj).siblings('.checkbox_class').attr('checked', 'checked');
    var splitName = iFileName.split('.');
    var lastName = splitName[splitName.length - 1];
    $('#file_last').val(lastName);
}
//修改文件名称  确定
function updFileName() {
    var btn = $('.table_box').attr('btn');
    var prevPath = '';
    for (var i = 0; i < $('.filePath').length; i++) {
        prevPath += '/' + $('.filePath').eq(i).text();
    }
    var newName = $('#FileNewName').val();
    var fileType = $('#file_type').val();//文件类型
    var lastName = $('#file_last').val();//文件后缀
    var newPath = prevPath + '/' + newName;//新路径
    var oldPath = $("#filePath").val();//旧路径
    var user = $('#user').val();
    var str1 = /^[a-zA-Z0-9_\.\-\u4e00-\u9fa5]+$/;
    var str2 = /^\S+$/;
    var str3 = /^[a-zA-Z0-9\u4e00-\u9fa5]/;
    if (btn == 1) {//一级页面修改
        if (fileType == '文件夹') {
            var newFileName = newPath;
        } else {
            var newFileName = newPath + '.' + lastName;
        }
        if (str1.test(newName) && str2.test(newName) && str3.test(newName)) {
            $.ajax({
                url: osshttp + '/oss/hdfs/rename',
                type: 'post',
                data: {'email': user, 'new_file': newFileName, 'old_file': oldPath, 'type': 'user'},
                dataType: 'json',
                success: function (data) {
                    if (data.state == true) {
                        alert('操作成功');
                        window.location.reload();
                    } else {
                        alert(data.msg);
                    }
                }
            });
        } else {
            alert('请注意文件命名规则');
        }
    }
    if (btn == 2) {//二级页面修改
        var newpath = '';
        var aPath = '';
        var mystr = oldPath.split('/');
        for (var j = 1; j < mystr.length - 1; j++) {
            newpath += '/' + mystr[j];
            aPath += '/' + mystr[j];//用于重构页面
        }
        if (str1.test(newName) && str2.test(newName) && str3.test(newName)) {
            newpath += '/' + newName;
            if (fileType != '文件夹') {
                newpath += '.' + lastName;
            }
            $.ajax({
                url: osshttp + '/oss/hdfs/rename',
                type: 'post',
                data: {'email': user, 'new_file': newpath, 'old_file': oldPath, 'type': 'user'},
                dataType: 'json',
                success: function (data) {
                    if (data.state == true) {
                        alert('操作成功');
                        $('#upd_fileName').modal('hide');
                        $.ajax({//改名成功二级页面重绘
                            url: osshttp + '/oss/hdfs/getNextFile',
                            type: 'post',
                            data: {'dirUrl': aPath, 'email': user, 'type': 'user'},
                            dataType: 'json',
                            success: function (data) {
                                if (data.state == true) {
                                    for (var i = 0; i < data.data.length; i++) {
                                        if (data.data[i].type == '文件夹') {
                                            data.data[i].url = '/oss/img/file.png';
                                        }
                                        if (data.data[i].type == '文本') {
                                            data.data[i].url = '/oss/img/txt.gif';
                                        }
                                        if (data.data[i].type == '办公文档') {
                                            data.data[i].url = '/oss/img/doc.gif';
                                        }
                                        if (data.data[i].type == '图片') {
                                            data.data[i].url = '/oss/img/jpg.gif';
                                        }
                                        if (data.data[i].type == '视频') {
                                            data.data[i].url = '/oss/img/mov.gif';
                                        }
                                        if (data.data[i].type == '压缩文件') {
                                            data.data[i].url = '/oss/img/zip.gif';
                                        }
                                        if (data.data[i].type == '演示文稿') {
                                            data.data[i].url = '/oss/img/ppt.gif';
                                        }
                                        if (data.data[i].type == '表格') {
                                            data.data[i].url = '/oss/img/excel.gif';
                                        }
                                        if (data.data[i].type == '电子文档') {
                                            data.data[i].url = '/oss/img/pdf.gif';
                                        }
                                        if (data.data[i].type == '未知文件') {
                                            data.data[i].url = '/oss/img/undefined.png';
                                        }
                                    }
                                    var source = $("#oss_child").html();
                                    var template = Handlebars.compile(source);
                                    $(".mymain").html(template(data));
                                    var str = '';
                                    var anPath = aPath.split('/');
                                    for (var j = 1; j < anPath.length - 1; j++) {//面包屑导航
                                        str += '<span> &gt; </span>' + '<span class="navback allPath" onclick="go_backParent1(this);">' + anPath[j] + '</span>';
                                    }
                                    str += '<span> &gt; </span>' + '<span class="allPath">' + anPath[anPath.length - 1] + '</span>';//最后一个面包屑不能点
                                    $('.list_nav').append(str);
//                                    myupload_file();
                                } else {
                                    alert(data.msg);
                                }
                            }
                        });
                    } else {
                        alert(data.msg);
                    }
                }
            });
        } else {
            alert('请注意文件命名规则');
        }
    }
}
//下载和删除文件 预处理
function myDo_file(obj, name) {
    var iFileName = $(obj).parent().siblings().eq(0).find(".file_name").text();
    var iFilePath = $(obj).parent().siblings().eq(0).find(".file_id").text();
    $("#iFileName").val(iFileName);
    $("#filePath").val(iFilePath);
    $("#" + name).text(iFileName);
    $('.checkbox_class').attr('checked', false);
    $(obj).parent().siblings().eq(0).find(".checkbox_class").attr('checked', 'checked');
    $('#allCheck1').attr("checked", false);
    $('#allCheck2').attr("checked", false);
    $('#allCheck3').attr("checked", false);
}
//单个文件下载 确定
function upload_one() {
    var btn = $('.table_box').attr('btn');
    var user = $('#user').val();
    var filePath = $("#filePath").val();
    var oPath = new Object();
    var aPath = [];
    aPath.push(filePath);
    oPath.dirUrl = aPath;
    var pathJson = JSON.stringify(oPath);
    for (var i = 0; i < $('.checkbox_class').length; i++) {
        if ($('.checkbox_class').eq(i).attr('checked') == 'checked') {
            var fileName = $('.checkbox_class').eq(i).next().next().text();
            var fileType = $('.checkbox_class').eq(i).parent().next().next().next().text();
        }
    }
    if (fileType == '文件夹') {
        var lastPath = '/oss/hdfs/downUrl';
    } else {
        var lastPath = '/oss/hdfs/loadUrl';
    }
    if (btn == 1 || btn == 2) {
        $.ajax({
            url: osshttp + lastPath,
            type: 'post',
            data: {'email': user, 'dirUrl': pathJson, 'type': 'user', 'fName': fileName},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    $('#myUpload').modal('hide');
                    $('#upload_iframe').attr('src', data.data.url + '&dirUrl=' + pathJson);
                } else {
                    alert(data.msg);//文件大于30M
                }
            }
        });
    }
    if (btn == 3) {
        $.ajax({
            url: osshttp + lastPath,
            type: 'post',
            data: {'email': user, 'dirUrl': pathJson, 'type': 'shared', 'fName': fileName},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    $('#myUpload').modal('hide');
                    $('#upload_iframe').attr('src', data.data.url + '&dirUrl=' + pathJson);
                } else {
                    alert(data.msg);//文件大于30M
                }
            }
        });
    }
}
//单个删除  确定
function delFile_one() {
    var btn = $('.table_box').attr('btn');
    var iFileName = $("#iFileName").val();//做删除用
    var user = $('#user').val();
    var path1 = $("#filePath").val();
    var oPath = new Object();
    var aPath = [];
    aPath.push(path1);
    oPath.dirUrl = aPath;
    var pathJson = JSON.stringify(oPath);
    if (btn == 1 || btn == 2) {
        var myright = 'user';
    }
    if (btn == 3) {
        var myright = 'shared';
    }
    $.ajax({
        url: osshttp + '/oss/hdfs/delete',
        type: 'post',
        data: {'dirUrl': pathJson, 'type': myright, 'email': user},
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
//                if(btn==1){//一级页面
//                    alert('操作成功');
//                    window.location.reload();
//                }
//                if(btn==2||btn==3){//二级页面和分享页面
                alert('操作成功');
                $('#delFile').modal('hide');
                for (var i = 0; i < $('.name_box').length; i++) {
                    if ($('.name_box').eq(i).text() == iFileName) {
                        $('.name_box').eq(i).parent().parent().remove();
                    }
//                    }
                }
            }
        }
    });
}
//单个分享文件  预处理
function myShare_file(obj, email) {
    var iFileName = $(obj).parent().siblings().eq(0).find(".file_name").text();
    var iFilePath = $(obj).parent().siblings().eq(0).find(".file_id").text();
    $("#iFileName").val(iFileName);
    $("#filePath").val(iFilePath);//存路径
    $("#" + email).val("");//清空邮箱
    $('.checkbox_class').attr('checked', false);
    $(obj).parent().siblings().eq(0).find(".checkbox_class").attr('checked', 'checked');
    $('#allCheck1').attr('checked', false);
    $('#allCheck2').attr('checked', false);
    $('#allCheck3').attr('checked', false);
}
//单个分享文件  确定
function share_one() {
    var myemail = $('#myEmail1').val();  //邮箱
    var eArr = myemail.split('、');
    var btn = 0;
    var str = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    var path = $("#filePath").val(); //文件路径
    var aPath = [];
    aPath.push(path);
    var myPath = new Object();
    myPath.dirUrl = aPath;
    var pathJson = JSON.stringify(myPath);
    var user = $('#user').val();
    for (var i = 0; i < eArr.length; i++) {
        if (!str.test(eArr[i])) {
            btn = 1;
        }
    }
    if (btn == 0) {
        if (eArr.length == 1) {
            var shareUrl = '/oss/hdfs/sharedFile';

        } else if (eArr.length > 1) {
            var shareUrl = '/oss/hdfs/sharedFiles';
        }
        $.ajax({
            url: osshttp + shareUrl,
            type: 'post',
            data: {'email': user, 'dirUrl': pathJson, 'dst_email': JSON.stringify({'dst_email': eArr}), 'type': user},//dst_email分享邮箱
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    if (data.msg == null) {
                        alert('分享成功');
                    } else {
                        alert(data.msg);
                    }
                    $('#FileShare').modal('hide');
                } else {
                   alert('分享失败,请重新分享');
                }
            }
        });
    } else if (btn == 1) {
        alert('请检查您的邮箱');
    }
}
//批量下载 确定
function upload_all() {
    var btn = $('.table_box').attr('btn');
    var user = $('#user').val();
    var checkNum = $('.checkbox_class').length;
    var pathArray = [];
    for (var i = 0; i < checkNum; i++) {
        if ($('.checkbox_class').eq(i).attr('checked') == 'checked') {
            pathArray.push($('.checkbox_class').eq(i).next().next().next().text());
        }
    }
    var oPath = new Object();
    oPath.dirUrl = pathArray;
    var pathJson = JSON.stringify(oPath);
    if (btn == 1 || btn == 2) {
        $.ajax({
            url: osshttp + '/oss/hdfs/downUrl',
            type: 'post',
            data: {'email': user, 'dirUrl': pathJson, 'type': 'user'},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    $('#allUpload').modal('hide');
                    $('#upload_iframe').attr('src', data.data.url + '&dirUrl=' + pathJson);
                } else {
                    alert(data.msg);
                }
            }
        });
    }
    if (btn == 3) {
        $.ajax({
            url: osshttp + '/oss/hdfs/downUrl',
            type: 'post',
            data: {'email': user, 'dirUrl': pathJson, 'type': 'shared'},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    $('#allUpload').modal('hide');
                    $('#upload_iframe').attr('src', data.data.url + '&dirUrl=' + pathJson);
                } else {
                    alert(data.msg);
                }
            }
        });
    }
}
//客户端下载
function client_up() {
    alert('暂未开放此功能');
}
//批量分享文件 预处理
function myShare_allFile(email) {
    $("#" + email).val("");//清空邮箱
}
//批量分享 确定
function shareAll() {
    var checkNum = $('.checkbox_class').length;
    var pathArray = [];
    var user = $('#user').val();
    var str = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    var myemail = $('#myEmail2').val();
    var eArr = myemail.split('、');
    var btn = 0;
    for (var i = 0; i < checkNum; i++) {
        if ($('.checkbox_class').eq(i).attr('checked') == 'checked') {
            pathArray.push($('.checkbox_class').eq(i).next().next().next().text());
        }
    }
    var oPath = new Object();
    oPath.dirUrl = pathArray;
    var pathJson = JSON.stringify(oPath);
    for (var j = 0; j < eArr.length; j++) {
        if (!str.test(eArr[j])) {
            btn = 1;
        }
    }
    if (btn == 0) {
        if (eArr.length == 1) {
            var shareUrl = '/oss/hdfs/sharedFile';

        } else if (eArr.length > 1) {
            var shareUrl = '/oss/hdfs/sharedFiles';
        }
        $.ajax({
            url: osshttp + shareUrl,
            type: 'post',
            data: {'email': user, 'dirUrl': pathJson, 'dst_email': JSON.stringify({'dst_email': eArr}), 'type': user},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    alert('操作成功');
                    $('#allShare').modal('hide');
                } else {
                    if (data.data.success == undefined) {
                        alert(data.msg);
                        //alert('分享失败,请重新分享');
                    } else if (data.data.success == null) {
                        alert('分享失败,请重新分享');
                    } else {
                        var failedEmail = myemail.split(data.data.success)[1];
                        $('#myEmail2').val(failedEmail);
                        alert('您的分享从' + data.data.failed + '开始失败,请检查后面的邮箱');
                    }
                }
            }
        });
    } else if (btn == 1) {
        alert('请检查您的邮箱');
    }
}
//批量删除
function del_All() {
    var btn = $('.table_box').attr('btn');
    var checkNum = $('.checkbox_class').length;
    var pathArray = [];
    var user = $('#user').val();
    for (var i = 0; i < checkNum; i++) {
        if ($('.checkbox_class').eq(i).attr('checked') == 'checked') {
            pathArray.push($('.checkbox_class').eq(i).next().next().next().text());//路径数组
        }
    }
    var oPath = new Object();
    oPath.dirUrl = pathArray;
    var pathJson = JSON.stringify(oPath);
    if (btn == 1 || btn == 2) {
        var myright = 'user';
    }
    if (btn == 3) {
        var myright = 'shared';
    }
    $.ajax({
        url: osshttp + '/oss/hdfs/delete',
        type: 'post',
        data: {'dirUrl': pathJson, 'type': myright, 'email': user},
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
                //if(btn==1||btn==2){//二级页面
                alert('操作成功');
                $('#allCheck1').attr('checked', false);
                $('#allCheck2').attr('checked', false);
                $('#allCheck3').attr('checked', false);
                $('#delAll').modal('hide');
                for (var j = 0; j < checkNum; j++) {
                    if ($('.checkbox_class').eq(j).attr('checked') == 'checked') {
                        $('.checkbox_class').eq(j).parent().parent().remove();
                        j--;//length在变,j删一个j应当往前走一个
                    }
                    //}
                }
                if ($('.checkbox_class').length == 0) {
                    $(".all_btns").css({visibility: "hidden"});
                }
//                if(btn==3){//分享页面
//
//                }
            } else {
                alert(data.msg);
            }
        }
    });
}
//单个移动文件  预处理
function myMove_file(obj) {
    $('#movePath').val('');
    var fileSize = $(obj).parent().siblings().eq(2).text();
    $('#share_Files').val(fileSize);
    var btn = $('.table_box').attr('btn');//判断是几级页面
    var iFileName = $(obj).parent().siblings().eq(0).find(".file_name").text();
    var filePath = $(obj).parent().siblings().eq(0).find(".file_id").text();//文件路径
    $("#iFileName").val(iFileName);
    $("#filePath").val(filePath);//存路径
    $('.checkbox_class').attr('checked', false);
    $(obj).parent().siblings().eq(0).find(".checkbox_class").attr('checked', 'checked');
    $('#allCheck1').attr('checked', false);
    $('#allCheck2').attr('checked', false);
    $('#allCheck3').attr('checked', false);
    $('#one_move').attr('disabled', false);
    var user = $('#user').val();
    var oPath = new Object();
    var aPath = [];
    aPath.push($("#filePath").val());
    oPath.dstDir = aPath;
    var oldPath = JSON.stringify(oPath);//用于筛选文件
    if (btn == 1 || btn == 2) {
        var myright = 'user';
    }
    if (btn == 3) {
        var myright = 'hehe';
    }
    $("#browser1")[0].innerHTML = '';
    $.ajax({
        url: osshttp + '/oss/hdfs/getDir',
        type: 'post',
        data: {'email': user, 'dirUrl': '', 'type': user, 'dstDir': oldPath},
        dataType: 'json',
        success: function (data) {
            if (data.data.length != 0) {
                if (data.state == true) {
                    var str = '';
                    for (var i = 0; i < data.data.length; i++) {
                        str += '' +
                        '<li class="expandable" btn=0 dirUrl="' + data.data[i].dirUrl + '" onclick="mvChild(event,this);">' +
                        '<div class="hitarea expandable-hitarea"></div>' +
                        '<span class="folder">' + data.data[i].dirName + '</span>' +
                        '</li>';
                    }
                    $("#browser1").append(str);
                } else {
                    alert(data.msg);
                }
            } else {
                $('#browser1')[0].innerHTML = '<span style="color:red;font-size:15px;">不能将文件移动到自身及其子目录下</span>';
                $('#one_move').attr('disabled', true);
            }
        }
    });
}
//查看文件移动子目录
function mvChild(event, obj) {
    event.stopPropagation();//阻止冒泡movePath
    //var e=event || window.event;
    //
    //if (e && e.stopPropagation){
    //    e.stopPropagation();
    //}
    //else{
    //    e.cancelBubble=true;
    //}

    var btn = parseInt($(obj).attr("btn"));//开关,判断是否发送ajax
    var nowPath = $(obj).attr('dirUrl');
    var user = $('#user').val();
    var oPath = new Object();
    var aPath = [];
    for (var j = 0; j < $('.checkbox_class').length; j++) {
        if ($('.checkbox_class').eq(j).attr('checked') == 'checked') {
            aPath.push($('.checkbox_class').eq(j).next().next().next().text());
        }
    }
    oPath.dstDir = aPath;
    var jsonPath = JSON.stringify(oPath);
    $('#movePath').val(nowPath);//每次点击都存下当前点击的文件夹路径，取最后一次作为要移动的路径
    $(obj)
    $('.folder').css({backgroundColor: '#fff'});
    $(obj).children('span').css({backgroundColor: '#d2d2d2'});
    if (btn == 0) {//文件夹打开
        $(obj).attr("class", "collapsable");
        $.ajax({
            url: osshttp + '/oss/hdfs/getDir',
            type: 'post',
            data: {'email': user, 'dirUrl': nowPath, 'type': 'user', 'dstDir': jsonPath},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    if (data.data.length > 0) {
                        var str = '<ul>';
                        for (var i = 0; i < data.data.length; i++) {
                            str += '' +
                            '<li class="expandable" btn=0 dirUrl="' + data.data[i].dirUrl + '" onclick="mvChild(event,this);">' +
                            '<div class="hitarea expandable-hitarea"></div>' +
                            '<span class="folder">' + data.data[i].dirName + '</span>' +
                            '</li>';
                        }
                        str += '</ul>';
                        $(obj).append(str);
                    }
                    $(obj).attr("btn", "1");
                } else {
                    alert(data.msg);
                }
            }
        });
    } else {//文件夹关闭
        $(obj).attr("class", "expandable");
        $(obj).find('ul').remove();
        $(obj).attr("btn", "0");
    }
}
//单个移动文件 确定
function move_one() {
    var user = $('#user').val();
    var aChecked = $('.checkbox_class').length;
    var movePath = $('#movePath').val();//要移动到的路径
    var btn = $('.table_box').attr('btn');//判断是几级页面
    var fileSize = $('#share_Files').val();
    var aPath = [];
    var str = /^\S+$/;
    for (var i = 0; i < aChecked; i++) {
        if ($('.checkbox_class').eq(i).attr('checked') == 'checked') {
            var one_movePath = $('.checkbox_class').eq(i).next().next().next().text();
            aPath.push(one_movePath);//以前的路径
        }
    }
    var oPath = new Object();
    oPath.dirUrl = aPath;
    var prevPath = JSON.stringify(oPath);
    if (btn == 1 || btn == 2) {
        var myright = 'user';
    }
    if (btn == 3) {
        var myright = 'hehe';
    }
    if (str.test(movePath)) {
        $.ajax({
            url: osshttp + '/oss/hdfs/mvFile',
            type: 'post',
            data: {'email': user, 'dstDir': movePath, 'dirUrl': prevPath, 'type': myright, 'size': fileSize},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {//一级页面移动,直接刷新
//                if(btn==1){
//                    window.location.reload();
//                }
//                if(btn==2||btn==3){//二级页面和分享页面移动,直接删除当行
                    alert('操作成功');
                    $('#myMoveFile').modal('hide');
                    for (var j = 0; j < aChecked; j++) {
                        if ($('.checkbox_class').eq(j).attr('checked') == 'checked') {
                            $('.checkbox_class').eq(j).parent().parent().remove();
                            j--;//length在变,j删一个j应当往前走一个
                        }
//                    }
                    }
                } else {
                    alert(data.msg);
                }
            }
        });
    } else {
        var dir = confirm('确定移动到根目录?');
        if (dir == true) {
            $.ajax({
                url: osshttp + '/oss/hdfs/mvFile',
                type: 'post',
                data: {'email': user, 'dstDir': movePath, 'dirUrl': prevPath, 'type': myright},
                dataType: 'json',
                success: function (data) {
                    if (data.state == true) {//一级页面移动,直接刷新
//                if(btn==1){
//                    window.location.reload();
//                }
//                if(btn==2||btn==3){//二级页面和分享页面移动,直接删除当行
                        alert('操作成功');
                        $('#myMoveFile').modal('hide');
                        for (var j = 0; j < aChecked; j++) {
                            if ($('.checkbox_class').eq(j).attr('checked') == 'checked') {
                                $('.checkbox_class').eq(j).parent().parent().remove();
                                j--;//length在变,j删一个j应当往前走一个
                            }
//                    }
                        }
                    } else {
                        alert(data.msg);
                    }
                }
            });
        }
    }

}
//批量移动文件  预处理
function all_moveFile() {
    var user = $('#user').val();
    var checkNum = $('.checkbox_class').length;
    $('#two_move').attr('disabled', false);
    var oPath = new Object();
    var aPath = [];
    for (var i = 0; i < checkNum; i++) {
        if ($('.checkbox_class').eq(i).attr('checked') == 'checked') {
            aPath.push($('.checkbox_class').eq(i).next().next().next().text());
        }
    }
    oPath.dstDir = aPath;
    var oldPath = JSON.stringify(oPath);
    $("#browser2")[0].innerHTML = '';
    $.ajax({
        url: osshttp + '/oss/hdfs/getDir',
        type: 'post',
        data: {'email': user, 'dirUrl': '', 'type': 'user', 'dstDir': oldPath},
        dataType: 'json',
        success: function (data) {
            if (data.data.length != 0) {
                if (data.state == true) {
                    var str = '';
                    for (var i = 0; i < data.data.length; i++) {
                        str += '' +
                        '<li class="expandable" btn=0 dirUrl="' + data.data[i].dirUrl + '" onclick="mvChild(event,this);">' +
                        '<div class="hitarea expandable-hitarea"></div>' +
                        '<span class="folder">' + data.data[i].dirName + '</span>' +
                        '</li>';
                    }
                    $("#browser2").append(str);
                } else {
                    alert(data.msg);
                }
            } else {
                $('#browser2')[0].innerHTML = '<span style="color:red;font-size:15px;">不能将文件移动到自身及其子目录下</span>';
                $('#two_move').attr('disabled', true);
            }
        }
    });
}
//批量移动  确定
function move_all() {
    var user = $('#user').val();
    var aChecked = $('.checkbox_class').length;
    var movePath = $('#movePath').val();//要移动到的路径
    var str = /^\S+$/;
    var aPath = [];
    var oPath = new Object();
    for (var i = 0; i < aChecked; i++) {
        if ($('.checkbox_class').eq(i).attr('checked') == 'checked') {
            var one_movePath = $('.checkbox_class').eq(i).next().next().next().text();
            aPath.push(one_movePath);//以前的路径
        }
    }
    oPath.dirUrl = aPath;
    var prevPath = JSON.stringify(oPath);
    if (str.test(movePath)) {
        $.ajax({
            url: osshttp + '/oss/hdfs/mvFile',
            type: 'post',
            data: {'email': user, 'dstDir': movePath, 'dirUrl': prevPath, 'type': 'user'},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    alert('操作成功');
                    $('#allMove').modal('hide');
                    for (var j = 0; j < aChecked; j++) {
                        if ($('.checkbox_class').eq(j).attr('checked') == 'checked') {
                            $('.checkbox_class').eq(j).parent().parent().remove();
                            j--;//length在变,j删一个j应当往前走一个
                        }
                    }
                    if ($('.checkbox_class').length == 0) {
                        $(".all_btns").css({visibility: "hidden"});
                    }

                } else {
                    alert(data.msg);
                }
            }
        });
    } else {
        var dir = confirm('确定移动到根目录?');
        if (dir == true) {
            $.ajax({
                url: osshttp + '/oss/hdfs/mvFile',
                type: 'post',
                data: {'email': user, 'dstDir': movePath, 'dirUrl': prevPath, 'type': 'user'},
                dataType: 'json',
                success: function (data) {
                    if (data.state == true) {
                        alert('操作成功');
                        $('#allMove').modal('hide');
                        for (var j = 0; j < aChecked; j++) {
                            if ($('.checkbox_class').eq(j).attr('checked') == 'checked') {
                                $('.checkbox_class').eq(j).parent().parent().remove();
                                j--;//length在变,j删一个j应当往前走一个
                            }
                        }
                    } else {
                        alert(data.msg);
                    }
                }
            });
        }
    }
}
//全选
function all_chose(obj) {
    if ($(obj)[0].checked) {
        $(".checkbox_class").attr("checked", true);
        if ($(".checkbox_class").length > 1) {
            $(".all_btns").css({visibility: "visible"});
        }
    } else {
        $(".checkbox_class").attr("checked", false);
        $(".all_btns").css({visibility: "hidden"});
    }
}
//取消选择
function not_chose(name) {
    $(".checkbox_class").attr("checked", false);
    $("#" + name).attr("checked", false);
    $(".all_btns").css({visibility: "hidden"});
}
//判断是否全选
function my_chose(name) {
    var btn = 0;
    var n = 0;
    var allNum = $(".checkbox_class").length;
    for (var i = 0; i < allNum; i++) {
        if ($(".checkbox_class")[i].checked == false) {
            btn = 1;
        } else {
            n++
        }
    }
    if (btn == 1) {
        $("#" + name).attr("checked", false);
    } else {
        $("#" + name).attr("checked", true);
    }
    if (n >= 2) {
        $(".all_btns").css({visibility: "visible"});
    } else {
        $(".all_btns").css({visibility: "hidden"});
    }
}
////////////////////////////////////////////////////////////////////////扩容页面操作
//进入立即扩容页面
function go_ossBuy() {
    $.ajax({
        url: osshttp + '/oss/account/getUserFile',
        type: 'post',
        data: {},
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
                var allSto = parseInt(data.data[0].allSto);
                var usedSto = parseInt(data.data[0].useSto);
                var source = $("#oss_buy").html();
                var template = Handlebars.compile(source);
                $(".mymain").html(template(data));
                if(usedSto>=1000){
    //                usedSto=Math.ceil(usedSto / 1000);
                   // $('.store_left').css({width: Math.floor(100 * usedSto / allSto) + '%'}).text(usedSto + 'GB');
                   // $('.store_right').css({width: Math.floor(100 * (allSto - usedSto) / allSto) + '%'}).text(Math.floor((allSto - usedSto)/1000) + 'GB');
                      var storUsed =parseInt(usedSto / 1000)+'.'+parseInt( usedSto%1000/100);
		      $('.store_left').text(storUsed+'GB').css({width:Math.floor((storUsed*1000/allSto)*100)+'%' });
		      $('.store_right').text(allSto/1000-storUsed+'GB').css({width:(1-parseFloat(storUsed*1000/allSto))*100+'%' });
		 }else{
                    $('.store_left').css({width:8 + '%'}).text(usedSto + 'M');
                    $('.store_right').css({width: 92 + '%'}).text(Math.floor((allSto - usedSto)/1000) + 'GB');
                }
                //$('.store_left').css({width: Math.floor(100 * usedSto / allSto) + '%'}).text(usedSto + 'GB');
                //$('.store_right').css({width: Math.floor(100 * (allSto - usedSto) / allSto) + '%'}).text((allSto - usedSto) + 'GB');
            } else {
                alert(data.msg);
            }
        }
    });
}
//确定购买数
function buyNum(obj) {
    $(obj).on('mousemove', function () {
        var buyCost = $(obj).val();
        $("#buy_num").val(buyCost);
        $("#oss_cost").css({visibility: "visible"}).text("￥" + buyCost * 20);
    });
}
///扩容
function buy_oss() {
    var buy_num = $('#buy_num').val();
    var user = $('#user').val();
    $.ajax({
        url: osshttp + '/oss/order/addOrder',
        type: 'post',
        data: {'email': user, 'add_space': buy_num, 'price': buy_num * 20},
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
                alert('购买成功,请等待审核');
            } else {
                alert(data.msg);
            }
        }
    });
}
//返回数据列表
function back_ossAll() {
    window.location.reload();
//    var source   = $("#oss_total").html();
//    var template = Handlebars.compile(source);
//    $(".mymain").html(template());
}
//////////////////////////////////////////////////////////////////////////分享页面操作
//进入分享页面
function go_myshare() {
    var user = $('#user').val();
    $.ajax({
        url: osshttp + '/oss/hdfs/getNextFile',
        type: 'post',
        data: {'email': user, 'type': 'shared'},//分享页面type改变
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
                for (var i = 0; i < data.data.length; i++) {
                    if (data.data[i].type == '文件夹') {
                        data.data[i].url = '/oss/img/file.png';
                    }
                    if (data.data[i].type == '文本') {
                        data.data[i].url = '/oss/img/txt.gif';
                    }
                    if (data.data[i].type == '办公文档') {
                        data.data[i].url = '/oss/img/doc.gif';
                    }
                    if (data.data[i].type == '图片') {
                        data.data[i].url = '/oss/img/jpg.gif';
                    }
                    if (data.data[i].type == '视频') {
                        data.data[i].url = '/oss/img/mov.gif';
                    }
                    if (data.data[i].type == '压缩文件') {
                        data.data[i].url = '/oss/img/zip.gif';
                    }
                    if (data.data[i].type == '演示文稿') {
                        data.data[i].url = '/oss/img/ppt.gif';
                    }
                    if (data.data[i].type == '表格') {
                        data.data[i].url = '/oss/img/excel.gif';
                    }
                    if (data.data[i].type == '电子文档') {
                        data.data[i].url = '/oss/img/pdf.gif';
                    }
                    if (data.data[i].type == '未知文件') {
                        data.data[i].url = '/oss/img/undefined.png';
                    }
                }
                var source = $("#oss_share").html();
                var template = Handlebars.compile(source);
                $(".mymain").html(template(data));
                if (data.data.length == 0) {
                    $('tbody').append('<tr><td colspan="8">暂无数据</td></tr>');
                }
            } else {
                alert(data.msg);
            }
        }
    });
}
//分享页面返回OSS首页
function go_ossAll() {
    window.location.reload();
}
///////////////////////////////////////////////////////////////////子页面1操作
//进入子页面1
function go_childPage1(obj) {
    var myPath = $(obj).next().text();//当前文件夹路径
    var user = $('#user').val();
    var fileType = $(obj).parent().next().next().next().text();
    var navPath = $(obj).next().text();//当前文件夹路径
    if (fileType == '文件夹') {//是文件夹才能点开
        var aPath = navPath.split('/');
        $.ajax({
            url: osshttp + '/oss/hdfs/getNextFile',
            type: 'post',
            data: {'dirUrl': myPath, 'email': user, 'type': 'user'},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    for (var i = 0; i < data.data.length; i++) {
                        if (data.data[i].type == '文件夹') {
                            data.data[i].url = '/oss/img/file.png';
                        }
                        if (data.data[i].type == '文本') {
                            data.data[i].url = '/oss/img/txt.gif';
                        }
                        if (data.data[i].type == '办公文档') {
                            data.data[i].url = '/oss/img/doc.gif';
                        }
                        if (data.data[i].type == '图片') {
                            data.data[i].url = '/oss/img/jpg.gif';
                        }
                        if (data.data[i].type == '视频') {
                            data.data[i].url = '/oss/img/mov.gif';
                        }
                        if (data.data[i].type == '压缩文件') {
                            data.data[i].url = '/oss/img/zip.gif';
                        }
                        if (data.data[i].type == '演示文稿') {
                            data.data[i].url = '/oss/img/ppt.gif';
                        }
                        if (data.data[i].type == '表格') {
                            data.data[i].url = '/oss/img/excel.gif';
                        }
                        if (data.data[i].type == '电子文档') {
                            data.data[i].url = '/oss/img/pdf.gif';
                        }
                        if (data.data[i].type == '未知文件') {
                            data.data[i].url = '/oss/img/undefined.png';
                        }
                    }
                    var source = $("#oss_child").html();
                    var template = Handlebars.compile(source);
                    $(".mymain").html(template(data));
                    if (data.data.length == 0) {
                        $('tbody').append('<tr><td colspan="8">暂无数据</td></tr>');
                    }
                    var str = '';
                    for (var j = 1; j < aPath.length - 1; j++) {//面包屑导航
                        str += '<span> &gt; </span>' + '<span class="navback allPath" onclick="go_backParent1(this);">' + aPath[j] + '</span>';
                    }
                    str += '<span> &gt; </span>' + '<span class="allPath">' + aPath[aPath.length - 1] + '</span>';//最后一个面包屑不能点
                    $('.list_nav').append(str);
                    //myupload_file();
                } else {
                    alert(data.msg);
                }
            }
        });
    }
}
//面包屑点击
function go_backParent1(obj) {
    var user = $('#user').val();
    var allpath = '';
    var prevpath = $(obj).prevAll('.navback');//本身之前的路径
    for (var l = prevpath.length; l > 0; l--) {
        allpath += '/' + prevpath.eq(l - 1).text();
        //console.log(allpath);
    }
    allpath += '/' + $(obj).text();//全路径
    //console.log(allpath);
    $.ajax({
        url: osshttp + '/oss/hdfs/getNextFile',
        type: 'post',
        data: {'dirUrl': allpath, 'email': user, 'type': 'user'},
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {//页面重绘
                for (var i = 0; i < data.data.length; i++) {
                    if (data.data[i].type == '文件夹') {
                        data.data[i].url = '/oss/img/file.png';
                    }
                    if (data.data[i].type == '文本') {
                        data.data[i].url = '/oss/img/txt.gif';
                    }
                    if (data.data[i].type == '办公文档') {
                        data.data[i].url = '/oss/img/doc.gif';
                    }
                    if (data.data[i].type == '图片') {
                        data.data[i].url = '/oss/img/jpg.gif';
                    }
                    if (data.data[i].type == '视频') {
                        data.data[i].url = '/oss/img/mov.gif';
                    }
                    if (data.data[i].type == '压缩文件') {
                        data.data[i].url = '/oss/img/zip.gif';
                    }
                    if (data.data[i].type == '演示文稿') {
                        data.data[i].url = '/oss/img/ppt.gif';
                    }
                    if (data.data[i].type == '表格') {
                        data.data[i].url = '/oss/img/excel.gif';
                    }
                    if (data.data[i].type == '电子文档') {
                        data.data[i].url = '/oss/img/pdf.gif';
                    }
                    if (data.data[i].type == '未知文件') {
                        data.data[i].url = '/oss/img/undefined.png';
                    }
                }
                var source = $("#oss_child").html();
                var template = Handlebars.compile(source);
                $(".mymain").html(template(data));
                if (data.data.length == 0) {
                    $('tbody').append('<tr><td colspan="8">暂无数据</td></tr>');
                }
                var str = '';
                for (var j = prevpath.length; j > 0; j--) {
                    str += '<span>&gt;</span>' + '<span class="navback allPath" onclick="go_backParent1(this);">' + prevpath.eq(j - 1).text() + '</span>';
                }
                str += '<span>&gt;</span>' + '<span class="allPath">' + $(obj).text() + '</span>';
                $('.list_nav').append(str);
//                myupload_file();
            } else {
                alert(data.msg);
            }
        }
    });
}
//返回rds与oss选择
function oss_back_all() {
    window.location.href = "http://product.unionbigdata.com/szy/view/";
}

//分享子页面
function go_childPage2(obj) {
    var myPath = $(obj).next().text();//当前文件夹路径
    var user = $('#user').val();
    var fileType = $(obj).parent().next().next().next().text();
    var navPath = $(obj).next().text();//当前文件夹路径
    if (fileType == '文件夹') {
        var aPath = navPath.split('/');
        $.ajax({
            url: osshttp + '/oss/hdfs/getNextFile',
            type: 'post',
            data: {'dirUrl': myPath, 'email': user, 'type': 'shared'},
            dataType: 'json',
            success: function (data) {
                if (data.state == true) {
                    for (var i = 0; i < data.data.length; i++) {
                        if (data.data[i].type == '文件夹') {
                            data.data[i].url = '/oss/img/file.png';
                        }
                        if (data.data[i].type == '文本') {
                            data.data[i].url = '/oss/img/txt.gif';
                        }
                        if (data.data[i].type == '办公文档') {
                            data.data[i].url = '/oss/img/doc.gif';
                        }
                        if (data.data[i].type == '图片') {
                            data.data[i].url = '/oss/img/jpg.gif';
                        }
                        if (data.data[i].type == '视频') {
                            data.data[i].url = '/oss/img/mov.gif';
                        }
                        if (data.data[i].type == '压缩文件') {
                            data.data[i].url = '/oss/img/zip.gif';
                        }
                        if (data.data[i].type == '演示文稿') {
                            data.data[i].url = '/oss/img/ppt.gif';
                        }
                        if (data.data[i].type == '表格') {
                            data.data[i].url = '/oss/img/excel.gif';
                        }
                        if (data.data[i].type == '电子文档') {
                            data.data[i].url = '/oss/img/pdf.gif';
                        }
                        if (data.data[i].type == '未知文件') {
                            data.data[i].url = '/oss/img/undefined.png';
                        }
                    }
                    var source = $("#oss_share").html();
                    var template = Handlebars.compile(source);
                    $(".mymain").html(template(data));
                    var str = '';
                    for (var j = 1; j < aPath.length - 1; j++) {//面包屑导航
                        str += '<span> &gt; </span>' + '<span class="navback allPath" onclick="go_backParent2(this);">' + aPath[j] + '</span>';
                    }
                    str += '<span> &gt; </span>' + '<span class="allPath">' + aPath[aPath.length - 1] + '</span>';//最后一个面包屑不能点
                    $('.share_nav').append(str);
                } else {
                    alert(data.msg);
                }
            }
        });
    }
}
function go_backParent2(obj) {
    var user = $('#user').val();
    var allpath = '';
    var prevpath = $(obj).prevAll('.navback');//本身之前的路径
    for (var i = 0; i < prevpath.length; i++) {
        allpath += '/' + prevpath.eq(i).text();
    }
    allpath += '/' + $(obj).text();//全路径
    $.ajax({
        url: osshttp + '/oss/hdfs/getNextFile',
        type: 'post',
        data: {'dirUrl': allpath, 'email': user, 'type': 'shared'},
        dataType: 'json',
        success: function (data) {
            if (data.state == true) {
                for (var i = 0; i < data.data.length; i++) {
                    if (data.data[i].type == '文件夹') {
                        data.data[i].url = '/oss/img/file.png';
                    }
                    if (data.data[i].type == '文本') {
                        data.data[i].url = '/oss/img/txt.gif';
                    }
                    if (data.data[i].type == '办公文档') {
                        data.data[i].url = '/oss/img/doc.gif';
                    }
                    if (data.data[i].type == '图片') {
                        data.data[i].url = '/oss/img/jpg.gif';
                    }
                    if (data.data[i].type == '视频') {
                        data.data[i].url = '/oss/img/mov.gif';
                    }
                    if (data.data[i].type == '压缩文件') {
                        data.data[i].url = '/oss/img/zip.gif';
                    }
                    if (data.data[i].type == '演示文稿') {
                        data.data[i].url = '/oss/img/ppt.gif';
                    }
                    if (data.data[i].type == '表格') {
                        data.data[i].url = '/oss/img/excel.gif';
                    }
                    if (data.data[i].type == '电子文档') {
                        data.data[i].url = '/oss/img/pdf.gif';
                    }
                    if (data.data[i].type == '未知文件') {
                        data.data[i].url = '/oss/img/undefined.png';
                    }
                }
                var source = $("#oss_share").html();
                var template = Handlebars.compile(source);
                $(".mymain").html(template(data));
                var str = '';
                for (var j = 0; j < prevpath.length; j++) {
                    str += '<span> &gt; </span>' + '<span class="navback allPath" onclick="go_backParent1(this);">' + prevpath.eq(j).text() + '</span>';
                }
                str += '<span> &gt; </span>' + '<span class="allPath">' + $(obj).text() + '</span>';
                $('.share_nav').append(str);
            } else {
                alert(data.msg);
            }
        }
    });
}
//修改描述
//修改描述预处理
function reUpd(obj){
    var oldDes=$(obj).prev().text().trim();
    $('#newDes').val(oldDes);
    var desName=$(obj).parent().prev().find('.name_box').text();
    $('#desc_name').val(desName);
}
//修改描述确定
function upd_Des(){
    var btn = $('.table_box').attr('btn');
    var user = $('#user').val();
    var desName=$('#desc_name').val();
    var newDesc=$('#newDes').val().trim();
   // var str = /^\S+$/;
   // console.log(str.test(newDesc));
    if(newDesc != ''){
       // var descLen=newDesc.split('');
        //if(descLen>50){
         //   alert('描述内容不能超过50字');
       // }else{
            if(btn==1){
                $.ajax({
                    url:osshttp+'/oss/filedesc/modifyDesc',
                    type:'post',
                    dataType:'json',
                    data:{'filename':desName,'description':newDesc},
                    success:function(data){
                        if(data.state==true){
                            alert('修改描述成功');
                            window.location.reload();
                        }else{
                            alert(data.msg);
                        }
                    }
                });
            }else if(btn==2){
                $.ajax({
                    url:osshttp+'/oss/filedesc/modifyDesc',
                    type:'post',
                    dataType:'json',
                    data:{'filename':desName,'description':newDesc},
                    success:function(data){
                        if(data.state==true){
                            for(var i=0;i<$('.name_box').length;i++){
                                if($('.name_box').eq(i).text()==desName){
                                    $('.name_box').eq(i).parent().next().find('.desc_box').text(newDesc);
                                }
                            }
			$('#udpDes').modal('hide');
                        }else{
                            alert(data.msg);
                        }
                    }
                });
            }

       // }
    }else{
        alert('请输入描述内容');
    }
}
//弹出框判断字数
function gbcount(obj,name){
    var curLength=$(obj).val().length;
    if (curLength > 50) {
        $(obj)[0].value = $(obj)[0].value.substring(0,50);
        $('#'+name).text('0');
    }
    else {
        $('#'+name).text(50 - curLength);
    }
}
