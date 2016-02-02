var chart1,chart2;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('店铺对比详情');
    var href=window.location.search.split('&');
    var storeNameList=decodeURI(href[0].split('=')[1]).split(',');
    var storeIdList=decodeURI(href[1].split('=')[1]).split(',');
//    var storeNameList=["酒泉网旗舰店", "聚象酒类专营店"];
//    var storeIdList=["jdshop_136731", "jdshop_mingjiu"];
    $('#storeNameList').val(storeNameList);
    $('#storeIdList').val(storeIdList);
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getStoreSaleduibi,
            type:'post',
            data:{
                storeNameList:storeNameList,
                storeIdList:storeIdList,
                dateType:1,
                saleType:1
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getStoreAttrduibi,
            type:'post',
            data:{
                storeNameList:storeNameList,
                storeIdList:storeIdList,
                dateType:1
            },
            dataType:'json'
        })
    ).done(function(response1,response2){
            var time='近三个月';
//  图1
            var data1=response1[0].data;
            var chartOneL=[],chartOneX=[],chartOneD=[],oneData=[],oneAttr=[],attr1,danwei1,formatter1;
            for(attr1 in data1){
                chartOneL.push(attr1);
                oneAttr.push(attr1);
            }
            for(var i11=0;i11<data1[oneAttr[0]].length;i11++){
                chartOneX.push(data1[oneAttr[0]][i11].day);
            }
            for(var i12=0;i12<oneAttr.length;i12++){
                oneData=[];
                for(var i13=0;i13<data1[oneAttr[i12]].length;i13++){
                    oneData.push(data1[oneAttr[i12]][i13].total);
                }
                chartOneD.push({
                    name:chartOneL[i12],
                    type:'line',
                    smooth:true,
                    data:oneData
                });
            }
            danwei1='万元';
            formatter1='销售额';
            chartOne(chartOneL,chartOneX,chartOneD,danwei1,time,formatter1);
//  图2
            var data2=response2[0].data;
            var chartTwoY=[],fourData1=[],fourData2=[],fourData3=[];  //1.同行好评率;2.店铺评分;3.发货速度好评率;
            for(var i2=0;i2<data2.length;i2++){
                chartTwoY.push(data2[i2].store_name);
                fourData1.push(data2[i2].store_industry_score);
                fourData2.push({
                    value:data2[i2].store_score*(-10),  //分数乘以10以免柱状条太短
                    name:data2[i2].store_score  //实际分数
                });
                fourData3.push(data2[i2].store_delivery_score);
            }
            chartTwo(chartTwoY,fourData1,fourData2,fourData3,time);
//  图表大小
            window.onresize=function(){
                chart1.resize();
                chart2.resize();
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
function chartOne(chartOneL,chartOneX,chartOneD,danwei,time,formatter){
    chart1 = echarts.init(document.getElementById('chart1'));
    chart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#219eef','#2fc4f1','#5bdaac','#9bce4a','#ced95d'],
        //title : {
        //    text: '店铺'+formatter+'对比',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'axis',
            axisPointer : {
                type:'shadow'
            },
            formatter:function(params){
                var str=params[0].name+'：<br/>';
                for(var i=0;i<params.length;i++){
                    str+=params[i].seriesName+'：'+params[i].value+danwei+'<br/>';
                }
                return str;
            }
        },
        legend: {
            data:chartOneL
        },
        toolbox: {
            show : true,
            feature : {
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true}
            },
            orient: 'vertical',
            x:'right',
            y:'center'
        },
        calculable : true,
        dataZoom:{
            show:true,
            start:0,
            end:100
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                splitLine:{
                    show:false
                },
                data : chartOneX
            }
        ],
        yAxis : [
            {
                name : formatter+'：'+danwei,
                type : 'value',
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                }
            }
        ],
        series : chartOneD
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
}
//图2
function chartTwo(chartTwoY,fourData1,fourData2,fourData3,time){
    chart2 = echarts.init(document.getElementById('chart2'));
    chart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#9bce4b','#1985e8','#4bce70'],
        title : {
            text: '店铺指标对比',
            x:'center',
            textStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei'
            }
        },
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter:function(params){
                return params[0].seriesName+'：'+params[0].value+'%<br/>'+params[1].seriesName+'：'+params[1].data.name+'分<br/>'+params[2].seriesName+'：'+params[2].value+'%';
            }
        },
        legend: {
            data:['店铺服务态度','产品描述服务度','店铺发货速度'],
            y:30
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            },
            orient: 'vertical',
            x:'right',
            y:'center'
        },
        calculable : true,
        grid:{
            x:150
        },
        xAxis : [
            {
                type : 'value',
                show : false,
                splitLine:{
                    show:false
                }
            }
        ],
        yAxis : [
            {
                type : 'category',
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                },
                axisTick : {show: false},
                data : chartTwoY
            }
        ],
        series : [
            {
                name:'店铺服务态度',
                type:'bar',
                barWidth : 10,
                data:fourData1
            },
            {
                name:'产品描述服务度',
                type:'bar',
                stack:'总量',
                barWidth : 10,
                data:fourData2
            },
            {
                name:'店铺发货速度',
                type:'bar',
                barWidth : 10,
                data:fourData3
            }
        ]
    };
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
}
//图1点击事件
$('#btn1').on('click',function(){
    var json;
    var storeNameList=$('#storeNameList').val();
    var storeIdList=$('#storeIdList').val();
//    var list=$(this).parent().next().find('.channel-active');
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            storeNameList:storeNameList.split(','),
            storeIdList:storeIdList.split(','),
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            saleType:parseInt(saleType)+1
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getStoreSaleduibi,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var time=startTime+'至'+endTime;
            var data1=response.data;
            var chartOneL=[],chartOneX=[],chartOneD=[],oneData=[],oneAttr=[],attr1,danwei1,formatter1;
            for(attr1 in data1){
                chartOneL.push(attr1);
                oneAttr.push(attr1);
            }
            for(var i11=0;i11<data1[oneAttr[0]].length;i11++){
                chartOneX.push(data1[oneAttr[0]][i11].day);
            }
            for(var i12=0;i12<oneAttr.length;i12++){
                oneData=[];
                for(var i13=0;i13<data1[oneAttr[i12]].length;i13++){
                    oneData.push(data1[oneAttr[i12]][i13].total);
                }
                chartOneD.push({
                    name:chartOneL[i12],
                    type:'line',
                    smooth:true,
                    data:oneData
                });
            }
            if(parseInt(saleType)==0){
                danwei1='万元';
                formatter1='销售额';
            }else{
                danwei1='万件';
                formatter1='销售量';
            }
            chartOne(chartOneL,chartOneX,chartOneD,danwei1,time,formatter1);
        }
    });
});
//图2点击事件
$('#btn2').on('click',function(){
    var json;
    var storeNameList=$('#storeNameList').val();
    var storeIdList=$('#storeIdList').val();
//    var list=$(this).parent().next().find('.channel-active');
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            storeNameList:storeNameList.split(','),
            storeIdList:storeIdList.split(','),
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图2
        url:AJAXURL.getStoreAttrduibi,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data2=response.data;
            var chartTwoY=[],fourData1=[],fourData2=[],fourData3=[];  //1.同行好评率;2.店铺评分;3.发货速度好评率;
            var time;
            for(var i2=0;i2<data2.length;i2++){
                chartTwoY.push(data2[i2].store_name);
                fourData1.push(data2[i2].store_industry_score);
                fourData2.push({
                    value:data2[i2].store_score*(-10),  //分数乘以10以免柱状条太短
                    name:data2[i2].store_score  //实际分数
                });
                fourData3.push(data2[i2].store_delivery_score);
            }
            time=startTime+'至'+endTime;
            chartTwo(chartTwoY,fourData1,fourData2,fourData3,time);
        }
    });
});