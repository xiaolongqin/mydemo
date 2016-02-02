/**
 *进入加载7天和商品购买量
 */
var _dianPuChart;
/**
 *初始化
 */
$(function(){
//日历初始化
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
    $('.title').text(_time.getFullYear()+'全国店铺销售分析');
//  默认展示商品购买量地址为全部
    $.ajax({
        url:SLSCHTTP+'/shopsales/getShopSales',
        type:'post',
        data:{dateType:3,address:'全部'},
        dataType:'json',
        success:function(response){
            var _len=response.data.length,_shopName=[],_cityName=[],_goodNum=[],_i1,_i2,_data=[],_loading;
            for(_i1=0;_i1<_len;_i1++){
                _shopName.push(response.data[_i1].shopsname);  //店铺名
                _cityName.push(response.data[_i1].province+response.data[_i1].city+response.data[_i1].village);  //地址名
                _goodNum.push(response.data[_i1]['goods_num']);  //商品购买量
            }
            for(_i2=0;_i2<_len;_i2++){
                _data.push({
                    name:_cityName[_i2],  //鼠标移入事件显示地址名
                    type:'scatter',
                    symbolSize : function(){  //圆点大小
                        return 6;
                    },
                    data:[[_i2,_goodNum[_i2]]]  //这里将每个点独立为一个个体，而不是之前的将所有数据变为二维数组再放到一起
                });
            }
            var option1 = {
                title : {
                    text: '店铺销售分析图',
                    x:'center',
                    textStyle:{
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter : function (params) {
                        return params.seriesName + '<br/>'
                            + _shopName[params.seriesIndex]+'：'+params.value[1]
                    },
                    axisPointer:{
                        show: true,
                        type : 'cross',
                        lineStyle: {
                            type : 'dashed',
                            width : 1
                        }
                    }
                },
                dataZoom: {
                    show: true,
                    start : 0,
                    end : 100
                },
                xAxis : [
                    {
                        type : 'value',
                        axisLabel: {
                            formatter : function(v){
                                return _shopName[v]
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name: '单位：个',
                        type : 'value'
                    }
                ],
                series : _data
            };
            _dianPuChart = echarts.init(document.getElementById('xiaoshouCharts'));
            _dianPuChart.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 15
                }
            });
            clearTimeout(_loading);
            _loading = setTimeout(function (){
                _dianPuChart.hideLoading();
                _dianPuChart.setOption(option1);
                _dianPuChart.setTheme({scatter:{symbol: 'circle'}});
            },2000);
            window.onresize = function () {
                _dianPuChart.resize();
            };
        }
    });
});
/**
 *快速选择时间
 */
function getShopSales(obj){
//  重置
    $(obj).addClass('time-active').siblings().removeClass('time-active');
    $('.charts-list').find('li').eq(0).addClass('time-active').siblings().removeClass('time-active');
    $('#time-start').val('');
    $('#time-end').val('');
    var _address=$('#shopDis').val();
    if(_address==''){
        _address='全部';
    }
    var _index=$(obj).index();
    switch (parseInt(_index)){
        case 0:_index=3;break;
        case 1:_index=6;break;
        case 2:_index=4;break;
    }
    $.ajax({
        url:SLSCHTTP+'/shopsales/getShopSales',
        type:'post',
        data:{dateType:_index,address:_address},
        dataType:'json',
        success:function(response){
            var _len=response.data.length,_shopName=[],_cityName=[],_goodNum=[],_i1,_i2,_data=[],_loading;
            for(_i1=0;_i1<_len;_i1++){
                _shopName.push(response.data[_i1].shopsname);  //店铺名
                _cityName.push(response.data[_i1].province+response.data[_i1].city+response.data[_i1].village);  //地址名
                _goodNum.push(response.data[_i1]['goods_num']);  //商品购买量
            }
            for(_i2=0;_i2<_len;_i2++){
                _data.push({
                    name:_cityName[_i2],  //鼠标移入事件显示地址名
                    type:'scatter',
                    symbolSize : function(){  //圆点大小
                        return 6;
                    },
                    data:[[_i2,_goodNum[_i2]]]  //这里将每个点独立为一个个体，而不是之前的将所有数据变为二维数组再放到一起
                });
            }
            var option1 = {
                title : {
                    text: '店铺销售分析图',
                    x:'center',
                    textStyle:{
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter : function (params) {
                        return params.seriesName + '<br/>'
                            + _shopName[params.seriesIndex]+'：'+params.value[1]
                    },
                    axisPointer:{
                        show: true,
                        type : 'cross',
                        lineStyle: {
                            type : 'dashed',
                            width : 1
                        }
                    }
                },
                dataZoom: {
                    show: true,
                    start : 0,
                    end : 100
                },
                xAxis : [
                    {
                        type : 'value',
                        axisLabel: {
                            formatter : function(v){
                                return _shopName[v]
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name: '单位：个',
                        type : 'value'
                    }
                ],
                series : _data
            };
            _dianPuChart = echarts.init(document.getElementById('xiaoshouCharts'));
            _dianPuChart.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 15
                }
            });
            clearTimeout(_loading);
            _loading = setTimeout(function (){
                _dianPuChart.hideLoading();
                _dianPuChart.setOption(option1);
                _dianPuChart.setTheme({scatter:{symbol: 'circle'}});
            },2000);
        }
    });
}
/**
 *日历确定
 */
