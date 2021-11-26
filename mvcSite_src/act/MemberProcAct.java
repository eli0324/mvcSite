package act;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class MemberProcAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String wtype = request.getParameter("wtype");	// ����(in), ����(up), Ż��(del) ���θ� �������� ��
		MemberInfo memberInfo = new MemberInfo();	// ȸ���� �����͵��� ������ �ν��Ͻ�
		MemberAddr memberAddr = null;	// �ּ������� ȸ�� ������ ��쿡�� �ʿ��� �ν��Ͻ�

		HttpSession session = request.getSession();
		MemberInfo mi = null;
		// ���� �����̳� Ż��� ����� ���� �Ӽ��� MemberInfo �� �ν��Ͻ��� ����

		if (wtype.equals("in") || wtype.equals("up")) {
		// ���� ó���ϴ� �۾��� ȸ�� �����̳� ���� ������ ���(����ڰ� �Է��� �����͵��� �޾ƿͼ� memberInfo �ν��Ͻ��� ����)
			memberInfo.setMi_phone(request.getParameter("p1") + "-" + 
				request.getParameter("p2").trim() + "-" + request.getParameter("p3").trim());
			memberInfo.setMi_email(request.getParameter("e1").trim() + "@" + request.getParameter("e3").trim());
			memberInfo.setMi_isad(request.getParameter("mi_isad"));
		}

		if (wtype.equals("in")) {
			memberInfo.setMi_pw(request.getParameter("mi_pw").trim());
			memberInfo.setMi_id(request.getParameter("mi_id").trim().toLowerCase());
			memberInfo.setMi_name(request.getParameter("mi_name").trim());
			memberInfo.setMi_gender(request.getParameter("mi_gender"));
			memberInfo.setMi_birth(request.getParameter("by") + "-" + 
				request.getParameter("bm") + "-" + request.getParameter("bd"));

			memberAddr = new MemberAddr();
			memberAddr.setMa_name("�⺻ �����");
			memberAddr.setMa_zip(request.getParameter("ma_zip"));
			memberAddr.setMa_addr1(request.getParameter("ma_addr1"));
			memberAddr.setMa_addr2(request.getParameter("ma_addr2").trim());

		} else if (wtype.equals("up") || wtype.equals("del")) {
		// ȸ�� ���� �����̳� Ż���� ��� �α��� �����̹Ƿ� ���̵� ���ǿ��� �����Ͽ� ������
			mi = (MemberInfo)session.getAttribute("memberInfo");
			memberInfo.setMi_id(mi.getMi_id());
		}

		MemberProcSvc memberProcSvc = new MemberProcSvc();
		int result = memberProcSvc.memberProc(wtype, memberInfo, memberAddr);
		// ����, ����, Ż�� ó���� �޼ҵ� ȣ��(memberAddr�� ������ Ż��� null�� ������)

		String lnk = "../index.jsp";
		if (result == 1) {	// ���������� ���۵Ǿ�����
			if (wtype.equals("in"))			lnk = "../login_form.jsp";
			else if (wtype.equals("del"))	lnk = "../logout.jsp";
			else if (wtype.equals("up")) {
				lnk = "update_form.jsp";
				mi.setMi_phone(memberInfo.getMi_phone());
				mi.setMi_email(memberInfo.getMi_email());
				mi.setMi_isad(memberInfo.getMi_isad());
				// ���� ���� ������ ���� ���ǿ� ����ִ� �α��� ȸ�� ������ �����Ŵ
			}
		} else {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('�۾��� �����߽��ϴ�. �ٽ� �õ��� ���ʽÿ�.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
		}

		// �۾� �� �̵��� ��ġ�� ����� ���� �����ϴ� ActionForward �ν��Ͻ� ����
		ActionForward forward = new ActionForward();
		forward.setRedirect(true);
		forward.setPath(lnk);

		return forward;
	}
}
