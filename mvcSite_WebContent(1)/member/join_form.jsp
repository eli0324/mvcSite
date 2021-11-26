<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%
if (session.getAttribute("memberInfo") != null) {
	out.println("<script>");
	out.println("alert('잘못된 경로로 들어오셨습니다.');");
	out.println("history.back();");
	out.println("</script>");
	out.close();
}

Calendar today = Calendar.getInstance();
int year = today.get(Calendar.YEAR);		// 올 해 연도
int month = today.get(Calendar.MONTH) + 1;	// 현재 월
int day = today.get(Calendar.DATE);			// 오늘 일
int lastDay = today.getActualMaximum(Calendar.DATE);	// today의 말일

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
<script src="../js/date_change.js"></script>
<script src="../js/jquery-3.6.0.js"></script>
<script>
function setDomain(domain) {
	var e3 = document.frmJoin.e3;
	if (domain == "direct") {
		e3.value = "";	e3.focus();
	} else {
		e3.value = domain;
	}
}

function chkDupId(uid) {
	if (uid.length >= 4) {	// 사용자가 입력한 아이디가 4자 이상일 경우
		$.ajax({
			type : "POST", 				// 데이터 전송 방법
			url : "/mvcSite/dupId", 	// 데이터를 받을 서버의 URL로 컨트롤러(서블릿)를 의미
			data : {"uid" : uid}, 		// 지정한 url로 보낼 데이터의 이름 및 값
			success : function(chkRs) {	// 데이터를 보내어 실행한 결과를 chkRs로 받아 옴
				var msg = "";	// 사용자에게 보여줄 메시지를 저장할 변수
				if (chkRs == 0) {	// uid와 동일한 회원 아이디가 없으면(사용할 수 있는 아이디 이면)
					msg = "<span class='fontBlue'>사용하실 수 있는 ID 입니다.</span>";
					$("#idChk").val("y");
				} else {			// uid와 동일한 회원 아이디가 있으면(사용할 수 없는 아이디 이면)
					msg = "<span class='fontRed'>이미 사용중인 ID 입니다. 다시 검색하세요.</span>";
					$("#idChk").val("n");
				}
				document.getElementById("idMsg").innerHTML = msg;
			}
		});
	} else {
		document.getElementById("idMsg").innerHTML = "아이디는 4~20자로 입력하세요.";
		$("#idChk").val("n");
	}
}
</script>
</head>
<body>
<h2>회원 가입 폼</h2>
<form name="frmJoin" action="member_proc.mem" method="post">
<input type="hidden" name="wtype" value="in" />
<input type="hidden" name="idChk" id="idChk" value="n" />
<div id="agreement" style="width:700px; height:100px; overflow:auto;">
약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />
약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />약관 내용<br />
</div>
<p style="width:700px; text-align:center;">
	<input type="radio" name="agree" value="y" id="agreeY" /><label for="agreeY">동의 함</label>
	<input type="radio" name="agree" value="n" id="agreeN" checked="checked" /><label for="agreeN">동의 안함</label>
</p><br />
<table width="700" cellpadding="5" cellspacing="0" id="joinBrd">
<tr>
<th width="20%">아이디</th>
<td width="*">
	<input type="text" name="mi_id" maxlength="20" onkeyup="chkDupId(this.value);" />
	<span id="idMsg" style="font-size:80%;">아이디는 4~20자로 입력하세요.</span>
</td>
</tr>
<tr><th>비밀번호</th><td><input type="password" name="mi_pw" /></td></tr>
<tr><th>비밀번호 확인</th><td><input type="password" name="mi_pw2" /></td></tr>
<tr><th>이름</th><td><input type="text" name="mi_name" /></td></tr>
<tr>
<th>성별</th>
<td>
	<input type="radio" name="mi_gender" value="m" id="genderM" /><label for="genderM">남자</label>
	<input type="radio" name="mi_gender" value="f" id="genderF" checked="checked" /><label for="genderF">여자</label>
</td>
</tr>
<tr>
<th>생년월일</th>
<td>
	<select name="by" onchange="resetday(this.value, this.form.bm.value, this.form.bd);">
<% for (int i = 1950 ; i <= year ; i++) { %>
		<option value="<%=i %>" <% if (i == year) { %>selected="selected"<% } %>><%=i %></option>
<% } %>
	</select>년
	<select name="bm" onchange="resetday(this.form.by.value, this.value, this.form.bd);">
<% for (int i = 1 ; i <= 12 ; i++) { %>
		<option value="<%=i < 10 ? "0" + i : i %>" <% if (i == month) { %>selected="selected"<% } %>><%=i %></option>
<% } %>
	</select>월
	<select name="bd">
<% for (int i = 1 ; i <= lastDay ; i++) { %>
		<option value="<%=i < 10 ? "0" + i : i %>" <% if (i == day) { %>selected="selected"<% } %>><%=i %></option>
<% } %>
	</select>일
</td>
</tr>
<tr>
<th>전화번호</th>
<td>
	<select name="p1">
		<option value="010">010</option>
		<option value="011">011</option>
		<option value="016">016</option>
		<option value="019">019</option>
	</select> -
	<input type="text" name="p2" size="4" maxlength="4" /> -
	<input type="text" name="p3" size="4" maxlength="4" />
</td>
</tr>
<tr>
<th>이메일</th>
<td>
	<input type="text" name="e1" size="10" /> @
	<select name="e2" onchange="setDomain(this.value);">
		<option value="">도메인 선택</option>
<% for (int i = 0 ; i < arrDomain.length ; i++) { %>
		<option value="<%=arrDomain[i] %>"><%=arrDomain[i] %></option>
<% } %>
		<option value="direct">직접 입력</option>
	</select>
	<input type="text" name="e3" size="10" />
</td>
</tr>
<tr>
<th>광고 수신</th>
<td>
	<input type="radio" name="mi_isad" value="y" id="adY" checked="checked" /><label for="adY">수신</label>
	<input type="radio" name="mi_isad" value="n" id="adN" /><label for="adN">미수신</label>
</td>
</tr>
<tr><th>우편번호</th><td><input type="text" name="ma_zip" size="5" maxlength="5" /></td></tr>
<tr>
<th>주소</th>
<td><input type="text" name="ma_addr1" />&nbsp;&nbsp;&nbsp;<input type="text" name="ma_addr2" /></td>
</tr>
</table>
<p style="width:700px; text-align:center;">
	<input type="submit" value="회원 가입" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="reset" value="다시 입력" />
</p>
</form>
<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
</body>
</html>
