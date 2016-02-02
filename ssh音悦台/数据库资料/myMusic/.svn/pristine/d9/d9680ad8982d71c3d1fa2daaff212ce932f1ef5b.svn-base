<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html><head>
    <title></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="sys/Css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="sys/Css/bootstrap-responsive.css">
    <link rel="stylesheet" type="text/css" href="sys/Css/style.css">
    <script type="text/javascript" src="sys/Js/jquery.js"></script>
    <script type="text/javascript" src="sys/Js/bootstrap.js"></script>
    <script type="text/javascript" src="sys/Js/ckform.js"></script>
    <script type="text/javascript" src="sys/Js/common.js"></script>

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
    		window.location.href="index.jsp";		
    	});
    	
    });
    </script>
</head>
<body>
	<form action="scenic.do?v=addscenicimgdata" method="post" class="definewidth m20" enctype="multipart/form-data">
		<table class="table table-bordered table-hover definewidth m10">
			<tr>
				<td width="10%" class="tableleft">景点图片</td>
				<td><input type="file" name="SCENICIMG_URL"> 
				<input type="hidden" id="scenic_id" name="SCENIC_ID" value="${scenic_id }">
				</td>
			</tr>
			<tr>
				<td class="tableleft">图片描述</td>
				<td><textarea rows="10" cols="0" name="SCENICIMG_DESCRIBE"></textarea> </td>
			</tr>
			<tr>
				<td class="tableleft"></td>
				<td>
					<input type="submit" class="btn btn-primary" id="addscenic" value="确定添加">
					&nbsp;&nbsp;
					<input type="button" class="btn btn-primary" id="back" value="返回列表">
					&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</form>
</body>
</html>