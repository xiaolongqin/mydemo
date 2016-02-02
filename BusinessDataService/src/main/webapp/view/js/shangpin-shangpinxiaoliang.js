var chart1,chart2;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('商品画像');
    $.when(
        $.ajax({  //图1
            url:AJAXURL.getDegreeSale,
            type:'post',
            data:{
                dateType:'1'
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getXiangxinSale,
            type:'post',
            data:{
                dateType:'1'
            },
            dataType:'json'
        })
    ).done(function(response1,response2){
            var time='近三个月';
//          图1
            var data2=response1[0].data,chartOneL=[],chartOneD=[],oneData,danwei1,formatter1;
            for(var i2=0;i2<data2.length;i2++){
                chartOneL.push(data2[i2].name);
                oneData=(parseFloat(data2[i2].total_num)/10000).toString();
                chartOneD.push(
                    {
                        value:oneData.substring(0,oneData.indexOf('.')+3),
                        name:data2[i2].A
                    }
                );
            }
            danwei1='万元';
            formatter1='销售额';
            chartOne(chartOneL,chartOneD,danwei1,formatter1,time);
//          图2  chartTwoD=[]为所有数据综合，即放入series里面的  totalTwoD为每一个类的数据集合  如酱香型数据集合
            var data3=response2[0].data,chartTwoL=[],chartTwoX=[],chartTwoD=[],totalTwoD=[],twoData,attr2,danwei2,formatter2;
            for(attr2 in data3){
                chartTwoL.push(attr2);
            }
            for(var i3=0;i3<data3[chartTwoL[0]].length;i3++){
                chartTwoX.push(data3[chartTwoL[0]][i3].day);
            }
            for(var i4=0;i4<chartTwoL.length;i4++){
                totalTwoD=[];  //数组重置
                for(var i5=0;i5<data3[chartTwoL[i4]].length;i5++){
                    twoData=(parseFloat(data3[chartTwoL[i4]][i5].total)/10000).toString();
                    totalTwoD.push(
                        {
                            value:twoData.substring(0,twoData.indexOf('.')+3),
                            name:data3[chartTwoL[i4]][i5].A
                        }
                    );  //每个类的数据总和
                }
                chartTwoD.push({
                    name:chartTwoL[i4],
                    type:'bar',
                    barMarWidth:30,
                    data:totalTwoD
                });
            }
            danwei2='万元';
            formatter2='销售额';
            chartTwo(chartTwoL,chartTwoX,chartTwoD,danwei2,formatter2,time);
//      图表大小变化
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
function chartOne(chartOneL,chartOneD,danwei1,formatter1,time){
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
        title : {
            //text: '商品'+formatter1+'情况',
            x:'center',
            //textStyle:{
            //    fontSize:13,
            //    fontFamily:'Microsoft Yahei'
            //},
            subtext:'温馨提示：点击柱状图可查看详情',
            subtextStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei',
                color:'red'
            }
        },
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type:'shadow'
            },
            formatter:'{b}'+'<br/>'+'销售额：'+'{c}'+danwei1
        },
        legend: {
            data:chartOneL,
            show:false
        },
        toolbox: {
            show : true,
            feature : {
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true}
            },
            orient : 'vertical',
            y : 'center'
        },
        dataZoom :{
            show:true,
            start:0,
            end:100
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                splitLine : {
                    show : false
                },
                data :chartOneL
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
                data:chartOneD
            }
        ]
    };
    setTimeout(function (){
        chart1.hideLoading();
        chart1.setOption(option);
    },1000);
//  点击事件
    var ecConfig = echarts.config;
    chart1.on(ecConfig.EVENT.CLICK,function(params){
        var goodAttr=params.data.name;  //后台所需价格段 如A1
        var typeName=encodeURI(params.name);  //商品类型端 如30-40%浓度
        var _iframe=$(window.parent.document.getElementById('iframepage'));
        _iframe.attr('src','shangpin-shangpinxiaoliang-child.html?goodAttr='+goodAttr+'&typeName='+typeName);
    });
}
//图2
function chartTwo(chartTwoL,chartTwoX,chartTwoD,danwei2,formatter2,time){
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
        title : {
            //text: '商品各型号'+formatter2,
            x:'center',
            y:-5,
            //textStyle:{
            //    fontSize:13,
            //    fontFamily:'Microsoft Yahei'
            //},
            subtext:'温馨提示：点击柱状图可查看详情',
            subtextStyle:{
                fontSize:13,
                fontFamily:'Microsoft Yahei',
                color:'red'
            }
        },
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type:'shadow'
            },
            formatter:function(params){
                return params.name+'<br/>'+params.seriesName+'<br/>'+formatter2+'：'+params.data.value+danwei2
            }
        },
        legend: {
            data:chartTwoL,
            y:42
        },
        toolbox: {
            show : true,
            feature : {
                restore : {show: true}
            },
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
                data :chartTwoX
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
                name : '单位：'+danwei2,
                type : 'value'
            }
        ],
        dataZoom:{
            show:true,
            start:0,
            end:100
        },
        series : chartTwoD
    };
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
//  点击事件
    var ecConfig = echarts.config;
    chart2.on(ecConfig.EVENT.CLICK,function(params){
        var goodAttr=params.data.name;  //后台所需价格段 如A1
        var typeName=encodeURI(params.seriesName);  //商品类型端 如清香型
        var _iframe=$(window.parent.document.getElementById('iframepage'));
        _iframe.attr('src','shangpin-shangpinxiaoliang-child.html?goodAttr='+goodAttr+'&typeName='+typeName);
    });
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
            endTime:endTime
        }
    }
    $.ajax({
        url:AJAXURL.getDegreeSale,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
//          图1
            var data2=response.data,chartOneL=[],chartOneD=[],oneData,danwei1,formatter1;
            for(var i2=0;i2<data2.length;i2++){
                chartOneL.push(data2[i2].name);
                oneData=(parseFloat(data2[i2].total_num)/10000).toString();
                chartOneD.push(
                    {
                        value:oneData.substring(0,oneData.indexOf('.')+3),
                        name:data2[i2].A
                    }
                );
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
            chartOne(chartOneL,chartOneD,danwei1,formatter1,time);
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
            endTime:endTime
        }
    }
    $.ajax({
        url:AJAXURL.getXiangxinSale,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data3=response.data,chartTwoL=[],chartTwoX=[],chartTwoD=[],totalTwoD=[],twoData,attr2,danwei2,formatter2;
            for(attr2 in data3){
                chartTwoL.push(attr2);
            }
            for(var i3=0;i3<data3[chartTwoL[0]].length;i3++){
                chartTwoX.push(data3[chartTwoL[0]][i3].day);
            }
            for(var i4=0;i4<chartTwoL.length;i4++){
                totalTwoD=[];  //数组重置
                for(var i5=0;i5<data3[chartTwoL[i4]].length;i5++){
                    twoData=(parseFloat(data3[chartTwoL[i4]][i5].total)/10000).toString();
                    totalTwoD.push(
                        {
                            value:twoData.substring(0,twoData.indexOf('.')+3),
                            name:data3[chartTwoL[i4]][i5].A
                        }
                    );  //每个类的数据总和
                }
                chartTwoD.push({
                    name:chartTwoL[i4],
                    type:'bar',
                    barMarWidth:30,
                    data:totalTwoD
                });
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
            chartTwo(chartTwoL,chartTwoX,chartTwoD,danwei2,formatter2,time);
        }
    });
});