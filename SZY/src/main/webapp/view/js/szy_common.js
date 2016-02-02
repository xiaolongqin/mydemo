/**
 * 数之云-用户-comoon
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-11-24 16:21:44
 * @version 1.0
 */
var HOST="/szy";//http://192.168.2.33:8080
var AJAXURL={
	login:HOST+"/user/login",                               //登录
	logout:HOST+"/user/logout",                              //退出

	register:HOST+"/user/register", 			            //注册
	checkEmail:HOST+"/user/checkEmail",						//检查邮箱
	checkUsername:HOST+"/user/checkUsername",               //检查用户名是否可以使用
	checkCard:HOST+"/user/checkCard",               		//检查身份证号码
	checkTel:HOST+"/user/checkTel",                         //检查手机号码
	updatePwd:HOST+"/user/updatePwd",						//修改登录密码
	getUser:HOST+"/user/getUser",							//获得基本信息
	modifyInfo:HOST+"/user/modifyInfo",						//修改基本信息
	resendMail:HOST+"/user/resendMail",                     //重新获取邮件

	getUserInfo:HOST+"/getInfo/getUserInfo",                //获得登录用户信息
	getProv:HOST+"/address/getProv",						//获得省份
	getCity:HOST+"/address/getCity",						//获得城市
	getArea:HOST+"/address/getArea",						//获得区县

	getService:HOST+"/util/getService",                     //获得服务
	toUsing:HOST+"/rds/toUsing",                             //立即使用

	getAllOrder:HOST+"/rds/getUserOrders",     				//获得所有订单
	refuseOrder:HOST+"/rds/cancel",  						//拒绝
	getAndSP:HOST+"/rds/getOrderDetail", 					//获得订单详情

	//OSS
	queryByState:HOST+"/oss/queryByState",				//获得用户OSS订单列表
	cancelByid:HOST+"/oss/cancelByid", 				//取消用户OSS订单
	getDetailsByNum:HOST+"/oss/getDetailsByNum", 		//获得用户OSS订单
	ossToUsing:HOST+"/oss/toUsing"                             //立即使用
}
$(function(){
	getUserLoginInfo();
	getAllService();
});
/**
 * 验证码
 */
function vfCode(img,chg,kind){
	this.img=img;
	this.chg=chg;
	this.kind=kind;
	this.init();
}
vfCode.prototype={
	init:function(){
		var d     =document,
			me    =this,
			vfImg =d.getElementById(me.img),
			vfChg =d.getElementById(me.chg);
		if(vfImg){
			vfImg.src=HOST+"/captcha/get?captcha="+me.kind+"&_t="+new Date().getTime();
			vfImg.setAttribute("data-kind",me.kind);
		}
		vfChg.onclick=function(){
			if(vfImg){
				vfImg.src=HOST+"/captcha/get?captcha="+me.kind+"&_t="+new Date().getTime();
			}
		}
	}
}
/**
 * 分页
 */
function PAGE(pageObj,currPage,totalPage,callback){
	this.pageObj=typeof(pageObj)=="string" ? $("#"+pageObj) : pageObj;
	this.currPage=currPage;
	this.totalPage=totalPage;
	this.callback=callback;
	this.init();
}
PAGE.prototype={
	init:function(){
		var me=this;
		me.createPageHtml(me.currPage,me.totalPage);
	},
	createPageHtml:function(currPage,totalPage){//创建分页标签HTML
		var me=this,
			html=[];
		html.push('<a data-num="prev" href="javascript:;"><上一页</a>');
	    for(var i=1;i<(totalPage+1);i++){
	        if(i==currPage){
	            html.push('<a data-num="'+i+'" class="curr" href="javascript:;">'+i+'</a>');
	        }else{
	            html.push('<a data-num="'+i+'" href="javascript:;">'+i+'</a>');
	        }
	    }
	    html.push('<a data-num="next" href="javascript:;">下一页></a>');
	    html.push('<span class="last">共<em>'+totalPage+'</em>页</span>');
	    me.pageObj.html(html.join(""));
	    me.bindPageClick(currPage,totalPage);
	},
	bindPageClick:function(currPage,totalPage){//绑定分页click
	    var me=this,
	        btns=me.pageObj.find("a"),
	        len=btns.length;
	    if(len>0){
		    for(var i=0;i<len;i++){
		        btns[i]._i=i;
		        btns[i].onclick=function(){
		            var dataNum=$(this).attr("data-num");
		            if(dataNum==currPage)return false;
		            if(dataNum=="prev"){
		                if(currPage!=1){
		                    dataNum=parseInt(currPage)-1;
		                }else{
		                    return false; 
		                }
		            }
		            if(dataNum=="next"){
		                if(currPage!=totalPage){
		                    dataNum=parseInt(currPage)+1;
		                }else{
		                    return false; 
		                }
		            }
		            if(me.callback){
		            	me.callback(dataNum);
		            }
		        }
		    }
		}
	}
}
/**
 * 获得登录用户信息
 */
