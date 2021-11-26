package act;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class PdtListAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();
		// ProductInfo�� �ν��Ͻ����� ���� ArrayList�� �����Ͽ� ��ǰ����� �������� ����

		request.setCharacterEncoding("utf-8");
		int cpage = 1, psize = 8, bsize = 10, spage, epage, rcnt, pcnt;
		if (request.getParameter("cpage") != null)	cpage = Integer.parseInt(request.getParameter("cpage"));
		if (request.getParameter("psize") != null)	psize = Integer.parseInt(request.getParameter("psize"));

		// �˻����� : �˻���, ��/�Һз�, �귣��, ���ݴ�
		String keyword, bcata, scata, brand, sprice, eprice;
		keyword	= request.getParameter("keyword");	// �˻���
		bcata	= request.getParameter("bcata");	// ��з� ���̵�
		scata	= request.getParameter("scata");	// �Һз� ���̵�
		brand	= request.getParameter("brand");	// �귣�� ���̵�
		sprice	= request.getParameter("sprice");	// ���ݴ� �� ���� ����
		eprice	= request.getParameter("eprice");	// ���ݴ� �� ���� ����

		// �˻� ���ǿ� ���� where�� ����
		String where = " where a.pcs_id = c.pcs_id and b.pcb_id = c.pcb_id and a.pb_id = d.pb_id and pi_isview = 'y' ";
		if (!isEmpty(keyword))	where += " and lcase(a.pi_name) like '%" + keyword.trim().toLowerCase() + "%' ";
		else	keyword = "";
		if (!isEmpty(bcata))	where += " and b.pcb_id = '" + bcata + "' ";
		else	bcata = "";
		if (!isEmpty(scata))	where += " and c.pcs_id = '" + scata + "' ";
		else	scata = "";
		if (!isEmpty(brand))	where += " and d.pb_id = '" + brand + "' ";
		else	brand = "";
		if (!isEmpty(sprice))	where += " and a.pi_price >= '" + sprice.trim() + "' ";
		else	sprice = "";
		if (!isEmpty(eprice))	where += " and a.pi_price <= '" + eprice.trim() + "' ";
		else	eprice = "";

		// ���ı��� : �Ǹŷ�-��, ����-����, ��ǰ��-��, ����-��, ��ȸ-��, ����-��, ���-��(�⺻)
		String sort = request.getParameter("sort");
		if (sort == null || sort.equals(""))	sort = "idd";	// ������ �⺻������ ��ǰ ��� �������� ����
		// ���ı��ؿ� ���� order by �� ����
		String order = " order by pi_" + sort.substring(0, sort.length() - 1) + 
			(sort.charAt(sort.length() - 1) == 'a' ? " asc" : " desc");

		PdtListSvc pdtListSvc = new PdtListSvc();
		rcnt = pdtListSvc.getPdtCount(where);
		// �˻��� ��ǰ�� �� ������ ����¡�� ���� �ʿ�
		pdtList = pdtListSvc.getPdtList(where, order, cpage, psize);
		// �� ������(cpage)���� ������ �˻��� ��ǰ����� �޾ƿ�

		pcnt = rcnt / psize;
		if (rcnt % psize > 0)	pcnt++;				// ��ü ������ ����
		spage = (cpage - 1) / bsize * bsize + 1;	// ����� ���� ������ ��ȣ
		epage = spage + bsize - 1;
		if (epage > pcnt)	epage = pcnt;			// ����� ���� ������ ��ȣ

		PdtPageInfo pdtPageInfo = new PdtPageInfo();// ����¡�� �ʿ��� �������� ������ �ν��Ͻ�
		pdtPageInfo.setCpage(cpage);	pdtPageInfo.setPsize(psize);		pdtPageInfo.setBsize(bsize);
		pdtPageInfo.setSpage(spage);	pdtPageInfo.setEpage(epage);		pdtPageInfo.setRcnt(rcnt);
		pdtPageInfo.setPcnt(pcnt);		pdtPageInfo.setKeyword(keyword);	pdtPageInfo.setBcata(bcata);
		pdtPageInfo.setScata(scata);	pdtPageInfo.setBrand(brand);		pdtPageInfo.setSprice(sprice);
		pdtPageInfo.setEprice(eprice);	pdtPageInfo.setSort(sort);

		ArrayList<PdtCataBig> cataBigList = pdtListSvc.getCataBigList();		// ��з� ���
		ArrayList<PdtCataSmall> cataSmallList = pdtListSvc.getCataSmallList();	// �Һз� ���
		ArrayList<PdtBrandInfo> brandInfoList = pdtListSvc.getBrandInfoList();	// �귣�� ���

		request.setAttribute("pdtPageInfo", pdtPageInfo);
		request.setAttribute("pdtList", pdtList);
		request.setAttribute("cataBigList", cataBigList);
		request.setAttribute("cataSmallList", cataSmallList);
		request.setAttribute("brandInfoList", brandInfoList);
		// ������ �ν��Ͻ����� request��ü�� �Ӽ����� �����Ͽ� �̵��� �������� ������

		ActionForward forward = new ActionForward();
		forward.setPath("/product/pdt_list.jsp");

		return forward;
	}

	private boolean isEmpty(String str) {	// ���ڿ��� � ���̵� ��� �ִ��� ���θ� �˻��ϴ� �޼ҵ�
		boolean empty = true;
		if (str != null && !str.equals(""))	empty = false;
		return empty;
	}
}
