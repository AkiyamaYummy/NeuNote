//{"GPA":3.6218,"college":"软件学院","grade":"2016级","headphoto":"","introduction":"","level":"全日制本科","major":"软件工程","nickname":"张格皓","password":"123456","sex":"男","stu_class":"软件1602","stu_id":"20164929","stu_name":"张格皓"}
function loadUserMessage() {
    var c_code = get_cookie("c_code");
    if(c_code === null){
        location.href = "login.jsp";
    }
    $.ajax({
        type: 'POST',
        dataType: 'text',
        url: "PersonalInfo",
        data:{"c_code":c_code,"type":"get"},
        async:false,
        success:function(data){
            if((data+"").trim() === "You are not logged in."){
                location.href = "login.jsp";
            }else{
                var me = JSON.parse((data+"").trim());
                loadPageUserMessage(me);
            }
        }.bind(this)
    });
}
function saveUserMessage() {
    var c_code = get_cookie("c_code");
    if(c_code === null){
        location.href = "login.jsp";
    }
    if($("#password").val() === "")alert("请输入密码。");
    else if($("#nickname").val() === "")alert("请输入昵称。");
    else if($("#newpassword").val() !== $("#newpasswordre").val())alert("两次输入的新密码不匹配。");
    else $.ajax({
        type: 'POST',
        dataType: 'text',
        url: "PersonalInfo",
        data:{"c_code":c_code,
            "type":"set",
            "nickname":$("#nickname").val(),
            "introduction":$("#introduction").val(),
            "password":$("#password").val(),
            "new_password":$("#newpassword").val(),
            "headphoto":$("#headphoto").val()
        }, async:false,
        success:function(data){
            if((data+"").trim() === "You are not logged in."){
                location.href = "login.jsp";
            }else if((data+"").trim() === "Password error."){
                alert("密码错误！请核对后重新输入。")
            }else{
                var me = JSON.parse((data+"").trim());
                loadPageUserMessage(me);
            }
        }.bind(this)
    });
}
function loadPageUserMessage(me) {
    if(me.nickname === "")me.nickname = me.stu_name;
    $("#nickname").val(me.nickname);
    $("#password").val("");
    $("#newpassword").val("");
    $("#newpasswordre").val("");
    $("#headphoto").val(me.headphoto);
    $("#studentID").val(me.stu_id);
    $("#realname").val(me.stu_name);
    $("#from").val(me.college+" "+me.grade+" "+me.major+" "+me.stu_class);
    $("#gpa").val(me.GPA);
    $("#introduction").val(me.introduction);
}