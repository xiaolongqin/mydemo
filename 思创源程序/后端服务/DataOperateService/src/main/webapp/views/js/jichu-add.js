/**
 *初始化日历
 */
var _nodeIdSave,_routeIdSave;
$(function(){
    //日历
    var _time=new Date();
    var _start=_time.getTime()-180*24*3600*1000;
    var _end=_time.getTime()-24*3600*1000;
    $('.form_date').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        startDate: new Date(_start),
        endDate:new Date(_end)
    });
    var option1 = {
        title : {
            text: '示例图',
            x:'center'
        },
        calculable : true,
        toolbox: {
            orient : 'vertical',
            y : 'top',
            show : true,
            feature : {
                restore : {show: true}
            }
        },
        series : [
            {
                name:'漏斗图',
                type:'funnel',
                width:'50%',
                height:'80%',
                x:'25%',
                y:30,
                data:[
                    {value:60, name:'宝贝详情页'},
                    {value:40, name:'宝贝订单页'},
                    {value:20, name:'结果页'},
                    {value:80, name:'店铺首页'},
                    {value:100, name:'路径首页'}
                ],
                itemStyle:{
                    normal:{
                        label: {
                            show: true,
                            position: 'inside'
                        }
                    }
                }
            }
        ]
    };
    var myChart1 = echarts.init(document.getElementById('zenggaiChart1'));
    myChart1.setOption(option1);
    window.onresize=function(){
        myChart1.resize();
    };
    $.ajax({
        url:SLSCHTTP+'/keyroute/getLastRouteId',
        type:'post',
        data:{},
        dataType:'json',
        success:function(response){
            _routeIdSave=parseInt(response.data[0]['route_id']);
        }
    });
});
//遮罩
function shade(){
    var divShade=window.parent.document.createElement('div');
    var _checkbox=$('#no-parent');
    divShade.style.width='100%';
    divShade.style.height=window.parent.document.body.clientHeight+150+'px';
    divShade.style.position='absolute';
    divShade.style.top=0;
    divShade.style.left=0;
    divShade.style.zIndex=999;
    divShade.style.backgroundColor="#2C2C2C";
    divShade.style.filter="alpha(opacity=60)";
    divShade.style.opacity="0.6";
    $(divShade).addClass('shade');
    window.parent.document.body.appendChild(divShade);
//  重置
    var _tr=$('#pageTable').find('tbody').find('tr');
    var _len=_tr.length;
    var _first=_tr.eq(0).find('span').eq(2).text();
    var _parent=$('.parent-content');
    if(_len==0){
        $.ajax({  //tbody里面没有数据就重新请求nodeId
            url:SLSCHTTP+'/keyroute/getLastNodeId',
            type:'post',
            data:{},
            dataType:'json',
            success:function(response){
                _nodeIdSave=parseInt(response.data[0].node_id)+1;
            }
        });
        _checkbox[0].checked=false;
        $('.search-btn').eq(0).css({visibility:'visible'});
        $('.checkbox').css({visibility:'visible'});
        $('#parentID').val('');
        _parent.text('暂无');
    }else{
        $('.search-btn').eq(0).css({visibility:'hidden'});
        $('.checkbox').css({visibility:'hidden'});
        $('#parentID').val(_first);
        for(var _i=0;_i<4;_i++){
            _parent.eq(_i).text(_tr.find('span').eq(_i+2).text());
        }
    }
    $('#childID').val('');
    $('#node-desc').val('');
    $('.child-content').text('暂无');
}
//去掉遮罩
function closeShade(){
    $('#modal-div').css({display:'none'});
    $(window.parent.document.body).find('.shade').remove();
}
$('body').on('keydown',function(event){
    event = event || window.event;
    if(event.keyCode==27){
        setTimeout(closeShade,110);
    }
});
//无上级
function noParent(obj){
    if($(obj)[0].checked==true){
        $('#parentID').val('');
        $('.parent-content').text('暂无');
    }
}
//如果选择了有上级，但是又往输入框里面输入了值，则取消无上级
function cancelNoParent(){
    $('#no-parent')[0].checked=false;
}
//查询框弹出
function seeSearch(obj){
//  所有重置
    if($(obj).index('.search-btn')==0){
        $('#search-content').attr('page','0').val('');  //上级页面的查询
        $('.id-title').eq(0).text('查询上级页面ID');
    }else{
        $('#search-content').attr('page','1').val('');  //下级页面的查询
        $('.id-title').eq(0).text('查询下级页面ID');
    }
    $('#page-table').find('tbody').html('<tr><td colspan="5">暂无数据</td></tr>');
    $('#modal-div').css({display:'block'});
    $('#search-content').focus();
}
//取消查询框
function delSearch(){
    $('#modal-div').css({display:'none'});
}
/**
 *查询页面id
 */
