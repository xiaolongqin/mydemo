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
    function updatescenicimg(obj) {
    	$.post("scenic.do?",{v:"editscenicimg",SCENICIMG_ID:obj,SCENICIMG_DESCRIBE:$("#"+obj).val()},function(date){
			$.each(date, function (i,vdate) {
				$("#"+obj).html(vdate.SCENICIMG_DESCRIBE);
				alert("修改成功");
			});
		},"json");
    };
    $(function () {
    	$("#addscenicimg").click(function(){
    		window.location.href="scenic.do?v=addscenicimg&scenic_id="+$(".scenic_id").val();
    	});
    	
    });
    </script>
</head>
<body>
<form class="form-inline definewidth m20" action="" method="post">
  图片描述：
    <input type="text" name="newstitle" id="scenicname" class="abc input-default" placeholder="" value="">&nbsp;&nbsp;  
    <input type="button" class="btn btn-primary" id="queryscenic" value="查询">&nbsp;&nbsp;
    <input type="button" class="btn btn-success" id="addscenicimg" value="新增图片" >
</form>
<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
    	<th><input type="checkbox" name="all"></th>
        <th>景点名称</th>       
        <th>景点图片</th> 
        <th>图片描述</th>
		<th>图片操作</th>      
    </tr>
    </thead>
	     <tbody class="scenicdata">
	   <c:forEach items="${list }" var="map">
	     <tr>
	    	<th><input type="checkbox" name="${map.SCENICIMG_ID }">
	    	 <input type="hidden" class="scenic_id" value="${map.SCENIC_ID }">
	    	</th>
            <td>${map.name1 }</td>
            <td><img alt="" style="width:600px;height: 400px" src="${pageContext.request.contextPath }/${map.SCENICIMG_URL }"></td>
            <td><textarea rows="20" cols="" id="${map.SCENICIMG_ID }">${map.SCENICIMG_DESCRIBE }</textarea></td>
            <td><br><br>
            <br><br><br>
            <br><br><br>         
            <input type="button" value="提交修改" id="" onclick="updatescenicimg('${map.SCENICIMG_ID }')" class="btn btn-primary"></td>
        </tr>
    </c:forEach>
        </tbody>
</table>

</html>