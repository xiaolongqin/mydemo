var _routeChart1,_routeChart2;
/**
 * 进入页面全查
 */
$(function(){
    changePage();
    $.ajax({
        url:SLSCHTTP+'/keyroute/searchRoutes',  //getRoutes
        type:'post',
        data:{name:'',pageNumber:1},
        dataType:'json',
        success:function(response){
            if(response.data.length!=0){
                var _list='',_i,_len=response.data.length,_state,_newState;
                for(_i=0;_i<_len;_i++){
                    if(response.data[_i]['route_state']==true){
                        _state='生效';
                        _newState='失效';
                    }else{
                        _state='失效';
                        _newState='生效';
                    }
                    _list+='<tr onclick="getNodes(this);">' +
                        '<td>'+response.data[_i]['route_id']+'</td>' +
                        '<td>'+response.data[_i]['route_name']+'</td>' +
                        '<td>'+_state+'</td>' +
                        '<td>' +
                        '<a class="btn-word" href="../html/jichu-change.html?&id='+response.data[_i]['route_id']+'">修改</a>' +
                        '<span class="btn-word" onclick="changeState(this);">'+_newState+'</span>' +
                        '<span class="btn-word" style="color: red;" onclick="delRoute(this);">删除</span>' +
                        '</td>' +
                        '</tr>';
                }
                $('#path-table').find('tbody').html(_list);
                $('#totalPage').text(response.data[0].total);
            }else{
                $('#path-table').find('tbody').html('<tr><td colspan="4">暂无数据</td></tr>');
            }
        }
    });
});
/**
 *进入增改页面
 */
function goAddUpd(){
    window.location.href=SLSCHTTP+'/views/html/jichu-add.html';
}
/**
 *搜索路径
 */
function searchRoute(){
    $('.page-change').find('input').val('');
    var param= $.trim($('#search-content').val());
    $.ajax({
        url:SLSCHTTP+'/keyroute/searchRoutes',
        type:'post',
        data:{name:param,pageNumber:1},
        dataType:'json',
        success:function(response){
            if(response.data.length!=0){
                var _list='',_i,_len=response.data.length,_state,_newState;
                for(_i=0;_i<_len;_i++){
                    if(response.data[_i]['route_state']==true){
                        _state='生效';
                        _newState='失效';
                    }else{
                        _state='失效';
                        _newState='生效';
                    }
                    _list+='<tr onclick="getNodes(this);">' +
                        '<td>'+response.data[_i]['route_id']+'</td>' +
                        '<td>'+response.data[_i]['route_name']+'</td>' +
                        '<td>'+_state+'</td>' +
                        '<td>' +
                        '<a class="btn-word" href="../html/jichu-change.html?&id='+response.data[_i]['route_id']+'">修改</a>' +
                        '<span class="btn-word" onclick="changeState(this);">'+_newState+'</span>' +
                        '<span class="btn-word" style="color: red;" onclick="delRoute(this);">删除</span>' +
                        '</td>' +
                        '</tr>';
                }
            }else{
                _list=' <tr><td colspan="4">暂无数据</td></tr>';
            }
            $('#path-table').find('tbody').html(_list);
            $('#totalPage').text(response.data[0].total);
        }
    });
    $('.node-box').css({display:'none'});
}
/**
 *回车事件
 */
