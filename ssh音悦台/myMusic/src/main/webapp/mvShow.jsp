<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">
	$(function() {
		$("#logo").load("logo.jsp");
		$("#floot").load("floot.jsp");
		queryMvByname(1,"","");
		$("#mmokd").click(function() {
			queryMvByname(1,$("#singersname").val(),$("#aeradd").val());
		});
	});
	function queryMvByname(page,name,area) {
		$.get("/myMusic/mv/mv_queryMvByAreaAndSinger", {
			page :page,area:area,name:name
		}, function(mvs) {					
			var cc = "";
			$.each(mvs, function(i, v) {
				cc+="<tr><td style='color: olive; font-weight: bold; font-size: 20px; width: 10%'>"+(i+1)+
				"</td><td style='color: green; font-size: 20px; width: 30%;'>"+v.name+"</td><td><span id='singer"+i+"' style='width:20%; cursor: pointer;color: green;'>"+v.singer.name+"</span></td> <td style='color: gray;; font-size: 20px; '>&nbsp;<span id='ss"+i+"' style='cursor: pointer;'>播放</span> &nbsp;&nbsp;&nbsp;<span id='xz"+i+"' style='cursor: pointer;'>下载</span>&nbsp;&nbsp;&nbsp;<span style='cursor: pointer;'>推荐</span></td></tr>";
				
			});
			$("tbody").html(cc);
			$.each(mvs, function(i, v) {
			clickbyid("s"+i,"/myMusic/mvplay.jsp?id="+v.id);
			clickbyid("ss"+i,"/myMusic/mvplay.jsp?id="+v.id);
			clickbyid("xz"+i,"/myMusic/mv/mv_downLoadMv?fileName="+v.video_url);
			clickbyid("singer"+i,"/myMusic/singer.jsp?id="+v.singer.id);
			});
		}, "json");
	}
</script>
<style type="text/css">
</style>
<title>音悦台--mv</title>
</head>
<body style="background-color: #fefefe; padding: 0">
	<div
		style="width: 100%; position: fixed; top: 0px; left: 0px; z-index: 2;"
		id="logo"></div>
	<div id="singer"
		style="width: 85%; margin: auto; margin-top: 52px; background: white; border-style: solid; border-width: 1px;">
		<div style="float: left; width: 100%; height: auto;">
			<h1>MV淘宝</h1>
			地区:<input type="text" id="aeradd" name="area" placeholder="地区：欧美，内地，日韩..."/> 关键词: <input type="text" id="singersname"  placeholder="输入歌手或者歌名" style="width: 100px" /> 
			<button type="button" class="btn btn-primary" type="button" id="mmokd">查询</button>
			<hr/>
			<div>
				<table style="width: 80%;margin: auto;">
				<tbody >
					
            </tbody>
				</table>
			</div>
		</div>
		
	</div>

	<div id="floot" style="clear:left;margin-left: 0;margin-bottom: 0px; "></div>
</body>
</html>