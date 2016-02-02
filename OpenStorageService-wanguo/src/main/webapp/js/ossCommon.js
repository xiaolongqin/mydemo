//显示RDS下拉
var timer=null;
//var myNavHttp='';//
//var logout = 'http://192.168.2.32:8080';//oss logout
var logout = '';//oss logout

$('.rds_list li').hover(function(){
    clearTimeout(timer);
    $('.rds_do').css({display:'block'});
},function(){
    timer=setTimeout(function(){
        $('.rds_do').css({display:'none'});
    },10);
});
$('.rds_do').hover(function(){
    clearTimeout(timer);
},function(){
    timer=setTimeout(function(){
        $('.rds_do').css({display:'none'});
    },10);
});
//显示用户名下拉
$('.do_list>li').eq(0).hover(function(){
    $('.user_do').css({display:'block'});
},function(){
    $('.user_do').css({display:'none'});
});
//显示消息下拉
$('.do_list>li').eq(1).hover(function(){
    $('.info_do').css({display:'block'});
},function(){
    $('.info_do').css({display:'none'});
});
//导航消息
//function see_mynews(obj){
//    var userid=$('#userId').text();
//    var btn=$(obj).attr('btn');
//    if(btn==0){
//        $('.info_do')[0].innerHTML='';
//        $.ajax({
//            url:'/rds/msg/all',
//            type:'post',
//            data:{'userid':userid,'currentPage':1,'pageSize':3},
//            dataType:'json',
//            success:function(data){
//                if(data.state==true){
//                    if(data.data.length>0){
//                        var str='';
//                        for(var i=0;i<3;i++){
//                            str+='<li onclick="go_user_message(this)">' +
//                                '<span>消息'+(i+1)+'</span>' +
//                                '<span class="info_date">'+data.data.list[i].time+'</span>' +
//                                '<span style="none">'+data.data.list[i].msgid+'</span>' +
//                                '</li>'
//                        }
//                        $('.info_do').prepend(str);
//                        $('.info_do').append('<li onclick="go_all_msg()">'+
//                            '<span style="float:right;padding-right:20px;color:#0099ff;">查看更多&gt;&gt;</span>' +
//                            '</li>');
//                    }else{
//                        $('.info_do').append('<li>'+
//                            '<span style="float:right;">暂无消息</span>' +
//                            '</li>');
//                    }
//                }else{
//                    alert(data.msg);
//                }
//            }
//        });
//        $(obj).attr('btn',1);
//    }
//}
////进入总消息页面
function go_all_msg(){
    window.location.href="/rds/view/msg?idNum="+'all';
}
//进入具体消息页面
function go_user_message(obj){
    var idNum=$(obj).find('span').eq(2).text();
    window.location.href="/rds/view/msg?idNum="+idNum;
}
////进入订单管理
//function go_dingdan(){
//    window.location.href='';
//}
////进入个人资料
//function go_userInfo(){
//    window.location.href='';
//}
//退出登录
function go_login(){
    $.ajax({
        url:logout+'/oss/account/logout',
        type:'post',
        data:{},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                window.location.href='http://product.unionbigdata.com/szy/view/login?logout='+1;//szy logout;
            }
        }
    });
}
//进入用户反馈
function go_userRes(){
    window.location.href="/oss/view/res";
}
//进入联系页面
function go_cont(){
    window.location.href="/oss/view/cont";
}


