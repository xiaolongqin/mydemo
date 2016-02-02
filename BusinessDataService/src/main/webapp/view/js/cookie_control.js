
function getCookie(c_name) /*获取cookie*/
{
	if (document.cookie.length>0){                /*如果有cookie*/
		c_start=document.cookie.indexOf(c_name + "=");     /*查询用户名为xx在cookie中的位置，避免其余地方也有类似符号*/
		if (c_start!=-1){                                   /*找到了我要找的cookie*/
			c_start=c_start + c_name.length+1;              /*直接跳到=后面*/
			c_end=document.cookie.indexOf(";",c_start);     /*获取以c_star他开始,";"结尾的位置*/
			if (c_end==-1){c_end=document.cookie.length}	/*c_end就是值得长度*/
			return unescape(document.cookie.substring(c_start,c_end));/*以c_start开始，取c_end长，然后反编码*/
		} 
	}
	return "";
}
function setCookie(c_name,value,expirems){
	var exdate=new Date();                /*设置一个时间对象*/
	exdate.setTime(exdate.getTime()+expirems);  /*设立一个新的时间点*/
	document.cookie=c_name+ "=" +escape(value)+           /*escape函数对value进行编码*/
	((expirems==null) ? "" : "; expires="+exdate.toUTCString());    /*之间用分好隔开  效果就如同“username=id;expires=”*/ 
}
function checkCookie(){
	shuzl=getCookie('shuzl');
	shuzltravel = getCookie('shuzltravel');
	if (shuzl==null || shuzl==""){
	  	if(shuzltravel==null||shuzltravel==""){
	 	document.location.href="advertise.html";
	 	}
	  	else{
	  	}
	  }
}
function checkUserInfo(){
	var my_username = getCookie("username");
	var my_password = getCookie("password");
	$("#username").val(my_username);
	$("#password").val(my_password);
	if(my_username!=null&&my_username!=""&&my_password!=null&&my_password!=""){
		userSubmit();	
	}
}
