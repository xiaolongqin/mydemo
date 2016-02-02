/**
 *初始化
 */
var _yewuChart1,_yewuChart2,_yewuChart3,_yewuChart4;
$(function(){
//     日历初始化
    //日历
    var _time=new Date();
    var _start=_time.getTime()-365*24*3600*1000;
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
    _yewuChart1 = echarts.init(document.getElementById('yewuChart1'));
    _yewuChart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart2 = echarts.init(document.getElementById('yewuChart2'));
    _yewuChart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart3 = echarts.init(document.getElementById('yewuChart3'));
    _yewuChart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
    _yewuChart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.when(
        $.ajax({  //饼图1
            url:SLSCHTTP+'/monitor/getSaveRate',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:1}
        }),
        $.ajax({  //饼图2
            url:SLSCHTTP+'/monitor/getTransRate',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:1}
        }),
        $.ajax({  //柱状图
            url:SLSCHTTP+'/monitor/getPuAndUv',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:1}
        }),
        $.ajax({  //折线图
            url:SLSCHTTP+'/monitor/getLineChart',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:1,pv_name:'1'}
        }),
        $.ajax({  //总数
            url:SLSCHTTP+'/monitor/getThreeTotal',
            type:'post',
            dataType:'json',
            data:{}
        })
    ).done(function(response1,response2,response3,response4,response5){
//          饼图1
            var option1,_total1= 0,_result1= 0,_totalNum1= 0,_allJson1=[],_legend1=[],_len1=response1[0].data.length,_i1,loadingTicket1;
            for(_i1=0;_i1<_len1-1;_i1++){
                _legend1.push(response1[0].data[_i1].app_channel);
                _allJson1.push({value:response1[0].data[_i1]['countNum3'],name:response1[0].data[_i1].app_channel,number:response1[0].data[_i1]['result']});
                _total1+=parseInt(response1[0].data[_i1]['countNum3']);
//                _result1+=parseInt(response1[0].data[_i1]['result']);
            }
            _result1=parseFloat(response1[0].data[_len1-1]['total_result']);
            _totalNum1=parseInt(response1[0].data[_len1-1]['countNum4']);
            option1 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n保存率(总): '+_result1+'%\n保存量(下载用户数总)(总): '+_total1+'户'+'      到达用户数(总): '+_totalNum1+'户',
                    x: 'center',
                    y: 'bottom',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 12,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    x : 'center',
                    y : 'top',
                    data: _legend1
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+':&nbsp;'+params.data.value+'户&nbsp;('+params.data.number+'%)'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '30%',
                        center: ['50%', '70%'],
                        minAngle:10,
                        data: _allJson1,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter:function (params) {
                                        return params.data.number+'%'
                                    }
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _yewuChart1 = echarts.init(document.getElementById('yewuChart1'));
//            _yewuChart1.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket1);
            loadingTicket1 = setTimeout(function (){
                _yewuChart1.hideLoading();
                _yewuChart1.setOption(option1);
            },2000);
//          饼图2
            var _total2=0,_result2= 0,_totalNum2= 0,_allJson2=[],option2,_legend2=[],_i2,_length2=response2[0].data.length,loadingTicket2;
            for(_i2=0;_i2<_length2-1;_i2++){
                _legend2.push(response2[0].data[_i2].app_channel);
                _allJson2.push({value:response2[0].data[_i2].countNum1,name:response2[0].data[_i2].app_channel,number:response2[0].data[_i2]['result']});
                _total2+=response2[0].data[_i2].countNum1;
//                _result2+=parseInt(response2[0].data[_i2]['result']);
            }
            _result2=parseFloat(response2[0].data[_length2-1]['total_result']);
            _totalNum2=parseInt(response2[0].data[_length2-1]['countNum0']);
            option2 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n转化率(总): '+_result2+'%\n到达量(到达用户数总): '+_total2+'户'+'      推广量(总目标用户数): '+_totalNum2+'户',
                    x: 'center',
                    y: 'bottom',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 12,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    x : 'center',
                    y : 'top',
                    data: _legend1
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+':&nbsp;'+params.data.value+'户&nbsp;('+params.data.number+'%)'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '30%',
                        center: ['50%', '70%'],
                        minAngle:10,
                        data: _allJson2,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter:function (params) {
                                        return params.data.number+'%'
                                    }
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _yewuChart2 = echarts.init(document.getElementById('yewuChart2'));
//            _yewuChart2.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket2);
            loadingTicket2 = setTimeout(function (){
                _yewuChart2.hideLoading();
                _yewuChart2.setOption(option2);
            },2000);
//          柱状图
            var _len3=response3[0].data.length,_i3,_x=[],_avgPv=[],_totalPv=[],_avgUv=[],_totalUv=[],option3,loadingTicket3;
            for(_i3=0;_i3<_len3;_i3++){
                _x.push(response3[0].data[_i3].app_channel+'('+response3[0].data[_i3].prod_name+')');
                _avgPv.push(response3[0].data[_i3].channel_avg_pv);
                _totalPv.push(response3[0].data[_i3].channel_sum_pv);
                _avgUv.push(response3[0].data[_i3].channel_avg_uv);
                _totalUv.push(response3[0].data[_i3].channel_sum_uv);
            }
            if(response3[0].data[0]){
                $('#time-title').text('时间： '+response3[0].data[0].startTime+'-'+response3[0].data[0].endTime);
            }
            option3 = {
                color :['#da7982','#ffb981','#57b1ed','#b6a2e1'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                legend: {
                    data: ['平均PV','总PV','平均UV','总UV']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    y: 'center',
                    feature: {
                        magicType: {show: true, type: ['tiled','stack']},
                        restore : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: _x
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '单位：户',
                        splitArea: {show: true}
                    }
                ],
                series: [
                    {
                        name: '平均PV',    //如加上stack:'总量'，则默认为堆积图
                        type: 'bar',
                        barMinHeight:5,
                        data: _avgPv
                    },
                    {
                        name: '总PV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _totalPv
                    },
                    {
                        name: '平均UV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _avgUv
                    },
                    {
                        name: '总UV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _totalUv
                    }
                ]
            };
//            _yewuChart3 = echarts.init(document.getElementById('yewuChart3'));
//            _yewuChart3.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket3);
            loadingTicket3 = setTimeout(function (){
                _yewuChart3.hideLoading();
                _yewuChart3.setOption(option3);
            },2000);
//          折线图
            var option4,_i4,_len4,_time4=[],_obj,_name=[],_attr4,_json4=[],loadingTicket4;
            for(_attr4 in response4[0].data.first){
                var _data4=[];
                _name.push(_attr4);  //渠道名
                for(var _j4=0;_j4<response4[0].data.first[_attr4].length;_j4++){
                    _data4.push(response4[0].data.first[_attr4][_j4].data1);
                }
                _json4.push({
                    name: _attr4,
                    type: 'line',
                    data: _data4
                });
            }
            _obj=response4[0].data.first[_name[0]];
            _len4=_obj.length;
            for(_i4=0;_i4<_len4;_i4++){
                _time4.push(_obj[_i4].time);  //时间
            }
            option4 = {
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    data:_name
                },
                toolbox: {
                    show : true,
                    orient: 'vertical',
                    y: 'center',
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : _time4
                    }
                ],
                yAxis : [
                    {
                        name: '单位：户',
                        type : 'value'
                    }
                ],
                grid:{
                    y:100
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                series : _json4
            };
//            _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
//            _yewuChart4.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket4);
            loadingTicket4 = setTimeout(function (){
                _yewuChart4.hideLoading();
                _yewuChart4.setOption(option4);
            },2000);
//          table
            var _attrTable,_tableList;
            for(_attrTable in response4[0].data.second){
                _tableList+='<tr>' +
                    '<td>'+response4[0].data.second[_attrTable].day+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].todayData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].yesData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayChanges+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayGrowth+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayOnDayGrowth+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].weedData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].weekChanges+'</td>' +
                    '</tr>';
            }
            $('#yewuTable').find('tbody').html(_tableList);