function getNumbers(){
    var date1=$('#time-start').val(),
        date2=$('#time-end').val();
    if(date1!='' && date2!='') {
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
//          重置
            $('.distri-time').find('li').removeClass('time-active');
            $('.charts-list').find('li').eq(0).addClass('time-active').siblings().removeClass('time-active');
            var _address=$('#shopDis').val();
            if(_address==''){
                _address='全部';
            }
            var _startTime=date1.split('-').join(''),
                _endTime=date2.split('-').join('');
            $.ajax({
                url:SLSCHTTP+'/shopsales/getShopSales',
                type:'post',
                data:{dateType:0,startTime:_startTime,endTime:_endTime,address:_address},
                dataType:'json',
                success:function(response){
                    var _len=response.data.length,_shopName=[],_cityName=[],_goodNum=[],_i1,_i2,_data=[],_loading;
                    for(_i1=0;_i1<_len;_i1++){
                        _shopName.push(response.data[_i1].shopsname);  //店铺名
                        _cityName.push(response.data[_i1].province+response.data[_i1].city+response.data[_i1].village);  //地址名
                        _goodNum.push(response.data[_i1]['goods_num']);  //商品购买量
                    }
                    for(_i2=0;_i2<_len;_i2++){
                        _data.push({
                            name:_cityName[_i2],  //鼠标移入事件显示地址名
                            type:'scatter',
                            symbolSize : function(){  //圆点大小
                                return 6;
                            },
                            data:[[_i2,_goodNum[_i2]]]  //这里将每个点独立为一个个体，而不是之前的将所有数据变为二维数组再放到一起
                        });
                    }
                    var option1 = {
                        title : {
                            text: '店铺销售分析图',
                            x:'center',
                            textStyle:{
                                fontFamily: '微软雅黑',
                                fontSize: 15,
                                fontWeight: 'normal',
                                color: '#0e90cf'
                            }
                        },
                        toolbox: {
                            show : true,
                            y: 'center',
                            feature : {
                                restore : {show: true}
                            }
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter : function (params) {
                                return params.seriesName + '<br/>'
                                    + _shopName[params.seriesIndex]+'：'+params.value[1]
                            },
                            axisPointer:{
                                show: true,
                                type : 'cross',
                                lineStyle: {
                                    type : 'dashed',
                                    width : 1
                                }
                            }
                        },
                        dataZoom: {
                            show: true,
                            start : 0,
                            end : 100
                        },
                        xAxis : [
                            {
                                type : 'value',
                                axisLabel: {
                                    formatter : function(v){
                                        return _shopName[v]
                                    }
                                }
                            }
                        ],
                        yAxis : [
                            {
                                name: '单位：个',
                                type : 'value'
                            }
                        ],
                        series : _data
                    };
                    _dianPuChart = echarts.init(document.getElementById('xiaoshouCharts'));
                    _dianPuChart.showLoading({  //载入动画
                        text : '请稍等',
                        effect : 'whirling',
                        textStyle : {
                            fontSize : 15
                        }
                    });
                    clearTimeout(_loading);
                    _loading = setTimeout(function (){
                        _dianPuChart.hideLoading();
                        _dianPuChart.setOption(option1);
                        _dianPuChart.setTheme({scatter:{symbol: 'circle'}});
                    },2000);
                }
            });
        }
    }else{
        alert('请选择正确的时间段');
    }
}
/**
 *自选类型
 */
