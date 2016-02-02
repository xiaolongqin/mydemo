/**
 * 数之云-用户-RDS
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-12-24 18:14:47
 * @version 1.0
 */
$(function(){
	var szyInNavUl=$("#J_szyInNavUl"),
		szyInNavUlWidth=szyInNavUl.width(),
		szyInNavUlOffset=szyInNavUl.offset(),
		szyInNavUlTop=szyInNavUlOffset.top,
		szyInNavUlLeft=szyInNavUlOffset.left;
	$(document).on("scroll",function(){
		var scrollTop=$(this).scrollTop();
		//console.log(scrollTop);
		if(scrollTop >= szyInNavUlTop){
			szyInNavUl.addClass('navbar-fixed-top');
			szyInNavUl.css({"width":szyInNavUlWidth,"left":szyInNavUlLeft+15});
		}else{
			szyInNavUl.removeClass('navbar-fixed-top');
			szyInNavUl.css("width","auto");
		}
	});
	var szyMainHeight=$("#J_szyMain").height();
	$("#J_szyMenuBox").css("height",szyMainHeight+"px");
});
/**
 * 立即使用
 */
function goUseRds(){
	$.ajax({
        url:AJAXURL.toUsing,
        type:"POST",
        dataType:"JSON",
        success:function(data){
            if(data.state){
                location.href=data.data.json;
            }else{
                location.href="login";
            }
        },
        error:function(){
        	 location.href="login";
        }
    });
}