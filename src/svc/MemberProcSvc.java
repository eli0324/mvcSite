package svc;

import static db.JdbcUtil.*;	
import java.util.*;
import java.sql.*;
import dao.*;
import vo.*;

public class MemberProcSvc {
	public int memberProc(String wtype, MemberInfo memberInfo, MemberAddr memberAddr) {
	Connection conn = getConnection();
	MemberProcDao memberProcDao = MemberProcDao.getInstance();
	memberProcDao.setConnection(conn);
	int result = 0;
	if (wtype.equals("in")) {
		result = memberProcDao.memberInsert(memberInfo, memberAddr);
	} else if (wtype.equals("up")) {
		result = memberProcDao.memberUpdate(memberInfo);
	} else if (wtype.equals("del")) {
//		result = memberProcDao.memberDelete(memberInfo);
	}
	
	if (result == 1)	commit(conn);	// 가입, 수정, 탈퇴 등의 작업이 일어난 레코드가 하나면 쿼리를 적용시킴
	else				rollback(conn);	// 작업이 일어난 레코드가 없으면 쿼리 적용을 취소하고, 처음으로 되돌림
	close(conn);
	
	return result;
	}
}
