add_or_edit = 0 ;
$(function(){
    FindPage('',1);
    changePage();
});
function FindPage(find_name,pageNum){
    var page_type=$('input:radio[name="pageFrom"]:checked').val();
    $.ajax({
        url:AJAXURL.searchPage,
        type:'post',
        data:{
            "page_type":page_type,
            "param":find_name,
            'pageNumber':pageNum
        },
        dataType:'json',
        success:function(response){
            var table = $('#PageTable').find('tbody');
            var tab = '' ;
            if(response.state == false){
                tab = '<tr><td colspan="5">查无数据</td></tr>';
            }
            else{
                var length = response.data.length;
                for(var i=0; i<length;i++){
                    var state = '' ;
                    var state_classt = '' ;
                    var state_classf = '' ;
                    if(response.data[i].page_state == 1){
                        state = '生效';
                        state_classt = ' class="status-f"';
                        state_classf = ' class="status-t" onclick="changeState(0,this);"';
                    }
                    else{
                        state = '作废';
                        state_classt = ' class="status-t" onclick="changeState(1,this);"';
                        state_classf = ' class="status-f"';
                    }
                    tab += '<tr>' +
                        '<td>' + response.data[i].page_id + '</td>' +
                        '<td>' + response.data[i].page_name + '</td>' +
                        '<td>' + response.data[i].page_url + '</td>' +
                        '<td pageState="' + response.data[i].page_state +
                            '" version="'+ response.data[i].page_version +
                            '" pagetype="' + response.data[i].page_type + '">' + state + '</td>' +
                        '<td>' +
                            '<a class="edit" onclick="EditPage(this);"> 修改 </a>' +
                            '<a' + state_classt + '"> 生效 </a>' +
                            '<a' + state_classf + '"> 作废 </a>' +
                        '</td>' +
                        '</tr>' ;
                }
                $('#PageTable').find('tbody').attr('totalpage',response.data[0].total);
                $('#totalPage').html("共"+response.data[0].total+"页");
                $('.page').find('span').html(pageNum);
            }
            table.html(tab);
        }
    });
}
//查询
function FindSomePage() {
    var name = $('.edit-page-find').find('input').val();
    FindPage(name,1);
}
/**
 *回车事件
 */
