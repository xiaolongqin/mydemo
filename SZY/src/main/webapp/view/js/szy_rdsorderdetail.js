/**
 * 数之云-用户-RDS订单详情
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-12-19 15:01:41
 * @version 1.0
 */
var ORDER_NUM="";
$(function(){
	var search=location.search;
	if(search && search!="" && search!=null ){
		var urlParm=getSearchAsArray(search);
		if(urlParm["orderid"]){
			getOrderInfo(urlParm["orderid"]);
		}else{
			alert("请求参数错误");
			location.href="rds";
		}
	}else{
		alert("请求参数错误");
		location.href="rds";
	}	
});
/**
 * 获得订单基本信息
 */
function getOrderInfo(orderid){
	$.ajax({
		url:AJAXURL.getAndSP,
		data:{"order_num":orderid},
		type:"POST",
		dataType:"JSON",
		success:function(json){
			if(json.state){
				var order=json.data,
					typename=getOrderType(order.type),
					pay_state=getPayStateName(order.pay_state),
					ck_name=getOrderCheckState(order.check_state),
					order_edate=order.order_edate=="" ? "--" : order.order_edate,
					space_state_name=getSpaceState(order.space_state);
				ORDER_NUM=order.order_num;
				if(order.pay_state==0 && order.check_state==0){//可以修改
					$("#J_rdsOrdeEditTag").removeClass('none');
				}else{
					$("#J_rdsOrdeEditTag").remove();
				}
				$("#J_rdsOrderId").html(order.order_num);
				$("#J_rdsOrderKind").html(typename);
				$("#J_rdsOrderDtime").html(order.order_cdate);
				$("#J_rdsOrderKtime").html(order_edate);
				$("#J_rdsOrderEmail").html(order.account_email);
				$("#J_rdsOrderRealName").html(order.account_name);
				$("#J_rdsOrderPhone").html(order.account_phone);
				$("#J_rdsOrderState").html(pay_state);
				$("#J_rdsOrderCheckState").html(ck_name);

				$("#J_rdsOrderSizeAdd").html(order.add_store);
				$("#J_rdsOrderConsAdd").html(order.add_conn);
				$("#J_rdsOrderTimeAdd").html(order.add_month);
				$("#J_rdsOrderPrice").html("￥"+order.price);

				$("#J_rdsSpName").html(order.space_name);
				$("#J_rdsSpSize").html(order.space_store);
				$("#J_rdsSpCons").html(order.space_conn);
				/*$("#J_rdsSpTime").html(order.space_month);*/
				$("#J_rdsSpState").html(space_state_name);
				$("#J_rdsSpEndTime").html(order.space_end_date);
			}else{
				ALERT.init({info:"数据异常，请稍后再试！"});
			}
		},
		error:function(){
			ALERT.init({info:"数据链接异常，请稍后再试！"});
		}
	});
}

