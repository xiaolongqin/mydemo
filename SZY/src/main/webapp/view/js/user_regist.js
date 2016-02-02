/**
 * 数之云-用户-注册
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-11-25 11:41:26
 * @version 1.0
 */
/*校验身份证号码*/
var Errors=new Array("验证通过","身份证号码位数不对","身份证号码出生日期超出范围或含有非法字符","身份证号码校验错误","身份证地区非法");function checkIdcard(d){var e={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"xingjiang",71:"台湾",81:"香港",82:"澳门",91:"国外"};
var f,b;var c,g;var a=new Array();a=d.split("");if(e[parseInt(d.substr(0,2))]==null){return Errors[4];}switch(d.length){case 15:if((parseInt(d.substr(6,2))+1900)%4==0||((parseInt(d.substr(6,2))+1900)%100==0&&(parseInt(d.substr(6,2))+1900)%4==0)){ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;
}else{ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;
}if(ereg.test(d)){return Errors[0];}else{return Errors[2];}break;case 18:if(parseInt(d.substr(6,4))%4==0||(parseInt(d.substr(6,4))%100==0&&parseInt(d.substr(6,4))%4==0)){ereg=/^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;
}else{ereg=/^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;
}if(ereg.test(d)){c=(parseInt(a[0])+parseInt(a[10]))*7+(parseInt(a[1])+parseInt(a[11]))*9+(parseInt(a[2])+parseInt(a[12]))*10+(parseInt(a[3])+parseInt(a[13]))*5+(parseInt(a[4])+parseInt(a[14]))*8+(parseInt(a[5])+parseInt(a[15]))*4+(parseInt(a[6])+parseInt(a[16]))*2+parseInt(a[7])*1+parseInt(a[8])*6+parseInt(a[9])*3;
f=c%11;g="F";b="10X98765432";g=b.substr(f,1);if(a[17]=="x"&&g=="X"){g="x";}if(g==a[17]){return Errors[0];}else{return Errors[3];}}else{return Errors[2];
}break;default:return Errors[1];break;}}
$(function(){
	
});
var _regEmail      	   =$("#J_regEmail"),
	_regUserName       =$("#J_regUserName"),
	_regName           =$("#J_regName"),
	_regIdCard         =$("#J_regIdCard"),
	_regPhone          =$("#J_regPhone"),
	_regCompany        =$("#J_regCompany"),
	_regPwd            =$("#J_regPwd"),
	_regPwdSure        =$("#J_regPwdSure"),
	_regVerification   =$("#J_regVerification"),
	_errorEmail        =$("#J_errorEmail"),
	_errorUserName     =$("#J_errorUserName"),
	_errorName         =$("#J_errorName"),
	_errorIdCard       =$("#J_errorIdCard"),
	_errorPhone        =$("#J_errorPhone"),
	_errorCompany      =$("#J_errorCompany"),
	_errorPwd          =$("#J_errorPwd"),
	_errorPwdSure      =$("#J_errorPwdSure"),
	_errorVerification =$("#J_errorVerification");
/**
 *文本框获得/失去焦点
 */
