/**
 * 数之云-用户-基本信息
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-12-05 10:01:20
 * @version 1.0
 */
/**
 *入口
 */
$(function(){
	loadUserInfo();
});
var infoUserId     	   =$("#J_infoUserId"),
	infoEmail          =$("#J_infoEmail"),
	infoUserName       =$("#J_infoUserName"),
	infoTime           =$("#J_infoTime"),
	infoPhone          =$("#J_infoPhone"),
	infoName           =$("#J_infoName"),
	infoCompany        =$("#J_infoCompany"),
	infoCategory       =$("#J_infoCategory"),
	infoBusiness       =$("#J_infoBusiness"),
	infoWww            =$("#J_infoWww"),
	infoFixedPh        =$("#J_infoFixedPh"),
	infoPro            =$("#J_infoPro"),
	infoCity           =$("#J_infoCity"),
	infoArea           =$("#J_infoArea"),
	infoStreet         =$("#J_infoStreet"),
	infoNoticePhone    =$("#J_infoNoticePhone"),
	infoNoticeName     =$("#J_infoNoticeName"),
	infoNoticeCompany  =$("#J_infoNoticeCompany"),
	infoNoticeCategory =$("#J_infoNoticeCategory"),
	infoNoticeBusiness =$("#J_infoNoticeBusiness"),
	infoNoticeWww      =$("#J_infoNoticeWww"),
	infoNoticeFixedPh  =$("#J_infoNoticeFixedPh"),
	infoNoticeAddess   =$("#J_infoNoticeAddess");
/**
 *文本框获得/失去焦点
 */
var INFO={
		PHONE:{
			FOCUS:function(){
				infoNoticePhone.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)){
					infoNoticePhone.removeClass("none").html("*请填写手机号码");
					return false;
				}
				if(!RE.PHONE.test(_value)){
					infoNoticePhone.removeClass("none").html("*手机号码格式不正确");
				}
			}
		},
		NAME:{
			FOCUS:function(){
				infoNoticeName.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)){
					infoNoticeName.removeClass("none").html("*请填写真实姓名");
					return false;
				}
				if(!/^(\w+|[\u4E00-\u9FA5]){2,10}$/.test(_value)){
					infoNoticeName.removeClass("none").html("*真实姓名由2到10个字组成");
				}
			}
		},
		COMPANY:{
			FOCUS:function(){
				infoNoticeCompany.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)){
					infoNoticeCompany.removeClass("none").html("*请填写公司名称");
					return false;
				}
				if(!/^([\-]*|\w+|[\u4E00-\u9FA5]){2,20}$/.test(_value)){
					infoNoticeCompany.removeClass("none").html("*公司名称由2到20个字组成");
				}
			}
		},
		BUSINESS:{
			FOCUS:function(){
				infoNoticeBusiness.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)) return false;
				if(!/^([\-]*|\w+|[\u4E00-\u9FA5]){2,20}$/.test(_value)){
					infoNoticeBusiness.removeClass("none").html("*主营业务由2到20个字组成");
				}
			}
		},
		WWW:{
			FOCUS:function(){
				infoNoticeWww.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)) return false;
				if(!/^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/.test(_value)){
					infoNoticeWww.removeClass("none").html("*网站地址格式不正确");
				}
			}
		},
		FIXEDPH:{
			FOCUS:function(){
				infoNoticeFixedPh.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)) return false;
				if(!/^(\d{4}-|\d{3}-)?(\d{8}|\d{7})$/.test(_value)){
					infoNoticeFixedPh.removeClass("none").html("*固定电话格式不正确");
				}
			}
		},
		STREET:{
			FOCUS:function(){
				infoNoticeAddess.addClass("none");
			},
			BLUR:function(value){
				var _value=$.trim(value);
				if(!RE.NULL.test(_value)) return false;
				if(!/^([\-]*|\w+|[\u4E00-\u9FA5]){2,40}$/.test(_value)){
					infoNoticeAddess.removeClass("none").html("*街道地址格式不正确");
				}
			}
		}
}
/**
 *保存信息
 */
