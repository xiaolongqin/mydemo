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
    $.ajax({
        url:SLSCHTTP+'/repeatorder/repeatOrder',
        type:'post',
        data:{paramType:1,dateType:1},  //正式对接dateType改为1
        dataType:'json',
        success:function(response){
            if(response.state==false){
                $('.no-data').css({display:'block'});
                $('.pic-box-top').css({display:'none'});
            }else{
                var _i,_len=response.data.length,_this=$('#te-zheng');
                for(_i=0;_i<_len;_i++){
                    if(_i<5){
                        _this.find('.left-circle').eq(_i).text(response.data[_i].percent+'%');
                        _this.find('.goods-word').eq(_i).text(response.data[_i].goods_name);
//                        +' ('+response.data[_i].address+')'
                    }else{
                        _this.find('.right-circle').eq(_i-5).text(response.data[_i].percent+'%');
                        _this.find('.goods-word2').eq(_i-5).text(response.data[_i].goods_name);
//                        +' ('+response.data[_i].address+')'
                    }
                }
//              隐藏域存时间点，为地图传时间做准备
                var _mapTime=$('.map-time');
                _mapTime.eq(0).val(1);
                _mapTime.eq(1).val('');
                _mapTime.eq(2).val('');
            }
        }
    });
});
/**
 *选择改变字体颜色
 */
function chongFuChange(obj){
    $(obj).addClass('chosen-active').siblings().removeClass('chosen-active');
    var _num=$(obj).siblings().length;
    if(parseInt(_num)==4){
        $('#start-time').val('');
        $('#end-time').val('');
    }
}
/**
 *搜索
 */
function seeChongFu(){
    $('#chongFu-diTu').css({visibility:'hidden'});
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
        _paramType=$('.top-list').find('.chosen-active').index(),
        _dateType,
        _json;
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
                paramType:parseInt(_paramType)+1,
                dateType:_dateType
            };
        }
    }
    $.ajax({
        url:SLSCHTTP+'/repeatorder/repeatOrder',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            if(response.state==false){  //展示数据异常图片
                $('.no-data').css({display:'block'});
                $('.pic-box-top').css({display:'none'});
                $('.pic-box-high').css({display:'none'});
                $('.pic-box-bottom').css({display:'none'});
            }else{
                var _len=response.data.length,_this;
                if(parseInt(_paramType)==0){
                    var _i;
                    _this=$('#te-zheng');
                    _this.find('.left-circle').text('');
                    _this.find('.goods-word').text('');
                    _this.find('.right-circle').text('');
                    _this.find('.goods-word2').text('');
                    for(_i=0;_i<_len;_i++){
                        if(_i<5){
                            _this.find('.left-circle').eq(_i).text(response.data[_i].percent+'%');
                            _this.find('.goods-word').eq(_i).text(response.data[_i].goods_name);
//                            +' ('+response.data[_i].address+')'
                        }else{
                            _this.find('.right-circle').eq(_i-5).text(response.data[_i].percent+'%');
                            _this.find('.goods-word2').eq(_i-5).text(response.data[_i].goods_name);
//                            +' ('+response.data[_i].address+')'
                        }
                    }
                    $('.no-data').css({display:'none'});
                    $('.pic-box-top').css({display:'block'});
                    $('.pic-box-high').css({display:'none'});
                    $('.pic-box-bottom').css({display:'none'});
                }else if(parseInt(_paramType)==1){
                    var _j;
                    _this=$('#dian-pu');
                    _this.find('.left-circle').text('');
                    _this.find('.goods-word').text('');
                    _this.find('.right-circle').text('');
                    _this.find('.goods-word2').text('');
                    for(_j=0;_j<_len;_j++){
                        if(_j<5){
                            _this.find('.left-circle').eq(_j).text(response.data[_j].percent+'%');
                            _this.find('.goods-word').eq(_j).text(response.data[_j].shopsname);
//                            +' ('+response.data[_j].address+')'
                        }else{
                            _this.find('.right-circle').eq(_j-5).text(response.data[_j].percent+'%');
                            _this.find('.goods-word2').eq(_j-5).text(response.data[_j].shopsname);
//                            +' ('+response.data[_j].address+')'
                        }
                    }
                    $('.no-data').css({display:'none'});
                    $('.pic-box-top').css({display:'none'});
                    $('.pic-box-high').css({display:'block'});
                    $('.pic-box-bottom').css({display:'none'});
                }else{
                    var _k;
                    _this=$('#yong-hu');
                    _this.find('.left-circle').text('');
                    _this.find('.goods-word').text('');
                    _this.find('.right-circle').text('');
                    _this.find('.goods-word2').text('');
                    for(_k=0;_k<_len;_k++){
                        if(_k<5){
                            _this.find('.left-circle').eq(_k).text(response.data[_k].percent+'%');
                            _this.find('.goods-word').eq(_k).text(response.data[_k].phone+'('+response.data[_k].address+')');
                        }else{
                            _this.find('.right-circle').eq(_k-5).text(response.data[_k].percent+'%');
                            _this.find('.goods-word2').eq(_k-5).text(response.data[_k].phone+'('+response.data[_k].address+')');
                        }
                    }
                    $('.no-data').css({display:'none'});
                    $('.pic-box-top').css({display:'none'});
                    $('.pic-box-high').css({display:'none'});
                    $('.pic-box-bottom').css({display:'block'});
                    $('#chongFu-diTu').css({visibility:'hidden'});
                }
//              隐藏域存时间点，为地图传时间做准备
                var _mapTime=$('.map-time');
                if(date1!='' && date2!=''){
                    _mapTime.eq(0).val(0);
                }else{
                    _mapTime.eq(0).val(_dateType);
                }
                _mapTime.eq(1).val(_startTime);
                _mapTime.eq(2).val(_endTime);
            }
        }
    });
}
/**
 *查看地图
 */
function chongFuMap(obj){
    var _name=$(obj).siblings().text().split(' (')[0],
        _mapTime=$('.map-time'),
        _index=$('.top-list').find('.chosen-active').index(),
        _dateType=_mapTime.eq(0).val(),
        _startTime=_mapTime.eq(1).val(),
        _endTime=_mapTime.eq(2).val(),
        _json;
    if(_name==''){
        return false;
    }
    if(parseInt(_index)==1){
        _json={shopName:_name,dateType:_dateType,startTime:_startTime,endTime:_endTime}
    }else{
        _json={goodName:_name,dateType:_dateType,startTime:_startTime,endTime:_endTime};
    }
    $('#chongFu-diTu').css({visibility:'visible'});
    $.ajax({
        url:SLSCHTTP+'/repeatorder/getAreaByType',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            var _i,_max=response.data[0].buytimes,_len=response.data.length,_data=[],chongFuChart,chongFuLoading;
            for(_i=0;_i<_len;_i++){
                _data.push({name:response.data[_i].province,value: parseInt(response.data[_i].buytimes)});
            }
            var chongFuMap = {
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
            chongFuChart = echarts.init(document.getElementById('chongFuChart'));
            chongFuChart.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 13
                }
            });
            clearTimeout(chongFuLoading);
            chongFuLoading = setTimeout(function (){
                chongFuChart.hideLoading();
                chongFuChart.setOption(chongFuMap);
            },2000);
        }
    });
}
/**
 *关闭地图
 */
function closeChongFuMap(){
    $('#chongFu-diTu').css({visibility:'hidden'});
}