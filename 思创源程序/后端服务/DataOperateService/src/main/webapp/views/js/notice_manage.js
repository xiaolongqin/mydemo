$(function(){
    getAllNotice();
    changePage();
});
/*一开始获取全部数据*/
function getAllNotice(name,pageNum){
    $.ajax({
        url:AJAXURL.searchNotice,
        type:'post',
        data:{
            'content':name,
            'pageNumber':pageNum
        },
        dataType:'json',
        success: function (response) {
            if(response.state == true){
                var person = response.data ;
                var personList = '';
                if(person.length==0){
                    personList='<tr><td colspan="3">查无数据</td></tr>' ;
                }else{
                    for(var i=0;i<person.length;i++){
                        personList += '<tr roleid="' + person[i].id + '"><td>' + person[i].title + '</td>' +
                            '<td>' + person[i].content + '</td>' +
                            '<td><a href="javascript:;" onclick="modifyNotice(this);">编辑</a><a href="javascript:;" onclick="delNotice(this);" data-toggle="modal" data-target="#myModal-del">删除</a></td>' +
                            '</tr>' ;
                    }
                    $('#characterTable').find('tbody').attr('totalpage',person[0].total);
                    $('#totalPage').html("共"+person[0].total+"页");
                }
                $('#characterTable').find('tbody').html(personList);
                $('.page').find('span').html(pageNum);
            }
            else{
                $('#characterTable').find('tbody').html('<tr><td colspan="3">查无数据</td></tr>');
            }
        }
    });
}

