$(function  query(){
        	 
        	 queyrphoto("1");
//        		$("#nextpage").click(function() {
//        			var page =($("#pageing").prop("innerHTML"))-1+2;
//        			queyrphoto(page);
//        		});
        		$("#backpage").click(function() {
        			var page =($("#pageing").prop("innerHTML"))-1;
        			queyrphoto(page);
        		});
        		$("#firstpage").click(function() {
        			queyrphoto("1");
        		});
        		
        		$("#nextpage").click(function() {
        			var page =($("#pageing").prop("innerHTML"))-1+2;
        			queryPhotoByTitle(page,$("#phototitle").val());
        		});
        		$('#addnew').click(function(){

     				window.location.href="add.jsp";
     		    });
        
        	 
        	/*-------------------------按图片title查询----------------------------*/
        		$("#cx").click(function(){
        			$("tbody").empty();
        			var phototitle=$("#phototitle").val();
        			queryPhotoByTitle("1",phototitle);     			
        		
        			
            		//$("#backpage").click(function() {
            			//var page =($("#pageing").prop("innerHTML"))-1;
            			//queryPhotoByTitle(page,phototitle);
            		//});
            		//$("#firstpage").click(function() {
            			//queryPhotoByTitle("1",phototitle);
            		//});
        			
        			
        		});
        		
        		
        		//删除
        		
        		$("#del").click(function(){
        			var i=1;
        			var dc=document.getElementsByName("delcheck");
        			for (i in dc){
        				if(i<dc.length){
        					if (dc[i].checked) { 
        						$.get("sysphoto.do",{v:"delPhotoByID","id":dc[i].value});
        					}
        				}
        			}
        			query();
       		 });
         });
    
         
     /*---------------------xml--------------------------------*/
     	
    	function   queyrphoto(page1){
    		if(page1<1){
    			alert("这是第一页");
    		}else{
    			
       	 $.get("sysphoto.do",{v:"queryAll",page:page1},function(data){
       		 var content="";
       		 $(data).find("photo").each(function(i){
       			var photography_id=$(this).children("photography_id").text();
       			var user_id=$(this).children("user_id").text();
       			var photography_title=$(this).children("photography_title").text();
       			var photography_type=$(this).children("photography_type").text();
       			var photography_uploadingtime=$(this).children("photography_uploadingtime").text();
       			var photography_imgurl=$(this).children("photography_imgurl").text();
       			var photography_tag=$(this).children("photography_tag").text();
       			var photography_imgunscramble=$(this).children("photography_imgunscramble").text();
       			var photography_imgexplain=$(this).children("photography_imgexplain").text();
       			var photography_visit=$(this).children("photography_visit").text();
       			content+="<tr>";
       			content += "<td><input type='checkbox' class='scenicchick' value="+photography_id+"  name='delcheck'></td>";
       			content+="<td>"+photography_id+"</td>";
       			content+="<td>"+user_id+"</td>";
       			content+="<td>"+photography_title+"</td>";
       			content+="<td>"+photography_type+"</td>";
       			content+="<td>"+photography_uploadingtime+"</td>";
       			content+="<td>"+photography_imgurl+"</td>";
       			content+="<td>"+photography_tag+"</td>";
       			content+="<td>"+photography_imgunscramble+"</td>";
       			content+="<td>"+photography_imgexplain+"</td>";
       			content+="<td>"+photography_visit+"</td>";
       			content+="<td><a href='sysphoto.do?v=editPhoto&photography_id="+photography_id+"'> edit </a></td>";
       			content+="</tr>";
       			 
       		 });
       		 $("tbody").append(content);
       		$("#pageing").html(page1);
       	 },"xml");
       	 
       	 
    		}
    		
       	}
     /*----------------------json------------------------------------*/
     
     function queryPhotoByTitle(page1,title){
    	 if(page1<1){
 			alert("这是第一页");
 		}else{
			$.post("sysphoto.do",{v:"queryPhotoByTitle","phototitle":title,"page":page1},function(data){
				var content="";
				$.each(data,function(index,value){
					content += "<tr>";
					content += "<td><input type='checkbox' class='delchick' value="+value.PHOTOGRAPHY_ID+" name='delcheck'></td>";
					content += "<td>"+value.PHOTOGRAPHY_ID+"</td>";
					content += "<td>"+value.USER_ID+"</td>";
					content += "<td>"+value.PHOTOGRAPHY_TITLE+"</td>";
					content += "<td>"+value.PHOTOGRAPHY_TYPE+"</td>";
					content += "<td>"+value.PHOTOGRAPHY_UPLOADINGTIME+"</td>";
					content += "<td>"+value.PHOTOGRAPHY_IMGURL+"</td>";
					content += "<td>"+value.PHOTOGRAPHY_TAG+"</td>";
					content += "<td>"+value.PHOTOGRAPHY_IMGUNSCRAMBLE+"</td>";
					content += "<td>"+value.PHOTOGRAPHY_IMGEXPLAIN+"</td>";
					content += "<td>"+value.PHOTOGRAPHY_VISIT+"</td>";
					content += "<td><a href='sysphoto.do?v=editPhoto&photography_id="+value.PHOTOGRAPHY_ID+"'>edit</a></td>";
					content += "</tr>";
				});
				$("tbody").append(content);
				$("#pageing").html(page1);
			},"json");
			
 		}
    	 
     }
