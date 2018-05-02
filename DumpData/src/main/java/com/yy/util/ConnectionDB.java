package com.yy.util;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据库连接类
 */
public class ConnectionDB {



	private  Connection conn  = null;
	/**
	 * 创建PreparedStatement对象
	 */
	private PreparedStatement preparedStatement = null;

	/**
	 * 创建结果集对象
	 */
	private ResultSet resultSet = null;


	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建立数据库连接
	 * @return 数据库连接
	 */
	public Connection getConnection(String url,String username,String password) {
		Connection  connection= null;
		try {
			// 获取连接
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/*public static void main(String[] args) {
		ConnectionDB db = new ConnectionDB();
		Connection  connection= db.getConnection("jdbc:mysql://l-db20-1.prod.qd1.corp.agrant.cn:3306/BELL_DATA_CENTER","lei.wang","1qaz@WSX");
		System.out.println(connection);
	}*/

	/**
	 * insert update delete SQL语句的执行的统一方法 ,单个执行
	 * @param sql SQL语句
	 * @param params 参数数组，若没有参数则为null
	 * @return 受影响的行数
	 */
	public int executeUpdate(String sql, Object[] params,Connection connnection) {
		// 受影响的行数
		int affectedLine = 0;
		try {
			preparedStatement = connnection.prepareStatement(sql);

			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setObject(i + 1, params[i]);
				}
			}
			// 执行
			affectedLine = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return affectedLine;
	}

	/**
	 * SQL 查询将查询结果直接放入ResultSet中
	 * @param sql SQL语句
	 * @param params 参数数组，若没有参数则为null
	 * @return 结果集
	 */
	public ResultSet executeQueryRS(String sql,List<Object> params,Connection connnection) {
		try {
			// 调用SQL
			preparedStatement = connnection.prepareStatement(sql);
			// 参数赋值
			if (params != null) {
				for (int i = 0; i < params.size(); i++) {
					preparedStatement.setObject(i + 1, params.get(i));
				}
			}
			// 执行
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultSet;
	}

	/**
	 * 获取结果集，并将结果放在List中
	 *
	 * @param sql
	 * SQL语句
	 * @return List
	 *  结果集
	 */
	public List<Object> excuteQuery(String sql, List<Object> params,Connection connnection) {
		// 执行SQL获得结果集
		ResultSet rs =executeQueryRS(sql, params,connnection);
		// 创建ResultSetMetaData对象
		ResultSetMetaData rsmd = null;
		// 结果集列数
		int columnCount = 0;
		try {
			rsmd = rs.getMetaData();
			// 获得结果集列数
			columnCount = rsmd.getColumnCount();
		} catch (SQLException e1) {
			System.out.println(e1.getMessage());
		}

		// 创建List
		List<Object> list = new ArrayList<Object>();
		try {
			// 将ResultSet的结果保存到List中
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}


	public Object excuteUnique(ConnectionDB db,String sql,List<Object> params,Connection connnection){

		// 执行SQL获得结果集
		ResultSet rs = db.executeQueryRS(sql, params,connnection);
		Object obj=null;
		try {
			Boolean  a = rs.next();
			// 将ResultSet
			System.out.println(a);
			if (a) {
				obj=rs.getObject(1);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return obj;
	}

	//List<Object>
	public  int insertBatch(ConnectionDB db,String sql,List<List<Object>> params,Connection connnection){
		// 调用SQL
		int count =0;
		try {
			preparedStatement = connnection.prepareStatement(sql);
			// 参数赋值
			if (params != null) {
				for (int i = 0,size= params.size(); i <size; i++) {
					StringBuffer bf = new StringBuffer();
					List<Object> l1=params.get(i);
					for(int j=0,lisize=l1.size();j<lisize;j++){
						Object a=l1.get(j);
						if(i == 0||i== size-1){
							bf.append(a+",");
						}
						preparedStatement.setObject(j+1,a);

					}
					if(i == 0||i== size-1){
						System.out.println(bf.toString());
					}

					preparedStatement.addBatch();

				}
			}
			int[] counts = preparedStatement.executeBatch();
			count = counts.length;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;


	}



	/**
	 * 关闭所有资源
	 */
	public Connection  closeAll(Connection connnection) {
		// 关闭结果集对象
		if (resultSet != null) {
			try {
				resultSet.close();
				resultSet=null;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 关闭PreparedStatement对象
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
				preparedStatement=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 关闭Connection 对象
		if (connnection != null) {
			try {
				connnection.close();
				connnection=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return  connnection;
	}



}
