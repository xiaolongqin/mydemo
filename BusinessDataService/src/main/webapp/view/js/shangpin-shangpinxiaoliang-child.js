var chart1,chart2,chart4;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('商品参数细分');
    var href=window.location.search.split('&');
    var typeName,goodAttr;
    goodAttr=href[0].split('=')[1];
    typeName=decodeURI(href[1].split('=')[1]);

//    typeName='30-40%Vol';
//    goodAttr='B1';

    $('#goodAttr').val(goodAttr);
    $('.title-box').text(typeName);
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getDetailGoodsInfo,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                good_attr:goodAttr
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getGoodsBrandInfo,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                good_attr:goodAttr
            },
            dataType:'json'
        }),
        $.ajax({  //表格
            url:AJAXURL.getGoodsNameInfo,
            type:'post',
            data:{
                dateType:1,
                good_attr:goodAttr
            },
            dataType:'json'
        }),
        $.ajax({  //图4
            url:AJAXURL.getGoodsFocus,
            type:'post',
            data:{
                dateType:1,
                good_attr:goodAttr
            },
            dataType:'json'
        })
    ).done(function(response1,response2,response3,response4){
            var time='近三个月';
//  图1
            var data1=response1[0].data;
            var chartOneX=[],chartOneD=[],oneData,danwei1,formatter1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneX.push(data1[i1].day);
                oneData=(parseFloat(data1[i1].total)/10000).toString();
                chartOneD.push(oneData.substring(0,oneData.indexOf('.')+3));
            }
            danwei1='万元';
            formatter1='销售额';
            chartOne(chartOneX,chartOneD,danwei1,time,formatter1);
//  图2
            var data2=response2[0].data;
            var chartTwoX=[],chartTwoD=[],danwei2,formatter2;
            for(var i2=0;i2<data2.length;i2++){
                if(i2<10){
                    chartTwoX.push(data2[i2].goodsbrand);
                    chartTwoD.push(data2[i2].total);
                }
            }
            danwei2='万元';
            formatter2='销售额';
            chartTwo(chartTwoX,chartTwoD,danwei2,time,formatter2);
//  表格
            var data3=response3[0].data;
            chartThree(data3);
//  图4
            var data4=response4[0].data[0];
            var chartFourD;
            chartFourD=[{
                value:data4.A1,
                name:'好评数'
            },{
                value:data4.A2,
                name:'中评数'
            },{
                value:data4.A3,
                name:'差评数'
            },{
                value:data4.A4,
                name:'品质评论数'
            },{
                value:data4.A5,
                name:'外观评论数'
            },{
                value:data4.A6,
                name:'性价比评论数'
            },{
                value:data4.A7,
                name:'口感评论数'
            }];
            chartFour(chartFourD,time);
//      图表大小变化
            window.onresize = function(){
                chart1.resize();
                chart2.resize();
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
function chartOne(chartOneX,chartOneD,danwei1,time,formatter1){
    chart1 = echarts.init(document.getElementById('chart1'));
    chart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#28b6f6'],
        //title : {
        //    text: '商品'+formatter1+'情况',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'item',
            axisPointer :{
                type: 'shadow'
            },
            formatter:'{b}<br/>{c}'+danwei1
        },
        legend: {
            data:[''],
            show:false
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
                data : chartOneX,
                splitLine:{
                    show:false
                }
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
                name : formatter1+'：'+danwei1
            }
        ],
        series : [
            {
                type:'bar',
                smooth:true,
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
//图2
function chartTwo(chartTwoX,chartTwoD,danwei2,time,formatter2){
    chart2 = echarts.init(document.getElementById('chart2'));
    chart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        //title : {
        //    text: '品牌'+formatter2+'情况',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'item',
            axisPointer :{
                type: 'shadow'
            },
            formatter:'{b}<br/>{c}'+danwei2
        },
        legend: {
            data:[''],
            show:false
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
                data : chartTwoX,
                splitLine:{
                    show:false
                }
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
                name : formatter2+'：'+danwei2
            }
        ],
        series : [
            {
                type:'bar',
                smooth:true,
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
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
}
//表格
function chartThree(data){
    var str='';
    for(var i=0;i<data.length;i++){
        str+='<tr>' +
            '<td>No.'+(i+1)+'</td>' +
            '<td><span title="'+data[i].goodsbrand+'">'+data[i].goodsbrand+'</span></td>' +
            '<td>'+data[i].goods_mod_sum1+'</td>' +
            '<td>'+data[i].goods_mod_sum2+'</td>' +
            '<td>'+data[i].goods_mod_sum3+'</td>' +
            '</tr>';
    }
    $('#goodTable').find('tbody').html(str);
}
//图4
function chartFour(chartFourD,time){
    chart4 = echarts.init(document.getElementById('chart4'));
    chart4.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color:AJAXURL.color,
        //title : {
        //    text: '商品消费者关注点',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'item',
            formatter: "{b} : {c}条 ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['好评数','中评数','差评数','品质评论数','外观评论数','性价比评论数','口感评论数']  //A1-A7依次
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
        series : [
            {
                type:'pie',
                radius : '50%',
                center: ['50%', '50%'],
                data:chartFourD
            }
        ]
    };
    setTimeout(function (){
        chart4.hideLoading();
        chart4.setOption(option);
    },1000);
}
//图1点击事件
$('#btn1').on('click',function(){
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
            good_attr:$('#goodAttr').val()
        }
    }
    $.ajax({
        url:AJAXURL.getDetailGoodsInfo,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data1=response.data;
            var chartOneX=[],chartOneD=[],oneData,danwei1,formatter1;
            for(var i1=0;i1<data1.length;i1++){
                chartOneX.push(data1[i1].day);
                oneData=(parseFloat(data1[i1].total)/10000).toString();
                chartOneD.push(oneData.substring(0,oneData.indexOf('.')+3));
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
            chartOne(chartOneX,chartOneD,danwei1,time,formatter1);
        }
    });
});
//图2点击事件
$('#btn2').on('click',function(){
    var json;
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            good_attr:$('#goodAttr').val()
        }
    }
    $.ajax({  //图2
        url:AJAXURL.getGoodsBrandInfo,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data2=response.data;
            var chartTwoX=[],chartTwoD=[],danwei2,formatter2;
            for(var i2=0;i2<data2.length;i2++){
                if(i2<10){
                    chartTwoX.push(data2[i2].goodsbrand);
                    chartTwoD.push(data2[i2].total);
                }
            }
            if(parseInt(saleType)==0){
                danwei2='万元';
                formatter2='销售额';
            }else{
                danwei2='万件';
                formatter2='销售量';
            }
            var time;
            time=startTime+'至'+endTime;
            chartTwo(chartTwoX,chartTwoD,danwei2,time,formatter2);
        }
    });
});
//表格点击事件
$('#btn3').on('click',function(){
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
            good_attr:$('#goodAttr').val()
        }
    }
    $.ajax({  //表格
        url:AJAXURL.getGoodsNameInfo,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data3=response.data;
            chartThree(data3);
        }
    });
});
//图4点击事件
$('#btn4').on('click',function(){
    var json;
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
            good_attr:$('#goodAttr').val()
        }
    }
    $.ajax({  //图4
        url:AJAXURL.getGoodsFocus,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data4=response.data[0];
            var chartFourD;
            chartFourD=[{
                value:data4.A1,
                name:'好评数'
            },{
                value:data4.A2,
                name:'中评数'
            },{
                value:data4.A3,
                name:'差评数'
            },{
                value:data4.A4,
                name:'品质评论数'
            },{
                value:data4.A5,
                name:'外观评论数'
            },{
                value:data4.A6,
                name:'性价比评论数'
            },{
                value:data4.A7,
                name:'口感评论数'
            }];
            var time;
            time=startTime+'至'+endTime;
            chartFour(chartFourD,time);
        }
    })
});