package act;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class PdtViewAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		int cpage = Integer.parseInt(request.getParameter("cpage"));
		int psize = Integer.parseInt(request.getParameter("psize"));
		// 상품보기에서 필수로 있어야 하는 값들이므로 검사없이 바로 int형으로 형변환 시킴
		String piid = request.getParameter("piid");	// 상품id

		String keyword	= request.getParameter("keyword");	if (keyword == null)	keyword = "";
		String bcata	= request.getParameter("bcata");	if (bcata == null)		bcata = "";
		String scata	= request.getParameter("scata");	if (scata == null)		scata = "";
		String brand	= request.getParameter("brand");	if (brand == null)		brand = "";
		String sprice	= request.getParameter("sprice");	if (sprice == null)		sprice = "";
		String eprice	= request.getParameter("eprice");	if (eprice == null)		eprice = "";
		String sort		= request.getParameter("sort");		if (sort == null)		sort = "idd";

		PdtViewSvc pdtViewSvc = new PdtViewSvc();
		ProductInfo pdtInfo = pdtViewSvc.getPdtInfo(piid);

		PdtPageInfo pdtPageInfo = new PdtPageInfo();// 페이징에 필요한 정보들을 저장할 인스턴스
		pdtPageInfo.setCpage(cpage);	pdtPageInfo.setPsize(psize);	pdtPageInfo.setKeyword(keyword);
		pdtPageInfo.setBcata(bcata);	pdtPageInfo.setScata(scata);	pdtPageInfo.setBrand(brand);
		pdtPageInfo.setSprice(sprice);	pdtPageInfo.setEprice(eprice);	pdtPageInfo.setSort(sort);

		request.setAttribute("pdtPageInfo", pdtPageInfo);
		request.setAttribute("pdtInfo", pdtInfo);
		// 생성한 인스턴스들을 request객체의 속성으로 저장하여 이동할 페이지로 가져감

		ActionForward forward = new ActionForward();
		forward.setPath("/product/pdt_view.jsp");

		return forward;
	}

	private boolean isEmpty(String str) {	// 문자열에 어떤 값이든 들어 있는지 여부를 검사하는 메소드
		boolean empty = true;
		if (str != null && !str.equals(""))	empty = false;
		return empty;
	}
}
