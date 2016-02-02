//切换选项
$('.go-left').on('click',function(){
    var This=$(this);
    var len=This.next().find('.change-list-box').children('li').length;
    if(len==1){
        return false;
    }
    var index=parseInt(This.next().find('.change-list-box').children('li:visible').index());  //当前li
    This.siblings('.go-right').find('img').attr('src','../images/yellow-right.png');
    if(index!=len-1){
        //This.parent().parent().next().css({'display':'none'});
        This.next().children('.change-list-box').children().eq(index+1).css('display','block').siblings().css('display','none');
    }
    if(index+2==len){
        This.find('img').attr('src','../images/grey-left.png');  //最后一个li不能点击
        return false;
    }
});
$('.go-right').on('click',function(){
    var This=$(this);
    var len=This.prev().find('.change-list-box').children('li').length;
    if(len==1){
        return false;
    }
    var index=parseInt(This.prev().find('.change-list-box').children('li:visible').index());
    This.siblings('.go-left').find('img').attr('src','../images/yellow-left.png');
    if(index-1>=0){
        //This.parent().parent().next().css({'display':'none'});
        This.prev().children('.change-list-box').children().eq(index-1).css('display','block').siblings().css('display','none');
    }
    if(index-1==0){
        This.find('img').attr('src','../images/grey-right.png');
    }
});
//销售额销售量评论数切换
$('.price-number').on('click',function(){
    var index=$(this).index();
    $(this).addClass('pn-active').siblings().removeClass('pn-active');
    var channel;
    if(parseInt(index)==2){
        channel=AJAXURL.channel2;
    }else{
        channel=AJAXURL.channel1;
    }
    var strTd='',strTr='',strAll='',strLi='';
    var channelList=[];
    for(var j=0;j<channel.length;j++){
        strLi+='<li><span>'+channel[j]+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
        channelList.push(channel[j]);
        strTd+='<td><div class="channel-name-box channel-active" onclick="channelChange(this);">'+channel[j]+'</div></td>';
        if((j+1)%3==0){  //每三个一行
            strTr='<tr>'+strTd+'</tr>';
            strAll+=strTr;
            strTr='';
            strTd='';
        }
    }
    strTr='<tr>'+strTd+'</tr>';  //每三个一行最后剩下的再为一行
    strAll+=strTr;
    var parent=$(this).parent().parent().parent();
    parent.siblings('.channel-box').find('tbody').html(strAll);
    parent.parent().siblings('.channel-row').find('ul').html('<li>渠道 :</li>'+strLi);
    parent.parent().siblings('.channel-row').css({'display':'block'});
});
//渠道对比选择
function chooseChannel(obj){
    var This=$(obj);
    var channelList;
    var len;
    var text;
    if(This.hasClass('channel-active')){
        This.removeClass('channel-active');
        channelList=This.parent().parent().parent().find('.channel-active');
        text=This.text();
        var spanList=$('.channel-row').find('span');
        spanList.each(function(){
            if($(this).text() == text){
                $(this).parent().remove();
            }
        });
        len=channelList.length;
        if(len==0){
            $('.channel-row').css('display','none');
        }
    }else{
        var str='';
        $('.channel-row').css('display','block');
        channelList=This.parent().parent().parent().find('.channel-active');
        len=channelList.length;
        if(len>=5){
            return false;
        }
        This.addClass('channel-active');
        text=This.text();
        str+='<li><span>'+text+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
        $('.channel-row>ul').append(str);
    }
}
//删除渠道
function delChannel(obj){
    var This=$(obj);
    var text=This.prev().text();
    var channelList=This.parent().parent().parent().siblings().find('.channel-table').find('.channel-active');
    channelList.each(function(){
        if($(this).text()==text){
            $(this).removeClass('channel-active');
        }
    });
    This.parents('.channel-row').siblings('.change-row').find('.channel-all').css('color','#7b797c').prev().attr('src','../images/check-none.png');
    var len=This.parent().siblings().length;
    if(len==1){
        This.parents('.channel-row').css('display','none');
        This.parents('.channel-row').siblings('.change-row').find('.channel-none').css('color','#f3b026').prev().attr('src','../images/check-all.png');
    }
    This.parent().remove();
}
//选择要查看的渠道，全选反选
function channelChange(obj){
    var This=$(obj);
    var channelBox=This.parents('.channel-table');
    var allNum=channelBox.find('.channel-name-box').length;
    var len;
    var text;
    var spanList=channelBox.parent().parent().siblings('.channel-row');
    if(This.hasClass('channel-active')){
        This.removeClass('channel-active');
        channelBox.siblings().find('.channel-all').css({'color':'#7b797c'}).prev().attr('src','../images/check-none.png');
        text=This.text();
        spanList.find('span').each(function(){
            if($(this).text() == text){
                $(this).parent().remove();
            }
        });
        len=spanList.find('li').length;
        if(len==1){
            spanList.css('display','none');
            channelBox.siblings().find('.channel-none').css({'color':'#f3b026'}).prev().attr('src','../images/check-all.png');
        }
    }else{
        var str='';
        spanList.css('display','block');
        This.addClass('channel-active');
        channelBox.siblings().find('.channel-none').css({'color':'#7b797c'}).prev().attr('src','../images/check-none.png');
        text=This.text();
        str+='<li><span>'+text+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
        spanList.find('ul').append(str);
    }
    var newNum=channelBox.find('.channel-active').length;
    if(newNum==allNum){
        channelBox.siblings().find('.channel-all').css({'color':'#f3b026'}).prev().attr('src','../images/check-all.png');
    }
}
//全选
function allChoose(obj){
    var This=$(obj);
    This.find('img').attr('src','../images/check-all.png');
    This.find('span').css({'color':'#f3b026'});
    This.prev().find('img').attr('src','../images/check-none.png');
    This.prev().find('span').css({'color':'#7b797c'});
    This.parent().prev().prev().find('.channel-name-box').addClass('channel-active');
    var strLi='';
    This.parent().siblings('.channel-table').find('.channel-active').each(function(){
        strLi+='<li><span>'+$(this).text()+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
    });
    This.parents('.change-row').siblings('.channel-row').find('li').not(':first').remove();
    This.parents('.change-row').siblings('.channel-row').find('ul').append(strLi);
    This.parents('.change-row').siblings('.channel-row').css('display','block');
}
//全不选
function noneChoose(obj){
    var This=$(obj);
    This.find('img').attr('src','../images/check-all.png');
    This.find('span').css({'color':'#f3b026'});
    This.next().find('img').attr('src','../images/check-none.png');
    This.next().find('span').css({'color':'#7b797c'});
    This.parent().prev().prev().find('.channel-name-box').removeClass('channel-active');
    This.parents('.change-row').siblings('.channel-row').find('li').not(':first').remove();
    This.parents('.change-row').siblings('.channel-row').css('display','none');
}
//详情显示
$('.title-tips').on('mouseover',function(){
    var This=$(this);
    This.css('width','58px');
    This.children('img').attr('src','../images/tip-y.png');
    This.children('span').css('display','inline-block');
});
//查看详情
function seeTips(obj){
    $('.tip-box').css('display','none');
    $(obj).siblings('.tip-box').css('display','block');
}
//详情前一页
function tipPrevPage(obj){
    var This=$(obj);
    var pageBox=This.parent().parent().find('.page-box');
    var nowPage=parseInt(pageBox.find('span').eq(0).text());
    var totalPage=parseInt(pageBox.find('span').eq(1).text());
    if(nowPage==1){
        nowPage=totalPage;
    }else{
        nowPage--;
    }
    pageBox.find('span').eq(0).text(nowPage);
    This.parent().siblings('.tip-content').css('display','none');
    This.parent().siblings('.tip-content').eq(nowPage-1).css('display','block');
}
//详情后一页
function tipNextPage(obj){
    var This=$(obj);
    var pageBox=This.parent().parent().find('.page-box');
    var nowPage=parseInt(pageBox.find('span').eq(0).text());
    var totalPage=parseInt(pageBox.find('span').eq(1).text());
    if(nowPage==totalPage){
        nowPage=1;
    }else{
        nowPage++;
    }
    pageBox.find('span').eq(0).text(nowPage);
    This.parent().siblings('.tip-content').css('display','none');
    This.parent().siblings('.tip-content').eq(nowPage-1).css('display','block');
}
//事件委托
$(document).bind("click",function(e){
    var target = $(e.target);
    if(target.next(".tip-box").length == 0 && !target.parents().hasClass('tip-box')){
        $(".tip-box").css('display','none');
    }
});
//导出显示
$('.down-box').on('mouseover',function(){
    var This=$(this);
    This.css('width','58px');
    This.children('img').attr('src','../images/download-y.png');
    This.children('span').css('display','inline-block');
});
//进入新窗口
function openWindow(obj){
    var url=$(obj).attr('title');
    if(url == 'null' || url == '暂无地址'){
        alert('暂无地址');
    }else{
        window.open(url);
    }
}

