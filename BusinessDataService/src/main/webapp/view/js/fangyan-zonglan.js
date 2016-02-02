var chart1;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('线上电商总览');
    $.ajax({
        url: AJAXURL.getOverView,
        type: 'post',
        data: {
            saleType:1,
            dateType:1,
            channelList:AJAXURL.channel1
        },
        dataType: 'json',
        success:function(response){
            var data=response.data;
            var chartOneL,chartOneD,total,danwei='元';
            total=data[4].total;
            $('#numberBox').text((total*1).toLocaleString().split('.')[0]+danwei);  //我尼玛，IE后面会自动多两个小数点
            chartOneL=[data[0].name,data[1].name,data[2].name,data[3].name];
            chartOneD=[(100*data[0].total/total).toFixed(2),(100*data[1].total/total).toFixed(2),(100*data[2].total/total).toFixed(2),(100*data[3].total/total).toFixed(2)];
            $('#name1').text(data[0].name);
            $('#name2').text(data[1].name);
            $('#name3').text(data[2].name);
            $('#name4').text(data[3].name).attr('title',data[3].name);
            for(var i=0;i<4;i++){
                $('.word-right ul li').eq(i).text((data[i].total).toLocaleString().split('.')[0]+danwei);
            }
            chartOne(chartOneL,chartOneD);
            $('.now-in').css({'display':'none'});
            $('.content-box').css({'visibility':'visible'});
//          渠道
            channelLists();
//  图表大小
            window.onresize=function(){
                chart1.resize();
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
    $('.now-in').css({'display':'block'});
    $('.content-box').css({'visibility':'hidden'});
    var saleType=$('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    $.ajax({
        url: AJAXURL.getOverView,
        type: 'post',
        data: {
            saleType:parseInt(saleType)+1,
            dateType:0,
            channelList:channelList,
            startTime:startTime,
            endTime:endTime
        },
        dataType: 'json',
        success:function(response) {
            var data=response.data;
            var chartOneL,chartOneD,total,danwei;
            if(parseInt(saleType)==0){
                danwei='元';
                $('#titleName').text('交易金额');
            }else if(parseInt(saleType)==1){
                danwei='件';
                $('#titleName').text('交易总量');
            }else{
                danwei='条';
                $('#titleName').text('评论总数');
            }
            total=data[4].total;
            $('#numberBox').text((total*1).toLocaleString().split('.')[0]+danwei);
            chartOneL=[data[0].name,data[1].name,data[2].name,data[3].name];
            chartOneD=[(100*data[0].total/total).toFixed(2),(100*data[1].total/total).toFixed(2),(100*data[2].total/total).toFixed(2),(100*data[3].total/total).toFixed(2)].reverse();
            $('#name1').text(data[0].name);
            $('#name2').text(data[1].name);
            $('#name3').text(data[2].name);
            $('#name4').text(data[3].name).attr('title',data[3].name);
            for(var i=0;i<4;i++){
                $('.word-right ul li').eq(i).text((data[i].total).toLocaleString().split('.')[0]+danwei);
            }
            chartOne(chartOneL,chartOneD);
            $('.now-in').css({'display':'none'});
            $('.content-box').css({'visibility':'visible'});
        }
    });
});
//图1
function chartOne(chartOneL,chartOneD){
    chart1 = echarts.init(document.getElementById('chart1'));
    var option = {
        calculable : false,
        grid:{
            x:40,
            //borderColor:'#fff',
            borderWidth:0
        },
        xAxis : [
            {
                type : 'value',
                axisLine:{show:false,lineStyle:{ color : '#fff'}},
                splitLine: {show: false},
                axisLabel: {
                    show: false
                },
                axisTick: {show: false}
            }
        ],
        yAxis : [
            {
                type : 'category',
                axisLine:{show:false,lineStyle:{ color : '#fff'}},
                splitLine: {show: false},
                axisTick: {show: false},
                axisLabel: {
                    textStyle:{
                        fontSize:12,
                        fontWeight:600,
                        fontFamily:'Microsoft Yahei',
                        color:'#4a5f6f'
                    }
                },
                data : ['商品','品牌','店铺','渠道']
            }
        ],
        series : [
            {
                type:'bar',
                barMaxWidth:15,
                barMinHeight:40,
                itemStyle: {normal: {
                    label : {show: true, position: 'inside',formatter: '{c}%'},
                    color:function(params){
                        var color=['#29b6f6','#9bce4a','#1885e8','#4cce71'];
                        return color[params.dataIndex%10];
                    }
                }},
                data:chartOneD
            }
        ]
    };
    chart1.setOption(option);
}