function Add_Character () {
    shade();
    Reset_Info();
    $('#myModal').attr('add','1');
}
function Reset_Info() {
    var dialog = $('#myModal');
    dialog.find('input').val('');
    dialog.find('textarea').val('');
    $('.dialog-content').find('div').attr('flag',0);
}
function hidered(){
    var dialog = $('#myModal');
    var name = dialog.find('input').val().trim();
    var description = dialog.find('textarea').val().trim();
    if( name != '' ){
        $('.input-alert-name').addClass('hidden');
    }
    else{
        $('.input-alert-name').removeClass('hidden');
    }
    if( description != '' ){
        $('.input-alert-des').addClass('hidden');
    }
    else{
        $('.input-alert-des').removeClass('hidden');
    }
}
/*点击保存按钮时*/
function Save_Info(){
    var dialog = $('#myModal');
    var check = $('.dialog-check-item');
    var name = $.trim(dialog.find('input').val());
    var description = $.trim(dialog.find('textarea').val());
    if( name == '' ){
        $('.input-alert-name').removeClass('hidden');
    }
    if( description == '' ){
        $('.input-alert-des').removeClass('hidden');
    }
    if( name && description ){
        if( dialog.attr('add') == 1 ){
            $.ajax({
                url:AJAXURL.addNotice,
                type:'post',
                data:{
                    'title':name,
                    'content':description
                },
                dataType:'json',
                success: function (response){
                    if(response.state==true){
                        getAllNotice();
                        $('#myModal').modal('hide');
                        closeShade();
                    }
                }
            });
        }
        else{
            $.ajax({
                url:AJAXURL.modifyNotice,
                type:'post',
                data:{
                    "id":dialog.attr('editID'),
                    "title":name,
                    "content":description
                },
                dataType:'json',
                success: function(response){
                    if(response.state == true){
                        $('#myModal').modal('hide');
                        closeShade();
                        getAllNotice();
                    }
                }
            });
        }
    }
}
/*查找指定的公告*/
function findNotice(){
    var name = $.trim($('#characterName').val());
//    if(name == ''){
//        alert('请输入查找的关键字');
//    }
//    else{
    getAllNotice(name,1);
//        $.ajax({
//            url:AJAXURL.searchNotice,
//            type:'post',
//            data:{
//                'content':name,
//                'pageNumber':pageNum
//            },
//            dataType:'json',
//            success: function (response) {
//                if(response.state == true){
//                    var person = response.data ;
//                    var personList = '';
//                    if(person.length==0){
//                        personList='<tr><td colspan="3">查无数据</td></tr>' ;
//                    }else{
//                        for(var i=0;i<person.length;i++){
//                            personList += '<tr roleid="' + person[i].id + '"><td>' + person[i].title + '</td>' +
//                                '<td>' + person[i].content + '</td>' +
//                                '<td><a href="javascript:;" onclick="modifyNotice(this);">编辑</a><a href="javascript:;" onclick="delNotice(this);">删除</a></td>' +
//                                '</tr>' ;
//                        }
//                    }
//                    $('#characterTable').find('tbody').html(personList);
//                }
//                else{
//                    $('#characterTable').find('tbody').html('<tr><td colspan="3">查无数据</td></tr>');
//                }
//            }
//        });
//    }
}
//修改公告
function modifyNotice(obj){
    shade();
    var modal = $('#myModal');
    Reset_Info();
    var name = $(obj).parent().parent().find('td').eq(0).html();
    var descr = $(obj).parent().parent().find('td').eq(1).html();
    var roleid = $(obj).parent().parent().attr('roleid');
    modal.modal('show');
    modal.attr('add','0');
    modal.attr('editID',roleid);
    $('#parentID').val(name);
    $('#childID').val(descr);


}
//删除公告
function delNotice(obj){
    var roleid = $(obj).parent().parent().attr('roleid');
    var delConfirm = $('.dialog-yes');
    shade();
    delConfirm.click(function(){
        $.ajax({
            url:AJAXURL.deleteNotice,
            type:'post',
            data:{
                "id":roleid
            },
            dataType:'json',
            success: function(response){
                if(response.state == true){
                    $('#myModal-del').modal('hide');
                    closeShade();
                    getAllNotice();
                }
            }
        });
    });
}
function modalNo(){
    $('#myModal-del').modal('hide');
    closeShade();
    getAllNotice();
}
//遮罩
function shade(){
    var divShade=window.parent.document.createElement('div');
    divShade.style.width='100%';
    divShade.style.height=window.parent.document.body.clientHeight+50+'px';
    divShade.style.position='absolute';
    divShade.style.top=0;
    divShade.style.left=0;
    divShade.style.zIndex=999;
    divShade.style.backgroundColor="#2C2C2C";
    divShade.style.filter="alpha(opacity=60)";
    divShade.style.opacity="0.6";
    $(divShade).addClass('shade');
    window.parent.document.body.appendChild(divShade);
}
//去掉遮罩
function closeShade(){
    $(window.parent.document.body).find('.shade').remove();
}
$('body').on('keydown',function(event){
    event = event || window.event;
    if(event.keyCode==27){
        setTimeout(closeShade,110);
    }
});
/*翻页*/
function changePage(){
    var page = $('.page');
    var changebtn = page.find('a');
    var pagenow ;
    var name ;
    var pagejmp;
    var totalpage ;
    changebtn.eq(0).click(function(){
        totalpage =  parseInt($('#characterTable').find('tbody').attr('totalpage'));
        pagenow = parseInt($.trim(page.find('span').text()));
        name = $.trim($('#characterName').val());
        if(pagenow <= 1){
            alert("已经是第一页了");
            return false;
        }
        else{
            getAllNotice(name,(pagenow-1));
        }
        $('.page').find('input').val('');
    });

    changebtn.eq(1).click(function(){
        totalpage =  parseInt($('#characterTable').find('tbody').attr('totalpage'));
        pagenow = parseInt($.trim(page.find('span').text()));
        name = $.trim($('#characterName').val());
        if(pagenow >= totalpage){
            alert("已到达最后一页");
            return false;
        }
        else{
            getAllNotice(name,(pagenow+1));
        }
        $('.page').find('input').val('');
    });

    changebtn.eq(2).click(function(){
        totalpage =  parseInt($('#characterTable').find('tbody').attr('totalpage'));
        pagejmp = $.trim(page.find('input').val());
        if(pagejmp > totalpage){
            alert("最大页数是"+totalpage+"，不能超过最大页数");
            return false;
        }
        else if(pagejmp>0 && (/^\d+$/.test(pagejmp))){
            getAllNotice(name,(pagejmp));
        }
        else{
            alert("请输入大于0的整数");
        }
    });
}
/**
 *回车事件
 */
$('#characterName').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        findNotice();
        $('.page').find('input').val('');
    }
});
/**
 *跳转页数回车事件
 */
$('.page').find('input').on('keydown',function(event){
    var page = $('.page');
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    var pagejmp = $.trim(page.find('input').val());
    var totalpage =  parseInt($('#characterTable').find('tbody').attr('totalpage'));
    var name = $.trim($('#characterName').val());
    if (evt.keyCode==13){
        evt.preventDefault();
        if(pagejmp > totalpage){
            alert("最大页数是"+totalpage+"，不能超过最大页数");
            return false;
        }
        else if(pagejmp>0 && (/^\d+$/.test(pagejmp))){
            getAllNotice(name,(pagejmp));
        }
        else{
            alert("请输入大于0的整数");
        }
    }
});