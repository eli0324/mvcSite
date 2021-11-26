package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class PdtViewSvc {
	public ProductInfo getPdtInfo(String piid) {
	// 검색된 상품의 정보를 ProductInfo 형 인스턴스에 담아 리턴하는 메소드
		ProductInfo pdtInfo = new ProductInfo();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		pdtInfo = productDao.getPdtInfo(piid);
		close(conn);

		return pdtInfo;
	}
}
