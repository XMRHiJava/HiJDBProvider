package hij.db.provider;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import hij.cache.extension.SQLParams;
import hij.db.DBOperate;
import hij.util.generic.IActionP1;
import hij.util.generic.IFuncP1;

public final class DBProvider {
	/**
	 * 返回队列(要求队列中的属性与查询字段名称相同)
	 * @param cls 队列中存储的数据类型
	 * @param param SQL参数(SQLParams为自定义类型)
	 * @param fun SQL中存在参数时,使用该函数设置参数(使用自定义的含名称参数)
	 * @param args SQL中包含格式化字符串时,输入的相关数据
	 * @return 队列
	 */
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
	

	/**
	 * 
	 * @param cls
	 * @param param
	 * @param fun
	 * @param args
	 * @return
	 * @throws SQLException
	 */
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
