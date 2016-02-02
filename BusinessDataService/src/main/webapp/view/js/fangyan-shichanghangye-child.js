var tableJson={
    dateType:1,
    startTime:'',
    endTime:'',
    pageNumber:1,
    saleType:1,
    seqType:1  //默认降序  1.降序;2升序
},
    countPageNumber= 0,
    chart2,chart3;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('市场行业细分价格区间分布');
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

    var href=window.location.search.split('&');
    var price=href[0].split('=')[1];
    var priceName=decodeURI(href[1].split('=')[1]);
//    var price='A3';
//    var priceName='300-500元';
    var time='近三个月';
    $('#price').val(price);
    $('.price-box').text(priceName);
    $.when(
        $.ajax({  //表格
            url:AJAXURL.getDetailPriceInfo,
            type:'post',
            data:{
                price:price,
                dateType:1,
                saleType:1,
                pageNumber:1,
                seqType:1,
                channelList:AJAXURL.channel1
            },
            dataType:'json'
        }),
        $.ajax({  //图1
            url:AJAXURL.getDetailMarketSale,
            type:'post',
            data:{
                price:price,
                dateType:1,
                saleType:1,
                channelList:AJAXURL.channel1
            },
            dataType:'json'
        }),
        $.ajax({  //图2
            url:AJAXURL.getGoodsBrandPercent,
            type:'post',
            data:{
                price:price,
                dateType:1,
                saleType:1,
                channelList:AJAXURL.channel1
            },
            dataType:'json'
        })
    ).done(function(response1,response3,response4){
//  表格
            createTable(response1[0].data,7);
            $('.triangle-box').eq(0).css('visibility','visible');
            $("#pageBox1").pagination(response1[0].data[0].total_page, {
                num_edge_entries: 1,
                num_display_entries: 10,
                callback: pageselectCallback
            });
//  存入所有变量，该变量用于分页ajax传值
            tableJson={
                dateType:1,
                startTime:'',
                endTime:'',
                saleType:1,
                price:price,
                pageNumber:1,
                seqType:1,
                channelList:AJAXURL.channel1
            };
//  图1
            var data3=response3[0].data;
            var chartThreeX=[],chartThreeD=[];
            var danwei3,threeData,fomatter3;
            danwei3='万元';
            fomatter3='销售额';
            for(var i3=0;i3<data3.length;i3++){
                chartThreeX.push(data3[i3].day);
                threeData=(parseFloat(data3[i3].total)/10000).toString();
                chartThreeD.push(threeData.substring(0,threeData.indexOf('.')+3));
            }
            chartOne(chartThreeX,chartThreeD,danwei3,fomatter3,time,priceName);
//  图2
            var data4=response4[0].data;
            var chartFourL=[],chartFourD=[];
            var formatter4='销售额';
            for(var i4=0;i4<data4.length;i4++){
                chartFourL.push(data4[i4].goodsbrand);
                chartFourD.push({
                    value:data4[i4].percent,
                    name:data4[i4].goodsbrand
                });
            }
            chartTwo(chartFourL,chartFourD,time,priceName,formatter4);
//          渠道
            channelLists();
//  图表大小变化
            window.onresize=function(){
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
//表格生成函数
function createTable(data,num){
    if(data.length==0 || data.length==undefined || data.length==null){
        $('#goodTable').find('tbody').html('<tr><td colspan="'+num+'">暂无数据</td></tr>');
        $("#pageBox1").pagination(1, {
            num_edge_entries: 1,
            num_display_entries: 10
        });
    }else{
        var str='',tableHeader='';
        if(num==7){  //销售量销售额表格
            for(var i=0;i<data.length;i++){  //10条一页
                str+='<tr>' +
                    '<td style="color: #f2ae25;">'+((parseInt(tableJson.pageNumber)-1)*10+i+1)+'</td>' +
                    '<td><span class="table-name" onclick="goNextPage(this);" title="'+data[i].goodsname+'-'+data[i].goodsid+'">'+data[i].goodsname+'</span><span class="goods-brand">'+data[i].goodsbrand+'</span><span class="table-price">'+data[i].price+'元</span><a href="javascript:void(0);" class="link-box" onclick="openWindow(this);" title="'+data[i].goods_url+'"></a></td>' +
                    '<td class="price-total1 table-active">'+data[i].total_sale.toLocaleString()+'元</td>' +
                    '<td class="price-total2">'+data[i].total_num.toLocaleString()+'件</td>' +
                    '<td><span class="channel-name">'+data[i].channel+'</span>&nbsp;'+'</td>' +
                    '<td><div class="table-pic-box"><span class="pic-prev">'+data[i].goods_sales_flag+'</span>'+'<img src="../images/'+data[i].icon1+'.png"/></div></td>' +
                    '<td>' +
                    '<span class="good-id">'+data[i].goodsid+'</span>'+
                    '<div class="compare-btn" onclick="addCompare(this);">加入对比</div>' +
                    '</td>' +
                    '</tr>';
            }
            tableHeader+='<tr>' +
                '<th>排名</th>' +
                '<th>商品名</th>' +
                '<th>' +
                '<span style="vertical-align: middle;">销售额</span>' +
                '<div class="triangle-box">' +
                '<span class="triangle-up up-down" onclick="upDown(this);"></span>' +
                '<span class="triangle-down up-down" onclick="upDown(this);"></span>' +
                '</div>' +
                '</th>' +
                '<th>' +
                '<span style="vertical-align: middle;">销售量</span>' +
                '<div class="triangle-box">' +
                '<span class="triangle-up up-down" onclick="upDown(this);"></span>' +
                '<span class="triangle-down up-down" onclick="upDown(this);"></span>' +
                '</div>' +
                '</th>' +
                '<th>渠道名</th>' +
                '<th>排名变化</th>' +
                '<th>操作</th>' +
                '</tr>';
        }else{  //评论数表格
            for(var i=0;i<data.length;i++){  //10条一页
                str+='<tr>' +
                    '<td style="color: #f2ae25;">'+((parseInt(tableJson.pageNumber)-1)*10+i+1)+'</td>' +
                    '<td><span class="table-name" onclick="goNextPage(this);" title="'+data[i].goodsname+'-'+data[i].goodsid+'">'+data[i].goodsname+'</span><span class="goods-brand">'+data[i].goodsbrand+'</span><span class="table-price">'+data[i].price+'元</span><a href="javascript:void(0);" class="link-box" onclick="openWindow(this);" title="'+data[i].goods_url+'"></a></td>' +
                    '<td class="price-total3 table-active">'+data[i].total_comment.toLocaleString()+'条</td>' +
                    '<td><span class="channel-name">'+data[i].channel+'</span>&nbsp;'+'</td>' +
                    '<td><div class="table-pic-box"><span class="pic-prev">'+data[i].goods_sales_flag+'</span>'+'<img src="../images/'+data[i].icon1+'.png"/></div></td>' +
                    '<td>' +
                    '<span class="good-id">'+data[i].goodsid+'</span>'+
                    '<div class="compare-btn" onclick="addCompare(this);">加入对比</div>' +
                    '</td>' +
                    '</tr>';
            }
            tableHeader+='<tr>' +
                '<th>排名</th>' +
                '<th>商品名</th>' +
                '<th>' +
                '<span style="vertical-align: middle;">评论数</span>' +
                '<div class="triangle-box">' +
                '<span class="triangle-up up-down" onclick="upDown(this);"></span>' +
                '<span class="triangle-down up-down" onclick="upDown(this);"></span>' +
                '</div>' +
                '</th>' +
                '<th>渠道名</th>' +
                '<th>排名变化</th>' +
                '<th>操作</th>' +
                '</tr>';
        }
        $('#goodTable').find('thead').html(tableHeader);
        $('#goodTable').find('tbody').html(str);
        if(parseInt(tableJson.saleType)==1){
            $('.price-total1').addClass('table-active');
            $('.price-total2').removeClass('table-active');
            $('#goodTable').find('.triangle-box').eq(0).css('visibility','visible');
            $('#goodTable').find('.triangle-box').eq(1).css('visibility','hidden');
        }else if(parseInt(tableJson.saleType)==2){
            $('.price-total2').addClass('table-active');
            $('.price-total1').removeClass('table-active');
            $('#goodTable').find('.triangle-box').eq(1).css('visibility','visible');
            $('#goodTable').find('.triangle-box').eq(0).css('visibility','hidden');
        }else{
            $('#goodTable').find('.triangle-box').eq(0).css('visibility','visible');
        }

        if(tableJson.saleType==2){  //销售量
            if(tableJson.seqType==1){  //降序
                $('#goodTable').find('.triangle-box').eq(1).find('.up-down').eq(1).css('border-top','6px solid #EF6666').prev().css('border-bottom','6px solid #fff');
            }else{
                $('#goodTable').find('.triangle-box').eq(1).find('.up-down').eq(0).css('border-bottom','6px solid #EF6666').next().css('border-top','6px solid #fff');
            }
        }else{  //销售额和评论数
            if(tableJson.seqType==1){
                $('#goodTable').find('.triangle-box').eq(0).find('.up-down').eq(1).css('border-top','6px solid #EF6666').prev().css('border-bottom','6px solid #fff');
            }else{
                $('#goodTable').find('.triangle-box').eq(0).find('.up-down').eq(0).css('border-bottom','6px solid #EF6666').next().css('border-top','6px solid #fff');
            }
        }
    }
}
//加入对比
function addCompare(obj){
    var num=$('.compare-box').find('li').length;
    var _this=$(obj);
    var str='';
    if(parseInt(num)==6){
        alert('最多5项对比');
        return false;
    }
    var id=_this.prev().text();
    var goodId=$('.compare-box').find('.good-id');
    for(var i=0;i<goodId.length;i++){
        if(goodId.eq(i).text()==id){
            alert('该产品已加入对比');
            return false;
        }
    }
    _this.addClass('btn-blue');
    str+='<li>' +
        '<span class="good-box">'+_this.parent().siblings().eq(1).text()+'</span>' +
        '<span class="good-id">'+_this.prev().text()+'</span>' +
        '<span class="glyphicon glyphicon-remove" onclick="removeCompare(this);"></span>' +
        '</li>';
    $('.compare-box').css('visibility','visible').find('ul').prepend(str);
}
//删除对比
function removeCompare(obj){
    var _this=$(obj);
    var _text=$(obj).prev().text();
    _this.parent().remove();  //删除当前
    var num=$('.compare-box ul').find('.good-box').length;
    if(parseInt(num)==0){
        $('.compare-box').css('visibility','hidden');  //删除所有就隐藏按钮
    }
    var idLists=$('#goodTable').find('.good-id');
    idLists.each(function(){
        if($(this).text()==_text){
            $(this).siblings().removeClass('btn-blue');  //将所删除的对比在表格中对应的那一条数据的按钮样式移除
        }
    });
}
//设置按钮选中
function btnBlue(){
    var idList=$('.compare-box').find('.good-id');
    var tdList=$('#goodTable').find('.good-id');
    for(var i=0;i<idList.length;i++){
        for(var j=0;j<tdList.length;j++){
            if(idList.eq(i).text() == tdList.eq(j).text()){
                tdList.eq(j).siblings().addClass('btn-blue');
            }
        }
    }
}
//表格1点击事件
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
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }
//  点确定更新所有变量
    tableJson={
        saleType:parseInt(saleType)+1,
        price:$('#price').val(),
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        seqType:1,  //重置为降序
        channelList:channelList
    };
    $.ajax({
        url:AJAXURL.getDetailPriceInfo,
        type:'post',
        data:tableJson,
        dataType:'json',
        success:function(response){
            var time,num;
            time=startTime+'至'+endTime;
            $('.time-box').text(time);
            if(tableJson.saleType==3){
                num=6;
            }else{
                num=7
            }
            createTable(response.data,num);
            if(tableJson.saleType==1){
                $('#goodTable').find('.triangle-box').eq(0).css('visibility','visible');
                $('#goodTable').find('.triangle-box').eq(1).css('visibility','hidden');
            }else if(tableJson.saleType==2){
                $('#goodTable').find('.triangle-box').eq(0).css('visibility','hidden');
                $('#goodTable').find('.triangle-box').eq(1).css('visibility','visible');
            }else{
                $('#goodTable').find('.triangle-box').eq(0).css('visibility','hidden');
            }
            $('.triangle-down').css('border-top','6px solid #EF6666');
            $('.triangle-up').css('border-bottom','6px solid #fff');
            $("#pageBox1").pagination(response.data[0].total_page, {  //点确定分页重置
                num_edge_entries: 1,
                num_display_entries: 10,
                callback: pageselectCallback
            });
            btnBlue();
        }
    });
});
//分页
function pageselectCallback(page_index){
    if(countPageNumber==0){
        countPageNumber=1;
        return false;
    }
    tableJson.pageNumber=parseInt(page_index)+1;
    $.ajax({
        url:AJAXURL.getDetailPriceInfo,
        type:'post',
        data:tableJson,
        dataType:'json',
        success:function(response){
            var num;
            if(tableJson.saleType==3){
                num=6;
            }else{
                num=7;
            }
            createTable(response.data,num);
            btnBlue();
        }
    });
}
//图1
function chartOne(chartThreeX,chartThreeD,danwei3,fomatter3,time,priceName){
    chart2 = echarts.init(document.getElementById('chart2'));
    chart2.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        color : ['#29b6f6'],
        //title : {
        //    text: priceName+'市场'+fomatter3+'变化',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        tooltip : {
            axisPointer :{
                type: 'shadow'
            },
            trigger: 'item',
            formatter : '{b} <br/> {c}'+danwei3
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
        legend: {
            data:[''],
            show:false
        },
        dataZoom:{
            show : true,
            start:0,
            end:100
        },
        xAxis : [
            {
                splitLine : {
                    show: false
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
                type : 'value',
                name : fomatter3+'：'+danwei3
            }
        ],
        series : [
            {
                type:'line',
                smooth:true,
                barMaxWidth:30,
                data:chartThreeD
            }
        ]
    };
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
}
//图2
function chartTwo(chartFourL,chartFourD,time,priceName,formatter){
    chart3 = echarts.init(document.getElementById('chart3'));
    chart3.showLoading({  //载入动画
        text : '请稍等',
        effect : 'whirling',
        textStyle : {
            fontSize : 14
        }
    });
    var option = {
        //title : {
        //    text: priceName+'市场品牌'+formatter+'占比',
        //    x:'center',
        //    textStyle:{
        //        fontSize:13,
        //        fontFamily:'Microsoft Yahei'
        //    }
        //},
        color : AJAXURL.color,
        tooltip : {
            trigger: 'item',
            formatter: "{b} : {d}%"
        },
        legend: {
            orient : 'vertical',
            x : 45,
            data:chartFourL
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
                name:'访问来源',
                type:'pie',
                radius : '45%',
                center: ['50%', '50%'],
                data:chartFourD,
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            formatter: '{d}%'
                        },
                        labelLine: {show: true}
                    }
                }
            }
        ]
    };
    setTimeout(function(){
        chart3.hideLoading();
        chart3.setOption(option);
    },1000);
}
//图1点击事件
$('#btn2').on('click',function(){
    var channelList=[];
    var channelAll=$(this).next().find('.channel-table').find('.channel-active');
    for(var i=0;i<channelAll.length;i++){
        channelList.push(channelAll.eq(i).text())
    }
    if(channelList.length==0){
        alert('至少选择一个渠道');
        return false;
    }
    var json;
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            price:$('#price').val(),
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            channelList:channelList
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getDetailMarketSale,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data3=response.data;
            var chartThreeX=[],chartThreeD=[];
            var danwei3,fomatter3,threeData,time;
            if(parseInt(saleType)==0){
                danwei3='万元';
                fomatter3='销售额';
            }else if(parseInt(saleType)==1){
                danwei3='万件';
                fomatter3='销售量';
            }else{
                danwei3='万条';
                fomatter3='评论数';
            }
            time=startTime+'至'+endTime;
            for(var i3=0;i3<data3.length;i3++){
                chartThreeX.push(data3[i3].day);
                threeData=(parseFloat(data3[i3].total)/10000).toString();
                chartThreeD.push(threeData.substring(0,threeData.indexOf('.')+3));
            }
            chartOne(chartThreeX,chartThreeD,danwei3,fomatter3,time,$('.price-box').eq(0).text());
        }
    })
});
//图2点击事件
$('#btn3').on('click',function(){
    var channelList=[];
    var channelAll=$(this).next().find('.channel-table').find('.channel-active');
    for(var i=0;i<channelAll.length;i++){
        channelList.push(channelAll.eq(i).text())
    }
    if(channelList.length==0){
        alert('至少选择一个渠道');
        return false;
    }
    var json;
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender5').val();
    var endTime=$('#myCalender6').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            price:$('#price').val(),
            saleType:parseInt(saleType)+1,
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            channelList:channelList
        };
    }
    $.ajax({  //图2
        url:AJAXURL.getGoodsBrandPercent,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data4=response.data;
            var chartFourL=[],chartFourD=[];
            var time,formatter;
            time=startTime+'至'+endTime;
            if(parseInt(saleType)==0){
                formatter='销售额';
            }else{
                formatter='销售量';
            }
            for(var i4=0;i4<data4.length;i4++){
                chartFourL.push(data4[i4].goodsbrand);
                chartFourD.push({
                    value:data4[i4].percent,
                    name:data4[i4].goodsbrand
                });
            }
            chartTwo(chartFourL,chartFourD,time,$('.price-box').eq(0).text(),formatter);
        }
    })
});
//进入商品详情
function goNextPage(obj){
    var This=$(obj);
    var goodName=encodeURI(This.text());
    var goodsId=This.parent().parent().find('.good-id').text();
    var channel=encodeURI(This.parent().parent().find('.channel-name').text());
    var goodsbrand=encodeURI(This.next().text());
    var _iframe=$(window.parent.document.getElementById('iframepage'));
    _iframe.attr('src','shangpin-shangpinxiangqing.html?goodName='+goodName+'&goodsId='+goodsId+'&channel='+channel+'&goodsbrand='+goodsbrand);
}
//开始对比
$('.compare-btn-go').on('click',function(){
    var goodNamelist=[],goodIdlist=[];
    var compareBox=$('.compare-box');
    var shopBox=compareBox.find('.good-box');
    var idBox=compareBox.find('.good-id');
    var _iframe=$(window.parent.document.getElementById('iframepage'));
    for(var i=0;i<shopBox.length;i++){
        goodNamelist.push(shopBox.eq(i).text());
        goodIdlist.push(idBox.eq(i).text());
    }
    if(goodNamelist.length<2){
        alert('至少选择两项对比');
        return false;
    }
    _iframe.attr('src','shangpin-shangpinduibi-child.html?goodNamelist='+encodeURI(goodNamelist)+'&goodIdlist='+encodeURI(goodIdlist));
});
//销售额销售量排列
function upDown(obj){
    var This=$(obj);
    var index=parseInt(This.index('.up-down'));
    if(index==0 || index==1){  //销售额和评论数
        if(index==0){
            tableJson.seqType=2;
        }else{
            tableJson.seqType=1;
        }
    }else{  //销售量
        if(index==2){
            tableJson.seqType=2;
        }else{
            tableJson.seqType=1;
        }
    }
    tableJson.pageNumber=1;
    $.ajax({
        url:AJAXURL.getDetailPriceInfo,
        type:'post',
        data:tableJson,
        dataType:'json',
        success:function(response){
            var num;
            if(tableJson.saleType==3){
                num=6;
            }else{
                num=7;
            }
            createTable(response.data,num);
            $("#pageBox1").pagination(response.data[0].total_page, {  //分页重置
                num_edge_entries: 1,
                num_display_entries: 10,
                callback: pageselectCallback
            });
            btnBlue();
        }
    });
}
//导出
function downLoad(){
    var _iframe=$('.downLoad-box')[0];
    var len=tableJson.channelList.length;
    var str='';
    for(var i=0;i<len;i++){
        str+='&channelList['+i+']='+tableJson.channelList[i];
    }
    _iframe.src=AJAXURL.exportDataExcel+'?dateType='+tableJson.dateType+'&startTime='+tableJson.startTime+'&endTime='+tableJson.endTime+'&seqType='+tableJson.seqType+'&price='+tableJson.price+'&saleType='+tableJson.saleType+'&channel_num='+len+str;
}