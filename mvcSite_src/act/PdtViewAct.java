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
		// ��ǰ���⿡�� �ʼ��� �־�� �ϴ� �����̹Ƿ� �˻���� �ٷ� int������ ����ȯ ��Ŵ
		String piid = request.getParameter("piid");	// ��ǰid

		String keyword	= request.getParameter("keyword");	if (keyword == null)	keyword = "";
		String bcata	= request.getParameter("bcata");	if (bcata == null)		bcata = "";
		String scata	= request.getParameter("scata");	if (scata == null)		scata = "";
		String brand	= request.getParameter("brand");	if (brand == null)		brand = "";
		String sprice	= request.getParameter("sprice");	if (sprice == null)		sprice = "";
		String eprice	= request.getParameter("eprice");	if (eprice == null)		eprice = "";
		String sort		= request.getParameter("sort");		if (sort == null)		sort = "idd";

		PdtViewSvc pdtViewSvc = new PdtViewSvc();
		ProductInfo pdtInfo = pdtViewSvc.getPdtInfo(piid);

		PdtPageInfo pdtPageInfo = new PdtPageInfo();// ����¡�� �ʿ��� �������� ������ �ν��Ͻ�
		pdtPageInfo.setCpage(cpage);	pdtPageInfo.setPsize(psize);	pdtPageInfo.setKeyword(keyword);
		pdtPageInfo.setBcata(bcata);	pdtPageInfo.setScata(scata);	pdtPageInfo.setBrand(brand);
		pdtPageInfo.setSprice(sprice);	pdtPageInfo.setEprice(eprice);	pdtPageInfo.setSort(sort);

		request.setAttribute("pdtPageInfo", pdtPageInfo);
		request.setAttribute("pdtInfo", pdtInfo);
		// ������ �ν��Ͻ����� request��ü�� �Ӽ����� �����Ͽ� �̵��� �������� ������

		ActionForward forward = new ActionForward();
		forward.setPath("/product/pdt_view.jsp");

		return forward;
	}

	private boolean isEmpty(String str) {	// ���ڿ��� � ���̵� ��� �ִ��� ���θ� �˻��ϴ� �޼ҵ�
		boolean empty = true;
		if (str != null && !str.equals(""))	empty = false;
		return empty;
	}
}
