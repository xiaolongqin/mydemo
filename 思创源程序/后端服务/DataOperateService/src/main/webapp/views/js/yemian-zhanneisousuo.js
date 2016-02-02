//球状函数
var BeautifullMath = function () {
    var obj = [], xm = 0, ym = 0, axe = 0, aye = 0, parts = 50, scr,aArr, txe, tye, nw, nh;
    var colorArr = ['#b6a2e1','#ffb981','#2dc6cb','#57b1ed','#da7982'];
    var addEvent = function (o, e, f) {
        window.addEventListener ? o.addEventListener(e, f, false) : o.attachEvent('on'+e, function(){f.call(o)})
    };
    var resize = function () {
        nw = scr.offsetWidth * .5;
        nh = scr.offsetHeight * .5;
    };
    var init = function (id, f) {
        scr = document.getElementById(id);
        aArr = document.getElementsByTagName('a');
        addEvent(document, 'mousemove', function(e){
            e = e || window.event;
            xm = e.clientX;
            ym = e.clientY;
        });
        resize();
        addEvent(window, 'resize', resize);
        __init(f);
        setInterval(run, 16);
    };
    var __init = function (f) {
        for (var i=0; i<aArr.length; i++) {
            var o = {};
            o.p = aArr[i];
            o.p.style.color = colorArr[Math.round(Math.random()*4)];
            var r = i/parts, j, a, b;
            j = i % parts;
            a = Math.floor(j)/200+(Math.floor(j/2)%10)/5* Math.PI * 2;
            b = Math.acos(-0.9+(j%4)*0.6);
            r = !!f?f(r):r-r*r+.5;
            var sbr = Math.sin(b) * r;
            o.x = Math.sin(a) * sbr;
            o.y = Math.cos(a) * sbr;
            o.z = Math.cos(b) * r;
            obj.push(o);
            o.transform = function () {
                var ax = .02 * txe,
                    ay = .02 * tye,
                    cx = Math.cos(ax),
                    sx = Math.sin(ax),
                    cy = Math.cos(ay),
                    sy = Math.sin(ay);
//rotation
                var z = this.y * sx + this.z * cx;
                this.y = this.y * cx + this.z * -sx;
                this.z = this.x * -sy + z * cy;
                this.x = this.x * cy + z * sy;
//3d
                var scale = 1 / (1 + this.z),
                    x = this.x * scale * nh + nw - scale * 2,
                    y = this.y * scale * nh + nh - scale * 2;
//set style
                var p = this.p.style;
                if (x >= 0 && y >=0 && x < nw * 2 && y < nh * 2) {
                    var c = Math.round(256 + (-this.z * 256));
                    p.left = Math.round(x) + 'px';
                    p.top = Math.round(y) + 'px';
                    p.fontSize = Math.round(15*scale) + 'px';
                    p.zIndex = 200 + Math.floor(-this.z * 100);
                    p.opacity = .8 - this.z;
                    p.filter = 'alpha(opacity='+100*(.8-this.z)+')';
                } else {
                    p.width = "0px"
                }
            }
        }
    };
//run function
    var run = function () {
        var se = 1 / nh;
        txe = (ym - axe) * se;
        tye = (xm - aye) * se;
        axe += txe;
        aye += tye;
        /* ---- anim particles ---- */
        for (var i = 0, o; o = obj[i]; i++) o.transform();
    };
    return {init:init}
}();
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
        url:SLSCHTTP+'/keyword/getKeyWords',
        type:'post',
        data:{dateType:1},
        dataType:'json',
        success:function(response){
//          展示球形
            var _ballData=[],_i,dataList='',_barData=[],_barBum=[],loadingTicket;
            for(var t=0;t<response.data.length;t++){
                if(t<20){
                    _ballData.push(response.data[t].keyword);
                }
                if(t<5){
                    _barData.push(response.data[t].keyword);
                    _barBum.push(response.data[t].searchtimes);
                }
            }
            for(_i=0;_i<_ballData.length;_i++){
                dataList+='<a>'+_ballData[_i]+'</a>';
            }
            $('#screen').html(dataList);
            BeautifullMath.init('screen', function(r){
                return .4;
            });
//          柱状图
            var option2 = {
                title : {
                    text: '热门关键词TOP5排名',
                    x:'center',
                    y:'bottom'
                },
                tooltip : {
                    trigger: 'axis'
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
                        data : _barData,
                        axisLabel: {
                            textStyle: {
                                fontFamily: '微软雅黑',
                                fontSize: 13
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name : '单位：次',
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
                                }
//                                label: {
//                                    show: true,
//                                    position: 'top',
//                                    formatter: '{b}\n{c}次',
//                                    textStyle: {
//                                        fontSize: 13,
//                                        fontFamily: '微软雅黑',
//                                        color: '#000'
//                                    }
//                                }
                            }
                        },
                        type:'bar',
                        barMinHeight: 10,
                        barWidth: 60,
                        data:_barBum
                    }
                ]
            };

            var myChart1 = echarts.init(document.getElementById('topWord'));
            myChart1.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 15
                }
            });
            clearTimeout(loadingTicket);
            loadingTicket = setTimeout(function (){
                myChart1.hideLoading();
                myChart1.setOption(option2);
            },2000);
            window.onresize = function () {
                myChart1.resize();
            };
            $('.charts-box').css({visibility:'visible'});
        }
    });
});
/**
 *快速选择时间
 */
