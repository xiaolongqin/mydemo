Handlebars.registerHelper("addOne",function(index,options){
   return parseInt(index)+1;
});

Handlebars.registerHelper('compare',function(data1,data2,data3){
    if(data1<data2){
        return(0);
    }else{
        return(data3);
    }
});
Handlebars.registerHelper('quanxian',function(data0){
    if(data0==0){
        return('只读');
    }else if(data0==1){
        return('读写');
    }
});
var myHttp='/rds';
/*获取验证码*/
function yanzheng(timeID,picID){
    var captcha=Date.parse(new Date());
    $('#'+timeID).val(captcha);
    $('#'+picID).attr('src',myHttp+'/captcha/get?'+captcha+'&&captcha='+timeID);
}
/*判断验证码时间*/
function verifyTime(name){
    var myTime=$('#'+name).val();
    var myDate=new Date().getTime();
    return (myDate-myTime)>30000;
}
/*登出*/
function userExit(){
   
    $.ajax({
        url:myHttp+'/logout',
        type:'post',
        data:{},
        success:function(){
            window.location.href="/rds";
        }
    });
}
/**********************************************空间页面操作***********************************************************/
/*********************************************************************************************************************/
/*查询登录用户*/
$(function(){

    $.ajax({
        url:myHttp+'/loginUser',
        type:'post',
        data:{},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                $('#myName').text(data.data);
            }else if(data.state==false){
                alert('出问题啦！');
                window.location.href="/rds";
            }
        },
        error:function(msg){
            alert('出问题啦！');
            window.location.href="/rds";
        }
    });
});
/*登录进来，查询空间*/
$(function(){
   
    $.ajax({
        url:myHttp+'/space/all',
        type:'post',
        data:{'number':1,'size':5},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#spaceHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
});
/*分页操作*/
/*首页*/
function spaceFirst(){
    $.support.cors = true;
    $.ajax({
        url:myHttp+'/space/all',
        type:'post',
        data:{'number':1,'size':5},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#spaceHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*尾页*/
function spaceLast(){
    var sTotalPage=$('#sTotalPage').text();
    $.support.cors = true;
    $.ajax({
        url:myHttp+'/space/all',
        type:'post',
        data:{'number':sTotalPage,'size':5},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#spaceHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*上一页*/
function spacePrev(){
    var sPageNumber=$('#sPageNumber').text();
    if(sPageNumber>1){
        sPageNumber--;
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/space/all',
            type:'post',
            data:{'number':sPageNumber,'size':5},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#spaceHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }
}
/*下一页*/
function spaceNext(){
    var sPageNumber=$('#sPageNumber').text();
    var sTotalPage=$('#sTotalPage').text();
    if(sPageNumber<sTotalPage){
        sPageNumber++;
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/space/all',
            type:'post',
            data:{'number':sPageNumber,'size':5},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#spaceHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }
}
/*跳页*/
function spaceGo(){
    var sTotalPage=$('#sTotalPage').text();
    var sPageNum=$('#sPageNum').val();
    if(sPageNum>0&&sPageNum<=sTotalPage){
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/space/all',
            type:'post',
            data:{'number':sPageNum,'size':5},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#spaceHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }else{
        alert('请输入正确的页码！');
    }
}
/*创建项目空间*/
/*点击创建空间按钮,输入框清空，生成验证吗*/
$('#addSpace').live('click',function(){//不能用on
    $('#newSpaceName').val('');
    $('#spaceWord').val('');
    yanzheng('ca_cre_space','spacePic');
});
/*创建空间*/
function creSpace(){
    var newSpaceName=$('#newSpaceName').val();
    var myInput=$('#spaceWord').val();
    var vTime=verifyTime('ca_cre_space');
    if(vTime==1){
        yanzheng('ca_cre_space','spacePic');
        alert('验证码超时！');
        return;
    }
    var str=/^\S+$/;
    if(str.test(newSpaceName)){
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/space/add',
            type:'post',
            data:{'name':newSpaceName,'captcha':'ca_cre_space','input':myInput},
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    alert('操作成功！');
                    $('#newSpace').modal('hide');
                    window.location.reload();
                }else if(data.state==false){
                    /*验证码输入错误*/
                    yanzheng('ca_cre_space','spacePic');
                    alert(data.msg);
                }
             }
        });
    }else{
        alert('请输入空间名！')
    }
}
/*删除项目空间*/
/*点击删除项目，清空输入框，生成验证图片*/
function delSpace(obj){
    $('#delSpaceWord').val('');
    var spaceId=$(obj).prev().text();//空间Id
    $('#spaceId').val(spaceId);
    var spaceName=$(obj).parent().prev().text();//空间名
    $('#spaceName').val(spaceName);
    yanzheng('ca_del_space','delSpacePic');
}
/*删除项目空间*/
function delSpaceGo(){
    var spaceId=$('#spaceId').val();
    var myInput=$('#delSpaceWord').val();
    var vTime=verifyTime('ca_del_space');
    if(vTime==1){
        yanzheng('ca_del_space','delSpacePic');
        alert('验证码超时！');
        return;
    }
   
    $.ajax({
        url:myHttp+'/space/del',
        type:'post',
        data:{'id':spaceId,'captcha':'ca_del_space','input':myInput},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                alert('操作成功！');
                $('#delSpace').modal('hide');
                window.location.reload();//删除成功重载页面
            }else if(data.state==false){
                /*验证码输入错误*/
                yanzheng('ca_del_space','delSpacePic');
                alert(data.msg);
            }
        }
    });
}
/*修改项目空间*/
/*点击修改项目，清空输入框，生成验证图片*/
function updSpace(obj){
    $('#updSpaceName').val('');
    var spaceId=$(obj).parent().find('span').eq(0).text();//空间Id
    $('#spaceId').val(spaceId);
    var spaceName=$(obj).parent().prev().find('span').eq(0).text();//空间名
    $('#spaceName').val(spaceName);
    yanzheng('ca_upd_space','updSpacePic');
}
/*修改项目空间*/
function updSpaceGo(){
    var spaceId=$('#spaceId').val();
    var spaceName=$('#updSpaceName').val();
    var myInput=$('#updSpaceWord').val();
    var vTime=verifyTime('ca_upd_space');
    if(vTime==1){
        yanzheng('ca_upd_space','updSpacePic');
        alert('验证码超时！');
        return;
    }
   
    $.ajax({
        url:myHttp+'/space/up',
        type:'post',
        data:{'id':spaceId,'name':spaceName,'captcha':'ca_upd_space','input':myInput},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                alert('操作成功！');
                $('#updSpace').modal('hide');
                window.location.reload();//删除成功重载页面
            }else if(data.state==false){
                /*验证码输入错误*/
                yanzheng('ca_upd_space','updSpacePic');
                alert(data.msg);
            }
        }
    });
}
/*跳入相应空间，进入数据库用户界面*/
function goDBuser(obj){
    var spaceId=$(obj).parent().next().find('span').first().text();
    $('#spaceId').val(spaceId);
   
    $.ajax({
        url:myHttp+'/user/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbuserHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('.hidetips').css({display:'block'});
                $('.hidetips').eq(0).prev().css({backgroundColor:'#8ebae6',color:'white'});
                $('.hidetips').eq(0).css({backgroundColor:'#0160bf',color:'white'});
                $('.hidetips').eq(1).css({backgroundColor:'#cbcaca',color:'black'});
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/********************************************数据库用户操作***********************************************************/
/*********************************************************************************************************************/
function userReload(){
    var spaceId=$('#spaceId').val();
   
    $.ajax({
        url:myHttp+'/user/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbuserHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('.hidetips').css({display:'block'});
                $('.hidetips').eq(0).prev().css({backgroundColor:'#8ebae6',color:'white'});
                $('.hidetips').eq(0).css({backgroundColor:'#0160bf',color:'white'});
                $('.hidetips').eq(1).css({backgroundColor:'#cbcaca',color:'black'});
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*分页操作*/
/*首页*/
function userFirst(){
    var spaceId=$('#spaceId').val();
    $.support.cors = true;
    $.ajax({
        url:myHttp+'/user/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbuserHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*尾页*/
function userLast(){
    var spaceId=$('#spaceId').val();
    var uTotalPage=$('#uTotalPage').text();
    $.support.cors = true;
    $.ajax({
        url:myHttp+'/user/all',
        type:'post',
        data:{'number':uTotalPage,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbuserHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*上一页*/
function userPrev(){
    var spaceId=$('#spaceId').val();
    var uPageNumber=$('#uPageNumber').text();
    if(uPageNumber>1){
        uPageNumber--;
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/user/all',
            type:'post',
            data:{'number':uPageNumber,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#dbuserHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }
}
/*下一页*/
function userNext(){
    var spaceId=$('#spaceId').val();
    var uPageNumber=$('#uPageNumber').text();
    var uTotalPage=$('#uTotalPage').text();
    if(uPageNumber<uTotalPage){
        uPageNumber++;
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/user/all',
            type:'post',
            data:{'number':uPageNumber,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#dbuserHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }
}
/*跳页*/
function userGo(){
    var spaceId=$('#spaceId').val();
    var uPageNum=$('#uPageNum').val();
    var uTotalPage=$('#uTotalPage').text();
    if(uPageNum>0&&uPageNum<=uTotalPage){
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/user/all',
            type:'post',
            data:{'number':uPageNum,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#dbuserHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }else{
        alert('请输入正确的页码！');
    }
}
/*创建用户*/
/*点击创建用户,输入框内容清空,生成验证码*/
$('#addUser').live('click',function(){
    $('#newUserName').val('');
    $('#userDes').val('');
    $('#myPwd').val('');
    var spaceId=$('#spaceId').val();
    $('#upreName').text('s'+spaceId+'_');
    yanzheng('ca_cre_user','newUserPic');
});
/*创建用户*/
function creUser(){
    var spaceId=$('#spaceId').val();
    var newUserName=$('#newUserName').val();
    var userDes=$('#userDes').val();
    var myPwd= $('#myPwd').val();
    var myInput=$('#newUserWord').val();
    var vTime=verifyTime('ca_cre_user');
    if(vTime==1){
        yanzheng('ca_cre_user','newUserPic');
        alert('验证码超时！');
        return;
    }
    var str=/^\S+$/;
    if(str.test(newUserName)&&str.test(userDes)){
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/user/add',
            type:'post',
            data:{'space_id':spaceId,'name':'s'+spaceId+'_'+newUserName,'pass':myPwd,'desc':userDes,'captcha':'ca_cre_user','input':myInput},
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    alert('操作成功！');
                    $('#newUser').modal('hide');
                    $('.modal-backdrop').remove();
                    userReload();
                }else if(data.state==false){
                    /*验证码输入错误*/
                    yanzheng('ca_cre_user','newUserPic');
                    alert(data.msg);
                }
            }
        });
    }else{
        alert('请输入完整的内容！');
    }
}
/*重置密码*/
/*点击重置密码，输入框清空，生成验证码*/
function rePwd(obj){
    $('#newUserPwd').val('');
    var userName=$(obj).parent().prev().prev().text();
    $('#userName').val(userName);
    var userId=$(obj).parent().find('span').eq(1).text();
    $('#userId').val(userId);
    yanzheng('ca_rep_user','rePwdPic');
}
/*重置密码*/
function rePwdGo(){
    var spaceId=$('#spaceId').val();
    var password=$('#newUserPwd').val();
    var userId=$('#userId').val();
    var userName=$('#userName').val();
    var myInput=$('#rePwdWord').val();
    var vTime=verifyTime('ca_rep_user');
    if(vTime==1){
        yanzheng('ca_rep_user','rePwdPic');
        alert('验证码超时！');
        return;
    }
   
    $.ajax({
        url:myHttp+'/user/upPass',
        type:'post',
        data:{'space_id':spaceId,'name':userName,'id':userId,'pass':password,'captcha':'ca_rep_user','input':myInput},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                alert('操作成功！');
                $('#rePass').modal('hide');
                $('.modal-backdrop').remove();
                userReload();
            }else if(data.state==false){
                /*验证码输入错误*/
                yanzheng('ca_rep_user','rePwdPic');
                alert(data.msg);
            }
        }
    });
}
/*显示空间管理员密码*/
function seePass(obj){
    var myPass=$(obj).siblings().first().text();
    $(obj).css({display:'none'});
    $('#rootPass').text(myPass);
    $('#rootPass').css({display:'inline-block'});
    $('#rootPass').next().css({display:'inline-block'});
}
/*关闭空间管理员密码*/
function closePass(obj){
    $(obj).css({display:'none'});
    $(obj).prev().css({display:'none'});
    $(obj).prev().prev().css({display:'inline-block'});
}
/*删除用户*/
/*点击删除，输入框清空，生成验证码*/
function delUser(obj){
    $('#delUserWord').val('');
    var userName=$(obj).parent().prev().prev().text();
    $('#userName').val(userName);
    var userId=$(obj).parent().find('span').eq(1).text();
    $('#userId').val(userId);
    yanzheng('ca_del_user','delUserPic');
}
/*删除用户*/
function delUserGo(){
    var spaceId=$('#spaceId').val();
    var userName=$('#userName').val();
    var userId=$('#userId').val();
    var myInput=$('#delUserWord').val();
    var vTime=verifyTime('ca_del_user');
    if(vTime==1){
        yanzheng('ca_del_user','delUserPic');
        alert('验证码超时！');
        return;
    }
   
    $.ajax({
        url:myHttp+'/user/del',
        type:'post',
        data:{'space_id':spaceId,'id':userId,'name':userName,'captcha':'ca_del_user','input':myInput},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                alert('操作成功！');
                $('#delUser').modal('hide');
                $('.modal-backdrop').remove();
                userReload();
            }else if(data.state==false){
                /*验证码输入错误*/
                yanzheng('ca_del_user','delUserPic');
                alert(data.msg);
            }
        }
    });
}
/******************************************************************************/
/******************************************************************************/
/*进入数据库用户详情*/
function userDetailsGo(obj){
    var spaceId=$('#spaceId').val();
    var userPwd=$(obj).parent().find('span').eq(0).text();
    $('#userPwd').val(userPwd);//存入密码
    var userId=$(obj).parent().find('span').eq(1).text();
    $('#userId').val(userId);//存入当前用户ID
    var userDesc=$(obj).parent().prev().text();
    $('#userDesc').val(userDesc);//存入当前描述
    var userName=$(obj).parent().prev().prev().text();
    $('#userName').val(userName);//存入当前用户名
   
    $.ajax({
        url:myHttp+'/user/get',
        type:'post',
        data:{'space_id':spaceId,'id':userId,'number':1,'size':5},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#userDetailsHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('#newuDesc').val(userDesc);//在更改描述的弹出框里面存入描述内容
            }else if(data.state==false){
                alert('出问题啦！');
            }
        }
    });
}
/******************************************************************************/
/******************************************************************************/
/******************************************************************************/

/*显示数据库管理*/
$('.left-box ul li').eq(2).on('click',function(){
    var spaceId=$('#spaceId').val();
   
    $.ajax({
        url:myHttp+'/db/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbmanHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('.hidetips').css({display:'block'});
                $('.hidetips').eq(0).prev().css({backgroundColor:'#8ebae6',color:'white'});
                $('.hidetips').eq(0).css({backgroundColor:'#cbcaca',color:'black'});
                $('.hidetips').eq(1).css({backgroundColor:'#0160bf',color:'white'});
            }else if(data.state==false){
                alert('出问题啦！');
            }
        }
    });
});
/*返回项目空间*/
$('.left-box ul li').eq(0).on('click',function(){
   
    $.ajax({
        url:myHttp+'/space/all',
        type:'post',
        data:{'number':1,'size':5},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#spaceHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('.hidetips').css({display:'none'});
                $('.hidetips').eq(0).prev().css({backgroundColor:'#0160bf',color:'white'});
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
});
/********************************************数据库管理操作***********************************************************/
/*********************************************************************************************************************/
function dbReload(){
    var spaceId=$('#spaceId').val();
   
    $.ajax({
        url:myHttp+'/db/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbmanHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('.hidetips').css({display:'block'});
                $('.hidetips').eq(0).prev().css({backgroundColor:'#8ebae6',color:'white'});
                $('.hidetips').eq(0).css({backgroundColor:'#cbcaca',color:'black'});
                $('.hidetips').eq(1).css({backgroundColor:'#0160bf',color:'white'});
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*分页操作*/
/*首页*/
function dbFirst(){
    var spaceId=$('#spaceId').val();
    $.support.cors = true;
    $.ajax({
        url:myHttp+'/db/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbmanHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*尾页*/
function dbLast(){
    var spaceId=$('#spaceId').val();
    var dTotalPage=$('#dTotalPage').text();
    $.support.cors = true;
    $.ajax({
        url:myHttp+'/db/all',
        type:'post',
        data:{'number':dTotalPage,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbmanHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*上一页*/
function dbPrev(){
    var spaceId=$('#spaceId').val();
    var dPageNumber=$('#dPageNumber').text();
    if(dPageNumber>1){
        dPageNumber--;
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/db/all',
            type:'post',
            data:{'number':dPageNumber,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#dbmanHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }
}
/*下一页*/
function dbNext(){
    var spaceId=$('#spaceId').val();
    var dPageNumber=$('#dPageNumber').text();
    var dTotalPage=$('#dTotalPage').text();
    if(dPageNumber<dTotalPage){
        dPageNumber++;
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/db/all',
            type:'post',
            data:{'number':dPageNumber,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#dbmanHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }
}
/*跳页*/
function dbGo(){
    var spaceId=$('#spaceId').val();
    var dNewPage=('#dNewPage').val();
    var dTotalPage=$('#dTotalPage').text();
    if(dNewPage>0&&dNewPage<=dTotalPage){
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/db/all',
            type:'post',
            data:{'number':dNewPage,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    var source   = $("#dbmanHtml").html();
                    var template = Handlebars.compile(source);
                    $('.right-box').html(template(data.data));
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            },
            error:function(msg){
                alert('出问题啦！');
            }
        });
    }else{
        alert('请输入正确的页码！');
    }
}
/*新建数据库*/
/*点击新建数据库，输入框清空*/
$('#addDb').live('click',function(){
    $('#newDbName').val('');
    var spaceId=$('#spaceId').val();
    $('#dpreName').text('s'+spaceId+'_');
});
/*新建数据库*/
function creDb(){
    var spaceId=$('#spaceId').val();
    var newDbName=$('#newDbName').val();
    var myCharset=$('#myCharset').val();
    var str=/^\S+$/;
    if(str.test(newDbName)){
        $.support.cors = true;
        $.ajax({
            url:myHttp+'/db/add',
            type:'post',
            data:{'db_name':'s'+spaceId+'_'+newDbName,'charset':myCharset,'space_id':spaceId},
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    alert('操作成功！');
                    $('#newDB').modal('hide');
                    $('.modal-backdrop').remove();
                    dbReload();
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            }
        });
    }else{
        alert('请输入数据库名！');
    }
}
/*删除数据库*/
/*点击删除，清空输入框，生成验证码*/
function delDB(obj){
    $('#delDbWord').val('');
    var dbName=$(obj).parent().prev().prev().text();
    $('#dbName').val(dbName);//存放要删除的数据库名
    var dbId=$(obj).parent().find('span').eq(0).text();
    $('#dbId').val(dbId);//存放要删除的数据库编号
    yanzheng('ca_del_db','delDbPic');
}
/*删除数据库*/
function delDbGo(){
    var spaceId=$('#spaceId').val();
    var dbName=$('#dbName').val();
    var dbId=$('#dbId').val();
    var myInput=$('#delDbWord').val();
    var vTime=verifyTime('ca_del_db');
    if(vTime==1){
        yanzheng('ca_del_db','delDbPic');
        alert('验证码超时！');
        return;
    }
   
    $.ajax({
        url:myHttp+'/db/del',
        type:'post',
        data:{'space_id':spaceId,'db_name':dbName,'id':dbId,'captcha':'ca_del_db','input':myInput},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                alert('操作成功');
                $('#delDb').modal('hide');
                $('.modal-backdrop').remove();
                dbReload();
            }else if(data.state==false){
                /*验证码输入错误*/
                yanzheng('ca_del_db','delDbPic');
                alert(data.msg);
            }
        }
    });
}
/*显示数据库用户*/
$('.left-box ul li').eq(1).on('click',function(){
    var spaceId=$('#spaceId').val();
   
    $.ajax({
        url:myHttp+'/user/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbuserHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('.hidetips').css({display:'block'});
                $('.hidetips').eq(0).prev().css({backgroundColor:'#8ebae6',color:'white'});
                $('.hidetips').eq(0).css({backgroundColor:'#0160bf',color:'white'});
                $('.hidetips').eq(1).css({backgroundColor:'#cbcaca',color:'black'});
            }else if(data.state==false){
                alert('出问题啦！');
            }
        }
    });
});
/***********************************************数据库用户详情********************************************************/
/*********************************************************************************************************************/
/*返回数据库用户管理*/
function backDBuser(){
    var spaceId=$('#spaceId').val();
   
    $.ajax({
        url:myHttp+'/user/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbuserHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('.hidetips').css({display:'block'});
                $('.hidetips').eq(0).prev().css({backgroundColor:'#8ebae6',color:'white'});
                $('.hidetips').eq(0).css({backgroundColor:'#0160bf',color:'white'});
                $('.hidetips').eq(1).css({backgroundColor:'#cbcaca',color:'black'});
            }else if(data.state==false){
                alert('出问题啦！');
            }
        },
        error:function(msg){
            alert('出问题啦！');
        }
    });
}
/*更改描述*/
function reUserGo(){
    var newuDesc=$('#newuDesc').val();
    var spaceId=$('#spaceId').val();
    var userId=$('#userId').val();
    var userName=$('#userName').val();
    var str=/^\S+$/;
   
    if(str.test(newuDesc)){
        $.ajax({
            url:myHttp+'/user/up',
            type:'post',
            data:{'space_id':spaceId,'name':userName,'id':userId,'desc':newuDesc},
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    alert('操作成功！');
                    $('#reUserName').modal('hide');
                    $('.modal-backdrop').remove();
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            }
        });
    }
}
/*显示密码*/
function seePwd(){
    userPwd=$('#userPwd').val();
    $('#dataPwd').text(userPwd);
    $('#dataPwd').css({display:'inline-block'});
    $('#closePwd').css({display:'inline-block'});
}
/*关闭密码*/
function closePwd(){
    $('#dataPwd').css({display:'none'});
    $('#closePwd').css({display:'none'});
}
/*新加绑定数据库*/
/*点新加绑定数据库按钮，输入框清空*/
function addConDb(){
    var spaceId=$('#spaceId').val();
    var userId=$('#userId').val();
   
    $.ajax({
        url:myHttp+'/user/getUn',//查询未绑定数据库的请求
        type:'post',
        data:{'space_id':spaceId,'id':userId},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var myData=data.data;
                $('#newdconName').html('');
                for(var i=0;i<myData.length;i++){
                    $('#newdconName').append('<option value='+myData[i].id+'>'+myData[i].db_name+'</option>');
                }
            }else if(data.state==false){
                alert('出问题啦');
            }
        }
    });
};
/*新加绑定数据库*/
function newConDB(){
    var spaceId=$('#spaceId').val();
    var newdconId=$('#newdconName').val();
    var newdconName=document.getElementById('newdconName');
    for(var i=0;i<newdconName.length;i++){
        if(newdconName[i].selected==true){
            var addconName=newdconName[i].innerHTML;
        }
    }
    var newdconName=$('#newdconName').text();
    var userId=$('#userId').val();
    var userName=$('#userName').val();
    if($('#inlineRadio3')[0].checked){
        var role=$('#inlineRadio3')[0].value;
    }else if($('#inlineRadio4')[0].checked){
        var role=$('#inlineRadio4')[0].value;
    }
    var str=/^\S+$/;
   
    if(str.test(newdconName)){
        $.ajax({
            url:myHttp+'/user/addPri',
            type:'post',
            data:{'name':userName,'space_id':spaceId,'id':userId,'db_name':addconName,'db_id':newdconId,'role':role},
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    alert('操作成功！');
                    $('#conDB').modal('hide');
                    $('.modal-backdrop').remove();
                    reloadNewBindDB();
                }else if(data.state==false){
                    alert('出问题啦！');
                }
            }
        });
    }
}
/*新加绑定数据库刷新*/
function reloadNewBindDB(){
    var spaceId=$('#spaceId').val();
    var userId=$('#userId').val();
   
    $.ajax({
        url:myHttp+'/user/get',
        type:'post',
        data:{'space_id':spaceId,'id':userId,'number':1,'size':5},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#userDetailsHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
            }else if(data.state==false){
                alert('页面加载错误！');
            }
        }
    });
}
/*修改权限*/
/*点击修改权限，存入要修改的数据库的名字，id和字符集*/
function updCharset(obj){
    var oldCharset=$(obj).parent().prev().attr("data-index");
    $('#bindDBcharset').val(oldCharset);//存入字符形式
    var bindDBName=$(obj).parent().prev().prev().text();
    $('#bindDBName').val(bindDBName);//存入数据库名
    var bindDBid=$(obj).prev().text();
    $('#bindDBid').val(bindDBid);//存入数据库ID
    if(oldCharset==0){
        $('#inlineRadio5').attr('checked','checked');
    }else if(oldCharset==1){
        $('#inlineRadio6').attr('checked','checked');
    }
}
/*修改权限*/
function reCharset(){
    var userId=$('#userId').val();
    var userName=$('#userName').val();
    var spaceId=$('#spaceId').val();
    var bindDBName=$('#bindDBName').val();
    var bindDBid=$('#bindDBid').val();
    var oldCharset=$('#bindDBcharset').val();
    if(document.getElementById('inlineRadio5').checked){//确定哪个radio该被选中
        var role=0;
    }else if(document.getElementById('inlineRadio6').checked){
        var role=1;
    }
    if(oldCharset==role){//权限没变化时不提交ajax
        alert('权限无变化！');
        $('#reCanDo').modal('hide');
        $('.modal-backdrop').remove();
    }else{
       
        $.ajax({
            url:myHttp+'/user/upPri',
            type:'post',
            data:{'name':userName,'space_id':spaceId,'id':userId,'db_name':bindDBName,'db_id':bindDBid,'role':role},
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    alert('操作成功！');
                    $('#reCanDo').modal('hide');
                    $('.modal-backdrop').remove();
                    reloadNewBindDB();
                }else if(data.state==false){
                    alert('出问题啦！');
                    $('#reCanDo').modal('hide');
                    $('.modal-backdrop').remove();
                }
            }
        });
    }
}
/*解除绑定*/
/*点击解除绑定，生成验证码图片*/
function delbind(obj){
    var bindDBName=$(obj).parent().prev().prev().text();
    $('#bindDBName').val(bindDBName);//存入数据库名
    var bindDBid=$(obj).parent().find('span').eq(0).text();
    $('#bindDBid').val(bindDBid);//存入数据库ID
    yanzheng('ca_del_con','delConPic');
}
/*解除绑定*/
function delBindGo(){
    var userId=$('#userId').val();
    var userName=$('#userName').val();
    var spaceId=$('#spaceId').val();
    var bindDBName=$('#bindDBName').val();
    var bindDBid=$('#bindDBid').val();
    var myInput=$('#delConWord').val();
    var vTime=verifyTime('ca_del_con');
    if(vTime==1){
        yanzheng('ca_del_con','delConPic');
        alert('验证码超时！');
        return;
    }
   
    $.ajax({
        url:myHttp+'/user/delPri',
        type:'post',
        data:{'name':userName,'space_id':spaceId,'id':userId,'db_name':bindDBName,'db_id':bindDBid,'captcha':'ca_del_con','input':myInput},
        dataType:'json',
        success:function(data){
            if(data.state==true){
                alert('操作成功！');
                $('#delCon').modal('hide');
                $('.modal-backdrop').remove();
                reloadNewBindDB();
            }else if(data.state==false){
                /*验证码输入错误*/
                yanzheng('ca_del_con','delConPic');
                alert(data.msg);
            }
        }
    });
}
/****************************************************数据库详情*******************************************************/
/*********************************************************************************************************************/
/*进入数据库详情*/
function dbDetails(obj){
    var spaceId=$('#spaceId').val();
    var dbName=$(obj).parent().prev().prev().text();
    var jsonData={'name':dbName};
    var source   = $("#dbDetailsHtml").html();
    var template = Handlebars.compile(source);
    $('.right-box').html(template(jsonData));
}
/*返回数据库操作界面*/
function backDB(){
    var spaceId=$('#spaceId').val();
    $.ajax({
        url:myHttp+'/db/all',
        type:'post',
        data:{'number':1,'size':5,'space_id':spaceId},//传当前页数和每一页的数据条数
        dataType:'json',
        success:function(data){
            if(data.state==true){
                var source   = $("#dbmanHtml").html();
                var template = Handlebars.compile(source);
                $('.right-box').html(template(data.data));
                $('.hidetips').css({display:'block'});
                $('.hidetips').eq(0).prev().css({backgroundColor:'#8ebae6',color:'white'});
                $('.hidetips').eq(0).css({backgroundColor:'#cbcaca',color:'black'});
                $('.hidetips').eq(1).css({backgroundColor:'#0160bf',color:'white'});
            }else if(data.state==false){
                alert('出问题啦！');
            }
        }
    });
}
