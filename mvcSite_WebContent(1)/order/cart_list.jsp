<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="vo.*" %>
<%@ include file="../_inc/incHead.jsp" %>
<%
request.setCharacterEncoding("utf-8");
if (!isLogin) {
	out.println("<script>");
	out.println("alert('로그인 후 사용하세요.');");
	out.println("location.href='login_form.jsp?url=cart_list.ord';");
	out.println("</script>");
	out.close();
}

PdtPageInfo pageInfo = (PdtPageInfo)request.getAttribute("pdtPageInfo");
String args = "?cpage=" + pageInfo.getCpage() + "&psize=" + pageInfo.getPsize();
if (pageInfo.getKeyword() != null && !pageInfo.getKeyword().equals(""))	args += "&keyword=" + pageInfo.getKeyword();
if (pageInfo.getBcata() != null && !pageInfo.getBcata().equals(""))		args += "&bcata=" + pageInfo.getBcata();
if (pageInfo.getScata() != null && !pageInfo.getScata().equals(""))		args += "&scata=" + pageInfo.getScata();
if (pageInfo.getBrand() != null && !pageInfo.getBrand().equals(""))		args += "&barnd=" + pageInfo.getBrand();
if (pageInfo.getSprice() != null && !pageInfo.getSprice().equals(""))	args += "&sprice=" + pageInfo.getSprice();
if (pageInfo.getEprice() != null && !pageInfo.getEprice().equals(""))	args += "&eprice=" + pageInfo.getEprice();
if (pageInfo.getSort() != null && !pageInfo.getSort().equals(""))		args += "&sort=" + pageInfo.getSort();
// [계속 쇼핑]버튼 클릭시 이동할 경로에 붙일 쿼리 스트링 생성

ArrayList<CartInfo> cartList = (ArrayList<CartInfo>)request.getAttribute("cartList");
// 현 화면에서 보여줄 장바구니에 담긴 상품들의 목록
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
#cartTable th { border-bottom:3px black double; }
#cartTable td { border-bottom:1px black solid; }
.optCmb { width:80px; height:22px; margin:2px 0; }
</style>
<script src="/mvcSite/js/jquery-3.6.0.js"></script>
<script>
function chkAll(all) {
	var arrChk = document.frmCart.chk;
	// 폼(frmCart) 안에 chk라는 이름의 컨트롤이 여러 개 있으므로 배열로 받아옴
	for (var i = 1 ; i < arrChk.length ; i++) {
		arrChk[i].checked = all.checked;
	}
}

function chCount(ocidx, occnt) {
// 장바구니 내의 특정 상품 수량을 변경시키는 함수(ocidx : t_order_cart 테이블의 PK로 변경시 where절의 조건으로 사용, occnt : 변경할 수량 값)
	$.ajax({
		type : "POST", 
		url : "/mvcSite/cart_proc.ord<%=args%>", 
		data : {"wtype" : "up", "kind" : "cnt", "ocidx" : ocidx, "occnt" : occnt}, 
		success : function(chkRs) {
			if (chkRs == 0)	alert("선택한 상품 수량 변경에 실패했습니다.\n새로 고침 후 다시 시도해 주십시오.");
			else			location.reload();
		}
	});
}

function chOption(piid, ocidx, optlen) {
// 장바구니 내의 특정 상품의 옵션을 변경시키는 함수
// piid : 상품아이디로 기존의 장바구니내 상품과 비교하기 위한 값
// ocidx : t_order_cart 테이블의 PK로 변경시 where절의 조건으로 사용
// optlen : 옵션의 개수로 변경될 옵션의 값들을 추출하기 위해 필요한 값
	var frm = document.frmCart;
	var cnt = eval("frm.cnt" + ocidx + ".value");	// 옵션이 변경되는 상품의 개수를 저장할 변수
	var opt = "";	// 변경할 옵셥 값(들)을 저장할 변수(230-white...)
	for (var i = 0 ; i < optlen ; i++) {
		opt += "-" + eval("frm.opt" + ocidx + i + ".value");
	}
	opt = opt.substring(1);

	if (opt != "") {
		$.ajax({
			type : "POST", 
			url : "/mvcSite/cart_proc.ord<%=args%>", 
			data : {"wtype" : "up", "kind" : "opt", "ocidx" : ocidx, "piid" : piid, "options" : opt}, 
			success : function(chkRs) {
				if (chkRs == 0)	alert("선택한 상품 삭제에 실패했습니다.\n새로 고침 후 다시 시도해 주십시오.");
				else			location.reload();
			}
		});
	}
}

function getSelectedValues() {
// chk 체크박스들에서 선택한 체크박스의 값들만 추출하여 문자열로 리턴하는 함수(삭제와 구매에서 사용됨)
	var arrChk = document.frmCart.chk;
	// 문서내의 frmCart 폼안에 있는 컨트롤들 중 chk라는 이름을 가진 컨트롤들을 배열로 받아옴(단, chk라는 이름의 컨트롤이 하나 밖에 없으면 배열을 생성되지 않음)
	var idxs = "";	// chk배열에 추출한 값들을 저장할 변수
	for (var i = 1 ; i < arrChk.length ; i++) {
	// 0번 인덱스가 아닌 1번 부터 시작하는 이유는 chk라는 이름의 첫번째 컨트롤이 hidden이므로
		if (arrChk[i].checked)	idxs += "," + arrChk[i].value;
		// 체크된 체크박스의 value들만 추출하여 idxs에 누적 저장
	}

	return idxs.substring(1);
	// idxs 변수에 누적된 문자열의 맨 앞에 있는 구분자(쉼표)를 제거한 후 리턴시킴
}

