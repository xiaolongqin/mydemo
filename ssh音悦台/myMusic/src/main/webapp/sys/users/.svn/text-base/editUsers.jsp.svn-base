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
    $(function () {
    	$("#back").click(function() {
    		window.location.href="/myMusic/sys/users/index.jsp";		
    	});
    	var id=$("#user_id").val();
    	$.get("/myMusic/user/queryUserByID",{id:id},function(date){
    		
			$.each(date, function (i,vdate) {
				
				$("#usernickname").val(vdate.nickname);
				$("#userusername").val(vdate.username);
				$("#useruserpass").val(vdate.userpass);
				$("#userphone").val(vdate.phone);
				$("#useremail").val(vdate.email);
				$("#usermoney").val(vdate.money);
				
			});
			
		},"json");
    });
    </script>
</head>

<body>
	<form action="/myMusic/user/edituser" method="post" class="definewidth m20">
	<input type="hidden" name="user.id" id="user_id" value="${param.id}"/>
		<table class="table table-bordered table-hover definewidth m10">
			<tr>
				<td width="10%" class="tableleft">用户昵称</td>
				<td><input type="text" name="user.nickname" id="usernickname" value="" /></td>
			</tr>
			<tr>
				<td class="tableleft">用户账号</td>
				<td><input type="text" name="user.username" id="userusername" value=""/></td>
			</tr>
			<tr>
				<td class="tableleft">用户密码</td>
				<td><input type="text" name="user.userpass" id="useruserpass" value=""/></td>
			</tr>
			<tr>
				<td class="tableleft">用户电话</td>
				<td><input type="text" name="user.phone" id="userphone" value="" /></td>
			</tr>
			<tr>
				<td class="tableleft">用户邮箱</td>
				<td><input type="text" name="user.email" id="useremail" value="" /></td>
			</tr>
			<tr>
				<td class="tableleft">用户金币</td>
				<td><input type="text" name="user.money" id="usermoney" value="" /></td>	
			</tr>
			<tr>
				<td class="tableleft"></td>
				<td>
					<input type="submit" class="btn btn-primary" id="edituser" value="确定修改">
					&nbsp;&nbsp;
					<input type="button" class="btn btn-primary" id="back" value="返回列表">
					&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</form>
</body>
</html>