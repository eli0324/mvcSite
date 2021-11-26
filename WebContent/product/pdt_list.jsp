<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="vo.*" %>
<%@ include file="../_inc/incHead.jsp" %>
<%
request.setCharacterEncoding("utf-8");

ArrayList<ProductInfo> pdtList = (ArrayList<ProductInfo>)request.getAttribute("pdtList");
ArrayList<PdtCataBig> cataBigList = (ArrayList<PdtCataBig>)request.getAttribute("cataBigList");
ArrayList<PdtCataSmall> cataSmallList = (ArrayList<PdtCataSmall>)request.getAttribute("cataSmallList");
ArrayList<PdtBrandInfo> brandInfoList = (ArrayList<PdtBrandInfo>)request.getAttribute("brandInfoList");
PdtPageInfo pdtPageInfo = (PdtPageInfo)request.getAttribute("pdtPageInfo");

String args = "", schargs = "";
// 검색관련 쿼리스트링 제작
if (pdtPageInfo.getKeyword() != null && !pdtPageInfo.getKeyword().equals(""))   schargs += "&keyword=" +pdtPageInfo.getKeyword();

if (pdtPageInfo.getBcata() != null && !pdtPageInfo.getBcata().equals(""))      schargs += "&bcata=" + pdtPageInfo.getBcata();

if (pdtPageInfo.getScata() != null && !pdtPageInfo.getScata().equals(""))      schargs += "&scata=" + pdtPageInfo.getScata();

if (pdtPageInfo.getBrand() != null && !pdtPageInfo.getBrand().equals(""))      schargs += "&brand=" + pdtPageInfo.getBrand();

if (pdtPageInfo.getSprice() != null && !pdtPageInfo.getSprice().equals(""))      schargs += "&sprice=" + pdtPageInfo.getSprice();

if (pdtPageInfo.getEprice() != null && !pdtPageInfo.getEprice().equals(""))      schargs += "&eprice=" + pdtPageInfo.getEprice();

args = "?cpage=" + pdtPageInfo.getCpage() + schargs;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
.bold {font-weight:bold;}
</style>
<script>
<%
String cbid = "", arrName = "";   
int k = 0;
for (PdtCataSmall cata : cataSmallList) {
   if (!cbid.equals(cata.getPcb_id())) {
   // 이전 대분류와 현재 대분류가 다르면(소분류의 소속 대분류가 변경되었으면 : 다른 소분류 배열을 생성해야 한다는 의미)
      cbid = cata.getPcb_id();
      arrName = "arr" + cbid;      // 해당 대분류에 소속된 소분류 목록을 저장할 배열 이름
      out.println("var " + arrName + " = new Array();");   // 소분류 배열 생성
      out.println(arrName + "[0] = new Option(\"\", \"전체 소분류\");");   // 소분류 배열의 첫번째 데이터 생성
      k = 1;   // 배열의 인덱스 번호로 사용할 값을 지정
   }
   out.println(arrName + "[" + k + "] = new Option(\"" + cata.getPcs_id() + "\", \"" + cata.getPcs_name() + "\");");
   k++;
}
%>

      function setCategory(x, target) {
         // x: 대분류에서 선택한 값   /   traget : 선택한 대분류에 따라 보여줄 소분류 콤보박스 객체
            // 1. 원래 소분류 콤보박스에 있던 데이터(option 태그)들을 모두 삭제
            for (var i = target.options.length - 1; i > 0 ; i-- ) {
               target.options[i] = null;
            }
         // 2. 선택한 대분류에 속하는 소분류 데이터를 소분류 콤보박스에 넣음
         if (x != "") {   // 특정 대분류를 선택했으면
            var arr =eval("arr" + x);
            // 대분류에서 선택한 값에 따라 사용할 배열을 지정 : 소분류 콤보박스에서 보여줄 option 태그들

            for (var i = 0; i <arr.length ; i++ ) {
               target.options[i] = new Option(arr[i].value, arr[i].text)
               // 지정한 arr 배열만큼 target에 option 요소 추가
            }

            target.options[0].selected = true;
            // target의 0번 인덱스에 해당하는 option 태그를 선택한 상태로 만듦
         }
      }
   </script>
