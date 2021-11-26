package dao;

import static db.JdbcUtil.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import vo.*;

public class MemberProcDao {
	private static MemberProcDao memberProcDao;
	private Connection conn;

	private MemberProcDao() {}

	public static MemberProcDao getInstance() {
		if (memberProcDao == null)	memberProcDao = new MemberProcDao();
		return memberProcDao;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public int memberInsert(MemberInfo memberInfo, MemberAddr memberAddr) {
	// ����ڰ� �Է��� ������� ȸ�� ������ �ϴ� �޼ҵ�
		PreparedStatement pstmt = null;
		int result = 0;

		try {
			String sql = "insert into t_member_info (mi_id, mi_pw, mi_name, mi_gender, mi_birth, mi_phone, " + 
				"mi_email, mi_isad, mi_point) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberInfo.getMi_id());
			pstmt.setString(2, memberInfo.getMi_pw());
			pstmt.setString(3, memberInfo.getMi_name());
			pstmt.setString(4, memberInfo.getMi_gender());
			pstmt.setString(5, memberInfo.getMi_birth());
			pstmt.setString(6, memberInfo.getMi_phone());
			pstmt.setString(7, memberInfo.getMi_email());
			pstmt.setString(8, memberInfo.getMi_isad());
			pstmt.setInt(9, 1000);
			result = pstmt.executeUpdate();

			if (result > 0) {	// ���������� t_member_info ���̺� ���ڵ尡 �߰��Ǿ��� ���
				result = 0;
				sql = "insert into t_member_address (mi_id, ma_name, ma_zip, ma_addr1, ma_addr2) values (?, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, memberInfo.getMi_id());
				pstmt.setString(2, memberAddr.getMa_name());
				pstmt.setString(3, memberAddr.getMa_zip());
				pstmt.setString(4, memberAddr.getMa_addr1());
				pstmt.setString(5, memberAddr.getMa_addr2());
				result = pstmt.executeUpdate();

				if (result > 0) {	// ���������� t_member_addr ���̺� ���ڵ尡 �߰��Ǿ��� ���
					result = 0;
					sql = "insert into t_member_point (mi_id, mp_kind, mp_point, mp_content) values (?, ?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, memberInfo.getMi_id());
					pstmt.setString(2, "����");
					pstmt.setInt(3, 1000);
					pstmt.setString(4, "���� ���ϱ�");
					result = pstmt.executeUpdate();
				}
			}

		} catch(Exception e) {
			System.out.println("MemberProcDao Ŭ������ memberInsert() �޼ҵ� ����");
			e.printStackTrace();
		}

		return result;
	}

	public int memberUpdate(MemberInfo memberInfo) {
	// ����ڰ� �Է��� �������� ���� �ϴ� �޼ҵ�
		Statement stmt = null;
		int result = 0;

		try {
			stmt = conn.createStatement();
			String sql = "update t_member_info set " + 
				"mi_phone = '"	+ memberInfo.getMi_phone()	+ "', " + 
				"mi_email = '"	+ memberInfo.getMi_email()	+ "', " + 
				"mi_isad = '"	+ memberInfo.getMi_isad()	+ "' " + 
				"where mi_id = '" + memberInfo.getMi_id()	+ "' ";
			result = stmt.executeUpdate(sql);

		} catch(Exception e) {
			System.out.println("MemberProcDao Ŭ������ memberUpdate() �޼ҵ� ����");
			e.printStackTrace();
		}

		return result;
	}
}