function getTypeSales(obj){
    $(obj).addClass('time-active').siblings().removeClass('time-active');
    var _index=$(obj).index(),  //选的是哪一项
        _time=$('.distri-time').find('.time-active').index(),
        _distName=$('#shopDis').val(),
        _startTime,
        _endTime,
        _json,
        _name;
    if(_time<0){  //自定义时间
        var date1=$('#time-start').val();  //开始时间
        var date2=$('#time-end').val();  //结束时间
        if(date1!='' && date2!=''){
            var year1 =  date1.substr(0,4);
            var year2 =  date2.substr(0,4);
            var month1 = date1.substr(5,2);
            var month2 = date2.substr(5,2);
            var day1 = date1.substr(8,2);
            var day2 = date2.substr(8,2);
            var temp1 = year1+"/"+month1+"/"+day1;
            var temp2 = year2+"/"+month2+"/"+day2;
            var dateaa= new Date(temp1);
            var datebb = new Date(temp2);
            var date = datebb.getTime() - dateaa.getTime();
            var time = Math.floor(date / (1000 * 60 * 60 * 24));
            if(time<0){
                alert('请选择正确的时间段');
                return false;
            }
//            if(time>90){
//                alert('您的时间跨度为'+time+'天，时间跨度超过90天');
//                return false;
//            }
        }else{
            alert('请选择正确的时间段');
            return false;
        }
        _startTime=date1.split('-').join('');
        _endTime=date2.split('-').join('');
        _json={dateType:0,startTime:_startTime,endTime:_endTime,address:_distName};
    }else{  //快速选择时间
        switch (parseInt(_time)){
            case 0:_time=3;break;
            case 1:_time=6;break;
            case 2:_time=4;break;
        }
        _json={dateType:_time,address:_distName};
        $('#time-start').val('');
        $('#time-end').val('');
    }
    $.ajax({
        url:SLSCHTTP+'/shopsales/getShopSales',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            var _len=response.data.length,_shopName=[],_cityName=[],_goodNum=[],_orderNum=[],_priceNum=[],_i1,_i2,_data=[],_loading;
            for(_i1=0;_i1<_len;_i1++){
                _shopName.push(response.data[_i1].shopsname);  //店铺名
                _cityName.push(response.data[_i1].province);  //地址名
                _goodNum.push(response.data[_i1]['goods_num']);  //商品购买量
                _orderNum.push(response.data[_i1]['orders_num']);  //订单量
                _priceNum.push(response.data[_i1]['order_total_price']);  //订单金额
            }
            if(parseInt(_index)==0){  //商品购买量
                _name='单位：个';
                for(_i2=0;_i2<_len;_i2++){
                    _data.push({
                        name:_cityName[_i2],  //鼠标移入事件显示地址名
                        type:'scatter',
                        symbolSize : function(){  //圆点大小
                            return 6;
                        },
                        data:[[_i2,_goodNum[_i2]]]  //这里将每个点独立为一个个体，而不是之前的将所有数据变为二维数组再放到一起
                    });
                }
            }
            if(parseInt(_index)==1){  //订单量
                _name='单位：笔';
                for(_i2=0;_i2<_len;_i2++){
                    _data.push({
                        name:_cityName[_i2],
                        type:'scatter',
                        symbolSize : function(){
                            return 6;
                        },
                        data:[[_i2,_orderNum[_i2]]]
                    });
                }
            }
            if(parseInt(_index)==2){  //订单金额
                _name='单位：元';
                for(_i2=0;_i2<_len;_i2++){
                    _data.push({
                        name:_cityName[_i2],
                        type:'scatter',
                        symbolSize : function(){
                            return 6;
                        },
                        data:[[_i2,_priceNum[_i2]]]
                    });
                }
            }
            var option1 = {
                title : {
                    text: '店铺销售分析图',
                    x:'center',
                    textStyle:{
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter : function (params) {
                        return params.seriesName + '<br/>'
                            + _shopName[params.seriesIndex]+'：'+params.value[1]
                    },
                    axisPointer:{
                        show: true,
                        type : 'cross',
                        lineStyle: {
                            type : 'dashed',
                            width : 1
                        }
                    }
                },
                dataZoom: {
                    show: true,
                    start : 0,
                    end : 100
                },
                xAxis : [
                    {
                        type : 'value',
                        axisLabel: {
                            formatter : function(v){
                                return _shopName[v]
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name: _name,
                        type : 'value'
                    }
                ],
                series : _data
            };
            _dianPuChart = echarts.init(document.getElementById('xiaoshouCharts'));
            _dianPuChart.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 15
                }
            });
            clearTimeout(_loading);
            _loading = setTimeout(function (){
                _dianPuChart.hideLoading();
                _dianPuChart.setOption(option1);
                _dianPuChart.setTheme({scatter:{symbol: 'circle'}});
            },2000);
        }
    });
}
/**
 *输入框确定
 */
