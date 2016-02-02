/**
 *初始化
 */
$(function(){
    //日历
    var _time=new Date();
    var _start=_time.getTime()-365*24*3600*1000;
    var _end=_time.getTime()-24*3600*1000;
    $('.form_date').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        startDate: new Date(_start),
        endDate:new Date(_end)
    });
});
//自动补全
$("#search-input").autocomplete({
    delay: 400,
    source: function(request, response) {
        $.ajax({
            url: SLSCHTTP+"/storeanalysis/searchShopName",
            type:'post',
            data: {
                shopName: request.term
            },
            dataType: "json",
            success: function(data) {
                var i,_itemLen=data.data.length,itemList=[];
                for(i=0;i<_itemLen;i++){
                    itemList.push(data.data[i]['shopsname']);
                }
                response($.map(itemList, function(item) {
                    return {
                        label: item
                    }
                }));
            }
        });
    },
    minLength: 1,
    open: function() {  //在下拉框被显示的时候触发
        $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
    },
    close: function() {  //在下拉框被隐藏的时候触发
        $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
    }
});
/**
 *点击改变颜色
 */
function mingXiChange(obj){
    $(obj).addClass('chosen-active').siblings().removeClass('chosen-active');
    var _num=$(obj).siblings().length;
    if(parseInt(_num)==2){  //点击的是上面
        var _index=$(obj).index();
        if(parseInt(_index)==0){
            $('#search-content').html('' +
                    '<option value="1">销量最好TOP10的商品</option>' +
                    '<option value="0">销量最差TOP10的商品</option>'
            );
            $('#dingDanSearch1').css({display:'block'});
            $('#dingDanSearch2').css({display:'none'});
        }else if(parseInt(_index)==1){
            $('#search-content').html('' +
                    '<option value="1">高流量高销售</option>' +
                    '<option value="2">高流量低销售</option>' +
                    '<option value="3">低流量高销售</option>' +
                    '<option value="4">低流量低销售</option>'
            );
            $('#dingDanSearch1').css({display:'block'});
            $('#dingDanSearch2').css({display:'none'});
        }else{
            $('#dingDanSearch1').css({display:'none'});
            $('#dingDanSearch2').css({display:'block'});
        }
    }
    if(parseInt(_num)==4){
        $('#start-time').val('');
        $('#end-time').val('');
    }
}
/**
 *效果和购物车分析
 */
function otherAnaly(){
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
        _index=$('.top-list').find('.chosen-active').index(),
        _name= $.trim($('#search-input').val()),
        _selectType=$('#search-content').val(),
        _dateType,
        _json;
        if(_name==''){
            alert('请输入店铺名');
            return false;
        }
    if(date1!='' && date2!=''){  //选择的是自定义时间
        var year1 = date1.substr(0, 4);
        var year2 = date2.substr(0, 4);
        var month1 = date1.substr(5, 2);
        var month2 = date2.substr(5, 2);
        var day1 = date1.substr(8, 2);
        var day2 = date2.substr(8, 2);
        var temp1 = year1 + "/" + month1 + "/" + day1;
        var temp2 = year2 + "/" + month2 + "/" + day2;
        var dateaa = new Date(temp1);
        var datebb = new Date(temp2);
        var date = datebb.getTime() - dateaa.getTime();
        var time = Math.floor(date / (1000 * 60 * 60 * 24));
        if(time<0){
            alert('请选择正确的时间段');
            return false;
        }else{
            $('.time-list').find('li').removeClass('chosen-active');
            var _startTime=date1.split('-').join('');
            var _endTime=date2.split('-').join('');
            _json={
                shopName:_name,
                selectType:_selectType,
                paramType:parseInt(_index)+1,
                dateType:0,
                startTime:_startTime,
                endTime:_endTime
            };
        }
    }else{  //快速选择时间
        if(date1!='' || date2!=''){  //自定义时间只选择了一边
            alert('请选择正确的时间段');
            return false;
        }else{
            _dateType=$('.time-list').find('.chosen-active').index();
            switch (parseInt(_dateType)){
                case 0:_dateType=1;break;
                case 1:_dateType=3;break;
                case 2:_dateType=6;break;
                case 3:_dateType=4;break;
                case 4:_dateType=5;break;
            }
            _json={
                shopName:_name,
                paramType:parseInt(_index)+1,
                selectType:_selectType,
                dateType:_dateType
            };
        }
    }
    $.ajax({
        url:SLSCHTTP+'/storeanalysis/detailAnalysis',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            var _len,_this;
            if(parseInt(_index)==0){  //展示效果
                if(response.state==false){
                    $('.no-data').css({display:'block'});
                    $('#xiao-guo').css({display:'none'});
                    $('#gou-wu').css({display:'none'});
                    $('#ding-dan').css({display:'none'});
                }else{
                    $('.no-data').css({display:'none'});
                    var _i;
                    _len=response.data.length;
                    _this=$('#xiao-guo');
                    _this.find('.left-circle').text('');
                    _this.find('.goods-word').text('');
                    _this.find('.right-circle').text('');
                    _this.find('.goods-word2').text('');
                    for(_i=0;_i<_len;_i++){
                        if(_i<5){
                            _this.find('.left-circle').eq(_i).text(response.data[_i].percent+'%');
                            _this.find('.goods-word').eq(_i).text(response.data[_i].goodsname+'('+response.data[_i].address+')');
                        }else{
                            _this.find('.right-circle').eq(_i-5).text(response.data[_i].percent+'%');
                            _this.find('.goods-word2').eq(_i-5).text(response.data[_i].goodsname+'('+response.data[_i].address+')');
                        }
                    }
                    _this.css({display:'block'});
                    $('#gou-wu').css({display:'none'});
                    $('#ding-dan').css({display:'none'});
                }
            }else{  //展示购物车
                if(response.state==false){
                    $('.no-data').css({display:'block'});
                    $('#xiao-guo').css({display:'none'});
                    $('#gou-wu').css({display:'none'});
                    $('#ding-dan').css({display:'none'});
                }else{
                    $('.no-data').css({display:'none'});
                    var _j,_data=[],_attr;
                    _this=$('#gou-wu');
                    for(_attr in response.data){
                        _data.push(response.data[_attr]);
                    }
                    _len=_data.length;
                    for(_j=0;_j<_len;_j++){
                        if(_j<3){
                            _this.find('.left-circle').eq(_j).text(_data[_j]+'%');
                        }else{
                            _this.find('.right-circle').eq(_j-3).text(_data[_j]+'%');
                        }
                    }
                    _this.css({display:'block'});
                    $('#xiao-guo').css({display:'none'});
                    $('#ding-dan').css({display:'none'});
                }
            }
        }
    });
}
/**
 *订单分析
 */
