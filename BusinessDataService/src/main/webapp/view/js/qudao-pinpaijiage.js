$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('品牌价格详情');
    var href,goodsbrand,price_attr,price;
    href=window.location.search.split('&');
    goodsbrand=decodeURI(href[0].split('=')[1]);
    price_attr=href[1].split('=')[1];
    price=decodeURI(href[2].split('=')[1]);
//    goodsbrand='茅台';
//    price_attr='A1';
//    price='0-100元';
    $('.name-box').text(goodsbrand+price);
    $('#goodsbrand').val(goodsbrand);
    $('#priceAttr').val(price_attr);
    $('#price').val(price);
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getGoodsbrandSaleByPrice,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand,
                price_attr:price_attr
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getBrandSaleByChannel,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand,
                price_attr:price_attr
            },
            dataType:'json'
        }),
        $.ajax({  //图3
            url:AJAXURL.getGoodsSaleByBrand,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                goodsbrand:goodsbrand,
                price_attr:price_attr
            },
            dataType:'json'
        }),
        $.ajax({  //图4
            url:AJAXURL.getbrandFaceByPrice,
            type:'post',
            data:{
                dateType:1,
                goodsbrand:goodsbrand,
                price_attr:price_attr
            },
            dataType:'json'
        })
    ).done(function(response1,response2,response3,response4){
            var time='近三个月';
//  图1
            var data1=response1[0].data;
            var chartOneL,chartOneX=[],chartOneD1=[],chartOneD2=[],danwei1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneX.push(data1[i1].day);
                chartOneD1.push(data1[i1].total);
                chartOneD2.push({
                    value:data1[i1].speed,
                    name:data1[i1].speed+'%'
                });
            }
            danwei1='万元';
            chartOneL=['销售额','增速'];
            chartOne(chartOneL,chartOneX,chartOneD1,chartOneD2,danwei1,time);
//  图2
            var data2=response2[0].data;
            var chartTwoX=[],chartTwoD=[],danwei2;
            for(var i2=0;i2<data2.length;i2++){
                chartTwoX.push(data2[i2].channel);
                chartTwoD.push(data2[i2].total);
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
                    goodsbrand:data3[i3].goodsbrand,
                    goodsid:data3[i3].goodsid,
                    channel:data3[i3].channel,
                    category:data3[i3].category
                });
            }
            danwei3='万元';
            chartThree(chartThreeX,chartThreeD,danwei3,time);
//  图4
            var data4=response4[0].data;
            var list4=[];
            for(var i7=0;i7<data4.length;i7++){
                list4.push(
                    [data4[i7].word,data4[i7].weight]
                );
            }
            chartFour(list4);

//  图表大小变化
            window.onresize = function(){
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
//        title : {
//            text: '价格销售变化情况',
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
                axisLine:{
                    onZero:false
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
                name :'单位：'+danwei,
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
//        title : {
//            text: '各渠道详情',
//            x:'center',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            }
//        },
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
//        title : {
//            text: '热销产品详情',
//            x:'center',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            }
//        },
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
                barMaxWidth:30,
                itemStyle:{
                    normal:{
                        color:function(params){
                            return AJAXURL.color[params.dataIndex%10];
                        }
                    }
                },
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
//图4
function chartFour(list){
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
    WordCloud(document.getElementById('chart4'),{
        list:newList
    });
}
//图1点击事件
$('#btn1').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var priceAttr=$('#priceAttr').val();
    var saleType=$(this).prev().find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            saleType:parseInt(saleType)+1,
            goodsbrand:goodsbrand,
            price_attr:priceAttr
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getGoodsbrandSaleByPrice,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data1=response.data;
            var chartOneL,chartOneX=[],chartOneD1=[],chartOneD2=[],danwei1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneX.push(data1[i1].day);
                chartOneD1.push(data1[i1].total);
                chartOneD2.push({
                    value:data1[i1].speed,
                    name:data1[i1].speed+'%'
                });
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
    });
});
//图2点击事件
$('#btn2').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var priceAttr=$('#priceAttr').val();
    var saleType=$(this).prev().find('.pn-active').index();
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
            saleType:parseInt(saleType)+1,
            goodsbrand:goodsbrand,
            price_attr:priceAttr
        };
    }
    $.ajax({  //图2
        url:AJAXURL.getBrandSaleByChannel,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data2=response.data;
            var chartTwoX=[],chartTwoD=[],danwei2;
            for(var i2=0;i2<data2.length;i2++){
                chartTwoX.push(data2[i2].channel);
                chartTwoD.push(data2[i2].total);
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
    });
});
//图3点击事件
$('#btn3').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var priceAttr=$('#priceAttr').val();
    var saleType=$(this).prev().find('.pn-active').index();
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
            saleType:parseInt(saleType)+1,
            goodsbrand:goodsbrand,
            price_attr:priceAttr
        };
    }
    $.ajax({  //图3
        url:AJAXURL.getGoodsSaleByBrand,
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
                    goodsbrand:data3[i3].goodsbrand,
                    goodsid:data3[i3].goodsid,
                    channel:data3[i3].channel,
                    category:data3[i3].category
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
    });
});
//图4点击事件
$('#btn4').on('click',function(){
    var json;
    var goodsbrand=$('#goodsbrand').val();
    var priceAttr=$('#priceAttr').val();
    var startTime=$('#myCalender7').val();
    var endTime=$('#myCalender8').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            goodsbrand:goodsbrand,
            price_attr:priceAttr
        };
    }
    $.ajax({  //图4
        url:AJAXURL.getbrandFaceByPrice,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data4=response.data;
            var list4=[];
            for(var i7=0;i7<data4.length;i7++){
                list4.push(
                    [data4[i7].word,data4[i7].weight]
                );
            }
            chartFour(list4);
        }
    })
});