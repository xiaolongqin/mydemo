(function(){
    //  公共时间范围
    var nowDate = new Date();
    var _start=nowDate.getTime()-730*24*3600*1000;
    var _defStart=nowDate.getTime()-90*24*3600*1000;  //自定义时间，作为三个月的起始时间
    var _end=nowDate.getTime()-24*3600*1000;
    var startTime = new Date(_start);
    var syear = startTime.getFullYear(),smonth = parseInt(startTime.getMonth())+1,sday = startTime.getDate() ;
    var defStartTime = new Date(_defStart);
    var dyear = defStartTime.getFullYear(),dmonth = parseInt(defStartTime.getMonth())+1, dday = defStartTime.getDate() ;
    var endTime = new Date(_end);
    var eyear = endTime.getFullYear(),emonth = parseInt(endTime.getMonth())+1,eday = endTime.getDate() ;
    var startd = syear + '-' + smonth + '-' + sday;
    var defStart = dyear + '-' + dmonth + '-' + dday;
    var endd = eyear + '-' + emonth + '-' + eday;
//  日历1
    var dateFirst1 = $('#myCalender1');
    var dateLast1 = $('#myCalender2');
    var dateFirst1Api;
    var dateLast1Api;
    dateFirst1.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateFirst1Api = api;
        dateFirst1Api.setDate(dyear,dmonth,dday);
    });
    dateLast1.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateLast1Api = api;
        dateLast1Api.setDate(eyear,emonth,eday);
    });
    dateFirst1.bind('change', function(){
        var firstTime = parseInt(dateFirst1Api.getDate('TIME'), 10);
        var lastTime = parseInt(dateLast1Api.getDate('TIME'), 10);
        if (lastTime < firstTime) {
            dateLast1Api.clearDate();
        }
        dateLast1Api.setOptions({
            startDate: firstTime - 24*3600*1000
        });
        dateLast1Api.show();
    });
//  日历2
    var dateFirst2 = $('#myCalender3');
    var dateLast2 = $('#myCalender4');
    var dateFirst2Api;
    var dateLast2Api;
    dateFirst2.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateFirst2Api = api;
        dateFirst2Api.setDate(dyear,dmonth,dday);
    });

    dateLast2.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateLast2Api = api;
        dateLast2Api.setDate(eyear,emonth,eday);
    });
    dateFirst2.bind('change', function(){
        var firstTime = parseInt(dateFirst2Api.getDate('TIME'), 10);
        var lastTime = parseInt(dateLast2Api.getDate('TIME'), 10);
        if (lastTime < firstTime) {
            dateLast2Api.clearDate();
        }
        dateLast2Api.setOptions({
            startDate: firstTime - 24*3600*1000
        });
        dateLast2Api.show();
    });
//  如果还有其它日历
    var dateFirst3 = $('#myCalender5');
    var dateLast3 = $('#myCalender6');
    var dateFirst3Api;
    var dateLast3Api;
    dateFirst3.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateFirst3Api = api;
        dateFirst3Api.setDate(dyear,dmonth,dday);
    });

    dateLast3.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateLast3Api = api;
        dateLast3Api.setDate(eyear,emonth,eday);
    });
    dateFirst3.bind('change', function(){
        var firstTime = parseInt(dateFirst3Api.getDate('TIME'), 10);
        var lastTime = parseInt(dateLast3Api.getDate('TIME'), 10);
        if (lastTime < firstTime) {
            dateLast3Api.clearDate();
        }
        dateLast3Api.setOptions({
            startDate: firstTime - 24*3600*1000
        });
        dateLast3Api.show();
    });

    var dateFirst4 = $('#myCalender7');
    var dateLast4 = $('#myCalender8');
    var dateFirst4Api;
    var dateLast4Api;
    dateFirst4.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateFirst4Api = api;
        dateFirst4Api.setDate(dyear,dmonth,dday);
    });

    dateLast4.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateLast4Api = api;
        dateLast4Api.setDate(eyear,emonth,eday);
    });
    dateFirst4.bind('change', function(){
        var firstTime = parseInt(dateFirst4Api.getDate('TIME'), 10);
        var lastTime = parseInt(dateLast4Api.getDate('TIME'), 10);
        if (lastTime < firstTime) {
            dateLast4Api.clearDate();
        }
        dateLast4Api.setOptions({
            startDate: firstTime - 24*3600*1000
        });
        dateLast4Api.show();
    });


    var dateFirst5 = $('#myCalender9');
    var dateLast5 = $('#myCalender10');
    var dateFirst5Api;
    var dateLast5Api;
    dateFirst5.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateFirst5Api = api;
        dateFirst5Api.setDate(dyear,dmonth,dday);
    });

    dateLast5.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateLast5Api = api;
        dateLast5Api.setDate(eyear,emonth,eday);
    });
    dateFirst5.bind('change', function(){
        var firstTime = parseInt(dateFirst5Api.getDate('TIME'), 10);
        var lastTime = parseInt(dateLast5Api.getDate('TIME'), 10);
        if (lastTime < firstTime) {
            dateLast5Api.clearDate();
        }
        dateLast5Api.setOptions({
            startDate: firstTime - 24*3600*1000
        });
        dateLast5Api.show();
    });

    var dateFirst6 = $('#myCalender11');
    var dateLast6 = $('#myCalender12');
    var dateFirst6Api;
    var dateLast6Api;
    dateFirst6.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateFirst6Api = api;
        dateFirst6Api.setDate(dyear,dmonth,dday);
    });

    dateLast6.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateLast6Api = api;
        dateLast6Api.setDate(eyear,emonth,eday);
    });
    dateFirst6.bind('change', function(){
        var firstTime = parseInt(dateFirst6Api.getDate('TIME'), 10);
        var lastTime = parseInt(dateLast6Api.getDate('TIME'), 10);
        if (lastTime < firstTime) {
            dateLast6Api.clearDate();
        }
        dateLast6Api.setOptions({
            startDate: firstTime - 24*3600*1000
        });
        dateLast6Api.show();
    });

    var dateFirst7 = $('#myCalender13');
    var dateLast7 = $('#myCalender14');
    var dateFirst7Api;
    var dateLast7Api;
    dateFirst7.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateFirst7Api = api;
        dateFirst7Api.setDate(dyear,dmonth,dday);
    });

    dateLast7.cxCalendar({
        startDate:startd,
        endDate:endd
    },function(api){
        dateLast7Api = api;
        dateLast7Api.setDate(eyear,emonth,eday);
    });
    dateFirst7.bind('change', function(){
        var firstTime = parseInt(dateFirst7Api.getDate('TIME'), 10);
        var lastTime = parseInt(dateLast7Api.getDate('TIME'), 10);
        if (lastTime < firstTime) {
            dateLast7Api.clearDate();
        }
        dateLast7Api.setOptions({
            startDate: firstTime - 24*3600*1000
        });
        dateLast7Api.show();
    });
})();