function saveInfo(){
	var _infoPhone      	=$.trim(infoPhone.val()),
		_infoName           =$.trim(infoName.val()),
		_infoCompany        =$.trim(infoCompany.val()),
		_infoCategory       =$.trim(infoCategory.val()),
		_infoBusiness       =$.trim(infoBusiness.val()),
		_infoWww            =$.trim(infoWww.val()),
		_infoFixedPh        =$.trim(infoFixedPh.val()),
		_infoPro            =$.trim(infoPro.val()),
		_infoCity           =$.trim(infoCity.val()),
		_infoArea           =$.trim(infoArea.val()),
		_infoStreet         =$.trim(infoStreet.val());
	if(!RE.NULL.test(_infoPhone)){
		infoNoticePhone.removeClass("none").html("*请输入手机号码");
		return false;
	}else if(!RE.PHONE.test(_infoPhone)){
		infoNoticePhone.removeClass("none").html("*手机号码格式不正确");
		return false;
	}
	if(!RE.NULL.test(_infoName)){
		infoNoticeName.removeClass("none").html("*请输入真实姓名");
		return false;
	}else if(!/^(\w+|[\u4E00-\u9FA5]){2,10}$/.test(_infoName)){
		infoNoticeName.removeClass("none").html("*真实姓名由2到10个字组成");
		return false;
	}
	if(!RE.NULL.test(_infoCompany)){
		infoNoticeCompany.removeClass("none").html("*请输入公司名称");
		return false;
	}else if(!/^([\-]*|\w+|[\u4E00-\u9FA5]){2,20}$/.test(_infoCompany)){
		infoNoticeCompany.removeClass("none").html("*公司名称由2到20个字组成");
		return false;
	}
	if(!/^([\-]*|\w+|[\u4E00-\u9FA5]){2,20}$/.test(_infoBusiness) && RE.NULL.test(_infoBusiness)){
		infoNoticeBusiness.removeClass("none").html("*主营业务由2到20个字组成");
		return false;
	}
	if(!/^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/.test(_infoWww) && RE.NULL.test(_infoWww)){
		infoNoticeWww.removeClass("none").html("*网站地址格式不正确");
		return false;
	}
	if(!/^(\d{4}-|\d{3}-)?(\d{8}|\d{7})$/.test(_infoFixedPh) && RE.NULL.test(_infoFixedPh)){
		infoNoticeFixedPh.removeClass("none").html("*固定电话格式不正确");
		return false;
	}
	if(!/^([\-]*|\w+|[\u4E00-\u9FA5]){2,40}$/.test(_infoStreet) && RE.NULL.test(_infoStreet)){
		infoNoticeAddess.removeClass("none").html("*街道地址格式不正确");
		return false;
	}
	var prov=infoPro.find("option[value="+_infoPro+"]").html(),
		city=infoCity.find("option[value="+_infoCity+"]").html(),
		area=infoArea.find("option[value="+_infoArea+"]").html(); 
	$.ajax({
		url:AJAXURL.modifyInfo,
		data:{"tel":_infoPhone,"realname":_infoName,"comname":_infoCompany,"classofindustry":_infoCategory,"primarybusiness":_infoBusiness,"websiteurl":_infoWww,"fixedline":_infoFixedPh,"prov":prov,"city":city,"area":area,"street":_infoStreet},
		type:"POST",
		dataType:"JSON",
		success:function(data){
			if(data.state){
				alert("信息已保存");
				location.reload();
			}
		}
	});
}
/**
 *加载用户信息
 */
function loadUserInfo(){
	$.ajax({
		url:AJAXURL.getUser,
		type:"POST",
		dataType:"JSON",
		success:function(data){
			if(data.state){
				var user=data.data;
				$("#J_infoEmail").html(user.email);
				$("#J_infoUserName").html(user.name);
				$("#J_infoTime").html(user.time);
				$("#J_infoPhone").val(user.tel);
				$("#J_infoName").val(user.realname);
				$("#J_infoCompany").val(user.comname);
				$("#J_infoCategory").val(user.classofindustry);
				$("#J_infoBusiness").val(user.primarybusiness);
				$("#J_infoWww").val(user.websiteurl);
				$("#J_infoFixedPh").val(user.fixedline);
				var address=user.address;
				if(address!=null){
					var adds=address.split("-");
					loadProv(adds);
					$("#J_infoStreet").val(adds[3]);
				}else{
					loadProv();
				}
			}
		}
	});
}
/**
 *加载省份
 */
function loadProv(adds){
	$.ajax({
		url:AJAXURL.getProv,
		type:"POST",
		dataType:"JSON",
		success:function(data){
			if(data.state){
				var prov=data.data,provLen=prov.length;
				var html=[];
				html.push('<option value="">--省份--</option>');
				for(var i = 0; i < provLen; i++) {
					if(adds && adds[0]==prov[i].name){
						html.push('<option selected="selected" value="'+prov[i].code+'">'+prov[i].name+'</option>');
						changeProv(adds,prov[i].code);
					}else{
						html.push('<option value="'+prov[i].code+'">'+prov[i].name+'</option>');
					}
				}
				$("#J_infoPro").html(html.join(""));
			}
		}
	});
}
/**
 *省份改变
 */
function changeProv(adds,_prov){
	var provId=null;
	if(_prov){
		provId=_prov;
	}else{
		provId=$("#J_infoPro").val();
	}
	$.ajax({
		url:AJAXURL.getCity,
		type:"POST",
		data:{"code":provId},
		dataType:"JSON",
		success:function(data){
			if(data.state){
				var city=data.data,cityLen=city.length;
				var html=[];
				html.push('<option value="">--市/州--</option>');
				for(var i = 0; i < cityLen; i++) {
					if(adds && adds[1]==city[i].name){
						html.push('<option selected="selected" value="'+city[i].code+'">'+city[i].name+'</option>');
						changeCity(adds,city[i].code)
					}else{
						html.push('<option value="'+city[i].code+'">'+city[i].name+'</option>');
					}
				}
				$("#J_infoCity").html(html.join(""));
			}
		}
	});
}
/**
 *城市改变改变
 */
function changeCity(adds,_city){
	var cityId=null;
	if(_city){
		cityId=_city;
	}else{
		cityId=$("#J_infoCity").val();
	}
	$.ajax({
		url:AJAXURL.getArea,
		type:"POST",
		data:{"code":cityId},
		dataType:"JSON",
		success:function(data){
			if(data.state){
				var area=data.data,areaLen=area.length;
				var html=[];
				html.push('<option value="">--区/县--</option>');
				for(var i = 0; i < areaLen; i++) {
					if(adds && adds[2]==area[i].name){
						html.push('<option selected="selected" value="'+area[i].code+'">'+area[i].name+'</option>');
					}else{
						html.push('<option value="'+area[i].code+'">'+area[i].name+'</option>');
					}
				}
				$("#J_infoArea").html(html.join(""));
			}
		}
	});
}
