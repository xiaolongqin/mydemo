/**
 * 数之云-用户-RDS订单
 * @authors Ye Hui (yehui@unionbigdata.com)
 * @date    2014-12-19 14:49:29
 * @version 1.0
 */
var RDS_AJAX_DATA={
	size:7,//每页数据条数
	number:1,//当前第几页
	pay_state:-1,
	check_state:-1,
	type:-1,
	/*account_id:0,*/
	order_num:"",
	account_email:"",
	account_name:""
}
$(function(){
	getAllOrder();
});
/**
 * 获得所有订单
 */
function getAllOrder(number){
	$("#J_comPage").html("");
	if(number) RDS_AJAX_DATA.number=number;
	var rdsTbBody=$("#J_rdsTbBody");
	$.ajax({
		url:AJAXURL.getAllOrder,
		data:RDS_AJAX_DATA,
		type:"POST",
		dataType:"JSON",
		beforeSend:function(){
			rdsTbBody.html('<i class="load-one">拼命加载中...</i>');
		},
		success:function(json){
			if(json.state){
				rdsTbBody.html(createOrderListHtml(json.data.list));
				if(json.data.list.length > 0){
					new PAGE("J_comPage",RDS_AJAX_DATA.number,json.data.totalPage,getAllOrder);
				}
			}else{
				rdsTbBody.html('数据异常，请稍后<a href="javascript:;" onclick="location.reload();return false;">重试</a>');
			}
		},
		error:function(){
			rdsTbBody.html('数据链接异常，请稍后<a href="javascript:;" onclick="location.reload();return false;">重试</a>');
		}
	});
}
/**
 * 创建订单列表HTML
 */
function createOrderListHtml(data){
	if(data){
		var html=[],len=data.length;
		if(len>0){
			for(var i=0;i<len;i++){
				var order     =data[i],
					typename  =getOrderType(order.type),
					pay_state =getPayStateName(order.pay_state);
				html.push('<div class="panel-list panel panel-default">');
					html.push('<div class="panel-heading">');
						html.push('<h3 class="pull-left panel-title">订单号：<span>'+order.order_num+'</span>  <a class="more" href="rdsorderdetail?orderid='+order.order_num+'" target="_blank" title="详情>>">详情>></a></h3>');
				  	html.push('</div>');
				  	html.push('<div class="panel-body">');
				    	html.push('<table class="tb-bd table" width="100%">');
							html.push('<tr>');
								html.push('<td width="18%">'+order.space_name+'</td>');
								html.push('<td width="22%"><div class="line">'+order.account_email+'</div></td>');
								html.push('<td width="15%"><div class="line">'+order.date+'</div></td>');
								html.push('<td width="10%"><div class="line">'+typename+'</div></td>');
								html.push('<td width="10%"><div class="line">'+pay_state+'</div></td>');
								html.push('<td width="10%"><div class="line price">￥'+order.price+'</div></td>');
								html.push('<td><div class="line">');
									if(order.check_state==0){
										html.push('<a href="javascript:;" onclick="denyOrder(\''+order.order_num+'\');return false;">取消</a>');
									}else{
										var check_state_name=getOrderCheckState(order.check_state);
										html.push(check_state_name);
									}
								html.push('</div></td>');
							html.push('</tr>');
						html.push('</table>');
				  	html.push('</div>');
				html.push('</div>');
			}	
		}else{
			html.push('<div class="tc mt20">没有查找到相关条件的结果</div>');
		}
		return html.join("");
	}
}
/**
 * 订单状态改变
 */
function orderStateChange(_state){
	RDS_AJAX_DATA.pay_state=_state;
	getAllOrder();
}
/**
 * 订单审核改变
 */
function orderCheckChange(_state){
	RDS_AJAX_DATA.check_state=_state;
	getAllOrder();
}
/**
 * 拒绝订单
 */
function denyOrder(orderId){
	if(orderId){
		ALERT.init({
			info:"确定拒绝吗？",
			callback:function(){
				$.ajax({
					url:AJAXURL.refuseOrder,
					data:{"order_num":orderId},
					type:"POST",
					dataType:"JSON",
					success:function(json){
						if(json.state){
							location.reload();
						}else{
							alert("操作失败，请稍后再试");
						}
					},
					error:function(){
						alert("数据链接异常，请稍后再试");
					}
				});
			}
		});
	}
}
