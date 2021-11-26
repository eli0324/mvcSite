package ctrl;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import svc.*;
import vo.*;

@WebServlet("/login")
public class LoginCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginCtrl() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String uid = request.getParameter("uid").trim().toLowerCase();
		String pwd = request.getParameter("pwd").trim();
		String url = request.getParameter("url").replace("$", "&");
		// 쿼리 스트링이 있는 url의 경우 '&' 를 '&'로 변경시켜 받아오기 때문에  다시 '$'를'&'로 변경시켜야 함 
		if (url.equals("")) url = "index.jsp";
		// 로그인 후 이동할 경로가 없을 경우 index 화면으로 이동하도록 주소를 지정
		
		LoginSvc loginSvc = new LoginSvc();
		MemberInfo memberInfo = loginSvc.getLoginMember(uid, pwd);
		
		HttpSession session = request.getSession();
		// JSP가 아니므로 세션 객체를 사용하려면 직접 HttpSession 인스턴스를 생성해야 함
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		if (memberInfo != null) {	// 로그인 성공 시
			session.setAttribute("memberInfo", memberInfo);
			response.sendRedirect(url);
		} else {
			out.println("<script>");
			out.println("alert('로그인에 실패했습니다.');");
			out.println("history.back();");
			out.println("</script>");
		}
	}
}
