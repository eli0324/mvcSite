package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class CartProcSvc {
	public int cartProc(String wtype, String kind, CartInfo cart, String where) {
		Connection conn = getConnection();
		OrderDao orderDao = OrderDao.getInstance();
		orderDao.setConnection(conn);
		int result = 0;

		if (wtype.equals("in")) {			// Ư�� ��ǰ(cart)�� ��ٱ��Ͽ� ��⸦ �� ���
			result = orderDao.cartInsert(cart);
		} else if (wtype.equals("up")) {	// Ư�� ��ǰ(cart)�� ���� �� ���
			if (kind.equals("cnt"))	result = orderDao.cartUpdateCount(cart, where);
			else					result = orderDao.cartUpdateOption(cart, where);
		} else if (wtype.equals("del")) {	// Ư�� ��ǰ(cart)�� ���� �� ���
			result = orderDao.cartDelete(cart, where);
		}

		if (result >= 1)	commit(conn);	// ���, ����, ���� ���� �۾��� �Ͼ ���ڵ尡 �ϳ� �̻��̸� ������ �����Ŵ
		else				rollback(conn);	// �۾��� �Ͼ ���ڵ尡 ������ ���� ������ ����ϰ�, ó������ �ǵ���
		close(conn);

		return result;
	}
}