$('#pageName').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        FindSomePage();
    }
});
function EditPage(obj){
    var info = $(obj).parent().parent();
    var pageid = info.find('td').eq(0).html();
    var url = info.find('td').eq(2).html();
    var descrip = info.find('td').eq(1).html();
    var version = info.find('td').eq(3).attr('version');
    var page_state = info.find('td').eq(3).attr('pageState');
    var check_t,check_f = '';
    if(page_state==1){
        check_t = 'checked="checked"';
    }
    else if(page_state==0){
        check_f = 'checked="checked"';
    }
    var pagetype = info.find('td').eq(3).attr('pagetype');
    var main = $('#main') ;
    var str = '<div class="title"><ul><li>流程说明</li></ul></div>' +
        '<div class="user-reg-step num4 clearfix">' +
            '<dl class="doing">' +
                '<dt class="st-num">' +
                    '<div class="st-bg">1</div>' +
                '</dt>' +
                '<dd class="st-text">修改页面</dd>' +
            '</dl>' +
            '<dl>' +
                '<dt class="st-num"><div class="st-bg succ">2</div></dt>' +
                '<dd class="st-text">修改按钮</dd>' +
            '</dl>' +
            '<dl><dt class="st-num"><div class="st-bg succ">3</div></dt><dd class="st-text">完成</dd></dl>' +
        '</div>' +
        '<div class="content-of-add-or-edit">' +
            '<div class="content clearfix add-page-info">' +
            '<div style="margin:50px 0 0 30%;text-align: center;">' +
            '<div class="row clearfix">' +
            '<label class="col-md-2 control-label">页面ID</label>' +
            '<div class="col-md-4" style="padding: 0">' +
            '<input type="text" class="form-control input-sm" placeholder="示例：100031" value="' + pageid + '" disabled>' +
            '</div>' +
        '</div>' +
        '<div class="row clearfix">' +
        '<label class="col-md-2 control-label">页面URL</label>' +
        '<div class="col-md-4" style="padding: 0">' +
        '<input type="text" class="form-control input-sm" placeholder="示例：100031" value="' + url + '">' +
        '</div>' +
        '</div>' +
        '<div class="row clearfix">' +
        '<div class="col-md-2 text-right">' +
        '<p style="line-height: 30px;">页面归属</p>' +
        '</div>' +
        '<select class="input-sm col-md-4" id="selectList" style="height: 30px;">' +
        '<option value="1">用户端</option>' +
        '<option value="2">商户端</option>' +
        '<option value="3">管家端</option>' +
        '</select>' +
        '</div>' +
        '<div class="row clearfix">' +
        '<div class="col-md-2 text-right"><p>页面状态</p></div>' +
        '<div class="col-md-4">' +
        '<label class="radio-inline">' +
        '<input class="input-sm" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="1" ' + check_t + '/>生效' +
        '</label>' +
        '<label class="radio-inline">' +
        '<input class="input-sm" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="0" ' + check_f + '/>作废' +
        '</label></div></div><div class="row clearfix">' +
        '<label class="col-md-2 control-label">页面描述</label>' +
        '<div class="col-md-4" style="padding: 0">' +
        '<input type="text" class="form-control input-sm" placeholder="示例：100031" value="' + descrip + '">' +
        '</div>' +
        '</div>' +
        '<div class="row clearfix">' +
        '<label class="col-md-2 control-label">版本号</label>' +
        '<div class="col-md-4" style="padding: 0">' +
        '<input type="text" class="form-control input-sm" placeholder="示例：100031" value="' + version + '">' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div class="btn-small"><a onclick="Next();" style="float:none;margin:0 auto;">下一步</a></div></div></div>';
    main.html(str);
}
function changeState(turn,obj){
    var page_id = $(obj).parent().parent().find('td').eq(0).html();
    $.ajax({
        url:AJAXURL.changeState,
        type:'post',
        data:{
            "page_id":page_id,
            "page_state":turn
        },
        dataType:'json',
        success:function(response){
            if(response.state == true){
                FindPage('');
            }
        }
    });
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
        totalpage =  parseInt($('#PageTable').find('tbody').attr('totalpage'));
        pagenow = parseInt($.trim(page.find('span').text()));
        name = $.trim($('#pageName').val());
        if(pagenow <= 1){
            alert("已经是第一页了");
            return false;
        }
        else{
            FindPage(name,(pagenow-1));
        }
        $('.page').find('input').val('');
    });

    changebtn.eq(1).click(function(){
        totalpage =  parseInt($('#PageTable').find('tbody').attr('totalpage'));
        pagenow = parseInt($.trim(page.find('span').text()));
        name = $.trim($('#pageName').val());
        if(pagenow >= totalpage){
            alert("已到达最后一页");
            return false;
        }
        else{
            FindPage(name,(pagenow+1));
        }
        $('.page').find('input').val('');
    });

    changebtn.eq(2).click(function(){
        totalpage =  parseInt($('#PageTable').find('tbody').attr('totalpage'));
        pagejmp = $.trim(page.find('input').val());
        if(pagejmp > totalpage){
            alert("最大页数是"+totalpage+"，不能超过最大页数");
            return false;
        }
        else if(pagejmp>0 && (/^\d+$/.test(pagejmp))){
            FindPage(name,(pagejmp));
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
    var totalpage =  parseInt($('#PageTable').find('tbody').attr('totalpage'));
    var name = $.trim($('#pageName').val());
    if (evt.keyCode==13){
        evt.preventDefault();
        if(pagejmp > totalpage){
            alert("最大页数是"+totalpage+"，不能超过最大页数");
            return false;
        }
        else if(pagejmp>0 && (/^\d+$/.test(pagejmp))){
            FindPage(name,(pagejmp));
        }
        else{
            alert("请输入大于0的整数");
        }
    }
});