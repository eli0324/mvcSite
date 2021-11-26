<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vo.*" %>
<%@ include file="../_inc/incHead.jsp" %>
<%
ProductInfo pdt = (ProductInfo)request.getAttribute("pdtInfo");
if (pdt == null) {
	out.println("<script>");
	out.println("alert('잘못된 경로로 들어오셨습니다.');");
	out.println("history.back();");
	out.println("</script>");
	out.close();
}
PdtPageInfo pageInfo = (PdtPageInfo)request.getAttribute("pdtPageInfo");
// 검색 조건 등의 정보를 저장하고 있는 인스턴스
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
.optCmb { width:100px; height:25px; }
</style>
<script>
var cnt = 1;	// 상품 개수를 저장한 변수
var total = <%=pdt.getPi_price() %>;	// 상품 가격을 저장한 변수
function changeCnt(op) {
	var obj1 = document.getElementById("cnt");
	var obj2 = document.getElementById("total");
	if (op == "+") {
		cnt = cnt + 1;
	} else {
		if (cnt > 1)	cnt = cnt - 1;
	}
	obj1.innerHTML = cnt;
	obj2.innerHTML = (cnt * total);
	document.frmPdt.occnt.value = cnt;
}

function cartBuy(chk) {
// 장바구니나 바로 구매로 이동시키는 함수로 비로그인 시 로그인 폼으로 이동시켜야 함
<% if (!isLogin) {	// 로그인 전이면(로그인 후 다시 돌아옴) %>
	location.href = "login_form.jsp?url=pdt_view.pdt?<%=request.getQueryString().replace("&", "$") %>";

<% } else {	// 로그인 상태면 해당 위치로 전송시킴 %>
	var lnk = "";
	if (chk == 'c')	lnk = "cart_proc.ord";	// 장바구니 담기일 경우 이동할 경로
	else			lnk = "order_form.ord";	// 바로 구매일 경우 이동할 경로

	var frm = document.frmPdt;
	frm.action = lnk;
	frm.submit();
<% } %>
}
</script>
</head>
<body>
<h2>상품 상세보기 화면</h2>
<table width="800" cellpadding="5">
<tr align="center"><td>
	<!-- 상품 이미지 및 상품 제원 정보 보기 영역 시작 -->
	<table width="100%" cellpadding="5">
	<tr align="center">
	<td width="55%">
		<table width="100%">
		<tr><td align="center" valign="middle">
			<img src="product/pdt_img/<%=pdt.getPi_img1() %>" width="300" height="300" />
		</td></tr>
		<tr><td align="center" valign="middle">
			<img src="product/pdt_img/<%=pdt.getPi_img1() %>" width="50" height="50" />
<% if (pdt.getPi_img2() != null && !pdt.getPi_img2().equals("")) { %>
			<img src="product/pdt_img/<%=pdt.getPi_img2() %>" width="50" height="50" />
<% } %>
<% if (pdt.getPi_img3() != null && !pdt.getPi_img3().equals("")) { %>
			<img src="product/pdt_img/<%=pdt.getPi_img3() %>" width="50" height="50" />
<% } %>
		</td></tr>
		</table>
	</td>
	<td width="*" valign="top">
		<table width="100%" cellpadding="3">
		<tr>
		<th width="25%">분류</th>
		<td width="*"><%=pdt.getPb_name() + " -> " + pdt.getPcs_name() %></td>
		</tr>
		<tr><th>브랜드</th><td><%=pdt.getPb_name() %></td></tr>
		<tr><th>상품명</th><td><%=pdt.getPi_name() %></td></tr>
		<tr><th>판매가</th><td><%=pdt.getPi_price() %></td></tr>
		<tr><th>할인율</th><td><%=pdt.getPi_discount() * 100 %>%</td></tr>
		<tr><th>별점평균</th><td><%=(pdt.getPi_score() == 0.0 ? "별점 없음" : pdt.getPi_score() + "점") %></td></tr>
		<form name="frmPdt" method="post">
		<input type="hidden" name="piid" value="<%=pdt.getPi_id() %>" />
		<input type="hidden" name="cpage" value="<%=pageInfo.getCpage() %>" />
		<input type="hidden" name="psize" value="<%=pageInfo.getPsize() %>" />
		<input type="hidden" name="keyword" value="<%=pageInfo.getKeyword() %>" />
		<input type="hidden" name="bcata" value="<%=pageInfo.getBcata() %>" />
		<input type="hidden" name="scata" value="<%=pageInfo.getScata() %>" />
		<input type="hidden" name="brand" value="<%=pageInfo.getBrand() %>" />
		<input type="hidden" name="sprice" value="<%=pageInfo.getSprice() %>" />
		<input type="hidden" name="eprice" value="<%=pageInfo.getEprice() %>" />
		<input type="hidden" name="sort" value="<%=pageInfo.getSort() %>" />
		<input type="hidden" name="occnt" value="1" />
		<input type="hidden" name="wtype" value="in" />
		<tr>
		<th>수량</th>
		<td>
			<input type="button" value="-" onclick="changeCnt(this.value);" />
			&nbsp;<span id="cnt">1</span>&nbsp;
			<input type="button" value="+" onclick="changeCnt(this.value);" />
		</td>
		</tr>
<%
if (pdt.getPi_option() != null && !pdt.getPi_option().equals("")) {
// 현 상품에 선택할 옵션이 있으면, 예) size,230,235,240,245,250,255,260,265,270:color,black,white,red,blue
	String[] arrOpt = pdt.getPi_option().split(":");	// 옵션을 종류별로 나누어 배열로 생성
	String optName = "";	// 옵션의 이름들을 저장할 변수
	for (int i = 0 ; i < arrOpt.length ; i++) {
		String[] arrTmp = arrOpt[i].split(",");
		out.println("<tr><th>" + arrTmp[0].toUpperCase() + "</th><td>");
		out.println("<select name='opt" + arrTmp[0] + "' class='optCmb'>");
		for (int j = 1 ; j < arrTmp.length ; j++) {
			out.println("<option value='" + arrTmp[j] + "'>" + arrTmp[j] + "</option>");
		}
		out.println("</select></td></tr>");
		optName += "," + arrTmp[0];
	}
	out.println("<input type='hidden' name='optName' value='" + optName.substring(1) + "' />");
}
%>
		<tr><td colspan="2" align="right">총 구매가격 : <span id="total"><%=pdt.getPi_price() %></span></td></tr>
		<tr><td colspan="2" align="center">
			<input type="button" value="장바구니 담기" onclick="cartBuy('c');" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="바로 구매하기" onclick="cartBuy('d');" />
		</td></tr>
		</form>
		</table>
	</td>
	</tr>
	</table>
	<!-- 상품 이미지 및 상품 제원 정보 보기 영역 종료 -->
</td></tr>
</table>
</body>
</html>