$('#search-content').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        searchRoute();
    }
});
$('#goPage').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        var _nowPage=parseInt($('#goPage').val()),
            _totalPage=parseInt($('#totalPage').text()),
            _name= $.trim($('#search-content').val());
        if(_nowPage > _totalPage){
            alert("最大页数是"+_totalPage+"，不能超过最大页数");
            return false;
        }else if(_nowPage>0 && (/^\d+$/.test(_nowPage))){
            $('#newPage').text(_nowPage);
            $.ajax({
                url:SLSCHTTP+'/keyroute/searchRoutes',
                type:'post',
                data:{name:_name,pageNumber:_nowPage},
                dataType:'json',
                success:function(response){
                    if(response.data.length!=0){
                        var _list='',_i,_len=response.data.length,_state,_newState;
                        for(_i=0;_i<_len;_i++){
                            if(response.data[_i]['route_state']==true){
                                _state='生效';
                                _newState='失效';
                            }else{
                                _state='失效';
                                _newState='生效';
                            }
                            _list+='<tr onclick="getNodes(this);">' +
                                '<td>'+response.data[_i]['route_id']+'</td>' +
                                '<td>'+response.data[_i]['route_name']+'</td>' +
                                '<td>'+_state+'</td>' +
                                '<td>' +
                                '<a class="btn-word" href="../html/jichu-change.html?&id='+response.data[_i]['route_id']+'">修改</a>' +
                                '<span class="btn-word" onclick="changeState(this);">'+_newState+'</span>' +
                                '<span class="btn-word" style="color: red;" onclick="delRoute(this);">删除</span>' +
                                '</td>' +
                                '</tr>';
                        }
                    }else{
                        _list=' <tr><td colspan="4">暂无数据</td></tr>';
                    }
                    $('#path-table').find('tbody').html(_list);
                    $('#totalPage').text(response.data[0].total);
                }
            });
            $('.node-box').css({display:'none'});
        }else{
            alert("请输入大于0的整数");
        }
    }
});
/**
 *获取节点列表
 */
