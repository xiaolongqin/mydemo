$(function(){
    var uid=window.location.search.split("=")[1];
    getRoles(uid);
});
function getRoles(uid){
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
    $.ajax({
        url:AJAXURL.getAccountById,
        type:'post',
        data:{
            "uid":uid
        },
        dataType:'json',
        success: function (response) {
            if(response.state == true){
                $('#UserName').val(response.data.name);
                var list = $('#selectList').find('option');
                for(var i=0;i<list.length;i++){
                    if($(list[i]).val() == response.data.role_id){
                        $(list[i]).attr("selected",true);
                        break;
                    }
                }
            }
        }
    });
}
function SaveUser(){
    var name = $('#UserName');
    var username = $.trim(name.val());
    var pass = $('#PassWord') ;
    var password = $.trim(pass.val());
    var flag = 0;
    var uid = window.location.search.split("=")[1];
    if(!/^(\w+|[\u4E00-\u9FA5]){4,15}$/.test(username)){
        name.next().removeClass('hidden');
        flag = 1;
    }
    if(flag == 0){
        //将三个信息传过去。成功则调用函数显示成功界面
        $.ajax({
            url:AJAXURL.editAccount,
            type:'post',
            data:{
                "uid":uid,
                "name":username,
                "role_id":$('#selectList').val()
            },
            dataType:'json',
            success:function(){
                SaveSuccess();
            }
        });
    }
}
function SaveSuccess(){
    var step = $('.user-reg-step').find('dl');
    $(step[0]).removeClass('doing');
    $(step[1]).addClass('doing');
    var str ='<div class="regist-success col-md-7 text-center"><img src="../images/finish-regist.png" alt="finish-regist"/><span>已修改成功！</span></div>';
    $('#content').html(str);
}
function redAlert(obj){
    var obj = $(obj);
    var objcontent = $.trim(obj.val());
    if(objcontent!=''){
        obj.next().addClass('hidden');
    }
}