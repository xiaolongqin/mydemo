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
    $.when(
        $.ajax({
            url:SLSCHTTP+'/storeanalysis/getGeneralAnalysis',
            type:'post',
            data:{selectType:1,dateType:1,paramType:1},  //正式对接dateType改为1
            dataType:'json'
        }),
        $.ajax({  //总数
            url:SLSCHTTP+'/monitor/getThreeTotal',
            type:'post',
            dataType:'json',
            data:{}
        })
    ).done(function(response1,response2){
            if(response1[0].state==false){
                $('.no-data').css({display:'block'});
                $('.pic-box-top').css({display:'none'});
            }else{
                var _i,_len=response1[0].data.length,_this=$('#three-box');
                for(_i=0;_i<_len;_i++){
                    if(_i<5){
                        _this.find('.left-circle').eq(_i).text(response1[0].data[_i].percent+'%');
                        _this.find('.goods-word').eq(_i).text(response1[0].data[_i].shopsname+' ('+response1[0].data[_i].address+')');
                    }else{
                        _this.find('.right-circle').eq(_i-5).text(response1[0].data[_i].percent+'%');
                        _this.find('.goods-word2').eq(_i-5).text(response1[0].data[_i].shopsname+' ('+response1[0].data[_i].address+')');
                    }
                }
//              隐藏域存时间点，为地图传时间做准备
                var _mapTime=$('.map-time');
                _mapTime.eq(0).val(1);
                _mapTime.eq(1).val('');
                _mapTime.eq(2).val('');
                _mapTime.eq(3).val(0);
                _mapTime.eq(4).val(1);
            }
//          总数
            $('.total-number').find('span').text(response2[0].data.shopCount[0].count);
    });
});
/**
 *选择改变字体颜色
 */
function xiaoShouChange(obj){
    $(obj).addClass('chosen-active').siblings().removeClass('chosen-active');
    var _num=$(obj).siblings().length,_index;
    if(parseInt(_num)==3){
        _index=$(obj).index();
        if(parseInt(_index)==2){
            $('#dingDanSearch1').css({display:'none'});
            $('#dingDanSearch2').css({display:'block'});
        }else{
            if(parseInt(_index)==3){
                $('#search-content').html('<option value="1">购买量最好TOP10的用户</option><option value="0">购买量最差TOP10的用户</option>');
            }else{
                $('#search-content').html('<option value="1">销量最好TOP10的店铺</option><option value="0">销量最差TOP10的店铺</option>');
            }
            $('#dingDanSearch1').css({display:'block'});
            $('#dingDanSearch2').css({display:'none'});
        }
    }
    if(parseInt(_num)==4){
        $('#start-time').val('');
        $('#end-time').val('');
    }
}
/**
 *其他分析
 */
