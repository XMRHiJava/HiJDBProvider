package hij.db.provider;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import hij.cache.extension.SQLParams;
import hij.db.DBOperate;
import hij.util.generic.IActionP1;
import hij.util.generic.IFuncP1;

public final class DBProvider {
	public static <T> List<T> executeQuery(Class<T> cls, SQLParams param, IFuncP1<String, Object> fun, Object...args) {
		if (cls == null || param == null) {
			return null;
		}
		
		String sql = param.getSql();
		if (args != null && args.length > 0)
		{
			sql = String.format(sql, args);
		}
		
		if (param.getParams() == null || param.getParams().size() < 1) {
			return getDB().executeQuery(cls, sql);
		}

		return getDB().executeQuery(cls, sql, new IActionP1<PreparedStatement>(){

			@Override
			public void action(PreparedStatement t) {
				SQLParams.fillParameters(t, param, fun);
			}			
		});
	}
	

	public static <T> T executeScalar(Class<T> cls, SQLParams param, IFuncP1<String, Object> fun, Object...args) throws SQLException {
		if (cls == null || param == null) {
			return null;
		}
		
		String sql = param.getSql();
		if (args != null && args.length > 0)
		{
			sql = String.format(sql, args);
		}
		
		if (param.getParams() == null || param.getParams().size() < 1) {
			return getDB().executeScalar(cls, sql);
		}

		return getDB().executeScalar(cls, sql, new IActionP1<PreparedStatement>(){

			@Override
			public void action(PreparedStatement t) {
				SQLParams.fillParameters(t, param, fun);
			}			
		});
	}
	
	private static DBOperate getDB() {
		if (db == null) {
			db = new DBOperate("", "","", 1, true);
		}
		return db;
	}
	static DBOperate db = null;
}
