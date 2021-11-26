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
	// 검색된 상품의 전체 개수를 리턴하는 메소드
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
			System.out.println("ProductDao 클래스의 getPdtCount() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return rcnt;
	}

	public ArrayList<ProductInfo> getPdtList(String where, String order, int cpage, int psize) {
	// 검색된 상품 목록을 ArrayList<ProductInfo>에 담아 리턴하는 메소드
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();
		// 상품목록을 저장할 ArrayList로 오직 ProductInfo형 인스턴스만 저장할 수 있음
		ProductInfo pdt = null;
		// pdtList에 저장할 ProductInfo형 인스턴스

		try {
			String sql = "select a.*, b.pcb_id, b.pcb_name, c.pcs_name, d.pb_name " + 
				" from t_product_info a, t_pdt_cata_big b, t_pdt_cata_small c, t_pdt_brand d " + 
				where + order + " limit " + ((cpage - 1) * psize) + ", " + psize;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				pdt = new ProductInfo();
				// pdtList에 저장할 하나의 상품 정보를 담을 인스턴스 생성

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
				// 받아온 컬럼들의 값을 pdt 인스턴스에 저장

				pdtList.add(pdt);
				// 하나의 상품 정보를 담은 인스턴스 pdt를 pdtList에 저장
			}

		} catch(Exception e) {
			System.out.println("ProductDao 클래스의 getPdtList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return pdtList;
	}
	public ProductInfo getPdtInfo(String piid) {
	// 검색된 상품의 정보를 ProductInfo 형 인스턴스에 담아 리턴하는 메소드
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
			System.out.println("ProductDao 클래스의 getPdtInfo() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return pdt;
	}

	public ArrayList<PdtCataBig> getCataBigList() {
	// 대분류 목록을 ArrayList<PdtCataBig>에 담아 리턴하는 메소드
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<PdtCataBig> cataBigList = new ArrayList<PdtCataBig>();
		// 상품목록을 저장할 ArrayList로 오직 PdtCataBig형 인스턴스만 저장할 수 있음
		PdtCataBig cata = null;
		// cataBigList에 저장할 PdtCataBig형 인스턴스

		try {
			String sql = "select * from t_pdt_cata_big";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cata = new PdtCataBig();
				// cataBigList에 저장할 하나의 분류 정보를 담을 인스턴스 생성
				cata.setPcb_id(rs.getString("pcb_id"));		cata.setPcb_name(rs.getString("pcb_name"));
				// 받아온 컬럼들의 값을 cata 인스턴스에 저장
				cataBigList.add(cata);
				// 하나의 상품 정보를 담은 인스턴스 pdt를 pdtList에 저장
			}

		} catch(Exception e) {
			System.out.println("ProductDao 클래스의 getCataBigList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return cataBigList;
	}

	public ArrayList<PdtCataSmall> getCataSmallList() {
	// 소분류 목록을 ArrayList<PdtCataSmall>에 담아 리턴하는 메소드
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
			System.out.println("ProductDao 클래스의 getCataSmallList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return cataSmallList;
	}

	public ArrayList<PdtBrandInfo> getBrandInfoList() {
	// 브랜드 목록을 ArrayList<PdtBrandInfo>에 담아 리턴하는 메소드
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
			System.out.println("ProductDao 클래스의 getBrandInfoList() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return brandInfoList;
	}
}