var REGCHECK={
		EAML:{
			ABLE:true,
			FOCUS:function(){
				_errorEmail.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value),me=this;
				if(!RE.NULL.test(_value)) return false;
				if(!RE.EMAIL.test(_value)){
					_errorEmail.removeClass("none").html("邮箱格式不正确");
				}else{
					$.ajax({
						url:AJAXURL.checkEmail,
						data:{"email":_value},
						type:"POST",
						dataType:"json",
						success:function(data){
							if(!data.state){
								_errorEmail.removeClass("succ").removeClass("none").html("此邮箱已被占用");
								me.ABLE=false;
							}else{
								_errorEmail.addClass("succ").removeClass("none").html("邮箱可用");
								me.ABLE=true;		
							}
						}
					});
				}
			}
		},
		USERNAME:{
			ABLE:true,
			FOCUS:function(){
				_errorUserName.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value),me=this;
				if(!RE.NULL.test(_value)) return false;
				if(!/^(\w+|[\u4E00-\u9FA5]){5,15}$/.test(_value)){
					_errorUserName.removeClass("none").html("注册用户名由5到15个字组成");
				}else{
					$.ajax({
						url:AJAXURL.checkUsername,
						data:{"username":_value},
						type:"POST",
						dataType:"json",
						success:function(data){
							if(!data.state){
								_errorUserName.removeClass("succ").removeClass("none").html("此用户名已被占用");
								me.ABLE=false;
							}else{
								_errorUserName.addClass("succ").removeClass("none").html("用户名可用");
								me.ABLE=true;		
							}
						}
					});
				}
			}
		},
		NAME:{
			ABLE:true,
			FOCUS:function(){
				_errorName.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value),me=this;
				if(!RE.NULL.test(_value)) return false;
				if(!/^(\w+|[\u4E00-\u9FA5]){2,10}$/.test(_value)){
					_errorName.removeClass("succ").removeClass("none").html("真实姓名由2到10个字组成");
				}
			}
		},
		IDCARD:{
			ABLE:true,
			FOCUS:function(){
				_errorIdCard.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value),me=this;
				if(!RE.NULL.test(_value)) return false;
				var _checkIdcard=checkIdcard(_value);
				if(_checkIdcard!="验证通过"){
					_errorIdCard.removeClass("succ").removeClass("none").html(_checkIdcard);
					return false;
				}else{
					$.ajax({
						url:AJAXURL.checkCard,
						data:{"identitycard":_value},
						type:"POST",
						dataType:"json",
						success:function(data){
							if(!data.state){
								_errorIdCard.removeClass("succ").removeClass("none").html("此身份证号码已被注册");
								me.ABLE=false;
							}else{
								_errorIdCard.addClass("succ").removeClass("none").html("身份证号码可用");
								me.ABLE=true;		
							}
						}
					});
				}
			}
		},
		PHONE:{
			ABLE:true,
			FOCUS:function(){
				_errorPhone.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value),me=this;
				if(!RE.NULL.test(_value)) return false;
				if(!RE.PHONE.test(_value)){
					_errorPhone.removeClass("succ").removeClass("none").html("手机号码不正确");
				}else{
					$.ajax({
						url:AJAXURL.checkTel,
						data:{"tel":_value},
						type:"POST",
						dataType:"json",
						success:function(data){
							if(!data.state){
								_errorPhone.removeClass("succ").removeClass("none").html("此手机号码已被占用");
								me.ABLE=false;
							}else{
								_errorPhone.addClass("succ").removeClass("none").html("手机号码可用");
								me.ABLE=true;		
							}
						}
					});
				}
			}
		},
		COMPANY:{
			FOCUS:function(){
				_errorCompany.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)) return false;
				if(!/^([a-zA-Z0-9]+|[\u4E00-\u9FA5]){2,20}$/.test(_value)){
					_errorCompany.removeClass("succ").removeClass("none").html("公司名称不正确");
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
					_errorPwd.removeClass("succ").removeClass("none").html("密码由6到20个字符组成");
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
					regPwd=$.trim($("#J_regPwd").val());
				if(!RE.NULL.test(_value)) return false;
				if(regPwd!=_value){
					_errorPwdSure.removeClass("succ").removeClass("none").html("两次输入的密码不一致");
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
					_errorVerification.removeClass("succ").removeClass("none").html("验证码不正确");
				}
			}
		}
}
/**
 *提交注册信息
 */
