<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*" %><!-- 로그인 정보를 담은 MemberInfo 인스턴스를 사용하기 위해 import -->
<%
MemberInfo memberInfo = (MemberInfo)session.getAttribute("memberInfo");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<% if (memberInfo == null) { %>
<a href="login_form.jsp">로그인</a><br />
<a href="member/join_form.jsp">회원 가입</a>
<% } else { %> 
<a href="member/update_form.jsp">정보 수정</a>
<a href="logout.jsp">로그 아웃</a>
<% } %>
<hr />
<a href="pdt_list.pdt">상품 목록</a>
<hr />
<% if (memberInfo == null) { %>
<a href="login_form.jsp?url=order/cart_list.ord">장바구니</a>
<% } else { %> 
<a href="cart_list.ord">장바구니</a>
<% } %>
<hr />
</body>
</html>
