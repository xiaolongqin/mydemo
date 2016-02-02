var tableJson={
        dateType:1,
        startTime:'',
        endTime:'',
        brandName:'',
        pageNumber:1,
        saleType:1,
        seqType:1  //默认降序  1.降序;2升序
    },
    countPageNumber= 0,
    chart2;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('品牌扫描');
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
        $.ajax({  //表格1
            url:AJAXURL.getGoodsbrandSaleTotal,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                pageNumber:1,
                brandName:'',
                channelList:''
            },
            dataType:'json'
        }),
        $.ajax({  //图1
            url:AJAXURL.getGoodsbrandTopBycategory,
            type:'post',
            data:{
                dateType:1,
                saleType:1
            },
            dataType:'json'
        })
    ).done(function(response1,response2,response3){
            var time='近三个月';
//  获取所有渠道
            var data2=response1[0].data;
            var strTd='',strTr='',strAll='',strLi='';
            var channelList=[];
            for(var i=0;i<data2.length;i++){
                strLi+='<li><span>'+data2[i].channel+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
                channelList.push(data2[i].channel);
                strTd+='<td><div class="channel-name-box channel-active" onclick="channelChange(this);">'+data2[i].channel+'</div></td>';
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
//  表格
            createTable(response2[0].data);
            $('.triangle-box').eq(0).css('visibility','visible');
            $("#pageBox1").pagination(response2[0].data[0].total_page, {
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
                brandName:'',
                pageNumber:1,
                seqType:1,
                channelList:channelList
            };
//  图1
            var data4=response3[0].data;
            var chartOneX=[],chartOneD=[],danwei4,fomatter4;
            for(var i4=0;i4<data4.length;i4++){
                chartOneX.push(data4[i4].goodsbrand);
                chartOneD.push(data4[i4].total);
            }
            danwei4='万元';
            fomatter4='销售额';
            chartOne(chartOneX,chartOneD,danwei4,time,fomatter4);
//  图表大小变化
            window.onresize=function(){
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
//表格生成函数
function createTable(data){
    var danwei,titleName;
    if(data.length==0 || data.length==undefined || data.length==null){
        $('#goodTable').find('tbody').html('<tr><td colspan="6">暂无数据</td></tr>');
        $("#pageBox1").pagination(1, {
            num_edge_entries: 1,
            num_display_entries: 10
        });
    }else{
        var str='';
        for(var i=0;i<data.length;i++){  //10条一页
            str+='<tr>' +
                '<td>'+((parseInt(tableJson.pageNumber)-1)*10+i+1)+'</td>' +
                '<td><span class="table-name" onclick="goDetails(this);" title="'+data[i].goodsbrand+'">'+data[i].goodsbrand+'</span></td>' +
                '<td class="price-total1 table-active">'+data[i].total_sale.toLocaleString()+'元</td>' +
                '<td class="price-total2">'+data[i].total_num.toLocaleString()+'件</td>' +
                '<td><span>'+data[i].channel+'</span></td>' +
//                '<td><div class="table-pic-box"><span class="pic-prev">'+data[i].goods_sales_flag+'</span>'+'<img src="../images/'+data[i].icon1+'.png"/></div></td>' +
                '<td>' +
                '<div class="compare-btn" onclick="addCompare(this);">加入对比</div>' +
                '</td>' +
                '</tr>';
        }
        $('#goodTable').find('tbody').html(str);
        if(parseInt(tableJson.saleType)==1){
            $('.price-total1').addClass('table-active');
            $('.price-total2').removeClass('table-active');
        }else{
            $('.price-total2').addClass('table-active');
            $('.price-total1').removeClass('table-active');
        }
        //$('#titleName').text(titleName);
    }
}
//分页
function pageselectCallback(page_index){
    if(countPageNumber==0){
        countPageNumber=1;
        return false;
    }
    tableJson.pageNumber=parseInt(page_index)+1;
    $.ajax({
        url:AJAXURL.getGoodsbrandSaleTotal,
        type:'post',
        data:tableJson,
        dataType:'json',
        success:function(response){
            createTable(response.data);
            btnBlue();
        }
    });
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
    var id=_this.parent().siblings().eq(1).text();
    var goodId=$('.compare-box').find('.good-box');
    for(var i=0;i<goodId.length;i++){
        if(goodId.eq(i).text()==id){
            alert('该产品已加入对比');
            return false;
        }
    }
    _this.addClass('btn-blue');
    str+='<li>' +
        '<span class="good-box">'+_this.parent().siblings().eq(1).text()+'</span>' +
        //'<span class="good-id">'+_this.prev().text()+'</span>' +
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
    var idLists=$('#goodTable').find('.table-name');
    idLists.each(function(){
        if($(this).text()==_text){
            $(this).parent().parent().find('.compare-btn').removeClass('btn-blue');  //将所删除的对比在表格中对应的那一条数据的按钮样式移除
        }
    });
}
//设置按钮选中
function btnBlue(){
    var idList=$('.compare-box').find('.good-box');
    var tdList=$('#goodTable').find('.table-name');
    for(var i=0;i<idList.length;i++){
        for(var j=0;j<tdList.length;j++){
            if(idList.eq(i).text() == tdList.eq(j).text()){
                tdList.eq(j).parent().parent().find('.compare-btn').addClass('btn-blue');
            }
        }
    }
}
//表格1点击事件
$('#btn1').on('click',function(){
    var json;
    var channelList=[];
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    var brandName= $.trim($('#brandName').val());
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }
    var channelAll=$('.channel-table').find('.channel-active');
    for(var i=0;i<channelAll.length;i++){
        channelList.push(channelAll.eq(i).text())
    }
    if(channelList.length==0){
        alert('至少选择1个渠道');
        return false;
    }
    json={
        saleType:parseInt(saleType)+1,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        seqType:1,
        brandName:brandName,
        channelList:channelList
    };
//  点确定更新所有变量
    tableJson={
        saleType:parseInt(saleType)+1,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        seqType:1,  //重置为降序
        brandName:brandName,
        channelList:channelList
    };
    $.ajax({  //表格
        url:AJAXURL.getGoodsbrandSaleTotal,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            if(tableJson.saleType==1){
                $('.triangle-box').eq(0).css('visibility','visible');
                $('.triangle-box').eq(1).css('visibility','hidden');
            }else{
                $('.triangle-box').eq(0).css('visibility','hidden');
                $('.triangle-box').eq(1).css('visibility','visible');
            }
            $('.triangle-down').css('border-top','6px solid #EF6666');
            $('.triangle-up').css('border-bottom','6px solid #fff');
            var time;
            time=startTime+'至'+endTime;
            $('.time-box').text(time);
            createTable(response.data);
            $("#pageBox1").pagination(response.data[0].total_page, {
                num_edge_entries: 1,
                num_display_entries: 10,
                callback: pageselectCallback
            });
        }
    });
});
//图1
function chartOne(chartOneX,chartOneD,danwei,time,fomatter){
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
            //text: '店铺'+fomatter+'TOP10',
            x:'center',
            //textStyle:{
            //    fontSize:13,
            //    fontFamily:'Microsoft Yahei'
            //},
            subtext:'温馨提示：点击柱状图可查看详情',
            subtextStyle:{
                color:'red',
                fontSize:12
            }
        },
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type:'shadow'
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
                splitLine:{
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
                name: '单位：' + danwei,
                type : 'value'
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
                data:chartOneD
            }
        ]
    };
    setTimeout(function (){
        chart2.hideLoading();
        chart2.setOption(option);
    },1000);
//  点击事件
    var ecConfig = echarts.config;
    chart2.on(ecConfig.EVENT.CLICK, function(params) {
        var brandName = encodeURI(params.name);
        var _iframe=$(window.parent.document.getElementById('iframepage'));
        _iframe.attr('src','pinpai-pinpaixiangqing.html?brandName='+brandName);
    });
}
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
            dateType:0,
            startTime:startTime,
            endTime:endTime,
            saleType:parseInt(saleType)+1
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getGoodsbrandTopBycategory,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data4=response.data;
            var chartOneX=[],chartOneD=[],danwei4,fomatter4,time;
            for(var i4=0;i4<data4.length;i4++){
                chartOneX.push(data4[i4].goodsbrand);
                chartOneD.push(data4[i4].total);
            }
            if(parseInt(saleType)==0){
                danwei4='万元';
                fomatter4='销售额';
            }else{
                danwei4='万件';
                fomatter4='销售量';
            }
            time=startTime+'至'+endTime;
            chartOne(chartOneX,chartOneD,danwei4,time,fomatter4);
        }
    });
});
//进入详情
function goDetails(obj){
    var brandName=encodeURI($(obj).text());
    var _iframe=$(window.parent.document.getElementById('iframepage'));
    _iframe.attr('src','pinpai-pinpaixiangqing.html?brandName='+brandName);
}
//开始对比
$('.compare-btn-go').on('click',function(){
    var brandNameList=[];
    var compareBox=$('.compare-box');
    var shopBox=compareBox.find('.good-box');
    var idBox=compareBox.find('.good-id');
    var _iframe=$(window.parent.document.getElementById('iframepage'));
    for(var i=0;i<shopBox.length;i++){
        brandNameList.push(shopBox.eq(i).text());
    }
    if(brandNameList.length<2){
        alert('至少选择两项对比');
        return false;
    }
    _iframe.attr('src','pinpai-pinpaiduibi-child.html?brandNameList='+encodeURI(brandNameList));
});
//销售额销售量排列
function upDown(obj){
    var This=$(obj);
    var index=parseInt(This.index('.up-down'));
    if(index==0 || index==1){  //销售额
        if(index==0){
            tableJson={
                saleType:1,
                brandName:tableJson.brandName,
                dateType:tableJson.dateType,
                startTime:tableJson.startTime,
                endTime:tableJson.endTime,
                pageNumber:1,
                seqType:2,  //重置为升序
                channelList:tableJson.channelList
            };
            This.css('border-bottom','6px solid #EF6666').next().css('border-top','6px solid #fff');
        }else{
            tableJson={
                saleType:1,
                brandName:tableJson.brandName,
                dateType:tableJson.dateType,
                startTime:tableJson.startTime,
                endTime:tableJson.endTime,
                pageNumber:1,
                seqType:1,  //重置为降序
                channelList:tableJson.channelList
            };
            This.css('border-top','6px solid #EF6666').prev().css('border-bottom','6px solid #fff');
        }
    }else{  //销售量
        if(index==2){
            tableJson={
                saleType:2,
                brandName:tableJson.brandName,
                dataType:tableJson.dateType,
                startTime:tableJson.startTime,
                endTime:tableJson.endTime,
                pageNumber:1,
                seqType:2,  //重置为升n序
                channelList:tableJson.channelList
            };
            This.css('border-bottom','6px solid #EF6666').next().css('border-top','6px solid #fff');
        }else{
            tableJson={
                saleType:2,
                brandName:tableJson.brandName,
                dataType:tableJson.dateType,
                startTime:tableJson.startTime,
                endTime:tableJson.endTime,
                pageNumber:1,
                seqType:1,  //重置为降序
                channelList:tableJson.channelList
            };
            This.css('border-top','6px solid #EF6666').prev().css('border-bottom','6px solid #fff');
        }
    }
    $.ajax({
        url:AJAXURL.getGoodsbrandSaleTotal,
        type:'post',
        data:tableJson,
        dataType:'json',
        success:function(response){
            createTable(response.data);
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
    _iframe.src=AJAXURL.exportDataExcel3+'?dateType='+tableJson.dateType+'&startTime='+tableJson.startTime+'&endTime='+tableJson.endTime+'&seqType='+tableJson.seqType+'&brandName='+tableJson.brandName+'&saleType='+tableJson.saleType+'&channel_num='+len+str;
}