	

$(function(){
	//跳转到增加歌手页面
	$("#addSinger").click(function(){
		window.location.href="/myMusic/sys/singer/addSinger.jsp";
	});
	//跳转到编辑界面
	
	//下一页
	$("#nextpage").click(function(){
	    var singerName = $("#singerName").val();
		var nowpage = $("#nowpage").html();
		var totalpage = $("#totalpage").html();
		if(nowpage>(totalpage-1)){
			return false;
		}
		var page = nowpage-1+2; 
		querySingerByName(page,singerName);
					
	});
	//上一页
	$("#backpage").click(function(){
		  var singerName = $("#singerName").val();
		var nowpage = $("#nowpage").html();
		if(nowpage<=1){
			return false;
		}
		querySingerByName(nowpage-1,singerName);
        
	});
	//首页
	$("#firstpage").click(function(){
		var singerName = $("#singerName").val();
		if(nowpage<=1){
			return false;
		}
		querySingerByName(1,singerName);

	});
	//末页
	$("#lastpage").click(function(){
		var nowpage = $("#nowpage").html();
		var totalpage = $("#totalpage").html();
		var singerName = $("#singerName").val();
		if(nowpage>=totalpage){
			return false;
		}
		querySingerByName(totalpage,singerName);
	});
	
	//按输入数字跳转页面
	$("#intoPageButton").click(function(){
		var singerName = $("#singerName").val();
		var totalpage = $("#totalpage").html();
		var pageDatas = $("#intopage").val();
		if((pageDatas<1)||(pageDatas)>totalpage){
			return false;
		}
		querySingerByName(pageDatas,singerName);

		
	});
	//全选，反选
	  $("#all").click( function () {
		  //prop:通过属性取值，或者赋值
		  if($("#all").prop("checked")){
			  
			  $("[class = checkbox]:checkbox").prop("checked", true);
			  
		  }else{
			  $("[class = chosebox]:checkbox").prop("checked", false);

		  }
      });
	  //批量删除
	  var dd;
	  var dds=new Array();
//	  $("#deleteSinger").click(function(){
//		  alert($("#data").val());
//		  dd = JSON.stringify($("#data").val()); 
//		  dds=$("#data").val();
//		  alert(dds[0]);
//		  $($("#data").val()[0]).each(function(i){
//			  alert(i);
//		  });
//		  $.each(dd, function(i,data) {
//			  alert(data);
//		  });
//	  });
	  
	  //删除
	  $("#deleteSinger").click(function(){
		  alert("kkkkkk");
		  alert($("#data").val());
		//window.location.href="/myMusic/singer/deleteSinger";
	  });
	  
	  //第一次请求页面
	  querySingerByName(2,"");
//按文本框条件查询
	  $("#querySinger").click(function(){
		  var singerName = $("#singerName").val();
			alert(singerName);
			querySingerByName(1,singerName);
		});
	
});
//根据姓名查询歌手
function querySingerByName(nowpage,singerName){
	
	$.get("/myMusic/singer/querySingerByNameAndPage",{nowpage:nowpage,name:singerName},function(data){
		var content = "";
		$.each(data,function(i,values){
			
//			IdCol[i] = values.id;
			content +="<tr><td><input type=\"checkbox\" class='checkbox' id="+values.id+"></td>" +
					   "<td>"+values.id+"</td>"+
			           "<td>"+values.name+"</td>"+
			           "<td>"+values.area+"</td>"+
			           "<td style='width:50%'>"+values.intro+"</td>"+
			           "<td><img style ='width:50px;height:50px' src='/myMusic/"+values.pic_url+"'></td>" +
			           "<td><a href='/myMusic/sys/singer/editSinger.jsp?id="+values.id+"'>编辑</a></td></tr>";
		});
		
		$("tbody").html(content);
		$("#nowpage").html(nowpage);
		
	},"json");
	
	$.get("/myMusic/singer/findTotalPage",{name:singerName},function(pageData){		
		$("#totalpage").html(pageData);
	});
	
}