function getNodes(obj){
    $(obj).addClass('tr-active').siblings().removeClass('tr-active');
    var route_id=$(obj).find('td').eq(0).text();
    $('#lujingChart2').html('');
    $.ajax({
        url:SLSCHTTP+'/keyroute/getNodes',
        type:'post',
        data:{route_id:route_id},
        dataType:'json',
        success:function(response){
            $('.node-box').css({display:'block'});
//          table列表
            var _list='',_i,_len=response.data.length,_legend=[],_chartData=[],_one,luJIngLoading1;
            _one=Math.floor(100/_len);  //漏斗图最大值为100
            for(_i=0;_i<_len;_i++){
                _list+='<tr>' +
                '<td>'+response.data[_i]['node_id']+'</td>' +
                '<td>'+response.data[_i]['node_name']+'</td>' +
                '<td>' +
                '<span>'+response.data[_i]['start_page_url']+'</span>' +
                '<span style="display: none;">'+response.data[_i]['start_time']+'</span>' +
                '</td>' +
                '<td>' +
                '<span>'+response.data[_i]['next_page_url']+'</span>' +
                '<span style="display: none;">'+response.data[_i]['end_time']+'</span>' +
                '</td>' +
                '</tr>';
                _legend.push(response.data[_i]['node_name']);
                _chartData.push({
                    value:_one*(_i+1),
                    name:response.data[_i]['node_name'],
                    startTime:response.data[_i]['start_time'],
                    endTime:response.data[_i]['end_time'],
                    startPageId:response.data[_i]['start_page_id'],
                    nextPageId:response.data[_i]['next_page_id']
                });
            }
            $('#node-table').find('tbody').html(_list);
//          图表
            var option1 = {
                toolbox: {
                    show : true,
                    orient: 'vertical',
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data : _legend
                },
                calculable : true,
                series : [
                    {
                        name:'漏斗图',
                        type:'funnel',
                        data:_chartData,
                        y:100,
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
            _routeChart1 = echarts.init(document.getElementById('lujingChart1'));
            _routeChart1.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 15
                }
            });
            clearTimeout(luJIngLoading1);
            luJIngLoading1 = setTimeout(function (){
                _routeChart1.hideLoading();
                _routeChart1.setOption(option1);
            },2000);
//          为图表添加点击事件
            var ecConfig = echarts.config,_clickChart;
            _routeChart1.on(ecConfig.EVENT.CLICK, function(param){
                var startTime,endTime,startPageId,nextPageId;
                startTime=param.data.startTime;
                endTime=param.data.endTime;
                startPageId=param.data.startPageId;
                nextPageId=param.data.nextPageId;
                $.ajax({
                    url:SLSCHTTP+'/keyroute/getRouteResult',
                    type:'post',
                    data:{startTime:startTime,endTime:endTime,startPageId:startPageId,nextPageId:nextPageId},
                    dataType:'json',
                    success:function(response){
                        var _num1= 0,  //普通会员人数
                            _num2= 0,  //铜牌会员人数
                            _num3= 0,  //银牌会员人数
                            _num4= 0,  //金牌会员人数
                            _num5= 0,  //白金会员人数
                            _num6= 0,  //钻一人数
                            _num7= 0,  //钻二人数
                            _num8= 0,  //非会员人数
                            _avg= 0,  //页面平均停留时间
                            _pv= 0,  //页面PV
                            _uv= 0,  //页面UV
                            _i,
                            _len=response.data.length,
                            luJIngLoading2;
                        for(_i=0;_i<_len;_i++){
                            _avg+=response.data[_i].avg_page_restime;
                            _pv+=response.data[_i].page_pv;
                            _uv+=response.data[_i].page_uv;
                            switch (response.data[_i].levelname){
                                case '普通会员':_num1+=1;break;
                                case '铜牌会员':_num2+=1;break;
                                case '银牌会员':_num3+=1;break;
                                case '金牌会员':_num4+=1;break;
                                case '白金会员':_num5+=1;break;
                                case '钻一':_num6+=1;break;
                                case '钻二':_num7+=1;break;
                                case '非会员':_num8+=1;break;
                            }
                        }
                        _avg=parseInt(_avg/_len);
                        _pv=parseInt(_pv/_len);
                        _uv=parseInt(_uv/_len);
                        if(isNaN(_avg)){
                            _avg=0;
                        }
                        if(isNaN(_pv)){
                            _pv=0;
                        }
                        if(isNaN(_uv)){
                            _uv=0;
                        }
                        var option2 = {
                            title : {
                                text: '页面平均停留时间：'+_avg+'秒\n'+'页面PV：'+_pv+'\n'+'页面UV：'+_uv+'\n',
                                x:'center',
                                textStyle:{
                                    fontSize: 14,
                                    fontWeight: 'normal'
                                }
                            },
                            toolbox: {
                                show : true,
                                orient: 'vertical',
                                y: 'center',
                                feature : {
                                    restore : {show: true}
                                }
                            },
                            tooltip : {
                                trigger: 'item',
                                formatter: "{b} : {c}"
                            },
                            legend: {
                                orient : 'vertical',
                                x : 'left',
                                data:['普通会员','铜牌会员','银牌会员','金牌会员','白金会员','钻一','钻二','非会员']
                            },
                            calculable : true,
                            series : [
                                {
                                    name:'访问来源',
                                    type:'pie',
                                    radius : '50%',
                                    minAngle : 5,
                                    y:150,
                                    center: ['50%', '60%'],
                                    data:[
                                        {value:_num1, name:'普通会员'},
                                        {value:_num2, name:'铜牌会员'},
                                        {value:_num3, name:'银牌会员'},
                                        {value:_num4, name:'金牌会员'},
                                        {value:_num5, name:'白金会员'},
                                        {value:_num6, name:'钻一'},
                                        {value:_num7, name:'钻二'},
                                        {value:_num8, name:'非会员'}
                                    ],
                                    itemStyle:{
                                        normal:{
                                            label:{
                                                show: true,
                                                formatter: '{d}%'
                                            },
                                            labelLine :{show:true}
                                        }
                                    }
                                }
                            ]
                        };
                        _routeChart2 = echarts.init(document.getElementById('lujingChart2'));
                        _routeChart2.showLoading({  //载入动画
                            text : '请稍等',
                            effect : 'whirling',
                            textStyle : {
                                fontSize : 15
                            }
                        });
                        clearTimeout(luJIngLoading2);
                        luJIngLoading2 = setTimeout(function (){
                            _routeChart2.hideLoading();
                            _routeChart2.setOption(option2);
                        },2000);
                    }
                });
                window.onresize=function(){
                    _routeChart1.resize();
                    _routeChart2.resize();
                };
            });
        }
    });
}
/**
 *修改状态
 */