function getUserLoginInfo(){
	var navUserLogin=$("#J_navUserLogin"),
		navUserRegist=$("#J_navUserRegist"),
		navUserline=$("#J_navUserline");
	if(navUserLogin[0]){
		$.ajax({
	        url:AJAXURL.getUserInfo,
	        type:"POST",
	        dataType:"JSON",
	        success:function(data){
	            if(data.state){
	                var name=data.data.name;
	                $("#J_navUserName").html(name);
	            }else{
	                navUserLogin.html('<a href="javascript:;" onclick="userLogin();return false;" title="登录">登录</a>');
	                navUserRegist.html('<a href="javascript:;" onclick="userRegist();return false;" title="注册">注册</a>');
	                navUserline.removeClass("none");
	            }
	        },
	        error:function(){
	        	navUserLogin.html('<a href="javascript:;" onclick="userLogin();return false;" title="登录">登录</a>');
	            navUserRegist.html('<a href="javascript:;" onclick="userRegist();return false;" title="注册">注册</a>');
	            navUserline.removeClass("none");
	        }
	    });
	}
}
/**
 * 用户登录
 */
function userLogin(){
	return location.href="login";
}
/**
 * 用户注册
 */
function userRegist(){
	return location.href="regist";
}
/**
 * 退出
 */
function exitLogin(){
	ALERT.init({
		info:"确定退出吗？",
		callback:function(){
			$.ajax({
		        url:AJAXURL.logout,
		        type:"POST",
		        dataType:"JSON",
		        success:function(data){
		            if(data.state){
		                location.href="login";
		            }else{
		                alert("数据异常,请稍后在试！");
		            }
		        },
		        error:function(){
		        	alert("链接异常,请稍后在试！");
		        }
		    });
		}
	});
}
/**
 * 正则表达式
 */
var RE={
	NULL:/^\S+$/,
	EMAIL:/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
	PHONE:/^1\d{10}$/
}
/*
 *功能：基于bootstrap的提示框
 *参数类型          ：对象
 *参数 info         ：提示文字信息       必须
 *参数 callback     ：确定按钮的回调函数 可选
 *参数 closeback    ：关闭按钮的回调函数 可选
 *调用实例 ALERT.init({info:"确定删除吗？",callback:aa,closeback:aa});
**/
var ALERT={
    init:function(parmObj){
        if(parmObj){
            this.appendHTML(parmObj);
        }
    },
    appendHTML:function(parmObj){
        var html=[],me=this;
        html.push('<!--对话框 S-->');
        html.push('<div class="modal fade" id="alertModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">');
            html.push('<div class="modal-dialog modal-sm">');
                html.push('<div class="modal-content">');
                    html.push('<div class="modal-header">');
                        html.push('<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>');
                        html.push('<h4 class="modal-title" id="myModalLabel">提示</h4>');
                    html.push('</div>');
                    html.push('<div class="modal-body">');
                        html.push(parmObj.info);
                    html.push('</div>');
                    html.push('<div class="modal-footer">');
                        html.push('<button type="button" class="btn btn-primary" id="J_alertBtn">确定</button>');
                        html.push('<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>');  
                    html.push('</div>');
                html.push('</div>');
            html.push('</div>');
        html.push('</div>');
        html.push('<!--对话框 E-->');
        $("body").append(html.join(""));
        var alertObj=$('#alertModal');
        if(alertObj){
            alertObj.modal("show");
        }
        $("#J_alertBtn").on("click",function(){
            if(parmObj.callback){
                parmObj.callback();    
            }else{
                alertObj.modal("hide");
            }
        });
        if(parmObj.closeback){
            alertObj.on("hidden.bs.modal",function(){
                parmObj.closeback();
            });
        }
        alertObj.on("hidden.bs.modal",function(){
            alertObj.remove();
        }); 
    }
}
/**
 * 返回密码的强度级别 
 */
