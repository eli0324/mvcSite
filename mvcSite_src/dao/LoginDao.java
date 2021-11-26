package dao;

import static db.JdbcUtil.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import vo.*;

public class LoginDao {
	private static LoginDao loginDao;
	private Connection conn;

	private LoginDao() {}

	public static LoginDao getInstance() {
		if (loginDao == null)	loginDao = new LoginDao();
		return loginDao;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public MemberInfo getLoginMember(String uid, String pwd) {
	// 지정한 아이디(uid)와 암호(pwd)로 로그인 작업을 처리한 후 회원정보를 MemberInfo형 인스턴스에 담아 리턴하는 메소드
		Statement stmt = null;
		ResultSet rs = null;
		MemberInfo memberInfo = null;	// 데이터가 없을 경우 null을 리턴하게 함

		try {
			stmt = conn.createStatement();
			String sql = "select * from t_member_info " + 
				" where mi_id = '" + uid + "' and mi_pw ='" + pwd + "' and mi_isact = 'y'";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				memberInfo = new MemberInfo();	// rs에 담긴 데이터들을 저장할 인스턴스 생성
				memberInfo.setMi_idx(rs.getInt("mi_idx"));
				memberInfo.setMi_id(rs.getString("mi_id"));
				memberInfo.setMi_pw(rs.getString("mi_pw"));
				memberInfo.setMi_name(rs.getString("mi_name"));
				memberInfo.setMi_gender(rs.getString("mi_gender"));
				memberInfo.setMi_birth(rs.getString("mi_birth"));
				memberInfo.setMi_phone(rs.getString("mi_phone"));
				memberInfo.setMi_email(rs.getString("mi_email"));
				memberInfo.setMi_isad(rs.getString("mi_isad"));
				memberInfo.setMi_grade(rs.getString("mi_grade"));
				memberInfo.setMi_point(rs.getInt("mi_point"));
				memberInfo.setMi_rebank(rs.getString("mi_rebank"));
				memberInfo.setMi_reaccount(rs.getString("mi_reaccount"));
				memberInfo.setMi_recommend(rs.getString("mi_recommend"));
				memberInfo.setMi_date(rs.getString("mi_date"));
				memberInfo.setMi_isact(rs.getString("mi_isact"));
				memberInfo.setMi_lastlogin(rs.getString("mi_lastlogin"));
			}	// rs가 비었으면 else 없이 그냥 memberInfo에 null이 들어있는 상태로 리턴함

		} catch(Exception e) {
			System.out.println("LoginDao 클래스의 getLoginMember() 메소드 오류");
			e.printStackTrace();
		}

		return memberInfo;
	}
}
