/**
 * 数之云-用户-OSS订单详情
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-12-19 15:01:41
 * @version 1.0
 */
var ORDER_NUM="";
$(function(){
	var search=location.search;
	if(search && search!="" && search!=null ){
		var urlParm=getSearchAsArray(search);
		if(urlParm["ordernum"]){
			getOrderInfo(urlParm["ordernum"]);
		}else{
			alert("请求参数错误");
			location.href="ossorder";
		}
	}else{
		alert("请求参数错误");
		location.href="ossorder";
	}	
});
/**
 * 获得订单基本信息
 */
function getOrderInfo(orderid){
	$.ajax({
		url:AJAXURL.getDetailsByNum,
		data:{"order_num":orderid},
		type:"POST",
		dataType:"JSON",
		success:function(json){
			if(json.state){
				var order=json.data[0],
					pay_state="";
				if(order.user_operate==0){
					pay_state=getPayStateName(order.pay_state)
				}else if(order.user_operate==1){
					pay_state="已取消";
				}
				$("#J_ossOrderId").html(order.order_num);
				$("#J_ossOrderDtime").html(order.order_time);
				$("#J_ossOrderState").html(pay_state);
				$("#J_ossOrderSize").html(order.all_space);
				$("#J_ossOrderSizeAdd").html(order.add_space);
				$("#J_ossOrderPrice").html("￥"+order.price);
			}else{
				ALERT.init({info:"数据异常，请稍后再试！"});
			}
		},
		error:function(){
			ALERT.init({info:"链接异常，请稍后再试！"});
		}
	});
}

