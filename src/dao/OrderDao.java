package dao;

import static db.JdbcUtil.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import vo.*;

public class OrderDao {
// 장바구니 및 주문 관련 DB작업을 처리하는 클래스
	private static OrderDao orderDao;
	private Connection conn;

	private OrderDao() {}

	public static OrderDao getInstance() {
		if (orderDao == null)	orderDao = new OrderDao();
		return orderDao;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public int cartInsert(CartInfo cart) {
	// 받아온 상품정보(cart)를 장바구니에 추가시키는 메소드
		Statement stmt = null;
		ResultSet rs = null;	// 동일한 상품이 장바구니에 있는지 여부를 판단하기 위해 필요
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "select a.oc_idx, a.oc_cnt, b.pi_stock from t_order_cart a, t_product_info b " + 
				" where a.pi_id = b.pi_id and a.pi_id = '" + cart.getPi_id() + 
				"' and a.mi_id = '" + cart.getMi_id() + "' and a.oc_opt = '" + cart.getOc_opt() + "' ";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {	// 추가하려는 상품과 동일한 상품(옵션 포함)이 이미 존재한다면(기존 상품 정보에서 수량만 조절)
				sql = "update t_order_cart set oc_cnt = oc_cnt + " + cart.getOc_cnt() + 
					" where oc_idx = " + rs.getInt("oc_idx");
				if (rs.getInt("pi_stock") > 0)	// 재고가 무제한(-1)이 아니면
					sql += " and (oc_cnt + " + cart.getOc_cnt() + ") <= " + rs.getInt("pi_stock");
				// 기존 수량과 새로 추가할 수량의 합이 재고량 이하일 경우에만 update 가능
			} else {			// 처음 추가하는 상품이면
				sql = "insert into t_order_cart (mi_id, pi_id, oc_opt, oc_cnt) values ('" + 
				cart.getMi_id() + "', '" + cart.getPi_id() + "', '" + cart.getOc_opt() + "', " + cart.getOc_cnt() + ") ";
			}
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("OrderDao 클래스의 cartInsert() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return result;
	}

	public int cartUpdateCount(CartInfo cart, String where) {
	// 받아온 상품정보(cart)를 이용하여 장바구니 내의 상품 수량을 변경시키는 메소드
		Statement stmt = null;
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "update t_order_cart set oc_cnt = " + cart.getOc_cnt() + where;
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("OrderDao 클래스의 cartUpdateCount() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(stmt);
		}

		return result;
	}

	public int cartUpdateOption(CartInfo cart, String where) {
	// 받아온 상품정보(cart)를 이용하여 장바구니 내의 상품 옵션을 변경시키는 메소드
		Statement stmt = null;
		ResultSet rs = null;	// 변경하려는 옵션과 동일한 상품이 이미 존재하는지 검사하기 위해 사용
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "select a.oc_idx, b.pi_stock from t_order_cart a, t_product_info b " + 
			"where a.pi_id = b.pi_id and b.pi_isview = 'y' and a.mi_id = '" + cart.getMi_id() + "' " + 
			"and a.pi_id = '" + cart.getPi_id() + "' and a.oc_opt = '" + cart.getOc_opt() + "' ";
			rs = stmt.executeQuery(sql);

			boolean tmp = false;
			if (rs.next()) {	// 장바구니 기존의 상품들 중 변경하려는 옵션과 동일한 상품이 있으면
				tmp = true;
				sql = "update t_order_cart set oc_opt = '" + cart.getOc_opt() + "', oc_cnt = oc_cnt + " + 
				cart.getOc_cnt() + " where mi_id = '" + cart.getMi_id() + "' and oc_idx = " + rs.getInt("oc_idx") + 
				" and oc_cnt + " + cart.getOc_cnt() + " <= " + rs.getInt("pi_stock");
			} else {			// 장바구니 기존의 상품들 중 변경하려는 옵션과 동일한 상품이 없으면
				sql = "update t_order_cart set oc_opt = '" + cart.getOc_opt() + "' " + where;
			}
			result = stmt.executeUpdate(sql);
			
			if (tmp && result > 0 ) cartDelete(cart,where);
			// 동일한 상품이 존재하고, 옵션변경으로 인한 수량 증가가 성공적으로 이루어 졌을 경우 기존의 데이터는 삭제 
			
		} catch(Exception e) {
			System.out.println("OrderDao 클래스의 cartUpdateCount() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs); close(stmt);
		}

		return result;
	}

	public int cartDelete(CartInfo cart, String where) {
	// 받아온 상품정보(cart)를 이용하여 장바구니 내의 상품을 삭제시키는 메소드
		Statement stmt = null;
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "delete from t_order_cart " + where;
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("OrderDao 클래스의 cartDelete() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(stmt);
		}

		return result;
	}

	public ArrayList<CartInfo> getCartList(String where) {
	// 장바구니 목록을 ArrayList<CartInfo>형 인스턴스로 리턴하는 메소드
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<CartInfo> cartList = new ArrayList<CartInfo>();
		CartInfo cart = null;

		try {
			stmt = conn.createStatement();
			String sql = "select a.*, b.pi_name, b.pi_img1, b.pi_option, b.pi_price, b.pi_stock " + 
			" from t_order_cart a, t_product_info b where a.pi_id = b.pi_id and b.pi_isview = 'y' " + 
			" and (b.pi_stock >= a.oc_cnt or b.pi_stock = -1) " + where + " order by a.pi_id";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cart = new CartInfo();
				// ArrayList인 cartList에 저장하기 위해 CartInfo형 인스턴스를 생성

				cart.setOc_idx(rs.getInt("oc_idx"));		cart.setMi_id(rs.getString("mi_id"));
				cart.setPi_id(rs.getString("pi_id"));		cart.setOc_cnt(rs.getInt("oc_cnt"));
				cart.setOc_opt(rs.getString("oc_opt"));		cart.setOc_date(rs.getString("oc_date"));
				cart.setPi_name(rs.getString("pi_name"));	cart.setPi_price(rs.getInt("pi_price"));
				cart.setPi_img1(rs.getString("pi_img1"));	cart.setPi_option(rs.getString("pi_option"));
				cart.setPi_stock(rs.getInt("pi_stock"));

				cartList.add(cart);
			}

		} catch(Exception e) {
			System.out.println("OrderDao 클래스의 getCartList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return cartList;
	}
}
