<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="IndexPage.css" type="text/css" />
<title>SecuredLogin</title>
</head>
<body>
<h1>Login</h1>
<form id="frm" action="LoginServlet" method="POST">
	Username: &nbsp;
	<input type="text" name="uname" /><br/>
	Password: &nbsp;&nbsp;<input type="password" name="passwd" /><br/>
	<input type="submit" value="Login" />
</form>
</body>
</html>