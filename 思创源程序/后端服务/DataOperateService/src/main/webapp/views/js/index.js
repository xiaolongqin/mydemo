$(function(){
    $.when(
        $.ajax({
            url:SLSCHTTP+'/account/getAccount',  //用户
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({
            url:SLSCHTTP+'/module/getLeftMenus',  //左边菜单
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({
            url:SLSCHTTP+'/rightMenus/getNotice',  //公告
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({
            url:SLSCHTTP+'/rightMenus/getReportOrder',  //报表浏览
            type:'post',
            data:{},
            dataType:'json'
        })
    ).done(function(response1,response2,response3,response4){
            if(response1[0].state==false){
                window.location.href=SLSCHTTP+'/views/html/login.html';
                return false;
            }
            $('#user').text(response1[0].data.account.name);  //存用户名
            var leftList=response2[0].data;  //加载1级菜单
            var listStr='';
            for(var i=0;i<leftList.length;i++){
                listStr += '<li btn="1">' +
                    '<div class="list-box" level="'+response2[0].data[i].level+'">' +
                    '<img src="../images/' + response2[0].data[i].image_url + '"alt=""/>' +
                    '<a onclick="topMenus(this);" code=' + response2[0].data[i].code + ' class="top-list">' + response2[0].data[i].name + '</a>' +
                    '<img class="right-pic" src="../images/left-gray.png" alt=""/>' +
                    '</div>' +
                    '</li>';
            }
            $('#left-nav').html(listStr);
            if(response3[0].state==true){   //存公告
                var _len=response3[0].data.length,_i,_list='';
                for(_i=0;_i<_len;_i++){
                    _list+='<li>'+(_i+1)+'. '+
                        '<span class="tip-title" data-toggle="modal" data-target="#myModal" onclick="seeContent(this);">'+response3[0].data[_i].title+'</span>' +
                        '<span style="display: none;">'+response3[0].data[_i].content+'</span>'+
                        '</li>';
                }
            }else{
                _list='<li>暂无公告</li>';
            }
            $('.tip-all').html(_list);
            $('[data-toggle="popover"]').popover();
            $('.right-list').find('p').text('暂无公告');
            var report=response4[0].data;  //存报表排名
            var reportList='';
            for (var j = 0; j < 3; j++) {
                reportList += '<tr>' +
                    '<td>' + (j + 1) + '</td>' +
                    '<td>' + report[j].name + '</td>' +
                    '<td>' + report[j].count + '</td>' +
                    '</tr>';
            }
            $('.report-table').find('tbody').html(reportList);
    });
});
/**
 *查看公告
 */
function seeContent(obj){
    $('#tip-title').text($(obj).text());
    $('#tip-content').text($(obj).next().text());
}
//左边导航1级点击
function topMenus(obj){
    var menuBtn=$(obj).parent().parent().attr('btn');
    $('.right-list').remove();
    $('.middle-box').removeClass('col-md-8').addClass('col-md-10');
    if(menuBtn=='1'){
        var menuCode=$(obj).attr('code');
        var level=$(obj).parent().attr('level');
        $.ajax({
            url:SLSCHTTP+'/module/getNextModule',
            type:'post',
            data:{'parentid':menuCode,'level':level},
            dataType:'json',
            success:function(response){
                var childMenus=response.data;
                var menuList='';
                for(var i=0;i<childMenus.length;i++){
                    if (response.data[i].childstate == '1') {  //有下一级的
                        if(response.data[i].url==null){
                            menuList += '<li btn="1">' +
                                '<div class="list-box" level="'+response.data[i].level+'">' +
                                '<img class="list-hide" src="../images/label1.png" alt="'+ response.data[i].id +'"/>' +
                                '<a onclick="childMenus(this);" code=' + response.data[i].code +' target="iframepage">' + response.data[i].name + '</a>' +
                                '<img class="right-pic" src="../images/left-blue.png" alt=""/>' +
                                '<div class="triangle list-hide"></div>' +
                                '</div>' +
                                '</li>';
                        }else{
                            menuList += '<li btn="1">' +
                                '<div class="list-box" level="'+response.data[i].level+'">' +
                                '<img class="list-hide" src="../images/label1.png" alt="'+ response.data[i].id +'"/>' +
                                '<a onclick="childMenus(this);" href="' + response.data[i].url + '" code=' + response.data[i].code +' target="iframepage">' + response.data[i].name + '</a>' +
                                '<img class="right-pic" src="../images/left-blue.png" alt=""/>' +
                                '<div class="triangle list-hide"></div>' +
                                '</div>' +
                                '</li>';
                        }

                    } else {  //无下一级的
                        if(response.data[i].url==null){
                            menuList += '<li btn="1">' +
                                '<div class="list-box" level="'+response.data[i].level+'">' +
                                '<img class="list-hide" src="../images/label1.png" alt="'+ response.data[i].id +'"/>' +
                                '<a onclick="childMenus(this);" code=' + response.data[i].code +' target="iframepage">' + response.data[i].name + '</a>' +
                                '<div class="triangle list-hide"></div>' +
                                '</div>' +
                                '</li>';
                        }else{
                            menuList += '<li btn="1">' +
                                '<div class="list-box" level="'+response.data[i].level+'">' +
                                '<img class="list-hide" src="../images/label1.png" alt="'+ response.data[i].id +'"/>' +
                                '<a onclick="childMenus(this);" href="' + response.data[i].url + '" code=' + response.data[i].code +' target="iframepage">' + response.data[i].name + '</a>' +
                                '<div class="triangle list-hide"></div>' +
                                '</div>' +
                                '</li>';
                        }
                    }
                }
                menuList='<ul class="list-unstyled tree-list">'+menuList+'</ul>';
                $(obj).parent().parent().append(menuList);
                $(obj).next().attr('src','../images/down-gray.png');
                $(obj).parent().parent().attr('btn','0');
            }
        });
    }else{
        $(obj).parent().parent().find('.tree-list').remove();
        $(obj).next().attr('src','../images/left-gray.png');
        $(obj).parent().parent().attr('btn','1');
    }
}
//1级之下的点击(为什么拆成两个来写，因为TMD的坑爹的点击特效)
function childMenus(obj){
    if($(obj).attr('href')){  //用于修改密码取消的时候页面回跳
        $('#prev-url').text($(obj).attr('href'));
    }
    var menuBtn=$(obj).parent().parent().attr('btn');  //打开才发送ajax
    $('#left-nav').find('.list-chosen').removeClass('list-chosen');
    $(obj).parent().parent().addClass('list-chosen');
    $(obj).parent().children().filter('.triangle').removeClass('list-hide');
    if(menuBtn=='1'){
        var menuCode=$(obj).attr('code');
        var _id=$(obj).prev().attr('alt');
        var _href=$(obj).attr('href');
        var level=$(obj).parent().attr('level');
        var _json;
        if(_href==undefined || _href=='' || _href==null){  //当前点击无页面
            _json={parentid:menuCode,id:0,level:level};
        }else{
            _json={parentid:menuCode,id:_id,level:level};
        }
        $.ajax({
            url:SLSCHTTP+'/module/getNextModule',
            type:'post',
            data:_json,
            dataType:'json',
            success:function(response){
                var childMenus=response.data;
                var menuList='';
                for(var i=0;i<childMenus.length;i++){
                    if (response.data[i].childstate == '1') {  //有下一级的
                        if(response.data[i].url==null){
                            menuList += '<li btn="1">' +
                            '<div class="list-box" level="'+response.data[i].level+'">' +
                            '<img class="list-hide list-hide2" src="../images/label1.png" alt="'+ response.data[i].id +'"/>' +
                            '<a onclick="childMenus(this);" code=' + response.data[i].code +' target="iframepage">' + response.data[i].name + '</a>' +
                            '<img class="right-pic" src="../images/left-blue.png" alt=""/>' +
                            '<div class="triangle list-hide"></div>' +
                            '</div>' +
                            '</li>';
                        }else{
                            menuList += '<li btn="1">' +
                            '<div class="list-box" level="'+response.data[i].level+'">' +
                            '<img class="list-hide list-hide2" src="../images/label1.png" alt="'+ response.data[i].id +'"/>' +
                            '<a onclick="childMenus(this);" href="' + response.data[i].url + '" code=' + response.data[i].code +' target="iframepage">' + response.data[i].name + '</a>' +
                            '<img class="right-pic" src="../images/left-blue.png" alt=""/>' +
                            '<div class="triangle list-hide"></div>' +
                            '</div>' +
                            '</li>';
                        }
                    } else {  //无下一级的
                        //if(response.data[i].url==null){
                        //    menuList += '<li btn="1">' +
                        //    '<div class="list-box">' +
                        //    '<img class="list-hide" src="../images/label1.png" alt=""/>' +
                        //    '<a onclick="childMenus(this);" code=' + response.data[i].code +' target="iframepage" aid=' + response.data[i].id +'>' + response.data[i].name + '</a>' +
                        //    '<div class="triangle list-hide"></div>' +
                        //    '</div>' +
                        //    '</li>';
                        //}else{
                            menuList += '<li btn="1">' +
                            '<div class="list-box" level="'+response.data[i].level+'">' +
                            '<img class="list-hide list-hide2" src="../images/label1.png" alt="'+ response.data[i].id +'"/>' +
                            '<a onclick="childMenus(this);" href="' + response.data[i].url + '" code=' + response.data[i].code +' target="iframepage">' + response.data[i].name + '</a>' +
                            '<div class="triangle list-hide"></div>' +
                            '</div>' +
                            '</li>';
                        //}
                    }
                }
                menuList='<ul class="list-unstyled tree-list">'+menuList+'</ul>';
                $(obj).parent().parent().append(menuList);
                $(obj).parent().parent().attr('btn','0');
                $(obj).parent().children('img').attr('src','../images/down-blue.png');
            }
        });
    }else{
        $(obj).parent().parent().find('.tree-list').remove();
        $(obj).parent().parent().attr('btn','1');
        $(obj).parent().children('img').attr('src','../images/left-blue.png');
    }
}
/**
 *修改密码 
 */
function changePwd(){
    $('.right-list').remove();
    $('.middle-box').removeClass('col-md-8').addClass('col-md-10');
    $('#iframepage').attr('src','change_pwd.html');
}
/**
 *退出
 */
function logout(){
    $.ajax({
        url:SLSCHTTP+'/account/logout',
        type:'post',
        data:{},
        dataType:'json',
        success:function(response){
            if(response.state==true){
                window.location.href=SLSCHTTP+'/views/html/login.html';
            }
        }
    });
}