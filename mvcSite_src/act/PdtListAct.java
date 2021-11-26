package act;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class PdtListAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();
		// ProductInfo형 인스턴스만을 담은 ArrayList를 생성하여 상품목록의 정보들을 저장

		request.setCharacterEncoding("utf-8");
		int cpage = 1, psize = 8, bsize = 10, spage, epage, rcnt, pcnt;
		if (request.getParameter("cpage") != null)	cpage = Integer.parseInt(request.getParameter("cpage"));
		if (request.getParameter("psize") != null)	psize = Integer.parseInt(request.getParameter("psize"));

		// 검색조건 : 검색어, 대/소분류, 브랜드, 가격대
		String keyword, bcata, scata, brand, sprice, eprice;
		keyword	= request.getParameter("keyword");	// 검색어
		bcata	= request.getParameter("bcata");	// 대분류 아이디
		scata	= request.getParameter("scata");	// 소분류 아이디
		brand	= request.getParameter("brand");	// 브랜드 아이디
		sprice	= request.getParameter("sprice");	// 가격대 중 시작 가격
		eprice	= request.getParameter("eprice");	// 가격대 중 종료 가격

		// 검색 조건에 따른 where절 생성
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

		// 정렬기준 : 판매량-내, 가격-오내, 상품명-오, 리뷰-내, 조회-내, 평점-내, 등록-내(기본)
		String sort = request.getParameter("sort");
		if (sort == null || sort.equals(""))	sort = "idd";	// 정렬의 기본값으로 상품 등록 역순으로 정렬
		// 정렬기준에 따른 order by 절 생성
		String order = " order by pi_" + sort.substring(0, sort.length() - 1) + 
			(sort.charAt(sort.length() - 1) == 'a' ? " asc" : " desc");

		PdtListSvc pdtListSvc = new PdtListSvc();
		rcnt = pdtListSvc.getPdtCount(where);
		// 검색된 상품의 총 개수로 페이징을 위해 필요
		pdtList = pdtListSvc.getPdtList(where, order, cpage, psize);
		// 현 페이지(cpage)에서 보여줄 검색된 상품목록을 받아옴

		pcnt = rcnt / psize;
		if (rcnt % psize > 0)	pcnt++;				// 전체 페이지 개수
		spage = (cpage - 1) / bsize * bsize + 1;	// 블록의 시작 페이지 번호
		epage = spage + bsize - 1;
		if (epage > pcnt)	epage = pcnt;			// 블록의 종료 페이지 번호

		PdtPageInfo pdtPageInfo = new PdtPageInfo();// 페이징에 필요한 정보들을 저장할 인스턴스
		pdtPageInfo.setCpage(cpage);	pdtPageInfo.setPsize(psize);		pdtPageInfo.setBsize(bsize);
		pdtPageInfo.setSpage(spage);	pdtPageInfo.setEpage(epage);		pdtPageInfo.setRcnt(rcnt);
		pdtPageInfo.setPcnt(pcnt);		pdtPageInfo.setKeyword(keyword);	pdtPageInfo.setBcata(bcata);
		pdtPageInfo.setScata(scata);	pdtPageInfo.setBrand(brand);		pdtPageInfo.setSprice(sprice);
		pdtPageInfo.setEprice(eprice);	pdtPageInfo.setSort(sort);

		ArrayList<PdtCataBig> cataBigList = pdtListSvc.getCataBigList();		// 대분류 목록
		ArrayList<PdtCataSmall> cataSmallList = pdtListSvc.getCataSmallList();	// 소분류 목록
		ArrayList<PdtBrandInfo> brandInfoList = pdtListSvc.getBrandInfoList();	// 브랜드 목록

		request.setAttribute("pdtPageInfo", pdtPageInfo);
		request.setAttribute("pdtList", pdtList);
		request.setAttribute("cataBigList", cataBigList);
		request.setAttribute("cataSmallList", cataSmallList);
		request.setAttribute("brandInfoList", brandInfoList);
		// 생성한 인스턴스들을 request객체의 속성으로 저장하여 이동할 페이지로 가져감

		ActionForward forward = new ActionForward();
		forward.setPath("/product/pdt_list.jsp");

		return forward;
	}

	private boolean isEmpty(String str) {	// 문자열에 어떤 값이든 들어 있는지 여부를 검사하는 메소드
		boolean empty = true;
		if (str != null && !str.equals(""))	empty = false;
		return empty;
	}
}
