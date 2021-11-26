<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*"%>
<%
MemberInfo memberInfo = (MemberInfo)session.getAttribute("memberInfo");
// 현재 로그인한 회원의 정보들을 추출하도록 MemberInfo형 인스턴스로 세션내의 정보를 생성

String[] phones = memberInfo.getMi_phone().split("-");
String[] emails = memberInfo.getMi_email().split("@");
String[] arrDomain = {"naver.com", "nate.com", "gmail.com", "daum.net", "yahoo.com"};
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
#joinBrd th, #joinBrd td { border-bottom:1px solid black; }
.fontBlue { color:blue; font-weight:bold; }
.fontRed { color:red; font-weight:bold; }
</style>
<script>
function setDomain(domain) {
	var e3 = document.frmUpdate.e3;
	if (domain == "direct") {
		e3.value = "";	e3.focus();
	} else {
		e3.value = domain;
	}
}
</script>
</head>
<body>
<h2>회원 정보 수정 폼</h2>
<form name="frmUpdate" action="member_proc.mem" method="post">
<input type="hidden" name="wtype" value="up" />
<table width="700" cellpadding="5" cellspacing="0" id="joinBrd">
<tr>
<th width="20%">아이디</th><td width="30%"><%=memberInfo.getMi_id() %></td>
<th width="20%">이름</th><td width="30%"><%=memberInfo.getMi_name() %></td>
</tr>
<tr>
<th>성별</th><td><%=(memberInfo.getMi_gender().equals("m") ? "남자" : "여자") %></td>
<th>생년월일</th><td><%=memberInfo.getMi_birth() %></td>
</tr>
<tr>
<th>전화번호</th>
<td colspan="3">
	<select name="p1">
		<option value="010" <% if (phones[0].equals("010")) { %>selected="selected"<% } %>>010</option>
		<option value="011" <% if (phones[0].equals("011")) { %>selected="selected"<% } %>>011</option>
		<option value="016" <% if (phones[0].equals("016")) { %>selected="selected"<% } %>>016</option>
		<option value="019" <% if (phones[0].equals("019")) { %>selected="selected"<% } %>>019</option>
	</select> -
	<input type="text" name="p2" size="4" maxlength="4" value="<%=phones[1] %>" /> -
	<input type="text" name="p3" size="4" maxlength="4" value="<%=phones[2] %>" />
</td>
</tr>
<tr>
<th>이메일</th>
<td colspan="3">
	<input type="text" name="e1" size="10" value="<%=emails[0] %>" /> @
	<select name="e2" onchange="setDomain(this.value);">
		<option value="">도메인 선택</option>
<% for (int i = 0 ; i < arrDomain.length ; i++) { %>
		<option value="<%=arrDomain[i] %>" <% if (emails[1].equals(arrDomain[i])) { %>selected="selected"<% } %>><%=arrDomain[i] %></option>
<% } %>
		<option value="direct">직접 입력</option>
	</select>
	<input type="text" name="e3" size="10" value="<%=emails[1] %>" />
</td>
</tr>
<tr>
<th>광고 수신</th>
<td colspan="3">
	<input type="radio" name="mi_isad" value="y" id="adY" 
		<% if (memberInfo.getMi_isad().equals("y")) { %>checked="checked"<% } %> /><label for="adY">수신</label>
	<input type="radio" name="mi_isad" value="n" id="adN" 
		<% if (memberInfo.getMi_isad().equals("n")) { %>checked="checked"<% } %> /><label for="adN">미수신</label>
</td>
</tr>
</table>
<p style="width:700px; text-align:center;">
	<input type="submit" value="정보 수정" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="reset" value="다시 입력" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="메인 화면" onclick="location.href='../index.jsp';" />
</p>
</form>
<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
</body>
</html>