function searchId(){
    var param= $.trim($('#search-content').val());
    if(param!=''){
        $.ajax({
            url:SLSCHTTP+'/keyroute/searchPage',
            type:'post',
            data:{param:param},
            dataType:'json',
            success:function(response){
                var _i,_len=response.data.length,_list='';
                if(_len==0){
                    _list='<tr><td colspan="5">暂无数据</td></tr>';
                }else{
                    for(_i=0;_i<_len;_i++){
                        var _type=response.data[_i]['page_type']+'',_state=response.data[_i]['page_state']+'';  //switch不会进行类型转换
                        switch (_type){
                            case '1':_type='用户端';break;
                            case '2':_type='商户端';break;
                            case '3':_type='管家端';break;
                        }
                        switch (_state){
                            case '0':_state='失效';break;
                            case '1':_state='生效';break;
                        }
                        _list+='<tr>' +
                            '<td>'+response.data[_i]['page_id']+'</td>' +  //id
                            '<td>'+response.data[_i]['page_name']+'</td>' +  //描述
                            '<td>'+response.data[_i]['page_url']+'</td>' +  //url
                            '<td>'+_state+'</td>' +  //状态
                            '<td style="display: none;">'+_type+'</td>'+  //归属
                            '<td style="display: none;">'+response.data[_i]['page_version']+'</td>'+  //版本
                            '<td>' +
                            '<label onclick="choseId(this);"><input class="my-check" type="checkbox"/>选择</label>' +
                            '</td>' +
                            '</tr>';
                    }
                }
                $('#page-table').find('tbody').html(_list);
            }
        });
    }else{
        alert('请输入查询内容');
    }
}
/**
 *回车事件
 */
$('#search-content').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        searchId();
    }
});
/**
 *选择id
 */
function choseId(obj){
    $(obj).parent().parent().siblings().find('input').attr('checked',false);  //除选中的checkebox以外都为不选中
}
/**
 *查询id确定
 */
function saveId(){
    var index=$('#search-content').attr('page'),_i,_j,_check=$('.my-check'),_len=_check.length,_td,conArray=[];  //判断文字应该存在上级还是下级
    for(_i=0;_i<_len;_i++){
        if(_check[_i].checked==true){
            _td=$('#page-table').find('tbody').find('tr').eq(_i).find('td');
            conArray=[
                _td.eq(1).text(),  //描述
                _td.eq(4).text(),  //归属
                _td.eq(3).text(),  //状态
                _td.eq(5).text(),  //版本
                _td.eq(0).text()   //id
            ];
        }
    }
    if(parseInt(index)==0){  //上级页面
        $('#no-parent')[0].checked=false;
        $('#parentID').val(conArray[4]);
        for(_j=0;_j<4;_j++){
            $('.parent-content').eq(_j).text(conArray[_j]);
        }
    }else{  //下级页面
        $('#childID').val(conArray[4]);
        for(_j=0;_j<4;_j++){
            $('.child-content').eq(_j).text(conArray[_j]);
        }
    }
    $('#modal-div').css({display:'none'});
}
/**
 *保存节点
 */
function saveNode(){
    var _list='',_parentId,_childId,_desc;
    _parentId=$('#parentID').val();
    _childId=$('#childID').val();
    _desc= $.trim($('#node-desc').val());
    var _child=$('.child-content');
    if(_parentId=='' && $('#no-parent')[0].checked==false){
        alert('请配置上级页面');
        return false;
    }
    if(_childId==''){
        alert('请配置下级页面');
        return false;
    }
    if(_desc==''){
        alert('请填入节点描述');
        return false;
    }
    if(_parentId==_childId){
        alert('上下级页面ID不能为同一个');
        return false;
    }
    if(_parentId==''){
        _parentId='-1';
    }
    _list+='<tr>' +
        '<td>'+(parseInt(_nodeIdSave))+'</td>' +
        '<td>' +
        '<span>'+_desc+'</span>'+
        '<span style="display: none;">'+_parentId+'</span>'+
        '<span style="display: none;">'+_childId+'</span>'+
        '<span style="display: none;">'+_child.eq(0).text()+'</span>'+  //描述
        '<span style="display: none;">'+_child.eq(1).text()+'</span>'+  //归属
        '<span style="display: none;">'+_child.eq(2).text()+'</span>'+  //状态
        '<span style="display: none;">'+_child.eq(3).text()+'</span>'+  //版本
        '</td>' +
        '<td><span class="del-word" onclick="delNode(this);">删除</span></td>' +
        '</tr>';
    _nodeIdSave+=1;
    $('#pageTable').find('tbody').prepend(_list);
    $('#pageTable').find('.del-word').not(':first').addClass('del-hidden');
    $('#table-box').css({display:'block'});
    $('.node-save').css({visibility:'visible'});
    $('#myModal').modal('hide');
    closeShade();
//  漏斗图改变
    var _i,_len=$('#pageTable').find('tbody').find('tr').length,_json=[],_num=Math.floor(100/_len);
    for(_i=0;_i<_len;_i++){
        _json.push({
            value: _num*(_i+1),
            name: $('#pageTable').find('tbody').find('tr').eq(_i).find('span').eq(0).text()
        });
    }
    var option1 = {
        title : {
            text: '示例图',
            x:'center'
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            }
        },
        calculable : true,
        series : [
            {
                name:'漏斗图',
                type:'funnel',
                width: '50%',
                height:'80%',
                x:'25%',
                y:30,
                data:_json,
                itemStyle:{
                    normal:{
                        label: {
                            show: true,
                            position: 'inside'
                        }
                    }
                }
            }
        ]
    };
    var myChart1 = echarts.init(document.getElementById('zenggaiChart1'));
    myChart1.setOption(option1);
    window.onresize=function(){
        myChart1.resize();
    };
}
/**
 *删除节点
 */
