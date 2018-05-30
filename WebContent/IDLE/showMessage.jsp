<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="userTransactionClasses.getStudentMessage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>学生全部信息</title>
</head>
<body>
	<textarea style="height:100%;width:100%;" disabled="disabled"
	 rows="100"><%=getStudentMessage.get(
					request.getParameter("studentId"),
					request.getParameter("password"))
		%>
	</textarea>
</body>
</html>