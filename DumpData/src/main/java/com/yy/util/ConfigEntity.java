package com.yy.util;

/**
 * Created by yang.yang on 2018/5/2.
 */
public class ConfigEntity {

    private  String  joyTableName;

    private  String  cueTableName;

    private  String  idName;

    private  String  timeName;

    public String getJoyTableName() {
        return joyTableName;
    }

    public void setJoyTableName(String joyTableName) {
        this.joyTableName = joyTableName;
    }

    public String getCueTableName() {
        return cueTableName;
    }

    public void setCueTableName(String cueTableName) {
        this.cueTableName = cueTableName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    @Override
    public String toString() {
        return "ConfigEntity{" +
                "joyTableName='" + joyTableName + '\'' +
                ", cueTableName='" + cueTableName + '\'' +
                ", idName='" + idName + '\'' +
                ", timeName='" + timeName + '\'' +
                '}';
    }
}
