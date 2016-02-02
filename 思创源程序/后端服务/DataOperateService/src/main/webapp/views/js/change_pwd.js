function redAlert(obj){
    var obj = $(obj);
    var objcontent = obj.val().trim();
    if(objcontent!=''){
        obj.parent().find('span').addClass('hidden');
    }
}
function ChangeSuccess(){
    var step = $('.user-reg-step').find('dl');
    $(step[0]).removeClass('doing');
    $(step[1]).addClass('doing');
    var str ='<div class="regist-success col-md-7 text-center"><img src="../images/finish-regist.png" alt="finish-regist"/><span>修改成功！</span></div>';
    $('#content').html(str);
    setTimeout(function(){
        top.location.href="login.html";
    },2000);
}
function SavePwd(){
    var name = $('#UserName');
    var username = $.trim(name.val());
    var pass = $('#PassWord') ;
    var password = $.trim(pass.val());
    var passNew = $('#PassWordConfirm');
    var passNewConfirm = $.trim(passNew.val());
    var flag = 0;
    if(!/^(\w+|[\u4E00-\u9FA5]){6,20}$/.test(password)){
        pass.next().removeClass('hidden');
        flag = 1;
    }
    if(!/^(\w+|[\u4E00-\u9FA5]){6,20}$/.test(username)){
        name.next().removeClass('hidden');
        flag = 1;
    }

    if( password != passNewConfirm ){
        passNew.next().removeClass('hidden');
        flag = 1;
    }
    if(flag == 0){
        //将三个信息传过去。成功则调用函数显示成功界面
        $.ajax({
            url:AJAXURL.updatePwd,
            type:'post',
            data:{
                "oldPwd":username,
                "newPwd":password,
                "confirmPwd":passNewConfirm
            },
            dataType:'json',
            success:function(response){
                if(response.state){
                    ChangeSuccess();
                }
                else{
                    $('#pwdError').removeClass('hidden');
                }
            }
        });
    }
}
function goBack(){
    var _href=$(window.parent.document).find('#prev-url').text();
    $(window.parent.document).find('#iframepage').attr('src',_href);
}