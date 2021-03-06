<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html><head>
    <title></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="/myMusic/sys/Css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/myMusic/sys/Css/bootstrap-responsive.css">
    <link rel="stylesheet" type="text/css" href="/myMusic/sys/Css/style.css">
    <script type="text/javascript" src="/myMusic/sys/Js/jquery.js"></script>
    <script type="text/javascript" src="/myMusic/sys/Js/bootstrap.js"></script>
    <script type="text/javascript" src="/myMusic/sys/Js/ckform.js"></script>
    <script type="text/javascript" src="/myMusic/sys/Js/common.js"></script>
    <script type="text/javascript" src="/myMusic/sys/Js/users.js"></script>

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
    //查询
    var dbb=new Array();
    function queryRecommend(page,name) {
		if(page<1){
		return false;
		}else{
			$.get("/myMusic/user/queryUserBynickname",{page:page,name:name},function(users){
				var content="";
				if(users.length<1){
					$("#pageing").html($("#pageing").html()-1);
					return false;
				}
				$.each(users, function(i,user) {
					dbb[i]=user.id;
					content +="<tr><td><input type=\"checkbox\" class='tt' id="+user.id+"></td>"+
					"<td>"+user.nickname+"</td>"+
					"<td>"+user.username+"</td>"+
					"<td>"+user.userpass+"</td>"+
					"<td>"+user.phone+"</td>"+
					"<td>"+user.email+"</td>"+
					"<td>"+user.money+"</td>"+
					"<th><a href='/myMusic/sys/users/editUsers.jsp?id="+user.id+"'>编辑</a></th></tr>";
				});
				//$("#trcf").html(users.length);
				$("tbody").html(content);
			},"json");
			 $("#all").prop("checked", false);
		}
		
	}
    //分页
    $(function() {
    	queryRecommend(1,"");
    	$("#queryusers").click(function(){
    		queryRecommend(1,$("#usersname").val());
    	});
    	//首页
    	$("#firstpage").click(function(){
    		queryRecommend(1,$("#usersname").val());
    		$("#pageing").html(1);
    	});
    	//上一页
    	$("#backpage").click(function() {
			var page =$("#pageing").html();
			if(page<=1){
				return false;
			}
			page=page-1;
			queryRecommend(page,$("#usersname").val());
			$("#pageing").html(page);
		});
    	//下一页
    	$("#nextpage").click(function(){
    		var page =$("#pageing").html();
			page=page-1+2;
			queryRecommend(page,$("#usersname").val());
			$("#pageing").html(page);

    	});
    	//删除
    	$("#deleteuser").click(function(){
    		$(dbb).each(function(i){
    			if($("#"+dbb[i]).prop("checked")){//如果选中要做的操作
    				$.get("/myMusic/user/deleteuser",{id:dbb[i]});
    			}
    		});
    		queryRecommend(1,"");
    		$("#pageing").html(1);
    	});
	})
    </script>
</head>
<body>
<form class="form-inline definewidth m20" action="news.do" method="post">
<input type="hidden" name="v" value="queryNewsByTitle" >  
   用户名称：
    <input type="text" name="usersname" id="usersname" class="abc input-default" placeholder="" value="">&nbsp;&nbsp;  
    <input type="button" class="btn btn-primary" id="queryusers" value="查询">
    &nbsp;&nbsp; <input type="button" class="btn btn-success" id="adduser" value="新增用户">
    &nbsp;&nbsp; <input type="button" class="btn btn-success" id="deleteuser" value="删除">
  
</form>
<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
    	<th><input type="checkbox" name="all"></th>
        <th>用户昵称</th>
        <th>用户账号</th>
        <th>用户密码</th>
        <th>用户电话</th>
        <th>用户邮箱</th>
		<th>用户金币</th> 
		<th>用户操作</th>      	
    </tr>
    </thead>
	 <tbody></tbody>
</table>
<div class="inline pull-right page">
         <span id="trcf"></span>5条记录 &nbsp;&nbsp;<span id="pageing">1</span>/<span id="pages"></span> 页 
          &nbsp;&nbsp;<a id="firstpage">首页</a>
          &nbsp;&nbsp;<a id="backpage">上页</a> 
          &nbsp;&nbsp;<a id="nextpage">下页</a>     
            </div>


</html>