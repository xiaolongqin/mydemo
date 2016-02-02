var tableJson={
        dateType:1,
        startTime:'',
        endTime:'',
        goodName:'',
        saleType:1,
        pageNumber:1,
        seqType:1  //默认降序  1.降序;2升序
    },
    countPageNumber= 0;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('商品扫描');
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
            url:AJAXURL.getGoodsScan,
            type:'post',
            data:{
                dateType:1,
                saleType:1,
                pageNumber:1
            },
            dataType:'json'
        })
    ).done(function(response1,response2){
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
                goodName:'',
                pageNumber:1,
                seqType:1,
                channelList:channelList
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
                '<td>'+((parseInt(tableJson.pageNumber)-1)*10+i+1)+'</td>' +
                '<td><span class="table-name" onclick="goNextPage(this);" title="'+data[i].goodsname+'-'+data[i].goodsid+'">'+data[i].goodsname+'</span><span class="brand-name">'+data[i].goodsbrand+'</span><span class="table-price">'+data[i].price+'元</span><a href="javascript:void(0);" class="link-box" onclick="openWindow(this);" title="'+data[i].goods_url+'"></a></td>' +
                '<td class="price-total1 table-active">'+data[i].total_sale.toLocaleString()+'元</td>' +
                '<td class="price-total2">'+data[i].total_num.toLocaleString()+'件</td>' +
                '<td><span class="channel-name">'+data[i].channel+' </span></td>' +
                '<td><div class="table-pic-box"><span class="pic-prev">'+data[i].goods_sales_flag+'</span>'+'<img src="../images/'+data[i].icon1+'.png"/></div></td>' +
                '<td>' +
                '<span class="good-id">'+data[i].goodsid+'</span>'+
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
        url:AJAXURL.getGoodsScan,
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
    var goodName= $.trim($('#goodName').val());
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
        goodName:goodName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        seqType:1,
        channelList:channelList
    };
//  点确定更新所有变量
    tableJson={
        saleType:parseInt(saleType)+1,
        goodName:goodName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1,
        seqType:1,  //重置为降序
        channelList:channelList
    };
    $.ajax({  //表格
        url:AJAXURL.getGoodsScan,
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
//销售额销售量排列
function upDown(obj){
    var This=$(obj);
    var index=parseInt(This.index('.up-down'));
    if(index==0 || index==1){  //销售额
        if(index==0){
            tableJson={
                saleType:1,
                goodName:tableJson.goodName,
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
                goodName:tableJson.goodName,
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
                goodName:tableJson.goodName,
                dateType:tableJson.dateType,
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
                goodName:tableJson.goodName,
                dateType:tableJson.dateType,
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
        url:AJAXURL.getGoodsScan,
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
    _iframe.src=AJAXURL.exportDataExcel4+'?dateType='+tableJson.dateType+'&startTime='+tableJson.startTime+'&endTime='+tableJson.endTime+'&seqType='+tableJson.seqType+'&goodName='+tableJson.goodName+'&saleType='+tableJson.saleType+'&channel_num='+len+str;
}