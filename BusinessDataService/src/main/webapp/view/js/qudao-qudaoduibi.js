var chart1,chart2;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('渠道对比');

//  鼠标移入移出
    var timer;
    $('.channel-btn')[0].onmouseover = show;
    $('.channel-btn')[0].onmouseout = hide;

    $('.channel-box')[0].onmouseover = show;
    $('.channel-box')[0].onmouseout = hide;
    function show(){
        clearInterval( timer );
        $('.channel-box')[0].style.display = 'block';
    }
    function hide(){
        timer = setTimeout(function(){
            $('.channel-box')[0].style.display = 'none';
        }, 200);
    }

    $.when(
        $.ajax({  //获取所有渠道
            url:AJAXURL.getAllChannelName,
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({  //图1
            url:AJAXURL.getChannelduibi,
            type:'post',
            data:{
                dateType:1,
                saleType:1
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getChannelGoodDuibi,
            type:'post',
            data:{
                dateType:1
            },
            dataType:'json'
        })
    ).done(function(response1,response2,response3){
//  获取所有渠道
            var data0=response1[0].data,time='近三个月';
            var strTd='',strTr='',strAll='',strLi='';
            for(var i=0;i<data0.length;i++){
                if(i==0 || i==1){
                    strTd+='<td><div class="channel-name-box channel-active" onclick="chooseChannel(this);">'+data0[i].channel+'</div></td>';
                    strLi+='<li><span>'+data0[i].channel+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
                }else{
                    strTd+='<td><div class="channel-name-box" onclick="chooseChannel(this);">'+data0[i].channel+'</div></td>';
                }
                if((i+1)%3==0){  //每三个一行
                    strTr='<tr>'+strTd+'</tr>';
                    strAll+=strTr;
                    strTr='';
                    strTd='';
                }
            }
            strTr='<tr>'+strTd+'</tr>';  //每三个一行最后剩下的再为一行
            strAll+=strTr;
            $('.channel-row>ul').append(strLi);
            $('.channel-box').find('tbody').html(strAll);
//  图1
            var data1=response2[0].data;
            var chartOneL=[],chartOneX,chartOneD=[],attrOne;
            var i2,dataOne,formatter1,formatter2,danWei,totalOne;
            formatter1='销售额';
            formatter2='万元';
            danWei='单位：万元';
            for(attrOne in data1){
                chartOneL.push(data1[attrOne][0].channel);
                chartOneX=[];  //重置数组
                dataOne=[];
                for(i2=0;i2<data1[attrOne].length;i2++){
                    chartOneX.push(data1[attrOne][i2].day);
                    totalOne=(parseFloat(data1[attrOne][i2].total_sale)/10000).toString();
                    dataOne.push(totalOne.substring(0,totalOne.indexOf('.')+3));
                }
                chartOneD.push({
                    name:data1[attrOne][0].channel,
                    type:'line',
                    smooth:true,
                    //itemStyle: {normal: {areaStyle: {type: 'default'}}},
                    data:dataOne
                });
            }
            chartOne(chartOneL,chartOneX,chartOneD,formatter1,formatter2,danWei,time);
//  图2
            var data2=response3[0].data;
            var chartTwoL=[],chartTwoD=[],attrTwo,twoData,formatter3='销售额',danwei3='万元';
            for(attrTwo in data2){
                chartTwoL.push(attrTwo);
                twoData=(parseFloat(data2[attrTwo][0].total_sale)/10000).toString();
                chartTwoD.push(twoData.substring(0,twoData.indexOf('.')+3));
            }
            chartTwo(chartTwoL,chartTwoD,formatter3,danwei3,time);
//          图表大小变化
            window.onresize = function(){
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
function chartOne(chartOneL,chartOneX,chartOneD,formatter1,formatter2,danWei,time){
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
        //    text: formatter1+'趋势对比',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'axis',
            axisPointer : {
                type: 'shadow'
            },
            formatter:function(params){
                var str=params[0].name+'：<br/>';
                for(var i=0;i<params.length;i++){
                    str+=params[i].seriesName+'：'+params[i].value+formatter2+'<br/>';
                }
                return str;
            }
        },
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
            x:'right',
            orient : 'vertical',
            y : 'center'
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                splitLine : {
                    show:false
                },
                boundaryGap : false,
                data : chartOneX
            }
        ],
        yAxis : [
            {
                type : 'value',
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                },
                name : danWei
            }
        ],
        dataZoom:{
            show : true,
            start : 0,
            end : 100
        },
        series : chartOneD
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
}
//图2
function chartTwo(chartTwoL,chartTwoD,formatter3,danwei3,time){
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
        //    text: formatter3+'细分对比',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type: 'shadow'
            },
            formatter:function(params){
                return params.name+'<br/>'+formatter3+'：<br/>'+params.value+danwei3;
            }
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            },
            x:'right',
            orient : 'vertical',
            y : 'center'
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
                data : chartTwoL
            }
        ],
        yAxis : [
            {
                type : 'value',
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                },
                name:'单位：'+danwei3
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
//图1点击事件
$('#btn1').on('click',function(){
    var json;
    var channelList=[];
    var list=$(this).next().find('.channel-active');
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        if(list.length<2){
            alert('请至少选择两个渠道进行对比');
            return false;
        }
        for(var i=0;i<list.length;i++){
            channelList.push(list.eq(i).text());
        }
        json={
            saleType:parseInt(saleType)+1,
            channelList:channelList,
            dateType:0,
            startTime:startTime,
            endTime:endTime
        };
        $.ajax({
            url:AJAXURL.getChannelduibi,
            type:'post',
            data:json,
            dataType:'json',
            success:function(response){
                var data1=response.data;
                var chartOneL=[],chartOneX,chartOneD=[],attrOne;
                var i2,dataOne,formatter1,formatter2,danWei,totalOne,time;
                if(parseInt(saleType)==0){
                    formatter1='销售额';
                    formatter2='万元';
                    danWei='单位：万元';
                }else{
                    formatter1='销售量';
                    formatter2='万件';
                    danWei='单位：万件';
                }
                for(attrOne in data1){
                    chartOneL.push(data1[attrOne][0].channel);
                    chartOneX=[];  //重置数组
                    dataOne=[];
                    for(i2=0;i2<data1[attrOne].length;i2++){
                        chartOneX.push(data1[attrOne][i2].day);
                        totalOne=(parseFloat(data1[attrOne][i2].total_sale)/10000).toString();
                        dataOne.push(totalOne.substring(0,totalOne.indexOf('.')+3));
                    }
                    chartOneD.push({
                        name:data1[attrOne][0].channel,
                        type:'line',
                        smooth:true,
                        //itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data:dataOne
                    });
                }
                time=startTime+'至'+endTime;
                chartOne(chartOneL,chartOneX,chartOneD,formatter1,formatter2,danWei,time);
            }
        });
    }
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
            endTime:endTime
        }
    }
    $.ajax({
        url:AJAXURL.getChannelGoodDuibi,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data2=response.data;
            var chartTwoL=[],chartTwoD=[],attrTwo,twoData,formatter3,danwei3,time;
            if(saleType==0){
                formatter3='销售额';
                danwei3='万元';
            }else{
                formatter3='销售量';
                danwei3='万件';
            }
            for(attrTwo in data2){
                chartTwoL.push(attrTwo);
                twoData=(parseFloat(data2[attrTwo][0].total_sale)/10000).toString();
                chartTwoD.push(twoData.substring(0,twoData.indexOf('.')+3));
            }
            time=startTime+'至'+endTime;
            chartTwo(chartTwoL,chartTwoD,formatter3,danwei3,time);
        }
    });
});