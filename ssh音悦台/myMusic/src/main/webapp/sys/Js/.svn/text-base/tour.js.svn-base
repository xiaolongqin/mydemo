var dd=new Array();//��ɾ��ʱ�����һ������
$(function() {
	/**
	 * ��ҳ
	 */
	querytour("1","");
    $("#addtour").click(function() {
		window.location.href="addTour.jsp";		
	});
	$("#firstpage").click(function() {
		querytour("1", $("#newstitle").val());
	});
    $("#backpage").click(function() {
    	var pp=$("#pageing").prop("innerHTML");
    	querytour((pp-1),$("#newstitle").val());
	});
    $("#nextpage").click(function() {
    var pp=$("#pageing").prop("innerHTML");
	querytour((pp-1+2),$("#newstitle").val());
   });
    /**
     * ��ѯ
     */
    $("#querytour1").click(function() {
    	querytour("1",$("#newstitle").val() );
    	
    });
    /**
	 * ɾ��
	 */
	
    $("#deletetuijie0").click(function(){
		$(dd).each(function(i){			
			if($("#"+dd[i]).prop("checked")==true){
				
				$.post("../../tourTarento.do",{v:"deletetuser",id:dd[i]},function(){
					
				});
			}
		});
		querytour("1","");
	
	});
});

function querytour(page1,title1) {
	if(page1<1){
		alert("��һҳ");
	}else{
		$.get("../../tourTarento.do",{v:"gettourBytitle",page:page1,title:title1},function(datas){
			var content="";
			$.each(datas, function(i,data) {
				dd[i]=data.RECOMMEND_ID;
				content +="<tr><td><input type=\"checkbox\" class='tt' id="+data.RECOMMEND_ID+"></td>"+
				"<td>"+data.RECOMMEND_TITLE+"</td>"+
				"<td>"+data.USER_ID+"</td>"+
				"<td>"+data.RECOMMEND_DESCRIPTION+"</td>"+
				"<td>"+data.RECOMMEND_TYPE+"</td>"+
				"<td>"+data.RECOMMEND_IMGURL+"</td>"+
				"<td>"+data.RECOMMEND_WORDURL+"</td>"+
				"<td><a href='../../tourTarento.do?v=edittour&RECOMMEND_ID="+data.RECOMMEND_ID+"'>edit</a></td></tr>";
			});
			$(".tourdata").html(content);
			$("#pageing").html(page1);
		},"json");
	}
	
	
	
}
