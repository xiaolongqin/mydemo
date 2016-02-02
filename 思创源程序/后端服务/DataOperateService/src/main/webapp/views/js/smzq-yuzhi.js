$(function(){
    var checkbox = $('.period-content').find('img');
    var checktitle = $('.period-title').find('img');
    var selectall = $('.select-all');
    checktitle.attr('flag','1');
    selectall.attr('flag','0');
    selectall.click(function(){
        if( selectall.attr('flag') == 0 ){
            checktitle.attr('flag','1');
            checkbox.attr('flag','1');
            checktitle.attr('src','../images/checked-true.png');
            checkbox.attr('src','../images/checked-true.png');
            selectall.attr('flag','1');
            selectall.find('img').attr('src','../images/checked-true.png');
        }
        else{
            $('.period').find('input').val('');
            checktitle.attr('flag','0');
            checkbox.attr('flag','0');
            checktitle.attr('src','../images/checked-false.png');
            checkbox.attr('src','../images/checked-false.png');
            selectall.attr('flag','0');
            selectall.find('img').attr('src','../images/checked-false.png');
        }
    });
    checkbox.attr('flag','0');
    checktitle.click(function(){
        var check = $(this);
        var content = check.parent().parent().find('.period-content').find('img');
        if ( check.attr('flag') == 0 ){
            check.attr('flag','1');
            check.attr('src','../images/checked-true.png');
//            content.attr('flag','1');
//            content.attr('src','../images/checked-true.png');
        }
        else{
            check.attr('flag','0');
            check.attr('src','../images/checked-false.png');
            check.parent().parent().find('input').val('');
            content.attr('flag','0');
            content.attr('src','../images/checked-false.png');
            selectall.attr('flag','0');
            selectall.find('img').attr('src','../images/checked-false.png');
        }
    });
    checkbox.click(function(){
        var check = $(this);
        var title = check.parent().parent().parent().find('.period-title').find('img');
        if ( check.attr('flag') == 0 ){
            check.attr('flag','1');
            check.attr('src','../images/checked-true.png');
            title.attr('src','../images/checked-true.png');
            title.attr('flag','1');
        }
        else{
            check.attr('flag','0');
            check.attr('src','../images/checked-false.png');
            check.siblings('input').val('');
            selectall.attr('flag','0');
            selectall.find('img').attr('src','../images/checked-false.png');
        }
    });
    var inputDays = $('.period-title').find('input');
    inputDays.change(function(){
        getDays();
    });
    getYuzhi();
    $('.yuzhi-child').on('keydown',function(){
        $(this).parent().find('img').attr('src','../images/checked-true.png');
        $(this).parent().find('img').attr('flag','1');
    })
});
function IsNum(e) {
    var k = window.event ? e.keyCode : e.which;
    if (((k >= 48) && (k <= 57)) || k == 8 ) {
    } else {
        if (window.event) {
            window.event.returnValue = false;
        }
        else {
            e.preventDefault(); //for firefox
        }
    }
}
function saveYuzhi(){
    var inputDays = $('.period-title').find('input');
    var inputFlag = $('.period-title').find('img');
    var flag = 1 ;//是否可以保存信息 1.可以保存  2.有某个时期的标题未勾选  0.有特定的时间填写错误或者未填
    for(var i=0;i<inputFlag.length;i++){
        if(parseInt(inputFlag.eq(i).attr('flag'))==0){
            flag=2;
            alert("所有时期必须勾选");
            break;
        }
    }
    if(flag == 1){
        for(var j=0;j<inputDays.length;j++){
            if( ($(inputDays[j]).val()=='')  && ($(inputDays[j]).parent().find('img').attr('flag')==1)){
                alert("第"+(j+1)+"个时期的时间未填");
                flag = 0 ;
                break;
            }
            if( ( $(inputDays[j]).val()<=parseInt($(inputDays[j]).parent().children('span').children('span').html()) ) && ($(inputDays[j]).parent().find('img').attr('flag')==1) ){
                alert("第"+(j+1)+"个时期的时间填写有误");
                flag = 0 ;
                break;
            }
        }
        var information = $('.period-content').find('input');
        for(var k=0;k<information.length;k++){
            if($(information[k]).val()=='' && $(information[k]).parent().find('img').attr('flag')==1){
                alert("第"+ parseInt(k/6+1) + "个时期的第" + (k%6+1) + "个数值未填" );
                flag = 0;
                break;
            }
        }
        if(flag == 1){
            var period = $('.period');
            var arr = [];
            for(var l=0;l<period.length;l++){
                var id = period.eq(l).find('.period-num').text();
                var stime = period.eq(l).find('.period-title').children('span').children('span').text();
                var etime = period.eq(l).find('.period-title').find('input').val();
                var perioditem = $('.period-item');
                var slogin ;
                var elogin ;
                var sorder ;
                var eorder ;
                var sprice ;
                var eprice ;
                if(parseInt(perioditem.eq(0).find('img').attr('flag'))==1){
                    slogin = period.eq(l).find('.period-content').find('input').eq(0).val();
                    elogin = period.eq(l).find('.period-content').find('input').eq(1).val();
                }
                if(parseInt(perioditem.eq(1).find('img').attr('flag'))==1){
                    sorder = period.eq(l).find('.period-content').find('input').eq(2).val();
                    eorder = period.eq(l).find('.period-content').find('input').eq(3).val();
                }
                if(parseInt(perioditem.eq(2).find('img').attr('flag'))==1){
                    sprice = period.eq(l).find('.period-content').find('input').eq(4).val();
                    eprice = period.eq(l).find('.period-content').find('input').eq(5).val();
                }
                var json = {
                    "thresholdid":id,
                    "start_time": stime,
                    "end_time": etime,
                    "start_login_num":slogin,
                    "end_login_num": elogin,
                    "start_order_num":sorder,
                    "end_order_num":eorder,
                    "start_order_price":sprice,
                    "end_order_price":eprice
                };
                arr.push(json);
            }
            $.ajax({
                url:AJAXURL.saveYuzhi,
                type:'post',
                data:{
                    "json":JSON.stringify(arr)
                },
                dataType:'json',
                success:function(response){
                    if(response.state==true){
//                        var _list=$(window.parent.document).find('.list-chosen');
//                        _list.next().addClass('list-chosen').attr('btn','0');
//                        _list.next().find('.triangle').removeClass('list-hide');
//                        _list.removeClass('list-chosen').attr('btn','1');
//                        _list.find('.triangle').addClass('list-hide');
                        window.location.href = SLSCHTTP + '/views/html/kehu-shengmingfenxi.html';
                    }
                }
            });
        }
    }
}

