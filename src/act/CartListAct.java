package act;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class CartListAct  implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		ArrayList<CartInfo> cartList = new ArrayList<CartInfo>();
		HttpSession session = request.getSession();	// 세션 생성
		MemberInfo memberInfo = (MemberInfo)session.getAttribute("memberInfo");
		if (memberInfo == null) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('로그인 후 사용하세요.');");
			out.println("location.href='login_form.jsp';");
			out.println("</script>");
			out.close();
		}

		String where = " and a.mi_id = '" + memberInfo.getMi_id() + "' ";
	

		CartListSvc cartListSvc = new CartListSvc();
		cartList = cartListSvc.getCartList(where);
		// 등록, 수정, 삭제를 모두 cartProc() 메소드에서 wtype에 따라 처리


		// 검색 조건들 및 정렬기준 PdtPageInfo 인스턴스
		PdtPageInfo pdtPageInfo = new PdtPageInfo();
		if (request.getParameter("cpage") == null) pdtPageInfo.setCpage(1);
		else	pdtPageInfo.setCpage(Integer.parseInt(request.getParameter("cpage")));
		
		if (request.getParameter("psize") == null) pdtPageInfo.setCpage(8);
		else	pdtPageInfo.setPsize(Integer.parseInt(request.getParameter("psize")));

		pdtPageInfo.setKeyword(request.getParameter("keyword"));
		pdtPageInfo.setBcata(request.getParameter("bcata"));
		pdtPageInfo.setScata(request.getParameter("scata"));
		pdtPageInfo.setBrand(request.getParameter("brand"));
		pdtPageInfo.setSprice(request.getParameter("sprice"));
		pdtPageInfo.setEprice(request.getParameter("eprice"));
		pdtPageInfo.setSort(request.getParameter("sort"));

		request.setAttribute("cartList", cartList);
		request.setAttribute("pdtPageInfo", pdtPageInfo);

		ActionForward forward = new ActionForward();
		forward.setPath("/order/cart_list.jsp");

		return forward;
	}
}
