# HiJDBProvider
一个Java数据库访问类库,
该库的特点是将SQL语句存储到某个文件夹下的XML文件中，并根据关键值获得SQL信息，这样就将SQL语句从代码中移除到配置文件中了。
同时还解决了SQL参数不能有名称的问题（这个问题是通过HiJCache解决的）



HiCSDBProvide
由来: 
SQL语句与逻辑代码不很兼容,将SQL语句放置到代码中,会增加代码的复杂度,将SQL从代码移出到配置文件中,通过配置文件中的key得到SQL信息.
1: 对SQL进行抽象,抽象出(SQLInfo):
SQL: SQL语句
sqlParameters: 参数数组

2: 如何创建SQLInfo:
1 该问题是如何正确的创建参数.
参数信息包括:
1) 参数的名称
2) 参数值如何获取
3) 对应的数据库类型
4) 是输出参数还是输入参数还是输出输入参数
抽象为ParamerCls(可以从XML中获得)
    public class ParamerCls
    {
        public string IsOutParamer;
        public string ParamerName;
        public string ParamerText;

        public ParamerCls(XmlNode node);
    }
2 提供将ParamerCls转换为参数的功能
	给出ParamerCls,给出数据库类型,获得一个SQL参数.
	
	参数值的取得:
	根据ParamerText取得.
	可以有如下情形:
	    从一个DataRow中获得一个列名为ParamerText的表格值
	    从一个Object中获得一个属性名为ParamerText的属性值
	这些抽象为一个委托: 给出一个ParamerText,获得一个Object值.
	即:
	 SQLHelper.GetSqlInfo(string key, Func<string, object> handler = null)
	

3: 抽象出一下几个接口:
ExecuteNonQuery: 执行操作
ExecuteScalar: 取得第一行第一列
ExecuteDataTable: 取得DataTable
ExecuteDataSet: 返回DataTable
OnTran: 支持事务

4: 参数抽象:
参数抽象为一个配置文件中的标识及生成参数数组的数据源,还需要考虑sql中包含{0}等Format的情况
string id, Func<string, object> handler = null
string id, IDictionary<string, string> mp, params object[] args

5: 数据库相关信息配置
数据库类型
数据库连接字符串
配置信息存放的位置
Init(int dbType, string connStr, string xmlFolder)
