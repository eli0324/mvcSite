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
	
	if (result == 1)	commit(conn);	// ����, ����, Ż�� ���� �۾��� �Ͼ ���ڵ尡 �ϳ��� ������ �����Ŵ
	else				rollback(conn);	// �۾��� �Ͼ ���ڵ尡 ������ ���� ������ ����ϰ�, ó������ �ǵ���
	close(conn);
	
	return result;
	}
}
