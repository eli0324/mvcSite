package dao;

import static db.JdbcUtil.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import vo.*;

public class OrderDao {
// ��ٱ��� �� �ֹ� ���� DB�۾��� ó���ϴ� Ŭ����
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
	// �޾ƿ� ��ǰ����(cart)�� ��ٱ��Ͽ� �߰���Ű�� �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;	// ������ ��ǰ�� ��ٱ��Ͽ� �ִ��� ���θ� �Ǵ��ϱ� ���� �ʿ�
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "select a.oc_idx, a.oc_cnt, b.pi_stock from t_order_cart a, t_product_info b " + 
				" where a.pi_id = b.pi_id and a.pi_id = '" + cart.getPi_id() + 
				"' and a.mi_id = '" + cart.getMi_id() + "' and a.oc_opt = '" + cart.getOc_opt() + "' ";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {	// �߰��Ϸ��� ��ǰ�� ������ ��ǰ(�ɼ� ����)�� �̹� �����Ѵٸ�(���� ��ǰ �������� ������ ����)
				sql = "update t_order_cart set oc_cnt = oc_cnt + " + cart.getOc_cnt() + 
					" where oc_idx = " + rs.getInt("oc_idx");
				if (rs.getInt("pi_stock") > 0)	// ��� ������(-1)�� �ƴϸ�
					sql += " and (oc_cnt + " + cart.getOc_cnt() + ") <= " + rs.getInt("pi_stock");
				// ���� ������ ���� �߰��� ������ ���� ��� ������ ��쿡�� update ����
			} else {			// ó�� �߰��ϴ� ��ǰ�̸�
				sql = "insert into t_order_cart (mi_id, pi_id, oc_opt, oc_cnt) values ('" + 
				cart.getMi_id() + "', '" + cart.getPi_id() + "', '" + cart.getOc_opt() + "', " + cart.getOc_cnt() + ") ";
			}
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("OrderDao Ŭ������ cartInsert() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return result;
	}

	public int cartUpdateCount(CartInfo cart, String where) {
	// �޾ƿ� ��ǰ����(cart)�� �̿��Ͽ� ��ٱ��� ���� ��ǰ ������ �����Ű�� �޼ҵ�
		Statement stmt = null;
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "update t_order_cart set oc_cnt = " + cart.getOc_cnt() + where;
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("OrderDao Ŭ������ cartUpdateCount() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(stmt);
		}

		return result;
	}

	public int cartUpdateOption(CartInfo cart, String where) {
	// �޾ƿ� ��ǰ����(cart)�� �̿��Ͽ� ��ٱ��� ���� ��ǰ �ɼ��� �����Ű�� �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;	// �����Ϸ��� �ɼǰ� ������ ��ǰ�� �̹� �����ϴ��� �˻��ϱ� ���� ���
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "select a.oc_idx, b.pi_stock from t_order_cart a, t_product_info b " + 
			"where a.pi_id = b.pi_id and b.pi_isview = 'y' and a.mi_id = '" + cart.getMi_id() + "' " + 
			"and a.pi_id = '" + cart.getPi_id() + "' and a.oc_opt = '" + cart.getOc_opt() + "' ";
			rs = stmt.executeQuery(sql);

			boolean tmp = false;
			if (rs.next()) {	// ��ٱ��� ������ ��ǰ�� �� �����Ϸ��� �ɼǰ� ������ ��ǰ�� ������
				tmp = true;
				sql = "update t_order_cart set oc_opt = '" + cart.getOc_opt() + "', oc_cnt = oc_cnt + " + 
				cart.getOc_cnt() + " where mi_id = '" + cart.getMi_id() + "' and oc_idx = " + rs.getInt("oc_idx") + 
				" and oc_cnt + " + cart.getOc_cnt() + " <= " + rs.getInt("pi_stock");
			} else {			// ��ٱ��� ������ ��ǰ�� �� �����Ϸ��� �ɼǰ� ������ ��ǰ�� ������
				sql = "update t_order_cart set oc_opt = '" + cart.getOc_opt() + "' " + where;
			}
			result = stmt.executeUpdate(sql);
			
			if (tmp && result > 0 ) cartDelete(cart,where);
			// ������ ��ǰ�� �����ϰ�, �ɼǺ������� ���� ���� ������ ���������� �̷�� ���� ��� ������ �����ʹ� ���� 
			
		} catch(Exception e) {
			System.out.println("OrderDao Ŭ������ cartUpdateCount() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs); close(stmt);
		}

		return result;
	}

	public int cartDelete(CartInfo cart, String where) {
	// �޾ƿ� ��ǰ����(cart)�� �̿��Ͽ� ��ٱ��� ���� ��ǰ�� ������Ű�� �޼ҵ�
		Statement stmt = null;
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "delete from t_order_cart " + where;
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("OrderDao Ŭ������ cartDelete() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(stmt);
		}

		return result;
	}

	public ArrayList<CartInfo> getCartList(String where) {
	// ��ٱ��� ����� ArrayList<CartInfo>�� �ν��Ͻ��� �����ϴ� �޼ҵ�
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
				// ArrayList�� cartList�� �����ϱ� ���� CartInfo�� �ν��Ͻ��� ����

				cart.setOc_idx(rs.getInt("oc_idx"));		cart.setMi_id(rs.getString("mi_id"));
				cart.setPi_id(rs.getString("pi_id"));		cart.setOc_cnt(rs.getInt("oc_cnt"));
				cart.setOc_opt(rs.getString("oc_opt"));		cart.setOc_date(rs.getString("oc_date"));
				cart.setPi_name(rs.getString("pi_name"));	cart.setPi_price(rs.getInt("pi_price"));
				cart.setPi_img1(rs.getString("pi_img1"));	cart.setPi_option(rs.getString("pi_option"));
				cart.setPi_stock(rs.getInt("pi_stock"));

				cartList.add(cart);
			}

		} catch(Exception e) {
			System.out.println("OrderDao Ŭ������ getCartList() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return cartList;
	}
}
