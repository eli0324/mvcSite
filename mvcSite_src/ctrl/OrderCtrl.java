package ctrl;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import act.*;
import vo.*;

@WebServlet("*.ord")
public class OrderCtrl extends HttpServlet {
// 주문관련 기능들에 대한 컨트롤러(장바구니, 결제 등)
	private static final long serialVersionUID = 1L;

    public OrderCtrl() {
        super();
    }

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestUri.substring(contextPath.length());

		ActionForward forward = null;
		Action action = null;

		switch (command) {
		case "/cart_proc.ord" :		// 장바구니 처리(담기, 수정, 삭제) 요청
			action = new CartProcAct();
			break;
		case "/cart_list.ord" :		// 장바구니 목록 화면 요청
			action = new CartListAct();
			break;
		case "/order_form.ord" :	// 결제 화면 요청
			break;
		case "/order_proc.ord" :	// 결제 처리 요청
			break;
		}

		try {
			forward = action.execute(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}

		if (forward != null) {
			if (forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
	    		dispatcher.forward(request, response);
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
}