function seeDist(){
    var _address= $.trim($('#shopDis').val()),  //地址
        _index=$('.charts-list').find('.time-active').index(),  //选的是哪一项
        _time=$('.distri-time').find('.time-active').index(),
        _startTime,
        _endTime,
        _json,
        _name;
    if(_time<0){  //自定义时间
        var date1=$('#time-start').val();  //开始时间
        var date2=$('#time-end').val();  //结束时间
        if(date1!='' && date2!=''){
            var year1 =  date1.substr(0,4);
            var year2 =  date2.substr(0,4);
            var month1 = date1.substr(5,2);
            var month2 = date2.substr(5,2);
            var day1 = date1.substr(8,2);
            var day2 = date2.substr(8,2);
            var temp1 = year1+"/"+month1+"/"+day1;
            var temp2 = year2+"/"+month2+"/"+day2;
            var dateaa= new Date(temp1);
            var datebb = new Date(temp2);
            var date = datebb.getTime() - dateaa.getTime();
            var time = Math.floor(date / (1000 * 60 * 60 * 24));
            if(time<0){
                alert('请选择正确的时间段');
                return false;
            }
//            if(time>90){
//                alert('您的时间跨度为'+time+'天，时间跨度超过90天');
//                return false;
//            }
        }else{
            alert('请选择正确的时间段');
            return false;
        }
        _startTime=date1.split('-').join('');
        _endTime=date2.split('-').join('');
        _json={dateType:0,startTime:_startTime,endTime:_endTime,address:_address};
    }else{  //快速选择时间
        switch (parseInt(_time)){
            case 0:_time=3;break;
            case 1:_time=6;break;
            case 2:_time=4;break;
        }
        _json={dateType:_time,address:_address};
        $('#time-start').val('');
        $('#time-end').val('');
    }
    $.ajax({
        url:SLSCHTTP+'/shopsales/getShopSales',
        type:'post',
        data:_json,
        dataType:'json',
        success:function(response){
            var _len=response.data.length,_shopName=[],_cityName=[],_goodNum=[],_orderNum=[],_priceNum=[],_i1,_i2,_data=[],_loading;
            for(_i1=0;_i1<_len;_i1++){
                _shopName.push(response.data[_i1].shopsname);  //店铺名
                _cityName.push(response.data[_i1].province);  //地址名
                _goodNum.push(response.data[_i1]['goods_num']);  //商品购买量
                _orderNum.push(response.data[_i1]['orders_num']);  //订单量
                _priceNum.push(response.data[_i1]['order_total_price']);  //订单金额
            }
            if(parseInt(_index)==0){  //商品购买量
                _name='单位：个';
                for(_i2=0;_i2<_len;_i2++){
                    _data.push({
                        name:_cityName[_i2],  //鼠标移入事件显示地址名
                        type:'scatter',
                        symbolSize : function(){  //圆点大小
                            return 6;
                        },
                        data:[[_i2,_goodNum[_i2]]]  //这里将每个点独立为一个个体，而不是之前的将所有数据变为二维数组再放到一起
                    });
                }
            }
            if(parseInt(_index)==1){  //订单量
                _name='单位：笔';
                for(_i2=0;_i2<_len;_i2++){
                    _data.push({
                        name:_cityName[_i2],
                        type:'scatter',
                        symbolSize : function(){
                            return 6;
                        },
                        data:[[_i2,_orderNum[_i2]]]
                    });
                }
            }
            if(parseInt(_index)==2){  //订单金额
                _name='单位：元';
                for(_i2=0;_i2<_len;_i2++){
                    _data.push({
                        name:_cityName[_i2],
                        type:'scatter',
                        symbolSize : function(){
                            return 6;
                        },
                        data:[[_i2,_priceNum[_i2]]]
                    });
                }
            }
            var option1 = {
                title : {
                    text: '店铺销售分析图',
                    x:'center',
                    textStyle:{
                        fontFamily: '微软雅黑',
                        fontSize: 15,
                        fontWeight: 'normal',
                        color: '#0e90cf'
                    }
                },
                toolbox: {
                    show : true,
                    y: 'center',
                    feature : {
                        restore : {show: true}
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter : function (params) {
                        return params.seriesName + '<br/>'
                            + _shopName[params.seriesIndex]+'：'+params.value[1]
                    },
                    axisPointer:{
                        show: true,
                        type : 'cross',
                        lineStyle: {
                            type : 'dashed',
                            width : 1
                        }
                    }
                },
                dataZoom: {
                    show: true,
                    start : 0,
                    end : 100
                },
                xAxis : [
                    {
                        type : 'value',
                        axisLabel: {
                            formatter : function(v){
                                return _shopName[v]
                            }
                        }
                    }
                ],
                yAxis : [
                    {
                        name: _name,
                        type : 'value'
                    }
                ],
                series : _data
            };
            _dianPuChart = echarts.init(document.getElementById('xiaoshouCharts'));
            _dianPuChart.showLoading({  //载入动画
                text : '请稍等',
                effect : 'whirling',
                textStyle : {
                    fontSize : 15
                }
            });
            clearTimeout(_loading);
            _loading = setTimeout(function (){
                _dianPuChart.hideLoading();
                _dianPuChart.setOption(option1);
                _dianPuChart.setTheme({scatter:{symbol: 'circle'}});
            },2000);
        }
    });
}
/**
 *回车事件
 */
$('#shopDis').on('keydown',function(event){
    var evt=event?event:(window.event?window.event:null);//兼容IE和FF
    if (evt.keyCode==13){
        evt.preventDefault();
        seeDist();
    }
});