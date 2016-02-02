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
            url:SLSCHTTP+'/orderanalysis/generalAnalysis',
            type:'post',
            data:{goodsType:1,dateType:1},  //正式上线改为dateType:1
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
                $('#xiao-guo').css({display:'none'});
                $('.pic-box-top').css({display:'block'});
                $('.pic-box-high').css({display:'none'});
            }else{
                var _i,_len=response1[0].data.length,_this=$('#xiao-guo');
                for(_i=0;_i<_len;_i++){
                    if(_i<5){
                        _this.find('.left-circle').eq(_i).text(response1[0].data[_i].percent+'%');
                        _this.find('.goods-word').eq(_i).text(response1[0].data[_i].goodsname);
//                        +' ('+response1[0].data[_i].address+')'
                    }else{
                        _this.find('.right-circle').eq(_i-5).text(response1[0].data[_i].percent+'%');
                        _this.find('.goods-word2').eq(_i-5).text(response1[0].data[_i].goodsname);
//                        +' ('+response1[0].data[_i].address+')'
                    }
                }
                _this.css({display:'block'});
//              隐藏域存时间点，为地图传时间做准备
                var _mapTime=$('.map-time');
                _mapTime.eq(0).val(1);
                _mapTime.eq(1).val('');
                _mapTime.eq(2).val('');
            }
//          总数
            $('.total-number').find('span').text(response2[0].data.orderCount[0].count);
    });
});
/**
 *选择改变字体颜色
 */
function dingDanChange(obj){
    $(obj).addClass('chosen-active').siblings().removeClass('chosen-active');
    var _num=$(obj).siblings().length,_index;
    if(parseInt(_num)==1){  //选择的是效果还是特征
        _index=$(obj).index();
        if(parseInt(_index)==0){  //选择的是效果
            $('#dingDanSearch1').css({display:'block'});
            $('#dingDanSearch2').css({display:'none'});
        }else{  //选择的是特征
            $('#dingDanSearch1').css({display:'none'});
            $('#dingDanSearch2').css({display:'block'});
        }
    }
    if(parseInt(_num)==4){  //点击的是快速选择时间，日历重置为空
        $('#start-time').val('');
        $('#end-time').val('');
    }
}
/**
 *效果展示
 */
function seeXiaoGuo(){
    $('#dingDan-diTu').css({visibility:'hidden'});
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
        _goodsType=$('#search-content').val(),
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
                goodsType:_goodsType,
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
                goodsType:_goodsType,
                dateType:_dateType
            };
        }
    }
    $.ajax({
        url:SLSCHTTP+'/orderanalysis/generalAnalysis',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            if(response.state==false){
                $('.no-data').css({display:'block'});
                $('#xiao-guo').css({display:'none'});
                $('.pic-box-top').css({display:'block'});
                $('.pic-box-high').css({display:'none'});
            }else{
                $('.no-data').css({display:'none'});
                var _i,_len=response.data.length,_this=$('#xiao-guo');
                for(_i=0;_i<_len;_i++){
                    if(_i<5){
                        _this.find('.left-circle').eq(_i).text(response.data[_i].percent+'%');
                        _this.find('.goods-word').eq(_i).text(response.data[_i].goodsname);
//                        +' ('+response.data[_i].address+')'
                    }else{
                        _this.find('.right-circle').eq(_i-5).text(response.data[_i].percent+'%');
                        _this.find('.goods-word2').eq(_i-5).text(response.data[_i].goodsname);
//                      +' ('+response.data[_i].address+')'
                    }
                }
                _this.css({display:'block'});
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
            }
        }
    });
}
/**
 *特征展示
 */
function seeTeZheng(){
    var date1=$('#start-time').val(),
        date2=$('#end-time').val(),
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
                dateType:_dateType
            };
        }
    }
    $.ajax({
        url:SLSCHTTP+'/orderanalysis/featureAnalysis',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            var _i,_data=[],_attr,_len,_this=$('#te-zheng');
            for(_attr in response.data[0]){
                if(response.data[0][_attr]==null){
                    _data.push('0');
                }else{
                    _data.push(response.data[0][_attr]);
                }
            }
            _this.find('.left-circle').eq(0).text(response.data[0].percent1+'%');
            _this.find('.left-circle').eq(1).text(response.data[0].percent2+'%');
            _this.find('.right-circle').eq(0).text(response.data[0].percent3+'%');
            _this.find('.right-circle').eq(1).text(response.data[0].percent4+'%');
            _this.find('.right-circle').eq(2).text(response.data[0].percent5+'%');
            $('.pic-box-top').css({display:'none'});
            $('.pic-box-high').css({display:'block'});
        }
    });
}
/**
 *查看地图
 */
function dingDanMap(obj){
    var _goodName=$(obj).siblings().text().split(' (')[0],
        _mapTime=$('.map-time'),
        _dateType=_mapTime.eq(0).val(),
        _startTime=_mapTime.eq(1).val(),
        _endTime=_mapTime.eq(2).val();
    $('#dingDan-diTu').css({visibility:'visible'});
    $.ajax({
        url:SLSCHTTP+'/orderanalysis/getAreasByGood',
        type:'post',
        data:{goodName:_goodName,dateType:_dateType,startTime:_startTime,endTime:_endTime},
        dataType:'json',
        success:function(response){
            var _i,_max=response.data[0].buytimes,_len=response.data.length,_data=[],dingDanChart,dingDanLoading;
            for(_i=0;_i<_len;_i++){
                _data.push({name: response.data[_i].province,value: parseInt(response.data[_i].buytimes)});
            }
            var dingDanMap = {
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
            dingDanChart = echarts.init(document.getElementById('dingDanChart'));
            dingDanChart.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 13
                }
            });
            clearTimeout(dingDanLoading);
            dingDanLoading = setTimeout(function (){
                dingDanChart.hideLoading();
                dingDanChart.setOption(dingDanMap);
            },2000);
        }
    });
}
/**
 *关闭地图
 */
function closeMap(){
    $('#dingDan-diTu').css({visibility:'hidden'});
}