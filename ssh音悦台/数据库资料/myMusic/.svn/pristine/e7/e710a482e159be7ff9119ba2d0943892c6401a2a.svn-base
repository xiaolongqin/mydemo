<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>后台登录</title>
<script type="text/javascript" src="/myMusic/js/jquery.js"></script>
<style type="text/css">
#loginbox{
    width:400px;
    height:300px;
    margin-left:600px;
    margin-top:300px;
    line-height: 50px;
    margin: 100px auto;
    text-align: center;
    border: 1px solid gray;
    background-image: url("/myMusic/resource/image/login.jpg");
}
#bb,#reg{
	width: 80px;
	height: 40px;
	font-size: x-large;
	font-family: fantasy;
}
</style>
<script type="text/javascript">
$(function(){
	$("#reg").click(function(){
		window.location.href="/myMusic/sysreg.jsp";
	});
});
</script>
</head>
<body>
<form action="/myMusic/admin/login" method="post">
<div id="loginbox">
    <h1>后台登录</h1>
管理员用户名:<input type="text" id="adminname" name="adminname"><br/>
密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:<input type="password" id="adminpassword" name="password"><p>
<input type="submit" value="登录" id="bb">&nbsp;&nbsp;&nbsp;<input type="button" value="注册" id="reg">
</div>
</form>
</body>
</html>