function changeState(obj){
    var _this=$(obj),_state=_this.text(),_newState,_code;
    if(_state=='失效'){
        _newState='生效';
        _code=0;
    }else{
        _newState='失效';
        _code=1;
    }
    setTimeout(function(){
        if(confirm('确认修改状态为 '+_state+' 吗?')){  //confirm和alert一样会阻止js执行，这里加了setTimeout是为了先让tr变色，再弹出对话框
            var route_id=_this.parent().siblings().eq(0).text();
            $.ajax({
                url:SLSCHTTP+'/keyroute/modifyState',
                type:'post',
                data:{route_id:route_id,state:_code},
                dataType:'json',
                success:function(response){
                    if(response.state==true){
                        _this.text(_newState);
                        _this.parent().prev().text(_state);
                    }
                }
            });
        }
    },100);
}
/**
 *删除路径
 */
function delRoute(obj){
//    if (window.event) {
//        event.cancelBubble = true;
//    }else if (event){
//        event.stopPropagation();
//    }
    var _this=$(obj),
        _routeId=_this.parent().siblings().eq(0).text(),
        _tbody,
        _tr;
    setTimeout(function(){
        if(confirm('确认删除该路径吗?')){
            $.ajax({
                url:SLSCHTTP+'/keyroute/delRoutes',
                type:'post',
                data:{route_id:_routeId},
                dataType:'json',
                success:function(response){
                    if(response.state==true){
                        _this.parent().parent().remove();
                        _tbody=$('#path-table').find('tbody');
                        _tr=_tbody.find('tr');
                        if(_tr.length==0){
                            _tbody.html('<tr><td colspan="4">暂无数据</td></tr>');
                        }
                        $('.node-box').css({display:'none'});
                    }
                }
            });
        }
    },100);
}
/**
 *分页
 */
