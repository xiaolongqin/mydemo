var tableJson={
        dateType:1,
        startTime:'',
        endTime:'',
        brandName:'全部',
        pageNumber:1,
        saleType:1
    },
    countPageNumber= 0;
$(function(){
    $(window.parent.document.getElementById('contentTitle')).text('商品飙升排行');
    $.when(
        $.ajax({  //  获取下拉框
            url:AJAXURL.getGoodsBrand,
            type:'post',
            data:{},
            dataType:'json'
        }),
        $.ajax({  //  表格
            url:AJAXURL.getGoodsTop,
            type:'post',
            data:{
                saleType:1,
                dateType:1,
                pageNumber:1,
                brandName:'全部'
            },
            dataType:'json'
        })
    ).done(function(response1,response2){
//  获取下拉框
            var data1=response1[0].data;
            var str1='';
            for(var i1=0;i1<data1.length;i1++){
                str1+='<option value="'+data1[i1].goodsbrand+'">'+data1[i1].goodsbrand+'</option>';
            }
            $('#goodList').append(str1);
//  获取表格
            createTable(response2[0].data);
            $("#pageBox1").pagination(response2[0].data[0].total_page, {
                num_edge_entries: 1,
                callback: pageselectCallback
            });

//  存入所有变量，该变量用于分页ajax传值
            tableJson={
                dateType:1,
                startTime:'',
                endTime:'',
                saleType:1,
                pageNumber:1,
                brandName:'全部'
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
            num_edge_entries: 1
        });
    }else{
        if(parseInt(tableJson.saleType)==1){
            danwei='元';
            titleName='销售额';
        }else{
            danwei='件';
            titleName='销售量';
        }
        var str='';
        for(var i=0;i<data.length;i++){  //10条一页
            str+='<tr>' +
                '<td>'+((parseInt(tableJson.pageNumber)-1)*10+i+1)+'</td>' +
                '<td><span class="table-name" onclick="goNextPage(this);" title="'+data[i].goodsname+'">'+data[i].goodsname+'</span><span class="channel-name">'+data[i].channel+'</span><span class="brand-name">'+data[i].goodsbrand+'</span></td>' +
                '<td>'+data[i].total.toLocaleString()+danwei+'<span class="good-id">'+data[i].goodsid+'</span></td>' +
                '<td style="color: #EF6666;font-weight: 600;">'+data[i].price+'元</td>' +
                '<td><div class="table-pic-box"><span class="pic-prev">'+data[i].goods_sales_flag+'</span>'+'<img src="../images/'+data[i].icon1+'.png"/></div></td>' +
                '<td>'+data[i].compete_index.toFixed(3)+'</td>'+
                '</tr>';
        }
        $('#goodTable').find('tbody').html(str);
        $('#titleName').text(titleName);
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
        url:AJAXURL.getGoodsTop,
        type:'post',
        data:tableJson,
        dataType:'json',
        success:function(response){
            createTable(response.data);
        }
    });
}
//表格点击事件
$('#btn1').on('click',function(){
    var json;
    var saleType=$(this).prev().find('.change-list-box').find('.pn-active').index();
    var startTime=$('#myCalender1').val();
    var endTime=$('#myCalender2').val();
    var brandName= $('#goodList').val();
    if(startTime=='' || endTime==''){
        alert('请选择日历时间');
        return false;
    }
    json={
        saleType:parseInt(saleType)+1,
        brandName:brandName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1
    };
//  点确定更新所有变量
    tableJson={
        saleType:parseInt(saleType)+1,
        brandName:brandName,
        dateType:0,
        startTime:startTime,
        endTime:endTime,
        pageNumber:1
    };
    $.ajax({  //  表格
        url:AJAXURL.getGoodsTop,
        type:'post',
        data:json,
        dataType:'json',
        success:function(response){
            var time;
            time=startTime+'至'+endTime;
            $('.time-box').text(time);
            createTable(response.data);
            $("#pageBox1").pagination(response.data[0].total_page, {
                num_edge_entries: 1,
                callback: pageselectCallback
            });
        }
    });
});
//进入商品详情
function goNextPage(obj){
    var This=$(obj);
    var goodName=encodeURI(This.text());
    var goodsId=This.parent().parent().find('.good-id').text();
    var channel=encodeURI(This.next().text());
    var goodsBrand=encodeURI(This.next().next().text());
    var _iframe=$(window.parent.document.getElementById('iframepage'));
    _iframe.attr('src','shangpin-shangpinxiangqing.html?goodName='+goodName+'&goodsId='+goodsId+'&channel='+channel+'&goodsBrand='+goodsBrand);
}