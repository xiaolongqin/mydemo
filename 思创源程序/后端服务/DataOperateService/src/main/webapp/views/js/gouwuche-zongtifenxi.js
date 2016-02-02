/*初始化*/
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
    $.ajax({
        url:SLSCHTTP+'/shoppingcart/resultRanking',
        type:'post',
        data:{goodsType:1,dateType:1},  //正式上线改为dateType:1
        dataType:'json',
        success:function(response){
            if(response.state==false){
                $('.no-data').css({display:'block'});
                $('#xiao-guo').css({display:'none'});
            }else{
                var _i,_len=response.data.length,_this=$('#xiao-guo');
                for(_i=0;_i<_len;_i++){
                    if(_i<5){
                        _this.find('.left-circle').eq(_i).text(response.data[_i].percent+'%');
                        _this.find('.goods-word').eq(_i).text(response.data[_i].goodsname+' ('+response.data[_i].address+')');
                    }else{
                        _this.find('.right-circle').eq(_i-5).text(response.data[_i].percent+'%');
                        _this.find('.goods-word2').eq(_i-5).text(response.data[_i].goodsname+' ('+response.data[_i].address+')');
                    }
                }
                _this.css({display:'block'});
            }
        }
    });
});
/**
 *选择改变字体颜色
 */
function changeColor(obj){
    $(obj).addClass('chosen-active').siblings().removeClass('chosen-active');
    var _num=$(obj).siblings().length,_index;
    if(parseInt(_num)==1){  //选择的是效果还是特征
        _index=$(obj).index();
        if(parseInt(_index)==0){  //选择的是效果，重置option
            $('#search-content').html(
                    '<option value="1">销量TOP10最好的商品</option>' +
                    '<option value="0">销量TOP10最差的商品</option>'
            );
        }else{  //选择的是特征，重置option
            $('#search-content').html(
                '<option value="1">高流量高销售</option>' +
                '<option value="2">高流量低销售</option>' +
                '<option value="3">低流量高销售</option>' +
                '<option value="4">低流量低销售</option>'
            );
        }
    }
    if(parseInt(_num)==4){  //点击的是快速选择时间，日历重置为空
        $('#start-time').val('');
        $('#end-time').val('');
    }
}
/**
 *选择好后点击确定
 */
function seeCart(){
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
        _index=$('.top-list').find('.chosen-active').index(),  //选择的是效果还是特征，以便控制应该展示的图片
        _url,
        _goodsType=$('#search-content').val(),
        _dateType,
        _json;
    if(parseInt(_index)==0){  //效果接口
        _url='/shoppingcart/resultRanking';
    }else{  //特征接口
        _url='/shoppingcart/featureRanking';
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
                goodsType:_goodsType,
                dateType:0,
                startTime:_startTime,
                endTime:_endTime
            };
        }
    }else{
        if(date1!='' || date2!=''){  //自定义时间只选择了一边
            alert('请选择正确的时间段');
            return false;
        }else{  //快速选择时间
            _dateType=$('.time-list').find('.chosen-active').index();
            switch (parseInt(_dateType)){
                case 0:_dateType=1;break;
                case 1:_dateType=3;break;
                case 2:_dateType=6;break;
                case 3:_dateType=4;break;
                case 4:_dateType=5;break;
            }
            _json={
                goodsType:_goodsType,
                dateType:_dateType
            };
        }
    }
    $.ajax({
        url:SLSCHTTP+_url,
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            var _len,_this;
            if(parseInt(_index)==0){  //效果展示
                if(response.state==false){
                    $('.no-data').css({display:'block'});
                    $('#xiao-guo').css({display:'none'});
                    $('.pic-box-top').css({display:'block'});
                    $('.pic-box-high').css({display:'none'});
                }else{
                    $('.no-data').css({display:'none'});
                    var _i;
                    _len=response.data.length;
                    _this=$('#xiao-guo');
                    for(_i=0;_i<_len;_i++){
                        if(_i<5){
                            _this.find('.left-circle').eq(_i).text(response.data[_i].percent+'%');
                            _this.find('.goods-word').eq(_i).text(response.data[_i].goodsname+' ('+response.data[_i].address+')');
                        }else{
                            _this.find('.right-circle').eq(_i-5).text(response.data[_i].percent+'%');
                            _this.find('.goods-word2').eq(_i-5).text(response.data[_i].goodsname+' ('+response.data[_i].address+')');
                        }
                    }
                    _this.css({display:'block'});
                    $('.pic-box-top').css({display:'block'});
                    $('.pic-box-high').css({display:'none'});
                }
            }else{  //特征展示
                var _j,_data=[],_attr;
                _this=$('#te-zheng');
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
                $('.pic-box-top').css({display:'none'});
                $('.pic-box-high').css({display:'block'});
            }
        }
    });
}