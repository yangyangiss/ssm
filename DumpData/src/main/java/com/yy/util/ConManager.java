package com.yy.util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ConManager {


	//神马MYSQL地址
	static String joyurl= "jdbc:mysql://l-db20-5.prod.qd1.corp.agrant.cn:3306/BELL_DATA_CENTER";
	static String joyuser= "lei.wang";
	static String joypwd = "1qaz@WSX";
	//CUE数据中心地址
	static String cueurl= "jdbc:mysql://l-db20-1.prod.qd1.corp.agrant.cn:3306/BELL_DATA_CENTER";
	static String cueuser= "lei.wang";
	static String cuepwd = "1qaz@WSX";


	/**
	 * 查找出数据库里面插入的最后一条数据的主键值
	 * @idname 主键值
	 * @oracletable 表名
	 */

	public  int getLastRecordId(String idname, String tableName, ConnectionDB db, Connection connnection){

		if(idname==""){
			return 0;
		}
		String sql ="select max("+idname+")   from "+ tableName;
		Object  tempObj= db.excuteUnique(db,sql,null,connnection);
		if(tempObj==null){
			tempObj=0;
		}
		int idlast=   Integer.parseInt(tempObj.toString());
		return idlast;
	}


	/**
	 * 查找表里面需要同步的数据数量
	 * @param idlast
	 * @param sqltable
	 * @param idname
	 * @param db
	 * @param connnection
	 * @return
	 */
	public int maxServercount(int idlast,String sqltable,String idname,ConnectionDB db,Connection connnection){
		List<Object> params = new ArrayList<Object>();
		String sql ="";
		if(idname==""){
			sql="select count(*) from "+sqltable;
		}else{
			sql="select count(*) from "+sqltable+" where "+idname+" > ? ";
			params.add(idlast);
		}
		Object obj =  db.excuteUnique(db,sql,params,connnection);
		int count = Integer.parseInt(obj.toString());
		return count;
	}


	/**
	 *  根据分页同步数据（带主键的情况）
	 * @param idLast   Last 主键
	 * @param pageNum    页数
	 * @param total  总条数
	 * @param pageSize 当前页数
	 * @param sqlTable  表名
	 * @param idName  主键名称
	 * @param db     数据库
	 * @param connnection  连接驱动
	 * @return
	 */
	public List<Object>  queryByPage(int idLast,int  pageNum,int total,int pageSize,String sqlTable,String idName,ConnectionDB db,Connection connnection){
		List<Object> params = new ArrayList<Object>();
		String sql = "";
		if(idName==""){ //无主键的情况
			sql = " select * from "+sqlTable;
		}else{
			params.add(idLast);
			params.add(pageSize*(pageNum-1));
			if(total<pageSize*(pageNum)){
				params.add(total);
			}else{
				params.add(pageSize*(pageNum));
			}

			sql = "select * from (select * from "+sqlTable+" where "+idName+"> ? order by "+idName+") temp_table limit ?,?";
		}
		List<Object> lists =db.excuteQuery(sql,params,connnection);
		int listcount =lists.size();
		System.out.println("第"+pageNum+"页查出"+listcount+"条数据需要导入");
		return lists;
	}


	public List<Object>  queryDataByTime(String tableName,String nowTime,String timeName,ConnectionDB db,Connection connnection){
		List<Object> params = new ArrayList<Object>();
		String startTime = DateUtil.getStartTime(nowTime);
		String endTime = DateUtil.getEndTime(nowTime);
		String sql = "select * from "+tableName+" where "+timeName+">='"+startTime+"' and "+timeName+"<='"+endTime +"'" ;
		List<Object> lists = db.excuteQuery(sql,params,connnection);
		return  lists;
	}


	/**
	 * 向cue的数据库里面插入sql
	 * @param lists
	 * @param tableName 表名
	 * @return
	 */
	public int insertSql(List<Object>lists,String tableName,ConnectionDB db,Connection connection){
		List<List<Object>> listParams= new ArrayList<List<Object>>();
		String sql ="";
		for (int i = 0; i < lists.size(); i++) {
			Map<String, Object>   map =(Map<String, Object>) lists.get(i);
			List<Object> l1=new ArrayList<Object>();
			StringBuffer bf = new StringBuffer();
			StringBuffer cul = new StringBuffer();
			Set<Entry<String, Object>> entryset = map.entrySet();
			for (Entry<String, Object> entry : entryset) {
				bf.append("?,");
				cul.append(entry.getKey()+",");
				l1.add(entry.getValue());
			}
			sql ="insert into "+tableName+" ("+ cul.substring(0,cul.length()-1)+")  values ("+bf.substring(0,bf.length()-1)+")";
			listParams.add(l1);
		}

		int count= db.insertBatch(db,sql,listParams,connection);

		return count;

	}






}
	
