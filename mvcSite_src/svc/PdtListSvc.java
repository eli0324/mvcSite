package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class PdtListSvc {
	public int getPdtCount(String where) {
	// 검색된 상품의 전체 개수를 리턴하는 메소드
		int rcnt = 0;	// 전체 상품 개수를 저장할 변수
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		rcnt = productDao.getPdtCount(where);
		close(conn);

		return rcnt;
	}

	public ArrayList<ProductInfo> getPdtList(String where, String order, int cpage, int psize) {
	// 검색된 상품 목록을 ArrayList<ProductInfo>에 담아 리턴하는 메소드
		ArrayList<ProductInfo> pdtList = new ArrayList<ProductInfo>();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		pdtList = productDao.getPdtList(where, order, cpage, psize);
		close(conn);

		return pdtList;
	}

	public ArrayList<PdtCataBig> getCataBigList() {
	// 대분류 목록을 ArrayList<PdtCataBig>에 담아 리턴하는 메소드
		ArrayList<PdtCataBig> cataBigList = new ArrayList<PdtCataBig>();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		cataBigList = productDao.getCataBigList();
		close(conn);

		return cataBigList;
	}

	public ArrayList<PdtCataSmall> getCataSmallList() {
	// 소분류 목록을 ArrayList<PdtCataSmall>에 담아 리턴하는 메소드
		ArrayList<PdtCataSmall> cataSmallList = new ArrayList<PdtCataSmall>();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		cataSmallList = productDao.getCataSmallList();
		close(conn);

		return cataSmallList;
	}

	public ArrayList<PdtBrandInfo> getBrandInfoList() {
	// 브랜드 목록을 ArrayList<PdtBrandInfo>에 담아 리턴하는 메소드
		ArrayList<PdtBrandInfo> brandInfoList = new ArrayList<PdtBrandInfo>();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		brandInfoList = productDao.getBrandInfoList();
		close(conn);

		return brandInfoList;
	}
}