function submitRegInfo(){
	var regEmail         =$.trim(_regEmail.val()),       //注册邮箱
		regUserName      =$.trim(_regUserName.val()),    //注册用户名
		regName          =$.trim(_regName.val()),        //真实姓名
		regIdCard        =$.trim(_regIdCard.val()),      //身份证号码
		regPhone         =$.trim(_regPhone.val()),       //手机号
		regCompany       =$.trim(_regCompany.val()),     //公司名称
		regPwd           =$.trim(_regPwd.val()),         //登录密码
		regPwdSure       =$.trim(_regPwdSure.val()),     //确认登录密码
		regVerification  =$.trim(_regVerification.val()),//验证码
		vfCodeKind       =$("#J_vfCodeImg").attr("data-kind");//
	if(!RE.NULL.test(regEmail)){
		_errorEmail.removeClass("succ").removeClass("none").html("请填写注册邮箱");
		return false;	
	}else if(!RE.EMAIL.test(regEmail)){
		_errorEmail.removeClass("succ").removeClass("none").html("邮箱格式不正确");
		return false;
	}
	if(!RE.NULL.test(regUserName)){
		_errorUserName.removeClass("succ").removeClass("none").html("请填写注册用户名");
		return false;
	}else if(!/^(\w+|[\u4E00-\u9FA5]){5,15}$/.test(regUserName)){
		_errorUserName.removeClass("succ").removeClass("none").html("注册用户名由5到15个字组成");
		return false;
	}
	if(!RE.NULL.test(regName)){
		_errorName.removeClass("succ").removeClass("none").html("请填写真实姓名");
		return false;
	}else if(!/^(\w+|[\u4E00-\u9FA5]){2,10}$/.test(regName)){
		_errorName.removeClass("succ").removeClass("none").html("真实姓名由2到10个字组成");
		return false;
	}
	var _checkIdcard=checkIdcard(regIdCard);
	if(!RE.NULL.test(regIdCard)){
		_errorIdCard.removeClass("succ").removeClass("none").html("请填写身份证号码");
		return false;
	}else if(_checkIdcard!="验证通过"){
		_errorIdCard.removeClass("succ").removeClass("none").html(_checkIdcard);
		return false;
	}
	if(!RE.NULL.test(regPhone)){
		_errorPhone.removeClass("succ").removeClass("none").html("请填写手机号码");
		return false;
	}else if(!RE.PHONE.test(regPhone)){
		_errorPhone.removeClass("succ").removeClass("none").html("手机号码不正确");
		return false;
	}
	if(!RE.NULL.test(regCompany)){
		_errorCompany.removeClass("succ").removeClass("none").html("请填写公司名称");
		return false;
	}else if(!/^([a-zA-Z0-9]+|[\u4E00-\u9FA5]){2,20}$/.test(regCompany)){
		_errorCompany.removeClass("succ").removeClass("none").html("公司名称不正确");
	}
	if(!RE.NULL.test(regPwd)){
		_errorPwd.removeClass("succ").removeClass("none").html("请填写登录密码");
		return false;
	}else if(!/^[A-Za-z0-9]{6,20}$/.test(regPwd)){
		_errorPwd.removeClass("succ").removeClass("none").html("登录密码由6到20个字符组成");
		return false;
	}
	if(regPwd!=regPwdSure){
		_errorPwdSure.removeClass("succ").removeClass("none").html("两次输入的密码不一致");
		return false;
	}
	if(!RE.NULL.test(regVerification)){
		_errorVerification.removeClass("succ").removeClass("none").html("请输入验证码");
		return false;
	}else if(!/^[a-zA-Z0-9]{6}$/.test(regVerification)){
		_errorVerification.removeClass("succ").removeClass("none").html("验证码不正确");
		return false;
	}
	if(!REGCHECK.EAML.ABLE && !REGCHECK.USERNAME.ABLE && !REGCHECK.NAME.ABLE && !REGCHECK.PHONE.ABLE && !REGCHECK.IDCARD.ABLE){
		return false;
	}
	$.ajax({
		url:AJAXURL.register,
		data:{"email":regEmail,"name":regUserName,"realname":regName,"regIdCard":regIdCard,"tel":regPhone,"comname":regCompany,"pwd":regPwd,"input":regVerification,"captcha":vfCodeKind},
		type:"POST",
		dataType:"json",
		success:function(data){
			if(data.state){
				location.href="regist_vf?email="+encodeURIComponent(regEmail);
			}else{
				new vfCode("J_vfCodeImg","J_vfCodeChange","register");
				if(data.msg==null){
					_errorVerification.removeClass("succ").removeClass("none").html("数据异常，请稍后再试");
				}else{
					_errorVerification.removeClass("succ").removeClass("none").html(data.msg);
				}
				
			}
		},
		error:function(){
			new vfCode("J_vfCodeImg","J_vfCodeChange","register");
			_errorVerification.removeClass("succ").removeClass("none").html("链接异常，请稍后再试");
		}
	});
}

