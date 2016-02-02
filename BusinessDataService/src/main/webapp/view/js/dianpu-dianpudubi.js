var shopTableJson,countPageNumber=0;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('店铺对比');
    $.when(
        $.ajax({  //TOP5
            url:AJAXURL.getStoreTop5,
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({  //表格
            url:AJAXURL.getStroreByName,
            type:'post',
            data:{
                storeName:'',
                dateType:1,
                pageNumber:1
            },
            dataType:'json'
        })
    ).done(function(response1,response2){
//  TOP5
            var data1=response1[0].data,timeStr;
            for(var i1=0;i1<data1.length;i1++){
                timeStr=data1[i1].time.split('至');
                $('.time-box').eq(i1).text(timeStr[0].split(' ')[0]+'至'+timeStr[1].split(' ')[0]);
                $('.good-name').eq(i1).text(data1[i1].store_name);
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
                storeName:'',
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
        var str='',score;
        for(var i=0;i<data.length;i++){
            if(parseFloat(data[i].store_industry_score)>0){
                score='高于';
            }else if(parseFloat(data[i].store_industry_score)==0){
                score='持平';
            }else{
                score='低于';
            }
            str+='<tr>' +
                '<td><span class="table-name" title="'+data[i].store_name+'">'+data[i].store_name+'</span></td>' +
                '<td>'+data[i].store_score+'</td>' +
                '<td><div class="td-img"><span style="float: left;">'+score+'</span><span>'+data[i].store_industry_score+'%</span><img src="../images/'+data[i].state+'.png"/></div></td>' +
                '<td>' +
                '<span class="good-id">'+data[i].store_id+'</span>' +
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
        url:AJAXURL.getStroreByName,
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
        '<span class="good-box">'+_this.parent().siblings().eq(0).text()+'</span>' +
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
    var idLists=$('#shopTable').find('.good-id');
    idLists.each(function(){
        if($(this).text()==_text){
            $(this).siblings().removeClass('btn-blue');  //将所删除的对比在表格中对应的那一条数据的按钮样式移除
        }
    });
}
//设置按钮选中
function btnBlue(){
    var idList=$('.compare-box').find('.good-id');
    var tdList=$('#shopTable').find('.good-id');
    for(var i=0;i<idList.length;i++){
        for(var j=0;j<tdList.length;j++){
            if(idList.eq(i).text() == tdList.eq(j).text()){
                tdList.eq(j).siblings().addClass('btn-blue');
            }
        }
    }
}
//表格点击事件
$('#btn1').on('click',function(){
    var json;
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    var storeName= $.trim($('#shopName').val());
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }
//    if(storeName==''){
//        $('#storeName span').text('全部');
//    }else{
//        $('#storeName span').text(storeName);
//    }
    json={
        storeName:storeName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1
    };
//  点确定更新所有变量
    shopTableJson={
        storeName:storeName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1
    };
    $.ajax({  //表格
        url:AJAXURL.getStroreByName,
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