//          总数
            var _totalList=$('.red-word').find('span');
            _totalList.eq(0).text(response5[0].data.userCount[0].count);
            _totalList.eq(1).text(response5[0].data.orderCount[0].count);
            _totalList.eq(2).text(response5[0].data.shopCount[0].count);

//          图表大小变化
            window.onresize = function () {
                _yewuChart1.resize();
                _yewuChart2.resize();
                _yewuChart3.resize();
                _yewuChart4.resize();
            };
    });
});
/**
 *详情
 */
function seeDetail(obj){
    var flag=$(obj).attr('flag');
    if(flag=='1'){
        $(obj).next().css({display:'block'});
        $(obj).attr('flag','0');
    }else{
        $(obj).next().css({display:'none'});
        $(obj).attr('flag','1');
    }
}
/**
 *详情分页
 */
function prevPages(){
    var _visible=$('.details-list').filter(':visible'),
        _index=parseInt(_visible.index());
    _visible.removeClass('details-active');
    _index-=1;
    if(_index<1){
        _index=6;
    }
    $('.details-list').eq(_index-1).addClass('details-active');
}
function nextPages(){
    var _visible=$('.details-list').filter(':visible'),
        _index=parseInt(_visible.index());
    _visible.removeClass('details-active');
    _index+=1;
    if(_index>6){
        _index=1;
    }
    $('.details-list').eq(_index-1).addClass('details-active');
}
/**
 *快速选择时间(和初始化函数一样，只是type改变而已)
 */
