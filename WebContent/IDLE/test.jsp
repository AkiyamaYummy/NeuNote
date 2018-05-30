<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="JS/cookietool.js"></script>
<script type="text/javascript" src="JS/jquery.js"></script>
</head>
<body>
<form action="NoteEdit" method="post">
<input type="text" name="type">
<input type="text" name="c_code" id="c_code">
<input type="submit" onclick="send();">
<script type="text/javascript">
function send(){
	var c_code = get_cookie("c_code");
	document.getElementById("c_code").value=c_code;
}

</script>
</form>

</body>
</html>