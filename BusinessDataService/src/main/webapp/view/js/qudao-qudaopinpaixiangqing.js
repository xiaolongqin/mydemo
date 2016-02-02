var chart1,chart2,chart3;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('品牌渠道详情');
    var href,goodsbrand,channel;
    href=window.location.search.split('&');
    goodsbrand=decodeURI(href[0].split('=')[1]);
    channel=decodeURI(href[1].split('=')[1]);
//    goodsbrand='茅台';
//    channel='天猫';
    $('#goodsbrand').val(goodsbrand);
    $('#channel').val(channel);
    $('.name-box').text(goodsbrand+channel);
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getGoodsbrandSaleBychannel,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand,
                channel:channel
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getStoreTopByBrand,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand,
                channel:channel
            },
            dataType:'json'
        }),
        $.ajax({  //图3
            url:AJAXURL.getGoodsTopByBrand3,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand,
                channel:channel
            },
            dataType:'json'
        })
    ).done(function(response1,response2,response3){
            var time='近三个月';
//  图1
            var data1=response1[0].data;
            var chartOneL=[],chartOneX=[],chartOneD1=[],chartOneD2=[],danwei1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneD1.push(data1[i1].total);
                chartOneD2.push({
                    value:data1[i1].speed,
                    name:data1[i1].speed+'%'
                });
                chartOneX.push(data1[i1].day);
            }
            danwei1='万元';
            chartOneL=['销售额','增速'];
            chartOne(chartOneL,chartOneX,chartOneD1,chartOneD2,danwei1,time);
//  图2
            var data2=response2[0].data;
            var chartTwoX=[],chartTwoD=[],danwei2;
            for(var i2=0;i2<data2.length;i2++){
                chartTwoX.push(data2[i2].store_name);
                chartTwoD.push({
                    value:data2[i2].total,
                    name:data2[i2].store_id
                });
            }
            danwei2='万元';
            chartTwo(chartTwoX,chartTwoD,danwei2,time);
//  图3
            var data3=response3[0].data;
            var chartThreeX=[],chartThreeD=[],danwei3;
            for(var i3=0;i3<data3.length;i3++){
                chartThreeX.push(data3[i3].goodsname);
                chartThreeD.push({
                    value:data3[i3].total,
                    goodsid:data3[i3].goodsid,
                    channel:data3[i3].channel,
                    category:data3[i3].category,
                    goodsbrand:data3[i3].goodsbrand
                });
            }
            danwei3='万元';
            chartThree(chartThreeX,chartThreeD,danwei3,time);
//  图表大小
            window.onresize=function(){
                chart1.resize();
                chart2.resize();
                chart3.resize();
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
function chartOne(chartOneL,chartOneX,chartOneD1,chartOneD2,danwei,time){
    chart1 = echarts.init(document.getElementById('chart1'));
    chart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#29b6f6','#6ae6e7'],
        title : {
            text: '品牌销售情况',
            x:'center',
            textStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei'
            }
        },
        tooltip : {
            trigger: 'axis',
            axisPointer :{
                type: 'shadow'
            },
            formatter:function(params){
                if(params.length==1){
                    if(params[0].seriesName=='增速'){
                        return params[0].seriesName+'：'+params[0].value+'%';
                    }else{
                        return params[0].seriesName+'：'+params[0].value+danwei;
                    }
                }else{
                    return params[0].seriesName+'：'+params[0].value+danwei+'<br/>'+params[1].seriesName+'：'+params[1].value+'%';
                }
            }
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
        dataZoom:{
            show:true,
            start:0,
            end:100
        },
        xAxis : [
            {
                splitLine : {
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
                name : '单位：'+danwei,
                type : 'value'
            },
            {
                splitLine : {
                    show:false
                },
                name : '单位：百分比',
                type : 'value'
            }
        ],
        series : [
            {
                name:chartOneL[0],
                type:'bar',
                barMaxWidth:30,
                smooth:true,
                data:chartOneD1
            },
            {
                name:chartOneL[1],
                type:'line',
                smooth:true,
                yAxisIndex: 1,
                data:chartOneD2
            }
        ]
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
}
//图2
function chartTwo(chartTwoX,chartTwoD,danwei,time){
    chart2 = echarts.init(document.getElementById('chart2'));
    chart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        title : {
            text: '品牌热销店铺TOP10',
            x:'center',
            textStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei'
            }
        },
        tooltip : {
            trigger: 'item',
            axisPointer :{
                type: 'shadow'
            },
            formatter:'{b}'+'<br/>'+'{c}'+danwei
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
        dataZoom:{
            show:true,
            start:0,
            end:100
        },
        xAxis : [
            {
                splitLine : {
                    show : false
                },
                type : 'category',
                data : chartTwoX
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
                name : '单位：'+danwei,
                type : 'value'
            }
        ],
        series : [
            {
                smooth:true,
                type:'bar',
                barMaxWidth:30,
                itemStyle:{
                    normal:{
                        color:function(params){
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
                data:chartTwoD
            }
        ]
    };
//  点击事件
//    var ecConfig = echarts.config;
//    chart2.on(ecConfig.EVENT.CLICK, function(params) {
//        var shopName=encodeURI(params.name);
//        var storeId=params.data.name;
//        var _iframe=$(window.parent.document.getElementById('iframepage'));
//        _iframe.attr('src','shop-detail.html?shopName='+shopName+'&storeId='+storeId+'&dateType='+1);
//        //window.location.href='shop-detail.html?shopName='+shopName+'&storeId='+storeId+'&dateType='+1;
//    });
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
}
//图3
function chartThree(chartThreeX,chartThreeD,danwei,time){
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
            text: '品牌热销商品TOP10',
            x:'center',
            textStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei'
            }
        },
        tooltip : {
            trigger: 'item',
            axisPointer :{
                type: 'shadow'
            },
            formatter:'{b}'+'<br/>'+'{c}'+danwei
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
        dataZoom:{
            show:true,
            start:0,
            end:100
        },
        xAxis : [
            {
                splitLine : {
                    show : false
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
                name : '单位：'+danwei,
                type : 'value'
            }
        ],
        series : [
            {
                smooth:true,
                type:'bar',
                itemStyle:{
                    normal:{
                        color:function(params){
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
                barMaxWidth:30,
                data:chartThreeD
            }
        ]
    };
//  点击事件
//    var ecConfig = echarts.config;
//    chart3.on(ecConfig.EVENT.CLICK, function(params) {
//        var goodName=encodeURI(params.name);
//        var channel=encodeURI(params.data.channel);
//        var goodType=encodeURI(params.data.category);
//        var goodBrand=encodeURI(params.data.goodsbrand);
//        var goodsId=params.data.goodsid;
//        var _iframe=$(window.parent.document.getElementById('iframepage'));
//        _iframe.attr('src','good-analy-detail.html?goodName='+goodName+'&channel='+channel+'&goodType='+goodType+'&goodBrand='+goodBrand+'&goodsId='+goodsId+'&dateType='+1);
//        //window.location.href='good-analy-detail.html?goodName='+goodName+'&channel='+channel+'&goodType='+goodType+'&goodBrand='+goodBrand+'&goodsId='+goodsId+'&dateType='+1;
//    });
    setTimeout(function (){
        chart3.hideLoading();
        chart3.setOption(option);
    },1000);
}
//图1点击事件
$('#btn1').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var saleType=$(this).prev().find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            goodsbrand:goodsbrand,
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getGoodsbrandSaleBychannel,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data1=response.data;
            var chartOneL=[],chartOneX=[],chartOneD1=[],chartOneD2=[],danwei1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneD1.push(data1[i1].total);
                chartOneD2.push({
                    value:data1[i1].speed,
                    name:data1[i1].speed+'%'
                });
                chartOneX.push(data1[i1].day);
            }
            if(parseInt(saleType)==0){
                danwei1='万元';
                chartOneL=['销售额','增速'];
            }else{
                danwei1='万件';
                chartOneL=['销售量','增速'];
            }
            var time;
            time=startTime+'至'+endTime;
            chartOne(chartOneL,chartOneX,chartOneD1,chartOneD2,danwei1,time);
        }
    })
});
//图2点击事件
$('#btn2').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var saleType=$(this).prev().find('.pn-active').index();
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            goodsbrand:goodsbrand,
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图2
        url:AJAXURL.getStoreTopByBrand,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data2=response.data;
            var chartTwoX=[],chartTwoD=[],danwei2;
            for(var i2=0;i2<data2.length;i2++){
                chartTwoX.push(data2[i2].store_name);
                chartTwoD.push({
                    value:data2[i2].total,
                    name:data2[i2].store_id
                });
            }
            if(parseInt(saleType)==0){
                danwei2='万元';
            }else{
                danwei2='万件';
            }
            var time;
            time=startTime+'至'+endTime;
            chartTwo(chartTwoX,chartTwoD,danwei2,time);
        }
    })
});
//图3点击事件
$('#btn3').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var saleType=$(this).prev().find('.pn-active').index();
    var startTime=$('#myCalender5').val();
    var endTime=$('#myCalender6').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            goodsbrand:goodsbrand,
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图3
        url:AJAXURL.getGoodsTopByBrand3,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data3=response.data;
            var chartThreeX=[],chartThreeD=[],danwei3;
            for(var i3=0;i3<data3.length;i3++){
                chartThreeX.push(data3[i3].goodsname);
                chartThreeD.push({
                    value:data3[i3].total,
                    goodsid:data3[i3].goodsid,
                    channel:data3[i3].channel,
                    category:data3[i3].category,
                    goodsbrand:data3[i3].goodsbrand
                });
            }
            if(parseInt(saleType)==0){
                danwei3='万元';
            }else{
                danwei3='万件';
            }
            var time;
            time=startTime+'至'+endTime;
            chartThree(chartThreeX,chartThreeD,danwei3,time);
        }
    })
});