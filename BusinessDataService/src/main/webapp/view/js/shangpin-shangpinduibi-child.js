var chart1,chart2,chart3;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('商品对比详情');
    var href=window.location.search.split('&');
    var goodsNamelist=decodeURI(href[0].split('=')[1]).split(',');
    var goodsIdList=decodeURI(href[1].split('=')[1]).split(',');
//    var goodsNamelist=['杜康绵柔三星 50度浓香型 500ml 优质白酒 中国名酒 整箱保真婚庆','特价礼盒白酒 茅台镇原产酱香白酒 石荣霄18年陈酿礼品4瓶套装'];
//    var goodsIdList=['taobao_523184690517','taobao_41058873468'];
    $('#goodsNameList').val(goodsNamelist);
    $('#goodsIdList').val(goodsIdList);
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getGoodsSaleduibi,
            type:'post',
            data:{
                goodsNameList:goodsNamelist,
                goodsIdList:goodsIdList,
                dateType:1,
                saleType:1
            },
            dataType:'json'
        }),
        $.ajax({  //图2,图3
            url:AJAXURL.getGoodsAttrduibi,
            type:'post',
            data:{
                goodsNameList:goodsNamelist,
                goodsIdList:goodsIdList,
                dateType:1,
                saleType:1
            },
            dataType:'json'
        })
    ).done(function(response1,response2){
            var time='近三个月';
//  图1
            var data1=response1[0].data;
            var chartOneL=[],chartOneL2=[],chartOneX=[],chartOneD=[],attr1,totalOneD,danwei1,formatter1;
            for(attr1 in data1){
                chartOneL.push(attr1);
                chartOneL2.push(attr1.split('(')[0]);
            }
            for(var i11=0;i11<data1[chartOneL[0]].length;i11++){
                chartOneX.push(data1[chartOneL[0]][i11].day);
            }
            for(var i12=0;i12<chartOneL.length;i12++){
                totalOneD=[];  //数组重置
                for(var i13=0;i13<data1[chartOneL[i12]].length;i13++){
                    totalOneD.push(
                        data1[chartOneL[i12]][i13].total
                    );  //每个类的数据总和
                }
                chartOneD.push({
                    name:chartOneL2[i12],
                    type:'bar',
                    barMaxWidth:30,
                    smooth:true,
                    data:totalOneD
                });
            }
            danwei1='万元';
            formatter1='销售额';
            chartOne(chartOneL2,chartOneX,chartOneD,danwei1,time,formatter1);
//  图2
//  图1：A1:好评率  A2:好评数  A3:中评率  A4:中评数  A5:差评率  A6:差评数  A7:品质关注度  A8:品质评论数  图2：A9:外观关注度  A10:性价比关注度  A11:外观评论数  A12:性价比评论数  A13:口感关注度  A14:口感评论数
            var data2=response2[0].data;
            var chartTwoY=[],chartTwoY2=[],attr2,twoDA1=[],twoDA2=[],twoDA3=[],twoDA4=[],twoDA5=[],twoDA6=[],twoDA7=[],twoDA8=[],twoDA9=[],twoDA10=[],twoDA11=[],twoDA12=[],twoDA13=[],twoDA14=[];
            for(attr2 in data2){
                chartTwoY.push(attr2);
                chartTwoY2.push(attr2.split('(')[0]);
            }
            for(var i21=0;i21<chartTwoY.length;i21++){
                for(var i22=0;i22<data2[chartTwoY[i21]].length;i22++){
//  图1
                    twoDA9.push({
                        value:data2[chartTwoY[i21]][i22].A9,
                        name:data2[chartTwoY[i21]][i22].A9
                    });
                    twoDA10.push({
                        value:data2[chartTwoY[i21]][i22].A10*(-1),
                        name:data2[chartTwoY[i21]][i22].A10
                    });
                    twoDA11.push({
                        value:parseInt(data2[chartTwoY[i21]][i22].A11/(1000)),
                        name:data2[chartTwoY[i21]][i22].A11+'条'
                    });
                    twoDA12.push({
                        value:parseInt(data2[chartTwoY[i21]][i22].A12/(-1000)),
                        name:data2[chartTwoY[i21]][i22].A12+'条'
                    });
                    twoDA13.push({
                        value:data2[chartTwoY[i21]][i22].A13,
                        name:data2[chartTwoY[i21]][i22].A13
                    });
                    twoDA14.push({
                        value:parseInt(data2[chartTwoY[i21]][i22].A14/(-1000)),
                        name:data2[chartTwoY[i21]][i22].A14+'条'
                    });
//  图2
                    twoDA1.push({
                        value:data2[chartTwoY[i21]][i22].A1,
                        name:data2[chartTwoY[i21]][i22].A1
                    });
                    twoDA2.push({
                        value:data2[chartTwoY[i21]][i22].A2/1000,
                        name:data2[chartTwoY[i21]][i22].A2+'条'
                    });
                    twoDA3.push({
                        value:data2[chartTwoY[i21]][i22].A3,
                        name:data2[chartTwoY[i21]][i22].A3
                    });
                    twoDA4.push({
                        value:data2[chartTwoY[i21]][i22].A4/1000,
                        name:data2[chartTwoY[i21]][i22].A4+'条'
                    });
                    twoDA5.push({
                        value:data2[chartTwoY[i21]][i22].A5,
                        name:data2[chartTwoY[i21]][i22].A5
                    });
                    twoDA6.push({
                        value:data2[chartTwoY[i21]][i22].A6/1000,
                        name:data2[chartTwoY[i21]][i22].A6+'条'
                    });
                    twoDA7.push({
                        value:data2[chartTwoY[i21]][i22].A7,
                        name:data2[chartTwoY[i21]][i22].A7
                    });
                    twoDA8.push({
                        value:data2[chartTwoY[i21]][i22].A8/1000,
                        name:data2[chartTwoY[i21]][i22].A8+'条'
                    });
                }
            }
            chartTwo(chartTwoY2,twoDA9,twoDA10,twoDA11,twoDA12,twoDA13,twoDA14,time);
            chartThree(chartTwoY2,twoDA1,twoDA2,twoDA3,twoDA4,twoDA5,twoDA6,twoDA7,twoDA8,time);
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
function chartOne(chartOneL,chartOneX,chartOneD,danwei,time,formatter1){
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
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type:'shadow'
            },
            formatter:'{b}'+'<br/>'+'{a}'+'<br/>'+'{c}'+danwei
        },
//        title : {
//            text: '商品'+formatter1+'对比',
//            x:'center',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            }
//        },
        legend: {
            data:chartOneL,
            y:30
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
                splitLine:{
                    show:false
                },
                data : chartOneX
            }
        ],
        yAxis : [
            {
                name : '单位：'+danwei,
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
function chartTwo(chartTwoY2,twoDA9,twoDA10,twoDA11,twoDA12,twoDA13,twoDA14,time){
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
//        title : {
//            text: '商品参数对比',
//            x:'center',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            }
//        },
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            formatter:function(params){
                var str='';
                for(var i=0;i<params.length;i++){
                    str+=params[i].seriesName+'：'+params[i].data.name+'<br/>';
                }
                return str;
            }
        },
        legend: {
            data:['外观关注度', '性价比关注度', '外观评论数', '性价比评论数','口感关注度','口感评论数'],
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
        xAxis : [
            {
                type : 'value',
                show : false,
                splitLine:{
                    show:false
                }
            }
        ],
        grid:{
            x:220
        },
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
                data : chartTwoY2
            }
        ],
        series : [
            {
                name:'外观关注度',
                type:'bar',
                barMaxWidth : 10,
                data:twoDA9
            },
            {
                name:'性价比关注度',
                type:'bar',
                barMaxWidth : 10,
                data:twoDA10
            },
            {
                name:'外观评论数',
                type:'bar',
                barMaxWidth : 10,
                data:twoDA11
            },
            {
                name:'性价比评论数',
                type:'bar',
                barMaxWidth : 10,
                data:twoDA12
            },
            {
                name:'口感关注度',
                type:'bar',
                barMaxWidth : 10,
                data:twoDA13
            },
            {
                name:'口感评论数',
                type:'bar',
                barMaxWidth : 10,
                data:twoDA14
            }
        ]
    };
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
}
//图3
function chartThree(chartTwoY2,twoDA1,twoDA2,twoDA3,twoDA4,twoDA5,twoDA6,twoDA7,twoDA8,time){
    chart3 = echarts.init(document.getElementById('chart3'));
    chart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : AJAXURL.color,
//        title : {
//            text: '商品评论对比',
//            x:'center',
//            textStyle:{
//                fontSize:13,
//                fontFamily:'Microsoft Yahei'
//            }
//        },
        tooltip : {
            trigger: 'axis',
            axisPointer : {
                type: 'shadow'
            },
            formatter:function(params){
                var str='';
                for(var i=0;i<params.length;i++){
                    str+=params[i].seriesName+'：'+params[i].data.name+'<br/>';
                }
                return str;
            }
        },
        legend: {
            //A1:好评率  A2:好评数  A3:中评率  A4:中评数  A5:差评率  A6:差评数  A7:品质关注度  A8:品质评论数
            data:['好评率','好评数','中评率','中评数','差评率','差评数','品质关注度','品质评论数'],
            y:30
        },
        toolbox: {
            show : true,
            orient : 'vertical',
            y : 'center',
            feature : {
                restore : {show: true}
            }
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
                data : chartTwoY2
            }
        ],
        yAxis : [
            {
                type : 'value',
                splitLine : {
                    show:false
                },
                splitArea : {show : true}
            }
        ],
        grid: {
            x2:40
        },
        series : [
            //A1:好评率  A2:好评数  A3:中评率  A4:中评数  A5:差评率  A6:差评数  A7:品质关注度  A8:品质评论数
            {
                name:'好评率',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:twoDA1
            },
            {
                name:'好评数',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:twoDA2
            },
            {
                name:'中评率',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:twoDA3
            },
            {
                name:'中评数',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:twoDA4
            },
            {
                name:'差评率',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:twoDA5
            },
            {
                name:'差评数',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:twoDA6
            },
            {
                name:'品质关注度',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:twoDA7
            },
            {
                name:'品质评论数',
                type:'bar',
                smooth:true,
                barMaxWidth:30,
                data:twoDA8
            }
        ]
    };
    setTimeout(function (){
        chart3.hideLoading();
        chart3.setOption(option);
    },1000);
}
//图1点击事件
$('#btn1').on('click',function(){
    var json;
    var goodsNameList=$('#goodsNameList').val();
    var goodsIdList=$('#goodsIdList').val();
//    var list=$(this).parent().next().find('.channel-active');
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            goodsNameList:goodsNameList.split(','),
            goodsIdList:goodsIdList.split(','),
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            saleType:parseInt(saleType)+1
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getGoodsSaleduibi,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data1=response.data;
            var chartOneL=[],chartOneL2=[],chartOneX=[],chartOneD=[],attr1,totalOneD,danwei1,formatter1;
            for(attr1 in data1){
                chartOneL.push(attr1);
                chartOneL2.push(attr1.split('(')[0]);
            }
            for(var i11=0;i11<data1[chartOneL[0]].length;i11++){
                chartOneX.push(data1[chartOneL[0]][i11].day);
            }
            for(var i12=0;i12<chartOneL.length;i12++){
                totalOneD=[];  //数组重置
                for(var i13=0;i13<data1[chartOneL[i12]].length;i13++){
                    totalOneD.push(
                        data1[chartOneL[i12]][i13].total
                    );  //每个类的数据总和
                }
                chartOneD.push({
                    name:chartOneL2[i12],
                    type:'bar',
                    barMaxWidth:30,
                    smooth:true,
                    data:totalOneD
                });
            }
            if(parseInt(saleType)==0){
                danwei1='万元';
                formatter1='销售额';
            }else{
                danwei1='万件';
                formatter1='销售量';
            }
            var time;
            time=startTime+'至'+endTime;
            chartOne(chartOneL2,chartOneX,chartOneD,danwei1,time,formatter1);
        }
    })
});
//图2点击事件
$('#btn2').on('click',function(){
    var json;
    var goodsNameList=$('#goodsNameList').val();
    var goodsIdList=$('#goodsIdList').val();
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            goodsNameList:goodsNameList.split(','),
            goodsIdList:goodsIdList.split(','),
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图2,图3
        url:AJAXURL.getGoodsAttrduibi,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data2=response.data;
            var chartTwoY=[],chartTwoY2=[],attr2,twoDA9=[],twoDA10=[],twoDA11=[],twoDA12=[],twoDA13=[],twoDA14=[];
            for(attr2 in data2){
                chartTwoY.push(attr2);
                chartTwoY2.push(attr2.split('(')[0]);
            }
            for(var i21=0;i21<chartTwoY.length;i21++){
                for(var i22=0;i22<data2[chartTwoY[i21]].length;i22++){
//  图1
                    twoDA9.push({
                        value:data2[chartTwoY[i21]][i22].A9,
                        name:data2[chartTwoY[i21]][i22].A9
                    });
                    twoDA10.push({
                        value:data2[chartTwoY[i21]][i22].A10*(-1),
                        name:data2[chartTwoY[i21]][i22].A10
                    });
                    twoDA11.push({
                        value:parseInt(data2[chartTwoY[i21]][i22].A11/(1000)),
                        name:data2[chartTwoY[i21]][i22].A11+'条'
                    });
                    twoDA12.push({
                        value:parseInt(data2[chartTwoY[i21]][i22].A12/(-1000)),
                        name:data2[chartTwoY[i21]][i22].A12+'条'
                    });
                    twoDA13.push({
                        value:data2[chartTwoY[i21]][i22].A13,
                        name:data2[chartTwoY[i21]][i22].A13
                    });
                    twoDA14.push({
                        value:parseInt(data2[chartTwoY[i21]][i22].A14/(-1000)),
                        name:data2[chartTwoY[i21]][i22].A14+'条'
                    });
                }
            }
            var time;
            time=startTime+'至'+endTime;
            chartTwo(chartTwoY2,twoDA9,twoDA10,twoDA11,twoDA12,twoDA13,twoDA14,time);
        }
    });
});
//图2点击事件
$('#btn3').on('click',function(){
    var json;
    var goodsNameList=$('#goodsNameList').val();
    var goodsIdList=$('#goodsIdList').val();
    var startTime=$('#myCalender5').val();
    var endTime=$('#myCalender6').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            goodsNameList:goodsNameList.split(','),
            goodsIdList:goodsIdList.split(','),
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图2,图3
        url:AJAXURL.getGoodsAttrduibi,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data2=response.data;
            var chartTwoY=[],chartTwoY2=[],attr2,twoDA1=[],twoDA2=[],twoDA3=[],twoDA4=[],twoDA5=[],twoDA6=[],twoDA7=[],twoDA8=[];
            for(attr2 in data2){
                chartTwoY.push(attr2);
                chartTwoY2.push(attr2.split('(')[0]);
            }
            for(var i21=0;i21<chartTwoY.length;i21++){
                for(var i22=0;i22<data2[chartTwoY[i21]].length;i22++){
//  图2
                    twoDA1.push({
                        value:data2[chartTwoY[i21]][i22].A1,
                        name:data2[chartTwoY[i21]][i22].A1
                    });
                    twoDA2.push({
                        value:data2[chartTwoY[i21]][i22].A2/1000,
                        name:data2[chartTwoY[i21]][i22].A2+'条'
                    });
                    twoDA3.push({
                        value:data2[chartTwoY[i21]][i22].A3,
                        name:data2[chartTwoY[i21]][i22].A3
                    });
                    twoDA4.push({
                        value:data2[chartTwoY[i21]][i22].A4/1000,
                        name:data2[chartTwoY[i21]][i22].A4+'条'
                    });
                    twoDA5.push({
                        value:data2[chartTwoY[i21]][i22].A5,
                        name:data2[chartTwoY[i21]][i22].A5
                    });
                    twoDA6.push({
                        value:data2[chartTwoY[i21]][i22].A6/1000,
                        name:data2[chartTwoY[i21]][i22].A6+'条'
                    });
                    twoDA7.push({
                        value:data2[chartTwoY[i21]][i22].A7,
                        name:data2[chartTwoY[i21]][i22].A7
                    });
                    twoDA8.push({
                        value:data2[chartTwoY[i21]][i22].A8/1000,
                        name:data2[chartTwoY[i21]][i22].A8+'条'
                    });
                }
            }
            var time;
            time=startTime+'至'+endTime;
            chartThree(chartTwoY2,twoDA1,twoDA2,twoDA3,twoDA4,twoDA5,twoDA6,twoDA7,twoDA8,time);
        }
    });
});