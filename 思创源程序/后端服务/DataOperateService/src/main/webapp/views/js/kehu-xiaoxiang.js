//性别默认为男
var _clientGender=1;
$(function(){
    $('#gender').val(_clientGender);
    $('#search-content').focus();
});
//自动补全
$("#search-content").autocomplete({
    delay: 400,
    source: function(request, response) {
        var _sex=$('input[name="inlineRadioOptions"]:checked').val();
        $.ajax({
            url: SLSCHTTP+"/customerportrait/searchUser",
            type:'post',
            data: {
                param: request.term,
                sex: _sex
            },
            dataType: "json",
            success: function(data) {
                var i,_itemLen=data.data.length,itemList=[];
                for(i=0;i<_itemLen;i++){
                    itemList.push(data.data[i]['name']+'{}'+data.data[i]['uid']);
                }
                response($.map(itemList, function(item) {
                    var _name=item.split('{}')[0];
                    var _uid=item.split('{}')[1];
                    return {
                        label: _name,  //修改jq-ui的源码，为其增加num字段，用来存每一个用户的uid，为了当用户名有重复时作为标识符传给后台(这是之前的，现在不用了。不删除是因为可以作为改源码的参考。)
                        num: _uid
                    }
                }));
            }
        });
    },
    minLength: 1,
    open: function() {  //在下拉框被显示的时候触发
        $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
    },
    close: function() {  //在下拉框被隐藏的时候触发
        $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
    }
});
/**
 *存uid
 */
//$(document).on('click','.ui-menu-item',function(){
//    $('#uid').val($(this).attr('uid'));
//});
/**
 *检测男女
 */
function testGender(){
    var _gender=$('input[name="inlineRadioOptions"]:checked').val();
    var _pic=$('.client-pic');
    if(parseInt(_gender)==1){
        _clientGender=1;
        _pic.attr('src','../images/male.jpg');
    }
    if(parseInt(_gender)==0){
        _clientGender=0;
        _pic.attr('src','../images/female.jpg');
    }
    if(parseInt($('#gender').val())!=_clientGender){  //性别选择改变了
        $('.client-box').css({display:'none'});
        $('.main').css({display:'none'});
//        $('#search-content').val('');
    }
}
/**
 *搜索用户
 */
function searchClinet(){
    var _name= $.trim($('#search-content').val());
    var _sex=$('input[name="inlineRadioOptions"]:checked').val();
    if(_name==''){
        alert('请输入查询内容');
        return false;
    }else{
        $.ajax({
            url:SLSCHTTP+'/customerportrait/searchUser',
            type:'post',
            data:{param:_name,sex:_sex},
            dataType:'json',
            success:function(response){
                var _list='',_i,_len=response.data.length,_table=$('.client-box');
                if(_len==0){
                    _list+='<tr><td colspan="3">暂无数据</td></tr>';
                }else{
                    for(_i=0;_i<_len;_i++){
                        _list+='' +
                            '<tr>' +
                            '<td>'+response.data[_i].name+'</td>' +
                            '<td>'+response.data[_i].phone+'</td>' +
                            '<td>' +
                            '<span class="do-list" onclick="seePic(this);">查看用户画像</span>' +
                            '<span class="clinet-hide">'+response.data[_i].uid+'</span>' +
                            '</td>' +
                            '</tr>';
                    }
                }
                _table.find('tbody').html(_list);
                _table.css({display:'block'});
                $('.main').css({display:'none'});
                $('#gender').val(_clientGender);
            }
        });
    }
}
/**
 *用户画像
 */
