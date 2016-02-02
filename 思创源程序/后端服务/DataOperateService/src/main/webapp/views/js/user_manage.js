$(function(){
    getAccounts();
    changePage();
});
/*一开始获取全部数据*/
function getAccounts(name,pageNum){
    $.ajax({
        url:AJAXURL.searchAccount,
        type:'post',
        data:{
            "name":name,
            "pageNumber":pageNum
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
                        personList += '<tr roleid="' + person[i].id + '"><td>' + person[i].name + '</td>' +
                            '<td>' + person[i].role_name + '</td>' +
                            '<td><a href="../html/edit_user.html?id=' + person[i].id + '">编辑</a><a href="javascript:;" onclick="deleteAccount(this);" data-toggle="modal" data-target="#myModal-del">删除</a></td>' +
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
//查找用戶
function searchAccount(){
    var name = $.trim($('#characterName').val());
        getAccounts(name,1);
    $('.page').find('input').val('');
}
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
            getAccounts(name,(pagenow-1));
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
            getAccounts(name,(pagenow+1));
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
            getAccounts(name,(pagejmp));
        }
        else{
            alert("请输入大于0的整数");
        }
    });
}
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
            getAccounts(name,(pagejmp));
        }
        else{
            alert("请输入大于0的整数");
        }
    }
});
/**
 *搜索回车事件
 */
$('#characterName').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        searchAccount();
        $('.page').find('input').val('');
    }
});
//删除用户
function deleteAccount(obj){
    var roleid = $(obj).parent().parent().attr('roleid');
    var delConfirm = $('.dialog-yes');
    shade();
    delConfirm.click(function(){
        $.ajax({
            url:AJAXURL.deleteAccount,
            type:'post',
            data:{
                "uid":roleid
            },
            dataType:'json',
            success: function(response){
                if(response.state == true){
                    var pageNum = $('.page').find('span').html();
                    $('#myModal-del').modal('hide');
                    closeShade();
                    getAccounts('',pageNum);
                }
            }
        });
    });
}
function modalNo(){
    var pageNum = $('.page').find('span').html();
    $('#myModal-del').modal('hide');
    closeShade();
    getAccounts('',pageNum);
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