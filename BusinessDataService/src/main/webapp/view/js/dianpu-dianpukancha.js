var shopJson={
    dateType:1,
    startTime:'',
    endTime:'',
    saleType:1,
    storeName:'',
    pageNumber:1,
    seqType:1  //默认降序  1.降序;2升序
},
    countPageNumber= 0,
    chart2;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('店铺勘察');
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
        $.ajax({  //表格
            url:AJAXURL.getStoreSaleTotal,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                pageNumber:1
            },
            dataType:'json'
        }),
        $.ajax({  //图1
            url:AJAXURL.getStoreTopBychannel,
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
            var strTd='',strTr='',strAll='',strOptins='',strLi='';
            var channelList=[];
            for(var i=0;i<data2.length;i++){
                strLi+='<li><span>'+data2[i].channel+'</span><img src="../images/del-cross.png" class="del-cross" onclick="delChannel(this);"/></li>';
                channelList.push(data2[i].channel);
                strOptins+='<option value="'+data2[i].channel+'">'+data2[i].channel+'</option>';
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
            $('#channelList').append(strOptins);
            $('.channel-row>ul').append(strLi);
//  表格
//            var danwei='元',titleName='销售额';
            createTable(response2[0].data);
            $('.triangle-box').eq(0).css('visibility','visible');
            $("#pageBox1").pagination(response2[0].data[0].total_page, {
                num_edge_entries: 1,
                num_display_entries: 10,
                callback: pageselectCallback
            });
//  存入所有变量，该变量用于分页ajax传值
            shopJson={
                dateType:1,
                startTime:'',
                endTime:'',
                saleType:1,
                storeName:'',
                pageNumber:1,
                seqType:1,
                channelList:channelList
            };
//  图1
            var data5=response3[0].data;
            var chartFiveX=[],chartFiveD=[],danwei5,fomatter3,shopName;
            danwei5='万元';
            fomatter3='销售额';
            shopName='全部';
            for(var i5=0;i5<data5.length;i5++){
                chartFiveX.push(data5[i5].store_name);
                chartFiveD.push({
                    value:data5[i5].total,
                    name:data5[i5].store_id
                });
            }
            chartOne(chartFiveX,chartFiveD,danwei5,time,fomatter3,shopName);
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
        $('#goodTable').find('tbody').html('<tr><td colspan="7">暂无数据</td></tr>');
        $("#pageBox1").pagination(1, {
            num_edge_entries: 1,
            num_display_entries: 10
        });
    }else{
        var str='';
        for(var i=0;i<data.length;i++){  //10条一页
            str+='<tr>' +
                '<td>'+((parseInt(shopJson.pageNumber)-1)*10+i+1)+'</td>' +
                '<td><span class="table-name" onclick="goNextPage(this);" title="'+data[i].store_name+'">'+data[i].store_name+'</span><a href="javascript:void(0);" class="link-box" onclick="openWindow(this);" title="'+data[i].store_url+'"></a></td>' +
                '<td class="price-total1 table-active">'+data[i].total_sale.toLocaleString()+'元</td>' +
                '<td class="price-total2">'+data[i].total_num.toLocaleString()+'件</td>' +
                '<td><span>'+data[i].channel+'</span></td>' +
                '<td><div class="table-pic-box"><span class="pic-prev">'+data[i].goods_sales_flag+'</span>'+'<img src="../images/'+data[i].icon1+'.png"/></div></td>' +
                '<td>' +
                '<span class="good-id">'+data[i].store_id+'</span>'+
                '<div class="compare-btn" onclick="addCompare(this);">加入对比</div>' +
                '</td>' +
                '</tr>';
        }
        $('#goodTable').find('tbody').html(str);
        if(parseInt(shopJson.saleType)==1){
            $('.price-total1').addClass('table-active');
            $('.price-total2').removeClass('table-active');
        }else{
            $('.price-total2').addClass('table-active');
            $('.price-total1').removeClass('table-active');
        }
    }
}
//分页
function pageselectCallback(page_index){
    if(countPageNumber==0){
        countPageNumber=1;
        return false;
    }
    shopJson.pageNumber=parseInt(page_index)+1;
    $.ajax({
        url:AJAXURL.getStoreSaleTotal,
        type:'post',
        data:shopJson,
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
    if(parseInt(num)>5){
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
    var json;
    var channelList=[];
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    var storeName= $.trim($('#shopName').val());
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
        storeName:storeName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        seqType:1,
        channelList:channelList
    };
//  点确定更新所有变量
    shopJson={
        saleType:parseInt(saleType)+1,
        storeName:storeName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        seqType:1,  //重置为降序
        channelList:channelList
    };
    $.ajax({  //表格
        url:AJAXURL.getStoreSaleTotal,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            if(shopJson.saleType==1){
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
function chartOne(chartFiveX,chartFiveD,danwei,time,fomatter,shopName){
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
        tooltip : {
            trigger: 'item',
            axisPointer : {
                type:'shadow'
            },
            formatter:'{b}'+'<br/>'+'{c}'+danwei
        },
        title : {
            //text: shopName+'店铺'+fomatter+'排行',
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
                data : chartFiveX,
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
                name : '单位：'+danwei,
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
                data:chartFiveD
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
        var shopName=encodeURI(params.name);
        var shopId=params.data.name;
        $(window.parent.document.getElementById('iframepage')).attr('src','dianpu-dianpuxiangqing.html?shopName='+shopName+'&shopId='+shopId);
    });
}
//图1点击事件
$('#btn2').on('click',function(){
    var json;
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var channel=$('#channelList').val();
    var startTime=$('#myCalender3').val();
    var endTime=$('#myCalender4').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }else{
        json={
            saleType:parseInt(saleType)+1,
            dateType:0,
            channel:channel,
            startTime:startTime,
            endTime:endTime
        };
    }
    $.ajax({  //图1
        url:AJAXURL.getStoreTopBychannel,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var data5=response.data;
            var chartFiveX=[],chartFiveD=[],time,danwei5,fomatter3,shopName;
            if(parseInt(saleType)==0){
                danwei5='万元';
                fomatter3='销售额';
            }else{
                danwei5='万件';
                fomatter3='销售量';
            }
            time=startTime+'至'+endTime;
            shopName=channel;
            for(var i5=0;i5<data5.length;i5++){
                chartFiveX.push(data5[i5].store_name);
                chartFiveD.push({
                    value:data5[i5].total,
                    name:data5[i5].store_id
                });
            }
            chartOne(chartFiveX,chartFiveD,danwei5,time,fomatter3,shopName);
        }
    })
});
//查看具体店铺
function goNextPage(obj){
    var shopName=encodeURI($(obj).text());
    var shopId=$(obj).parent().parent().find('.good-id').text();
    var _iframe=$(window.parent.document.getElementById('iframepage'));
    _iframe.attr('src','dianpu-dianpuxiangqing.html?shopName='+shopName+'&shopId='+shopId);
}
//开始对比
$('.compare-btn-go').on('click',function(){
    var storeNameList=[],storeIdList=[];
    var compareBox=$('.compare-box');
    var shopBox=compareBox.find('.good-box');
    var idBox=compareBox.find('.good-id');
    var _iframe=$(window.parent.document.getElementById('iframepage'));
    for(var i=0;i<shopBox.length;i++){
        storeNameList.push(shopBox.eq(i).text());
        storeIdList.push(idBox.eq(i).text());
    }
    if(storeNameList.length<2){
        alert('至少选择两项对比');
        return false;
    }
    _iframe.attr('src','dianpu-dianpuduibi-child.html?storeNameList='+encodeURI(storeNameList)+'&storeIdList='+encodeURI(storeIdList));
});
//销售额销售量排列
function upDown(obj){
    var This=$(obj);
    var index=parseInt(This.index('.up-down'));
    if(index==0 || index==1){  //销售额
        if(index==0){
            shopJson={
                saleType:1,
                storeName:shopJson.storeName,
                dateType:shopJson.dateType,
                startTime:shopJson.startTime,
                endTime:shopJson.endTime,
                pageNumber:1,
                seqType:2,  //重置为升序
                channelList:shopJson.channelList
            };
            This.css('border-bottom','6px solid #EF6666').next().css('border-top','6px solid #fff');
        }else{
            shopJson={
                saleType:1,
                storeName:shopJson.storeName,
                dateType:shopJson.dateType,
                startTime:shopJson.startTime,
                endTime:shopJson.endTime,
                pageNumber:1,
                seqType:1,  //重置为降序
                channelList:shopJson.channelList
            };
            This.css('border-top','6px solid #EF6666').prev().css('border-bottom','6px solid #fff');
        }
    }else{  //销售量
        if(index==2){
            shopJson={
                saleType:2,
                storeName:shopJson.storeName,
                dateType:shopJson.dateType,
                startTime:shopJson.startTime,
                endTime:shopJson.endTime,
                pageNumber:1,
                seqType:2,  //重置为升n序
                channelList:shopJson.channelList
            };
            This.css('border-bottom','6px solid #EF6666').next().css('border-top','6px solid #fff');
        }else{
            shopJson={
                saleType:2,
                storeName:shopJson.storeName,
                dateType:shopJson.dateType,
                startTime:shopJson.startTime,
                endTime:shopJson.endTime,
                pageNumber:1,
                seqType:1,  //重置为降序
                channelList:shopJson.channelList
            };
            This.css('border-top','6px solid #EF6666').prev().css('border-bottom','6px solid #fff');
        }
    }
    $.ajax({  //表格
        url:AJAXURL.getStoreSaleTotal,
        type:'post',
        data:shopJson,
        dataType:'json',
        success:function(response){
            createTable(response.data);
            $("#pageBox1").pagination(response.data[0].total_page, {
                num_edge_entries: 1,
                num_display_entries: 10,
                callback: pageselectCallback
            });
        }
    });
}
//导出
function downLoad(){
    var _iframe=$('.downLoad-box')[0];
    var len=shopJson.channelList.length;
    var str='';
    for(var i=0;i<len;i++){
        str+='&channelList['+i+']='+shopJson.channelList[i];
    }
    _iframe.src=AJAXURL.exportDataExcel2+'?dateType='+shopJson.dateType+'&startTime='+shopJson.startTime+'&endTime='+shopJson.endTime+'&seqType='+shopJson.seqType+'&store_name='+shopJson.storeName+'&saleType='+shopJson.saleType+'&channel_num='+len+str;
}