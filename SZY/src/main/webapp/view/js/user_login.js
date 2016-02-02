/**
 * 数之云-用户-登录
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-11-24 15:28:40
 * @version 1.0
 */
/*var userMainH=0;*/
$(function(){
	/*userMainH=$("#J_userMain").height();
    getmarginTop();
    window.onresize=function(){
        getmarginTop();
    }*/
    $("#J_userEmail,#J_userPwd").keydown(function(e){ 
        if(e.keyCode==13) {
            userSubmit();
        }   
    });
});
/**
 *计算marginTop的值
 */
function getmarginTop(){
    var documentH=$(document).height();
    var mtTop=(documentH-userMainH-100)/2;
    $("#J_userMain").css("marginTop",mtTop+"px");
}
/**
 *登录提交
 */
function userSubmit(){
	var userEmail=$.trim($("#J_userEmail").val()),
		userPwd=$.trim($("#J_userPwd").val()),
		userSubmit=$("#J_userSubmit"),
		userError=$("#J_userError"),
        data={},
        search=location.search;
	if(!RE.NULL.test(userEmail)){
		userError.html("请输入登录邮箱");
		return false;
	}else if(!RE.EMAIL.test(userEmail)){
		userError.html("登录邮箱不正确");
		return false;
	}
	if(!RE.NULL.test(userPwd)){
		userError.html("请输入登录密码");
		return false;
	}else if(userPwd.length < 6 || userPwd.length > 20){
		userError.html("登录密码不正确");
		return false;
	}
	userSubmit.button('loading');
    data={"email":userEmail,"pwd":userPwd}
    if(search || search!="" || search!=null){
        var searchPram=getSearchAsArray(search);
        if(searchPram["url"]){
           data={"email":userEmail,"pwd":userPwd,"url":searchPram["url"]} 
        }
    }
	$.ajax({
        url:AJAXURL.login,
        data:data,
        type:"POST",
        dataType:"JSON",
        success:function(data){
            userSubmit.button('reset');
            if(data.state){
                location.href=data.data.json;
            }else{
                userError.html("用户名或密码错误");return false;
            }
        },
        error:function(){
        	userSubmit.button('reset');
        	userError.html("数据请求异常，请稍后再试");return false;
        }
    });
}