function quickTime(obj){
    $(obj).addClass('time-active').siblings().removeClass('time-active');
    $('#time-start').val('');  //日历清空
    $('#time-end').val('');
    $('#client-tab').find('li').eq(0).addClass('list-active').siblings().removeClass('list-active');  //选项重置
    $('#yewu-tab').find('li').eq(0).addClass('list-active').siblings().removeClass('list-active');
    var _type=$(obj).index();
    switch (parseInt(_type)){
        case 0:_type=1;break;
        case 1:_type=3;break;
        case 2:_type=6;break;
        case 3:_type=4;break;
        case 4:_type=5;break;
    }
    _yewuChart1 = echarts.init(document.getElementById('yewuChart1'));
    _yewuChart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart2 = echarts.init(document.getElementById('yewuChart2'));
    _yewuChart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart3 = echarts.init(document.getElementById('yewuChart3'));
    _yewuChart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
    _yewuChart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.when(
        $.ajax({  //饼图1
            url:SLSCHTTP+'/monitor/getSaveRate',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:_type}
        }),
        $.ajax({  //饼图2
            url:SLSCHTTP+'/monitor/getTransRate',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:_type}
        }),
        $.ajax({  //柱状图
            url:SLSCHTTP+'/monitor/getPuAndUv',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:_type}
        }),
        $.ajax({  //折线图
            url:SLSCHTTP+'/monitor/getLineChart',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:_type,pv_name:'1'}
        })
    ).done(function(response1,response2,response3,response4){
//          饼图1
            var option1,_total1= 0,_result1= 0,_totalNum1= 0,_allJson1=[],_legend1=[],_len1=response1[0].data.length,_i1,loadingTicket1;
            for(_i1=0;_i1<_len1-1;_i1++){
                _legend1.push(response1[0].data[_i1].app_channel);
                _allJson1.push({value:response1[0].data[_i1]['countNum3'],name:response1[0].data[_i1].app_channel,number:response1[0].data[_i1]['result']});
                _total1+=parseInt(response1[0].data[_i1]['countNum3']);
//                _result1+=parseInt(response1[0].data[_i1]['result']);
            }
            _result1=parseFloat(response1[0].data[_len1-1]['total_result']);
            _totalNum1=parseInt(response1[0].data[_len1-1]['countNum4']);
            option1 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n保存率(总): '+_result1+'%\n保存量(下载用户数总)(总): '+_total1+'户'+'      到达用户数(总): '+_totalNum1+'户',
                    x: 'center',
                    y: 'bottom',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 12,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    x : 'center',
                    y : 'top',
                    data: _legend1
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+':&nbsp;'+params.data.value+'户&nbsp;('+params.data.number+'%)'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '30%',
                        center: ['50%', '70%'],
                        minAngle:10,
                        data: _allJson1,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter:function (params) {
                                        return params.data.number+'%'
                                    }
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _yewuChart1 = echarts.init(document.getElementById('yewuChart1'));
//            _yewuChart1.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket1);
            loadingTicket1 = setTimeout(function (){
                _yewuChart1.hideLoading();
                _yewuChart1.setOption(option1);
            },2000);
//          饼图2
            var _total2=0,_result2= 0,_totalNum2= 0,_allJson2=[],option2,_legend2=[],_i2,_length2=response2[0].data.length,loadingTicket2;
            for(_i2=0;_i2<_length2-1;_i2++){
                _legend2.push(response2[0].data[_i2].app_channel);
                _allJson2.push({value:response2[0].data[_i2].countNum1,name:response2[0].data[_i2].app_channel,number:response2[0].data[_i2]['result']});
                _total2+=response2[0].data[_i2].countNum1;
//                _result2+=parseInt(response2[0].data[_i2]['result']);
            }
            _result2=parseFloat(response2[0].data[_length2-1]['total_result']);
            _totalNum2=parseInt(response2[0].data[_length2-1]['countNum0']);
            option2 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n转化率(总): '+_result2+'%\n到达量(到达用户数总): '+_total2+'户'+'      推广量(总目标用户数): '+_totalNum2+'户',
                    x: 'center',
                    y: 'bottom',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 12,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    x : 'center',
                    y : 'top',
                    data: _legend1
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+':&nbsp;'+params.data.value+'户&nbsp;('+params.data.number+'%)'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '30%',
                        center: ['50%', '70%'],
                        minAngle:10,
                        data: _allJson2,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter:function (params) {
                                        return params.data.number+'%'
                                    }
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _yewuChart2 = echarts.init(document.getElementById('yewuChart2'));
//            _yewuChart2.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket2);
            loadingTicket2 = setTimeout(function (){
                _yewuChart2.hideLoading();
                _yewuChart2.setOption(option2);
            },2000);
//          柱状图
            var _len3=response3[0].data.length,_i3,_x=[],_avgPv=[],_totalPv=[],_avgUv=[],_totalUv=[],option3,loadingTicket3;
            for(_i3=0;_i3<_len3;_i3++){
                _x.push(response3[0].data[_i3].app_channel+'('+response3[0].data[_i3].prod_name+')');
                _avgPv.push(response3[0].data[_i3].channel_avg_pv);
                _totalPv.push(response3[0].data[_i3].channel_sum_pv);
                _avgUv.push(response3[0].data[_i3].channel_avg_uv);
                _totalUv.push(response3[0].data[_i3].channel_sum_uv);
            }
            if(response3[0].data[0]){
                $('#time-title').text('时间： '+response3[0].data[0].startTime+'-'+response3[0].data[0].endTime);
            }
            option3 = {
                color :['#da7982','#ffb981','#57b1ed','#b6a2e1'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                legend: {
                    data: ['平均PV','总PV','平均UV','总UV']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    y: 'center',
                    feature: {
                        magicType: {show: true, type: ['tiled','stack']},
                        restore : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: _x
                    }
                ],
                yAxis: [
                    {
                        name: '单位：户',
                        type: 'value',
                        splitArea: {show: true}
                    }
                ],
                series: [
                    {
                        name: '平均PV',    //如加上stack:'总量'，则默认为堆积图
                        type: 'bar',
                        barMinHeight:5,
                        data: _avgPv
                    },
                    {
                        name: '总PV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _totalPv
                    },
                    {
                        name: '平均UV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _avgUv
                    },
                    {
                        name: '总UV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _totalUv
                    }
                ]
            };
//            _yewuChart3 = echarts.init(document.getElementById('yewuChart3'));
//            _yewuChart3.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket3);
            loadingTicket3 = setTimeout(function (){
                _yewuChart3.hideLoading();
                _yewuChart3.setOption(option3);
            },2000);
//          折线图
            var option4,_i4,_len4,_time4=[],_obj,_name=[],_attr4,_json4=[],loadingTicket4;
            for(_attr4 in response4[0].data.first){
                var _data4=[];
                _name.push(_attr4);  //渠道名
                for(var _j4=0;_j4<response4[0].data.first[_attr4].length;_j4++){
                    _data4.push(response4[0].data.first[_attr4][_j4].data1);
                }
                _json4.push({
                    name: _attr4,
                    type: 'line',
                    data: _data4
                });
            }
            _obj=response4[0].data.first[_name[0]];
            _len4=_obj.length;
            for(_i4=0;_i4<_len4;_i4++){
                _time4.push(_obj[_i4].time);  //时间
            }
            option4 = {
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    data:_name
                },
                toolbox: {
                    show : true,
                    orient: 'vertical',
                    y: 'center',
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : _time4
                    }
                ],
                yAxis : [
                    {
                        name: '单位：户',
                        type : 'value'
                    }
                ],
                grid:{
                    y:100
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                series : _json4
            };
//            _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
//            _yewuChart4.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket4);
            loadingTicket4 = setTimeout(function (){
                _yewuChart4.hideLoading();
                _yewuChart4.setOption(option4);
            },2000);
//          table
            var _attrTable,_tableList;
            for(_attrTable in response4[0].data.second){
                _tableList+='<tr>' +
                    '<td>'+response4[0].data.second[_attrTable].day+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].todayData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].yesData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayChanges+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayGrowth+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayOnDayGrowth+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].weedData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].weekChanges+'</td>' +
                    '</tr>';
            }
            $('#yewuTable').find('tbody').html(_tableList);
        });
}
/**
 *自定义时间(和初始化函数一样，只是type改变而已)
 */
