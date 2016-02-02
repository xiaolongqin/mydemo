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
    		window.location.href="sys/passages/index.jsp";		
    	});
    	
    	$.get("syspassages.do",{v:"getPassageById",scenery_id:$("#scenery_id").val()},function(date){
    		$.each(date,function(i,map){
    			$("#SCENERY_TITLE").val(map.SCENERY_TITLE);
    			$("#SCENERY_WORDURL").val(map.SCENERY_WORDURL);
    			$("#SCENERY_TYPE").val(map.SCENERY_TYPE);
    			$("#SCENERY_IMGURL").val(map.SCENERY_IMGURL);
    			$("#SCENERY_TAG").html(map.SCENERY_TAG);
    			$("#SCENERY_VISIT").val(map.SCENERY_VISIT);
    		});
    	},"json");
    });
    </script>
</head>
<body>
	<form action="syspassages.do?v=editPassagedate" method="post" class="definewidth m20">
	<input type="hidden" name="SCENERY_ID" id="scenery_id" value="${scenery_id }">
		<table class="table table-bordered table-hover definewidth m10">
			<tr>
				<td width="10%" class="tableleft">文章名称</td>
				<td><input type="text" name="SCENERY_TITLE"  id="SCENERY_TITLE"/>&nbsp;&nbsp; <span id="isuser" style="color: red;"></span></td>
			</tr>
			<tr>
				<td class="tableleft">文章地址</td>
				<td><input type="text" name="SCENERY_WORDURL"  id="SCENERY_WORDURL" /></td>
			</tr>
			<tr>
				<td class="tableleft">文章类型</td>
			    <td><input type="text" name="SCENERY_TYPE"  id="SCENERY_TYPE"/></td>
			</tr>
			
			<tr>
				<td class="tableleft">文章图片地址</td>
				<td><input type="text" name="SCENERY_IMGURL"  id="SCENERY_IMGURL"/></td>
			</tr>
			<tr>
				<td class="tableleft">文章描述</td>
				<td><textarea rows="10" cols="0" name="SCENERY_TAG" id="SCENERY_TAG"></textarea> </td>
			</tr>
			
			<tr>
				<td class="tableleft">访问量</td>
				<td><input type="text" name="SCENERY_VISIT"  id="SCENERY_VISIT" /></td>
			</tr>
			
			<tr>
				<td class="tableleft"></td>
				<td>
					<input type="submit" class="btn btn-primary" id="editpassage" value="确定修改">
					&nbsp;&nbsp;
					<input type="button" class="btn btn-primary" id="back" value="返回列表">
					&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</form>
</body>
</html>