function carAnaly(){
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
        _index=2,
        _name= $.trim($('#search-input').val()),
        _dateType,
        _json;
        if(_name==''){
            alert('请输入店铺名');
            return false;
        }
    if(date1!='' && date2!=''){  //选择的是自定义时间
        var year1 = date1.substr(0, 4);
        var year2 = date2.substr(0, 4);
        var month1 = date1.substr(5, 2);
        var month2 = date2.substr(5, 2);
        var day1 = date1.substr(8, 2);
        var day2 = date2.substr(8, 2);
        var temp1 = year1 + "/" + month1 + "/" + day1;
        var temp2 = year2 + "/" + month2 + "/" + day2;
        var dateaa = new Date(temp1);
        var datebb = new Date(temp2);
        var date = datebb.getTime() - dateaa.getTime();
        var time = Math.floor(date / (1000 * 60 * 60 * 24));
        if(time<0){
            alert('请选择正确的时间段');
            return false;
        }else{
            $('.time-list').find('li').removeClass('chosen-active');
            var _startTime=date1.split('-').join('');
            var _endTime=date2.split('-').join('');
            _json={
                shopName:_name,
                paramType:parseInt(_index)+1,
                selectType:-1,
                dateType:0,
                startTime:_startTime,
                endTime:_endTime
            };
        }
    }else {  //快速选择时间
        if(date1!='' || date2!=''){  //自定义时间只选择了一边
            alert('请选择正确的时间段');
            return false;
        }else{
            _dateType=$('.time-list').find('.chosen-active').index();
            switch (parseInt(_dateType)){
                case 0:_dateType=1;break;
                case 1:_dateType=3;break;
                case 2:_dateType=6;break;
                case 3:_dateType=4;break;
                case 4:_dateType=5;break;
            }
            _json={
                shopName:_name,
                paramType:parseInt(_index)+1,
                selectType:-1,
                dateType:_dateType
            };
        }
    }
    $.ajax({
        url:SLSCHTTP+'/storeanalysis/detailAnalysis',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            var _this=$('#ding-dan');
            _this.find('.left-circle').text('');
            _this.find('.right-circle').text('');
            if(response.state==false){
                $('.no-data').css({display:'block'});
                $('#xiao-guo').css({display:'none'});
                $('#gou-wu').css({display:'none'});
                $('#ding-dan').css({display:'none'});
            }else{
                $('.no-data').css({display:'none'});
                var _j,_attr,_data=[];
                for(_attr in response.data[0]){
                    _data.push(response.data[0][_attr]);
                }
                _this.find('.left-circle').eq(0).text(response.data[0].percent1+'%');
                _this.find('.left-circle').eq(1).text(response.data[0].percent2+'%');
                _this.find('.right-circle').eq(0).text(response.data[0].percent3+'%');
                _this.find('.right-circle').eq(1).text(response.data[0].percent4+'%');
                _this.find('.right-circle').eq(2).text(response.data[0].percent5+'%');
                _this.css({display:'block'});
                $('#xiao-guo').css({display:'none'});
                $('#gou-wu').css({display:'none'});
            }
        }
    });
}
/**
 *回车事件
 */
$('#search-input').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        var index=parseInt($('.top-list').find('.chosen-active').index());
        if(index==2){
            carAnaly();
        }else{
            otherAnaly();
        }
    }
});