function myTime(){
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
        $('.time-list').find('li').removeClass('time-active');  //快速选择时间重置
        $('#client-tab').find('li').eq(0).addClass('list-active').siblings().removeClass('list-active');  //选项重置
        $('#yewu-tab').find('li').eq(0).addClass('list-active').siblings().removeClass('list-active');
    }else{
        alert('请选择正确的时间段');
        return false;
    }
    _yewuChart1 = echarts.init(document.getElementById('yewuChart1'));
    _yewuChart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart2 = echarts.init(document.getElementById('yewuChart2'));
    _yewuChart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart3 = echarts.init(document.getElementById('yewuChart3'));
    _yewuChart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
    _yewuChart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    var _startTime=date1.split('-').join(''),
        _endTime=date2.split('-').join('');
    $.when(
        $.ajax({  //饼图1
            url:SLSCHTTP+'/monitor/getSaveRate',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:0,startTime:_startTime,endTime:_endTime}
        }),
        $.ajax({  //饼图2
            url:SLSCHTTP+'/monitor/getTransRate',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:0,startTime:_startTime,endTime:_endTime}
        }),
        $.ajax({  //柱状图
            url:SLSCHTTP+'/monitor/getPuAndUv',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',type:0,startTime:_startTime,endTime:_endTime}
        }),
        $.ajax({  //折线图
            url:SLSCHTTP+'/monitor/getLineChart',
            type:'post',
            dataType:'json',
            data:{prod_name:'全部',pv_name:'1',type:0,startTime:_startTime,endTime:_endTime}
        })
    ).done(function(response1,response2,response3,response4){
//          饼图1
            var option1,_total1= 0,_result1= 0,_totalNum1= 0,_allJson1=[],_legend1=[],_len1=response1[0].data.length,_i1,loadingTicket1;
            for(_i1=0;_i1<_len1-1;_i1++){
                _legend1.push(response1[0].data[_i1].app_channel);
                _allJson1.push({value:response1[0].data[_i1]['countNum3'],name:response1[0].data[_i1].app_channel,number:response1[0].data[_i1]['result']});
                _total1+=parseInt(response1[0].data[_i1]['countNum3']);
//                _result1+=parseInt(response1[0].data[_i1]['result']);
            }
            _result1=parseFloat(response1[0].data[_len1-1]['total_result']);
            _totalNum1=parseInt(response1[0].data[_len1-1]['countNum4']);
            option1 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n保存率(总): '+_result1+'%\n保存量(下载用户数总)(总): '+_total1+'户'+'      到达用户数(总):'+_totalNum1+'户',
                    x: 'center',
                    y: 'bottom',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 12,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    x : 'center',
                    y : 'top',
                    data: _legend1
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+':&nbsp;'+params.data.value+'户&nbsp;('+params.data.number+'%)'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '30%',
                        center: ['50%', '70%'],
                        minAngle:10,
                        data: _allJson1,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter:function (params) {
                                        return params.data.number+'%'
                                    }
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _yewuChart1 = echarts.init(document.getElementById('yewuChart1'));
//            _yewuChart1.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket1);
            loadingTicket1 = setTimeout(function (){
                _yewuChart1.hideLoading();
                _yewuChart1.setOption(option1);
            },2000);
//          饼图2
            var _total2=0,_result2= 0,_totalNum2= 0,_allJson2=[],option2,_legend2=[],_i2,_length2=response2[0].data.length,loadingTicket2;
            for(_i2=0;_i2<_length2-1;_i2++){
                _legend2.push(response2[0].data[_i2].app_channel);
                _allJson2.push({value:response2[0].data[_i2].countNum1,name:response2[0].data[_i2].app_channel,number:response2[0].data[_i2]['result']});
                _total2+=response2[0].data[_i2].countNum1;
//                _result2+=parseInt(response2[0].data[_i2]['result']);
            }
            _result2=parseFloat(response2[0].data[_length2-1]['total_result']);
            _totalNum2=parseInt(response2[0].data[_length2-1]['countNum0']);
            option2 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n转化率(总): '+_result2+'%\n到达量(到达用户数总): '+_total2+'户'+'      推广量(总目标用户数)：'+_totalNum2+'户',
                    x: 'center',
                    y: 'bottom',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 12,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    x : 'center',
                    y : 'top',
                    data: _legend1
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+':&nbsp;'+params.data.value+'户&nbsp;('+params.data.number+'%)'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '30%',
                        center: ['50%', '70%'],
                        minAngle:10,
                        data: _allJson2,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter:function (params) {
                                        return params.data.number+'%'
                                    }
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _yewuChart2 = echarts.init(document.getElementById('yewuChart2'));
//            _yewuChart2.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket2);
            loadingTicket2 = setTimeout(function (){
                _yewuChart2.hideLoading();
                _yewuChart2.setOption(option2);
            },2000);
//          柱状图
            var _len3=response3[0].data.length,_i3,_x=[],_avgPv=[],_totalPv=[],_avgUv=[],_totalUv=[],option3,loadingTicket3;
            for(_i3=0;_i3<_len3;_i3++){
                _x.push(response3[0].data[_i3].app_channel+'('+response3[0].data[_i3].prod_name+')');
                _avgPv.push(response3[0].data[_i3].channel_avg_pv);
                _totalPv.push(response3[0].data[_i3].channel_sum_pv);
                _avgUv.push(response3[0].data[_i3].channel_avg_uv);
                _totalUv.push(response3[0].data[_i3].channel_sum_uv);
            }
            if(response3[0].data[0]){
                $('#time-title').text('时间： '+response3[0].data[0].startTime+'-'+response3[0].data[0].endTime);
            }
            option3 = {
                color :['#da7982','#ffb981','#57b1ed','#b6a2e1'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                legend: {
                    data: ['平均PV','总PV','平均UV','总UV']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    y: 'center',
                    feature: {
                        magicType: {show: true, type: ['tiled','stack']},
                        restore : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: _x
                    }
                ],
                yAxis: [
                    {
                        name: '单位：户',
                        type: 'value',
                        splitArea: {show: true}
                    }
                ],
                series: [
                    {
                        name: '平均PV',    //如加上stack:'总量'，则默认为堆积图
                        type: 'bar',
                        barMinHeight:5,
                        data: _avgPv
                    },
                    {
                        name: '总PV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _totalPv
                    },
                    {
                        name: '平均UV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _avgUv
                    },
                    {
                        name: '总UV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _totalUv
                    }
                ]
            };
//            _yewuChart3 = echarts.init(document.getElementById('yewuChart3'));
//            _yewuChart3.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket3);
            loadingTicket3 = setTimeout(function (){
                _yewuChart3.hideLoading();
                _yewuChart3.setOption(option3);
            },2000);
//          折线图
            var option4,_i4,_len4,_time4=[],_obj,_name=[],_attr4,_json4=[],loadingTicket4;
            for(_attr4 in response4[0].data.first){
                var _data4=[];
                _name.push(_attr4);  //渠道名
                for(var _j4=0;_j4<response4[0].data.first[_attr4].length;_j4++){
                    _data4.push(response4[0].data.first[_attr4][_j4].data1);
                }
                _json4.push({
                    name: _attr4,
                    type: 'line',
                    data: _data4
                });
            }
            _obj=response4[0].data.first[_name[0]];
            _len4=_obj.length;
            for(_i4=0;_i4<_len4;_i4++){
                _time4.push(_obj[_i4].time);  //时间
            }
            option4 = {
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    data:_name
                },
                toolbox: {
                    show : true,
                    orient: 'vertical',
                    y: 'center',
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : _time4
                    }
                ],
                yAxis : [
                    {
                        name: '单位：户',
                        type : 'value'
                    }
                ],
                grid:{
                    y:100
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                series : _json4
            };
//            _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
//            _yewuChart4.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket4);
            loadingTicket4 = setTimeout(function (){
                _yewuChart4.hideLoading();
                _yewuChart4.setOption(option4);
            },2000);
//          table
            var _attrTable,_tableList;
            for(_attrTable in response4[0].data.second){
                _tableList+='<tr>' +
                    '<td>'+response4[0].data.second[_attrTable].day+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].todayData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].yesData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayChanges+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayGrowth+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayOnDayGrowth+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].weedData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].weekChanges+'</td>' +
                    '</tr>';
            }
            $('#yewuTable').find('tbody').html(_tableList);
    });
}
/**
 *饼图,柱状图和折线图切换
 */
