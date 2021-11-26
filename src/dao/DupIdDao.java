package dao;

import static db.JdbcUtil.*;
import javax.sql.*;
import java.sql.*;

public class DupIdDao {
	private static DupIdDao dupIdDao;
	private Connection conn;

	private DupIdDao() {}

	public static DupIdDao getInstance() {
		if (dupIdDao == null)	dupIdDao = new DupIdDao();
		return dupIdDao;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public int chkDupId(String uid) {
	// ȸ�� ���Խ� ����ڰ� ����� ���̵��� �ߺ� ���θ� ������ �޼ҵ�
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;		// �ߺ��� ���̵��� ������ ������ ����(�ߺ� : 1, ���ߺ� : 0)

		try {
			stmt = conn.createStatement();
			String sql = "select count(*) from t_member_info where mi_id = '" + uid + "' ";
			rs = stmt.executeQuery(sql);
			rs.next();
			result = rs.getInt(1);
		} catch(Exception e) {
			System.out.println("DupIdDao Ŭ������ chkDupId() �޼ҵ� ����");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return result;
	}
}