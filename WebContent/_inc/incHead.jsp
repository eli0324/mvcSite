<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*" %>
<%
request.setCharacterEncoding("utf-8");
MemberInfo memberInfo = (MemberInfo)session.getAttribute("memberInfo");
boolean isLogin = false;
if (memberInfo != null) isLogin = true;
%>
<a href="/mvcSite/">인덱스</a>
<style>
a:link { text-decoration:none; color:black;}
a:visited { text-decoration:none; color:black;}
a:hover { text-decoration:underline; color:red;}
</style>

