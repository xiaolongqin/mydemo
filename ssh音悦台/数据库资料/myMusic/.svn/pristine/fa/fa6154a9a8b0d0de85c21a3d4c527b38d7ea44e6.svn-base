var cc=new Array();
$(function () {
	querscenic("1","");
	$("#nextpage").click(function() {
		var page =($("#pageing").prop("innerHTML"))-1+2;
		querscenic(page,$("#scenicname").val());
	});
	$("#backpage").click(function() {
		var page =($("#pageing").prop("innerHTML"))-1;
		querscenic(page,$("#scenicname").val());
	});
	$("#firstpage").click(function() {
		querscenic("1",$("#scenicname").val());
	});
	$("#queryscenic").click(function() {
		$(".scenicdata").empty();
		querscenic("1",$("#scenicname").val());
	});
	$("#addscenic").click(function() {
		window.location.href="addScenic.jsp";		
	});
	$("#deletescenic").click(function() {
		$(".scenicchick").each(function (i) {
			if($("#"+cc[i]).prop("checked")==true){
				$.post("../../scenic.do",{v:"deleteScenicById",id:cc[i]},function(){
					
				});
			}
		});	
		$(".scenicdata").empty();
		querscenic("1","");
	});
	$("#all").click(function() {
		if($("#all").prop("checked")==true){
			$(".scenicchick").each(function (i) {
				$("#"+cc[i]).prop("checked","true");
			});
		}
		if($("#all").prop("checked")==false){
			$(".scenicchick").each(function (j) {
				$("#"+cc[j]).prop("checked",false);
			});
		}		
	});
});

function querscenic(page1,name1) {
	if(page1<1){
		alert("这是第一页");
	}
	else {		
		$.post("../../scenic.do",{v:"getScenicByName",page:page1,name:name1},function(date){
			var content="";
			$.each(date, function (i,vdate) {
				cc[((page1-1)*6)+i]=vdate.SCENIC_ID;
				content+="<tr><td><input type='checkbox' class='scenicchick' id="+vdate.SCENIC_ID+"></td>"+
				"<td>"+vdate.SCENIC_NAME+"</td>"+
				"<td>"+vdate.SCENIC_DESCRIBE+"</td>"+
				"<td>"+vdate.SCENIC_ADDRESS+"</td>"+
				"<td>"+vdate.SCENIC_TYPE+"</td>"+
				"<td><a href='../../scenic.do?v=queryimg&page=1&scenic_id="+vdate.SCENIC_ID+"'>"+vdate.SCENIC_IMG+"</a></td>"+
				"<td>"+vdate.SCENIC_VISIT+"</td>"+
				"<td><a href='../../scenic.do?v=editscenic&scenic_id="+vdate.SCENIC_ID+"'>"+vdate.SCENIC_EDIT+"</a></td></tr>";
			});
			$(".scenicdata").append(content);
			$("#pageing").html(page1);
		},"json");
	};
};