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
		if ($("#singerid").val().length > 0) {
				$("#attention").click(function() {
					$.get("/myMusic/front/islogin",{},function(user){
					if(user==null){
						login();s
					}
					else{
						$.get("/myMusic/front/queryAttentionBySingerAndUser",{"attention.singer.id":$("#singerid").val()},function(isattention){
							alert(isattention);
							if(isattention){
								$("#attention").click(function() {
									$.get("/myMusic/front/saveAttention",{"attention.singer.id":$("#singerid").val()},function(){
										alert("关注成功！");
										$("#attention").prop("disabled","disabled");
									});
								});
							}
							else{
								$("#attention").prop("disabled","disabled");
							}
						});
					}
					},"json");
				});
			
			$.get("/myMusic/singer/querySingerById", {
				id : $("#singerid").val()
			}, function(singer) {
				$.get("/myMusic/mv/mv_queryMvBySinger", {
					id : singer.id
				}, function(mvs) {					
					var cc = "";
					$.each(mvs, function(i, v) {
						cc+="<tr><td style='color: olive; font-weight: bold; font-size: 20px; width: 10%'>"+(i+1)+
						"</td><td style='color: green; font-size: 20px; width: 60%;'><span id='s"+i+"' style='cursor: pointer;'>"+v.name+
						"</span></td><td style='color: gray;; font-size: 20px; width: 30%'><span id='ss"+i+"' style='cursor: pointer;'>播放</span> &nbsp;&nbsp;&nbsp;<span style='cursor: pointer;'>下载</span>&nbsp;&nbsp;&nbsp;<span style='cursor: pointer;'>推荐</span></td></tr>";
						
					});
					$("tbody").html(cc);
					$.each(mvs, function(i, v) {
					clickbyid("s"+i,"/myMusic/mvplay.jsp?id="+v.id);
					clickbyid("ss"+i,"/myMusic/mvplay.jsp?id="+v.id);
					});
				}, "json");
				$("title").html("音悦台--歌手简介 — " + singer.name);
				$("#singername").html(singer.name);
				$("#singerintro").html(singer.intro);
				$("#singerimg").prop("src", singer.pic_url);

			}, "json");
			
		}
	});
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
		<div style="float: left; width: 75%; height: 100px;">
			<h1><span>Ta的Mv</span><input id="attention" style="font-size: 15px;margin-left: 20px;" type="button" value="关注歌手" > </h1>
			<hr>
			<div>
				<table>
				<tbody>
					<tr>
						<td
							style='color: olive; font-weight: bold; font-size: 20px; width: 10%'>1 </td>
						<td style='color: green; font-size: 20px; width: 60%;'><span
							style="cursor: pointer;">一万个理由</span></td>
						<td style='color: gray;; font-size: 20px; width: 30%'><span
							style='cursor: pointer;'>播放</span> &nbsp;&nbsp;&nbsp;<span
							style='cursor: pointer;'>下载</span>&nbsp;&nbsp;&nbsp;<span
							style='cursor: pointer;'>推荐</span></td>
					</tr>
					<tr>
						<td
							style='color: olive; font-weight: bold; font-size: 20px; width: 10%'>2</td>
						<td style='color: green; font-size: 20px; width: 60%;'><span
							style="cursor: pointer;">包容</span></td>
						<td style='color: gray;; font-size: 20px; width: 30%'><span
							style='cursor: pointer;'>播放</span> &nbsp;&nbsp;&nbsp;<span
							style='cursor: pointer;'>下载</span>&nbsp;&nbsp;&nbsp;<span
							style='cursor: pointer;'>推荐</span></td>
					</tr>
</tbody>
				</table>
			</div>
		</div>
		<div
			style="float: right; border-style: solid; border-width: 1px; border-color: #dcdcdc; margin-top: 20px;">
			<img id="singerimg" alt="" src="/myMusic/images/zy.png"
				style="width: 200px; height: 200px; margin: auto;">
			<h3>歌手简介</h3>
			<div  style="width: 200px;">
				<span id="singername" style="font-size: 15px; font-weight: bolder;">郑源</span> : 
				<span id="singerintro">华语流行男歌手，音乐人，唱作人，词曲创作者，
					当代通俗艺术研究者，华语流行情歌界代表性人物之一，北京戏曲学院音乐教授，毕业于星海音乐学院。2005年发行成名曲《一万个理由》彩铃下载量约一亿两千万余次，相继发行了《为什么相爱的人不能在一起》、《不要在我寂寞的时候说爱我》、《难道爱一个人有错吗》、《爱情里没有谁对谁错》、《包容》、《爱情码头》、《寒江雪》、《爱情路》等作品。</span>
			</div>
		</div>
	</div>

	<div id="floot" style="margin-left: 0; clear: both;"></div>
</body>
</html>