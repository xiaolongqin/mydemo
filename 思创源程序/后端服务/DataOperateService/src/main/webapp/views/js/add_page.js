var pageid,url,descrip,version,guishu,page_state;
var add_or_edit = 1; //标记是添加还是修改。添加为1，修改为0 。
function Next(){
    var input = $('.add-page-info').find('.form-control');
    var alert = $('.red-text-small');
    pageid = input[0].value;
    url = input[1].value;
    descrip = input[2].value;
    version = input[3].value;
    guishu = $('#selectList').val();
    page_state=$('input:radio[name="inlineRadioOptions"]:checked').val();
    for(var i=0;i<4;i++){
        if( input[i].value == '' ){
            alert.eq(i).removeClass('hidden');
        }
    }
    input.change(function () {
        $(this).parent().next().addClass('hidden');
    });
    //下一步 传值val=1生效 0作废
    if( pageid!='' && url!='' && descrip!='' && version!='' && add_or_edit == 1 ){
        $.ajax({
            url:AJAXURL.checkPageId,
            type:'post',
            data:{
                "page_id":pageid
            },
            dataType:'json',
            success:function(response){
                if(response.state == true){
                    var step = $('.user-reg-step').find('dl');
                    $(step[0]).removeClass('doing');
                    $(step[1]).addClass('doing');
                    var str ='<div class="add-btn-page">' +
                        '<div class="clearfix">' +
                            '<label class="col-md-1 control-label">页面ID</label>' +
                            '<div class="col-md-3" style="padding: 0">' +
                            '<input type="text" class="form-control input-sm" placeholder="示例：100031" value=" ' + pageid + ' " disabled>' +
                            '</div>' +
                            '<a onclick="addBtn();">' +
                            '<div class="pull-left add-btn text-center add-btn-btn">' +
                            '<img src="../images/add.png" alt="add"/><span>添加</span>' +
                            '</div>' +
                            '</a>' +
                            '</div>' +
                        '</div>' +
                        '<div class="add-btn-content" id="addBtnContent">' +
                            '<div class="add-btn-content-1">' +
                                '<div class="clearfix">' +
                                    '<label class="col-md-1 control-label">按钮ID</label>' +
                                    '<div class="col-md-3" style="padding: 0">' +
                                        '<input type="text" class="form-control input-sm btn-id" placeholder="示例：100101" >' +
                                        '<span>' +
                                    '</div>' +
                                    '<label class="col-md-2 control-label">按钮描述</label>' +
                                    '<div class="col-md-3" style="padding: 0">' +
                                        '<input type="text" class="form-control input-sm btn-descr" placeholder="示例：快速注册">' +
                                    '</div>' +
                                    '<a>' +
                                    '<div class="pull-left add-btn text-center add-btn-btn" onclick="deletebtn(this)">' +
                                    '<img src="../images/del.png" alt="add"/><span>删除</span>' +
                                    '</div>' +
                                    '</a>' +
                                '</div>' +
                            '</div>' +
                            '<div class="btn-save">' +
                            '<div class="btn-small">' +
                            '<a onclick="lastStep();">上一步</a>' +
                            '</div>' +
                            '<div class="btn-small">' +
                            '<a onclick="SaveBtn();">保存</a>' +
                            '</div>' +
                        '</div>' +
                        '</div>';
                    $('.content-of-add-or-edit').html(str);
               //     deletebtn();
                }
                else{
                    alert.eq(0).removeClass('hidden');
                    alert.eq(0).html('*页面ID重复');
                }
            }
        });
    }
    else if(add_or_edit == 0){
        var step = $('.user-reg-step').find('dl');
        $(step[0]).removeClass('doing');
        $(step[1]).addClass('doing');
        var str ='<div class="add-btn-page">' +
                '<div class="clearfix">' +
                    '<label class="col-md-1 control-label">页面ID</label>' +
                    '<div class="col-md-3" style="padding: 0">' +
                    '<input type="text" class="form-control input-sm" placeholder="示例：100031" value=" ' + pageid + ' " disabled>' +
                    '</div>' +
                    '<a onclick="addBtn();">' +
                        '<div class="pull-left add-btn text-center add-btn-btn">' +
                        '<img src="../images/add.png" alt="add"/><span>添加</span>' +
                        '</div>' +
                    '</a>' +
                '</div>' +
            '</div>' +
            '<div class="add-btn-content" id="addBtnContent">' +
                '<div class="btn-save">' +
                    '<div class="btn-small">' +
                        '<a onclick="lastStep();">上一步</a>' +
                    '</div>' +
                    '<div class="btn-small">' +
                        '<a onclick="SaveBtn();">保存</a>' +
                    '</div>' +
                '</div>' +
            '</div>';
        $('.content-of-add-or-edit').html(str);
        $.ajax({
            url:AJAXURL.getPageAction,
            type:'post',
            data:{
                "page_id":pageid
            },
            dataType:'json',
            success:function(response){
                if(response.state == true){
                    var btnbox = $('.add-btn-content');
                    var buttons = '<div class="btn-save">' +
                        '<div class="btn-small">' +
                        '<a onclick="lastStep();">上一步</a>' +
                        '</div>' +
                        '<div class="btn-small">' +
                        '<a onclick="SaveBtn();">保存</a>' +
                        '</div>' +
                        '</div>' ;
                    var length = response.data.length;
                    if(length!=0){
                        var btn ='';
                        for(var i=0;i<length;i++){
                            btn = btn + '<div class="clearfix">' +
                                '<label class="col-md-1 control-label">按钮ID</label>' +
                                '<div class="col-md-3" style="padding: 0">' +
                                '<input type="text" class="form-control input-sm btn-id" placeholder="示例：100101" value="' + response.data[i].page_action_id + '" disabled>' +
                                '</div>' +
                                '<label class="col-md-2 control-label">按钮描述</label>' +
                                '<div class="col-md-3" style="padding: 0">' +
                                '<input type="text" class="form-control input-sm btn-descr" placeholder="示例：快速注册" value="' + response.data[i].page_action_name + '">' +
                                '</div>' +
                                '<a>' +
                                '<div class="pull-left add-btn text-center add-btn-btn" onclick="deletebtn(this)">' +
                                '<img src="../images/del.png" alt="add"/><span>删除</span>' +
                                '</div>' +
                                '</a></div>';

                        }
                        btnbox.html('<div class="add-btn-content-1">'+btn+'</div>'+buttons);
                    }
                //    btnbox.html(buttons);
                 //   deletebtn();
                }
            }
        });
     //   deletebtn();
    }
}
function SaveBtn(){
    var content = $('.add-btn-content');
    var btnid = content.find('.btn-id');
    var btndes = content.find('.btn-descr');
    var status = 1;
    for(var i=0;i<btnid.length;i++){
        if(btnid.eq(i).val()==''){
            alert('请输入第'+i+'个按钮id。');
            status = 0 ;
        }
        else if(btndes.eq(i).val()==''){
            alert('请输入第'+i+'个按钮描述。');
            status = 0 ;
        }
        else if(btnid.eq(i).attr('disabled')!="disabled"){
            $.ajax({
                url:AJAXURL.checkActionId,
                type:'post',
                data:{
                    "page_action_id":btnid.eq(i).val()
                },
                dataType:'json',
                async: false,
                success:function(response){
                    if(response.state == false){
                        status = 0 ;
                        alert('按钮id已经存在。');
                    }
                    else{
                        for(var j=i+1;j<btnid.length;j++){
                            if(btnid.eq(i).val() == btnid.eq(j).val()){
                                status = 0 ;
                                alert('输入的按钮id重复。')
                            }
                        }
                    }
                }
            });
        }
    }

    if(status==1){
        var idArray = $('.btn-id');
        var nameArray = $('.btn-descr');
        var jsonArray = [];
        for(var j=0;j<idArray.length;j++){

                var btnjson = {
                    "actionId":idArray.eq(j).val(),
                    "actionName":nameArray.eq(j).val()
                };
                jsonArray.push(btnjson);

        }
        if(add_or_edit == 0 ){
            $.ajax({
                url: AJAXURL.editPage,
                type: 'post',
                data: {
                    "page_id": pageid,
                    "page_url": url,
                    "page_type": guishu,
                    "page_state": page_state,
                    "page_name": descrip,
                    "page_version": version,
                    "json": JSON.stringify(jsonArray)
                },
                dataType: 'json',
                success: function (response) {
                    if(response.state == true ){
                        var step = $('.user-reg-step').find('dl');
                        step.removeClass('doing');
                        $(step[2]).addClass('doing');
                        var str = '<div class="save-success col-md-7 text-center">' +
                            '<img src="../images/finish-regist.png" alt="finish-save"/><span>保存成功！</span></div>';
                        $('.content-of-add-or-edit').html(str);
                    }
                }
            });
        }
        else{
            $.ajax({
                url:AJAXURL.savePage,
                type:'post',
                data:{
                    "page_id":pageid,
                    "page_url":url,
                    "page_type":guishu,
                    "page_state":page_state,
                    "page_name":descrip,
                    "page_version":version,
                    "json":JSON.stringify(jsonArray)
                },
                dataType:'json',
                success:function(response){
                    if(response.state == true ){
                        var step = $('.user-reg-step').find('dl');
                        step.removeClass('doing');
                        $(step[2]).addClass('doing');
                        var str = '<div class="save-success col-md-7 text-center">' +
                            '<img src="../images/finish-regist.png" alt="finish-save"/><span>保存成功！</span></div>';
                        $('.content-of-add-or-edit').html(str);
                    }
                }
            });
        }
    }
}
function deletebtn(obj){
    var delbtn = $('.add-btn-content').find('.add-btn-btn');
    var len = delbtn.length;
    if(len == 1){
        alert("至少需要有一个按钮");
        return false;
    }
    else{
        $(obj).parent().parent().remove();
    }
}
function lastStep(){
    var step = $('.user-reg-step').find('dl');
    var check_t,check_f = '';
    var disable = '';
    if(page_state==1){
        check_t = 'checked="checked"';
    }
    else if(page_state==0){
        check_f = 'checked="checked"';
    }
    if(add_or_edit == 0){
        disable = 'disabled' ;
    }
    step.removeClass('doing');
    $(step[0]).addClass('doing');
    var str =' <div class="content clearfix add-page-info">' +
        '<div style="margin:50px 0 0 30%;text-align: center;">' +
        '<div class="row clearfix">' +
        '<label class="col-md-2 control-label">页面ID</label>' +
        '<div class="col-md-4" style="padding: 0">' +
        '<input type="text" class="form-control input-sm" placeholder="示例：100100" value="' + pageid + '" ' + disable + '>' +
        '</div>' +
        '</div>' +
        '<div class="row clearfix">' +
        '<label class="col-md-2 control-label">页面URL</label>' +
        '<div class="col-md-4" style="padding: 0">' +
        '<input type="text" class="form-control input-sm" placeholder="示例：login_success_page" value="' + url + '">' +
        '</div>' +
        '</div>' +
        '<div class="row clearfix">' +
        '<div class="col-md-2 text-right">' +
        '<p style="line-height: 30px;">页面归属</p>' +
        '</div>' +
        '<select class="input-sm col-md-4" id="selectList" style="height: 30px;">' +
        '<option value="1">1</option>' +
        '</select>' +
        '</div>' +
        '<div class="row clearfix">' +
        '<div class="col-md-2 text-right"><p>页面状态</p></div>' +
        '<div class="col-md-4">' +
        '<label class="radio-inline">' +
        '<input class="input-sm" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="1" ' + check_t + '/>生效' +
        '</label>' +
        '<label class="radio-inline">' +
        '<input class="input-sm" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="0" ' + check_f + '/>作废' +
        '</label></div></div><div class="row clearfix">' +
        '<label class="col-md-2 control-label">页面描述</label>' +
        '<div class="col-md-4" style="padding: 0">' +
        '<input type="text" class="form-control input-sm" placeholder="示例：登录成功页面" value="' + descrip + '">' +
        '</div>' +
        '</div>' +
        '<div class="row clearfix">' +
        '<label class="col-md-2 control-label">版本号</label>' +
        '<div class="col-md-4" style="padding: 0">' +
        '<input type="text" class="form-control input-sm" placeholder="示例：3.1.0" value="' + version + '">' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div class="btn-small"><a onclick="Next();" style="float:none;margin:0 auto;">下一步</a></div></div>';
    $('.content-of-add-or-edit').html(str);
}
function addBtn(){
    var content = $('.add-btn-content-1');
    var str ;
    str = content.html() +  '<div class="clearfix">' +
        '<label class="col-md-1 control-label">按钮ID</label>' +
        '<div class="col-md-3" style="padding: 0">' +
        '<input type="text" class="form-control input-sm btn-id" placeholder="示例：100101">' +
        '</div>' +
        '<label class="col-md-2 control-label">按钮描述</label>' +
        '<div class="col-md-3" style="padding: 0">' +
        '<input type="text" class="form-control input-sm btn-descr" placeholder="示例：快速注册">' +
        '</div>' +
        '<a>' +
        '<div class="pull-left add-btn text-center add-btn-btn" onclick="deletebtn(this)">' +
        '<img src="../images/del.png" alt="add"/><span>删除</span>' +
        '</div>' +
        '</a>' +
        '</div>' ;
    content.html(str);
  //  deletebtn();
}