function loginIn(){
    var userName=$('#userName').val();
    var password=$('#password').val();
    var str=/^\S+$/;
    if(str.test(userName)&&str.test(password)){
        $.ajax({
            url:'/rds/login',
            type:'post',
            data:{'name':userName,'pass':password},//传当前页数和每一页的数据条数
            dataType:'json',
            success:function(data){
                if(data.state==true){
                    window.location.href="/rds/main";
                }else if(data.state==false){
                    alert(data.msg);
                }
            }
        });
    }else{
        alert('请输入完整的登录信息！');
    }
}