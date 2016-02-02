var chart1,chart2,chart3,chart4,chart5,chart6,chart7;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('品牌详情');
    var href=window.location.search;
    var goodsbrand=decodeURI(href.split('=')[1]);
//    var goodsbrand='泸州老窖';
    $('.brand-name').text(goodsbrand);
    $('#goodsbrand').val(goodsbrand);
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getStoreDetaiInfo2,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getGoodsbrandSaleBysqudao,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand
            }
        }),
        $.ajax({  //图3
            url:AJAXURL.getGoodsSaleByGoodsbrand,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand
            }
        }),
        //图4被砍掉了
        $.ajax({  //图5
            url:AJAXURL.getGoodsbrandPriceInfo,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand
            }
        }),
        $.ajax({  //图6
            url:AJAXURL.getGoodsbrandFaceChange,
            type:'post',
            data:{
                dateType:1,
                goodsbrand:goodsbrand
            }
        }),
        $.ajax({  //图7图8
            url:AJAXURL.getGoodsbrandFace,
            type:'post',
            data:{
                dateType:1,
                goodsbrand:goodsbrand
            }
        })
    ).done(function(response1,response2,response3,response5,response6,response7){
            var time='近三个月';
//  图1
            var data1=response1[0].data;
            var chartOneL=[],chartOneX1=[],chartOneX2=[],danwei1,formatter1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneL.push(data1[i1].day);
                chartOneX1.push(data1[i1].total);
                chartOneX2.push(data1[i1].speed);
            }
            danwei1='万元';
            formatter1='销售额';
            chartOne(chartOneL,chartOneX1,chartOneX2,danwei1,formatter1,goodsbrand,time);
//  图2
            var data2=response2[0].data;
            var chartTwoX=[],chartTwoD=[],danwei2,formatter2;
            for(var i2=0;i2<data2.length;i2++){
                chartTwoX.push(data2[i2].channel);
                chartTwoD.push(data2[i2].total);
            }
            danwei2='万元';
            formatter2='销售额';
            chartTwo(chartTwoX,chartTwoD,danwei2,formatter2,goodsbrand,time);
//  图3
            var data3=response3[0].data;
            var chartThreeX=[],chartThreeD=[],danwei3,formatter3;
            for(var i3=0;i3<data3.length;i3++){
                chartThreeX.push(data3[i3].goodsname);
                chartThreeD.push({
                    value:data3[i3].total,
                    goodName:data3[i3].goodsname,
                    channel:data3[i3].channel,
                    goodType:data3[i3].category,
                    goodBrand:data3[i3].goodsbrand,
                    goodsId:data3[i3].goodsid
                });
            }
            danwei3='万元';
            formatter3='销售额';
            chartThree(chartThreeX,chartThreeD,danwei3,formatter3,goodsbrand,time);
//  图5
            var data5=response5[0].data;
            var chartFiveX=[],chartFiveD=[],danwei5,formatter5;
            for(var i5=0;i5<data5.length;i5++){
                chartFiveX.push(data5[i5].name);
                chartFiveD.push({
                    value:data5[i5].total_num,
                    priceAttr:data5[i5].A,
                    price:data5[i5].name,
                    goodsbrand:data5[i5].goodsbrand
                });
            }
            danwei5='万元';
            formatter5='销售额';
            chartFive(chartFiveX,chartFiveD,danwei5,formatter5,goodsbrand,time);
//  图6
            var data6=response6[0].data;
            var chartSixOneD=[],chartSixTwoD=[],chartSixX=[];
            for(var i6=0;i6<data6.length;i6++){
                chartSixX.push(data6[i6].day);
                chartSixOneD.push(data6[i6].goods_mod_comment1);
                chartSixTwoD.push(data6[i6].goods_mod_sum1);
            }
            chartSix(chartSixX,chartSixOneD,chartSixTwoD,goodsbrand,time);
//  图7图8
            var data7=response7[0].data;
            var chartSevenD=[],list7=[];
            for(var i7=0;i7<data7.length;i7++){
                chartSevenD.push({
                    value:data7[i7].weight,
                    name:data7[i7].word
                });
                list7.push(
                    [data7[i7].word,data7[i7].weight]
                );
            }
            chartSeven(chartSevenD,goodsbrand,time);
            chartEight(list7);
//  图表大小变化
            window.onresize = function(){
                chart1.resize();
                chart2.resize();
                chart3.resize();
                chart4.resize();
                chart5.resize();
                chart6.resize();
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
//图8
function chartEight(list){
    //var list=[];  //list必须是二维数组[[],[]]形式  源码333行开始是控制颜色的代码  父元素的css必须加相对定位，因为字符云是绝对定位的
    //for(var i=0;i<20;i++){
    //    list.push(
    //        ['白酒',parseInt(Math.random()*40+20)]  //控制字符大小
    //    )
    //}
    var oldList=list;
    var total=0;
    var newList=[];
    for(var i=0;i<oldList.length;i++){
        total+=oldList[i][1];
    }
    for(var j=0;j<oldList.length;j++){
        newList.push([oldList[j][0],Math.floor(oldList[j][1]/total*800)]);
    }
    WordCloud(document.getElementById('chart7'),{
        list:newList
    });
}
//图1
function chartOne(chartOneL,chartOneX1,chartOneX2,danwei,formatter,goodsbrand,time){
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
//        title : {
//            text: goodsbrand+formatter+'情况',
//            x:'center',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            }
//        },
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
                    return params[0].name+'<br/>'+params[0].value+danwei+'<br/>增速：'+params[1].value+'%';
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
        dataZoom:{
            show:true,
            start:0,
            end:100
        },
        calculable : true,
        xAxis : [
            {
                splitLine:{
                    show:false
                },
                axisLine:{
                    onZero:false
                },
                type : 'category',
                data : chartOneL
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
                name : '增速：百分比',
                type : 'value'
            }
        ],
        series : [

            {
                name:chartOneL[0],
                type:'bar',
                barMaxWidth:30,
                data:chartOneX1
            },
            {
                name:chartOneL[1],
                type:'line',
                smooth:true,
                yAxisIndex: 1,
                data:chartOneX2
            }
        ]
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
}
//图2
function chartTwo(chartTwoX,chartTwoD,danwei,formatter,goodsbrand,time){
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
            x:'center',
//            text: goodsbrand+'各渠道'+formatter+'情况',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            },
            subtext:'温馨提示：点击柱状图可查看详情',
            subtextStyle:{
                color:'red',
                fontSize:'12px',
                fontWeight:'normal'
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
                type : 'category',
                splitLine:{
                    show:false
                },
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
                itemStyle: {
                    normal: {
                        color: function(params) {
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
                data:chartTwoD
            }
        ]
    };
//  点击事件
    var ecConfig = echarts.config;
    chart2.on(ecConfig.EVENT.CLICK, function(params) {
        var goodsbrand=encodeURI($('#goodsbrand').val());
        var channel=encodeURI(params.name);
        var _iframe=$(window.parent.document.getElementById('iframepage'));
        _iframe.attr('src','qudao-qudaopinpaixiangqing.html?goodsbrand='+goodsbrand+'&channel='+channel);
    });
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
}
//图3
function chartThree(chartThreeX,chartThreeD,danwei,formatter,goodsbrand,time){
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
            x:'center',
//            text: goodsbrand+formatter+'TOP5',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            },
            subtext:'温馨提示：点击柱状图可查看详情',
            subtextStyle:{
                color:'red',
                fontSize:'12px',
                fontWeight:'normal'
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
                type : 'category',
                splitLine:{
                    show:false
                },
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
        var goodName=encodeURI(params.data.goodName);
        var goodsId=params.data.goodsId;
        var channel=encodeURI(params.data.channel);
        var goodsbrand=encodeURI(params.data.goodBrand);
        var _iframe=$(window.parent.document.getElementById('iframepage'));
        _iframe.attr('src','shangpin-shangpinxiangqing.html?goodName='+goodName+'&goodsId='+goodsId+'&channel='+channel+'&goodsbrand='+goodsbrand);
    });
    setTimeout(function (){
        chart3.hideLoading();
        chart3.setOption(option);
    },1000);
}
//图5
function chartFive(chartFiveX,chartFiveD,danwei,formatter,goodsbrand,time){
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
            x:'center',
//            text: goodsbrand+formatter+'分布',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            },
            subtext:'温馨提示：点击柱状图可查看详情',
            subtextStyle:{
                color:'red',
                fontSize:'12px',
                fontWeight:'normal'
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
        legend: {
            data:[''],
            show:false
        },
        xAxis : [
            {
                type : 'category',
                splitLine:{
                    show:false
                },
                data : chartFiveX
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
                itemStyle: {
                    normal: {
                        color: function(params) {
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
                barMaxWidth:30,
                data:chartFiveD
            }
        ]
    };
//  点击事件
    var ecConfig = echarts.config;
    chart4.on(ecConfig.EVENT.CLICK, function(params) {
        var goodsbrand=encodeURI(params.data.goodsbrand);
        var priceAttr=params.data.priceAttr;
        var price=encodeURI(params.data.price);
        var _iframe=$(window.parent.document.getElementById('iframepage'));
        _iframe.attr('src','qudao-pinpaijiage.html?goodsbrand='+goodsbrand+'&priceAttr='+priceAttr+'&price='+price);
    });
    setTimeout(function (){
        chart4.hideLoading();
        chart4.setOption(option);
    },1000);
}
//图6
function chartSix(chartSixX,chartSixOneD,chartSixTwoD,goodsbrand,time){
    chart5 = echarts.init(document.getElementById('chart5'));
    chart5.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#29b6f6','#6ae6e7'],
//        title : {
//            text: goodsbrand+'口碑情况',
//            x:'center',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            }
//        },
        tooltip : {
            trigger: 'axis',
            axisPointer :{
                type: 'shadow'
            },
            formatter:function(params){
                return params[0].name+'<br/>'+params[0].seriesName+'：'+params[0].value+'条<br/>'+params[1].seriesName+'：'+params[1].value;
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
                type : 'category',
                splitLine:{
                    show:false
                },
                axisLine:{
                    onZero:false
                },
                data : chartSixX
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
                name : '单位：条',
                type : 'value'
            },
            {
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                },
                type : 'value'
            }
        ],
        series : [

            {
                name:'评论数',
                type:'bar',
                barMaxWidth:30,
                data:chartSixTwoD
            },
            {
                name:'好评率',
                smooth:true,
                type:'line',
                yAxisIndex: 1,
                data:chartSixOneD
            }
        ]
    };
    setTimeout(function (){
        chart5.hideLoading();
        chart5.setOption(option);
    },1000);
}
//图7
function chartSeven(chartSevenD,goodsbrand,time){
    chart6 = echarts.init(document.getElementById('chart6'));
    chart6.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : AJAXURL.color,
//        title : {
//            text: goodsbrand+'品牌印象',
//            x:'center',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            }
//        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c}"
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            },
            orient : 'vertical',
            y:'center'
        },
        calculable : true,
        series : [
            {
                name:'权重',
                type:'pie',
                radius : '45%',
                center: ['50%', '50%'],
                data:chartSevenD,
                itemStyle:{
                    normal:{
                        label:{
                            show:true,
                            formatter:'{b}：{c}'
                        }
                    }
                }
            }
        ]
    };
    setTimeout(function (){
        chart6.hideLoading();
        chart6.setOption(option);
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
    $.ajax({
        url:AJAXURL.getStoreDetaiInfo2,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data1=response.data;
            var chartOneL=[],chartOneX1=[],chartOneX2=[],danwei1,formatter1,time;
            for(var i1=0;i1<data1.length;i1++){
                chartOneL.push(data1[i1].day);
                chartOneX1.push(data1[i1].total);
                chartOneX2.push(data1[i1].speed);
            }
            if(parseInt(saleType)==0){
                danwei1='万元';
                formatter1='销售额';
            }else{
                danwei1='万件';
                formatter1='销售量';
            }
            time=startTime+'至'+endTime;
            chartOne(chartOneL,chartOneX1,chartOneX2,danwei1,formatter1,goodsbrand,time);
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
    $.ajax({
        url:AJAXURL.getGoodsbrandSaleBysqudao,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data2=response.data;
            var chartTwoX=[],chartTwoD=[],danwei2,formatter2,time;
            for(var i2=0;i2<data2.length;i2++){
                chartTwoX.push(data2[i2].channel);
                chartTwoD.push(data2[i2].total);
            }
            if(parseInt(saleType)==0){
                danwei2='万元';
                formatter2='销售额';
            }else{
                danwei2='万件';
                formatter2='销售量';
            }
            time=startTime+'至'+endTime;
            chartTwo(chartTwoX,chartTwoD,danwei2,formatter2,goodsbrand,time);
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
    $.ajax({
        url:AJAXURL.getGoodsSaleByGoodsbrand,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data3=response.data;
            var chartThreeX=[],chartThreeD=[],danwei3,formatter3;
            for(var i3=0;i3<data3.length;i3++){
                chartThreeX.push(data3[i3].goodsname);
                chartThreeD.push({
                    value:data3[i3].total,
                    goodName:data3[i3].goodsname,
                    channel:data3[i3].channel,
                    goodType:data3[i3].category,
                    goodBrand:data3[i3].goodsbrand,
                    goodsId:data3[i3].goodsid
                });
            }
            if(parseInt(saleType)==0){
                danwei3='万元';
                formatter3='销售额';
            }else{
                danwei3='万件';
                formatter3='销售量';
            }
            var time;
            time=startTime+'至'+endTime;
            chartThree(chartThreeX,chartThreeD,danwei3,formatter3,goodsbrand,time);
        }
    })
});
//图4点击事件
$('#btn4').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var saleType=$(this).prev().find('.pn-active').index();
    var startTime=$('#myCalender7').val();
    var endTime=$('#myCalender8').val();
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
    $.ajax({  //图5
        url:AJAXURL.getGoodsbrandPriceInfo,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data5=response.data;
            var chartFiveX=[],chartFiveD=[],danwei5,formatter5;
            for(var i5=0;i5<data5.length;i5++){
                chartFiveX.push(data5[i5].name);
                chartFiveD.push({
                    value:data5[i5].total_num,
                    priceAttr:data5[i5].A,
                    price:data5[i5].name,
                    goodsbrand:data5[i5].goodsbrand
                });
            }
            if(parseInt(saleType)==0){
                danwei5='万元';
                formatter5='销售额';
            }else{
                danwei5='万件';
                formatter5='销售量';
            }
            var time;
            time=startTime+'至'+endTime;
            chartFive(chartFiveX,chartFiveD,danwei5,formatter5,goodsbrand,time);
        }
    })
});
//图5点击事件
$('#btn5').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var saleType=$(this).prev().find('.pn-active').index();
    var startTime=$('#myCalender9').val();
    var endTime=$('#myCalender10').val();
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
    $.ajax({
        url:AJAXURL.getGoodsbrandFaceChange,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data6=response.data;
            var chartSixOneD=[],chartSixTwoD=[],chartSixX=[];
            for(var i6=0;i6<data6.length;i6++){
                chartSixX.push(data6[i6].day);
                chartSixOneD.push(data6[i6].goods_mod_comment1);
                chartSixTwoD.push(data6[i6].goods_mod_sum1);
            }
            var time;
            time=startTime+'至'+endTime;
            chartSix(chartSixX,chartSixOneD,chartSixTwoD,goodsbrand,time);
        }
    })
});
//图6图7点击事件
$('#btn6').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var startTime=$('#myCalender11').val();
    var endTime=$('#myCalender12').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            goodsbrand:goodsbrand,
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图7图8
        url:AJAXURL.getGoodsbrandFace,
        type:'post',
        data:json,
        success:function(response){
            var data7=response.data;
            var chartSevenD=[],list7=[];
            for(var i7=0;i7<data7.length;i7++){
                chartSevenD.push({
                    value:data7[i7].weight,
                    name:data7[i7].word
                });
                list7.push(
                    [data7[i7].word,data7[i7].weight]
                );
            }
            var time;
            time=startTime+'至'+endTime;
            chartSeven(chartSevenD,goodsbrand,time);
            chartEight(list7);
        }
    })
});