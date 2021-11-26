package act;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class CartProcAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String wtype = request.getParameter("wtype");	// ���(in), ����(up), ����(del) ���θ� �������� ��
		String kind = request.getParameter("kind");		// ������ ����(cnt)���� �ɼ�(opt)������ ������ ��
		String piid = request.getParameter("piid");		// ��ǰ ���̵�
		String ocidxs = request.getParameter("ocidx");	// ��ٱ��� �ε����� ������ ���� ���� �ε����� ���ڿ��� ���� �� ���
		int occnt = 0, ocidx = 0;
		if (wtype.equals("in") || wtype.equals("up") && kind.equals("cnt"))
			occnt = Integer.parseInt(request.getParameter("occnt"));	// ��ǰ����

		if (wtype.equals("up"))			ocidx = Integer.parseInt(request.getParameter("ocidx"));	// ��ٱ��� �ε���
		// ��ٱ��Ͽ� ��ǰ�� ����� ��� �ε��� ��ȣ�� �ʿ�����Ƿ� 'in'�� ��츦 �����ϰ� ��ٱ��� �ε����� �޾ƿ�

		String optName = request.getParameter("optName");	// �ɼǸ��(��ǥ�� �����ϸ�, ���� ���� ����)
		String options = "";	// ����ڰ� ������ �ɼǵ��� ������ ����(���� ���� ����)
		if (optName != null) {	// ����ڰ� ������ �ɼ��� ������
			String[] arrOpt = optName.split(",");	// �ɼ��� �̸����� ��ǥ�� �������� �Ͽ� �迭�� ����
			for (int i = 0 ; i < arrOpt.length ; i++) {
				options += "-" + request.getParameter("opt" + arrOpt[i]);
				// ����ڰ� ������ �ɼǵ��� �ϳ��� �޾ƿͼ� �������� �������� �ϳ��� ���ڿ��� ����(�� : 260-black)
			}
			options = options.substring(1);	// �� ���� ������ ����
		} else if (wtype.equals("up") && kind != null && kind.equals("opt")) {
		// ��ٱ��Ͽ��� �ɼ� �����
			options = request.getParameter("options");	// ��ٱ��Ͽ��� �ɼ� ����� ������ �ɼ� ���ڿ�(�� : 230-white)
		}

		HttpSession session = request.getSession();	// ���� ����
		MemberInfo memberInfo = (MemberInfo)session.getAttribute("memberInfo");
		if (memberInfo == null) {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('�α��� �� ����ϼ���.');");
			out.println("location.href='login_form.jsp';");
			out.println("</script>");
			out.close();
		}

		CartInfo cart = new CartInfo();
		cart.setOc_idx(ocidx);		cart.setPi_id(piid);		cart.setOc_cnt(occnt);
		cart.setOc_opt(options);	cart.setOc_idxs(ocidxs);	cart.setMi_id(memberInfo.getMi_id());
		// ��ٱ��� ���, ����, ���� �� �ʿ��� �������� CartInfo�� �ν��Ͻ��� ����

		String where = "";
		if (wtype.equals("up") || wtype.equals("del")) {
		// ��ٱ��� ����, ���� �� ����� where���� ����
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
		// ���, ����, ������ ��� cartProc() �޼ҵ忡�� wtype�� ���� ó��
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);

		if (result == 0) {
			out.println("<script>");
			out.println("alert('��ٱ��� �۾��� ������ �߻��߽��ϴ�.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
		}

		// �˻� ���ǵ� �� ���ı��� ���� ��Ʈ��
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
