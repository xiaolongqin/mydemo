<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>注册页面</title>
<style type="text/css">
#reg {
	width: 440px;
	height: 530px;
	margin: 100px auto;
	text-align: center;
	line-height: 40px;
	border: 1px solid gray;
	font-size: 22px;
	background-image: url("/myMusic/resource/image/zhuce.jpg");
}
#bt{
	width: 80px;
	height: 40px;
	font-size: x-large;
	font-family: fantasy;
}
</style>
</head>
<body>
<form action="/myMusic/user/registerUser" method="post" class="definewidth m20">
	<div>
		<div id="reg">
		 <h1>音悦台注册</h1>
		 	*&nbsp;&nbsp;&nbsp;&nbsp;Emil：<input type="text" name="user.email"><p>
		 	*用&nbsp;户&nbsp;名：<input type="text" name="user.nickname"><p>
		 	*账&nbsp;&nbsp;&nbsp;号:<input type="text" name="user.username"><p>
		 	*密&nbsp;&nbsp;&nbsp;&nbsp;码：<input type="password" name="user.userpass"><p>
		 	*确认密码：<input type="password"><p>
		 	*电&nbsp;&nbsp;&nbsp;&nbsp;话：<input type="text" name="user.phone"><p>
		 	<input type="submit" value="注  册" id="bt">&nbsp;&nbsp;&nbsp;&nbsp;
		 	<input type="button" value="取  消" id="bt">
		</div>
	</div>
	</form>
</body>
</html>