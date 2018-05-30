<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8"%>
<%@ page import="databaseTransactionClasses.CheckConnection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>登录-NEUNOTE</title>
    <script type="text/javascript" src="JS/cookietool.js"></script>
    <script type="text/javascript" src="JS/jquery.js"></script>
    <script type="application/javascript"
            src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-MML-AM_CHTML">
    </script>
    <script>
        function loginPageVerify(){
            var c_code = get_cookie("c_code");
            if(c_code === null){
                document.cookie="c_code=<%=CheckConnection.generateC_code()%>";
                c_code = get_cookie("c_code");
            }
            jQuery.ajax({
                type: 'POST',
                dataType: 'text',
                url: "CheckCookies",
                data:{"c_code":c_code},
                async:false,
                success:function(data){
                    if((data+"").trim() === "Verify successed."){
                        location.href = "index.html";
                    } else {
                        document.cookie='c_code=<%=CheckConnection.generateC_code()%>';
                    }
                }.bind(this)
            });
        }
        function submitOnclick(){
            var c_code = get_cookie("c_code");
            var studentId = document.getElementById("studentId").value;
            var password = document.getElementById("password").value;
            $("#loginMask").show();
            $("#loginMessage").show();
            $("#loginMask").unbind("click");
            $("#loginMessage").find("p").html("正在登陆，请稍等。如果您是第一次登陆本系统，可能要等待五十秒左右。");
            jQuery.ajax({
                type: 'POST',
                dataType: 'text',
                url: "CheckCookies",
                data:{"c_code":c_code,"studentId":studentId,"password":password},
                async:false,
                success:function(data){
                    if((data+"").trim() === "Login successed."){
                        location.href = "index.html";
                    } else {
                        /** do something **/
                        $("#loginMessage").find("p").html("登陆失败，请检查您的学号和密码有没有填写正确。");
                        $("#loginMask").bind("click",function () {
                            $("#loginMask").hide();
                            $("#loginMessage").hide();
                        });
                    }
                }.bind(this)
            });
        }
        loginPageVerify()
    </script>
    <link rel="stylesheet" href="CSS/font.css"/>
    <link rel="stylesheet" href="CSS/loginpage.css"/>
    <style>
        #loginMask{
            position: fixed;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            display: none;
            background: rgba(225,225,225,0.2);
        }
        #loginMessage{
            text-align: center;
            background-color: black;
            color: white;
            width: 500px;
            height: 80px;
            margin: auto;
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            border: solid 1px white;
            padding: 5px;
            display: none;
        }
        #loginMessage p{
            display: block;
            text-align: center;
            font-size: 20px;
        }
    </style>
</head>
<body onload="bodyOnload()">
<div id="loginPanel">
    <p>登&nbsp;&nbsp;&nbsp;&nbsp;录</p>
    <div id="items">
        <div class="item">
            <label>学号</label>
            <input type="text" name="studentId" id="studentId"/><br />
        </div>
        <div class="item">
            <label>密码</label>
            <input type="password" name="password" id="password"/><br />
        </div>
        <button onclick="return submitOnclick();">登录</button>
    </div>
</div>
<div id="main-top-panel">
    <div class="main-title">$NEU \infty NOTE$</div>
</div>
<div id="loginMask"></div>
<div id="loginMessage">
    <p></p>
</div>
<script>
    function bodyOnload(){
        MathJax.Hub.Config({
            showProcessingMessages: false,
            messageStyle: "none",
            extensions: ["tex2jax.js"],
            jax: ["input/TeX", "output/HTML-CSS"],
            tex2jax: {
                inlineMath:  [ ["$", "$"] ],
                displayMath: [ ["$$","$$"] ],
                skipTags: ['script', 'noscript', 'style', 'textarea', 'pre','code','a','protect'],
                ignoreClass:"text-flag"
            },
            "HTML-CSS": {
                availableFonts: ["STIX","TeX"],
                showMathMenu: false
            }
        });
    }
</script>
</body>
</html>