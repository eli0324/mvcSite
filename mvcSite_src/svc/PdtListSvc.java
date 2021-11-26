package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class PdtListSvc {
	public int getPdtCount(String where) {
	// �˻��� ��ǰ�� ��ü ������ �����ϴ� �޼ҵ�
		int rcnt = 0;	// ��ü ��ǰ ������ ������ ����
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		rcnt = productDao.getPdtCount(where);
		close(conn);

		return rcnt;
	}

	public ArrayList<ProductInfo> getPdtList(String where, String order, int cpage, int psize) {
	// �˻��� ��ǰ ����� ArrayList<ProductInfo>�� ��� �����ϴ� �޼ҵ�
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		pdtList = productDao.getPdtList(where, order, cpage, psize);
		close(conn);

		return pdtList;
	}

	public ArrayList<PdtCataBig> getCataBigList() {
	// ��з� ����� ArrayList<PdtCataBig>�� ��� �����ϴ� �޼ҵ�
		ArrayList<PdtCataBig> cataBigList = new ArrayList<PdtCataBig>();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		cataBigList = productDao.getCataBigList();
		close(conn);

		return cataBigList;
	}

	public ArrayList<PdtCataSmall> getCataSmallList() {
	// �Һз� ����� ArrayList<PdtCataSmall>�� ��� �����ϴ� �޼ҵ�
		ArrayList<PdtCataSmall> cataSmallList = new ArrayList<PdtCataSmall>();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		cataSmallList = productDao.getCataSmallList();
		close(conn);

		return cataSmallList;
	}

	public ArrayList<PdtBrandInfo> getBrandInfoList() {
	// �귣�� ����� ArrayList<PdtBrandInfo>�� ��� �����ϴ� �޼ҵ�
		ArrayList<PdtBrandInfo> brandInfoList = new ArrayList<PdtBrandInfo>();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		brandInfoList = productDao.getBrandInfoList();
		close(conn);

		return brandInfoList;
	}
}
