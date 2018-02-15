package com.qianwang.dao.util;



public class DynamicDataSourceHolder {

    private static final ThreadLocal<DataSourceNode> originalHolder = new ThreadLocal<DataSourceNode>();

    private static final ThreadLocal<String> currentHolder = new InheritableThreadLocal<String>();

    private static final ThreadLocal<Boolean> manualSwitchHolder = new ThreadLocal<Boolean>();

    public static void putDataSource(String name) {
        currentHolder.set(name);
    }

    public static void putOriginalDataSource(String name) {
        DataSourceNode out=originalHolder.get();
        if(out==null){
            originalHolder.set(new DataSourceNode(name));
        }else{
            DataSourceNode child=new DataSourceNode(name, out);
            originalHolder.set(child);
        }
    }

    public static void setOriginalDataSource(DataSourceNode dataSourceNode){
        originalHolder.set(dataSourceNode);
    }

    public static String getDataSouce() {
        return currentHolder.get();
    }

    public static DataSourceNode getOriginalDataSource(){
        return originalHolder.get();
    }

    public static boolean getManualSwitch(){
        if(manualSwitchHolder.get()==null){
            return false;
        }
        return manualSwitchHolder.get().booleanValue();
    }

    public static void setManualSwitch(Boolean manualSwitchValue){
        manualSwitchHolder.set(manualSwitchValue);
    }
}