function seeZongTi(){
//    $('#chongFu-diTu').css({visibility:'hidden'});
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
        _selectType=$('#search-content').val(),
        _paramType=$('.top-list').find('.chosen-active').index(),
        _dateType,
        _json;
    $('#zongTi-diTu').css({visibility:'hidden'});
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
                selectType:_selectType,
                paramType:parseInt(_paramType)+1,
                dateType:0,
                startTime:_startTime,
                endTime:_endTime
            };
        }
    }else{
        if(date1!='' || date2!='') {  //自定义时间只选择了一边
            alert('请选择正确的时间段');
            return false;
        }else {  //快速选择时间
            _dateType=$('.time-list').find('.chosen-active').index();
            switch (parseInt(_dateType)){
                case 0:_dateType=1;break;
                case 1:_dateType=3;break;
                case 2:_dateType=6;break;
                case 3:_dateType=4;break;
                case 4:_dateType=5;break;
            }
            _json={
                selectType:_selectType,
                paramType:parseInt(_paramType)+1,
                dateType:_dateType
            };
        }
    }
    $.ajax({
        url:SLSCHTTP+'/storeanalysis/getGeneralAnalysis',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            if(response.state==false){  //展示数据异常图片
                $('.no-data').css({display:'block'});
                $('.pic-box-top').css({display:'none'});
                $('.pic-box-high').css({display:'none'});
            }else{
                var _i,_len=response.data.length,_this=$('#three-box');
                var  _paramType=$('.top-list').find('.chosen-active').index();
                if(parseInt(_paramType)!=3){
                    for(_i=0;_i<_len;_i++){
                        if(_i<5){
                            _this.find('.left-circle').eq(_i).text(response.data[_i].percent+'%');
                            _this.find('.goods-word').eq(_i).text(response.data[_i].shopsname+' ('+response.data[_i].address+')');
                        }else{
                            _this.find('.right-circle').eq(_i-5).text(response.data[_i].percent+'%');
                            _this.find('.goods-word2').eq(_i-5).text(response.data[_i].shopsname+' ('+response.data[_i].address+')');
                        }
                    }
                }else{
                    for(_i=0;_i<_len;_i++){
                        if(_i<5){
                            _this.find('.left-circle').eq(_i).text(response.data[_i].percent+'%');
                            _this.find('.goods-word').eq(_i).text(response.data[_i].phone+' ('+response.data[_i].address+')');
                        }else{
                            _this.find('.right-circle').eq(_i-5).text(response.data[_i].percent+'%');
                            _this.find('.goods-word2').eq(_i-5).text(response.data[_i].phone+' ('+response.data[_i].address+')');
                        }
                    }
                }
                $('.no-data').css({display:'none'});
                $('.pic-box-top').css({display:'block'});
                $('.pic-box-high').css({display:'none'});
//              隐藏域存时间点，为地图传时间做准备
                var _mapTime=$('.map-time');
                if(date1!='' && date2!=''){
                    _mapTime.eq(0).val(0);
                }else{
                    _mapTime.eq(0).val(_dateType);
                }
                _mapTime.eq(1).val(_startTime);
                _mapTime.eq(2).val(_endTime);
                _mapTime.eq(3).val(_paramType);
                _mapTime.eq(4).val(_selectType);
            }
        }
    });
}
/**
 *订单分析(这里没地图展示，不用存时间)
 */
function seeDingDan(){
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
        _dateType,
        _json;
    $('#zongTi-diTu').css({visibility:'hidden'});
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
                selectType:-1,
                paramType:3,
                dateType:0,
                startTime:_startTime,
                endTime:_endTime
            };
        }
    }else{
        if(date1!='' || date2!='') {  //自定义时间只选择了一边
            alert('请选择正确的时间段');
            return false;
        }else {  //快速选择时间
            _dateType=$('.time-list').find('.chosen-active').index();
            switch (parseInt(_dateType)){
                case 0:_dateType=1;break;
                case 1:_dateType=3;break;
                case 2:_dateType=6;break;
                case 3:_dateType=4;break;
                case 4:_dateType=5;break;
            }
            _json={
                selectType:-1,
                paramType:3,
                dateType:_dateType
            };
        }
    }
    $.ajax({
        url:SLSCHTTP+'/storeanalysis/getGeneralAnalysis',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            if(response.state==false){  //展示数据异常图片
                $('.no-data').css({display:'block'});
                $('.pic-box-top').css({display:'none'});
                $('.pic-box-high').css({display:'none'});
            }else{
                var _attr,_data=[],_this=$('#dingDan-box');
                for(_attr in response.data[0]){
                    _data.push(response.data[0][_attr]);
                }
                _this.find('.left-circle').eq(0).text(response.data[0].percent1+'%');
                _this.find('.left-circle').eq(1).text(response.data[0].percent2+'%');
                _this.find('.right-circle').eq(0).text(response.data[0].percent3+'%');
                _this.find('.right-circle').eq(1).text(response.data[0].percent4+'%');
                _this.find('.right-circle').eq(2).text(response.data[0].percent5+'%');
                $('.no-data').css({display:'none'});
                $('.pic-box-top').css({display:'none'});
                $('.pic-box-high').css({display:'block'});
            }
        }
    });
}
/**
 *查看地图
 */
