package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class PdtViewSvc {
	public ProductInfo getPdtInfo(String piid) {
	// �˻��� ��ǰ�� ������ ProductInfo �� �ν��Ͻ��� ��� �����ϴ� �޼ҵ�
		ProductInfo pdtInfo = new ProductInfo();
		Connection conn = getConnection();
		ProductDao productDao = ProductDao.getInstance();
		productDao.setConnection(conn);
		pdtInfo = productDao.getPdtInfo(piid);
		close(conn);

		return pdtInfo;
	}
}
