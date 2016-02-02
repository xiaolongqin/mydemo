var flag=0;
var flagRegister01 = 0;
var flagRegister02 = 0;
var flagRegister03 = 0;
var flagRegister04 = 9;
var flagRegister05 = 1;
var verifycodetime = null;
var saveinfo = 0;
var mon_sea_year = 0; //包月：1  包季：2  包年：3
var time1;

$("#rg-inputphone").keypress(function(e){
	if(e.which==13){
		checkRegInfo();
	}
})
function init(){
	$('#usernamePic').fadeOut(0);
	document.getElementById("username").focus();
	checkCookie();
	checkUserInfo();
	getClassify();
	var startTime = new Date();
	var syear = startTime.getFullYear(),smonth = parseInt(startTime.getMonth())+1,sday = startTime.getDate() ;
	var startd = syear + '-' + smonth + '-' + (sday-1);
//  上面两个图的日历
	var dateFirst1 = $('#timechoose01');
	var dateLast1 = $('#timechoose02');
	var dateFirst1Api;
	var dateLast1Api;
	dateFirst1.cxCalendar({
		startDate:startd
	},function(api){
		dateFirst1Api = api;
	});

	dateLast1.cxCalendar({
		startDate:startd
	},function(api){
		dateLast1Api = api;
	});
	dateFirst1.bind('change', function(){
		$("#pleaseChooseRight").text("");
		$("#bookmonth,#bookseason,#bookyear").css("background","url(../images/inputbkg.png)");
		$("#moneyToPay").text("");
		var firstTime = parseInt(dateFirst1Api.getDate('TIME'), 10);
		var lastTime = parseInt(dateLast1Api.getDate('TIME'), 10);
		if (lastTime < firstTime) {
			dateLast1Api.clearDate();
		}
		dateLast1Api.setOptions({
			startDate: firstTime
		});
		dateLast1Api.show();
		$("#pleaseChooseRight").text("");
	});
	/*document.onselectstart = function(){return false;}/*设置文字能被选中*/
	$("#registerbg").fadeOut(0);
	$("#paybg").fadeOut(0);
	$("#successbg").fadeOut(0);
	
}
function checkbox(){
	switch(flag){
		case 0:document.getElementById("checkbox").id = "checkedbox";
			   saveinfo=1;
			   flag=1;
			   break;
		case 1:document.getElementById("checkedbox").id = "checkbox";
			   saveinfo=0;
			   flag=0;
			   break;
	}
	
}
function sendMessage(){
	var message = $("#exampleInputEmail1").val();
	if(!checkEmail(message)){
		$("#tips").html("<span id='errorTips'>请输入正确的邮箱地址</span>");
	}
	else{
		$('#tips').html("<span id='rightTips'>邮箱验证成功,正在发送请求</span>");
		var content = $("#exampleInputEmail1").val();
		var data = {
			"email":content
		}
		$.ajax({
			url:AJAXURL.passMail,
			type:"post",
			data:data,
			dataType:"JSON",
			success:function(response){
				if(response.msg==null){
					$('#tips').text("邮件发送成功，请注意查收");
					/*$("#tips").css(color:"green");*/
				}
				else{
					$('#tips').text(response.msg);
				}
				
			}
		});
	}
}
function checkEmail(str){
    var re = /^[a-z0-9]+[\._-]?[a-z0-9]+@[a-z0-9]+-?[a-z0-9]*(\.[a-z0-9]+-?[a-z0-9]+)?\.(com|org|net|com.cn|org.cn|net.cn|cn)$/
    if(re.test(str)){
        return 1;
    }else{
        return 0;
    }
}
/*清除文本框原有提示*/
$(document).ready(function(){
	init();
	$("#exampleInputEmail1").focus(function(){
		$("#tips").html("");
		document.onselectstart=function(){return true};
		var val =$(this).val();//获取当前值
		if(val == "请输入注册邮箱"){
			$(this).val("");	
		}
	}).blur(function(){
		document.onselectstart=function(){return false};
		var val =$(this).val();//获取当前值
		if(val == ""){
		$(this).val("请输入注册邮箱");
		}
	});
})
/*注册页切换*/
$("#btnRegister").on("click",function(){
	$("#loginbg").fadeOut(0);
	$("#registerbg").fadeIn(100);
})
$("#goback").on("click",function(){
	/*用户名重置*/
	$("#rg-username").css("border","none");
	$("#rg-username").val("");
	$("#usernameTips").text("");
	/*密码重置*/
	$("#rg-inputkey").css("border","none");
	$("#rg-inputkey").val("");
	$("#inputkeyTips").text("");
	/*确认密码重置*/
	$("#rg-inputcheckkey").css("border","none");
	$("#rg-inputcheckkey").val("");
	$("#inputCheckKeyTips").text("");
	/*注册邮箱重置*/
	$("#rg-inputemail").css("border","none");
	$("#rg-inputemail").val("");
	$("#inputEmailTips").text("");
	/*单位重置*/
	$("#rg-inputunit").val("");
	/*电话号码重置*/
	$("#rg-inputphone").css("border","none");
	$("#rg-inputphone").val("");
	$("#testPhoneTips").text("");
	/*页面切换功能*/
	$("#registerbg").fadeOut(0);
	$("#loginbg").fadeIn(100);
})

