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
</head>
<body>
<form class="form-inline definewidth m20" action="" method="post">
<input type="hidden" name="v" value="queryNewsByTitle" >  
 评论内容：
    <input type="text" name="scenery_title" id="scenery_title" class="abc input-default" placeholder="" value="">&nbsp;&nbsp;  
    <input type="button" class="btn btn-primary" id="querypassage" value="查询">
    &nbsp;&nbsp; <input type="button" class="deletepassage" id="deletepassage" value="删除">
</form>
<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
    	<th><input type="checkbox" name="box" class="box" ></th>
        <th>评论内容</th>
        <th>相关Mv</th>
        <th>评论用户</th>
        <th>评论日期</th>     
    </tr>
    </thead>
	     <tbody class="passagesdata">
	     
        </tbody>
</table>
<div class="inline pull-right page">
         <span id="trcf"></span>条记录 &nbsp;&nbsp;<span id="pageing">1</span>/<span id="pages"></span> 页 
          &nbsp;&nbsp;<a id="firstpage">首页</a>
          &nbsp;&nbsp;<a id="backpage">上页</a> 
          &nbsp;&nbsp;<a id="nextpage">下页</a>     
          &nbsp;&nbsp;<a id="trailertpage">尾页</a>    </div>


</html>