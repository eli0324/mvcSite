package act;

import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import svc.*;
import vo.*;

public class MemberProcAct implements Action {
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String wtype = request.getParameter("wtype");	// 가입(in), 수정(up), 탈퇴(del) 여부를 구분짓는 값
		MemberInfo memberInfo = new MemberInfo();	// 회원의 데이터들을 저장할 인스턴스
		MemberAddr memberAddr = null;	// 주소정보로 회원 가입일 경우에만 필요한 인스턴스

		HttpSession session = request.getSession();
		MemberInfo mi = null;
		// 정보 수정이나 탈퇴시 사용할 세션 속성을 MemberInfo 형 인스턴스로 생성

		if (wtype.equals("in") || wtype.equals("up")) {
		// 현재 처리하는 작업이 회원 가입이나 정보 수정일 경우(사용자가 입력한 데이터들을 받아와서 memberInfo 인스턴스에 저장)
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
			memberAddr.setMa_name("기본 배송지");
			memberAddr.setMa_zip(request.getParameter("ma_zip"));
			memberAddr.setMa_addr1(request.getParameter("ma_addr1"));
			memberAddr.setMa_addr2(request.getParameter("ma_addr2").trim());

		} else if (wtype.equals("up") || wtype.equals("del")) {
		// 회원 정보 수정이나 탈퇴의 경우 로그인 상태이므로 아이디를 세션에서 추출하여 가져감
			mi = (MemberInfo)session.getAttribute("memberInfo");
			memberInfo.setMi_id(mi.getMi_id());
		}

		MemberProcSvc memberProcSvc = new MemberProcSvc();
		int result = memberProcSvc.memberProc(wtype, memberInfo, memberAddr);
		// 가입, 수정, 탈퇴를 처리할 메소드 호출(memberAddr은 수정과 탈퇴시 null로 가져감)

		String lnk = "../index.jsp";
		if (result == 1) {	// 정상적으로 동작되었으면
			if (wtype.equals("in"))			lnk = "../login_form.jsp";
			else if (wtype.equals("del"))	lnk = "../logout.jsp";
			else if (wtype.equals("up")) {
				lnk = "update_form.jsp";
				mi.setMi_phone(memberInfo.getMi_phone());
				mi.setMi_email(memberInfo.getMi_email());
				mi.setMi_isad(memberInfo.getMi_isad());
				// 정보 수정 성공시 현재 세션에 들어있는 로그인 회원 정보도 변경시킴
			}
		} else {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('작업에 실패했습니다. 다시 시도해 보십시오.');");
			out.println("history.back();");
			out.println("</script>");
			out.close();
		}

		// 작업 후 이동할 위치와 방법에 대해 지정하는 ActionForward 인스턴스 생성
		ActionForward forward = new ActionForward();
		forward.setRedirect(true);
		forward.setPath(lnk);

		return forward;
	}
}
