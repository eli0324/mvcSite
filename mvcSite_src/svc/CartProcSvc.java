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

		if (wtype.equals("in")) {			// 특정 상품(cart)을 장바구니에 담기를 할 경우
			result = orderDao.cartInsert(cart);
		} else if (wtype.equals("up")) {	// 특정 상품(cart)을 수정 할 경우
			if (kind.equals("cnt"))	result = orderDao.cartUpdateCount(cart, where);
			else					result = orderDao.cartUpdateOption(cart, where);
		} else if (wtype.equals("del")) {	// 특정 상품(cart)을 삭제 할 경우
			result = orderDao.cartDelete(cart, where);
		}

		if (result >= 1)	commit(conn);	// 등록, 수정, 삭제 등의 작업이 일어난 레코드가 하나 이상이면 쿼리를 적용시킴
		else				rollback(conn);	// 작업이 일어난 레코드가 없으면 쿼리 적용을 취소하고, 처음으로 되돌림
		close(conn);

		return result;
	}
}
