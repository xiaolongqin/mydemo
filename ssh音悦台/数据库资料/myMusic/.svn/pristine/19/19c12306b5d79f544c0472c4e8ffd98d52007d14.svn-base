$(function () {
	querscenic("1","");
	$("#nextpage").click(function() {
		var page =($("#pageing").prop("innerHTML"))-1+2;
		querscenic(page,$("#usersname").val());
	});
	$("#backpage").click(function() {
		var page =($("#pageing").prop("innerHTML"))-1;
		querscenic(page,$("#usersname").val());
	});
	$("#firstpage").click(function() {
		querscenic("1",$("#usersname").val());
	});
	$("#queryusers").click(function() {
		querscenic("1",$("#usersname").val());
	});
	$("#adduser").click(function() {
		window.location.href="/myMusic/sys/users/addUser.jsp";		
	});
});
function querscenic(page1,name1) {
	if(page1<1){
		alert("���ǵ�һҳ");
	}
	else {		
		$.post("../../users.do",{v:"getUsersByName",page:page1,name:name1},function(date){
			var content="";
			$.each(date, function (i,vdate) {
				content+="<tr><td><input type='checkbox' class='scenicchick' name="+vdate.USERS_ID+"></td>"+
				"<td>"+vdate.USERS_NAME+"</td>"+
				"<td>"+vdate.USERS_PASS+"</td>"+
				"<td>"+vdate.USERS_NICKNAME+"</td>"+
				"<td>"+vdate.USERS_DESCRIPTION+"</td>"+
				"<td>"+vdate.USERS_HOME+"</td>"+
				"<td>"+vdate.USERS_EXP+"</td>"+
				"<td><a href='../../users.do?v=edituser&USERS_ID="+vdate.USERS_ID+"'>"+vdate.USERS_EDIT+"</a></td></tr>";
			});
			$(".usersdata").html(content);
			$("#pageing").html(page1);
		},"json");
	};
};