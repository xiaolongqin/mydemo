<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">
var singerarea="";
	$(function() {
		$("#logo").load("logo.jsp");
		$("#floot").load("floot.jsp");
		querySingerByAreaAndName(1,"");
		$("#ssinger").click(function() {
			var xxxz=$("#singernamess").val();
			querySingerByAreaAndName(1,xxxz);
		});
		$(".areabb").each(function(i) {
			$("#areabb"+i).click(function() {
				$(".areabb").each(function(j) {
					if(i!=j){
						$("#areabb"+j).css("background","");
					}
					else {
						$("#areabb"+j).css("background","#ffc");
						if(i!=0){							
						singerarea=$("#areabb"+i).val().substring(0,2);
						querySingerByAreaAndName(1,"");
						$("#singernamess").val("");
						}
						else {
							singerarea="";
							querySingerByAreaAndName(1,"");
							$("#singernamess").val("");
						}
					}
				});
			});
		});
	});
	function querySingerByAreaAndName(page,name) {
		$.get("/myMusic/singer/querySingerByAreaAndName",{nowpage:page,area:singerarea,name:name},function(singers){
			var mvcc="";
			$.each(singers, function(i,singer) {	
				mvcc=mvcc+"<div onclick='clickbyid1("+singer.id+")' id='singer"+i+"' style='font-size: 15px;float: left;width: 10%;margin: 2%;height: 140px;background: #ffc;cursor: pointer;' align='center' ><img src='/myMusic/"+singer.pic_url+"'  style='width: 100%;height: 100px;'><span style='font-weight: bold;'>"+singer.name+"</span></div>";
				
			});
			$("#singerss").html(mvcc);
		},"json");
	}
	function clickbyid1(id) {
		window.location.href = "/myMusic/singer.jsp?id="+id;

	}
</script>
<style type="text/css">
</style>
<title>音悦台--歌手简介 — 郑源</title>
</head>
<body style="background-color: #fefefe; padding: 0">
	<input type="hidden" id="singerid" value="${param.id }">
	<div
		style="width: 100%; position: fixed; top: 0px; left: 0px; z-index: 2;"
		id="logo"></div>
	<div id="singer" 
		style="width: 85%; margin: auto; margin-top: 52px; background: white; border-style: solid; border-width: 1px;">
		<div style="float: right ; width: 79%; height: 100px; border-style: solid; border-width: 1px;border-color: #dcdcdc; margin-top: 20px;">
			<h1>流行音乐人</h1>
			<hr>
			<div style="clear: both;" id="singerss">
				
				
								
			</div>
		</div>
		<div
			style="float:left; ; border-style: solid; border-width: 1px; border-color: #dcdcdc; margin-top: 20px;width: 20%;height: 450px;" >
			
			<h3>音乐人</h3>
			
			<div  style="width: 100%;" >
				<input id="singernamess" type="text" style="width: 70%;"><input id="ssinger" type="button" value="搜索" style="font-weight: bold;">
			</div>
			<hr>
			<div style=" width: 100%" ></div>
			<input id="areabb0" class="areabb" type="button" value="全部音乐人" style="width: 90%;margin: auto; font-size: 20px;margin-top: 10px;font-weight: bolder;background: #ffc ;cursor: pointer;">
			<input id="areabb1" class="areabb" type="button" value="大陆音乐人" style="width: 90%;margin: auto; font-size: 20px;margin-top: 20px;font-weight: bolder;cursor: pointer;">
			<input id="areabb2" class="areabb" type="button" value="港台音乐人" style="width: 90%;margin: auto; font-size: 20px;margin-top: 20px;font-weight: bolder;cursor: pointer;">
			<input id="areabb3" class="areabb" type="button" value="日韩音乐人" style="width: 90%;margin: auto; font-size: 20px;margin-top: 20px;font-weight: bolder;cursor: pointer;">
			<input id="areabb4" class="areabb" type="button" value="欧美音乐人" style="width: 90%;margin: auto; font-size: 20px;margin-top: 20px;font-weight: bolder;cursor: pointer;">
			<input id="areabb5" class="areabb" type="button" value="其他音乐人" style="width: 90%;margin: auto; font-size: 20px;margin-top: 20px;font-weight: bolder;cursor: pointer;">
		</div>
	</div>

	<div id="floot" style="margin-left: 0; clear: both;"></div>
</body>
</html>