<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
 <%@include file="common.jsp"%>
<!DOCTYPE html>
<html><head>
    <title></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${ctx}/sys/Css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/sys/Css/bootstrap-responsive.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/sys/Css/style.css">
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

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }
    </style>
  <script type="text/javascript">
  var dd=new Array();
  $(function() {
	  $("#addnew").click(function() {
			window.location.href = "${ctx}/sys/MV/add.jsp";
		});
	    queryMv(1,"");
		
		$("#cx").click(function() {
			queryMv(1,$("#mvName").val());
		});
		$("#nextpage").click(function() {
			var page =$("#pageing").html();
			page=page-1+2;
			queryMv(page,$("#mvName").val());
			$("#pageing").html(page);
		});
		$("#backpage").click(function() {
			var page =$("#pageing").html();
			if(page<=1){
				return false;
			}
			page=page-1;
			queryMv(page,$("#mvName").val());
			$("#pageing").html(page);
		});
		
		//批量操作
		  $("#all").click( function () {
			  //prop:通过属性取值，或者赋值
			  if($("#all").prop("checked")){
				  $("[class = tt]:checkbox").prop("checked", true);
			  }else{
				  $("[class = tt]:checkbox").prop("checked", false);
			  }
			  $(dd).each(function(i){
				  $("#"+dd[i]).click( function () {
					  if(($("#all").prop("checked"))&&!($("#"+dd[i]).prop("checked"))){
						  $("#all").prop("checked", false);
					  }
				  });

				});
	      });
		//删除
		  $("#del").click(function() {
				 alert("确定删除选中记录吗?");
				 $(dd).each(function(i){
					 if($("#"+dd[i]).prop("checked")){
						 $.get("${ctx}/mv/mv_delMv",{id:dd[i]});
					 }
					});
				 queryMv(1,"");
				 $("#pageing").html(1);

			});
		
		
	});

  function queryMv(page,name) {
		if(page<1){
		return false;
		}else{
			$.get("${ctx}/mv/mv_queryMvByName",{page:page,name:name},function(mv){
				var content="";
				if(mv.length<1){
					$("#pageing").html($("#pageing").html()-1);
					return false;
				}
				
				$.each(mv, function(i,v) {
					dd[i]=v.id;
					content +="<tr> <th style='width: 5%'><input type=\"checkbox\" class='tt' id="+v.id+"></th>"
					+"<th style='width: 10%'>"+v.name+"</th>"
					+"<th style='width: 10%'><img  src=${ctx}/"+v.pic_url+" style='width: 100px;height: 50px;'</th>"
					+"<th style='width: 5%'>"+v.type+"</th>"
					+"<th style='width: 5%'>"+v.area+"</th>"
					+"<th style='width: 5%'>"+v.click+"</th>"
					+"<th style='width: 10%'>"+v.video_url+"</th>"
					+"<th style='width: 10%'>"+v.singer.name+"</th>"
					+"<th style='width: 20%'>"+v.describe.substring(0,25) +"...</th>"
					+"<th style='width: 10%'>"+v.upload_date+"</th>"
					+"<th style='width: 5%'>"+v.price+"</th>"
					+"<th><a href='${ctx}/sys/MV/edit.jsp?id="+v.id+"'>编辑</a></th></tr>";
				});
				$("#trcf").html(mv.length);
				$("tbody").html(content);
			},"json");
			 $("#all").prop("checked", false);//下一页刷新全选按钮
		}
	}
  </script>  
   
    
</head>
<body>
<form class="form-inline definewidth m20" id="photoform"  method="post">
    图片名称：
    <input type="text" name="title" id="mvName" class="abc input-default" placeholder="" value="">&nbsp;&nbsp;  
   <input id="cx" type="button" class="btn btn-primary" value="查询">&nbsp;&nbsp; <button type="button" class="btn btn-success" id="addnew">新增MV</button>&nbsp;&nbsp;
   <input id="del" type="button" class="btn btn-primary" value="删除">
</form>
<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
    	
        <th><input type="checkbox" name="delete" id="all">all</th>
        <th>名称</th>
        <th>图片</th>
		<th>类型</th>
        <th>地区</th>
        <th>点击</th>
        <th>视频url</th>
        <th>歌手</th>
        <th>描述</th>
        <th>上传时间</th>
        <th>价格</th>
        <th>操作</th>
    </tr>
    </thead>
    
	     <tbody>
	     
        </tbody>
</table>
<div class="inline pull-right page">
		<span id="trcf"></span> 条记录&nbsp;&nbsp; 第<span id="pageing">1</span><span
			id="pages"></span> 页 &nbsp;&nbsp;<a id="firstpage">首页</a>
		&nbsp;&nbsp;<a href="#" id="backpage">上一页</a> &nbsp;&nbsp;<a href="#"
			id="nextpage">下一页</a>


	</div>
</body></html>