function callDel(ocidx) {
	var isConfirm = false;
	if (ocidx == 0) {
	// 선택한 상품(들) 삭제시(여러 상품 선택시 여러 개의 oc_idx를 쉼표로 연결하여 문자열 생성, 상품을 하나도 선택하지 않았으면 경고메시지 출력)
		ocidx = getSelectedValues();
		// 삭제하려고 선택한 상품들의 oc_idx값을 받아옴
		if (ocidx != "") {	// 삭제할 상품들의 인덱스가 있으면
			isConfirm = confirm("선택한 상품들을 장바구니에서 삭제하시겠습니까?");
		} else {	// 삭제할 상품들의 인덱스가 없으면(선택하지 않은 경우)
			alert("삭제할 상품을 하나 이상 선택하세요.");
		}
	} else {	// 특정 상품 삭제시
		isConfirm = confirm("해당 상품을 장바구니에서 삭제하시겠습니까?");
	}

	if (isConfirm) {
		$.ajax({
			type : "POST", 
			url : "/mvcSite/cart_proc.ord<%=args%>", 
			data : {"wtype" : "del", "ocidx" : ocidx}, 
			success : function(chkRs) {
				if (chkRs == 0)	alert("선택한 상품 삭제에 실패했습니다.\n새로 고침 후 다시 시도해 주십시오.");
				else			location.reload();
			}
		});
	}
}
</script>
</head>
<body>
<h2>장바구니 화면</h2>
<form name="frmCart" action="order_form.ord" method="post">
<input type="hidden" name="chk" value="" />
<table width="700" cellpadding="5" cellspacing="0" id="cartTable">
<tr>
<th width="5%"><input type="checkbox" name="all" checked="checked" onclick="chkAll(this);" /></th>
<th width="*">상품</th><th width="25%">옵션</th>
<th width="10%">수량</th><th width="10%">가격</th><th width="10%">삭제</th>
</tr>
<%
if (cartList.size() > 0) {	// 장바구니에 상품이 들어 있으면
	int total = 0;	// 총 구매가격을 저장할 변수
	for (int i = 0 ; i < cartList.size() ; i++) {
		CartInfo cart = cartList.get(i);
		String lnk = "<a href='pdt_view.pdt" + args + "&piid=" + cart.getPi_id() + "'>";
		int max = cart.getPi_stock();	// 남은 재고량
		if (max == -1 || max > 100)	max = 100;
		// 재고가 무제한 이거나 100을 넘을 경우 최대값으로 100 지정
%>
<tr align="center">
<td><input type="checkbox" name="chk" value="<%=cart.getOc_idx() %>" checked="checked" /></td>
<td align="left">
	<%=lnk %><img src="/mvcSite/product/pdt_img/<%=cart.getPi_img1() %>" width="50" height="50" align="absmiddle" />
	<%=cart.getPi_name() %></a>
</td>
<td>
<%
		if (cart.getPi_option() != null && !cart.getPi_option().equals("")) {
		// 현 상품에 선택할 옵션이 있으면, 예) size,230,235,240,245,250,255,260,265,270:color,black,white,red,blue
			String[] arrOpt = cart.getPi_option().split(":");	// 옵션을 종류별로 나누어 배열로 생성
			String[] arrChoose = cart.getOc_opt().split("-");	// 사용자가 선택한 옵션을 하이픈을 기준으로 하여 배열로 생성

			for (int k = 0 ; k < arrOpt.length ; k++) {
				String[] arrTmp = arrOpt[k].split(",");			// 특정 옵션 내에서 선택할 수 있는 값들의 배열
				out.println("<select name='opt" + cart.getOc_idx() + k + "' class='optCmb' " + 
				"onchange=\"chOption('" + cart.getPi_id() + "', " + cart.getOc_idx() + ", " + arrOpt.length + ");\">");
				for (int j = 1 ; j < arrTmp.length ; j++) {
					String slt = "";
					if (arrChoose[k].equals(arrTmp[j]))	slt = "selected='selected'";
					out.println("<option value='" + arrTmp[j] + "' " + slt + ">" + arrTmp[j] + "</option>");
				}
				out.println("</select><br />");
			}
		} else {
			out.println("옵션 없음");
		}
%>
</td>
<td>
	<select name="cnt<%=cart.getOc_idx() %>" class='optCmb' onchange="chCount(<%=cart.getOc_idx()%>, this.value);">
<%		for (int j = 1 ; j <= max ; j++) { %>
		<option value="<%=j %>" <% if (j == cart.getOc_cnt()) { %>selected="selected"<% } %>><%=j %></option>
<%		} %>
	</select>
</td>
<td><%=cart.getPi_price() * cart.getOc_cnt() %></td>
<td><input type="button" value="삭제" onclick="callDel(<%=cart.getOc_idx()%>);" /></td>
</tr>
<%
		total += cart.getPi_price() * cart.getOc_cnt();
	}
%>
</table>
<table width="700" cellpadding="15" cellspacing="0">
<tr>
<td width="*">
	<input type="button" value="선택한 상품 구매" />
	<input type="button" value="선택한 상품 삭제" onclick="callDel(0);" />
</td>
<td width="250" align="right">총 구매가격 : <%=total %> 원</td>
</tr>
<tr><td colspan="2" align="center">
	<input type="button" value="전체 구매" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="계속 쇼핑" onclick="location.href='pdt_list.pdt<%=args %>';" />
</td></tr>
<%
} else {	// 장바구니가 비었으면
%>
<tr><td colspan="6" align="center">장바구니가 비었습니다.</td></tr>
<tr><td colspan="6" align="center">
	<input type="button" value="계속 쇼핑" onclick="location.href='pdt_list.pdt<%=args %>';" />
</td></tr>
<%
}
%>
</table>
</form>
</body>
</html>