function seePic(obj){
    var _uid=$(obj).next().text();
    $(obj).parent().parent().addClass('tr-active').siblings().removeClass('tr-active');
    $.ajax({
        url:SLSCHTTP+'/customerportrait/getCustomerPortrait',
        type:'post',
        data:{uid:_uid},
        dataType:'json',
        success:function(response){
//              基本信息
            var _baseInfo=response.data.baseinfo,
                _baseInfoData=[],
                _tr1=$('.table1').find('table').find('tr'),
                _attr1;
            for(_attr1 in _baseInfo){
                if(_baseInfo[_attr1]==null){
                    _baseInfoData.push('未知');
                }else{
                    _baseInfoData.push(_baseInfo[_attr1]);
                }
            }
//              这尼玛坑爹啊，对照静态页面一个一个比对吧
            _tr1.eq(0).find('td').eq(1).text(_baseInfoData[7]);
            _tr1.eq(1).find('td').eq(1).text(_baseInfoData[9]);
            _tr1.eq(2).find('td').eq(1).text(_baseInfoData[2]);
            _tr1.eq(3).find('td').eq(1).text(_baseInfoData[11]);
            _tr1.eq(4).find('td').eq(1).text(_baseInfoData[3]);
            _tr1.eq(5).find('td').eq(1).text(_baseInfoData[0]);
            _tr1.eq(6).find('td').eq(1).text(_baseInfoData[1]);
            _tr1.eq(7).find('td').eq(1).text(_baseInfoData[8]);
            _tr1.eq(8).find('td').eq(1).text(_baseInfoData[12]);
            _tr1.eq(9).find('td').eq(1).text(_baseInfoData[10]);
            _tr1.eq(10).find('td').eq(1).text(_baseInfoData[5]);
            _tr1.eq(11).find('td').eq(1).text(_baseInfoData[4]);
            _tr1.eq(12).find('td').eq(1).text(_baseInfoData[13]);
            _tr1.eq(13).find('td').eq(1).text(_baseInfoData[6]);
//              消费行为
            var _conBehavior=response.data.conBehavior,
                _conBehaviorData=[],
                _tr2=$('.table2').find('table').find('tr'),
                _attr2;
            for(_attr2 in _conBehavior){
                if(_conBehavior[_attr2]==null){
                    _conBehaviorData.push('未知');
                }else{
                    _conBehaviorData.push(_conBehavior[_attr2]);
                }
            }
            _tr2.eq(0).find('td').eq(1).text(_conBehaviorData[3]);
            _tr2.eq(1).find('td').eq(1).text(_conBehaviorData[5]);
            _tr2.eq(2).find('td').eq(1).text(_conBehaviorData[4]);
            _tr2.eq(3).find('td').eq(1).text(_conBehaviorData[1]);
            _tr2.eq(4).find('td').eq(1).text(_conBehaviorData[2]);
//              活跃度
            var _activation=response.data.activation,
                _activationData=[],
                _tr3=$('.table3').find('table').find('tr'),
                _attr3;
            for(_attr3 in _activation){
                if(_activation[_attr3]==null){
                    _activationData.push('未知');
                }else{
                    _activationData.push(_activation[_attr3]);
                }
            }
            _tr3.eq(0).find('td').eq(1).text(_activationData[1]);
            _tr3.eq(1).find('td').eq(1).text(_activationData[2]);
//              用户关注，这里后台数据还不完整，后两项在静态页面上写的未知
            var _hobbyShop=response.data.hobbyShop,
                _tr4=$('.table4').find('table').find('tr');
            if(_hobbyShop==null){
                _tr4.eq(0).find('td').eq(1).text('未知');
            }else{
                _tr4.eq(0).find('td').eq(1).text(_hobbyShop['喜好商铺']);
            }
            if(response.data.hobbyGood==null){  //商品
                _tr4.eq(1).find('td').eq(1).text('未知');
            }else{
                _tr4.eq(1).find('td').eq(1).text(response.data.hobbyGood.num_1);
            }
//              用户群
            var _userGroup=response.data.userGroup,
                _userGroupData=[],
                _tr5=$('.table5').find('table').find('tr'),
                _attr5;
            for(_attr5 in _userGroup){
                if(_userGroup[_attr5]==null){
                    _userGroupData.push('未知');
                }else{
                    _userGroupData.push(_userGroup[_attr5]);
                }
            }
            _tr5.eq(0).find('td').eq(1).text(_userGroupData[3]);
            _tr5.eq(1).find('td').eq(1).text(_userGroupData[1]);
            _tr5.eq(2).find('td').eq(1).text(_userGroupData[2]);
//              用户浏览爱好
            var _browseHobby=response.data.browseHobby,_len=_browseHobby.length,_table6=$('.table6');
            if(_len==0){
                _table6.find('tr').eq(1).find('td').text('未知');
                _table6.find('tr').eq(2).find('td').text('未知');
                _table6.find('tr').eq(3).find('td').text('未知');
            }else{
                if(_len==1){
                    _table6.find('tr').eq(1).find('td').text(_browseHobby[0].words);
                    _table6.find('tr').eq(2).find('td').text('未知');
                    _table6.find('tr').eq(3).find('td').text('未知');
                }
                if(_len==2){
                    _table6.find('tr').eq(1).find('td').text(_browseHobby[0].words);
                    _table6.find('tr').eq(2).find('td').text(_browseHobby[1].words);
                    _table6.find('tr').eq(3).find('td').text('未知');
                }
                if(_len==3){
                    _table6.find('tr').eq(1).find('td').text(_browseHobby[0].words);
                    _table6.find('tr').eq(2).find('td').text(_browseHobby[1].words);
                    _table6.find('tr').eq(3).find('td').text(_browseHobby[2].words);
                }
            }
            if(response.data.searchHobby==null){
                $('.hobby').text('未知');
            }else{
                $('.hobby').text(response.data.searchHobby['word_content']);
            }
            $('.main').css({display:'block'});
        }
    });
}
/**
 *回车事件
 */
$('#search-content').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        searchClinet();
    }
});