function changePage(){
//  上一页
    $('.page-change').find('a').eq(0).on('click',function(){
        $('.page-change').find('input').val('');
        var _nowPage=parseInt($('#newPage').text()),
            _totalPage=parseInt($('#totalPage').text()),
            _name= $.trim($('#search-content').val());
        if(_nowPage==1){
            alert("已经是第一页了");
            return false;
        }else{
            _nowPage--;
            $('#newPage').text(_nowPage);
            $.ajax({
                url:SLSCHTTP+'/keyroute/searchRoutes',
                type:'post',
                data:{name:_name,pageNumber:_nowPage},
                dataType:'json',
                success:function(response){
                    if(response.data.length!=0){
                        var _list='',_i,_len=response.data.length,_state,_newState;
                        for(_i=0;_i<_len;_i++){
                            if(response.data[_i]['route_state']==true){
                                _state='生效';
                                _newState='失效';
                            }else{
                                _state='失效';
                                _newState='生效';
                            }
                            _list+='<tr onclick="getNodes(this);">' +
                                '<td>'+response.data[_i]['route_id']+'</td>' +
                                '<td>'+response.data[_i]['route_name']+'</td>' +
                                '<td>'+_state+'</td>' +
                                '<td>' +
                                '<a class="btn-word" href="../html/jichu-change.html?&id='+response.data[_i]['route_id']+'">修改</a>' +
                                '<span class="btn-word" onclick="changeState(this);">'+_newState+'</span>' +
                                '<span class="btn-word" style="color: red;" onclick="delRoute(this);">删除</span>' +
                                '</td>' +
                                '</tr>';
                        }
                    }else{
                        _list=' <tr><td colspan="4">暂无数据</td></tr>';
                    }
                    $('#path-table').find('tbody').html(_list);
                    $('#totalPage').text(response.data[0].total);
                }
            });
            $('.node-box').css({display:'none'});
        }
    });
//  下一页
    $('.page-change').find('a').eq(1).on('click',function(){
        $('.page-change').find('input').val('');
        var _nowPage=parseInt($('#newPage').text()),
            _totalPage=parseInt($('#totalPage').text()),
            _name= $.trim($('#search-content').val());
        if(_nowPage==_totalPage){
            alert("已到达最后一页");
            return false;
        }else{
            _nowPage++;
            $('#newPage').text(_nowPage);
            $.ajax({
                url:SLSCHTTP+'/keyroute/searchRoutes',
                type:'post',
                data:{name:_name,pageNumber:_nowPage},
                dataType:'json',
                success:function(response){
                    if(response.data.length!=0){
                        var _list='',_i,_len=response.data.length,_state,_newState;
                        for(_i=0;_i<_len;_i++){
                            if(response.data[_i]['route_state']==true){
                                _state='生效';
                                _newState='失效';
                            }else{
                                _state='失效';
                                _newState='生效';
                            }
                            _list+='<tr onclick="getNodes(this);">' +
                                '<td>'+response.data[_i]['route_id']+'</td>' +
                                '<td>'+response.data[_i]['route_name']+'</td>' +
                                '<td>'+_state+'</td>' +
                                '<td>' +
                                '<a class="btn-word" href="../html/jichu-change.html?&id='+response.data[_i]['route_id']+'">修改</a>' +
                                '<span class="btn-word" onclick="changeState(this);">'+_newState+'</span>' +
                                '<span class="btn-word" style="color: red;" onclick="delRoute(this);">删除</span>' +
                                '</td>' +
                                '</tr>';
                        }
                    }else{
                        _list=' <tr><td colspan="4">暂无数据</td></tr>';
                    }
                    $('#path-table').find('tbody').html(_list);
                    $('#totalPage').text(response.data[0].total);
                }
            });
            $('.node-box').css({display:'none'});
        }
    });
//  跳页
    $('.page-change').find('a').eq(2).on('click',function(){
        var _nowPage=parseInt($('#goPage').val()),
            _totalPage=parseInt($('#totalPage').text()),
            _name= $.trim($('#search-content').val());
        if(_nowPage > _totalPage){
            alert("最大页数是"+_totalPage+"，不能超过最大页数");
            return false;
        }else if(_nowPage>0 && (/^\d+$/.test(_nowPage))){
            $('#newPage').text(_nowPage);
            $.ajax({
                url:SLSCHTTP+'/keyroute/searchRoutes',
                type:'post',
                data:{name:_name,pageNumber:_nowPage},
                dataType:'json',
                success:function(response){
                    if(response.data.length!=0){
                        var _list='',_i,_len=response.data.length,_state,_newState;
                        for(_i=0;_i<_len;_i++){
                            if(response.data[_i]['route_state']==true){
                                _state='生效';
                                _newState='失效';
                            }else{
                                _state='失效';
                                _newState='生效';
                            }
                            _list+='<tr onclick="getNodes(this);">' +
                                '<td>'+response.data[_i]['route_id']+'</td>' +
                                '<td>'+response.data[_i]['route_name']+'</td>' +
                                '<td>'+_state+'</td>' +
                                '<td>' +
                                '<a class="btn-word" href="../html/jichu-change.html?&id='+response.data[_i]['route_id']+'">修改</a>' +
                                '<span class="btn-word" onclick="changeState(this);">'+_newState+'</span>' +
                                '<span class="btn-word" style="color: red;" onclick="delRoute(this);">删除</span>' +
                                '</td>' +
                                '</tr>';
                        }
                    }else{
                        _list=' <tr><td colspan="4">暂无数据</td></tr>';
                    }
                    $('#path-table').find('tbody').html(_list);
                    $('#totalPage').text(response.data[0].total);
                }
            });
            $('.node-box').css({display:'none'});
        }else{
            alert("请输入大于0的整数");
        }
    });
}