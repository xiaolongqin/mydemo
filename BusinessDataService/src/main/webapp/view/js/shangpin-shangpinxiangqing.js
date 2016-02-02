var tableJson1={
        dateType:1,
        startTime:'',
        endTime:'',
        pageNumber:1,
        goodsBrand:''
    },
    tableJson2={
        dateType:1,
        startTime:'',
        endTime:'',
        pageNumber:1,
        goodsBrand:''
    },
    countPageNumber1= 0,
    countPageNumber2= 0,
    chart1,
    chart2,
    chart3,
    chart31;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('商品详情');
    var href=window.location.search.split('&');
    var goodName,goodsId,channel,goodsBrand;
    goodName=decodeURI(href[0].split('=')[1]);
    goodsId=href[1].split('=')[1];
    channel=decodeURI(href[2].split('=')[1]);
    goodsBrand=decodeURI(href[3].split('=')[1]);
//    goodName='今世缘 地缘 42度 浓香型 500ml';
//    goodsId='jd_1139134';
//    channel='京东';
//    goodsBrand='今世缘';
    $('#goodName').val(goodName);
    $('#goodsId').val(goodsId);
    $('#channel').val(channel);
    $('#goodsBrand').val(goodsBrand);
    $('.good-name').text(goodName).attr('title',goodName);
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
            url:AJAXURL.getGoodsSaleInfo,
            type:'post',
            data:{
                dateType:1,
                goodName:goodName,
                goodsId:goodsId,
                channel:channel,
                saleType:1
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getChannelGoodDuibi2,
            type:'post',
            data:{
                dateType:1,
                goodName:goodName,
                goodsId:goodsId,
                channelList:[channel],
                saleType:1
            },
            dataType:'json'
        }),
        $.ajax({  //图3
            url:AJAXURL.getGoodsComment,
            type:'post',
            data:{
                dateType:1,
                goodsId:goodsId
            },
            dataType:'json'
        }),
        $.ajax({  //表格1
            url:AJAXURL.getGoodsTopByCategory,
            type:'post',
            data:{
                dateType:1,
                pageNumber:1,
                goodsBrand:goodsBrand
            },
            dataType:'json'
        }),
        $.ajax({  //表格2
            url:AJAXURL.getGoodsTopByBrand,
            type:'post',
            data:{
                dateType:1,
                pageNumber:1,
                goodsBrand:goodsBrand
            },
            dataType:'json'
        })
    ).done(function(response0,response1,response3,response4,response5,response6){
            var time='近三个月';
//  获取渠道
            var data0=response0[0].data;
            var strTd='',strTr='',strAll='',strLi='';
            for(var i=0;i<data0.length;i++){
                if(data0[i].channel == channel){
                    strTd+='<td><div class="channel-name-box channel-active" onclick="channelChange(this);">'+data0[i].channel+'</div></td>';
                    strLi+='<li><span>'+data0[i].channel+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
                }else{
                    strTd+='<td><div class="channel-name-box" onclick="channelChange(this);">'+data0[i].channel+'</div></td>';
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
            $('.channel-box').find('tbody').html(strAll);
            $('.channel-row>ul').append(strLi);
//  图1
            var data1=response1[0].data;
            var chartOneX=[],chartOneD1=[],chartOneD2=[],name1,danwei1,highPrice,lowPrice;
            highPrice=data1.list1[1].price;
            lowPrice=data1.list1[0].price;
            for(var i1=0;i1<data1.list2.length;i1++){
                chartOneX.push(data1.list2[i1].day);
                chartOneD1.push(data1.list2[i1].total);
                chartOneD2.push(data1.list2[i1].price);
            }
            danwei1='万元';
            name1='销售额';
            chartOne(chartOneX,chartOneD1,chartOneD2,name1,danwei1,time,highPrice,lowPrice);
//  图2
            var data3=response3[0].data;
            var chartThreeL=[],chartThreeX=[],chartThreeD=[],attr3,chartThreeAll=[],danwei3,formatter3,i31,i32;
            for(attr3 in data3){
                chartThreeAll.push(data3[attr3]);
                chartThreeL.push(attr3);  //取legend
                allThreeD=[];
                for(i31=0;i31<data3[attr3].length;i31++){
                    allThreeD.push(data3[attr3][i31].total);
                }
                chartThreeD.push(
                    {
                        name:attr3,
                        type:'line',
                        smooth:true,
                        barMaxWidth:30,
                        data:allThreeD
                    }
                );
            }
            for(i32=0;i32<chartThreeAll[0].length;i32++){  //取横坐标
                chartThreeX.push(chartThreeAll[0][i32].day);
            }
            danwei3='万元';
            formatter3='销售额';
            chartTwo(chartThreeL,chartThreeX,chartThreeD,danwei3,time,formatter3);
//  图3
//            ['好评率','好评数','差评率','差评数','品质关注度','品质评论数','外观关注度','性价比关注度','外观评论数','性价比评论数','口感关注度','口感评论数']
//            ['好评率','差评率','品质关注度','外观关注度','性价比关注度','口感关注度']
            var data4=response4[0].data;
            var chartTwoD=[],chartTwoD2=[];
            chartTwoD.push({
                value:data4[0].A1,
                name:data4[0].A1
            },{
                value:data4[0].A5,
                name:data4[0].A5
            },{
                value:data4[0].A7,
                name:data4[0].A7
            },{
                value:data4[0].A9,
                name:data4[0].A9
            },{
                value:data4[0].A10,
                name:data4[0].A10
            },{
                value:data4[0].A13,
                name:data4[0].A13
            });
            chartTwoD2.push({
                value:data4[0].A2/1000,
                name:data4[0].A2+'条'
            },{
                value:data4[0].A6/1000,
                name:data4[0].A6+'条'
            },{
                value:data4[0].A8/1000,
                name:data4[0].A8+'条'
            },{
                value:data4[0].A11/1000,
                name:data4[0].A11+'条'
            },{
                value:data4[0].A12/1000,
                name:data4[0].A12/1000+'条'
            },{
                value:data4[0].A14/1000,
                name:data4[0].A14+'条'
            });
            chartThree(chartTwoD,time);
            chartThree2(chartTwoD2,time);
            var best=data4[0].best;
            var worst=data4[0].worst;
            $('.comment-content').eq(0).text(best);
            $('.comment-content').eq(1).text(worst);
//  表格1
            createTable1(response5[0].data);
            $("#pageBox1").pagination(response5[0].data[0].total_page, {
                num_edge_entries: 1,
                callback: pageselectCallback1
            });
            tableJson1={
                dateType:1,
                startTime:'',
                endTime:'',
                pageNumber:1,
                goodsBrand:goodsBrand
            };
//  表格2
            createTable2(response6[0].data);
            $("#pageBox2").pagination(response6[0].data[0].total_page, {
                num_edge_entries: 1,
                callback: pageselectCallback2
            });
            tableJson2={
                dateType:1,
                startTime:'',
                endTime:'',
                pageNumber:1,
                goodsBrand:goodsBrand
            };
//  图表大小
            window.onresize=function(){
                chart1.resize();
                chart2.resize();
                chart3.resize();
                chart31.resize();
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
function chartOne(chartOneX,chartOneD1,chartOneD2,name1,danwei1,time,highPrice,lowPrice){
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
        title: {
            //text :  '商品价格销售走势',
            x : 'center',
            //textStyle:{
            //    fontSize:13,
            //    fontFamily:'Microsoft Yahei'
            //},
            subtext : '历史最高价：'+highPrice+'元'+'   '+'历史最低价：'+lowPrice+'元',
            subtextStyle:{
                fontSize:12,
                fontFamily:'Microsoft Yahei',
                color:'red'
            }
        },
        tooltip : {
            trigger: 'axis',
            axisPointer :{
                type: 'shadow'
            },
            formatter:function(params){
                return params[0].name+'<br/>'+name1+'：'+params[0].value+danwei1+'<br/>'+'单价：'+params[1].value+'元';
            }
        },
        toolbox: {
            show : true,
            orient: 'vertical',
            x:'right',
            y:'center',
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
                type : 'value',
                name : name1+'：'+danwei1,
                axisLabel : {
                    formatter: '{value} '
                }
            },
            {
                splitLine : {
                    show:false
                },
                type : 'value',
                name : '单价：元',
                axisLabel : {
                    formatter: '{value} '
                }
            }
        ],
        series : [
            {
                type:'bar',
                barMaxWidth:30,
                name:name1+'：'+danwei1,
                data:chartOneD1
            },
            {
                smooth:true,
                type:'line',
                name : '单价：元',
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
function chartTwo(chartThreeL,chartThreeX,chartThreeD,danwei3,time,formatter){
    chart2 = echarts.init(document.getElementById('chart2'));
    chart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#219eef','#2fc4f1','#5bdaac','#9bce4a','#ced95d'],
        //title: {
        //    text :  '各渠道详情',
        //    x : 'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            trigger: 'axis',
            axisPointer :{
                type: 'shadow'
            },
            formatter:function(params){
                var str=params[0].name+'<br/>';
                for(var i=0;i<params.length;i++){
                    str+=params[i].seriesName+'：'+params[i].value+danwei3+'<br/>';
                }
                return str;
            }
        },
        legend: {
            data:chartThreeL,
            y:30
        },
        toolbox: {
            show : true,
            orient: 'vertical',
            x:'right',
            y:'center',
            feature : {
                magicType : {show: true, type: ['line', 'bar']},
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
                type : 'value',
                name : formatter+'：'+danwei3
            }
        ],
        series : chartThreeD
    };
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
}
//图3
function chartThree(chartTwoD,time){
    chart3 = echarts.init(document.getElementById('chart3'));
    chart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color:['#28b6f6'],
        //title: {
        //    text :  '商品口碑',
        //    x : 'center',
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
            formatter:function(params){
                return params.name+'：'+params.data.name;
            }
        },
        toolbox: {
            show : true,
            orient: 'vertical',
            x:'right',
            y:'center',
            feature : {
                restore : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                },
                show:false,
                type : 'value'
            }
        ],
        yAxis : [
            {
                splitLine : {
                    show:false
                },
                type : 'category',
                data : ['好评数','差评数','品质评论数','外观评论数','性价比评论数','口感评论数']
            }
        ],
        series : [
            {
                type:'bar',
                barMaxWidth:10,
                data:chartTwoD
            }
        ]
    };
    setTimeout(function (){
        chart3.hideLoading();
        chart3.setOption(option);
    },1000);
}
function chartThree2(chartTwoD,time){
    chart31 = echarts.init(document.getElementById('chart31'));
    chart31.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color:['#28b6f6'],
        //title: {
        //    text :  '商品口碑',
        //    x : 'center',
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
            formatter:function(params){
                return params.name+'：'+params.data.name;
            }
        },
        toolbox: {
            show : true,
            orient: 'vertical',
            x:'right',
            y:'center',
            feature : {
                restore : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                splitArea:{
                    show:true
                },
                splitLine : {
                    show:false
                },
                show:false,
                type : 'value'
            }
        ],
        yAxis : [
            {
                splitLine : {
                    show:false
                },
                type : 'category',
                data : ['好评率','差评率','品质关注度','外观关注度','性价比关注度','口感关注度']
            }
        ],
        series : [
            {
                type:'bar',
                barMaxWidth:10,
                data:chartTwoD
            }
        ]
    };
    setTimeout(function (){
        chart31.hideLoading();
        chart31.setOption(option);
    },1000);
}
//表格1生成函数
function createTable1(data){
    if(data.length==0 || data.length==undefined || data.length==null){
        $('#goodTable').find('tbody').html('<tr><td colspan="5">暂无数据</td></tr>');
        $("#pageBox1").pagination(1, {
            num_edge_entries: 1
        });
    }else{
        var str='';
        var danwei='件';
        var price='元';
        for(var i=0;i<data.length;i++){  //10条一页
            str+='<tr>' +
                '<td>'+((parseInt(tableJson1.pageNumber)-1)*10+i+1)+'</td>' +
                '<td><span class="table-name" title="'+data[i].goodsname+'">'+data[i].goodsname+'</span></td>' +
                '<td>'+data[i].total.toLocaleString()+danwei+'</td>' +
                '<td style="color: #EF6666;font-weight: 600;">'+data[i].price+price+'</td>' +
                '<td>'+data[i].compete_index+'</td>' +
                '</tr>';
        }
        $('#goodTable').find('tbody').html(str);
    }
}
//表格1分页
function pageselectCallback1(page_index){
    if(countPageNumber1==0){
        countPageNumber1=1;
        return false;
    }
    tableJson1.pageNumber=parseInt(page_index)+1;
    $.ajax({
        url:AJAXURL.getGoodsTopByCategory,
        type:'post',
        data:tableJson1,
        dataType:'json',
        success:function(response){
            createTable1(response.data);
        }
    });
}
//表格2生成函数
function createTable2(data){
    if(data.length==0 || data.length==undefined || data.length==null){
        $('#goodTable2').find('tbody').html('<tr><td colspan="5">暂无数据</td></tr>');
        $("#pageBox2").pagination(1, {
            num_edge_entries: 1
        });
    }else{
        var str='';
        var danwei='件';
        for(var i=0;i<data.length;i++){  //10条一页
            str+='<tr>' +
                '<td>'+((parseInt(tableJson2.pageNumber)-1)*10+i+1)+'</td>' +
                '<td><span class="table-name" title="'+data[i].goodsname+'">'+data[i].goodsname+'</span></td>' +
                '<td>'+data[i].total.toLocaleString()+danwei+'</td>' +
                '<td><div class="table-pic-box"><span class="pic-prev">'+data[i].goods_sales_flag+'</span>'+'<img src="../images/'+data[i].icon1+'.png"/></div></td>' +
                '<td>'+data[i].compete_index+'</td>' +
                '</tr>';
        }
        $('#goodTable2').find('tbody').html(str);
    }
}
//表格2分页
function pageselectCallback2(page_index){
    if(countPageNumber2==0){
        countPageNumber2=1;
        return false;
    }
    tableJson2.pageNumber=parseInt(page_index)+1;
    $.ajax({
        url:AJAXURL.getGoodsTopByBrand,
        type:'post',
        data:tableJson2,
        dataType:'json',
        success:function(response){
            createTable2(response.data);
        }
    });
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
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            goodName:$('#goodName').val(),
            goodsId:$('#goodsId').val(),
            channel:$('#channel').val(),
            saleType:parseInt(saleType)+1
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getGoodsSaleInfo,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data1=response.data;
            var chartOneX=[],chartOneD1=[],chartOneD2=[],name1,danwei1,highPrice,lowPrice;
            highPrice=data1.list1[1].price;
            lowPrice=data1.list1[0].price;
            for(var i1=0;i1<data1.list2.length;i1++){
                chartOneX.push(data1.list2[i1].day);
                chartOneD1.push(data1.list2[i1].total);
                chartOneD2.push(data1.list2[i1].price);
            }
            if(parseInt(saleType)==0){
                danwei1='万元';
                name1='销售额';
            }else{
                danwei1='万件';
                name1='销售量';
            }
            var time;
            time=startTime+'至'+endTime;
            chartOne(chartOneX,chartOneD1,chartOneD2,name1,danwei1,time,highPrice,lowPrice);
        }
    });
});
//图2点击事件
$('#btn2').on('click',function(){
    var json;
    var saleType=$(this).prev().find('.pn-active').index();
    var channelList=[],channelBox=$('.channel-table').find('.channel-active');
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }
    for(var i=0;i<channelBox.length;i++){
        channelList.push(channelBox.eq(i).text());
    }
    if(channelList.length==0){
        alert('至少选择一个渠道');
        return false;
    }
    json={
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        goodName:$('#goodName').val(),
        goodsId:$('#goodsId').val(),
        channelList:channelList,
        saleType:parseInt(saleType)+1
    };
    $.ajax({  //图2
        url:AJAXURL.getChannelGoodDuibi2,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data3=response.data;
            var chartThreeL=[],chartThreeX=[],chartThreeD=[],attr3,chartThreeAll=[],danwei3,formatter3,i31,i32;
            for(attr3 in data3){
                chartThreeAll.push(data3[attr3]);
                chartThreeL.push(attr3);  //取legend
                allThreeD=[];
                for(i31=0;i31<data3[attr3].length;i31++){
                    allThreeD.push(data3[attr3][i31].total);
                }
                chartThreeD.push(
                    {
                        name:attr3,
                        type:'line',
                        smooth:true,
                        barMaxWidth:30,
                        data:allThreeD
                    }
                );
            }
            for(i32=0;i32<chartThreeAll[0].length;i32++){  //取横坐标
                chartThreeX.push(chartThreeAll[0][i32].day);
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
            chartTwo(chartThreeL,chartThreeX,chartThreeD,danwei3,time,formatter3);
        }
    })
});
//图3点击事件
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
            goodsId:$('#goodsId').val()
        };
    }
    $.ajax({  //图3
        url:AJAXURL.getGoodsComment,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data4=response.data;
            var chartTwoD=[],chartTwoD2=[];
            var time;
            time=startTime+'至'+endTime;
            chartTwoD.push({
                value:data4[0].A1,
                name:data4[0].A1
            },{
                value:data4[0].A5,
                name:data4[0].A5
            },{
                value:data4[0].A7,
                name:data4[0].A7
            },{
                value:data4[0].A9,
                name:data4[0].A9
            },{
                value:data4[0].A10,
                name:data4[0].A10
            },{
                value:data4[0].A13,
                name:data4[0].A13
            });
            chartTwoD2.push({
                value:data4[0].A2/1000,
                name:data4[0].A2+'条'
            },{
                value:data4[0].A6/1000,
                name:data4[0].A6+'条'
            },{
                value:data4[0].A8/1000,
                name:data4[0].A8+'条'
            },{
                value:data4[0].A11/1000,
                name:data4[0].A11+'条'
            },{
                value:data4[0].A12/1000,
                name:data4[0].A12/1000+'条'
            },{
                value:data4[0].A14/1000,
                name:data4[0].A14+'条'
            });
            chartThree(chartTwoD,time);
            chartThree2(chartTwoD2,time);
            var best=data4[0].best;
            var worst=data4[0].worst;
            $('.comment-content').eq(0).text(best);
            $('.comment-content').eq(1).text(worst);
        }
    });
});
//表格1点击事件
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
            pageNumber:1,
            goodsBrand:$('#goodsBrand').val()
        };
    }
//  点确定更新所有变量
    tableJson1={
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        goodsBrand:$('#goodsBrand').val()
    };
    $.ajax({  //表格1
        url:AJAXURL.getGoodsTopByCategory,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            createTable1(response.data);
            $("#pageBox1").pagination(response.data[0].total_page, {
                num_edge_entries: 1,
                callback: pageselectCallback1
            });
        }
    });
});
//表格2点击事件
$('#btn5').on('click',function(){
    var json;
    var startTime=$('#myCalender9').val();
    var endTime=$('#myCalender10').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            pageNumber:1,
            goodsBrand:$('#goodsBrand').val()
        };
    }
//  点确定更新所有变量
    tableJson2={
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        goodsBrand:$('#goodsBrand').val()
    };
    $.ajax({  //表格2
        url:AJAXURL.getGoodsTopByBrand,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            createTable2(response.data);
            $("#pageBox2").pagination(response.data[0].total_page, {
                num_edge_entries: 1,
                callback: pageselectCallback2
            });
        }
    });
});