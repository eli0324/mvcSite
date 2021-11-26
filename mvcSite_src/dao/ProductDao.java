package dao;

import static db.JdbcUtil.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import vo.*;

public class ProductDao {
	private static ProductDao productDao;
	private Connection conn;

	private ProductDao() {}

	public static ProductDao getInstance() {
		if (productDao == null)	productDao = new ProductDao();
		return productDao;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public int getPdtCount(String where) {
	// �˻��� ��ǰ�� ��ü ������ �����ϴ� �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;
		int rcnt = 0;

		try {
			String sql = "select count(*) from t_product_info a, t_pdt_cata_big b, t_pdt_cata_small c, t_pdt_brand d " + where;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			rcnt = rs.getInt(1);
		} catch(Exception e) {
			System.out.println("ProductDao Ŭ������ getPdtCount() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return rcnt;
	}

	public ArrayList<ProductInfo> getPdtList(String where, String order, int cpage, int psize) {
	// �˻��� ��ǰ ����� ArrayList<ProductInfo>�� ��� �����ϴ� �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();
		// ��ǰ����� ������ ArrayList�� ���� ProductInfo�� �ν��Ͻ��� ������ �� ����
		ProductInfo pdt = null;
		// pdtList�� ������ ProductInfo�� �ν��Ͻ�

		try {
			String sql = "select a.*, b.pcb_id, b.pcb_name, c.pcs_name, d.pb_name " + 
				" from t_product_info a, t_pdt_cata_big b, t_pdt_cata_small c, t_pdt_brand d " + 
				where + order + " limit " + ((cpage - 1) * psize) + ", " + psize;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				pdt = new ProductInfo();
				// pdtList�� ������ �ϳ��� ��ǰ ������ ���� �ν��Ͻ� ����

				pdt.setPi_idx(rs.getInt("pi_idx"));				pdt.setPi_id(rs.getString("pi_id"));
				pdt.setPcs_id(rs.getString("pcs_id"));			pdt.setPb_id(rs.getString("pb_id"));
				pdt.setPi_origin(rs.getString("pi_origin"));	pdt.setPi_release(rs.getString("pi_release"));
				pdt.setPi_name(rs.getString("pi_name"));		pdt.setPi_cost(rs.getInt("pi_cost"));
				pdt.setPi_price(rs.getInt("pi_price"));			pdt.setPi_discount(rs.getDouble("pi_discount"));
				pdt.setPi_option(rs.getString("pi_option"));	pdt.setPi_img1(rs.getString("pi_img1"));
				pdt.setPi_img2(rs.getString("pi_img2"));		pdt.setPi_img3(rs.getString("pi_img3"));
				pdt.setPi_desc(rs.getString("pi_desc"));		pdt.setPi_stock(rs.getInt("pi_stock"));
				pdt.setPi_readcnt(rs.getInt("pi_readcnt"));		pdt.setPi_salecnt(rs.getInt("pi_salecnt"));
				pdt.setPi_review(rs.getInt("pi_review"));		pdt.setPi_score(rs.getDouble("pi_score"));
				pdt.setPi_ascall(rs.getString("pi_ascall"));	pdt.setPi_isview(rs.getString("pi_isview"));
				pdt.setPi_date(rs.getString("pi_date"));		pdt.setAi_idx(rs.getInt("ai_idx"));
				pdt.setPcb_id(rs.getString("pcb_id"));			pdt.setPcb_name(rs.getString("pcb_name"));
				pdt.setPcs_name(rs.getString("pcs_name"));		pdt.setPb_name(rs.getString("pb_name"));
				// �޾ƿ� �÷����� ���� pdt �ν��Ͻ��� ����

				pdtList.add(pdt);
				// �ϳ��� ��ǰ ������ ���� �ν��Ͻ� pdt�� pdtList�� ����
			}

		} catch(Exception e) {
			System.out.println("ProductDao Ŭ������ getPdtList() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return pdtList;
	}
	public ProductInfo getPdtInfo(String piid) {
	// �˻��� ��ǰ�� ������ ProductInfo �� �ν��Ͻ��� ��� �����ϴ� �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;
		ProductInfo pdt = null;

		try {
			String sql = "select a.*, b.pcb_id, b.pcb_name, c.pcs_name, d.pb_name " + 
				" from t_product_info a, t_pdt_cata_big b, t_pdt_cata_small c, t_pdt_brand d " + 
				" where a.pcs_id = c.pcs_id and b.pcb_id = c.pcb_id and a.pb_id = d.pb_id and " + 
				" a.pi_stock <> 0 and a.pi_isview = 'y' and a.pi_id = '" + piid + "' ";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				pdt = new ProductInfo();

				pdt.setPi_idx(rs.getInt("pi_idx"));				pdt.setPi_id(rs.getString("pi_id"));
				pdt.setPcs_id(rs.getString("pcs_id"));			pdt.setPb_id(rs.getString("pb_id"));
				pdt.setPi_origin(rs.getString("pi_origin"));	pdt.setPi_release(rs.getString("pi_release"));
				pdt.setPi_name(rs.getString("pi_name"));		pdt.setPi_cost(rs.getInt("pi_cost"));
				pdt.setPi_price(rs.getInt("pi_price"));			pdt.setPi_discount(rs.getDouble("pi_discount"));
				pdt.setPi_option(rs.getString("pi_option"));	pdt.setPi_img1(rs.getString("pi_img1"));
				pdt.setPi_img2(rs.getString("pi_img2"));		pdt.setPi_img3(rs.getString("pi_img3"));
				pdt.setPi_desc(rs.getString("pi_desc"));		pdt.setPi_stock(rs.getInt("pi_stock"));
				pdt.setPi_readcnt(rs.getInt("pi_readcnt"));		pdt.setPi_salecnt(rs.getInt("pi_salecnt"));
				pdt.setPi_review(rs.getInt("pi_review"));		pdt.setPi_score(rs.getDouble("pi_score"));
				pdt.setPi_ascall(rs.getString("pi_ascall"));	pdt.setPi_isview(rs.getString("pi_isview"));
				pdt.setPi_date(rs.getString("pi_date"));		pdt.setAi_idx(rs.getInt("ai_idx"));
				pdt.setPcb_id(rs.getString("pcb_id"));			pdt.setPcb_name(rs.getString("pcb_name"));
				pdt.setPcs_name(rs.getString("pcs_name"));		pdt.setPb_name(rs.getString("pb_name"));
			}

		} catch(Exception e) {
			System.out.println("ProductDao Ŭ������ getPdtInfo() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return pdt;
	}

	public ArrayList<PdtCataBig> getCataBigList() {
	// ��з� ����� ArrayList<PdtCataBig>�� ��� �����ϴ� �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<PdtCataBig> cataBigList = new ArrayList<PdtCataBig>();
		// ��ǰ����� ������ ArrayList�� ���� PdtCataBig�� �ν��Ͻ��� ������ �� ����
		PdtCataBig cata = null;
		// cataBigList�� ������ PdtCataBig�� �ν��Ͻ�

		try {
			String sql = "select * from t_pdt_cata_big";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cata = new PdtCataBig();
				// cataBigList�� ������ �ϳ��� �з� ������ ���� �ν��Ͻ� ����
				cata.setPcb_id(rs.getString("pcb_id"));		cata.setPcb_name(rs.getString("pcb_name"));
				// �޾ƿ� �÷����� ���� cata �ν��Ͻ��� ����
				cataBigList.add(cata);
				// �ϳ��� ��ǰ ������ ���� �ν��Ͻ� pdt�� pdtList�� ����
			}

		} catch(Exception e) {
			System.out.println("ProductDao Ŭ������ getCataBigList() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return cataBigList;
	}

	public ArrayList<PdtCataSmall> getCataSmallList() {
	// �Һз� ����� ArrayList<PdtCataSmall>�� ��� �����ϴ� �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<PdtCataSmall> cataSmallList = new ArrayList<PdtCataSmall>();
		PdtCataSmall cata = null;

		try {
			String sql = "select * from t_pdt_cata_small";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cata = new PdtCataSmall();
				cata.setPcs_id(rs.getString("pcs_id"));
				cata.setPcb_id(rs.getString("pcb_id"));
				cata.setPcs_name(rs.getString("pcs_name"));
				cataSmallList.add(cata);
			}

		} catch(Exception e) {
			System.out.println("ProductDao Ŭ������ getCataSmallList() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return cataSmallList;
	}

	public ArrayList<PdtBrandInfo> getBrandInfoList() {
	// �귣�� ����� ArrayList<PdtBrandInfo>�� ��� �����ϴ� �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<PdtBrandInfo> brandInfoList = new ArrayList<PdtBrandInfo>();
		PdtBrandInfo brand = null;

		try {
			String sql = "select * from t_pdt_brand";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				brand = new PdtBrandInfo();
				brand.setPb_id(rs.getString("pb_id"));
				brand.setPb_name(rs.getString("pb_name"));
				brandInfoList.add(brand);
			}

		} catch(Exception e) {
			System.out.println("ProductDao Ŭ������ getBrandInfoList() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return brandInfoList;
	}
}
