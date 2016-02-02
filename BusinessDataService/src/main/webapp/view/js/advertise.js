/**
 * Created by abcgr_000 on 2015/12/18.
 */
getClassify();
$("#registerbg1").fadeOut(0);
$("#paybg").fadeOut(0);
$("#successblock").fadeOut(0);

$("#rg-inputphone").keypress(function(e){
    if(e.which==13){
        giveMeNext();
    }
});
function jumpToLogin(){
    setCookie('shuzltravel','daociyiyou',5000);
    window.location.href="login.html";
}
function jumpToTry(){
    $("#logo").fadeOut(0);
    $("#registerbg1").fadeIn(100);
}
function jumpToTry1(){
    window.location.href="advertise.html#anchor-page1";
    $("#logo").fadeOut(0);
    $("#registerbg1").fadeIn(100);
}
$("#try-goback").on("click",function(){
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
    $("#registerbg1").fadeOut(0);
    $("#logo").fadeIn(100);
})
$("#goback01").on("click",function(){
    $("#paybg").fadeOut(0);
    $("#registerbg1").fadeIn(100);
})
$("#closeCurrent").on("click",function(){
    $("#paybg").fadeOut(0);
    $("#logo").fadeIn(100);
})
$('#select01').selectlist({
    zIndex: 11,
    width: 345,
    height: 40,
    showMaxHeight:250,
    triangleSize: 6,   //右侧小三角大小
    triangleColor: '#cd992f'  //右侧小三角颜色
});
$('#select02').selectlist({
    zIndex: 10,
    width: 285,
    height: 40,
    showMaxHeight:200,
    triangleSize: 6,   //右侧小三角大小
    triangleColor: '#cd992f'  //右侧小三角颜色
});
$("#timechoose01").datepicker({
    changeMonth: true,
    changeYear: true,
    dateFormat: 'yy,mm,dd',
    buttonImage:'../images/calendar.png'
});
$("#timechoose02").datepicker({
    changeMonth: true,
    changeYear: true,
    dateFormat: 'yy,mm,dd',
    buttonImage:"../images/calendar.png"
});
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
                        $("#usernameTips").text("");
                    }
                    else{
                        flagRegister01=0;
                        $("#rg-username").css("border","solid 1px #b00");
                        $("#usernameTips").text("用户名已存在");
                    }
                },
                error: function(msg){
                    $("#usernameTips").text("用户名验重未服务开启");
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
        if($("#rg-inputkey").val()!=""){
            if($("#rg-inputkey").val()==$("#rg-inputcheckkey").val()){
                $("#rg-inputcheckkey").css("border","none");
                $("#rg-inputkey").css("border","none");
                $("#inputkeyTips").text("");
                $("#inputCheckKeyTips").text("");
                flagRegister03=1;
            }
            else{
                $("#inputCheckKeyTips").text("两次密码不一致");
                $("#rg-inputcheckkey").css("border","solid 1px #b00");
                flagRegister03 = 0;
            }
        }
        else{
            $("#rg-inputcheckkey").css("border","none");
            $("#inputCheckKeyTips").text("");
            flagRegister03=1;
        }
    }
}
/*验证输入邮箱*/
$("#rg-inputemail").on("blur",function(){
    testInputEmail();
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
function checkEmail(str){
    var re = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/
    if(re.test(str)){
        return 1;
    }else{
        return 0;
    }
}
function checkRegInfo(){
    testUsername();
    testInputKey();
    testInputCheckKey();
    testInputEmail();
    testInputPhone();
    /*验证phone*/
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
    /*验证是否通过*/
    if((flagRegister01+flagRegister02+flagRegister03+flagRegister05)==4&&$("#inputEmailTips").text()=="可用"){
        return 0;
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
        return 1;/*控制下一步的开关*/
    }
}
$("#rg-inputphone").keypress(function(e){
    if(e.which==13){
        giveMeNext();
    }
})
function giveMeNext(){
    if(!checkRegInfo()){
        var verifycode1 = (new Date()).getTime();
        $("#registerbg1").fadeOut(0);
        $("#paybg").fadeIn(100);
        $("#verifyimg").attr('src',DATAHTTP+'/account/img?str='+verifycode1);
        verifycodetime = verifycode1;
        getClassify();
    }
}
/*用户注册*/
function saveUser(){
    var userName = $.trim($("#rg-username").val());
    var userPwd = $.trim($("#rg-inputkey").val());
    var email = $.trim($("#rg-inputemail").val());
    var department = $.trim($("#rg-inputunit").val());
    var telephone = $.trim($("#rg-inputphone").val());
    var category_id = $("#select02").find(".selected").attr("data-value");
    var category_name= $("#select02").find(".selected").text();
    var ctime10 = new Date().getTime();
    var etime10 = new Date().getTime();
    var verifycode = "niha";
    /*alert("1:"+userName+"2:"+userPwd+"3:"+email+"4:"+department+"5:"+telephone+"6:"+category_id+"7:"+category_name);*/
    var data = {"name":userName,
        "pass":userPwd,
        "email":email,
        "dep":department,
        "phone":telephone,
        "cata":category_id,
        "cata_name":category_name,
        "ctime":ctime10,
        "etime":etime10+864000000,
        "is_flag_apply":1
    };
    /*var data1 = {"name":userName,
     "pass":userPwd,
     "email":email,
     "dep":department,
     "phone":telephone,
     "cata":category_id,
     "cata_name":category_name,
     "ctime":ctime10,
     "etime":etime10,
     "inputRandomCode":verifycode,
     "str":new Date().getTime()
     };*/
    //if(checkSystemTime()){
    $.ajax({
        url:AJAXURL.addTestAccount,
        type:"post",
        data:data,
        dataType:"JSON",
        success:function(response){
            if(response.state){
                setCookie('shuzl','daociyiyou',1000*60*60*24);
                $("#paybg").fadeOut(0);
                $("#successblock").fadeIn(100);
            }
            else{
                alert("注册失败");
            }
        },
        error:function(response){
            $("#showMeTips").text("后台服务未开启");
        }
    });
}
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
                width: 285,
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
/*注册下一步ENTER自动提交*/
$("#rg-inputunit").keypress(function(e){
    if(e.which==13){
        giveMeNext();
    }
});
/*第一个页面的箭头动画*/
var timerArrow = setInterval(function(){
    $('#nextPageBtn').animate({bottom:"20px",opacity:1},{
        duration: 800,
        easing: "swing",
        complete:function(){
            $('#nextPageBtn').css({
                bottom:"40px",
                opacity:0.3
            });
        }
    });
},800);
/*点击箭头打开新世界的大门*/
var pageBtnFlag = 0 ;
$('#nextPageBtn').on('click',function(){
//			if(pageBtnFlag == 0){
//				pageBtnFlag = 1 ;
//				$('#content').append('<div id="advertise1" class="section section2"><h1>市场透视</h1><img src="../images/node1.png" id="img1" /><img src="../images/node2.png" id="img2" /> <img src="../images/node3.png" id="img3" /> <img src="../images/node4.png" id="img4" /><br /> <h3>追踪市场动态</h3> <h3>掌握市场喜好</h3> <h3>透视线上市场</h3> <h3>占据市场先机</h3> </div> <div id="advertise2" class="section section3"> <h1>全渠道展示</h1> <h3>锁定线上关注渠道，展示关注渠道实时动态，辅助渠道决策</h3> <img src="../images/advertise2.png"/> </div> <div id="advertise3" class="section"> <h1>品牌全维</h1> <img src="../images/brand1.png" id="brand1" alt="" /> <img src="../images/brand2.png" id="brand2" alt="" /> <img src="../images/brand3.png" id="brand3" alt="" /> <img src="../images/brand4.png" id="brand4" alt="" /> <h3>诊断全面情况</h3> <h3>分析市场能力</h3> <h3>获取全维画像</h3> <h3>指导线上竞争</h3> </div> <div id="advertise4" class="section section4"> <h1>竞争分析</h1> <h3>多维度竞品追踪，实时获取竞品市场表现，对比分析自有品牌，先发制人，赢得竞争优势</h3> <img src="../images/compete.png"/> </div> <div class="section section5"> <div id="decoration2"> <img src="../images/decoration2.png"/> </div> <div id="advertise5"> <h1>我们只为您提供最专业的数据服务</h1> <button id="btnuse2" onclick="jumpToLogin();">立即使用</button> <button id="btndemo2" onclick="jumpToTry1();">申请试用</button> </div> <div id="bottom"> <ul id="bottomcontent"> <li class="list1"><a href="#">公司主页</a></li> <li class="list1"><a href="#">新浪微博</a></li> <li class="list1"><a href="#">联系我们</a></li> <li class="imglist"><img src="../images/smallbluelogo.png"></li> <li class="list1"><a href="#">功能文档</a></li> <li class="list1"><a href="#">服务条款</a></li> <li class="list1"><a href="#">隐私政策</a></li> </ul> <h4>备案号：蜀ICP备13021642号-4 Copyright ©2015 成都数之联科技有限公司 版权所有</h4> </div> </div>');
//				$('#content').fullpage({
//					anchors: ['anchor-page1', 'anchor-page2', 'anchor-page3', 'anchor-page4', 'anchor-page5','anchor-page6'],
//					menu: '#menu',
//					afterLoad: function(anchorLink, index){
//						if(index == 2){
//							for(var i=1;i<=4;i++){
//								$("#img"+i).attr('src','../images/node'+i+'.gif');
//							}
//						}
//						if(index == 3){
//							$("#advertise2 img").attr('src','../images/advertise2.gif');
//						}
//						if(index == 4){
//							for(var i=1;i<=4;i++){
//								$("#brand"+i).attr('src','../images/brand'+i+'.gif');
//							}
//						}
//						if(index == 5){
//							$("#advertise4 img").attr('src','../images/compete.gif');
//						}
//					}
//				});
//			}
    window.location.href = window.location.href.split('#')[0] + '#anchor-page2';
});