function resetYuzhi(){
    var checkbox = $('.period-content').find('img');
    var checktitle = $('.period-title').find('img');
    var selectall = $('.select-all');
    checktitle.attr('flag','0');
    checkbox.attr('flag','0');
    checktitle.attr('src','../images/checked-false.png');
    checkbox.attr('src','../images/checked-false.png');
    selectall.attr('flag','0');
    selectall.find('img').attr('src','../images/checked-false.png');
    $('.yuzhi-content').find('input').val(null);
}

function getDays(){
    var inputDays = $('.period-title').find('input');
    for(var i=0;i<inputDays.length-1;i++){
        $(inputDays[i+1]).parent().children('span').children('span').html( $(inputDays[i]).val() ) ;
    }

     var changedays = $('.period-title');
     changedays.eq(changedays.length-1).children('span').children('span').html( $(inputDays[inputDays.length-1]).val() );
}
function getYuzhi(){
    $.ajax({
        url:AJAXURL.getThreshold,
        type:'post',
        data:{},
        dataType:'json',
        success:function(response){
            if(response.state == true){
                var period = $('.period'),len=response.data.length;
                for(var i=0;i<len;i++){
                    period.eq(i).find('.period-title').eq(0).find('span').eq(0).find('span').text(response.data[i].start_time);
                    period.eq(i).find('.period-title').eq(0).find('.yuzhi-value').val(response.data[i].end_time);
                    
                    if(response.data[i].start_login_num!='null' && response.data[i].start_login_num!=''){
                        period.eq(i).find('.period-item').eq(0).find('img').attr('src','../images/checked-true.png');
                        period.eq(i).find('.period-item').eq(0).find('img').attr('flag','1');
                        period.eq(i).find('.period-item').eq(0).find('.yuzhi-value').eq(0).val(response.data[i].start_login_num);
                    }
                    if(response.data[i].end_login_num!='null' && response.data[i].end_login_num!=''){
                        period.eq(i).find('.period-item').eq(0).find('.yuzhi-value').eq(1).val(response.data[i].end_login_num);
                    }


                    if(response.data[i].start_order_num!='null' && response.data[i].start_order_num!=''){
                        period.eq(i).find('.period-item').eq(1).find('img').attr('src','../images/checked-true.png');
                        period.eq(i).find('.period-item').eq(1).find('img').attr('flag','1');
                        period.eq(i).find('.period-item').eq(1).find('.yuzhi-value').eq(0).val(response.data[i].start_order_num);
                    }
                    if(response.data[i].end_order_num!='null' && response.data[i].end_order_num!=''){
                        period.eq(i).find('.period-item').eq(1).find('.yuzhi-value').eq(1).val(response.data[i].end_order_num);
                    }

                    if(response.data[i].start_order_price!='null' && response.data[i].start_order_price!=''){
                        period.eq(i).find('.period-item').eq(2).find('img').attr('src','../images/checked-true.png');
                        period.eq(i).find('.period-item').eq(2).find('img').attr('flag','1');
                        period.eq(i).find('.period-item').eq(2).find('.yuzhi-value').eq(0).val(response.data[i].start_order_price);
                    }
                    if(response.data[i].end_order_price!='null' && response.data[i].end_order_price!=''){
                        period.eq(i).find('.period-item').eq(2).find('.yuzhi-value').eq(1).val(response.data[i].end_order_price);
                    }
                }
            }
        }
    });
}