//  鼠标移入移出
var timer;
$('.channel-btn').on('mouseover',function(){
    var index=$(this).index('.channel-btn');
    show(index);
});
$('.channel-btn').on('mouseout',function(){
    var index=$(this).index('.channel-btn');
    hide(index);
});

$('.channel-box').on('mouseover',function(){
    var index=$(this).index('.channel-box');
    show(index);
});
$('.channel-box').on('mouseout',function(){
    var index=$(this).index('.channel-box');
    hide(index);
});
function show(index){
    clearInterval( timer );
    $('.channel-box')[index].style.display = 'block';
}
function hide(index){
    timer = setTimeout(function(){
        $('.channel-box')[index].style.display = 'none';
    }, 200);
}
//渠道
function channelLists(){
    var strTd='',strTr='',strAll='',strLi='';
    var channelList=[];
    for(var j=0;j<AJAXURL.channel1.length;j++){
        strLi+='<li><span>'+AJAXURL.channel1[j]+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
        channelList.push(AJAXURL.channel1[j]);
        strTd+='<td><div class="channel-name-box channel-active" onclick="channelChange(this);">'+AJAXURL.channel1[j]+'</div></td>';
        if((j+1)%3==0){  //每三个一行
            strTr='<tr>'+strTd+'</tr>';
            strAll+=strTr;
            strTr='';
            strTd='';
        }
    }
    strTr='<tr>'+strTd+'</tr>';  //每三个一行最后剩下的再为一行
    strAll+=strTr;
    $('.channel-box').find('tbody').html(strAll);
    $('.channel-row>ul').append(strLi);
}