function fourTab(obj){
    $(obj).addClass('list-active').siblings('').removeClass('list-active');
    $('#yewu-tab').find('li').eq(0).addClass('list-active').siblings().removeClass('list-active');  //折线图选项重置
    var _name=$(obj).text(),
        _index=$('.time-list').find('.time-active').index(),
        _startTime,
        _endTime,
        _time,
        _time2;
    if(_index<0){  //取的是自定义时间
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
//            if(time>90){
//                alert('您的时间跨度为'+time+'天，时间跨度超过90天');
//                return false;
//            }
        }else{
            alert('请选择正确的时间段');
            return false;
        }
        _startTime=date1.split('-').join('');
        _endTime=date2.split('-').join('');
        _time={prod_name:_name,type:0,startTime:_startTime,endTime:_endTime};
        _time2={prod_name:_name,pv_name:1,type:0,startTime:_startTime,endTime:_endTime};
    }else{
        switch (parseInt(_index)){
            case 0:_index=1;break;
            case 1:_index=3;break;
            case 2:_index=6;break;
            case 3:_index=4;break;
            case 4:_index=5;break;
        }
        _time={prod_name:_name,type:_index};
        _time2={prod_name:_name,pv_name:1,type:_index};
    }
    _yewuChart1 = echarts.init(document.getElementById('yewuChart1'));
    _yewuChart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart2 = echarts.init(document.getElementById('yewuChart2'));
    _yewuChart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart3 = echarts.init(document.getElementById('yewuChart3'));
    _yewuChart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
    _yewuChart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.when(
        $.ajax({  //饼图1
            url:SLSCHTTP+'/monitor/getSaveRate',
            type:'post',
            dataType:'json',
            data:_time
        }),
        $.ajax({  //饼图2
            url:SLSCHTTP+'/monitor/getTransRate',
            type:'post',
            dataType:'json',
            data:_time
        }),
        $.ajax({  //柱状图
            url:SLSCHTTP+'/monitor/getPuAndUv',
            type:'post',
            dataType:'json',
            data:_time
        })
        ,
        $.ajax({  //折线图
            url:SLSCHTTP+'/monitor/getLineChart',
            type:'post',
            dataType:'json',
            data:_time2
        })
    ).done(
        function(response1,response2,response3,response4){
//          饼图1
            var option1,_total1= 0,_result1= 0,_totalNum1= 0,_allJson1=[],_legend1=[],_len1=response1[0].data.length,_i1,loadingTicket1;
            for(_i1=0;_i1<_len1-1;_i1++){
                _legend1.push(response1[0].data[_i1].app_channel);
                _allJson1.push({value:response1[0].data[_i1]['countNum3'],name:response1[0].data[_i1].app_channel,number:response1[0].data[_i1]['result']});
                _total1+=parseInt(response1[0].data[_i1]['countNum3']);
//                _result1+=parseInt(response1[0].data[_i1]['result']);
            }
            _result1=parseFloat(response1[0].data[_len1-1]['total_result']);
            _totalNum1=parseInt(response1[0].data[_len1-1]['countNum4']);
            option1 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n保存率(总): '+_result1+'%\n保存量(下载用户数总)(总): '+_total1+'户'+'      到达用户数(总): '+_totalNum1+'户',
                    x: 'center',
                    y: 'bottom',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 12,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    x : 'center',
                    y : 'top',
                    data: _legend1
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+':&nbsp;'+params.data.value+'户&nbsp;('+params.data.number+'%)'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '30%',
                        center: ['50%', '70%'],
                        minAngle:10,
                        data: _allJson1,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter:function (params) {
                                        return params.data.number+'%'
                                    }
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _yewuChart1 = echarts.init(document.getElementById('yewuChart1'));
//            _yewuChart1.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket1);
            loadingTicket1 = setTimeout(function (){
                _yewuChart1.hideLoading();
                _yewuChart1.setOption(option1);
            },2000);
//          饼图2
            var _total2=0,_result2= 0,_totalNum2= 0,_allJson2=[],option2,_legend2=[],_i2,_length2=response2[0].data.length,loadingTicket2;
            for(_i2=0;_i2<_length2-1;_i2++){
                _legend2.push(response2[0].data[_i2].app_channel);
                _allJson2.push({value:response2[0].data[_i2].countNum1,name:response2[0].data[_i2].app_channel,number:response2[0].data[_i2]['result']});
                _total2+=response2[0].data[_i2].countNum1;
//                _result2+=parseInt(response2[0].data[_i2]['result']);
            }
            _result2=parseFloat(response2[0].data[_length2-1]['total_result']);
            _totalNum2=parseInt(response2[0].data[_length2-1]['countNum0']);
            option2 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n转化率(总): '+_result2+'%\n到达量(到达用户数总): '+_total2+'户'+'      推广量(总目标用户数): '+_totalNum2+'户',
                    x: 'center',
                    y: 'bottom',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    x : 'center',
                    y : 'top',
                    data: _legend1
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+':&nbsp;'+params.data.value+'户&nbsp;('+params.data.number+'%)'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '30%',
                        center: ['50%', '70%'],
                        minAngle:10,
                        data: _allJson2,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter:function (params) {
                                        return params.data.number+'%'
                                    }
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _yewuChart2 = echarts.init(document.getElementById('yewuChart2'));
//            _yewuChart2.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket2);
            loadingTicket2 = setTimeout(function (){
                _yewuChart2.hideLoading();
                _yewuChart2.setOption(option2);
            },2000);
//          柱状图
            var _len3=response3[0].data.length,_title,_i3,_x=[],_avgPv=[],_totalPv=[],_avgUv=[],_totalUv=[],option3,loadingTicket3;
            for(_i3=0;_i3<_len3;_i3++){
                _x.push(response3[0].data[_i3].app_channel+'('+response3[0].data[_i3].prod_name+')');
                _avgPv.push(response3[0].data[_i3].channel_avg_pv);
                _totalPv.push(response3[0].data[_i3].channel_sum_pv);
                _avgUv.push(response3[0].data[_i3].channel_avg_uv);
                _totalUv.push(response3[0].data[_i3].channel_sum_uv);
            }
            if(response3[0].data[0]){
                _title='时间： '+response3[0].data[0].startTime+'-'+response3[0].data[0].endTime;
            }
            option3 = {
                color :['#da7982','#ffb981','#57b1ed','#b6a2e1'],
                title : {
                    text: _title,
                    x: 'left',
                    y: 'top',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                legend: {
                    data: ['平均PV','总PV','平均UV','总UV']
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    y: 'center',
                    feature: {
                        magicType: {show: true, type: ['tiled','stack']},
                        restore : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: _x
                    }
                ],
                yAxis: [
                    {
                        name: '单位：户',
                        type: 'value',
                        splitArea: {show: true}
                    }
                ],
                series: [
                    {
                        name: '平均PV',    //如加上stack:'总量'，则默认为堆积图
                        type: 'bar',
                        barMinHeight:5,
                        data: _avgPv
                    },
                    {
                        name: '总PV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _totalPv
                    },
                    {
                        name: '平均UV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _avgUv
                    },
                    {
                        name: '总UV',
                        type: 'bar',
                        barMinHeight:5,
                        data: _totalUv
                    }
                ]
            };
//            _yewuChart3 = echarts.init(document.getElementById('yewuChart3'));
//            _yewuChart3.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket3);
            loadingTicket3 = setTimeout(function (){
                _yewuChart3.hideLoading();
                _yewuChart3.setOption(option3);
            },2000);
//          折线图
            var option4,_i4,_len4,_time4=[],_obj,_name=[],_attr4,_json4=[],loadingTicket4;
            for(_attr4 in response4[0].data.first){
                var _data4=[];
                _name.push(_attr4);  //渠道名
                for(var _j4=0;_j4<response4[0].data.first[_attr4].length;_j4++){
                    _data4.push(response4[0].data.first[_attr4][_j4].data1);
                }
                _json4.push({
                    name: _attr4,
                    type: 'line',
                    data: _data4
                });
            }
            _obj=response4[0].data.first[_name[0]];
            _len4=_obj.length;
            for(_i4=0;_i4<_len4;_i4++){
                _time4.push(_obj[_i4].time);  //时间
            }
            option4 = {
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    data:_name
                },
                toolbox: {
                    show : true,
                    orient: 'vertical',
                    y: 'center',
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : _time4
                    }
                ],
                yAxis : [
                    {
                        name: '单位：户',
                        type : 'value'
                    }
                ],
                grid:{
                    y:100
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                series : _json4
            };
//            _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
//            _yewuChart4.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket4);
            loadingTicket4 = setTimeout(function (){
                _yewuChart4.hideLoading();
                _yewuChart4.setOption(option4);
            },2000);
//          table
            var _attrTable,_tableList;
            for(_attrTable in response4[0].data.second){
                _tableList+='<tr>' +
                    '<td>'+response4[0].data.second[_attrTable].day+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].todayData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].yesData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayChanges+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayGrowth+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].dayOnDayGrowth+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].weedData+'</td>' +
                    '<td>'+response4[0].data.second[_attrTable].weekChanges+'</td>' +
                    '</tr>';
            }
            $('#yewuTable').find('tbody').html(_tableList);
        }
    );
}
/**
 *折线图和表格切换
 */