function zongTiMap(obj){
    var  _paramType=$('.top-list').find('.chosen-active').index();
    if(parseInt(_paramType)==3){
        return false;
    }
    var _name=$(obj).siblings().text().split(' (')[0],
        _box=$('#zongTiChart'),
        _mapTime=$('.map-time'),
        _dateType=_mapTime.eq(0).val(),
        _startTime=_mapTime.eq(1).val(),
        _endTime=_mapTime.eq(2).val(),
        _index=_mapTime.eq(3).val(),
        _selectType=_mapTime.eq(4).val(),
        _title='',
        _url,
        _json;
    _box.html('');
    $('#zongTi-diTu').css({visibility:'hidden'});
    if(parseInt(_selectType)==1){
        _title='销量最好TOP10的商铺';
    }else{
        _title='销量最差TOP10的商铺';
    }
    if(parseInt(_index)==1){  //展示表格
        _url='/storeanalysis/storeSaleAnalysis';
        _json={
            selectType:parseInt(_selectType),
            dateType:parseInt(_dateType),
            startTime:_startTime,
            endTime:_endTime,
            shopName:_name
        };
    }else{  //展示地图
        _url='/storeanalysis/getAreaByType';
        _json={
            dateType:parseInt(_dateType),
            startTime:_startTime,
            endTime:_endTime,
            shopName:_name
        };
    }
    $.ajax({
        url:SLSCHTTP+_url,
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            if(parseInt(_index)==1){
                var _i,_len=response.data.length,_strLeft='',_strRight='',_total=0;
                for(_i=0;_i<_len;_i++){
                    _total+=parseInt(response.data[_i].totalPrice);
                    if(_i<5){
                        _strLeft+='<tr><td>'+response.data[_i].goods_name+'</td><td>'+response.data[_i].totalPrice+'元</td></tr>';
                    }else{
                        _strRight+='<tr><td>'+response.data[_i].goods_name+'</td><td>'+response.data[_i].totalPrice+'元</td></tr>';
                    }
                }
                $('.map-title').html(_title+'<span style="padding: 0 20px;"></span>'+' 总金额：'+_total+'元');
                _box.html('<div class="row" style="margin-top: 30px;">'+
                    '<div class="col-md-6">'+
                    '<table class="table zong-ti-table">'+
                     _strLeft+
                    '</table></div>'+
                    '<div class="col-md-6">'+
                    '<table class="table zong-ti-table">'+
                    _strRight+
                    '</table></div>'+
                    '</div>'
                );
            }else{
                $('.map-title').html('');
                var _j,_max=response.data[0].buytimes,_leng=response.data.length,_data=[],zongTiChart,zongTiLoading;
                for(_j=0;_j<_leng;_j++){
                    _data.push({name:response.data[_j].province,value: parseInt(response.data[_j].buytimes)});
                }
                var zongTiMap = {
                    title : {
                        text: '分布图',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item'
                    },
                    dataRange: {
                        min: 0,
                        max: parseInt(_max),
                        x: 'left',
                        y: 'bottom',
                        text:['高','低'],           // 文本，默认为数值文本
                        calculable : false
                    },
                    series : [
                        {
                            name: '销售量',
                            type: 'map',
                            mapType: 'china',
                            roam: false,
                            itemStyle:{
                                normal:{label:{show:true}},
                                emphasis:{label:{show:true}}
                            },
                            data:_data
                        }
                    ]
                };
                zongTiChart = echarts.init(document.getElementById('zongTiChart'));
                zongTiChart.showLoading({  //载入动画
                    text : '请稍等',
                    effect : 'whirling',
                    textStyle : {
                        fontSize : 13
                    }
                });
                clearTimeout(zongTiLoading);
                zongTiLoading = setTimeout(function (){
                    zongTiChart.hideLoading();
                    zongTiChart.setOption(zongTiMap);
                },2000);
            }
            $('#zongTi-diTu').css({visibility:'visible'});
        }
    });
}
/**
 *关闭地图
 */
function closeZongTiMap(){
    $('#zongTi-diTu').css({visibility:'hidden'});
}
