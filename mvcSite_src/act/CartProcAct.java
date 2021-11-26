package act;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class CartProcAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String wtype = request.getParameter("wtype");	// 등록(in), 수정(up), 삭제(del) 여부를 구분짓는 값
		String kind = request.getParameter("kind");		// 수정시 수량(cnt)인지 옵션(opt)인지를 구분할 값
		String piid = request.getParameter("piid");		// 상품 아이디
		String ocidxs = request.getParameter("ocidx");	// 장바구니 인덱스로 삭제시 여러 개의 인덱스를 문자열로 받을 때 사용
		int occnt = 0, ocidx = 0;
		if (wtype.equals("in") || wtype.equals("up") && kind.equals("cnt"))
			occnt = Integer.parseInt(request.getParameter("occnt"));	// 상품개수

		if (wtype.equals("up"))			ocidx = Integer.parseInt(request.getParameter("ocidx"));	// 장바구니 인덱스
		// 장바구니에 상품을 등록할 경우 인덱스 번호는 필요없으므로 'in'일 경우를 제외하고 장바구니 인덱스를 받아옴

		String optName = request.getParameter("optName");	// 옵션명들(쉼표로 구분하며, 없을 수도 있음)
		String options = "";	// 사용자가 선택한 옵션들을 저장할 변수(없을 수도 있음)
		if (optName != null) {	// 사용자가 선택한 옵션이 있으면
			String[] arrOpt = optName.split(",");	// 옵션의 이름들을 쉼표를 기준으로 하여 배열로 생성
			for (int i = 0 ; i < arrOpt.length ; i++) {
				options += "-" + request.getParameter("opt" + arrOpt[i]);
				// 사용자가 선택한 옵션들을 하나씩 받아와서 하이픈을 기준으로 하나의 문자열로 생성(예 : 260-black)
			}
			options = options.substring(1);	// 맨 앞의 하이픈 제거
		} else if (wtype.equals("up") && kind != null && kind.equals("opt")) {
		// 장바구니에서 옵션 변경시
			options = request.getParameter("options");	// 장바구니에서 옵션 변경시 변경할 옵션 문자열(예 : 230-white)
		}

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

		CartInfo cart = new CartInfo();
		cart.setOc_idx(ocidx);		cart.setPi_id(piid);		cart.setOc_cnt(occnt);
		cart.setOc_opt(options);	cart.setOc_idxs(ocidxs);	cart.setMi_id(memberInfo.getMi_id());
		// 장바구니 등록, 수정, 삭제 시 필요한 정보들을 CartInfo형 인스턴스에 담음

		String where = "";
		if (wtype.equals("up") || wtype.equals("del")) {
		// 장바구니 수정, 삭제 시 사용할 where절을 생성
			where = " where mi_id = '" + memberInfo.getMi_id() + "' and ";
			if (wtype.equals("up"))	{
				where += " oc_idx = " + ocidx;
			} else if (wtype.equals("del")) {
				String[] arrIdx = ocidxs.split(",");
				String tmpStr = "";
				for (int i = 0 ; i < arrIdx.length ; i++) {
					tmpStr += " or oc_idx = " + arrIdx[i];
				}
				where += " (" + tmpStr.substring(4) + ") ";
			}
		}

		CartProcSvc cartProcSvc = new CartProcSvc();
		int result = cartProcSvc.cartProc(wtype, kind, cart, where);
		// 등록, 수정, 삭제를 모두 cartProc() 메소드에서 wtype에 따라 처리
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);

		if (result == 0) {
			out.println("<script>");
			out.println("alert('장바구니 작업시 문제가 발생했습니다.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
		}

		// 검색 조건들 및 정렬기준 쿼리 스트링
		String args = "?cpage=" + request.getParameter("cpage") + "&psize=" + request.getParameter("psize");
		String keyword	= request.getParameter("keyword");	if (!isEmptyStr(keyword))	args += "&keyword=" + keyword;
		String bcata	= request.getParameter("bcata");	if (!isEmptyStr(bcata))		args += "&bcata=" + bcata;
		String scata	= request.getParameter("scata");	if (!isEmptyStr(scata))		args += "&scata=" + scata;
		String brand	= request.getParameter("brand");	if (!isEmptyStr(brand))		args += "&brand=" + brand;
		String sprice	= request.getParameter("sprice");	if (!isEmptyStr(sprice))	args += "&sprice=" + sprice;
		String eprice	= request.getParameter("eprice");	if (!isEmptyStr(eprice))	args += "&eprice=" + eprice;
		String sort		= request.getParameter("sort");		if (!isEmptyStr(sort))		args += "&sort=" + sort;

		ActionForward forward = new ActionForward();
		forward.setRedirect(true);
		forward.setPath("cart_list.ord" + args);

		return forward;
	}

	private boolean isEmptyStr(String str) {
		boolean isEmpty = false;
		if (str == null || str.equals(""))	isEmpty = true;
		return isEmpty;
	}
}
