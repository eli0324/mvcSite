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
	// 회원 가입시 사용자가 사용할 아이디의 중복 여부를 리턴할 메소드
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;		// 중복된 아이디의 개수를 저장할 변수(중복 : 1, 미중복 : 0)

		try {
			stmt = conn.createStatement();
			String sql = "select count(*) from t_member_info where mi_id = '" + uid + "' ";
			rs = stmt.executeQuery(sql);
			rs.next();
			result = rs.getInt(1);
		} catch(Exception e) {
			System.out.println("DupIdDao 클래스의 chkDupId() 메소드 오류");
			e.printStackTrace();
		} finally {
			close(rs);	close(stmt);
		}

		return result;
	}
}
