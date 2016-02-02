var chart1,chart2,chart3,chart4;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('渠道总览');
    $.when(
        $.ajax({  //上面两图
            url:AJAXURL.getOnlineChannelTop,
            type:'post',
            data:{
                'dateType':1
            },
            dataType:'json'
        }),
        $.ajax({  //下面两图
            url:AJAXURL.getDetailChannelTop,
            type:'post',
            data:{
                'dateType':1
            },
            dataType:'json'
        })
    ).done(function(response1,response2){
//      最上面两个图表  response1包括了两个图的数据
            var data1 = response1[0].data;
            var chartOneD = [],chartOneX = [],chartOneData = data1.first,time='近三个月';
            for(var i1=0;i1<chartOneData.length;i1++){
                var datastr = (parseFloat(chartOneData[i1].totalsale)/10000).toString();
                chartOneD.push(datastr.substring(0,datastr.indexOf('.')+3));
                chartOneX.push(chartOneData[i1].channel);
            }
            chartOne(chartOneX,chartOneD,time);
            var data2 = response1[0].data;
            var chartTwoLegend = [],chartTwoD = [],chartTwoData = data2.second;
            for(var i2=0;i2<chartTwoData.length;i2++){
                chartTwoLegend.push(chartTwoData[i2].channel);
                chartTwoD.push({
                    'value': chartTwoData[i2].percent,
                    'name' : chartTwoData[i2].channel
                })
            }
            chartTwo(chartTwoLegend,chartTwoD);
//      下面两个图
            var data4=response2[0].data.first;
            var chartFourX=[],chartFourD=[];
            for(var i4=0;i4<data4.length;i4++){
                var data4str = (parseFloat(data4[i4].total)/10000).toString();
                chartFourX.push(data4[i4].channel);
                chartFourD.push({
                    value:data4str.substring(0,data4str.indexOf('.')+3),
                    percent:data4[i4].percent
                });
            }
            chartThree(chartFourX,chartFourD,time);
            chartFour(parseFloat(data4[0].percent),data4[0].channel);
//  图表大小变化
            window.onresize = function(){
                chart1.resize();
                chart2.resize();
                chart3.resize();
                chart4.resize();
            };

            var iframe = $(window.parent.document.getElementById('iframepage'))[0];
            var bHeight = document.body.scrollHeight;
            var dHeight = document.documentElement.scrollHeight;
            var height = Math.min(bHeight, dHeight);
            iframe.height =  height+50;
            if(height<400){
                height=400;
            }
            $(window.parent.document).find('.left-nav').css('height',(height+40)+'px');
    });
});
//图1
function chartOne(chartOneX,chartOneData,time){
    chart1 = echarts.init(document.getElementById('chart1'));
    chart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        title : {
            //text: '线上电商销售额排名',
            x:'center',
            //textStyle:{
            //    fontSize:13,
            //    fontFamily:'Microsoft Yahei'
            //},
            subtext : '温馨提示：点击柱状图可查看详情',
            subtextStyle : {
                fontSize: 13,
                fontWeight: 'normal',
                color: 'red'
            }
        },
        tooltip : {
            axisPointer :{
                type: 'none'
            },
            trigger: 'axis',
            formatter:function(params){
                return params[0].name+'<br/>'+'销售额：'+params[0].value+'万元'
            }
        },
        toolbox: {
            show : true,
            feature : {
                //magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true}
            },
            orient : 'vertical',
            y : 'center'
        },
        legend: {
            data:['销售额'],
            x:'left',
            y:'35',
            show:false
        },
        dataZoom :{
            show : true,
            start : 0,
            end :100
        },
        calculable : true,
        xAxis : [
            {
                splitLine : {
                    show: false
                },
                type : 'category',
                data : chartOneX
            }
        ],
        yAxis : [
            {
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                },
                type : 'value',
                name : '单位：万元'
            }
        ],
        series : [
            {
                smooth:true,
                name:'销售额',
                type:'bar',
                barMaxWidth:30,
                itemStyle: {
                    normal: {
                        color: function(params) {
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
                data:chartOneData
            }
        ]
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
//  点击事件
    var ecConfig = echarts.config;
    chart1.on(ecConfig.EVENT.CLICK, function(params) {
        var name =encodeURI(params.name);
        $(window.parent.document.getElementById('iframepage')).attr('src','qudao-qudaoxiangqing.html?name='+name);
    });
}
//图2
function chartTwo(chartLegend,chartData){
    chart2 = echarts.init(document.getElementById('chart2'));
    chart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : AJAXURL.color,
        title : {
            text: new Date().getFullYear() + '年\n中国B2C网络购物交易市场份额占比图',
            x:'center',
            textStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei'
            }
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c}%"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:chartLegend
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            },
            orient : 'vertical',
            y : 'center'
        },
        calculable : true,
        series : [
            {
                name:'占比',
                type:'pie',
                radius : '40%',
                center: ['50%', '50%'],
                data:chartData,
                itemStyle:{
                    normal:{
                        label:{
                            show:true,
                            formatter:'{c}%'
                        }
                    }
                }
            }
        ]
    };
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
}
//图3
function chartThree(chartFourX,chartFourD,time){
    chart3 = echarts.init(document.getElementById('chart3'));
    chart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        //title : {
        //    text: '线上电商销售量排名',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'axis',
            axisPointer :{
                type: 'none'
            },
            formatter:function(params){
                return params[0].name+'<br/>'+'销售量：'+params[0].value+'万件'
            }
        },
        dataZoom :{
            show : true,
            start : 0,
            end :100
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            },
            orient : 'vertical',
            y : 'center'
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                splitLine : {
                    show : false
                },
                data : chartFourX
            }
        ],
        yAxis : [
            {
                type : 'value',
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                },
                name : '单位：万件'
            }
        ],
        series : [
            {
                smooth:true,
                barMaxWidth:30,
                type:'bar',
                itemStyle: {
                    normal: {
                        color: function(params) {
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
                data: chartFourD
            }
        ]
    };
    setTimeout(function (){
        chart3.hideLoading();
        chart3.setOption(option);
    },1000);
    var ecConfig=echarts.config;
    chart4=echarts.init(document.getElementById('chart4'));
    chart3.on(ecConfig.EVENT.HOVER,function(params){
        if(params.data.percent!=undefined){
            chartFour(params.data.percent,params.name);
        }
    });
}
//仪表盘
function chartFour(percent,name){
    var dataJson,realJson=percent;  //dataJson用来存放决定指针指向的数据，realJson用来存放真实数据(在仪表盘正中展现的数据)。目前确定的市场占有率是0-10为低，10-20为中，20以上为高
    if(realJson<=10){
        dataJson=realJson*2;
    }
    if(10<realJson<=20){
        dataJson=20+(realJson-10)*6;
    }
    if(realJson>20){
        dataJson=80+realJson*0.2;
    }
    var option = {
        title:{
            text: '市场占比率：低(0%-10%)'+' 中(10%-20%)'+' 高(20%以上)',
            x:'center',
            textStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei',
                color:'red',
                fontWeight:'normal'
            },
            subtext:'\n'+'\n'+name,
            subtextStyle:{
                fontSize:15,
                fontFamily:'Microsoft Yahei',
                color:'#000',
                fontWeight:'normal'
            }
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            },
            orient : 'vertical',
            y : 'center'
        },
        series : [
            {
                name:'市场占比率',
                type:'gauge',
                startAngle: 190,
                endAngle: -10,
                center : ['50%', '74%'],    // 默认全局居中
                radius : 180,
                axisLine: {            // 坐标轴线
                    lineStyle: {       // 属性lineStyle控制线条样式
                        width: 130,
                        color: [[0.2, '#6ae6e7'],[0.8, '#29b6f6'],[1, '#1885e8']]
                    }
                },
                splitLine: {show:false},  //控制仪表盘大刻度
                axisTick: {            // 坐标轴小标记
                    splitNumber: 1,   // 每份split细分多少段
                    length :12,        // 属性length控制线长
                    show:false
                },
                axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
                    formatter: function(v){
                        if(v==10){
                            return '低';
                        }
                        if(v==50){
                            return '中';
                        }
                        if(v==90){
                            return '高';
                        }
                    },
                    textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                        color: '#fff',
                        fontSize: 15,
                        fontWeight: 'bolder'
                    }
                },
                pointer: {
                    width:20,
                    length: '80%',
                    color: 'rgba(255, 255, 255, 0.8)'
                },
                title : {
                    show : true,
                    offsetCenter: [0, '-60%'],       // x, y，单位px
                    textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                        color: '#fff',
                        fontSize: 20
                    }
                },
                detail : {
                    show : true,
                    backgroundColor: 'rgba(0,0,0,0)',
                    borderWidth: 0,
                    borderColor: '#ccc',
                    width: 50,
                    height: 40,
                    offsetCenter: [0, -30],       // x, y，单位px
                    formatter:realJson+'%',
                    textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                        fontSize : 20
                    }
                },
                data:[{value: dataJson, name: '市场占比率'}]
            }
        ]
    };
    if(dataJson<0.5){
        dataJson=0.5;
    }
    option.series[0].data[0].value = dataJson;
    //chart4=echarts.init(document.getElementById('chart4'));
    chart4.setOption(option);
}
//上面两图点击事件
$('#btn1').on('click',function(){
    var json;
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({
        url:AJAXURL.getOnlineChannelTop,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data = response.data;
            var chartOneData = [],chartOneX = [],chartOneD = data.first,time;
            for(var i1=0;i1<chartOneD.length;i1++){
                var datastr = (parseFloat(chartOneD[i1].totalsale)/10000).toString();
                chartOneData.push(datastr.substring(0,datastr.indexOf('.')+3));
                chartOneX.push(chartOneD[i1].channel);
            }
            var chartTwoLegend = [],chartTwoData = [],chartTwoD = data.second;
            for(var i2=0;i2<chartTwoD.length;i2++){
                chartTwoLegend.push(chartTwoD[i2].channel);
                chartTwoData.push({
                    'value': chartTwoD[i2].percent,
                    'name' : chartTwoD[i2].channel
                })
            }
            time=startTime+'至'+endTime;
            chartOne(chartOneX,chartOneData,time);
            chartTwo(chartTwoLegend,chartTwoData);
        }
    });
});
//下面两图点击
$('#btn2').on('click',function(){
    var json;
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //下面两图
        url:AJAXURL.getDetailChannelTop,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data4=response.data.first;
            var chartFourX=[],chartFourD=[],time;
            for(var i4=0;i4<data4.length;i4++){
                var data4str = (parseFloat(data4[i4].total)/10000).toString();
                chartFourX.push(data4[i4].channel);
                chartFourD.push({
                    value:data4str.substring(0,data4str.indexOf('.')+3),
                    percent:data4[i4].percent
                });
            }
            time=startTime+'至'+endTime;
            chartThree(chartFourX,chartFourD,time);
            chartFour(parseFloat(data4[0].percent),data4[0].channel);
        }
    });
});