package com.yy.util;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.sql.Connection;
import java.util.*;


public class DbControl {



	//根据主键自增同步数据
	public Boolean insertCueTimeOrder(String joyTableName, String cueTableName, String idName, int pagSize) {
		ConnectionDB db = new ConnectionDB();
		Connection cueCon = db.getConnection(ConManager.cueurl, ConManager.cueuser, ConManager.cuepwd);
		Connection joyCon = db.getConnection(ConManager.joyurl, ConManager.joyuser, ConManager.joypwd);
		ConManager cm = new ConManager();
		try {
			int idLast = cm.getLastRecordId(idName, cueTableName, db, joyCon); //最后一个序列号
			int total = cm.maxServercount(idLast, joyTableName, idName, db, cueCon);//同步的总数量
			//对数据进行分页
			int totalPageNum = (total + pagSize - 1) / pagSize;
			for (int i = 1; i <= totalPageNum; i++) {
				List<Object> lists = cm.queryByPage(idLast, i, total, pagSize, joyTableName, idName, db, joyCon);
				int count = cm.insertSql(lists, cueTableName, db, cueCon);
				System.out.println("第" + i + "页插入的条数为" + count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			joyCon = db.closeAll(joyCon);
			cueCon = db.closeAll(cueCon);
		}
		return true;
	}


	/**
	 * 根据id 和 时间同步数据
	 *
	 * @param joyTableName A数据库
	 * @param cueTableName B数据库
	 * @param idName       id
	 * @param nowTime      当前时间
	 * @param timeName     更新日期字段名称
	 * @return
	 */
	public Boolean insertDataByUpdateTime(String joyTableName, String cueTableName, String idName, String timeName,String nowTime) {
		ConnectionDB db = new ConnectionDB();
		Connection cueCon = db.getConnection(ConManager.cueurl, ConManager.cueuser, ConManager.cuepwd);
		Connection joyCon = db.getConnection(ConManager.joyurl, ConManager.joyuser, ConManager.joypwd);
		List<Object> addList = new ArrayList<Object>(); //差异集合
		ConManager cm = new ConManager();
		if (ReadConfig.partitionTableList.contains(cueTableName)){ //如果为分区表
			String partitionTime = DateUtil.getPartitionTime(nowTime);
			cueTableName = cueTableName+"_"+partitionTime;
		}
		System.out.println(cueTableName);
		try {
			//查询神马上一日0点到24点的数据
			List<Object> joyData = cm.queryDataByTime(joyTableName, nowTime, timeName, db, joyCon);
			Map<String,Object> joyMap = changeStruct(joyData,idName,timeName);
			//查询CUE上一日0点到24点的数据
			List<Object> cueData = cm.queryDataByTime(cueTableName, nowTime, timeName, db, cueCon);
			Map<String,Object> cueMap = changeStruct(cueData,idName,timeName);

			for (String key : joyMap.keySet()) {
				if(cueMap.get(key) == null){ // 未找到相同的数据，将数据添加到新增集合中
					System.out.println("Key = " + key);
					addList.add(joyMap.get(key));
				}
			}
		int count = cm.insertSql(addList, cueTableName, db, cueCon);
		System.out.println("插入的条数为" + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			joyCon = db.closeAll(joyCon);
			cueCon = db.closeAll(cueCon);
		}
		return true;
	}

	/**
	 * List 转换为Map

	 */
	public Map<String, Object> changeStruct(List<Object> list, String idName, String timeName) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> dataMap = (Map<String, Object>) list.get(i);
			Object idObj = dataMap.get(idName);
			String id = "";
			if (idObj instanceof Integer) { //针对不同类型需要转换
				id = (Integer) idObj + "";
			} else if (idObj instanceof String) {
				id = (String) idObj;
			}
			Date time = (Date) dataMap.get(timeName);
			String key = id + "&" + time.getTime();
			map.put(key, dataMap);
		}
		return map;
	}

}