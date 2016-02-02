var shopTableJson,countPageNumber=0;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('品牌对比');
    $.when(
        $.ajax({  //TOP5
            url:AJAXURL.getStoreTop52,
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({  //表格
            url:AJAXURL.getGoodsbrandByName,
            type:'post',
            data:{},
            dataType:'json'
        })
    ).done(function(response1,response2){
//  TOP5
            var data1=response1[0].data,timeStr;
            for(var i1=0;i1<data1.length;i1++){
                timeStr=data1[i1].time.split('至');
                $('.time-box').eq(i1).text(timeStr[0].split(' ')[0]+'至'+timeStr[1].split(' ')[0]);
                $('.good-name').eq(i1).text(data1[i1].goodsbrand);
            }
//  表格
            createTable(response2[0].data);
            $("#pageBox1").pagination(response2[0].data[0].total_page, {
                num_edge_entries: 1,
                num_display_entries: 10,
                callback: pageselectCallback
            });
//  存入所有变量，该变量用于分页ajax传值
            shopTableJson={
                dateType:1,
                startTime:'',
                endTime:'',
                brandName:'',
                pageNumber:1
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
    if(data.length==0 || data.length==undefined || data.length==null){
        $('#shopTable').find('tbody').html('<tr><td colspan="4">暂无数据</td></tr>');
        $("#pageBox1").pagination(1, {
            num_edge_entries: 1,
            num_display_entries: 10
        });
    }else{
        var str='';
        for(var i=0;i<data.length;i++){
            str+='<tr>' +
                '<td><span class="table-name" title="'+data[i].goodsbrand+'">'+data[i].goodsbrand+'</span></td>' +
                '<td>'+data[i].goods_mod_comment1+'</td>' +
                '<td>'+data[i].goods_mod_comment2+'</td>' +
                '<td>'+data[i].goods_mod_comment3+'</td>' +
                '<td style="width: 250px;">' +
                '<div class="compare-btn" onclick="addCompare(this);">加入对比</div>' +
                '</td>' +
                '</tr>';
        }
        $('#shopTable').find('tbody').html(str);
    }
}
//分页
function pageselectCallback(page_index){
    if(countPageNumber==0){
        countPageNumber=1;
        return false;
    }
    shopTableJson.pageNumber=parseInt(page_index)+1;
    $.ajax({
        url:AJAXURL.getGoodsbrandByName,
        type:'post',
        data:shopTableJson,
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
    var id=_this.parent().siblings().eq(0).text();
    var goodId=$('.compare-box').find('.good-box');
    for(var i=0;i<goodId.length;i++){
        if(goodId.eq(i).text()==id){
            alert('该产品已加入对比');
            return false;
        }
    }
    _this.addClass('btn-blue');
    str+='<li>' +
        '<span class="good-box">'+_this.parent().siblings().eq(0).text()+'</span>' +
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
    var idLists=$('#shopTable').find('.table-name');
    idLists.each(function(){
        if($(this).text()==_text){
            $(this).parent().parent().find('.compare-btn').removeClass('btn-blue');  //将所删除的对比在表格中对应的那一条数据的按钮样式移除
        }
    });
}
//设置按钮选中
function btnBlue(){
    var idList=$('.compare-box').find('.good-box');
    var tdList=$('#shopTable').find('.table-name');
    for(var i=0;i<idList.length;i++){
        for(var j=0;j<tdList.length;j++){
            if(idList.eq(i).text() == tdList.eq(j).text()){
                tdList.eq(j).parent().parent().find('.compare-btn').addClass('btn-blue');
            }
        }
    }
}
//表格点击事件
$('#btn1').on('click',function(){
    var json;
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    var brandName= $.trim($('#shopName').val());
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }
//    if(brandName==''){
//        $('#storeName span').text('全部');
//    }else{
//        $('#storeName span').text(brandName);
//    }
    json={
        brandName:brandName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1
    };
//  点确定更新所有变量
    shopTableJson={
        brandName:brandName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1
    };
    $.ajax({  //表格
        url:AJAXURL.getGoodsbrandByName,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            createTable(response.data);
            $("#pageBox1").pagination(response.data[0].total_page, {
                num_edge_entries: 1,
                num_display_entries: 10,
                callback: pageselectCallback
            });
        }
    })
});
//开始对比
$('.compare-btn-go').on('click',function(){
    var brandNameList=[];
    var compareBox=$('.compare-box');
    var shopBox=compareBox.find('.good-box');
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