function giveMeNext(){
	if(checkRegInfo() != 0){
		var verifycode1 = (new Date()).getTime();
		$("#registerbg").fadeOut(0);
		$("#paybg").fadeIn(100);
		/*$("#verifyimg").attr('src','http://192.168.0.76:8080/BusinessDataService/account/img?str='+verifycode1);*/
		$("#verifyimg").attr('src',AJAXURL.verifyCode+'?str='+verifycode1);
		verifycodetime = verifycode1;
		getClassify();
	}
}
$("#goback01").on("click",function(){
	$("#pleaseChooseRight").val("");//提示清零
	$("#inputverifycode").val("");//验证码清零
	$("#timechoose01").val("");//时间选择1清零
	$("#timechoose02").val("");//时间选择2清零
	$("#bookmonth,#bookseason,#bookyear").css("background","url(../images/inputbkg.png)");
	mon_sea_year = 0 ;
	$("#moneyToPay").text("");
	
	$("#paybg").fadeOut(0);
	$("#registerbg").fadeIn(100);
})
$("#closeCurrent").on("click",function(){
	$("#paybg").fadeOut(0);
	$("#loginbg").fadeIn(100);
})
$("#bookmonth").on("click",function(){
	$(this).css("background","#efa818");
	$("#bookseason,#bookyear").css("background","url(../images/inputbkg.png)");
	$("#moneyToPay").text("￥"+formatMoney("50.00"));
	$("#timechoose01").val("");
	$("#timechoose02").val("");
	mon_sea_year = 1 ;
	clearInterval(time1);
})
$("#bookseason").on("click",function(){
	$(this).css("background","#efa818");
	$("#bookmonth,#bookyear").css("background","url(../images/inputbkg.png)");
	$("#moneyToPay").text("￥"+formatMoney("150.00"));
		$("#timechoose01").val("");
	$("#timechoose02").val("");
	mon_sea_year = 2 ;
	clearInterval(time1);
})
$("#bookyear").on("click",function(){
	$(this).css("background","#efa818");
	$("#bookseason,#bookmonth").css("background","url(../images/inputbkg.png)");
	$("#moneyToPay").text("￥"+formatMoney("300.00"));
	$("#timechoose01").val("");
	$("#timechoose02").val("");
	mon_sea_year = 3 ;
	clearInterval(time1);
})
/*金额转换*/
function formatMoney(money){
	var dotIdx = money.lastIndexOf('.');
	var moneyInt,dotVal,temp='';
	if(dotIdx > -1){
		moneyInt = money.substring(0 , dotIdx);
		if(moneyInt.length <= 3){
			return money;
		}
		dotVal = money.substring(dotIdx , money.length)
	}else{
		if(money.length <= 3){// 不足3位直接返回
			return money;
		}
	}
	moneyInt = reverse(moneyInt);
	for(var i = 0;i < moneyInt.length;i++){
		if(i * 3 + 3 > moneyInt.length){
			temp += moneyInt.substring(i * 3 , moneyInt.length);
			break;
		}
		temp += moneyInt.substring(i * 3, i * 3 + 3) + ',';
	}
	// 最后一位不一定有有逗号，如果有那么就给删掉
	if(temp.lastIndexOf(',') == temp.length -1){
		temp = temp.substring(0 , temp.length - 1);
	}
	return reverse(temp) + dotVal;
}