</head>
<body>
<h2>상품 목록</h2>
<!-- 상품 검색(대분류, 소분류, 검색어, 브랜드, 가격대) 폼 시작 -->
<form name="frmSch" method="get">
<!-- 상품에 대한 검색 조건을 선택 및 입력하는 폼(검색조건 등을 쿼리스트링으로 가지고 다녀야 하므로 전송방식은 get으로 설정 -->
<input type="hidden" name="sort" value="<%=pdtPageInfo.getSort() %>" />
<table width="800" cellpadding="5">
<tr>
<th width="10%">분류</th>
<td width="40%">
   <select name="bcata" onchange="setCategory(this.value, this.form.scata);">
      <option value="">전체 대분류</option>
<% for(PdtCataBig cata : cataBigList) { %>
      <option value="<%=cata.getPcb_id() %>"
      <%if (cata.getPcb_id().equals(pdtPageInfo.getBcata())) { %>selected="selected"<% } %>><%=cata.getPcb_name() %></option>
<% } %>
   </select>
   <select name="scata">
      <option value="">전체 소분류</option>
<%
if (pdtPageInfo.getBcata() != null && !pdtPageInfo.getBcata().equals("")) {
// 검색조건으로 대분류가 존재하면 (해당 대분류에 소속된 소분류의 목록을 보여줌)   
   for (PdtCataSmall cata : cataSmallList) {
      if (pdtPageInfo.getBcata().equals(cata.getPcb_id())) {
      // 검색 조건이 대분유롸 동일한 대분류를 가진 소분류일 경우   
%>
      <option value="<%=cata.getPcs_id() %>"
      <%if (cata.getPcs_id().equals(pdtPageInfo.getScata())) { %>selected="selected"<% } %>><%=cata.getPcs_name() %></option>
<%
      }
   }
}
%>
   </select>
</td>
<th width="10%">브랜드</th>
<td width="40%">
   <select name="bcata">
      <option value="">전체 브랜드</option>
<% for(PdtBrandInfo br : brandInfoList) { %>
   <option value="<%=br.getPb_id() %>"<% if (br.getPb_id().equals(pdtPageInfo.getBrand())) { %>selected="selected"<% } %>><%=br.getPb_name() %></option>
<% } %>
   </select>
</td>
</tr>
<tr>
<th>상품명</th>
<td><input type="text" name="keyword" value="<%=pdtPageInfo.getKeyword() %>" /></td>
<th>가격대</th>
<td>
   <input type="text" name="sprice" value="<%=pdtPageInfo.getSprice() %>" size="5" />원~
   <input type="text" name="eprice" value="<%=pdtPageInfo.getEprice() %>" size="5" />원
</td>
</tr>
<tr><td colspan="4" align="center" style="border-bottom:1px solid black;">
   <input type="submit" value="상품 검색" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <input type="reset" value="조건 초기화" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <input type="button" value="전체 검색" onclick="location.href='pdt_list.pdt';" /><br />
</td></tr>
</table>
</form>
<!-- 상품 검색 폼 종료 -->

<p style="width:800px;" align="right">정렬조건 :
   <a href="pdt_list.pdt<%=args%>&sort=idd" <%=(pdtPageInfo.getSort().equals("idd") ? "class='bold'" : "") %>>신상품순</a>&nbsp;
   <a href="pdt_list.pdt<%=args%>&sort=salecntd" <%=(pdtPageInfo.getSort().equals("salecntd") ? "class='bold'" : "") %>>판매량 수</a>&nbsp;
   <a href="pdt_list.pdt<%=args%>&sort=pricea" <%=(pdtPageInfo.getSort().equals("pricea") ? "class='bold'" : "") %>>낮은 가격순</a>&nbsp;
   <a href="pdt_list.pdt<%=args%>&sort=priced" <%=(pdtPageInfo.getSort().equals("priced") ? "class='bold'" : "") %>>높은 가격순</a>&nbsp;
   <a href="pdt_list.pdt<%=args%>&sort=namea" <%=(pdtPageInfo.getSort().equals("namea") ? "class='bold'" : "") %>>상품명순</a>&nbsp;
   <a href="pdt_list.pdt<%=args%>&sort=reviewd" <%=(pdtPageInfo.getSort().equals("reviewd") ? "class='bold'" : "") %>>리뷰순</a>&nbsp;
   <a href="pdt_list.pdt<%=args%>&sort=readcntd" <%=(pdtPageInfo.getSort().equals("readcntd") ? "class='bold'" : "") %>>조회순</a>&nbsp;
   <a href="pdt_list.pdt<%=args%>&sort=scored" <%=(pdtPageInfo.getSort().equals("scored") ? "class='bold'" : "") %>>평점순</a>&nbsp;   
</p>

<!-- 상품 목록 시작 -->
<table width="800" cellpadding="5">
<%
if (pdtList.size() > 0 ) {
// 상품 검색결과가 있으면
   for (int i = 0 ; i < pdtList.size() ; i++) {
      ProductInfo pi = pdtList.get(i);
      String lnk = null;
      if (pi.getPi_stock() != 0) {   // 상품의 재고가 남아 있는 경우
         lnk = "<a href=\"pdt_view.pdt" + args + "&piid=" + pi.getPi_id() + 
            "&sort=" + pdtPageInfo.getSort() + "&psize=" + pdtPageInfo.getPsize() + "\">";
      } else {   // 상품의 재고가 없는 품절일 경우
         lnk = "<a href=\"javasript:alert('품절된 상품입니다.');\">";
      }
      
      if (pdtPageInfo.getPsize() == 8) {
      // 한 페이지에 12개의 상품 목록을 보여줄 경우 (한 줄에 4개씩 보여주기)
         if (i % 4 == 0)      out.println("<tr align='center'>");
%>
<td width="25%">
   <%=lnk %><img src="product/pdt_img/<%=pi.getPi_img1() %>" width="180" height="200" /><br />
   <%=pi.getPi_name() %></a><br /><%=pi.getPi_price() %> 원
</td>
<%
         if (i % 4 == 3)      out.println("</tr>");

         if (i == pdtList.size() - 1 && i % 4 != 3) {   
         // 현재 출력하는 데이터가 pdtList의 마지막 데이터이면서 4칸을 모두 채우지 못했을 경우
            for (int j = 0 ; j < (4 - 1 - (i % 4)) ; j++) {         out.println("<td>&nbsp;</td>");
            out.println("</tr>");
            }
         }
      } else {
      // 한 페이지에 10개의 상품 목록을 보여줄 경우 (한 줄에 1개씩 보여주기)
%>
<tr>
<td width="150" align="center"><%=lnk %><img src="product/pdt_img/<%=pi.getPi_img1() %>" width="110" height="130" /></a></td>
<td width="*"><%=lnk + pi.getPi_name() %></a><br /><%=pi.getPb_name() %></td>
<td width="100"><strong><%=pi.getPi_price() %></strong> 원</td>
</tr>   
<%      
      }
   }
} else {
   out.println("<tr><th>검색된 상품이 없습니다.</th></tr>");
}
%>
</table>
<%
if (pdtList.size() > 0 ) {
// 상품 검색 결과가 있으면 페이지 번호를 출력
   out.println("<p style='width:800px;' align='center'>");   
   args = "?sort=" + pdtPageInfo.getSort() + "&psize=" + pdtPageInfo.getPsize() + schargs;   
   if (pdtPageInfo.getCpage() == 1) {
      out.println("[&lt;&lt;]&nbsp;&nbsp;[&lt;]&nbsp;&nbsp;");
   } else {
      out.print("<a href='pdt_list.pdt" + args + "&cpage=1'>[&lt;&lt;]</a>&nbsp;&nbsp;");
      out.println("<a href='pdt_list.pdt" + args + "&cpage=" + (pdtPageInfo.getCpage() - 1) + "'>[&lt;]</a>&nbsp;&nbsp;");
   }   // 첫  페이지와 이전 페이지 링크
   
   for (int i = 1, j = pdtPageInfo.getSpage() ; i <= pdtPageInfo.getBsize() && j <= pdtPageInfo.getEpage() ; i++, j++) {
      if (pdtPageInfo.getCpage() == j) {
         out.print("&nbsp;<strong>" + j + "</strong>&nbsp;");
      } else {
         out.print("&nbsp;<a href='pdt_list.pdt" + args + "&cpage=" + j + "'>" + j + "</a>&nbsp;");
      }
   }
   
   if (pdtPageInfo.getCpage() == pdtPageInfo.getPcnt()) {
      out.println("[&lt;&lt;][&gt;]&nbsp;&nbsp;[&gt;&gt;]");
   } else {
      out.print("&nbsp;&nbsp;<a href='pdt_list.pdt" + args + "&cpage=" + (pdtPageInfo.getCpage() + 1) + "'>[&gt;]</a>");
      out.println("&nbsp;&nbsp;<a href='pdt_list.pdt" + args + "&cpage=" + (pdtPageInfo.getPcnt()) + "'>[&gt;&gt;]</a>");
   }
   out.println("</p>");
}
%>
</body>
</html>