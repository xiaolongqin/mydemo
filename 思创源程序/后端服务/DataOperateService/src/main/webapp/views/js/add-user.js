$(function(){
   getRoles();
});
function getRoles(){
    $.ajax({
        url:AJAXURL.getRoles,
        type:'post',
        data:{},
        dataType:'json',
        success: function (response) {
            var person = response.data ;
            var personList = '';
            var dropDown = $('#selectList');
            for(var i=0;i<person.length;i++){
                personList += '<option value="' + person[i].id + '">' + person[i].name + '</option>' ;
            }
            dropDown.html(personList);
        }
    });
}
function redAlert(obj){
    var obj = $(obj);
    var objcontent = $.trim(obj.val());
    if(objcontent!=''){
        obj.next().addClass('hidden');
    }
}
//function dropDownChange(){
//    var dropList = $('#J_scaleMenu').find('li');
//    var btntext = $('#J_dropdown').find('span');
//    console.log(btntext.html());
//    dropList.click(function(){
//        btntext.html($(this).html());
//    });
//}
function SaveUser(){
    var name = $('#UserName');
    var username = $.trim(name.val());
    var pass = $('#PassWord') ;
    var password = $.trim(pass.val());
    var flag = 0;
    var passNew = $('#PassWordConfirm');
    var passNewConfirm = $.trim(passNew.val());
    if(password == '' && passNewConfirm==''){
        password = '123456' ;
        passNewConfirm = '123456';
    }
    else if(!/^(\w+|[\u4E00-\u9FA5]){6,20}$/.test(password)){
        pass.next().removeClass('hidden');
        flag = 1;
        return false;
    }

    if(!/^(\w+|[\u4E00-\u9FA5]){4,15}$/.test(username)){
        name.next().removeClass('hidden');
        flag = 1;
        return false;
    }
    if( password != passNewConfirm ){
        passNew.next().removeClass('hidden');
        flag = 1;
        return false;
    }
    if(flag == 0){
        //将三个信息传过去。成功则调用函数显示成功界面
        $.ajax({
            url:AJAXURL.saveUser,
            type:'post',
            data:{
                "name":username,
                "pass":password,
                "role_id":$('#selectList').val()
            },
            dataType:'json',
            success:function(response){
                if(response.state == true){
                    SaveSuccess();
                }
                else{
                    alert(response.msg);
                }
            }
        });
    }
}
function SaveSuccess(){
    var step = $('.user-reg-step').find('dl');
    $(step[0]).removeClass('doing');
    $(step[1]).addClass('doing');
    var str ='<div class="regist-success col-md-7 text-center"><img src="../images/finish-regist.png" alt="finish-regist"/><span>已注册成功！</span></div>';
    $('#content').html(str);
}
