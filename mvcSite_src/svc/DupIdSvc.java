package svc;

import static db.JdbcUtil.*;
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class DupIdSvc {
	public int chkDupId(String uid) {
		Connection conn = getConnection();
		DupIdDao dupIdDao = DupIdDao.getInstance();
		dupIdDao.setConnection(conn);
		int result = dupIdDao.chkDupId(uid);
		close(conn);

		return result;
	}
}
