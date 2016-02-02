<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${ctx}/sys/Css/bootstrap.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/sys/Css/bootstrap-responsive.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/sys/Css/style.css" />
<script type="text/javascript" src="${ctx}/sys/Js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/sys/Js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/sys/Js/ckform.js"></script>
<script type="text/javascript" src="${ctx}/sys/Js/common.js"></script>
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
		var id=$("#mv_id").val();
		$.get("${ctx}/mv/mv_queryMvById",{id:id},function(mv){
			$("#singers").val(mv.singer.name);
			$("#mvName").val(mv.name);
			$("#mv_pic").val(mv.pic_url);
			$("#pic").prop("src","/myMusic/" + mv.pic_url);
			$(".type").each(function(j){
				if($("#type"+j).val()==mv.type){
					$("#type"+j).attr("checked","checked");
				}	
			});
			
			$(".area").each(function(j){
				if($("#area"+j).val()==mv.area){
					$("#area"+j).attr("checked","checked");
				}	
			});
			$("#videourl").val("/myMusic/" +mv.video_url);
			$("#click").val(mv.click);
			$("#price").val(mv.price);
			
			$("#describe").html(mv.describe);
			
		},"json");
		
		
		
		$('#backid').click(function() {
			window.location.href = "index.jsp";
		});
		
		$('#ok').click(function() {
			if($("#singername").val()==0){
				alert("请选择歌手");
				return false;
			};
			
		});
		$('#singername').change(function() {
			var id=$("#singername").val();
			if(id!=0){
			$("#singerid").html(id);
			}
			
		});

		$("#singers").keyup(
				function() {
					singers = $("#singers").val();
					if(singers.trim()==""){
						$("#singername").html("<option value='0'>输入不能为空</option>");
						return false;
					}
					$.get("${ctx}/mv/mv_querySingerByName", {
						name : singers
					}, function(data) {
						var content = "";
						if (data.length > 0) {
							content = "<option value='0'>--请选择--</option>";
							$.each(data, function(i, s) {
								content = content + "<option value="+s.id+">" + s.name
										+ "</option>";
							});
						} else {
							content = "<option value='0'>没有找到先关歌手</option>";
						}
						$("#singername").html(content);
					}, "json");

				});

	});
</script>

</head>
<form action="${ctx}/mv/mv_updateMv" method="post" enctype="multipart/form-data">
<input type="hidden" name="mv.id" id="mv_id" value="${param.id}"/>
<input type="hidden" name="mv.pic_url" id="mv_pic" />
	<table class="table table-bordered table-hover definewidth m10">
		<tr>
			<td class="tableleft">歌手名称</td>
			<td><input type="text" id="singers"  value="" style="width: 100px" /> <select
				name="mv.singer.id" id="singername" style="width: 150px;" >
					<option value="0">请先查询</option>
			</select>
			    歌手ID:<span   id="singerid"></span> 
				</td>
		</tr>
		<tr>
			<td class="tableleft">mv名称</td>
			<td><input type="text"  id="mvName" name="mv.name" /></td>
		</tr>
		<tr>
			<td width="10%" class="tableleft">mv图片</td>
			<td><img  id="pic" alt="没找到图片"  style="width: 100px;height: 50px;">   <input type="file" name="upload" /></td>
		</tr>
		<tr>
			<td class="tableleft">类型</td>
			<td><input type="radio" name="mv.type" class="type" id="type0" value="流行"  /> 流行
				<input type="radio" name="mv.type" class="type" id="type1" value="古典"  /> 古典 
				<input type="radio" name="mv.type" class="type" id="type2" value="摇滚"  /> 摇滚
				<input type="radio" name="mv.type" class="type" id="type3" value="民谣"  /> 民谣
				<input type="radio" name="mv.type" class="type" id="type4" value="其他"  /> 其他
				</td>
		</tr>
		<tr>
			<td class="tableleft">地区</td>
			<td><input type="radio" name="mv.area" class="area" id="area0" value="内地" /> 内地
				<input type="radio" name="mv.area" class="area" id="area1" value="港台" /> 港台 
				<input type="radio" name="mv.area" class="area" id="area2" value="欧美" /> 欧美 
				<input type="radio" name="mv.area" class="area" id="area3" value="日韩" />日韩 
				<input type="radio" name="mv.area" class="area" id="area4" value="其它" /> 其它
			</td>
		</tr>
		<tr>
			<td width="10%" class="tableleft">mv视频</td>
			<td><input type="text" name="mv.video_url" id="videourl"  />   <input type="file" id="upload" name="upload" />
			</td>
		</tr>
		<tr>
			<td class="tableleft">点击量</td>
			<td><input type="text" name="mv.click" id="click"  readonly="readonly" />
			</td>
		</tr>
		<tr>
			<td class="tableleft">价格</td>
			<td><input type="text" id="price" name="mv.price" /></td>
		</tr>
		<tr>
			<td class="tableleft">描述</td>
			<td><textarea rows="5" cols="21" id="describe" name="mv.describe"></textarea></td>
		</tr>
		<tr>
			<td class="tableleft"></td>
			<td>
				<button type="submit" class="btn btn-primary" type="button" id="ok">保存修改</button>&nbsp;&nbsp;
				<button type="button" class="btn btn-success" name="backid"
					id="backid">返回列表</button>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
