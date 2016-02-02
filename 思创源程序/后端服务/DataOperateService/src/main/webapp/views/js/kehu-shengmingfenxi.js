var _keHuChart1,_keHuChart2,_keHuChart3,_keHuChart4,_keHuChart5,_keHuChart6,_keHuChart7,_keHuChart8,_keHuChart9;
/**
 *初始化
 */
$(function(){
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
});
/**
 *显示详情
 */
function seeDetails(obj){
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
function prevPage(){
    var index=$('.details-list').filter(':visible').index();
    $('.details-list').removeClass('details-active');
    index-=1;
    if(index<1){
        index=5;
    }
    $('.details-list').eq(index-1).addClass('details-active');
}
function nextPage(){
    var index=$('.details-list').filter(':visible').index();
    $('.details-list').removeClass('details-active');
    index+=1;
    if(index>5){
        index=1;
    }
    $('.details-list').eq(index-1).addClass('details-active');
}
/**
 *客户分析计算
 */
function calculate(){
    $('.calculate-box').css({display:'none'});
    $('.cal-content').css({display:'block'});
    _keHuChart1 = echarts.init(document.getElementById('fenxiChart1'));
    _keHuChart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart2 = echarts.init(document.getElementById('fenxiChart2'));
    _keHuChart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart3 = echarts.init(document.getElementById('fenxiChart3'));
    _keHuChart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart4 = echarts.init(document.getElementById('fenxiChart4'));
    _keHuChart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart5 = echarts.init(document.getElementById('fenxiChart5'));
    _keHuChart5.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart6 = echarts.init(document.getElementById('fenxiChart6'));
    _keHuChart6.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart7 = echarts.init(document.getElementById('fenxiChart7'));
    _keHuChart7.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart8 = echarts.init(document.getElementById('fenxiChart8'));
    _keHuChart8.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart9 = echarts.init(document.getElementById('fenxiChart9'));
    _keHuChart9.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.when(
        $.ajax({
            url:SLSCHTTP+'/lifecycle/getIndicatorPercent',  //指标分布
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({  //消费客户群体特征分析
            url:SLSCHTTP+'/lifecycle/getCustomerGroup',
            type:'post',
            data:{dateType:'1'},  //上线改为1
            dataType:'json'
        }),
        $.ajax({  //习惯分析
            url:SLSCHTTP+'/lifecycle/getCustHabit',
            type:'post',
            data:{dateType:'1',paramType:'1'},  //上线改为1
            dataType:'json'
        }),
        $.ajax({  //地域分析
            url:SLSCHTTP+'/lifecycle/getCustDistribute',
            type:'post',
            data:{dateType:'1',paramType:'1'},  //上线改为1
            dataType:'json'
        }),
        $.ajax({  //离网分析
            url:SLSCHTTP+'/lifecycle/getCustPredict',
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({  //TOP5购买品种
            url:SLSCHTTP+'/lifecycle/getTop5Brand',
            type:'post',
            data:{dateType:'1'},
            dataType:'json'
        }),
        $.ajax({  //客户生日图
            url:SLSCHTTP+'/lifecycle/getBirthBless',
            type:'post',
            data:{birthType:'1'},
            dataType:'json'
        })
    ).done(function(response1,response2,response3,response4,response5,response6,response7){
//          指标分布图
            var _keHuData1=[],option1,_attr1,keHuLoading1;
            for(_attr1 in response1[0].data){
                switch (_attr1){
                    case '1': _keHuData1.unshift({name:'开拓期',value:response1[0].data[_attr1][0].percent==null?0:response1[0].data[_attr1][0].percent,number:response1[0].data[_attr1][1].start_time+'天<=注册时间<='+response1[0].data[_attr1][1].end_time+'天'});break;
                    case '2': _keHuData1.unshift({name:'巩固期',value:response1[0].data[_attr1][0].percent==null?0:response1[0].data[_attr1][0].percent,number:response1[0].data[_attr1][1].start_time+'天<=注册时间<='+response1[0].data[_attr1][1].end_time+'天'});break;
                    case '3': _keHuData1.unshift({name:'成长期',value:response1[0].data[_attr1][0].percent==null?0:response1[0].data[_attr1][0].percent,number:response1[0].data[_attr1][1].start_time+'天<=注册时间<='+response1[0].data[_attr1][1].end_time+'天'});break;
                    case '4': _keHuData1.unshift({name:'成熟期',value:response1[0].data[_attr1][0].percent==null?0:response1[0].data[_attr1][0].percent,number:response1[0].data[_attr1][1].start_time+'天<=注册时间<='+response1[0].data[_attr1][1].end_time+'天'});break;
                    case '5': _keHuData1.unshift({name:'衰退期',value:response1[0].data[_attr1][0].percent==null?0:response1[0].data[_attr1][0].percent,number:response1[0].data[_attr1][1].start_time+'天<=注册时间<='+response1[0].data[_attr1][1].end_time+'天'});break;
                    case '6': _keHuData1.unshift({name:'恢复期',value:response1[0].data[_attr1][0].percent==null?0:response1[0].data[_attr1][0].percent,number:response1[0].data[_attr1][1].start_time+'天<=注册时间'});break;
                }
            }
            option1 = {
                color :['#ff8870','#fe9c62','#fbbf61','#ffdb62','#85dbff','#68bcf7','#51a1f1','#6a7ef7','#9f75f7','#c779f7','#ee78e9','#fe9cda'],
                title: {
                    text: '\n指标分布占比率',
                    x: 'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data: ['开拓期', '巩固期', '成长期', '成熟期', '衰退期' ,'恢复期']
                },
                tooltip: {
                    trigger: 'item',
                    formatter: function(params){
                        return params.data.name+'：'+params.data.value+'%<br/>'+params.data.number
                    }
                },
                toolbox: {
                    show: true,
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '50%',
                        center: ['50%', '60%'],
                        minAngle: 10,
                        data: _keHuData1,
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter: '{c}%'
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _keHuChart1 = echarts.init(document.getElementById('fenxiChart1'));
//            _keHuChart1.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading1);
            keHuLoading1 = setTimeout(function (){
                _keHuChart1.hideLoading();
                _keHuChart1.setOption(option1);
            },2000);
//          消费客户群体特征分析饼图
            var option2,keHuLoading2;
            $('.man-word').eq(0).find('p').text(response2[0].data.total['1']);
            $('.man-word').eq(1).find('p').text(response2[0].data.total['0']);
            option2 = {
                color:['#6495ed','#da7982'],
                title: {
                    text: '\n总人数：'+response2[0].data.total.total+'人',
                    x: 'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data: ['男性', '女性']
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{b} : {c}人"
                },
                toolbox: {
                    show: true,
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '40%',
                        center: ['50%', '55%'],
                        minAngle: 10,
                        data: [
                            {value: response2[0].data.total['1'], name: '男性'},
                            {value: response2[0].data.total['0'], name: '女性'}
                        ],
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter: '{d}%'
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _keHuChart2 = echarts.init(document.getElementById('fenxiChart2'));
//            _keHuChart2.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading2);
            keHuLoading2 = setTimeout(function (){
                _keHuChart2.hideLoading();
                _keHuChart2.setOption(option2);
            },2000);
//          消费客户群体特征分柱状图男
            var option3,keHuLoading3,_manPer,__manPerArr=[],_manNum,_manNumArr=[];
            for(_manPer in response2[0].data.man){
                __manPerArr.unshift(parseInt(response2[0].data.man[_manPer]));
            }
            for(_manNum in response2[0].data.manMaps){
                _manNumArr.unshift(response2[0].data.manMaps[_manNum]);
            }
            option3 = {
                xAxis: [
                    {
                        show: false,
                        type: 'value',
                        position: 'top'
                    }
                ],
                yAxis: [
                    {
                        type: 'category',
                        axisLine: {show: false},
                        axisTick: {show: false},
                        splitLine: {show: false},
                        data: _manNumArr,
                        axisLabel : {
                            textStyle: {
                                color: '#6495ed'
                            }
                        }
                    }
                ],
                grid: {
                    y: 0,
                    y2: 35,
                    borderColor: '#ccc'  //设置边框颜色
                },
                series: [
                    {
                        type: 'bar',
                        stack: '总量',
                        barMinHeight: 5,
                        itemStyle: { normal: {
                            color: '#6495ed',
                            borderRadius: 0,
                            label: {
                                show: true,
                                position: 'right',
                                formatter: '{c}%'
                            }
                        }},
                        data: __manPerArr
                    }
                ]
            };
//            _keHuChart3 = echarts.init(document.getElementById('fenxiChart3'));
//            _keHuChart3.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading3);
            keHuLoading3 = setTimeout(function (){
                _keHuChart3.hideLoading();
                _keHuChart3.setOption(option3);
            },2000);
//          消费客户群体特征分柱状图女
            var option4,keHuLoading4,_womanPer,__womanPerArr=[],_womanNum,_womanNumArr=[];
            for(_womanPer in response2[0].data.woman){
                __womanPerArr.unshift(parseInt(response2[0].data.woman[_womanPer]));
            }
            for(_womanNum in response2[0].data.womanMaps){
                _womanNumArr.unshift(response2[0].data.womanMaps[_womanNum]);
            }
            option4 = {
                xAxis: [
                    {
                        show: false,
                        type: 'value',
                        position: 'top'
                    }
                ],
                yAxis: [
                    {
                        type: 'category',
                        axisLine: {show: false},
                        axisTick: {show: false},
                        splitLine: {show: false},
                        data: _womanNumArr,
                        axisLabel : {
                            textStyle: {
                                color: '#da7982'
                            }
                        }
                    }
                ],
                grid: {
                    y: 0,
                    y2: 35,
                    borderColor: '#ccc'  //设置边框颜色
                },
                series: [
                    {
                        type: 'bar',
                        stack: '总量',
                        barMinHeight: 5,
                        itemStyle: { normal: {
                            color: '#da7982',
                            borderRadius: 0,
                            label: {
                                show: true,
                                position: 'right',
                                formatter: '{c}%'
                            }
                        }},
                        data: __womanPerArr
                    }
                ]
            };
//            _keHuChart4 = echarts.init(document.getElementById('fenxiChart4'));
//            _keHuChart4.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading4);
            keHuLoading4 = setTimeout(function (){
                _keHuChart4.hideLoading();
                _keHuChart4.setOption(option4);
            },2000);
//          客户习惯分析图
            var option5,keHuLoading5,_iTop,_iHours,_attrHours,_xiGuAnArr=[];
            for(_iTop=0;_iTop<5;_iTop++){
                var _attrLast=parseInt(response3[0].data['top5'][_iTop]['times'])+1;
                if(_attrLast==24){
                    _attrLast=0;
                }
                $('.time-store').eq(_iTop).text(response3[0].data['top5'][_iTop]['times']+':00'+'-'+_attrLast+':00');
                $('.person-store').eq(_iTop).text(response3[0].data['top5'][_iTop]['num1']);
            }
            for(_iHours=0;_iHours<24;_iHours++){
                _xiGuAnArr.push(response3[0].data.hours[_iHours]['num1']);
            }
            option5 = {
                title : {
                    text: '高峰时段来访人数',
                    x: 'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                tooltip : {
                    trigger: 'axis',
                    formatter: "{c}",
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                toolbox: {
                    orient : 'vertical',
                    y : 'center',
                    show : true,
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                xAxis : [
                    {
                        name : '来访时间点',
                        type : 'category',
                        data : ['0时','1时','2时','3时','4时','5时','6时','7时','8时','9时','10时','11时','12时','13时','14时','15时','16时','17时','18时','19时','20时','21时','22时','23时'
                        ],
                        axisLabel: {
                            textStyle: {
                                fontFamily: '微软雅黑',
                                fontSize: 12
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name : '单位：人',
                        type : 'value'
                    }
                ],
                series : [
                    {
                        itemStyle: {
                            normal: {
                                color: function(params) {
                                    var colorList = [
                                        '#da7982','#57b1ed','#2dc6cb','#ffb981','#b6a2e1'
                                    ];
                                    return colorList[params.dataIndex%5]
                                },
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '{c}',
                                    textStyle: {
                                        fontSize: 12,
                                        fontFamily: '微软雅黑',
                                        color: '#000'
                                    }
                                }
                            }
                        },
                        type:'bar',
                        barMinHeight: 5,
                        data:_xiGuAnArr
                    }
                ]
            };
//            _keHuChart5 = echarts.init(document.getElementById('fenxiChart5'));
//            _keHuChart5.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading5);
            keHuLoading5 = setTimeout(function (){
                _keHuChart5.hideLoading();
                _keHuChart5.setOption(option5);
            },2000);
//          地域分布图
            var option6,_iDis,_disArr=[],_proNum=response4[0].data.length,keHuLoading6;
            for(_iDis=0;_iDis<_proNum;_iDis++){
                $('.city-store').eq(_iDis).text(response4[0].data[_iDis].city);
                _disArr.push({name: response4[0].data[_iDis].province,value: response4[0].data[_iDis]['data1']});
            }
            option6 = {
                title : {
                    text: '消费客户地域分布图',
                    subtext: '成交金额：元',
                    x:'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    },
                    subtextStyle: {
                        color: '#0e90cf'
                    }
                },
                tooltip : {
                    trigger: 'item'
                },
                dataRange: {
                    min: 0,
                    max: parseInt(response4[0].data[0]['data1']),
                    x: 'left',
                    y: 'top',
                    text:['高','低'],
                    calculable : true
                },
                series : [
                    {
                        name: '消费',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        itemStyle:{
                            normal:{
                                label:{
                                    show:true
                                }

                            },
                            emphasis:{
                                label:{
                                    show:true
                                }
                            }
                        },
                        data:_disArr
                    }
                ]
            };
//            _keHuChart6 = echarts.init(document.getElementById('fenxiChart6'));
//            _keHuChart6.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading6);
            keHuLoading6 = setTimeout(function (){
                _keHuChart6.hideLoading();
                _keHuChart6.setOption(option6);
            },2000);
//          离网分析图
            var option7,_liWangAttr1,_liWangAttr2,_liWangArr=[],_xArr=[],keHuLoading7;
            for(_liWangAttr1 in response5[0].data){
                for(_liWangAttr2 in response5[0].data[_liWangAttr1]){
                    _liWangArr.push({value:response5[0].data[_liWangAttr1][_liWangAttr2],name:_liWangAttr2});  //尼玛有的时候pramas没有name，只能用这种奇奇怪怪的方式自己构造数据了
                    _xArr.push(_liWangAttr2);
                }
            }
            option7 = {
                title : {
                    text: '',
                    x:'center',
                    y:'top',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {
                        type : 'shadow'
                    }
                },
                toolbox: {
                    orient : 'vertical',
                    y : 'center',
                    show : true,
                    feature : {
                        restore : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : _xArr,
                        axisLabel: {
                            textStyle: {
                                fontFamily: '微软雅黑'
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name: '单位：人',
                        type : 'value'
                    }
                ],
                series : [
                    {
                        itemStyle: {
                            normal: {
                                color: function(params){
                                    switch (params.data.name){  //取自己构造的数据
                                        case '离网死亡型':
                                        case '活跃突减型':
                                        case '沉默使用型':
                                        case '沉默离网趋向型': return '#da7982';break;
                                        case '富豪型':
                                        case '富帅型':
                                        case '粉领型':
                                        case '灰领型':
                                        case '白领型':
                                        case '蓝领型':
                                        case '土豪型':
                                        case '大富豪型': return '#57b1ed';break;
                                        case '多闻强记型':
                                        case '半面不忘型':
                                        case '触目成诵型': return '#2dc6cb';break;
                                        case '购物粉丝型':
                                        case '购物关注型':
                                        case '购物热衷型':
                                        case '购物忠实型': return '#ffb981';break;
                                        case '爱好兴趣型':
                                        case '关注使用型':
                                        case '粉丝忠诚型':
                                        case '狂热忠诚型': return '#b6a2e1';break;
                                    }
                                },
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '{c}',
                                    textStyle: {
                                        fontSize: 13,
                                        fontFamily: '微软雅黑',
                                        color: '#000'
                                    }
                                }
                            }
                        },
                        type:'bar',
                        barMinHeight: 5,
                        data:_liWangArr
                    }
                ]
            };
//            _keHuChart7 = echarts.init(document.getElementById('fenxiChart7'));
//            _keHuChart7.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading7);
            keHuLoading7 = setTimeout(function (){
                _keHuChart7.hideLoading();
                _keHuChart7.setOption(option7);
            },200);
//          TOP5购买品种图
            var option8,_iBrand,_nameArr=[],_numArr=[],keHuLoading8;
            for(_iBrand=0;_iBrand<5;_iBrand++){
                _nameArr.push(response6[0].data[_iBrand].type_name);
                _numArr.push(response6[0].data[_iBrand]['COUNT(type_name)']);
            }
            option8 = {
                color : ['#da7982','#57b1ed','#2dc6cb','#ffb981','#b6a2e1'],
                title : {
                    text: 'TOP5品种大类',
                    x:'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                toolbox: {
                    orient : 'vertical',
                    y : 'center',
                    show : true,
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : _nameArr,
                        axisLabel: {
                            textStyle: {
                                fontFamily: '微软雅黑'
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name: '单位：笔',
                        type : 'value'
                    }
                ],
                series : [
                    {
                        itemStyle: {
                            normal: {
                                color: function(params) {
                                    var colorList = [
                                        '#da7982','#57b1ed','#2dc6cb','#ffb981','#b6a2e1'
                                    ];
                                    return colorList[params.dataIndex]
                                },
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '{c}',
                                    textStyle: {
                                        fontSize: 13,
                                        fontFamily: '微软雅黑',
                                        color: '#000'
                                    }
                                }
                            }
                        },
                        type:'bar',
                        barMinHeight: 5,
                        barWidth: 20,
                        data:_numArr
                    }
                ]
            };
//            _keHuChart8 = echarts.init(document.getElementById('fenxiChart8'));
//            _keHuChart8.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading8);
            keHuLoading8 = setTimeout(function (){
                _keHuChart8.hideLoading();
                _keHuChart8.setOption(option8);
            },2000);
//          客户生日图
            var option9,_inBirth,keHuLoading9;
            _inBirth=response7[0].data['countUid']==null?0:response7[0].data['countUid'];
            option9 = {
                color:['#ffb981'],
                calculable : false,
                series : (function (){
                    var series = [];
                    for (var i = 0; i < 30; i++) {
                        series.push({
                            type:'pie',
                            itemStyle : {normal : {
                                label : {show : i > 28},
                                labelLine : {show : i > 28, length:20}
                            }},
                            radius : [i * 4 + 10, i * 4 + 13],
                            data:[
                                {value: i * _inBirth ,  name:'生日圈用户：'+_inBirth+'人'}
                            ]
                        });
                    }
                    series[0].markPoint = {
                        symbol:'emptyCircle',
                        symbolSize:series[0].radius[0],
                        data:[{x:'50%',y:'50%'}]
                    };
                    return series;
                })()
            };
//            _keHuChart9 = echarts.init(document.getElementById('fenxiChart9'));
//            _keHuChart9.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading9);
            keHuLoading9 = setTimeout(function (){
                _keHuChart9.hideLoading();
                _keHuChart9.setOption(option9);
            },2000);
//          为图表添加点击事件
            var ecConfig = echarts.config;
            _keHuChart9.on(ecConfig.EVENT.CLICK, function(param){
                $('#birth-list').css({visibility:'visible'});
                $('#birth-dis').val('');
                $('.table-box').find('tbody').html('<tr><td colspan="7">暂无数据</td></tr>');
            });
            window.onresize=function(){
                _keHuChart1.resize();
                _keHuChart2.resize();
                _keHuChart3.resize();
                _keHuChart4.resize();
                _keHuChart5.resize();
                _keHuChart6.resize();
                _keHuChart7.resize();
                _keHuChart8.resize();
                _keHuChart9.resize();
            };
    });
}
/**
 *消费客户群体特征分析
 */
//切换卡
function getCustomerGroup(obj){
    $(obj).siblings().removeClass('time-active');
    $(obj).addClass('time-active');
    var index=$(obj).index();
    var dateType=parseInt(index)+1;
    $('#group-start').val('');  //选切换卡则把日历时间重置
    $('#group-end').val('');
    _keHuChart2 = echarts.init(document.getElementById('fenxiChart2'));
    _keHuChart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart3 = echarts.init(document.getElementById('fenxiChart3'));
    _keHuChart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart4 = echarts.init(document.getElementById('fenxiChart4'));
    _keHuChart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.ajax({
        url:SLSCHTTP+'/lifecycle/getCustomerGroup',
        type:'post',
        data:{dateType:dateType},
        dataType:'json',
        success:function(response){
            var option2,keHuLoading2;
            $('.man-word').eq(0).find('p').text(response.data.total['1']);
            $('.man-word').eq(1).find('p').text(response.data.total['0']);
            option2 = {
                color:['#6495ed','#da7982'],
                title: {
                    text: '\n总人数：'+response.data.total.total+'人',
                    x: 'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data: ['男性', '女性']
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{b} : {c}人"
                },
                toolbox: {
                    show: true,
                    feature : {
                        restore : {show: true}
                    }
                },
                calculable: true,
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '40%',
                        center: ['50%', '55%'],
                        minAngle: 10,
                        data: [
                            {value: response.data.total['1'], name: '男性'},
                            {value: response.data.total['0'], name: '女性'}
                        ],
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    formatter: '{d}%'
                                },
                                labelLine: {show: true}
                            }
                        }
                    }
                ]
            };
//            _keHuChart2 = echarts.init(document.getElementById('fenxiChart2'));
//            _keHuChart2.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading2);
            keHuLoading2 = setTimeout(function (){
                _keHuChart2.hideLoading();
                _keHuChart2.setOption(option2);
            },2000);
            var option3,keHuLoading3,_manPer,__manPerArr=[],_manNum,_manNumArr=[];
            for(_manPer in response.data.man){
                __manPerArr.unshift(parseInt(response.data.man[_manPer]));
            }
            for(_manNum in response.data.manMaps){
                _manNumArr.unshift(response.data.manMaps[_manNum]);
            }
            option3 = {
                xAxis: [
                    {
                        show: false,
                        type: 'value',
                        position: 'top'
                    }
                ],
                yAxis: [
                    {
                        type: 'category',
                        axisLine: {show: false},
                        axisTick: {show: false},
                        splitLine: {show: false},
                        data: _manNumArr,
                        axisLabel : {
                            textStyle: {
                                color: '#6495ed'
                            }
                        }
                    }
                ],
                grid: {
                    y: 0,
                    y2: 35,
                    borderColor: '#ccc'  //设置边框颜色
                },
                series: [
                    {
                        type: 'bar',
                        stack: '总量',
                        barMinHeight: 5,
                        itemStyle: { normal: {
                            color: '#6495ed',
                            borderRadius: 0,
                            label: {
                                show: true,
                                position: 'right',
                                formatter: '{c}%'
                            }
                        }},
                        data: __manPerArr
                    }
                ]
            };
//            _keHuChart3 = echarts.init(document.getElementById('fenxiChart3'));
//            _keHuChart3.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading3);
            keHuLoading3 = setTimeout(function (){
                _keHuChart3.hideLoading();
                _keHuChart3.setOption(option3);
            },2000);
            var option4,keHuLoading4,_womanPer,__womanPerArr=[],_womanNum,_womanNumArr=[];
            for(_womanPer in response.data.woman){
                __womanPerArr.unshift(parseInt(response.data.woman[_womanPer]));
            }
            for(_womanNum in response.data.womanMaps){
                _womanNumArr.unshift(response.data.womanMaps[_womanNum]);
            }
            option4 = {
                xAxis: [
                    {
                        show: false,
                        type: 'value',
                        position: 'top'
                    }
                ],
                yAxis: [
                    {
                        type: 'category',
                        axisLine: {show: false},
                        axisTick: {show: false},
                        splitLine: {show: false},
                        data: _womanNumArr,
                        axisLabel : {
                            textStyle: {
                                color: '#da7982'
                            }
                        }
                    }
                ],
                grid: {
                    y: 0,
                    y2: 35,
                    borderColor: '#ccc'  //设置边框颜色
                },
                series: [
                    {
                        type: 'bar',
                        stack: '总量',
                        barMinHeight: 5,
                        itemStyle: { normal: {
                            color: '#da7982',
                            borderRadius: 0,
                            label: {
                                show: true,
                                position: 'right',
                                formatter: '{c}%'
                            }
                        }},
                        data: __womanPerArr
                    }
                ]
            };
//            _keHuChart4 = echarts.init(document.getElementById('fenxiChart4'));
//            _keHuChart4.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading4);
            keHuLoading4 = setTimeout(function (){
                _keHuChart4.hideLoading();
                _keHuChart4.setOption(option4);
            },2000);
        }
    });
}
//自定义时间
function getTimeCustomer(){
    $('.customer-time').find('li').removeClass('time-active');
    var date1=$('#group-start').val();
    var date2=$('#group-end').val();
    _keHuChart2 = echarts.init(document.getElementById('fenxiChart2'));
    _keHuChart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart3 = echarts.init(document.getElementById('fenxiChart3'));
    _keHuChart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    _keHuChart4 = echarts.init(document.getElementById('fenxiChart4'));
    _keHuChart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
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
        }else{
            var startTime=date1.split('-').join('');
            var endTime=date2.split('-').join('');
            $.ajax({
                url:SLSCHTTP+'/lifecycle/getCustomerGroup',
                type:'post',
                data:{dateType:0,startTime:startTime,endTime:endTime},
                dataType:'json',
                success:function(response){
                    var option2,keHuLoading2;
                    $('.man-word').eq(0).find('p').text(response.data.total['1']);
                    $('.man-word').eq(1).find('p').text(response.data.total['0']);
                    option2 = {
                        color:['#6495ed','#da7982'],
                        title: {
                            text: '\n总人数：'+response.data.total.total+'人',
                            x: 'center',
                            textStyle: {
                                fontFamily: '微软雅黑',
                                fontSize: 15,
                                fontWeight: 'normal',
                                color: '#0e90cf'
                            }
                        },
                        legend: {
                            orient: 'vertical',
                            x: 'left',
                            data: ['男性', '女性']
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{b} : {c}人"
                        },
                        toolbox: {
                            show: true,
                            feature : {
                                restore : {show: true}
                            }
                        },
                        calculable: true,
                        series: [
                            {
                                name: '访问来源',
                                type: 'pie',
                                radius: '40%',
                                center: ['50%', '55%'],
                                minAngle: 10,
                                data: [
                                    {value: response.data.total['1'], name: '男性'},
                                    {value: response.data.total['0'], name: '女性'}
                                ],
                                itemStyle: {
                                    normal: {
                                        label: {
                                            show: true,
                                            formatter: '{d}%'
                                        },
                                        labelLine: {show: true}
                                    }
                                }
                            }
                        ]
                    };
//                    _keHuChart2 = echarts.init(document.getElementById('fenxiChart2'));
//                    _keHuChart2.showLoading({  //载入动画
//                        text : '请稍等',
//                        effect : 'whirling',
//                        textStyle : {
//                            fontSize : 15
//                        }
//                    });
                    clearTimeout(keHuLoading2);
                    keHuLoading2 = setTimeout(function (){
                        _keHuChart2.hideLoading();
                        _keHuChart2.setOption(option2);
                    },2000);
                    var option3,keHuLoading3,_manPer,__manPerArr=[],_manNum,_manNumArr=[];
                    for(_manPer in response.data.man){
                        __manPerArr.unshift(parseInt(response.data.man[_manPer]));
                    }
                    for(_manNum in response.data.manMaps){
                        _manNumArr.unshift(response.data.manMaps[_manNum]);
                    }
                    option3 = {
                        xAxis: [
                            {
                                show: false,
                                type: 'value',
                                position: 'top'
                            }
                        ],
                        yAxis: [
                            {
                                type: 'category',
                                axisLine: {show: false},
                                axisTick: {show: false},
                                splitLine: {show: false},
                                data: _manNumArr,
                                axisLabel : {
                                    textStyle: {
                                        color: '#6495ed'
                                    }
                                }
                            }
                        ],
                        grid: {
                            y: 0,
                            y2: 35,
                            borderColor: '#ccc'  //设置边框颜色
                        },
                        series: [
                            {
                                type: 'bar',
                                stack: '总量',
                                barMinHeight: 5,
                                itemStyle: { normal: {
                                    color: '#6495ed',
                                    borderRadius: 0,
                                    label: {
                                        show: true,
                                        position: 'right',
                                        formatter: '{c}%'
                                    }
                                }},
                                data: __manPerArr
                            }
                        ]
                    };
//                    _keHuChart3 = echarts.init(document.getElementById('fenxiChart3'));
//                    _keHuChart3.showLoading({  //载入动画
//                        text : '请稍等',
//                        effect : 'whirling',
//                        textStyle : {
//                            fontSize : 15
//                        }
//                    });
                    clearTimeout(keHuLoading3);
                    keHuLoading3 = setTimeout(function (){
                        _keHuChart3.hideLoading();
                        _keHuChart3.setOption(option3);
                    },2000);
                    var option4,keHuLoading4,_womanPer,__womanPerArr=[],_womanNum,_womanNumArr=[];
                    for(_womanPer in response.data.woman){
                        __womanPerArr.unshift(parseInt(response.data.woman[_womanPer]));
                    }
                    for(_womanNum in response.data.womanMaps){
                        _womanNumArr.unshift(response.data.womanMaps[_womanNum]);
                    }
                    option4 = {
                        xAxis: [
                            {
                                show: false,
                                type: 'value',
                                position: 'top'
                            }
                        ],
                        yAxis: [
                            {
                                type: 'category',
                                axisLine: {show: false},
                                axisTick: {show: false},
                                splitLine: {show: false},
                                data: _womanNumArr,
                                axisLabel : {
                                    textStyle: {
                                        color: '#da7982'
                                    }
                                }
                            }
                        ],
                        grid: {
                            y: 0,
                            y2: 35,
                            borderColor: '#ccc'  //设置边框颜色
                        },
                        series: [
                            {
                                type: 'bar',
                                stack: '总量',
                                barMinHeight: 5,
                                itemStyle: { normal: {
                                    color: '#da7982',
                                    borderRadius: 0,
                                    label: {
                                        show: true,
                                        position: 'right',
                                        formatter: '{c}%'
                                    }
                                }},
                                data: __womanPerArr
                            }
                        ]
                    };
//                    _keHuChart4 = echarts.init(document.getElementById('fenxiChart4'));
//                    _keHuChart4.showLoading({  //载入动画
//                        text : '请稍等',
//                        effect : 'whirling',
//                        textStyle : {
//                            fontSize : 15
//                        }
//                    });
                    clearTimeout(keHuLoading4);
                    keHuLoading4 = setTimeout(function (){
                        _keHuChart4.hideLoading();
                        _keHuChart4.setOption(option4);
                    },2000);
                }
            });
        }
    }else{
        alert('请选择正确的时间段');
    }
}
/**
 *客户习惯分析
 */
function getCustHabit(obj){
    $(obj).siblings().removeClass('time-active');
    $(obj).addClass('time-active');
    var paramType=$('.habit-type').find('.time-active').text();
    var dateType=$('.habit-time').find('.time-active').text();
    switch (paramType){
        case '消费客户':paramType=1;break;
        case '活跃客户':paramType=2;break;
        case '沉默客户':paramType=3;break;
        case '全部客户':paramType=4;break;
    }
    switch (dateType){
        case '昨天':dateType=1;break;
        case '最近7天':dateType=3;break;
        case '最近30天':dateType=4;break;
    }
    _keHuChart5 = echarts.init(document.getElementById('fenxiChart5'));
    _keHuChart5.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.ajax({
        url:SLSCHTTP+'/lifecycle/getCustHabit',
        type:'post',
        data:{paramType:paramType,dateType:dateType},
        dataType:'json',
        success:function(response){
            var option5,keHuLoading5,_iTop,_iHours,_attrHours,_xiGuAnArr=[];
            for(_iTop=0;_iTop<5;_iTop++){
                var _attrLast=parseInt(response.data['top5'][_iTop]['times'])+1;
                if(_attrLast==24){
                    _attrLast=0;
                }
                $('.time-store').eq(_iTop).text(response.data['top5'][_iTop]['times']+':00'+'-'+_attrLast+':00');
                $('.person-store').eq(_iTop).text(response.data['top5'][_iTop]['num1']);
            }
            for(_iHours=0;_iHours<24;_iHours++){
                _xiGuAnArr.push(response.data.hours[_iHours]['num1']);
            }
            option5 = {
                title : {
                    text: '高峰时段来访人数',
                    x: 'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                tooltip : {
                    trigger: 'axis',
                    formatter: "{c}",
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                toolbox: {
                    orient : 'vertical',
                    y : 'center',
                    show : true,
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                xAxis : [
                    {
                        name : '来访时间点',
                        type : 'category',
                        data : ['0时','1时','2时','3时','4时','5时','6时','7时','8时','9时','10时','11时','12时','13时','14时','15时','16时','17时','18时','19时','20时','21时','22时','23时'
                        ],
                        axisLabel: {
                            textStyle: {
                                fontFamily: '微软雅黑',
                                fontSize: 12
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name : '单位：人',
                        type : 'value'
                    }
                ],
                series : [
                    {
                        itemStyle: {
                            normal: {
                                color: function(params) {
                                    var colorList = [
                                        '#da7982','#57b1ed','#2dc6cb','#ffb981','#b6a2e1'
                                    ];
                                    return colorList[params.dataIndex%5]
                                },
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '{c}',
                                    textStyle: {
                                        fontSize: 12,
                                        fontFamily: '微软雅黑',
                                        color: '#000'
                                    }
                                }
                            }
                        },
                        type:'bar',
                        barMinHeight: 5,
                        data:_xiGuAnArr
                    }
                ]
            };
//            _keHuChart5 = echarts.init(document.getElementById('fenxiChart5'));
//            _keHuChart5.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading5);
            keHuLoading5 = setTimeout(function (){
                _keHuChart5.hideLoading();
                _keHuChart5.setOption(option5);
            },2000);
        }
    });
}
/**
 *地域分布
 */
//切换卡
function getCustDistribute(obj){
    $(obj).siblings().removeClass('time-active');
    $(obj).addClass('time-active');
    var total=$(obj).parent().find('li');
    if(total.length==5){  //选切换卡时间则把日历时间重置
        $('#distri-start').val('');
        $('#distri-end').val('');
    }
    var dateType=$('.distri-time').find('.time-active').index();
    var paramType=$('.distri-money').find('.time-active').index();
    var date1=$('#distri-start').val();
    var date2=$('#distri-end').val();
    var startTime=date1.split('-').join('');
    var endTime=date2.split('-').join('');
    var _name;
    switch (parseInt(paramType)){
        case 0:_name='成交金额：元';break;
        case 1:_name='购买人数：人';break;
        case 2:_name='成交笔数：笔';break;
        case 3:_name='客单价：元';break;
    }
    _keHuChart6 = echarts.init(document.getElementById('fenxiChart6'));
    _keHuChart6.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.ajax({
        url:SLSCHTTP+'/lifecycle/getCustDistribute',
        type:'post',
        data:{dateType:parseInt(dateType)+1,startTime:startTime,endTime:endTime,paramType:parseInt(paramType)+1},
        dataType:'json',
        success:function(response){
            var option6,_iDis,_disArr=[],_proNum=response.data.length,keHuLoading6;
            for(_iDis=0;_iDis<_proNum;_iDis++){
                $('.city-store').eq(_iDis).text(response.data[_iDis].city);
                _disArr.push({name: response.data[_iDis].province,value: response.data[_iDis]['data1']});
            }
            option6 = {
                title : {
                    text: '消费客户地域分布图',
                    subtext: _name,
                    x:'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    },
                    subtextStyle: {
                        color: '#0e90cf'
                    }
                },
                tooltip : {
                    trigger: 'item'
                },
                dataRange: {
                    min: 0,
                    max: parseInt(response.data[0]['data1']),
                    x: 'left',
                    y: 'top',
                    text:['高','低'],
                    calculable : true
                },
                series : [
                    {
                        name: '消费',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        itemStyle:{
                            normal:{
                                label:{
                                    show:true
                                }

                            },
                            emphasis:{
                                label:{
                                    show:true
                                }
                            }
                        },
                        data:_disArr
                    }
                ]
            };
//            _keHuChart6 = echarts.init(document.getElementById('fenxiChart6'));
//            _keHuChart6.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading6);
            keHuLoading6 = setTimeout(function (){
                _keHuChart6.hideLoading();
                _keHuChart6.setOption(option6);
            },2000);
        }
    });
}
//自定义时间
function getTimeDistri(){
    $('.distri-time').find('li').removeClass('time-active');
    var date1=$('#distri-start').val();
    var date2=$('#distri-end').val();
    _keHuChart6 = echarts.init(document.getElementById('fenxiChart6'));
    _keHuChart6.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    if(date1!='' && date2!='') {
        var year1 = date1.substr(0, 4);
        var year2 = date2.substr(0, 4);
        var month1 = date1.substr(5, 2);
        var month2 = date2.substr(5, 2);
        var day1 = date1.substr(8, 2);
        var day2 = date2.substr(8, 2);
        var temp1 = year1 + "/" + month1 + "/" + day1;
        var temp2 = year2 + "/" + month2 + "/" + day2;
        var dateaa = new Date(temp1);
        var datebb = new Date(temp2);
        var date = datebb.getTime() - dateaa.getTime();
        var time = Math.floor(date / (1000 * 60 * 60 * 24));
        if(time<0){
            alert('请选择正确的时间段');
            return false;
        }else{
            var paramType=$('.distri-money').find('.time-active').index();
            var startTime=date1.split('-').join('');
            var endTime=date2.split('-').join('');
            var _name;
            switch (parseInt(paramType)){
                case 0:_name='成交金额：元';break;
                case 1:_name='购买人数：人';break;
                case 2:_name='成交笔数：笔';break;
                case 3:_name='客单价：元';break;
            }
            $.ajax({
                url:SLSCHTTP+'/lifecycle/getCustDistribute',
                type:'post',
                data:{dateType:0,paramType:parseInt(paramType)+1,startTime:startTime,endTime:endTime},
                dataType:'json',
                success:function(response){
                    var option6,_iDis,_disArr=[],_proNum=response.data.length,keHuLoading6;
                    for(_iDis=0;_iDis<_proNum;_iDis++){
                        $('.city-store').eq(_iDis).text(response.data[_iDis].city);
                        _disArr.push({name: response.data[_iDis].province,value: response.data[_iDis]['data1']});
                    }
                    option6 = {
                        title : {
                            text: '消费客户地域分布图',
                            subtext: _name,
                            x:'center',
                            textStyle: {
                                fontFamily: '微软雅黑',
                                fontSize: 15,
                                fontWeight: 'normal',
                                color: '#0e90cf'
                            },
                            subtextStyle: {
                                color: '#0e90cf'
                            }
                        },
                        tooltip : {
                            trigger: 'item'
                        },
                        dataRange: {
                            min: 0,
                            max: parseInt(response.data[0]['data1']),
                            x: 'left',
                            y: 'top',
                            text:['高','低'],
                            calculable : true
                        },
                        series : [
                            {
                                name: '消费',
                                type: 'map',
                                mapType: 'china',
                                roam: false,
                                itemStyle:{
                                    normal:{
                                        label:{
                                            show:true
                                        }

                                    },
                                    emphasis:{
                                        label:{
                                            show:true
                                        }
                                    }
                                },
                                data:_disArr
                            }
                        ]
                    };
//                    _keHuChart6 = echarts.init(document.getElementById('fenxiChart6'));
//                    _keHuChart6.showLoading({  //载入动画
//                        text : '请稍等',
//                        effect : 'whirling',
//                        textStyle : {
//                            fontSize : 15
//                        }
//                    });
                    clearTimeout(keHuLoading6);
                    keHuLoading6 = setTimeout(function (){
                        _keHuChart6.hideLoading();
                        _keHuChart6.setOption(option6);
                    },2000);
                }
            });
        }
    }else{
        alert('请选择正确的时间段');
    }
}
/**
 *客户消费Top5
 */
function getTopBrand(obj){
    $(obj).siblings().removeClass('time-active');
    $(obj).addClass('time-active');
    var dateType=$(obj).index();
    _keHuChart8 = echarts.init(document.getElementById('fenxiChart8'));
    _keHuChart8.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.ajax({
        url:SLSCHTTP+'/lifecycle/getTop5Brand',
        type:'post',
        data:{dateType:parseInt(dateType)+1},
        dataType:'json',
        success:function(response){
            var option8,_iBrand,_nameArr=[],_numArr=[],keHuLoading8;
            for(_iBrand=0;_iBrand<5;_iBrand++){
                _nameArr.push(response.data[_iBrand].type_name);
                _numArr.push(response.data[_iBrand]['COUNT(type_name)']);
            }
            option8 = {
                color : ['#da7982','#57b1ed','#2dc6cb','#ffb981','#b6a2e1'],
                title : {
                    text: 'TOP5品种大类',
                    x:'center',
                    textStyle: {
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                toolbox: {
                    orient : 'vertical',
                    y : 'center',
                    show : true,
                    feature : {
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true}
                    }
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : _nameArr,
                        axisLabel: {
                            textStyle: {
                                fontFamily: '微软雅黑'
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name : '单位：笔',
                        type : 'value'
                    }
                ],
                series : [
                    {
                        itemStyle: {
                            normal: {
                                color: function(params) {
                                    var colorList = [
                                        '#da7982','#57b1ed','#2dc6cb','#ffb981','#b6a2e1'
                                    ];
                                    return colorList[params.dataIndex]
                                },
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '{c}',
                                    textStyle: {
                                        fontSize: 13,
                                        fontFamily: '微软雅黑',
                                        color: '#000'
                                    }
                                }
                            }
                        },
                        type:'bar',
                        barMinHeight: 5,
                        barWidth: 20,
                        data:_numArr
                    }
                ]
            };
//            _keHuChart8 = echarts.init(document.getElementById('fenxiChart8'));
//            _keHuChart8.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading8);
            keHuLoading8 = setTimeout(function (){
                _keHuChart8.hideLoading();
                _keHuChart8.setOption(option8);
            },2000);
        }
    });
}
/**
 *客户生日祝福提醒
 */
function getBirthBless(obj){
    closeList();
    $(obj).siblings().removeClass('time-active');
    $(obj).addClass('time-active');
    var birthType=$(obj).text();
    switch (birthType){
        case '明天': birthType=1;break;
        case '最近7天': birthType=2;break;
        case '最近30天': birthType=3;break;
    }
    _keHuChart9 = echarts.init(document.getElementById('fenxiChart9'));
    _keHuChart9.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 15
        }
    });
    $.ajax({
        url:SLSCHTTP+'/lifecycle/getBirthBless',
        type:'post',
        data:{birthType:birthType},
        dataType:'json',
        success:function(response){
            var option9,_inBirth,_outBirth,keHuLoading9;
            _inBirth=response.data['countUid']==null?0:response.data['countUid'];
            option9 = {
                color:['#ffb981'],
                calculable : false,
                series : (function (){
                    var series = [];
                    for (var i = 0; i < 30; i++) {
                        series.push({
                            type:'pie',
                            itemStyle : {normal : {
                                label : {show : i > 28},
                                labelLine : {show : i > 28, length:20}
                            }},
                            radius : [i * 4 + 10, i * 4 + 13],
                            data:[
                                {value: i * _inBirth ,  name:'生日圈用户：'+_inBirth+'人'}
                            ]
                        });
                    }
                    series[0].markPoint = {
                        symbol:'emptyCircle',
                        symbolSize:series[0].radius[0],
                        data:[{x:'50%',y:'50%'}]
                    };
                    return series;
                })()
            };
//            _keHuChart9 = echarts.init(document.getElementById('fenxiChart9'));
//            _keHuChart9.showLoading({  //载入动画
//                text : '请稍等',
//                effect : 'whirling',
//                textStyle : {
//                    fontSize : 15
//                }
//            });
            clearTimeout(keHuLoading9);
            keHuLoading9 = setTimeout(function (){
                _keHuChart9.hideLoading();
                _keHuChart9.setOption(option9);
            },2000);
            //          为图表添加点击事件
            var ecConfig = echarts.config;
            _keHuChart9.on(ecConfig.EVENT.CLICK, function(param){
                $('#birth-list').css({visibility:'visible'});
                $('#birth-dis').val('');
                $('.table-box').find('tbody').html('<tr><td colspan="7">暂无数据</td></tr>');
            });
        }
    });
}
/**
 *搜索生日用户
 */
function searchBirList(){
    var _disName= $.trim($('#birth-dis').val());
    if(_disName!=''){
        var birthType=$('.bir-date').find('.time-active').index();
        switch (parseInt(birthType)){
            case 0: birthType=1;break;
            case 1: birthType=2;break;
            case 2: birthType=3;break;
        }
        $.ajax({
            url:SLSCHTTP+'/lifecycle/getBirthInfo',
            type:'post',
            data:{birthType:birthType,param:_disName},
            dataType:'json',
            success:function(response){
                var _str='<tr><td colspan="8"><div class="time-btn" dir="'+_disName+'" onclick="exportBirth(this);" style="position:relative;left:50%;margin-left:-25px;">导出</div></td></tr>',
                    _i,
                    _len=response.data.length;
                if(_len==0){
                    _str='<tr><td colspan="8">暂无数据</td></tr>';
                }else{
                    for(_i=0;_i<_len;_i++){
                        _str+='<tr>' +
                            '<td>'+response.data[_i].uid+'</td>' +
                            '<td>'+response.data[_i].name+'</td>' +
                            '<td>'+response.data[_i].phone+'</td>' +
                            '<td>'+response.data[_i].province+'</td>' +
                            '<td>'+response.data[_i].city+'</td>' +
                            '<td>'+response.data[_i].town+'</td>' +
                            '<td>'+response.data[_i].village+'</td>' +
                            '<td>'+response.data[_i].address+'</td>' +
                            '</tr>';
                    }
                }
                $('.table-box').find('tbody').html(_str);
                $('#birth-save').val(_disName);
            }
        });
    }else{
        alert('请输入查询地区(如省市名称)');
    }
}
/**
 *回车事件
 */
$('#birth-dis').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        searchBirList();
    }
});
/**
 *导出
 */
function exportBirth(obj){
    var _disName=$(obj).attr('dir');
    var birthType=$('.bir-date').find('.time-active').index();
    switch (parseInt(birthType)){
        case 0: birthType=1;break;
        case 1: birthType=2;break;
        case 2: birthType=3;break;
    }
    var _iframe=$('#downLoadBir')[0];
    _iframe.src = SLSCHTTP+'/lifecycle/exportExcel?birthType='+birthType+'&param='+_disName;
}
/**
 *关闭生日列表
 */
function closeList(){
    $('#birth-list').css({visibility:'hidden'});
}