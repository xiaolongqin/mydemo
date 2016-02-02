 $(function(){
    Check_Box();
    getRole();
     changePage();
});
/*一开始获取全部数据*/
function getRole(name,pageNum){
    $.ajax({
        url:AJAXURL.searchRoles,
        type:'post',
        data:{
            'param':name,
            'pageNumber':pageNum
        },
        dataType:'json',
        success: function (response) {
            if(response.state == true){
                var person = response.data ;
                var personList = '';
                if(person.length==0){
                    personList='<tr><td colspan="3">查无数据</td></tr>' ;
                }else{
                    for(var i=0;i<person.length;i++){
                        var role_id = person[i].id;
                        var del_or_not = '';
                        $.ajax({
                            url: AJAXURL.checkRole,
                            type: 'post',
                            data: {
                                "role_id":role_id
                            },
                            dataType: 'json',
                            async: false,
                            success: function (response) {
                                if(response.state == true){
                                    del_or_not = '<a href="javascript:;" onclick="delCharacter(this);" data-toggle="modal" data-target="#myModal-del">删除</a>';
                                }
                                else{
                                    del_or_not = '<a style="color:#666;cursor:default;text-decoration: none;">删除</a>'
                                }
                            }
                        });
                        personList += '<tr roleid="' + person[i].id + '"><td>' + person[i].name + '</td>' +
                            '<td>' + person[i].descr + '</td>' +
                            '<td><a href="javascript:;" onclick="editCharacter(this);">编辑</a>' + del_or_not +
                            '</tr>' ;
                    }
                    $('#characterTable').find('tbody').attr('totalpage',person[0].total);
                    $('#totalPage').html("共"+person[0].total+"页");
                }
                $('#characterTable').find('tbody').html(personList);
                $('.page').find('span').html(pageNum);
            }
            else{
                $('#characterTable').find('tbody').html('<tr><td colspan="3">查无数据</td></tr>');
            }
        }
    });
}
function Add_Character () {
    shade();
    Reset_Info();
    $('#myModal').attr('add','1');
}
function Reset_Info() {
    var dialog = $('#myModal');
    dialog.find('img').attr('src','../images/checked-false.png');
    dialog.find('input').val('');
    dialog.find('textarea').val('');
    $('.dialog-content').find('div').attr('flag',0);
}
/*多选框*/
function Check_Box() {
    //二级
    var check = $('.dialog-check-item');
    check.attr('flag','0');
    check.click( function(){
        if($(this).attr('flag') == 0){
            $(this).find('img').attr('src','../images/checked-true.png');
            $(this).attr('flag','1');
            $(this).parent().parent().find('.checks').find('img').attr('src','../images/checked-true.png');
            $(this).parent().parent().find('.checks').attr('flag','1');
          $('.check-text').css({visibility:'hidden'});
        }
        else{
            $(this).find('img').attr('src','../images/checked-false.png');
            $(this).attr('flag','0');
            var sib = $(this).parent().find('.dialog-check-item');
            var state = 0 ;
            for(var i=0;i<sib.length;i++){
                if( sib.eq(i).attr('flag') == 1 ){
                    state = 1 ;
                }
            }
            if(state == 0){
                $(this).parent().parent().find('.checks').find('img').attr('src','../images/checked-false.png');
                $(this).parent().parent().find('.checks').attr('flag','0');
            }

        }
    } );
    //一级
    var checks = $('.checks');
    checks.attr('flag','0');
    checks.click(function() {
        if ($(this).attr('flag') == 0) {
            $(this).parent().find('img').attr('src', '../images/checked-true.png');
            $(this).parent().find('div').attr('flag','1');
            $(this).attr('flag', '1');
            $('.check-text').css({visibility: 'hidden'});
        }
        else {
            $(this).parent().find('img').attr('src', '../images/checked-false.png');
            $(this).parent().find('div').attr('flag','0');
            $(this).attr('flag', '0');
        }
    });
    //全选
    var selectall = $('.select-all');
    selectall.attr('flag','0');
    selectall.click(function() {
        if ($(this).attr('flag') == 0) {
            $(this).parent().parent().parent().find('img').attr('src', '../images/checked-true.png');
            $(this).parent().parent().parent().find('div').attr('flag','1');
            $(this).attr('flag', '1');
            $('.check-text').css({visibility:'hidden'});
        }
        else {
            $(this).parent().parent().parent().find('img').attr('src', '../images/checked-false.png');
            $(this).parent().parent().parent().find('div').attr('flag','0');
            $(this).attr('flag', '0');
        }
    });
}
/*点击保存按钮时*/
function Save_Info(){
    var dialog = $('#myModal');
    var check = $('.dialog-check-item');
    var name = $.trim(dialog.find('input').val());
    var description = $.trim(dialog.find('textarea').val());
    var checktop = $('.checks');
    var state = 0;
    var arraycheck = [];
    if( name == '' ){
        $('.input-alert-name').removeClass('hidden');
    }
    if( description == '' ){
        $('.input-alert-des').removeClass('hidden');
    }
    for(var i=0;i<checktop.length;i++){
        if(checktop.eq(i).attr('flag') == 1){
            state ++ ;
        }
    }
    if( state == 0 ){
      $('.check-text').css({visibility:'visible'});
    }
    if( name && description && state!=0 ){
        for(var i=0;i<checktop.length;i++){
        //遍历被勾选上的一级，为它var个数组
            if(checktop.eq(i).attr('flag') == 1){
                var arr = [];
                var firstone = {
                    "module_id":checktop.eq(i).find('span').attr('checkid')
                };
                arr.push(firstone);
                var checksec = $(checktop[i]).parent().find('.dialog-check-item');
                for(var j=0;j<checksec.length;j++){  //遍历其下面的二级，看谁被勾选了
                    if(checksec.eq(j).attr('flag')==1){
                        //被勾选了就var一个json放它的id
                        var idjson = {
                            "module_id":checksec.eq(j).find('span').attr('checkid')
                        };
                        //然后放到一级数组里
                        arr.push(idjson);
                    }
                }
                //一级数组构建完成，放到大数组里
                arraycheck.push(arr);
            }
        }//两层循环结束后，大数组就是发ajax的参数。
        if( dialog.attr('add') == 1 ){
            $.ajax({
                url:AJAXURL.saveCharacter,
                type:'post',
                data:{
                    "name":name,
                    "descr":description,
                    "json":JSON.stringify(arraycheck)
                },
                dataType:'json',
                success:function(response){
                    if(response.state==true){
                        $('#myModal').modal('hide');
                        closeShade();
                        getRole();
                    }
                    else{
                        $('.check-text').css({visibility:'visible'});
                        $('.check-text').html(response.msg);
                    }
                }
            });
        }
        else{
            $.ajax({
                url:AJAXURL.editCharacterRole,
                type:'post',
                data:{
                    "id":dialog.attr('editID'),
                    "name":name,
                    "descr":description,
                    "json":JSON.stringify(arraycheck)
                },
                dataType:'json',
                success:function(response){
                    if(response.state==true){
                        $('#myModal').modal('hide');
                        closeShade();
                        getRole();
                    }
                }
            });
        }
    }
}
function hidered(){
    var dialog = $('#myModal');
    var name = $.trim(dialog.find('input').val());
    var description = $.trim(dialog.find('textarea').val());
    if( name != '' ){
        $('.input-alert-name').addClass('hidden');
        $('.check-text').css({visibility:'hidden'});
        $('.check-text').html('请至少选择一项分析');
    }
    else{
        $('.input-alert-name').removeClass('hidden');
    }
    if( description != '' ){
        $('.input-alert-des').addClass('hidden');
    }
    else{
        $('.input-alert-des').removeClass('hidden');
    }
}
/*查找指定的角色*/
function findCharacter(){
    var name = $.trim($('#characterName').val());
    getRole(name,1);
//    if(name == ''){
//        alert('请输入角色名');
//    }
//    else{
//        $.ajax({
//            url:AJAXURL.searchRoles,
//            type:'post',
//            data:{
//                'param':name,
//                'pageNumber':pageNum
//            },
//            dataType:'json',
//            success: function (response) {
//                var person = response.data ;
//                var personList = '';
//                if(person.length==0){
//                    personList='<tr><td colspan="3">查无数据</td></tr>' ;
//                }else{
//                    for(var i=0;i<person.length;i++){
//                        personList += '<tr roleid="' + person[i].id + '"><td>' + person[i].name + '</td>' +
//                            '<td>' + person[i].descr + '</td>' +
//                            '<td><a href="javascript:;" onclick="editCharacter(this);">编辑</a><a href="javascript:;" onclick="delCharacter(this);">删除</a></td>' +
//                            '</tr>' ;
//                    }
//                }
//                $('#characterTable').find('tbody').html(personList);
//            }
//        });
//    }
}
//遮罩
function shade(){
    var divShade=window.parent.document.createElement('div');
    divShade.style.width='100%';
    divShade.style.height=window.parent.document.body.clientHeight+50+'px';
    divShade.style.position='absolute';
    divShade.style.top=0;
    divShade.style.left=0;
    divShade.style.zIndex=999;
    divShade.style.backgroundColor="#2C2C2C";
    divShade.style.filter="alpha(opacity=60)";
    divShade.style.opacity="0.6";
    $(divShade).addClass('shade');
    window.parent.document.body.appendChild(divShade);
}
//去掉遮罩
function closeShade(){
    $(window.parent.document.body).find('.shade').remove();
}
$('body').on('keydown',function(event){
    event = event || window.event;
    if(event.keyCode==27){
        setTimeout(closeShade,110);
    }
});
//删除角色
function delCharacter(obj){
    var roleid = $(obj).parent().parent().attr('roleid');
    var delConfirm = $('.dialog-yes');
    shade();
    delConfirm.click(function(){
        $.ajax({
            url:AJAXURL.delCharacter,
            type:'post',
            data:{
                "role_id":roleid
            },
            dataType:'json',
            success: function(response){
                if(response.state == true){
                    $('#myModal-del').modal('hide');
                    closeShade();
                    getRole();
                }
            }
        });
    });
}
function modalNo(){
    $('#myModal-del').modal('hide');
    closeShade();
    getRole();
}
//编辑角色
function editCharacter(obj){
    shade();
    var modal = $('#myModal');
    Reset_Info();
    var name = $(obj).parent().parent().find('td').eq(0).html();
    var descr = $(obj).parent().parent().find('td').eq(1).html();
    var roleid = $(obj).parent().parent().attr('roleid');
    modal.modal('show');
    modal.attr('add','0');
    modal.attr('editID',roleid);
    $('#parentID').val(name);
    $('#childID').val(descr);

    $.ajax({
        url:AJAXURL.editCharacter,
        type:'post',
        data:{
            "role_id":roleid
        },
        dataType:'json',
        success: function(response){
            if(response.state == true){
                var role = response.data;
                var idArray=[];
                if(role[0].length==0){
                    for(var i=1;i<role.length;i++){
                        for(var j=0;j<role[i].length;j++){
                            idArray.push(role[i][j].id);
                        }
                    }
                }
                else{
                    for(var i=0;i<role.length;i++){
                        for(var j=0;j<role[i].length;j++){
                            idArray.push(role[i][j].id);
                        }
                    }
                }
                var checkspan = $('.check-content').find('span');
                for(var k=0;k<checkspan.length;k++){
                    if($.inArray( parseInt( checkspan.eq(k).attr('checkid') ),idArray) !=-1 ){
                        checkspan.eq(k).prev().attr('src','../images/checked-true.png');
                        $(checkspan.eq(k)).parent().attr('flag','1');
                    }
                }
            }
        }
    });
}
 /*翻页*/
 function changePage(){
     var page = $('.page');
     var changebtn = page.find('a');
     var pagenow ;
     var name ;
     var pagejmp;
     var totalpage ;
     changebtn.eq(0).click(function(){
         totalpage =  parseInt($('#characterTable').find('tbody').attr('totalpage'));
         pagenow = parseInt($.trim(page.find('span').text()));
         name = $.trim($('#characterName').val());
         if(pagenow <= 1){
             alert("已经是第一页了");
             return false;
         }
         else{
             getRole(name,(pagenow-1));
         }
         $('.page').find('input').val('');
     });

     changebtn.eq(1).click(function(){
         totalpage =  parseInt($('#characterTable').find('tbody').attr('totalpage'));
         pagenow = parseInt($.trim(page.find('span').text()));
         name = $.trim($('#characterName').val());
         if(pagenow >= totalpage){
             alert("已到达最后一页");
             return false;
         }
         else{
             getRole(name,(pagenow+1));
         }
         $('.page').find('input').val('');
     });

     changebtn.eq(2).click(function(){
         totalpage =  parseInt($('#characterTable').find('tbody').attr('totalpage'));
         pagejmp = $.trim(page.find('input').val());
         if(pagejmp > totalpage){
             alert("最大页数是"+totalpage+"，不能超过最大页数");
             return false;
         }
         else if(pagejmp>0 && (/^\d+$/.test(pagejmp))){
             getRole(name,(pagejmp));
         }
         else{
             alert("请输入大于0的整数");
         }
     });
 }
 /**
  *回车事件
  */
 $('#characterName').on('keydown',function(event){
     var evt=event?event:(window.event?window.event:null);//兼容IE和FF
     if (evt.keyCode==13){
         evt.preventDefault();
         findCharacter();
         $('.page').find('input').val('');
     }
 });
 /**
  *跳转页数回车事件
  */
 $('.page').find('input').on('keydown',function(event){
     var page = $('.page');
     var evt=event?event:(window.event?window.event:null);//兼容IE和FF
     var pagejmp = $.trim(page.find('input').val());
     var totalpage =  parseInt($('#characterTable').find('tbody').attr('totalpage'));
     var name = $.trim($('#characterName').val());
     if (evt.keyCode==13){
         evt.preventDefault();
         if(pagejmp > totalpage){
             alert("最大页数是"+totalpage+"，不能超过最大页数");
             return false;
         }
         else if(pagejmp>0 && (/^\d+$/.test(pagejmp))){
             getRole(name,(pagejmp));
         }
         else{
             alert("请输入大于0的整数");
         }
     }
 });