/**
* 反转
**/
function reverse(num){
	if(!num) return num;
	var ary = [];
	for(var i=num.length - 1;i < num.length;i--){
		if(i < 0){
			break;
		}
		ary.push(num.charAt(i));
	}
	return ary.join('');
}

/*用户名验证*/
$("#rg-username").on("blur",function(){
	testUsername();
});
function testUsername(){
	if($("#rg-username").val()==""){
		flagRegister01=0;	
		$("#rg-username").css("border","solid 1px #b00");
		$("#usernameTips").text("用户名不能为空");
	}
	else{
		var reg = /^[a-zA-Z\d]\w{3,11}[a-zA-Z\d]$/;
		$("#usernameTips").html("<img src='../images/move2.gif' />");
		if(reg.test($("#rg-username").val())){
			
			var username = $("#rg-username").val();
			var data = {"name":username};
			$.ajax({
					url:AJAXURL.checkExist,
					type:"post",
					data:data,
					async:false,
					dataType:"JSON",
					success:function(response){
						if(response.state==true){
							$("#rg-username").css("border","none");
							flagRegister01=1;
							$("#usernameTips").text("可用");
						}
						else{
							
							flagRegister01=0;
							$("#rg-username").css("border","solid 1px #b00");
							$("#usernameTips").text("用户名已存在");
						}
					},
					error: function(msg){
						$("#usernameTips").text("用户名验重服务未开启");
					}
			});
		}
		else{
			flagRegister01=0;	
			$("#rg-username").css("border","solid 1px #b00");
			$("#usernameTips").text("请输入5-13位用户名");
		}
		
	}
}
/*ajax判断用户名是否存在*/
$("#rg-inputkey").on("blur",function(){
	testInputKey();
})
/*验证密码*/
function testInputKey(){
	flagRegister02 = 0;
	if($("#rg-inputkey").val()==""){
		$("#rg-inputkey").css("border","solid 1px #b00");
		$("#inputkeyTips").text("密码不能为空");
	}
	else{
		var reg = /^[a-zA-Z\d]\w{4,18}[a-zA-Z\d]$/;
		if($("#rg-inputcheckkey").val()!=""){
			if(reg.test($("#rg-inputkey").val())){
			if($("#rg-inputkey").val()==$("#rg-inputcheckkey").val()){
				$("#rg-inputkey").css("border","none");
				$("#rg-inputcheckkey").css("border","none");
				flagRegister02=1;
				$("#inputkeyTips").text("");
				$("#inputCheckKeyTips").text("");
			}
			else{
				$("#rg-inputkey").css("border","solid 1px #b00");
				$("#inputCheckKeyTips").text("两次密码不一致");
				flagRegister02 = 0;
			}
			}
			else{
				$("#inputkeyTips").text("请输入6-20位密码");
				flagRegister02 = 0;
				$("#rg-inputkey").css("border","solid 1px #b00");
			}
		}
		else{
			if(reg.test($("#rg-inputkey").val())){
				$("#rg-inputkey").css("border","none");
				$("#inputkeyTips").text("");
				flagRegister02=1;	
			}
			else{
				$("#rg-inputkey").css("border","solid 1px #b00");
				flagRegister02=0;
				$("#inputkeyTips").text("请输入6-20位密码");
			}
			
		}
	}
}
/*验证第二次输入密码*/
$("#rg-inputcheckkey").on("blur",function(){
	testInputCheckKey();
})
function testInputCheckKey(){
	flagRegister03 = 0;
	if($("#rg-inputcheckkey").val()==""){
		$("#rg-inputcheckkey").css("border","solid 1px #b00");
		$("#inputCheckKeyTips").text("密码不能为空");
	}
	else{
		var reg = /^[a-zA-Z\d]\w{4,18}[a-zA-Z\d]$/;
		if($("#rg-inputkey").val()!=""){
			if(reg.test($("#rg-inputcheckkey").val())){
			if($("#rg-inputcheckkey").val()==$("#rg-inputkey").val()){
				$("#rg-inputkey").css("border","none");
				$("#rg-inputcheckkey").css("border","none");
				flagRegister03=1;
				$("#inputkeyTips").text("");
				$("#inputCheckKeyTips").text("");
			}
			else{
				$("#rg-inputcheckkey").css("border","solid 1px #b00");
				$("#inputCheckKeyTips").text("两次密码不一致");
				flagRegister03 = 0;
			}
			}
			else{
				$("#inputCheckKeyTips").text("请输入6-20位密码");
				flagRegister03 = 0;
				$("#rg-inputcheckkey").css("border","solid 1px #b00");
			}
		}
		else{
			if(reg.test($("#rg-inputcheckkey").val())){
				$("#rg-inputcheckkey").css("border","none");
				$("#inputCheckKeyTips").text("");
				flagRegister03=1;	
			}
			else{
				$("#rg-inputcheckkey").css("border","solid 1px #b00");
				flagRegister03=0;
				$("#inputCheckKeyTips").text("请输入6-20位密码");
			}
			
		}
	}
}
/*验证输入邮箱*/
$("#rg-inputemail").on("blur",function(){
	var ansult = testInputEmail();
})
function testInputEmail(){
	if($("#rg-inputemail").val()==""){
		$("#rg-inputemail").css("border","solid 1px #b00");
		$("#inputEmailTips").text("邮箱不能为空");
	}
	else{
		if(checkEmail($("#rg-inputemail").val())){
			var email = $("#rg-inputemail").val();
			var data = {"email":email};
			$.ajax({
					url:AJAXURL.checkMail,
					type:"post",
					data:data,
					dataType:"JSON",
					success:function(response){
						if(response.state==true){
							$("#rg-inputemail").css("border","none");
							$("#inputEmailTips").text("可用");
						}
						else{
							$("#rg-inputemail").css("border","solid 1px #b00");
							$("#inputEmailTips").text(response.msg);
						}
					},
					error: function(response){
						$("#inputEmailTips").text("邮箱验重服务未开启");
						$("#rg-inputemail").css("border","solid 1px #b00");
					}
			});
		}
		else{
			$("#rg-inputemail").css("border","solid 1px #b00");
			$("#inputEmailTips").text("请输入正确的邮箱地址");
		}
	}
}
/*验证电话号码*/
$("#rg-inputphone").on("blur",function(){
	testInputPhone();
})
function testInputPhone(){
	var reg = /^((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$/;
	if(reg.test($("#rg-inputphone").val())){
		$.ajax({
					url:"https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="+$("#rg-inputphone").val(),
					 dataType: "jsonp",
            		 jsonp: "callback",
					success:function(response){
						if(response.mts){
							$("#testPhoneTips").text(response.carrier);
							$("#rg-inputphone").css("border","none");
							flagRegister05=1;
						}
						else{
							$("#testPhoneTips").text("该手机号码不存在");
							flagRegister05 = 0;
							$("#rg-inputphone").css("border","solid 1px #b00");
						}
					},
					error: function(msg){
						$("#testPhoneTips").text("手机号码不存在");
					}
			});
		$("#rg-inputphone").css("border","none");
		$("#testPhoneTips").text("");
		flagRegister05=1;
	}
	else if($("#rg-inputphone").val()==""){
		$("#rg-inputphone").css("border","none");
		$("#testPhoneTips").text("");
		flagRegister05=1;
	}
	else{
		$("#rg-inputphone").css("border","solid 1px #b00");
		flagRegister05 = 0;
		$("#testPhoneTips").text("请输入正确的电话号码");
	}
}
/**
 * 正则表达式
 */
var RE={
    NULL:/^\S+$/,
    EMAIL:/^[a-z0-9]+[\._-]?[a-z0-9]+@[a-z0-9]+-?[a-z0-9]*(\.[a-z0-9]+-?[a-z0-9]+)?\.(com|org|net|com.cn|org.cn|net.cn|cn)$/i,
    PHONE:/^1\d{10}$/
};
/*取消提示*/
$("#username,#password").on("blur",function(){
	$("#loginTips").text("");
})
/*用户登录*/
function userSubmit(){
	var userName = $.trim($("#username").val());
	var userPwd = $.trim($("#password").val());
	var loginFlag = 0;
	/*添加等待条*/
	
	if(userPwd==""&&userName==""){
		$("#loginTips").text("请填写用户名和密码");
		$("#username").focus();
		loginFlag=1;
	}else if(userPwd==""){
		$("#loginTips").text("请输入密码");
		$("#password").focus();
		loginFlag=1;
	}else if(userName==""){
		$("#loginTips").text("请输入用户名");
		$("#username").focus();
		loginFlag=1;
	}else{
		$("#loginTips").html("<img src='../images/move.gif'>");
		var data = {"name":userName,"pass":userPwd};
	$.ajax({
		url:AJAXURL.login,
		type:"post",
		data:data,
		dataType:"JSON",
		success:function(response){
			if(response.state){
			    setCookie('shuzl','daociyiyou',1000*60*60*24);
			    if(saveinfo==1){
			    setCookie("username",$("#username").val(),1000*60*60*24*30);/*存30天*/
			    setCookie("password",$("#password").val(),1000*60*60*24*30);
			    }
				//var uid=response.data.uid;
				window.location.href='index.html';
			}
			else{
				$("#loginTips").text(response.msg);
			}
		},
		error:function(response){
			$("#loginTips").text("网络验证失败");
		}
	});
	}
	
}
/*检查注册信息*/
function checkRegInfo(){
	testUsername();
	testInputKey();
	testInputCheckKey();
	testInputEmail();
	testInputPhone();
	var reg = /^((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$/;
	if(reg.test($("#rg-inputphone").val())){
		$("#rg-inputphone").css("border","none");
		flagRegister05=1;
	}
	else if($("#rg-inputphone").val()==""){
		$("#rg-inputphone").css("border","none");
		flagRegister05=1;
	}
	else{
		$("#rg-inputphone").css("border","solid 1px #b00");
		flagRegister05 = 0;
	}
	if((flagRegister01+flagRegister02+flagRegister03+flagRegister05)==4&&$("#inputEmailTips").text()=="可用"){
		return 1;
	}
	else{
		if(flagRegister01==0){
			$("#rg-username").css("border","solid 1px #b00");
		}
		if(flagRegister02==0){
			$("#rg-inputkey").css("border","solid 1px #b00");
		}
		if(flagRegister03==0){
			$("#rg-inputcheckkey").css("border","solid 1px #b00");
		}
		if($("#inputEmailTips").text()!="可用"){
			$("#rg-inputemail").css("border","solid 1px #b00");
		}
		if(flagRegister05==0){
			$("#rg-inputphone").css("border","solid 1px #b00");
		}
		return 0;/*控制下一步的开关*/
	}
}
var timeflag01=0;
var timeflag02=0;
var timeflag03=0;
$("#timechoose01").on("change",function(){
	if($(this).val()){
		timeflag01=1;
		mon_sea_year = 0;
	}
	else{
		timeflag01=0;
	}
})
$("#timechoose02").on("change",function(){
	if($(this).val()){
		timeflag02=1;
		mon_sea_year = 0;
	}
	else{
		timeflag02=0;
	}
})
$("#timechoose02,#timechoose01").on("change",function(){
	if(timecompare()){
		timeflag03=1;
	}
	else{
		timeflag03=0;
	}
})
/*生成10位时间戳*/
function getTimeStamp(inputtime){
	var dt = new Date(inputtime.replace(/,/,"/"));
	var timestamp13 = dt.getTime();
	var timestamp10 = Math.floor(timestamp13/1000);
	return timestamp10;
}
/*用户注册前检测控制*/
function sendRegister(){
	if( ((timeflag01+timeflag02+timeflag03)==3) || (mon_sea_year != 0)){
		saveUser();
		$("#pleaseChooseRight").text("");
	}
	else{
		$("#pleaseChooseRight").text("请选择正确的时间");
	}
}
/*注册检测子函数*/
/*检测时间选择顺序*/
function timecompare(){
	var currentime = $.trim($("#timechoose01").val());
	var endtime = $.trim($("#timechoose02").val());
	var ctime = new Date(currentime.replace(/,/,"/"));
	var etime = new Date(endtime.replace(/,/,"/"));
	if(etime>=ctime){
		return 1;
	}
}
/*用户注册*/
function saveUser(){
	var userName = $.trim($("#rg-username").val());
	var userPwd = $.trim($("#rg-inputkey").val());
	var email = $.trim($("#rg-inputemail").val());
	var department = $.trim($("#rg-inputunit").val());
	var telephone = $.trim($("#rg-inputphone").val());
	var verifycode = $.trim($("#inputverifycode").val());
	var currentime = 0;
	var endtime = 0;
	var category_id = $("#select02").find(".selected").attr("data-value");
	var category_name= $("#select02").find(".selected").text();
	var today = new Date();
	if(mon_sea_year == 0){ //若没有选中包月XXXXXXXX
		currentime = $.trim($("#timechoose01").val());
		endtime = $.trim($("#timechoose02").val());
	}
	if(mon_sea_year == 1){//包月
		var month = today.getMonth()+1;
		var MonthLater;
		var year = today.getFullYear();
		var yearLater ;
		if (month == 12){//末尾判定:加上1后若大于12，则到1月，同时年份数+1
			MonthLater = 1;
			yearLater = year +1 ;
		}
		else{
			MonthLater = month + 1 ;
			yearLater = year ;
		}
		currentime =year.toString() + '-' + month.toString() + '-' + today.getDate().toString();
		endtime = yearLater.toString() + '-' + MonthLater.toString() + '-' + today.getDate().toString();
	}
	else if(mon_sea_year == 2){//包季
		var month = today.getMonth()+1;
		var SeasonLater;
		var year = today.getFullYear();
		var yearLater ;
		if (month == 10){//末尾判定:加上3后若大于12，则分别到1、2、3月，同时年份数+1
			SeasonLater = 1;
			yearLater = year +1 ;
		}
		else if(month == 11){
			SeasonLater = 2;
			yearLater = year +1 ;
		}
		else if(month == 12){
			SeasonLater = 3;
			yearLater = year +1 ;
		}
		else{
			SeasonLater = month + 3 ;
			yearLater = year ;
		}
		currentime = year.toString() + '-' + month.toString() + '-' + today.getDate().toString();
		endtime = yearLater.toString() + '-' + SeasonLater.toString() + '-' + today.getDate().toString();
	}
	else if(mon_sea_year == 3){//包年
		currentime = today.getFullYear().toString() + '-' + (today.getMonth()+1).toString() + '-' + today.getDate().toString();
		endtime = (today.getFullYear()+1).toString() + '-' + (today.getMonth()+1).toString() + '-' + today.getDate().toString();
	}
	var ctime10 = (getTimeStamp(currentime)); //将起止时间格式化
	var etime10 = (getTimeStamp(endtime));

	var data = {
		"name":userName,
		"pass":userPwd,
		"email":email,
		"dep":department,
		"phone":telephone,
		"ctime":ctime10,
		"etime":etime10,
		"cata":category_id,
		"cata_name":category_name,
		"is_flag_apply":0,
		"inputRandomCode":verifycode,
		"str":verifycodetime
	};
	//if(checkSystemTime()){register
	$.ajax({
		url:AJAXURL.saveUser,
		type:"post",
		data:data,
		dataType:"JSON",
		success:function(response){
			if(response.state){
				var returnUID = response.data;
				setCookie('shuzl','daociyiyou',1000*60*60*24);
				var data2 = {
					"email":email,
					"uid":returnUID
				};
				$.ajax({
					url:AJAXURL.registMail,
					type:"post",
					data:data2,
					dataType:"JSON",
					success:function(response){
						if(response.state){
							setCookie('shuzl','daociyiyou',1000*60*60*24);
							$("#paybg").fadeOut(0);
							$("#successbg").fadeIn(100);
						}
						else{
							alert("内层失败");
						}
					}
				});
				/*$("#paybg").fadeOut(0);
				$("#successbg").fadeIn(100);*/
			}
			else{
				alert("注册失败:" + response.msg);
			}
		},
		error:function(response){
			alert("外层错误");
		}
	});
	
	/*setTimeout(function(){window.location.reload()},5000);//自动跳转*/
	//}
	//else{
	//	alert("开始时间小于系统时间，请重新选择");
	//}
}
/*重新获取验证码*/
$("#getVerifyAgain").on("click",function(){
	var dt = new Date().getTime();
	$("#verifyimg").attr('src',DATAHTTP+'/account/img?str='+dt);
	verifycodetime = dt;
});
/*比较系统时间*/
//function checkSystemTime(){
//	 flag=0;
//	$.ajax({
//		url:AJAXURL.getNow,
//		type:"post",
//		dataType:"JSON",
//		success:function(response){
//			var systime = response.data;
//			var currentime = $("#timechoose01").val();
//			var ctime = new Date(currentime.replace(/,/,"/"));
//			var timeformated = ctime.getTime();
//			if(systime>timeformated){
//				flag=0;
//			}
//			else{
//				flag=1;
//			}
//		},
//		error:function(errormsg){
//			alert("获取系统时间失败");
//		}
//	});
//	alert("flag:"+flag);
//	return 1;
//}
/*登录密码ENTER自动提交*/
$("#password").keypress(function(e){
	if(e.which==13){
		userSubmit();
	}
})
/*注册下一步ENTER自动提交*/
$("#rg-inputunit").keypress(function(e){
	if(e.which==13){
		giveMeNext();
	}
})
/*为日历获取系统日期*/
function getSystemDate(){
	$.ajax({
		url:AJAXURL.getNow,
		type:"post",
		dataType:"JSON",
		success:function(response){
			var systime = response.data;
			return systime;
		},
		error:function(errormsg){
			alert("获取系统日期失败");
		}
	});
}
/*绑定日历*/
/*获取分类二级列表信息*/
function getClassify(){
	$.ajax({
		url:AJAXURL.getCata,
		type:"post",
		dataType:"JSON",
		success:function(response){
			$("#select02").html("");
			var goodslength = response.data.length; //获取行业二级产品数量
			for(var i=0;i<goodslength;i++){
				$("#select02").append("<option value='"+response.data[i].category_id+"'>"+response.data[i].category_name+"</option>");	
			}
			$('#select02').selectlist({
					zIndex: 10,
					width: 350,
					height: 40,
					showMaxHeight:200,
					triangleSize: 6,   //右侧小三角大小
           			triangleColor: '#cd992f'  //右侧小三角颜色
			});
		},
		error:function(errormsg){
			
		}
	});
}
$("#backtoLogin").on("click",function(){
	window.location.reload();
})
//获取日历时间
//$('#timechoose01').on('click',function(){
//	var nowTime;
//	$.ajax({
//		url:AJAXURL.getNow,
//		type:"post",
//		dataType:"JSON",
//		success:function(response){
//			nowTime = response.data;
//			setTimeout(function(){
//				var startTime = new Date(nowTime);
//				var syear = startTime.getFullYear(),smonth = parseInt(startTime.getMonth())+1,sday = startTime.getDate() ;
//				var startd = syear + '-' + smonth + '-' + sday;
//				//  上面两个图的日历
//				var dateFirst1 = $('#timechoose01');
//				var dateLast1 = $('#timechoose02');
//				var dateFirst1Api;
//				var dateLast1Api;
//				dateFirst1.cxCalendar({
//					startDate:startd
//				},function(api){
//					dateFirst1Api = api;
//				});
//
//				dateLast1.cxCalendar({
//					startDate:startd
//				},function(api){
//					dateLast1Api = api;
//				});
//				dateFirst1.bind('change', function(){
//					var firstTime = parseInt(dateFirst1Api.getDate('TIME'), 10);
//					var lastTime = parseInt(dateLast1Api.getDate('TIME'), 10);
//					if (lastTime < firstTime) {
//						dateLast1Api.clearDate();
//					}
//					dateLast1Api.setOptions({
//						startDate: firstTime
//					});
//					dateLast1Api.show();
//				});
//			},200);
//		},
//		error:function(errormsg){
//			alert("获取系统日期失败");
//		}
//	});
//});
/*时间选择2失去焦点*/
$("#timechoose01").on("blur",function(){
	 time1 = setInterval(function(){
		if($("#timechoose02").val()!=""){
			var currentime = $.trim($("#timechoose01").val());
			var endtime = $.trim($("#timechoose02").val());
			var ctime = new Date(currentime.replace(/,/,"/"));
			var etime = new Date(endtime.replace(/,/,"/"));
			var duringtime = etime.getTime()-ctime.getTime();
			var duringdate = duringtime/86400000;
			$("#moneyToPay").text("￥"+formatMoney((duringdate*2).toString()));
			
		}
	},500);
	
	var dt = new Date($("#timechoose01").val().replace(/,/,"/"));
	
})

