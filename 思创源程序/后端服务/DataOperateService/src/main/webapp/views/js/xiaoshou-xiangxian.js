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
    $.ajax({
        url:SLSCHTTP+'/orderanalysis/quadrantAnalysisFirst',
        type:'post',
        data:{dateType:1},  //正式对接dateType改为1
        dataType:'json',
        success:function(response){
            var _i1,_i2,_i3,_i4,_data1=[],_data2=[],_data3=[],_data4=[],keHuLoading;
            for(_i1=0;_i1<response.data.namea.length;_i1++){
                _data1.push([parseInt(response.data.namea[_i1].times),parseInt(response.data.namea[_i1].buytimes)]);
            }
            for(_i2=0;_i2<response.data.nameb.length;_i2++){
                _data2.push([parseInt(response.data.nameb[_i2].times),parseInt(response.data.nameb[_i2].buytimes)]);
            }
            for(_i3=0;_i3<response.data.namec.length;_i3++){
                _data3.push([parseInt(response.data.namec[_i3].times),parseInt(response.data.namec[_i3].buytimes)]);
            }
            for(_i4=0;_i4<response.data.named.length;_i4++){
                _data4.push([parseInt(response.data.named[_i4].times),parseInt(response.data.named[_i4].buytimes)]);
            }
            var xiangXian = {
                color :['#da7982','#57b1ed','#2dc6cb','#ffb981'],
                title : {
                    text: '用户象限分析图'
                },
                tooltip : {
                    trigger: 'axis',
                    showDelay : 0,
                    formatter : function (params) {
                        if (params.value.length > 1) {
                            return params.seriesName + ' : '
                                + params.value[1];
                        }
                        else {
                            return params.seriesName + ' : '
                                + params.name + ' : '
                                + params.value;
                        }
                    },
                    axisPointer:{
                        show: true,
                        type : 'cross',
                        lineStyle: {
                            type : 'dashed',
                            width : 1
                        }
                    }
                },
                legend: {
                    data:['购物冲动型','目标明确型','理想比较型','海淘犹豫型']
                },
                dataZoom : {
                    show : true,
                    start : 0,
                    end : 100
                },
                toolbox: {
                    show : true,
                    y : 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                xAxis : [
                    {
                        name:'用户签收至\n 购买时长\n(单位:小时)',
                        type : 'value',
                        scale:true,
                        axisLabel : {
                            formatter: '{value}'
                        }
                    }
                ],
                yAxis : [
                    {
                        name:'用户检测购买数量：次',
                        type : 'value',
                        scale:true,
                        axisLabel : {
                            formatter: '{value}'
                        }
                    }
                ],
                series : [
                    {
                        name:'购物冲动型',
                        type:'scatter',
                        data: _data1
                    },
                    {
                        name:'目标明确型',
                        type:'scatter',
                        data: _data2
                    },
                    {
                        name:'理想比较型',
                        type:'scatter',
                        data: _data3
                    },
                    {
                        name:'海淘犹豫型',
                        type:'scatter',
                        data: _data4
                    }
                ]
            };
            var myChart1 = echarts.init(document.getElementById('xiangXianChart'));
            myChart1.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 15
                }
            });
            clearTimeout(keHuLoading);
            keHuLoading = setTimeout(function (){
                myChart1.hideLoading();
                myChart1.setOption(xiangXian);
            },2000);
            myChart1.setTheme({scatter:{symbol: 'circle'}});
            window.onresize = function () {
                myChart1.resize();
            };
        }
    });
});
/**
 *点击改变颜色
 */
function xiangXianChange(obj){
    $(obj).addClass('chosen-active').siblings().removeClass('chosen-active');
    $('#start-time').val('');
    $('#end-time').val('');
}
/**
 *点击确定
 */