function delNode(obj){
    var _len=$('#pageTable').find('tbody').find('tr').length;
    $(obj).parent().parent().remove();
    $('#pageTable').find('.del-word').eq(0).removeClass('del-hidden');
    _len-=1;
    //_nodeIdSave-=1;
    if(_len==0){
        $('#table-box').css({display:'none'});
        $('.node-save').css({visibility:'hidden'});
    }
//  漏斗图改变
    var _i,_leng=$('#pageTable').find('tbody').find('tr').length,_json=[],_num=Math.floor(100/_leng);
    if(_leng==0){
        _json=[
            {value:60, name:'宝贝详情页'},
            {value:40, name:'宝贝订单页'},
            {value:20, name:'结果页'},
            {value:80, name:'店铺首页'},
            {value:100, name:'路径首页'}
        ]
    }else{
        for(_i=0;_i<_leng;_i++){
            _json.push({
                value: _num*(_i+1),
                name: $('#pageTable').find('tbody').find('tr').eq(_i).find('span').eq(0).text()
            });
        }
    }
    var option1 = {
        title : {
            text: '示例图',
            x:'center'
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            }
        },
        calculable : true,
        series : [
            {
                name:'漏斗图',
                type:'funnel',
                width: '50%',
                height:'80%',
                x:'25%',
                y:30,
                data:_json,
                itemStyle:{
                    normal:{
                        label: {
                            show: true,
                            position: 'inside'
                        }
                    }
                }
            }
        ]
    };
    var myChart1 = echarts.init(document.getElementById('zenggaiChart1'));
    myChart1.setOption(option1);
    window.onresize=function(){
        myChart1.resize();
    };
}
/**
 *保存所有
 */
function saveAll(){
    var date1=$('#time-start').val();  //开始时间
    var date2=$('#time-end').val();  //结束时间
    if(date1!='' && date2!=''){
        var year1 =  date1.substr(0,4);
        var year2 =  date2.substr(0,4);
        var month1 = date1.substr(5,2);
        var month2 = date2.substr(5,2);
        var day1 = date1.substr(8,2);
        var day2 = date2.substr(8,2);
        var temp1 = year1+"/"+month1+"/"+day1;
        var temp2 = year2+"/"+month2+"/"+day2;
        var dateaa= new Date(temp1);
        var datebb = new Date(temp2);
        var date = datebb.getTime() - dateaa.getTime();
        var time = Math.floor(date / (1000 * 60 * 60 * 24));
        if(time<0){
            alert('请选择正确的时间段');
            return false;
        }
//        if(time>90){
//            alert('您的时间跨度为'+time+'天，时间跨度超过90天');
//            return false;
//        }
    }else{
        alert('请选择正确的时间段');
        return false;
    }
    var routeDesc=$.trim($('#path-desc').val());  //路径描述
    if(routeDesc==''){
        alert('请输入路径描述');
        return false;
    }
    var _tr=$('#pageTable').find('tbody').find('tr'),_td,_len=_tr.length,_i,_json=[];
    if(_len==0){
        alert('至少有一个节点');
        return false;
    }
    for(_i=0;_i<_len;_i++){
        _td=_tr.eq(_i).find('td');
        _json.push({
            node_id: _td.eq(0).text(),
            node_name: _td.eq(1).find('span').eq(0).text(),
            startPageId: _td.eq(1).find('span').eq(1).text(),
            nextPageId: _td.eq(1).find('span').eq(2).text()
        });
    }
    var _route={route_id:_routeIdSave+1,route_name:routeDesc},startTime=date1.split('-').join(''),endTime=date2.split('-').join('');
    $.ajax({
        url:SLSCHTTP+'/keyroute/addRouteNodes',
        type:'post',
        data:{startTime:startTime,endTime:endTime,node:JSON.stringify(_json),route:JSON.stringify(_route)},
        dataType:'json',
        success:function(response){
            if(response.state==true){
                window.location.href=SLSCHTTP+'/views/html/jichu-lujingjiankong.html';
            }else{
                alert('添加失败，页面刷新后重新添加');
                window.location.reload();
            }
        }
    });
}