function checkStrong(sValue){ 
	var modes = 0;                       
	if (/\d/.test(sValue)) modes++; //数字            
	if (/[a-z]/.test(sValue)) modes++; //小写            
	if (/[A-Z]/.test(sValue)) modes++; //大写
	return modes;
}
/**
 * 获得所有服务 
 */   
function getAllService(){
	var szyMenu=$("#J_szyMenu");
	if(szyMenu[0]){
		$.ajax({
	        url:AJAXURL.getService,
	        type:"POST",
	        dataType:"JSON",
	        success:function(json){
	            if(json.state){
	            	var data=json.data,len=data.length,
	            		html=[];
	            	if(SZYMENUID.typeid==-1){
	            		html.push('<dl class="curr">');
	            	}else{
	            		html.push('<dl>');
	            	}
						html.push('<dt class="clearfix">');
							html.push('<i class="big-icon pull-left"></i>');
							html.push('<div class="big-name pull-left"><a href="all">所有产品</a></div>');
						html.push('</dt>');
						html.push('<dd></dd>');
					html.push('</dl>');
	            	for(var i=0;i<len;i++){
	            		var kind=data[i],
	            			list=kind.list,
	            			listLen=list.length,
	            			dlClass="";
	            		if(SZYMENUID.typeid==kind.typeid){
            				dlClass+="curr";
            			}
            			if(i==len-1){
	            			dlClass+=" last";
	            		}
	            			html.push('<dl class="'+dlClass+'">');
	            			html.push('<dt class="clearfix">');
	            				html.push('<i class="big-icon pull-left"></i>');
	            				html.push('<div class="big-name pull-left">'+kind.typename+'</div>');
	            			html.push('</dt>');
	            			html.push('<dd>');
	            				html.push('<ul>');
	            					for(var j=0;j<listLen;j++){
	            						var serv=list[j],
	            							liClass="list clearfix";
	            						if(serv.serviceid==SZYMENUID.serviceid){
	            							liClass+=" curr";
	            						}
	            						if(i==len-1 && j==listLen-1){
	            							liClass+=" last";
	            						}
	            							html.push('<li class="'+liClass+'">');
		            						html.push('<i class="small-icon pull-left"></i>');
		            						html.push('<div class="small-left pull-left">');
		            							html.push('<div class="small-name"><a href="'+serv.indexurl+'">'+serv.servicename+'</a></div>');
		            							html.push('<div class="small-des">'+serv.servicedesc+'</div>');
		            						html.push('</div>');
		            					html.push('</li>');
	            					}	
	            				html.push('</ul>');
	            			html.push('</dd>');
	            		html.push('</dl>');
	            	}
	            	$("#J_szyMenu").html(html.join("")); 
	            }else{
	                
	            }
	        },
	        error:function(){}
	    });
	}
}
/**
 *获得支付状态name
 */
function getPayStateName(state){
	var name="";
	if(state==0){
		name="未付款";
	}else if(state==1){
		name="已付款";
	}
	return name;
}
/**
 *获得订单类型
 */
function getOrderType(type){
	var name="";
	if(type==0){
		name="新开服务";
	}else if(type==1){
		name="续费升级";
	}
	return name;
}
/**
 *获得订单审核状态
 */
function getOrderCheckState(state){
	var name="";
	switch(state){
		case 0 : name="未审核";break;
		case 1 : name="已拒绝";break;
		case 2 : name="已通过";break;
	}
	return name;
}
/**
 *获得空间状态
 */
function getSpaceState(state){
	var name="";
	switch(state){
		case 0 : name="已启用";break;
		case 1 : name="已过期";break;
		case 2 : name="已关闭";break;
	}
	return name;
}
/**
 *解析URL参数
 */
function getSearchAsArray(srchStr){
    var results=new Array();
    var input=srchStr.substr(1);
    if(input){
        var srchArray=input.split("&");
        var tempArray=new Array();
        for(var i=0;i<srchArray.length;i++)
        {
            tempArray=srchArray[i].split("=");
            if(tempArray.length>2){
                results[tempArray[0]]=tempArray[1]+"="+tempArray[2];
            }else{
               results[tempArray[0]]=tempArray[1]; 
            }  
        }
    }
    return results;
}