function seeXiangXian(){
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
        _paramType=$('#xiangxian-content').val(),
        _url,
        _dateType,
        _json;
    if(date1!='' && date2!=''){  //选择的是自定义时间
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
            $('.time-list').find('li').removeClass('chosen-active');
            var _startTime=date1.split('-').join('');
            var _endTime=date2.split('-').join('');
            _json={
                paramType:_paramType,
                dateType:0,
                startTime:_startTime,
                endTime:_endTime
            };
        }
    }else{
        if(date1!='' || date2!='') {  //自定义时间只选择了一边
            alert('请选择正确的时间段');
            return false;
        }else{  //快速选择时间
            _dateType=$('.time-list').find('.chosen-active').index();
            switch (parseInt(_dateType)){
                case 0:_dateType=1;break;
                case 1:_dateType=3;break;
                case 2:_dateType=6;break;
                case 3:_dateType=4;break;
                case 4:_dateType=5;break;
            }
            _json={
                paramType:_paramType,
                dateType:_dateType
            };
        }
    }
    if(parseInt(_paramType)==0){  //全查接口
        _url='/orderanalysis/quadrantAnalysisFirst';
        $.ajax({
            url:SLSCHTTP+_url,
            type:'post',
            data:_json,
            dataType:'json',
            success:function(response){
                var _i1,_i2,_i3,_i4,_data1=[],_data2=[],_data3=[],_data4=[],keHuLoading;
                for(_i1=0;_i1<response.data.namea.length;_i1++){
                    _data1.push([parseInt(response.data.namea[_i1].times),parseInt(response.data.namea[_i1].buytimes)]);
                }
                for(_i2=0;_i2<response.data.nameb.length;_i2++){
                    _data2.push([parseInt(response.data.nameb[_i2].times),parseInt(response.data.nameb[_i2].buytimes)]);
                }
                for(_i3=0;_i3<response.data.namec.length;_i3++){
                    _data3.push([parseInt(response.data.namec[_i3].times),parseInt(response.data.namec[_i3].buytimes)]);
                }
                for(_i4=0;_i4<response.data.named.length;_i4++){
                    _data4.push([parseInt(response.data.named[_i4].times),parseInt(response.data.named[_i4].buytimes)]);
                }
                var xiangXian = {
                    color :['#da7982','#57b1ed','#2dc6cb','#ffb981','#b6a2e1'],
                    title : {
                        text: '用户象限分析图'
                    },
                    tooltip : {
                        trigger: 'axis',
                        showDelay : 0,
                        formatter : function (params) {
                            if (params.value.length > 1) {
                                return params.seriesName + ' : '
                                    + params.value[1];
                            }
                            else {
                                return params.seriesName + ' : '
                                    + params.name + ' : '
                                    + params.value;
                            }
                        },
                        axisPointer:{
                            show: true,
                            type : 'cross',
                            lineStyle: {
                                type : 'dashed',
                                width : 1
                            }
                        }
                    },
                    legend: {
                        data:['购物冲动型','目标明确型','理想比较型','海淘犹豫型']
                    },
                    dataZoom : {
                        show : true,
                        start : 0,
                        end : 100
                    },
                    toolbox: {
                        show : true,
                        y : 'center',
                        feature : {
                            restore : {show: true}
                        }
                    },
                    xAxis : [
                        {
                            name:'用户签收至\n 购买时长\n(单位:小时)',
                            type : 'value',
                            scale:true,
                            axisLabel : {
                                formatter: '{value}'
                            }
                        }
                    ],
                    yAxis : [
                        {
                            name:'用户检测购买数量：次',
                            type : 'value',
                            scale:true,
                            axisLabel : {
                                formatter: '{value}'
                            }
                        }
                    ],
                    series : [
                        {
                            name:'购物冲动型',
                            type:'scatter',
                            data: _data1
                        },
                        {
                            name:'目标明确型',
                            type:'scatter',
                            data: _data2
                        },
                        {
                            name:'理想比较型',
                            type:'scatter',
                            data: _data3
                        },
                        {
                            name:'海淘犹豫型',
                            type:'scatter',
                            data: _data4
                        }
                    ]
                };
                var myChart1 = echarts.init(document.getElementById('xiangXianChart'));
                myChart1.showLoading({  //载入动画
                    text : '请稍等',
                    effect : 'whirling',
                    textStyle : {
                        fontSize : 15
                    }
                });
                clearTimeout(keHuLoading);
                keHuLoading = setTimeout(function (){
                    myChart1.hideLoading();
                    myChart1.setOption(xiangXian);
                },2000);
                myChart1.setTheme({scatter:{symbol: 'circle'}});
                window.onresize = function () {
                    myChart1.resize();
                };
            }
        });
    }else{  //具体类型接口
        _url='/orderanalysis/quadrantAnalysis';
        var _type;
        switch (parseInt(_paramType)){
            case 1:_type='购物冲动型';break;
            case 2:_type='目标明确型';break;
            case 3:_type='理想比较型';break;
            case 4:_type='海淘犹豫型';break;
        }
        $.ajax({
            url:SLSCHTTP+_url,
            type:'post',
            data:_json,
            dataType:'json',
            success:function(response){
                var _i1,_data1=[],keHuLoading;
                for(_i1=0;_i1<response.data.length;_i1++){
                    _data1.push([parseInt(response.data[_i1].times),parseInt(response.data[_i1].buytimes)]);
                }
                var xiangXian = {
                    title : {
                        text: '用户象限分析图'
                    },
                    tooltip : {
                        trigger: 'axis',
                        showDelay : 0,
                        formatter : function (params) {
                            if (params.value.length > 1) {
                                return params.seriesName + ' : '
                                    + params.value[1];
                            }
                            else {
                                return params.seriesName + ' : '
                                    + params.name + ' : '
                                    + params.value;
                            }
                        },
                        axisPointer:{
                            show: true,
                            type : 'cross',
                            lineStyle: {
                                type : 'dashed',
                                width : 1
                            }
                        }
                    },
                    legend: {
                        data:[_type]
                    },
                    dataZoom : {
                        show : true,
                        start : 0,
                        end : 100
                    },
                    toolbox: {
                        show : true,
                        y : 'center',
                        feature : {
                            restore : {show: true}
                        }
                    },
                    xAxis : [
                        {
                            name:'用户签收至\n 购买时长\n(单位:小时)',
                            type : 'value',
                            scale:true,
                            axisLabel : {
                                formatter: '{value}'
                            }
                        }
                    ],
                    yAxis : [
                        {
                            name:'用户检测购买数量：次',
                            type : 'value',
                            scale:true,
                            axisLabel : {
                                formatter: '{value}'
                            }
                        }
                    ],
                    series : [
                        {
                            name:_type,
                            type:'scatter',
                            data: _data1
                        }
                    ]
                };
                var myChart1 = echarts.init(document.getElementById('xiangXianChart'));
                myChart1.showLoading({  //载入动画
                    text : '请稍等',
                    effect : 'whirling',
                    textStyle : {
                        fontSize : 15
                    }
                });
                clearTimeout(keHuLoading);
                keHuLoading = setTimeout(function (){
                    myChart1.hideLoading();
                    myChart1.setOption(xiangXian);
                },2000);
                myChart1.setTheme({scatter:{symbol: 'circle'}});
                window.onresize = function () {
                    myChart1.resize();
                };
            }
        });
    }
}