var chart1,chart3,chart4,chart5;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('市场行业细分');
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getDetailMarket,
            type:'post',
            data:{
                dateType:1,
                channelList:AJAXURL.channel1
            },
            dataType:'json'
        }),
        $.ajax({  //图2联动图
            url:AJAXURL.getDetailPrice,
            type:'post',
            data:{
                dateType:1
            },
            dataType:'json'
        }),
        $.ajax({  //图3
            url:AJAXURL.getUserFocus,
            type:'post',
            data:{
                dateType:1
            },
            dataType:'json'
        })
    ).done(function(response1,response2,response3){
//  图1
            var data1=response1[0].data,chartOneD=[],chartOneX=[],oneData,danwei1,formatter1,time1='近三个月';
            for(var i1=0;i1<data1.length;i1++){
                chartOneX.push(data1[i1].day);
                oneData=(parseFloat(data1[i1].total)/10000).toString();
                chartOneD.push(oneData.substring(0,oneData.indexOf('.')+3));
            }
            danwei1='万元';
            formatter1='销售额';
            chartOne(chartOneX,chartOneD,danwei1,formatter1,time1);
//  图2  联动图
            var data4=response2[0].data,fourData,chartThreeL=[],chartThreeJson1=[],chartThreeJson2=[],danwei3;
            for(var i4=0;i4<data4.length;i4++){
                fourData=(parseFloat(data4[i4].total_num)/10000).toString();
                chartThreeL.push(data4[i4].name);
                chartThreeJson1.push({
                    name: data4[i4].name,
                    type:'bar',
                    data:[fourData.substring(0,fourData.indexOf('.')+3)]
                    //stack:'总量',
                    //itemStyle : {
                    //    normal: {
                    //        label : {
                    //            show: true,
                    //            position: 'inside',
                    //            textStyle:{color:'#000'},
                    //            formatter:function(params){
                    //                return params.seriesName;
                    //            }
                    //        }
                    //    }
                    //},
                });
                chartThreeJson2.push({
                    value:fourData.substring(0,fourData.indexOf('.')+3),
                    name: data4[i4].name,
                    A: data4[i4].A
                });
            }
            danwei3='万件';
            chartThree(chartThreeL,chartThreeJson1,chartThreeJson2,danwei3,time1);
//      图4
            var data5=response3[0].data;
            var attr5,textFive=[],chartFiveD=[];
            for(attr5 in data5[0]){
                textFive.push({
                    'text':attr5
                });
                chartFiveD.push(data5[0][attr5].toFixed(4));
            }
            chartFour(textFive,chartFiveD,time1);
//          渠道
            channelLists();
            var strTd='',strTr='',strAll='',strLi='';
            var channelList=[];
            for(var j=0;j<AJAXURL.channel2.length;j++){
                strLi+='<li><span>'+AJAXURL.channel2[j]+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
                channelList.push(AJAXURL.channel2[j]);
                strTd+='<td><div class="channel-name-box channel-active" onclick="channelChange(this);">'+AJAXURL.channel2[j]+'</div></td>';
                if((j+1)%3==0){  //每三个一行
                    strTr='<tr>'+strTd+'</tr>';
                    strAll+=strTr;
                    strTr='';
                    strTd='';
                }
            }
            strTr='<tr>'+strTd+'</tr>';  //每三个一行最后剩下的再为一行
            strAll+=strTr;
            $('.channel-box').eq(1).find('tbody').html(strAll);
            $('.channel-row').eq(1).find('ul').html('<li>渠道: </li>'+strLi);
            $('.channel-box').eq(2).find('tbody').html(strAll);
            $('.channel-row').eq(2).find('ul').html('<li>渠道: </li>'+strLi);
//          图表大小变化
            window.onresize = function(){
                chart1.resize();
                chart3.resize();
                chart4.resize();
                chart5.resize();
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
function chartOne(chartOneX,chartOneD,danwei,formatter,time){
    chart1 = echarts.init(document.getElementById('chart1'));
    chart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#29b6f6'],
        //title : {
        //    text: '行业'+formatter,
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type: 'shadow'
            },
            formatter : function(params){
                return params.name+'：<br/>'+params.value+danwei
            }
        },
        legend: {
            show:false,
            data:[]
        },
        toolbox: {
            show : true,
            orient : 'vertical',
            y:'center',
            feature : {
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true}
            }
        },
        dataZoom:{
            show:true,
            start:0,
            end:100
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                splitLine : {
                    show:false
                },
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
                name : formatter+'：'+danwei
            }
        ],
        series : [
            {
                smooth:true,
                type:'bar',
                barMaxWidth:30,
                data:chartOneD
            }
        ]
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
}
//图2 联动
function chartThree(chartThreeL,chartThreeJson1,chartThreeJson2,danwei3,time){
    chart3 = echarts.init(document.getElementById('chart3'));
    chart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    chart4 = echarts.init(document.getElementById('chart4'));
    chart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : AJAXURL.color,
        //title : {
        //    text: '行业价格分布',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type: 'shadow'
            },
            formatter:function(params){
                return params.seriesName+'：'+params.data+'万件';
            }
        },
        legend: {
            data:chartThreeL,
            show:false
        },
        toolbox: {
            show : true,
            orient : 'vertical',
            y : 'center',
            feature : {
                magicType : {show: true, type: ['stack', 'tiled']},
                restore : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                splitLine:{
                    show:false
                },
                data:[''],
                show:false
            }
        ],
        yAxis : [
            {
                type : 'value',
                splitArea : {show : true},
                name:'单位：'+danwei3
            }
        ],
        grid: {
            x:80
        },
        dataZoom:{
            show:true,
            start:0,
            end:100
        },
        series : chartThreeJson1
    };
    var option2 = {
        color : AJAXURL.color,
        title : {
            text:'温馨提示：点击饼图可查看详情',
            textStyle:{
                fontSize: 12,
                color: 'red',
                fontWeight: 'normal'
            },
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{b} : {c}万件({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:chartThreeL
        },
        calculable : true,
        series : [
            {
                type:'pie',
                radius : '45%',
                center: ['50%', '50%'],
                data:chartThreeJson2,
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
    setTimeout(function (){
        chart3.hideLoading();
        chart3.setOption(option);
    },1000);
    setTimeout(function (){
        chart4.hideLoading();
        chart4.setOption(option2);
    },1000);
    chart4.connect(chart3);
    chart3.connect(chart4);
//  点击事件
    var ecConfig = echarts.config;
    chart4.on(ecConfig.EVENT.CLICK, function(params) {
        var name =params.data.A;  //A1  后台要的商品标识符
        var price = encodeURI(params.name);  //商品价格区段
        $(window.parent.document.getElementById('iframepage')).attr('src','fangyan-shichanghangye-child.html?name='+name+'&price='+price);
    });
}
//图3 雷达图
function chartFour(textFive,chartFiveD,time){
    chart5 = echarts.init(document.getElementById('chart5'));
    chart5.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#a9e2fb'],  //控制边框和面积色
        title : {
            text: '用户关注点',
            x:'center',
            textStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei'
            }
        },
        tooltip : {
            trigger: 'axis',
            formatter: function(params){
                return params[0].indicator+'：'+params[0].value;
            },
            textStyle:{
                fontSize:'12px'
            }
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            },
            orient:'vertical',
            y:'center'
        },
        calculable: true,
        polar : [
            {
                indicator : textFive,
                center : ['50%','50%'],  //控制圆心位置
                radius : 150,
                startAngle: 90,
                splitNumber: 8,
                name : {
                    formatter: '{value}',
                    textStyle: {color:'#000'}
                },
                scale: true,
                type: 'circle',
                axisLine: {            // 坐标轴线
                    show: true,        // 默认显示，属性show控制显示与否
                    lineStyle: {       // 属性lineStyle控制线条样式
                        color: '#d6dee6',
                        width: 1,
                        type: 'solid'
                    }
                },
                splitArea : {
                    show : true,
                    areaStyle : {
                        color: ['#edf2f5','#ffffff']
                    }
                }
            }
        ],
        series : [
            {
                name: '雷达图',
                type: 'radar',
                itemStyle: {
                    emphasis: {
                        lineStyle: {
                            width: 4
                        }
                    },
                    normal: {
                        areaStyle: {
                            type: 'default'
                        }
                    }
                },
                data : [
                    {
                        value : chartFiveD,
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    type: 'solid'
                                }
                            }
                        }
                    }
                ]
            }
        ]
    };
    setTimeout(function (){
        chart5.hideLoading();
        chart5.setOption(option);
    },1000);
}
//图1点击事件
$('#btn1').on('click',function(){
    var channelList=[];
    var channelAll=$(this).next().find('.channel-table').find('.channel-active');
    for(var i=0;i<channelAll.length;i++){
        channelList.push(channelAll.eq(i).text())
    }
    if(channelList.length==0){
        alert('至少选择一个渠道');
        return false;
    }
    var json;
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            channelList:channelList
        };
    }
    $.ajax({
        url:AJAXURL.getDetailMarket,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data1=response.data,chartOneD=[],chartOneX=[],oneData,danwei1,formatter1,time1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneX.push(data1[i1].day);
                oneData=(parseFloat(data1[i1].total)/10000).toString();
                chartOneD.push(oneData.substring(0,oneData.indexOf('.')+3));
            }
            if(parseInt(saleType)==0){
                danwei1='万元';
                formatter1='销售额';
            }else if(parseInt(saleType)==1){
                danwei1='万件';
                formatter1='销售量';
            }else{
                danwei1='万条';
                formatter1='评论数';
            }
            time1=startTime+'至'+endTime;
            chartOne(chartOneX,chartOneD,danwei1,formatter1,time1);
        }
    });
});
//图2点击事件
$('#btn2').on('click',function(){
    var channelList=[];
    var channelAll=$(this).next().find('.channel-table').find('.channel-active');
    for(var i=0;i<channelAll.length;i++){
        channelList.push(channelAll.eq(i).text())
    }
    if(channelList.length==0){
        alert('至少选择一个渠道');
        return false;
    }
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
            endTime:endTime,
            channelList:channelList
        };
    }
    $.ajax({
        url:AJAXURL.getDetailPrice,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data4=response.data,fourData,chartThreeL=[],chartThreeJson1=[],chartThreeJson2=[],danwei3,time;
            for(var i4=0;i4<data4.length;i4++){
                fourData=(parseFloat(data4[i4].total_num)/10000).toString();
                chartThreeL.push(data4[i4].name);
                chartThreeJson1.push({
                    name: data4[i4].name,
                    type:'bar',
                    data:[fourData.substring(0,fourData.indexOf('.')+3)]
                    //stack:'总量',
                    //itemStyle : {
                    //    normal: {
                    //        label : {
                    //            show: true,
                    //            position: 'inside',
                    //            textStyle:{color:'#000'},
                    //            formatter:function(params){
                    //                return params.seriesName;
                    //            }
                    //        }
                    //    }
                    //},
                });
                chartThreeJson2.push({
                    value:fourData.substring(0,fourData.indexOf('.')+3),
                    name: data4[i4].name,
                    A: data4[i4].A
                });
            }
            danwei3='万件';
            time=startTime+'至'+endTime;
            chartThree(chartThreeL,chartThreeJson1,chartThreeJson2,danwei3,time);
        }
    })
});
//图3点击事件
$('#btn3').on('click',function(){
    var channelList=[];  //目前后台因为该模块查询渠道过于耗时，所以实际上并未使用channelList这个字段，而是查询的全部渠道
    var channelAll=$(this).next().find('.channel-table').find('.channel-active');
    for(var i=0;i<channelAll.length;i++){
        channelList.push(channelAll.eq(i).text())
    }
    if(channelList.length==0){
        alert('至少选择一个渠道');
        return false;
    }
    var json;
    var startTime=$('#myCalender5').val();
    var endTime=$('#myCalender6').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            channelList:channelList
        };
    }
    $.ajax({  //图3
        url:AJAXURL.getUserFocus,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data5=response.data;
            var attr5,textFive=[],chartFiveD=[],time;
            for(attr5 in data5[0]){
                textFive.push({
                    'text':attr5
                });
                chartFiveD.push(data5[0][attr5].toFixed(4));
            }
            time=startTime+'至'+endTime;
            chartFour(textFive,chartFiveD,time);
        }
    })
});