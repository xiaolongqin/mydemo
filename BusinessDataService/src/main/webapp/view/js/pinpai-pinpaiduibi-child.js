var chart1;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('品牌对比详情');
    var href=window.location.search;
    var brandNameList=decodeURI(href.split('=')[1]).split(',');
//    var brandNameList=["洋河", "五粮液"];
    $('#brandLists').val(brandNameList);
    var time='近三个月';
    $.ajax({  //图1图2
        url:AJAXURL.getGoodsbrandAttrduibi,
        type:'post',
        data:{
            dateType:1,
            goodsbrandList:brandNameList
        },
        dataType:'json',
        success:function(response){
            var data=response.data;
            var chartOneX=[],d1=[],d2=[],d3=[],d4=[],d5=[],d6=[],d7=[],d8=[],d9=[],d10=[],d11=[],data8,data9,data10,data11;
            for(var i1=0;i1<data.length;i1++){
                chartOneX.push(data[i1].goodsbrand);
                d1.push({
                    value:data[i1].A1,
                    name:data[i1].A1
                });
                d2.push({
                    value:data[i1].A2,
                    name:data[i1].A2
                });
                d3.push({
                    value:data[i1].A3,
                    name:data[i1].A3
                });

                d4.push({
                    value:data[i1].A4,
                    name:data[i1].A4
                });
                d5.push({
                    value:data[i1].A5,
                    name:data[i1].A5
                });
                d6.push({
                    value:data[i1].A6,
                    name:data[i1].A6
                });
                d7.push({
                    value:data[i1].A7,
                    name:data[i1].A7
                });

                data8=data[i1].A8;
                d8.push({
                    value:data8.toFixed(2),
                    name:data[i1].A8
                });
                data9=data[i1].A9;
                d9.push({
                    value:data9.toFixed(2),
                    name:data[i1].A9
                });
                data10=data[i1].A10;
                d10.push({
                    value:data10.toFixed(2),
                    name:data[i1].A10
                });
                data11=data[i1].A11;
                d11.push({
                    value:data10.toFixed(2),
                    name:data[i1].A11
                });
            }
            chartOne(chartOneX,d1,d2,d3,d4,d5,d6,d7,time);
            chartTwo(chartOneX,d8,d9,d10,d11,time);
//  图表大小变化
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
        }
    });
});
//图1
function chartOne(chartOneX,d1,d2,d3,d4,d5,d6,d7,time){
    chart1 = echarts.init(document.getElementById('chart1'));
    chart1.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color:AJAXURL.color,
        title : {
            //text: '品牌对比',
            //x:'center',
            //textStyle:{
            //    fontSize:13,
            //    fontFamily:'Microsoft Yahei'
            //}
        },
        tooltip : {
            trigger: 'axis',
            axisPointer : {
                type:'shadow'
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
            y : 30,
            data:['好评率','中评率','差评率','品质关注度','口感关注度','外观关注度','性价比关注度']
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
            start:0,
            end:100,
            show:true
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
                axisLabel:{
                    show:false
                },
                type : 'value'
            }
        ],
        series : [
            {
                name:'好评率',
                type:'bar',
                barMaxWidth:30,
                data:d1
            },
            {
                name:'中评率',
                type:'bar',
                barMaxWidth:30,
                data:d2
            },
            {
                name:'差评率',
                type:'bar',
                barMaxWidth:30,
                data:d3
            },
            {
                name:'品质关注度',
                type:'bar',
                barMaxWidth:30,
                data:d4
            },{
                name:'口感关注度',
                type:'bar',
                barMaxWidth:30,
                data:d5
            },
            {
                name:'外观关注度',
                type:'bar',
                barMaxWidth:30,
                data:d6
            },
            {
                name:'性价比关注度',
                type:'bar',
                barMaxWidth:30,
                data:d7
            }
        ]
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
}
//图2
function chartTwo(chartOneX,d8,d9,d10,d11,time){
    chart2 = echarts.init(document.getElementById('chart2'));
    chart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color:AJAXURL.color,
        title : {
            //text: '品牌对比',
            //x:'center',
            //textStyle:{
            //    fontSize:13,
            //    fontFamily:'Microsoft Yahei'
            //}
        },
        tooltip : {
            trigger: 'axis',
            axisPointer : {
                type:'shadow'
            },
            formatter:function(params){
                var str='';
                for(var i=0;i<params.length;i++){
                    str+=params[i].seriesName+'：'+params[i].data.name+'条<br/>';
                }
                return str;
            }
        },
        legend: {
            y : 30,
            data:['性价比评论数','品质评论数','口感评论数']
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
            start:0,
            end:100,
            show:true
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
                axisLabel:{
                    show:false
                },
                type : 'value'
            }
        ],
        series : [
            {
                name:'外观评论数',
                type:'bar',
                barMaxWidth:30,
                data:d8
            },
            {
                name:'性价比评论数',
                type:'bar',
                barMaxWidth:30,
                data:d9
            },
            {
                name:'品质评论数',
                type:'bar',
                barMaxWidth:30,
                data:d10
            },
            {
                name:'口感评论数',
                type:'bar',
                barMaxWidth:30,
                data:d11
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
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            goodsbrandList:$('#brandLists').val().split(','),
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getGoodsbrandAttrduibi,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data=response.data;
            var chartOneX=[],d1=[],d2=[],d3=[],d4=[],d5=[],d6=[],d7=[],d8=[],d9=[],d10=[],d11=[],data8,data9,data10,data11;
            for(var i1=0;i1<data.length;i1++){
                chartOneX.push(data[i1].goodsbrand);
                d1.push({
                    value:data[i1].A1,
                    name:data[i1].A1
                });
                d2.push({
                    value:data[i1].A2,
                    name:data[i1].A2
                });
                d3.push({
                    value:data[i1].A3,
                    name:data[i1].A3
                });

                d4.push({
                    value:data[i1].A4,
                    name:data[i1].A4
                });
                d5.push({
                    value:data[i1].A5,
                    name:data[i1].A5
                });
                d6.push({
                    value:data[i1].A6,
                    name:data[i1].A6
                });
                d7.push({
                    value:data[i1].A7,
                    name:data[i1].A7
                });

                data8=data[i1].A8;
                d8.push({
                    value:data8.toFixed(2),
                    name:data[i1].A8
                });
                data9=data[i1].A9;
                d9.push({
                    value:data9.toFixed(2),
                    name:data[i1].A9
                });
                data10=data[i1].A10;
                d10.push({
                    value:data10.toFixed(2),
                    name:data[i1].A10
                });
                data11=data[i1].A11;
                d11.push({
                    value:data10.toFixed(2),
                    name:data[i1].A11
                });
            }
            var time;
            time=startTime+'至'+endTime;
            chartOne(chartOneX,d1,d2,d3,d4,d5,d6,d7,time);
            chartTwo(chartOneX,d8,d9,d10,d11,time);
        }
    });
});