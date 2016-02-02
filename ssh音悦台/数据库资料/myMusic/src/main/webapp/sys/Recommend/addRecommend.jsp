<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="/myMusic/sys/Css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="/myMusic/sys/Css/bootstrap-responsive.css">
<link rel="stylesheet" type="text/css" href="/myMusic/sys/Css/style.css">
<script type="text/javascript" src="/myMusic/sys/Js/jquery.js"></script>
<script type="text/javascript" src="/myMusic/sys/Js/bootstrap.js"></script>
<script type="text/javascript" src="/myMusic/sys/Js/ckform.js"></script>
<script type="text/javascript" src="/myMusic/sys/Js/common.js"></script>

<style type="text/css">
body {
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}

@media ( max-width : 980px) {
	/* Enable use of floated navbar text */
	.navbar-text.pull-right {
		float: none;
		padding-left: 5px;
		padding-right: 5px;
	}
}
</style>
<script type="text/javascript">
	$(function() {
		$('#period').change(function() {
			var id=$("#period").val();
			var period= $("#mvperiods").val();
			if(id==0){
				period=period-1;
			}
			else{
				period=period-1+2;
			}
			$("#mvperiods").val(period);
			
		});
		$("#querymv").click(function() {
			var mvname=$("#name").val();
				if(mvname.trim()==""){
					$("#mvname").html("<option value='0'>--不能为空--</option>");
					return false;
				}
				$.get("/myMusic/recommend/queryMvByName", {
					name:mvname
				}, function(data) {
					var cc = "";
					if (data.length > 0) {
						cc = "<option value='0'>--请选择--</option>";
						$.each(data, function(i, v) {
							cc = cc + "<option value="+v.id+">" + v.name
									+ "</option>";
						});
					} else {
						cc = "<option value='0'>--没有找到--</option>";
					}
					$("#mvname").html(cc);
				}, "json");

		});
	});
</script>
</head>
<body>
	<form action="/myMusic/recommend/addRecommend" method="post"
		class="definewidth m20">
		<table class="table table-bordered table-hover definewidth m10">
			<tr>
				<td width="10%" class="recommend">推荐期数</td>
				<td><input type="text" id="mvperiods" style="width: 100px;"
					name="recommend.periods" value="1" readonly="readonly" /> <select
					style="width: 100px;" id="period">
						<option value="0">当前期</option>
						<option value="1">下一期</option>
				</select></td>
			</tr>
			<tr>
				<td class="recommend">查询Mv</td>
				<td><input id="name" type="text" style="width: 150px;"> <input id="querymv"
					class="btn btn-success" type="button" value="查询"><span id="ts"></span>  </td>
			</tr>
			<tr>
				<td class="recommend">推荐Mv</td>
				<td><select id="mvname" name="recommend.mv.id" ><option value="0">--先查询，再选择--</option>
				</select></td>
			</tr>
			<tr>
				<td class="tableleft"></td>
				<td>
					<button type="submit" class="btn btn-primary" type="button">保存</button>
					&nbsp;&nbsp;
					<button type="button" class="btn btn-success" name="backid"
						id="backid">返回列表</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
