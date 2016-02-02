/**
 * 数之云-用户-修改登录密码
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-12-03 11:41:26
 * @version 1.0
 */
var _upOldPwd      	   =$("#J_upOldPwd"),
	_upPwd             =$("#J_upPwd"),
	_upPwdSure         =$("#J_upPwdSure"),
	_upVerification    =$("#J_upVerification"),
	_errorOldPwd       =$("#J_errorOldPwd"),
	_errorPwd          =$("#J_errorPwd"),
	_errorPwdSure      =$("#J_errorPwdSure"),
	_errorVerification =$("#J_errorVerification");

$(function(){
	getUserInfo();
	new vfCode("J_vfCodeImg","J_vfCodeChange","updatepwd");
});
/**
 *注册信息验证
 */
var REGCHECK={
		OLDPWD:{
			FOCUS:function(){
				_errorOldPwd.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)){
					return false;
				}
				if(!/^[A-Za-z0-9]{6,20}$/.test(_value)){
					_errorOldPwd.removeClass("none").html("密码由6到20个字符组成");
				}
			}
		},
		PWD:{
			FOCUS:function(){
				_errorPwd.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)){
					$("#J_userPwdLen").addClass("none");
					$("#J_userPwdLay").css({"width":"0px"});
					return false;
				}
				if(!/^[A-Za-z0-9]{6,20}$/.test(_value)){
					_errorPwd.removeClass("none").html("密码由6到20个字符组成");
				}
			},
			KEYUP:function(value){
				var _value=$.trim(value),
					pwdLen=_value.length,
					pwdStr=null;
				if(!RE.NULL.test(_value)) return false;
				if(pwdLen > 5){
					$("#J_userPwdLen").removeClass("none");
					pwdStr=checkStrong(_value);
					$("#J_userPwdLay").animate({"width":100*pwdStr+"px"},"slow");
				}else{
					$("#J_userPwdLen").addClass("none");
					$("#J_userPwdLay").css({"width":"0px"});
				}
			}
		},
		PWDSURE:{
			FOCUS:function(){
				_errorPwdSure.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value),
					upPwd=$.trim(_upPwd.val());
				if(!RE.NULL.test(_value)) return false;
				if(upPwd!=_value){
					_errorPwdSure.removeClass("none").html("两次输入的密码不一致");
				}
			}
		},
		VF:{
			FOCUS:function(){
				_errorVerification.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)) return false;
				if(!/^[a-zA-Z0-9]{6}$/.test(_value)){
					_errorVerification.removeClass("none").html("验证码不正确");
				}
			}
		}
}
/**
 *提交注册信息
 */
function submitUpdatePwd(){
	var upOldPwd   	   =$.trim(_upOldPwd.val()),      //登录密码
		upPwd          =$.trim(_upPwd.val()),         //登录密码
		upPwdSure      =$.trim(_upPwdSure.val()),     //确认登录密码
		upVerification =$.trim(_upVerification.val()),//验证码
		errorUpPwd     =$("#J_errorUpPwd");//
	if(!RE.NULL.test(upOldPwd)){
		_errorOldPwd.removeClass("none").html("请填写旧密码");
		return false;
	}else if(!/^[A-Za-z0-9]{6,20}$/.test(upOldPwd)){
		_errorOldPwd.removeClass("none").html("登录密码由6到20个字符组成");
		return false;
	}
	if(!RE.NULL.test(upPwd)){
		_errorPwd.removeClass("none").html("请填写新密码");
		return false;
	}else if(!/^[A-Za-z0-9]{6,20}$/.test(upPwd)){
		_errorPwd.removeClass("none").html("登录密码由6到20个字符组成");
		return false;
	}
	if(upPwd!=upPwdSure){
		_errorPwdSure.removeClass("none").html("两次输入的密码不一致");
		return false;
	}
	if(!RE.NULL.test(upVerification)){
		_errorVerification.removeClass("none").html("请输入验证码");
		return false;
	}else if(!/^[a-zA-Z0-9]{6}$/.test(upVerification)){
		_errorVerification.removeClass("none").html("验证码不正确");
		return false;
	}
	$.ajax({
		url:AJAXURL.updatePwd,
		data:{"newpwd":upPwd,"input":upVerification,"captcha":"updatepwd"},
		type:"POST",
		dataType:"JSON",
		success:function(data){
			if(data.state){
				location.href="updatepwdrlt";
			}else{
				if(data.msg==null){
					errorUpPwd.removeClass("none").html("数据异常，请稍后再试");
					new vfCode("J_vfCodeImg","J_vfCodeChange","updatepwd");
				}else{
					errorUpPwd.removeClass("none").html(data.msg);
					new vfCode("J_vfCodeImg","J_vfCodeChange","updatepwd");
				}
				
			}	
		},
		error:function(){
			errorUpPwd.removeClass("none").html("链接异常，请稍后再试");
		}
	});
}
/**
 * 获得登录用户信息
 */
function getUserInfo(){
	var upPwdEmail=$("#J_upPwdEmail"),
		upPwdUserName=$("#J_upPwdUserName");
	$.ajax({
        url:AJAXURL.getUserInfo,
        type:"POST",
        dataType:"JSON",
        success:function(data){
        	var user=data.data;
            if(data.state){
                upPwdEmail.html(user.email);
                upPwdUserName.html(user.name);
            }
        },
        error:function(){
        	
        }
    });
}

