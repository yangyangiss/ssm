package com.yy.util;


import java.util.List;

public class test2 {



	public static void main(String[] args)  {

		// int  size = 10000;// 分页一条显示的条数
		String nowTime = DateUtil.getNowTime();
		DbControl dbc = new DbControl();
		List<ConfigEntity> configList =  ReadConfig.getConfigList("D:/config.txt");
		if(configList != null &&  configList.size()>0){
			for (int i = 0 ; i<  configList.size();i++){
				ConfigEntity entity = configList.get(i);
				String  joyTableName = entity.getJoyTableName();
				String  cueTableName = entity.getCueTableName();
                String  idName = entity.getIdName();
				String  timeName = entity.getTimeName();
				dbc.insertDataByUpdateTime(joyTableName,cueTableName,idName,timeName,nowTime);
			}
		}

		//dbc.insertDataByUpdateTime("yy_order_dtl","order_dtl","id","update_time","2018-05-03 19:00:23");
		dbc.insertCueTimeOrder("bellerealtimeorder","yy_table","id",10);




	}








}


