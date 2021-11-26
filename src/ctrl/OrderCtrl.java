package ctrl;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import act.*;
import vo.*;

@WebServlet("*.ord")
public class OrderCtrl extends HttpServlet {
// �ֹ����� ��ɵ鿡 ���� ��Ʈ�ѷ�(��ٱ���, ���� ��)
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
		case "/cart_proc.ord" :		// ��ٱ��� ó��(���, ����, ����) ��û
			action = new CartProcAct();
			break;
		case "/cart_list.ord" :		// ��ٱ��� ��� ȭ�� ��û
			action = new CartListAct();
			break;
		case "/order_form.ord" :	// ���� ȭ�� ��û
			break;
		case "/order_proc.ord" :	// ���� ó�� ��û
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
