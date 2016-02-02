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
    		window.location.href="sys/scenic/index.jsp";		
    	});
    	$.post("scenic.do",{v:"getScenicById",scenic_id:$("#scenic_id").val()},function(date){
			$.each(date, function (i,vdate) {
				$("#SCENIC_NAME").val(vdate.SCENIC_NAME);
				$("#SCENIC_DESCRIBE").html(vdate.SCENIC_DESCRIBE);
				$("#SCENIC_ADDRESS").val(vdate.SCENIC_ADDRESS);
				$("#SCENIC_VISIT").val(vdate.SCENIC_VISIT);
				$(".SCENIC_TYPE").each(function(j){
					if($("#SCENIC_TYPE"+j).val()==vdate.SCENIC_TYPE){
						$("#SCENIC_TYPE"+j).attr("checked","checked");
					}				
				});
			});
		},"json");
    });
    </script>
</head>
<body>
	<form action="scenic.do?v=editscenicdata" method="post" class="definewidth m20">
	<input type="hidden" name="SCENIC_ID" id="scenic_id" value="${scenic_id }">
		<table class="table table-bordered table-hover definewidth m10">
			<tr>
				<td width="10%" class="tableleft">景点名称</td>
				<td><input type="text" name="SCENIC_NAME" id="SCENIC_NAME" />&nbsp;&nbsp; <span id="isuser" style="color: red;"></span></td>
			</tr>
			<tr>
				<td class="tableleft">景点地址</td>
				<td><input type="text" name="SCENIC_ADDRESS" id="SCENIC_ADDRESS" /></td>
			</tr>
			<tr>
				<td class="tableleft">景点类型</td>
				<td>
				<input type="radio" class="SCENIC_TYPE" name="SCENIC_TYPE" id="SCENIC_TYPE0" value="名山草原" checked="checked"/>名山草原
            	<input type="radio" class="SCENIC_TYPE" name="SCENIC_TYPE" id="SCENIC_TYPE1" value="峡谷湖泊"/> 峡谷湖泊
				<input type="radio" class="SCENIC_TYPE" name="SCENIC_TYPE" id="SCENIC_TYPE2" value="古城古镇"/> 古城古镇
				<input type="radio" class="SCENIC_TYPE" name="SCENIC_TYPE" id="SCENIC_TYPE3" value="故居寺庙"/> 故居寺庙
				<input type="radio" class="SCENIC_TYPE" name="SCENIC_TYPE" id="SCENIC_TYPE4" value="其他"/> 其他
				</td>
			</tr>
			<tr>
				<td class="tableleft">访问量</td>
				<td><input type="text" name="SCENIC_VISIT" id="SCENIC_VISIT" /></td>
			</tr>
			<tr>
				<td class="tableleft">景点描述</td>
				<td><textarea rows="10" cols="0" name="SCENIC_DESCRIBE" id="SCENIC_DESCRIBE"></textarea> </td>
			</tr>
			<tr>
				<td class="tableleft"></td>
				<td>
					<input type="submit" class="btn btn-primary" id="editscenic" value="确定修改">
					&nbsp;&nbsp;
					<input type="button" class="btn btn-primary" id="back" value="返回列表">
					&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</form>
</body>
</html>