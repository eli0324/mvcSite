package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class CartListSvc {
	public ArrayList<CartInfo> getCartList(String where) {
		Connection conn = getConnection();
		OrderDao orderDao = OrderDao.getInstance();
		orderDao.setConnection(conn);
		ArrayList<CartInfo> cartList = orderDao.getCartList(where);
		close(conn);

		return cartList;
	}
}

