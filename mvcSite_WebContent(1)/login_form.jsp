<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*" %>
<%
MemberInfo memberInfo = (MemberInfo)session.getAttribute("memberInfo");
if (memberInfo != null) {
	out.println("<script>");
	out.println("alert('잘못된 경로로 들어오셨습니다.');");
	out.println("history.back();");
	out.println("</script>");
	out.close();
}

request.setCharacterEncoding("utf-8");
String url = request.getParameter("url");

if (url == null)	url = "";
// 로그인 후 이동할 페이지 주소로 없는 경우 빈 문자열로 지정
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>로그인 폼</h2>
<form name="frmLogin" action="login" method="post">
<input type="hidden" name="url" value="<%=url %>" />
<label for="uid">아이디 : </label><input type="text" name="uid" value="test1" /><br />
<label for="pwd">비밀번호 : </label><input type="password" name="pwd" value="1111" /><br />
<input type="submit" value="로그인" />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type="reset" value="다시 입력" />
</form>
</body>
</html>
