$(function(){
//    setInterval(reinitIframe,200);
    var timer,timer2;
//  用户菜单
    $('#userName')[0].onmouseover = show;
    $('#userName')[0].onmouseout = hide;

    $('.subMenu-box')[0].onmouseover = show;
    $('.subMenu-box')[0].onmouseout = hide;
    function show(){
        clearInterval( timer );
        $('.subMenu-box')[0].style.display = 'block';
    }
    function hide(){
        timer = setTimeout(function(){
            $('.subMenu-box')[0].style.display = 'none';
        }, 100);
    }
//  用户菜单
    $('.notice-pic')[0].onmouseover = show2;
    $('.notice-pic')[0].onmouseout = hide2;

    $('.notice-all')[0].onmouseover = show2;
    $('.notice-all')[0].onmouseout = hide2;
    function show2(){
        clearInterval( timer2 );
        $('.notice-all')[0].style.display = 'block';
    }
    function hide2(){
        timer2 = setTimeout(function(){
            $('.notice-all')[0].style.display = 'none';
        }, 100);
    }
    $.when(
        $.ajax({  //获取账户
            url:AJAXURL.getAccountMe,
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({  //获取消息
            url:AJAXURL.getLastedNotice,
            type:'post',
            data:{},
            dataType:'json'
        })
    ).done(function(response1,response2){
//  未登录拦截
            if(response1[0].state==false){
                window.location.href='login.html';
                return false;
            }
//  获取账户
            var data1=response1[0].data;
            var level=data1.account.level;
            var str='';
            $('#userName').text(data1.account.name);
            $('#userId').text(data1.account.id);
            //用户菜单
            if(parseInt(level)==1){  //管理员
                str+='<li>' +
                    '<img src="../images/manage-notice.png"/>' +
                    '<span data-href="notice_manage.html">管理公告</span>' +  //onclick="goPage(this);"
                    '</li>' +
                    '<li>' +
                    '<img src="../images/yijian.png"/>' +
                    '<span>查看意见</span>' +
                    '</li>' +
                    '<li>' +
                    '<img src="../images/shenhe.png"/>' +
                    '<span>申请审核</span>' +
                    '</li>' +
                    '<li>' +
                    '<img src="../images/userLogo.png"/>' +
                    '<span>用户管理</span>' +
                    '</li>';
            }else{  //普通用户
                str+='<li>' +
                    '<img src="../images/manage-notice.png"/>' +
                    '<span>意见反馈</span>' +
                    '</li>' +
                    '<li>' +
                    '<img src="../images/yijian.png"/>' +
                    '<span>修改信息</span>' +
                    '</li>' +
                    '<li>' +
                    '<img src="../images/shenhe.png"/>' +
                    '<span>继续订购</span>' +
                    '</li>';
            }
            $('#subMenu').prepend(str);
//  获取消息
            var data2=response2[0].data;
            var str2='';
            if(data2.length==0){
                $('.notice-box>img').attr('src','../images/no-notice.png');
                str2+='<li class="content-title clearfix">暂无消息</li>';
                $('.notice-list').append(str2);
            }else{
                $('.notice-box>img').attr('src','../images/notice.png');
                for(var i=0;i<data2.length;i++){
                    str2+='<li class="content-title clearfix">' +
                        '<span class="notice-title">'+(i+1)+'.'+data2[i].title+'</span>' +
                        '<span class="notice-time">'+data2[i].time+'</span>' +
                        '</li>' +
                        '<li class="notice-content">'+data2[i].content+'</li>';
                }
                $('.notice-list').append(str2);
            }
//            $('#iframepage').attr('src','fangyan-zonglan.html');
    });
});
//1级导航点击
$('.top-list .list-name').on('click',function(){
    var _this=$(this);
    var _src=_this.prev().attr('src').split('-');
    if(!_this.parent().next().is(":animated")){
        $('.child-list').not(_this.parent().next()).slideUp(200);  //动画效果
//        $('.child-list .list-img').attr('src','../images/left-second-nor.png');
//        $('.child-list .list-name').removeClass('child-list-active');
        if(_this.parent().next().is(':visible')){
            _this.parent().next().slideUp(200);
        }else{
            _this.parent().next().slideDown(200);
        }
        $('.list-name').removeClass('list-name-white');  //字体改变
        _this.addClass('list-name-white');
        var _topList=$('.top-list');
        for(var i=0;i<_topList.length;i++){  //图片改变
            var _newSrc=_topList.eq(i).find('.list-img').attr('src').split('-');
            _topList.eq(i).find('.list-img').attr('src',_newSrc[0]+'-'+_newSrc[1]+'-normal.png');
        }
        _this.prev().attr('src',_src[0]+'-'+_src[1]+'-active.png');
    }
});
//2级导航点击
$('.child-list .list-name').on('click',function(){
    var _href=$(this).attr('data-href');
    $('#iframepage').attr('src',_href);
    $('.child-list .list-img').attr('src','../images/left-second-nor.png');
    $('.child-list .list-name').removeClass('child-list-active');
    $(this).addClass('child-list-active').prev().attr('src','../images/left-second-act.png');
    //reinitIframe();
});
//公告点击
$('body').on('click','.content-title',function(){
    var _this=$(this);
    $('.notice-content').not(_this.next()).slideUp(200);  //动画效果
    _this.addClass('notice-active').siblings().removeClass('notice-active');
    if(!_this.next().is(":animated")){
        if(_this.next().is(':visible')){
            _this.next().slideUp(200);
        }else{
            _this.next().slideDown(200);
        }
    }
});
//退出
function logOut(){
    $.ajax({
        url:AJAXURL.logout,
        type:'post',
        data:{},
        dataType:'json',
        success:function(response){
            window.location.href='login.html';
        }
    });
}
//iframe自适应
//function reinitIframe(){
//    var iframe = document.getElementById("iframepage");
//    var bHeight = iframe.contentWindow.document.body.scrollHeight;
//    var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
//    var height = Math.min(bHeight, dHeight);
//    iframe.height =  height;
//    if(height<400){
//        height=400;
//    }
//    $('.left-nav').css('height',(height+40)+'px');
//}
$('.web-box').on('click',function(){
    window.location.reload();
});
//用户操作页
function goPage(obj){
    var This=$(obj);
    var Href=This.attr('data-href');
    var iframe = document.getElementById("iframepage");
    $(iframe).attr('src',Href);
}