function oneTab(obj){
    $(obj).addClass('list-active').siblings().removeClass('list-active');
    var _name=$(obj).index(),
        _index=$('.time-list').find('.time-active').index(),
        _prodName=$('#client-tab').find('.list-active').text(),  //当前选择为哪个端
        _startTime,
        _endTime,
        _time;
    switch (parseInt(_name)){
        case 0:_name=1;break;
        case 1:_name=2;break;
        case 2:_name=3;break;
        case 3:_name=4;break;
        case 4:_name=5;break;
    }
    if(_index<0){  //取的是自定义时间
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
//            if(time>90){
//                alert('您的时间跨度为'+time+'天，时间跨度超过90天');
//                return false;
//            }
        }else{
            alert('请选择正确的时间段');
            return false;
        }
        _startTime=date1.split('-').join('');
        _endTime=date2.split('-').join('');
        _time={prod_name:_prodName,pv_name:_name,type:0,startTime:_startTime,endTime:_endTime};
    }else{
        switch (parseInt(_index)){
            case 0:_index=1;break;
            case 1:_index=3;break;
            case 2:_index=6;break;
            case 3:_index=4;break;
            case 4:_index=5;break;
        }
        _time={prod_name:_prodName,pv_name:_name,type:_index};
    }
    _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
    _yewuChart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.ajax({
        url:SLSCHTTP+'/monitor/getLineChart',
        type:'post',
        data:_time,
        dataType:'json',
        success:function(response){
//          折线图
            var option4,_i4,_len4,_time4=[],_obj,_name=[],_attr4,_json4=[],loadingTicket4;
            for(_attr4 in response.data.first){
                var _data4=[];
                _name.push(_attr4);  //渠道名
                for(var _j4=0;_j4<response.data.first[_attr4].length;_j4++){
                    _data4.push(response.data.first[_attr4][_j4].data1);
                }
                _json4.push({
                    name: _attr4,
                    type: 'line',
                    data: _data4
                });
            }
            _obj=response.data.first[_name[0]];
            _len4=_obj.length;
            for(_i4=0;_i4<_len4;_i4++){
                _time4.push(_obj[_i4].time);  //时间
            }
            option4 = {
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    data:_name
                },
                toolbox: {
                    show : true,
                    orient: 'vertical',
                    y: 'center',
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        boundaryGap : false,
                        data : _time4
                    }
                ],
                yAxis : [
                    {
                        name: '单位：户',
                        type : 'value'
                    }
                ],
                grid:{
                    y:100
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                series : _json4
            };
//            _yewuChart4 = echarts.init(document.getElementById('yewuChart4'));
//            _yewuChart4.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(loadingTicket4);
            loadingTicket4 = setTimeout(function (){
                _yewuChart4.hideLoading();
                _yewuChart4.setOption(option4);
            },2000);
//          table
            var _attrTable,_tableList;
            for(_attrTable in response.data.second){
                _tableList+='<tr>' +
                    '<td>'+response.data.second[_attrTable].day+'</td>' +
                    '<td>'+response.data.second[_attrTable].todayData+'</td>' +
                    '<td>'+response.data.second[_attrTable].yesData+'</td>' +
                    '<td>'+response.data.second[_attrTable].dayChanges+'</td>' +
                    '<td>'+response.data.second[_attrTable].dayGrowth+'</td>' +
                    '<td>'+response.data.second[_attrTable].dayOnDayGrowth+'</td>' +
                    '<td>'+response.data.second[_attrTable].weedData+'</td>' +
                    '<td>'+response.data.second[_attrTable].weekChanges+'</td>' +
                    '</tr>';
            }
            $('#yewuTable').find('tbody').html(_tableList);
        }
    });
}