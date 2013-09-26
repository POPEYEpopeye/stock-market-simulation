package market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DbPoolUtil {
	private static DbPoolUtil _instance = null;
	private static DataSource ds = null;

	private DbPoolUtil() throws Exception {
		Context ctx = new InitialContext();
		if (ctx == null)
			throw new Exception("No Context");
		ds = (DataSource) ctx.lookup("java:comp/env/jdbc/test");
	}

	public static DbPoolUtil getInstance() throws Exception {
		if (null == _instance) {
			_instance = new DbPoolUtil();
		}
		
		return _instance;
	}
	
	public Connection getConnection() throws Exception {
		return ds.getConnection();
	}
	
	public static void closeConnection(Connection con) {
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeStatement(Statement st) {
		try {
			if (st != null)
				st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closePrepStmt(PreparedStatement prepStmt) {
		try {
			if (prepStmt != null)
				prepStmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}