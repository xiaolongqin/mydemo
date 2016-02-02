<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html><head>
    <title></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="../Css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="../Css/bootstrap-responsive.css">
    <link rel="stylesheet" type="text/css" href="../Css/style.css">
    <script type="text/javascript" src="../Js/jquery.js"></script>
    <script type="text/javascript" src="../Js/bootstrap.js"></script>
    <script type="text/javascript" src="../Js/ckform.js"></script>
    <script type="text/javascript" src="../Js/common.js"></script>

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
    		window.location.href="sys/passages/index.jsp";		
    	});
    });
    </script>
</head>
<body>
	<form action="../../syspassages.do?v=addPassage" method="post" class="definewidth m20">
		<table class="table table-bordered table-hover definewidth m10">
			<tr>
				<td width="10%" class="tableleft">文章名称</td>
				<td><input type="text" name="SCENERY_TITLE" value="" />&nbsp;&nbsp; <span id="isuser" style="color: red;"></span></td>
			</tr>
			<tr>
				<td class="tableleft">文章路径</td>
				<td><input type="text" name="SCENERY_WORDURL" value="" /></td>
			</tr>
			<tr>
				<td class="tableleft">文章类型</td>
						    <td><input type="text" name="SCENERY_TYPE"  value="" /></td>
			
			</tr>
			
			<tr>
				<td class="tableleft">文章图片地址</td>
				<td><input type="text" name="SCENERY_IMGURL" value="0" /></td>
			</tr>
			<tr>
				<td class="tableleft">文章描述</td>
				<td><textarea rows="10" cols="0" name="SCENERY_TAG"></textarea> </td>
			</tr>
			<tr>
				<td class="tableleft">访问量</td>
				<td><input type="text" name="SCENERY_VISIT" value="0" /></td>
			</tr>
			
			<tr>
				<td class="tableleft"></td>
				<td>
					<input type="submit" class="btn btn-primary" id="addpassage" value="确定添加">
					&nbsp;&nbsp;
					<input type="button" class="btn btn-primary" id="back" value="返回列表">
					&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</form>
</body>
</html>