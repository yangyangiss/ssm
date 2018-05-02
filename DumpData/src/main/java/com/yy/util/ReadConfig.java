package com.yy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang.yang on 2018/5/2.
 */
public class ReadConfig {

    /**
     * 需要分区的表集合
     */
    public  static List<String>   partitionTableList  = new ArrayList<String>();

    static  {
        partitionTableList.add("order_dtl");
        partitionTableList.add("order_main");
        partitionTableList.add("order_payway");
        partitionTableList.add("order_promotion_dtl");
        partitionTableList.add("company_member");
        partitionTableList.add("company_member_acct_itme_info");
    }

    public static List<ConfigEntity> getConfigList(String pathName){
        File file = new File(pathName);
        List<ConfigEntity>  configEntityList =  new ArrayList<ConfigEntity>();
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                if (s.startsWith("#")){
                    continue;
                }else {
                    String [] values =  s.split("\\|");
                    ConfigEntity  entity =  new ConfigEntity();
                    entity.setJoyTableName(values[0]);
                    entity.setCueTableName(values[0]);
                    entity.setIdName(values[1]);
                    entity.setTimeName(values[2]);
                    configEntityList.add(entity);
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return configEntityList;
    }



    public static void main(String[] args){

        System.out.println(partitionTableList.contains("company_member1"));
    }
}
