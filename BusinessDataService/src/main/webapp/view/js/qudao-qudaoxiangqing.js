var chart1,chart3,chart4;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('渠道详情');
    var channel=decodeURI(window.location.search.split('=')[1]);
//    var channel='京东';
    var time='近三个月';
    var json={
        channelName:channel,
        dateType:'1',
        saleType:'1'
    };
    $.when(
        $.ajax({  //下拉框渠道名
            url:AJAXURL.getAllChannelName,
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({  //图1
            url:AJAXURL.getOnlineChannelSale,
            type:'post',
            data:json,
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getChannelGoodTop,
            type:'post',
            data:json,
            dataType:'json'
        }),
        $.ajax({  //图3
            url:AJAXURL.getChannelShopTop,
            type:'post',
            data:json,
            dataType:'json'
        })
    ).done(function(response1,response2,response3,response4){
//  下拉框所有渠道
            if(response1[0].state == true){
                var channelAll = response1[0].data;
                for(var i=0;i<channelAll.length;i++){
                    $('#channelList').append('<option value="' + channelAll[i].channel + '">' + channelAll[i].channel + '</option>');
                }
                $('#channelList').val(channel);
            }
//  图1
            var chartOneData = response2[0].data;
            var chartOneX = [],chartOneD = [],formatter='销售额',danwei='万元';
            for(var i1=0;i1<chartOneData.length;i1++){
                var datastr = (parseFloat(chartOneData[i1].total)/10000).toString();
                chartOneD.push(datastr.substring(0,datastr.indexOf('.')+3));
                chartOneX.push(chartOneData[i1].day);
            }
            chartOne(chartOneX,chartOneD,formatter,danwei,channel,time);
//  图2
            var chartThreeData = response3[0].data;
            var chartThreeX = [],chartThreeD = [];
            for(var i3=0;i3<chartThreeData.length;i3++){
                var datastr3 = (parseFloat(chartThreeData[i3].total)/10000).toString();
                chartThreeD.push({
                    value:datastr3.substring(0,datastr3.indexOf('.')+3),
                    name:chartThreeData[i3].goodName,
                    goodsid:chartThreeData[i3].goodsid,
                    goodsbrand:chartThreeData[i3].goodsbrand,
                    channel:chartThreeData[i3].channel
                });
                chartThreeX.push(chartThreeData[i3].goodName);
            }
            chartThree(chartThreeX,chartThreeD,formatter,danwei,channel,time);
//  图3
            var chartFourData = response4[0].data;
            var chartFourX = [],chartFourD = [];
            for(var i4=0;i4<chartFourData.length;i4++){
                var datastr4 = (parseFloat(chartFourData[i4].total)/10000).toString();
                chartFourD.push({
                    value:datastr4.substring(0,datastr4.indexOf('.')+3),
                    name:chartFourData[i4].shopName,
                    storeId:chartFourData[i4].store_id
                });
                chartFourX.push(chartFourData[i4].shopName);
            }
            chartFour(chartFourX,chartFourD,formatter,danwei,channel,time);
//  图表大小变化
            window.onresize = function(){
                chart1.resize();
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
function chartOne(chartOneX,chartOneD,formatter,danwei,channel,time){
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
        tooltip : {
            axisPointer :{
                type: 'none'
            },
            trigger: 'item',
            formatter:function(params){
                return params.name+'<br/>'+formatter+'：'+params.data+danwei;
            }
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
        //title: {
        //    text :  channel + '电商' + formatter + '图',
        //    x : 'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        calculable : true,
        dataZoom:{
            show : true,
            start:0,
            end:100
        },
        xAxis : [
            {
                splitLine : {
                    show: false
                },
                splitArea:{
                    show:false
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
                name : formatter+'：'+danwei
            }
        ],
        series : [
            {
                name:'柱状',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:chartOneD
            }
            //{
            //    name:'折线',
            //    smooth:true,
            //    type:'line',
            //    yAxisIndex: 1,
            //    data:chartOneD
            //}
        ]
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
}
//图2
function chartThree(chartThreeX,chartThreeD,formatter,danwei,channel,time){
    chart3 = echarts.init(document.getElementById('chart3'));
    chart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        title : {
            //text:  channel+'热销商品'+formatter+'TOP10',
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
                return params[0].name+'<br/>'+ formatter +'：'+params[0].value+danwei;
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
                data : chartThreeX
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
                name : '单位：'+danwei
            }
        ],
        series : [
            {
                smooth:true,
                type:'bar',
                barMaxWidth:30,
                itemStyle: {
                    normal: {
                        color: function(params) {
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
                data:chartThreeD
            }
        ]
    };
//  点击事件
    var ecConfig = echarts.config;
    chart3.on(ecConfig.EVENT.CLICK, function(params) {
        var goodName=encodeURI(params.data.name);
        var goodsId=params.data.goodsid;
        var channel=encodeURI(params.data.channel);
        var goodsbrand=encodeURI(params.data.goodsbrand);
        var _iframe=$(window.parent.document.getElementById('iframepage'));
        _iframe.attr('src','shangpin-shangpinxiangqing.html?goodName='+goodName+'&goodsId='+goodsId+'&channel='+channel+'&goodsbrand='+goodsbrand);
    });
    setTimeout(function (){
        chart3.hideLoading();
        chart3.setOption(option);
    },1000);
}
//图3
function chartFour(chartThreeX,chartThreeD,formatter,danwei,channel,time){
    chart4 = echarts.init(document.getElementById('chart4'));
    chart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        title : {
            //text:  channel+'热门店铺'+formatter+'TOP10',
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
                return params[0].name+'<br/>'+ formatter +'：'+params[0].value+danwei;
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
                data : chartThreeX
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
                name : '单位：'+danwei
            }
        ],
        series : [
            {
                smooth:true,
                type:'bar',
                barMaxWidth:30,
                itemStyle: {
                    normal: {
                        color: function(params) {
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
                data:chartThreeD
            }
        ]
    };
    setTimeout(function (){
        chart4.hideLoading();
        chart4.setOption(option);
    },1000);
//  点击事件
    var ecConfig = echarts.config;
    chart4.on(ecConfig.EVENT.CLICK, function(params) {
        var shopName=encodeURI(params.data.name);
        var shopId=params.data.storeId;
        var _iframe=$(window.parent.document.getElementById('iframepage'));
        _iframe.attr('src','dianpu-dianpuxiangqing.html?shopName='+shopName+'&shopId='+shopId);
    });
}
//三个图点击事件
$('#btn1').on('click',function(){
    var json;
    var channel=$('#channelList').val();
    var saleType=$(this).prev().find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            channelName:channel,
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getOnlineChannelSale,
            type:'post',
            data:json,
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getChannelGoodTop,
            type:'post',
            data:json,
            dataType:'json'
        }),
        $.ajax({  //图3
            url:AJAXURL.getChannelShopTop,
            type:'post',
            data:json,
            dataType:'json'
        })
    ).done(function(response2,response3,response4){
            //  图1
            var formatter,danwei;
            if(parseInt(saleType)==0){
                formatter='销售额';
                danwei='万元';
            }else{
                formatter='销售量';
                danwei='万件';
            }
            var chartOneData = response2[0].data;
            var chartOneX = [],chartOneD = [],time=startTime+'至'+endTime;
            for(var i1=0;i1<chartOneData.length;i1++){
                var datastr = (parseFloat(chartOneData[i1].total)/10000).toString();
                chartOneD.push(datastr.substring(0,datastr.indexOf('.')+3));
                chartOneX.push(chartOneData[i1].day);
            }
            chartOne(chartOneX,chartOneD,formatter,danwei,channel,time);
//  图2
            var chartThreeData = response3[0].data;
            var chartThreeX = [],chartThreeD = [];
            for(var i3=0;i3<chartThreeData.length;i3++){
                var datastr3 = (parseFloat(chartThreeData[i3].total)/10000).toString();
                chartThreeD.push({
                    value:datastr3.substring(0,datastr3.indexOf('.')+3),
                    name:chartThreeData[i3].goodName,
                    goodsid:chartThreeData[i3].goodsid,
                    goodsbrand:chartThreeData[i3].goodsbrand,
                    channel:chartThreeData[i3].channel
                });
                chartThreeX.push(chartThreeData[i3].goodName);
            }
            chartThree(chartThreeX,chartThreeD,formatter,danwei,channel,time);
//  图3
            var chartFourData = response4[0].data;
            var chartFourX = [],chartFourD = [];
            for(var i4=0;i4<chartFourData.length;i4++){
                var datastr4 = (parseFloat(chartFourData[i4].total)/10000).toString();
                chartFourD.push({
                    value:datastr4.substring(0,datastr4.indexOf('.')+3),
                    name:chartFourData[i4].shopName,
                    storeId:chartFourData[i4].store_id
                });
                chartFourX.push(chartFourData[i4].shopName);
            }
            chartFour(chartFourX,chartFourD,formatter,danwei,channel,time);
    });
});