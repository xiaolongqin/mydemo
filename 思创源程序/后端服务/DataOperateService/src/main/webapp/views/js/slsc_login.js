$(function(){
	$("#J_userName,#J_userPwd").keydown(function(e){
        if(e.keyCode==13) {
            userSubmit();
        }   
    });
});
/**
 * 正则表达式
 */
var RE={
    NULL:/^\S+$/,
    EMAIL:/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
    PHONE:/^1\d{10}$/
};

/**
 *登录提交
 */
function userSubmit(){
	var userName=$.trim($("#J_userName").val()),
		userPwd=$.trim($("#J_userPwd").val());
    if(!RE.NULL.test(userName)){
		alert("请输入用户名");
		return false;
	}
 //    else if(userName.length < 2 || userName.length > 20){
	// 	alert("用户名长度不正确");
	// 	return false;
	// }
	if(!RE.NULL.test(userPwd)){
		alert("请输入登录密码");
		return false;
	}
 //    else if(userPwd.length < 6 || userPwd.length > 20){
	// 	alert("登录密码长度不正确");
	// 	return false;
	// }
    var data={"name":userName,"pass":userPwd};
    // console.log(AJAXURL.login);
    $.ajax({
        url:AJAXURL.login,
        data:data,
        type:"POST",
        dataType:"JSON",
        success:function(data){
            if(data.state){

                 window.location.href = SLSCHTTP + '/views/html/index.html';
            }else{
                alert("用户名或密码错误");
            }
        },
        error:function(){
            alert("数据请求异常，请稍后再试");
        }
    });
}
/**
 *重置
 */
function userReset(){
    $('#J_userName').val('');
    $('#J_userPwd').val('');
}