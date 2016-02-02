var chart1,chart2;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('店铺详情');
    var href=window.location.search.split('&');
    var shopName=decodeURI(href[0].split('=')[1]);
    var storeId=href[1].split('=')[1];
//    var shopName='晋隆九鼎酒类专营店';
//    var storeId='jdshop_123229';
    var time='近三个月';
    $('.shop-name').text(shopName);
    $('#shopName').val(shopName);
    $('#shopId').val(storeId);
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getStoreDetaiInfo,
            type:'post',
            data:{
                saleType:1,
                storeId:storeId,
                dateType:1
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getStoreComment,
            type:'post',
            data:{
                storeId:storeId,
                dateType:1
            },
            dataType:'json'
        })
    ).done(function(response1,response2){
//  图1
            var data1=response1[0].data;
            var chartOneL,chartOneX=[],chartOneD1=[],chartOneD2=[],danwei1,formatter1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneX.push(data1[i1].day);
                chartOneD1.push(data1[i1].total);
                chartOneD2.push(data1[i1].avg_price);
            }
            danwei1='万元';
            chartOneL=['销售额','平均贡献值'];
            chartOne(chartOneL,chartOneX,chartOneD1,chartOneD2,danwei1,time);
//  图2
            var data3=response2[0].data;
            var chartThreeX=['产品描述符合度','店铺服务态度','店铺发货速度'],chartThreeD1=[];
            chartThreeD1.push(data3[0].store_score,data3[0].store_industry_score,data3[0].store_delivery_score);
            chartThree(chartThreeX,chartThreeD1,time);
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
        //title: {
        //    text : '店铺销售情况',
        //    x : 'center',
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
                var str=params[0].name+'<br/>';
                for(var i=0;i<params.length;i++){
                    if(params[i].seriesName=='销售量' || params[i].seriesName=='销售额'){
                        str+=params[i].seriesName+'：'+params[i].value+danwei+'<br/>';
                    }else{
                        str+=params[i].seriesName+'：'+params[i].value+'<br/>';
                    }
                }
                return str;
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
        //legend: {
        //    data:chartOneL,
        //    y:30
        //},
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
                splitLine:{
                    show:false
                },
                splitArea:{
                    show:true
                },
                name : chartOneL[0]+'：'+danwei,
                type : 'value'
            },
            {
                splitLine:{
                    show:false
                },
                type : 'value',
                name : '平均贡献值'
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
function chartThree(chartThreeX,chartThreeD1,time){
    chart2 = echarts.init(document.getElementById('chart2'));
    chart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color:['#29b6f6'],
        //title: {
        //    text : '店铺评价情况',
        //    x : 'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type:'shadow'
            },
            formatter:'{b}'+'<br/>'+'{c}分'
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
                type : 'category',
                splitLine:{
                    show:false
                },
                data : chartThreeX
            }
        ],
        yAxis : [
            {
                splitLine:{
                    show:false
                },
                splitArea:{
                    show:true
                },
                type : 'value'
            }
        ],
        series : [
            {
                smooth:true,
                type:'bar',
                barMaxWidth:30,
                data:chartThreeD1
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
    var saleType=$(this).prev().find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            storeId:$('#shopId').val(),
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getStoreDetaiInfo,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data1=response.data;
            var chartOneL,chartOneX=[],chartOneD1=[],chartOneD2=[],danwei1,time;
            for(var i1=0;i1<data1.length;i1++){
                chartOneX.push(data1[i1].day);
                chartOneD1.push(data1[i1].total);
                chartOneD2.push(data1[i1].avg_price);
            }
            if(parseInt(saleType)==0){
                danwei1='万元';
                chartOneL=['销售额','平均贡献值'];
            }else{
                danwei1='万件';
                chartOneL=['销售量','平均贡献值'];
            }
            time=startTime+'至'+endTime;
            chartOne(chartOneL,chartOneX,chartOneD1,chartOneD2,danwei1,time);
        }
    })
});
//图2点击事件
$('#btn2').on('click',function(){
    var json;
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            storeId:$('#shopId').val(),
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({
        url:AJAXURL.getStoreComment,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data3=response.data,time;
            time=startTime+'至'+endTime;
            var chartThreeX=['产品描述符合度','店铺服务态度','店铺发货速度'],chartThreeD1=[];
            chartThreeD1.push(data3[0].store_score,data3[0].store_industry_score,data3[0].store_delivery_score);
            chartThree(chartThreeX,chartThreeD1,time);
        }
    });
});