function zhanNeiSearch(obj){
    $(obj).addClass('chosen-active').siblings().removeClass('chosen-active');
    var _dateType=$(obj).index();
    $('#start-time').val('');
    $('#end-time').val('');
    switch (parseInt(_dateType)){
        case 0:_dateType=1;break;
        case 1:_dateType=3;break;
        case 2:_dateType=6;break;
        case 3:_dateType=4;break;
        case 4:_dateType=5;break;
    }
    $.ajax({
        url:SLSCHTTP+'/keyword/getKeyWords',
        type:'post',
        data:{dateType:_dateType},
        dataType:'json',
        success:function(response){
//          展示球形
            var _ballData=[],_i,dataList='',_barData=[],_barBum=[],loadingTicket;
            for(var t=0;t<response.data.length;t++){
                if(t<20){
                    _ballData.push(response.data[t].keyword);
                }
                if(t<5){
                    _barData.push(response.data[t].keyword);
                    _barBum.push(response.data[t].searchtimes);
                }
            }
            for(_i=0;_i<_ballData.length;_i++){
                dataList+='<a>'+_ballData[_i]+'</a>';
            }
            $('#screen').html(dataList);
            BeautifullMath.init('screen', function(r){
                return .4;
            });
//          柱状图
            var option2 = {
                title : {
                    text: '热门关键词TOP5排名'
                },
                tooltip : {
                    trigger: 'axis'
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
                        data : _barData,
                        axisLabel: {
                            textStyle: {
                                fontFamily: '微软雅黑',
                                fontSize: 13
                            }
                        }
                    }
                ],
                yAxis : [
                    {
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
                                    formatter: '{b}\n{c}次',
                                    textStyle: {
                                        fontSize: 13,
                                        fontFamily: '微软雅黑',
                                        color: '#000'
                                    }
                                }
                            }
                        },
                        type:'bar',
                        barMinHeight: 10,
                        barWidth: 60,
                        data:_barBum
                    }
                ]
            };

            var myChart1 = echarts.init(document.getElementById('topWord'));
            myChart1.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 15
                }
            });
            clearTimeout(loadingTicket);
            loadingTicket = setTimeout(function (){
                myChart1.hideLoading();
                myChart1.setOption(option2);
            },2000);
            window.onresize = function () {
                myChart1.resize();
            };
            $('.charts-box').css({visibility:'visible'});
        }
    });
}
/**
 *自定义时间
 */
function seeCharts(){
    var date1=$('#start-time').val(),
        date2=$('#end-time').val();
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
            $.ajax({
                url:SLSCHTTP+'/keyword/getKeyWords',
                type:'post',
                data:{dateType:0,startTime:_startTime,endTime:_endTime},
                dataType:'json',
                success:function(response){
//          展示球形
                    var _ballData=[],_i,dataList='',_barData=[],_barBum=[],loadingTicket;
                    for(var t=0;t<response.data.length;t++){
                        if(t<20){
                            _ballData.push(response.data[t].keyword);
                        }
                        if(t<5){
                            _barData.push(response.data[t].keyword);
                            _barBum.push(response.data[t].searchtimes);
                        }
                    }
                    for(_i=0;_i<_ballData.length;_i++){
                        dataList+='<a>'+_ballData[_i]+'</a>';
                    }
                    $('#screen').html(dataList);
                    BeautifullMath.init('screen', function(r){
                        return .4;
                    });
//          柱状图
                    var option2 = {
                        title : {
                            text: '热门关键词TOP5排名'
                        },
                        tooltip : {
                            trigger: 'axis'
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
                                data : _barData,
                                axisLabel: {
                                    textStyle: {
                                        fontFamily: '微软雅黑',
                                        fontSize: 13
                                    }
                                }
                            }
                        ],
                        yAxis : [
                            {
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
                                            formatter: '{b}\n{c}次',
                                            textStyle: {
                                                fontSize: 13,
                                                fontFamily: '微软雅黑',
                                                color: '#000'
                                            }
                                        }
                                    }
                                },
                                type:'bar',
                                barMinHeight: 10,
                                barWidth: 60,
                                data:_barBum
                            }
                        ]
                    };

                    var myChart1 = echarts.init(document.getElementById('topWord'));
                    myChart1.showLoading({  //载入动画
                        text : '请稍等',
                        effect : 'whirling',
                        textStyle : {
                            fontSize : 15
                        }
                    });
                    clearTimeout(loadingTicket);
                    loadingTicket = setTimeout(function (){
                        myChart1.hideLoading();
                        myChart1.setOption(option2);
                    },2000);
                    window.onresize = function () {
                        myChart1.resize();
                    };
                    $('.charts-box').css({visibility:'visible'});
                }
            });
        }
    }else{
        alert('请选择正确的时间段');
    }
}