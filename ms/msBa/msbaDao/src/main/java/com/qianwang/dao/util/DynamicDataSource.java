package com.qianwang.dao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);
    private List<String> dataSourceValus=new ArrayList<String>();

    /**
     * 可以手动指定数据源
     * @param dataSourceKey
     */
    public static void setDataSourceKey(String dataSourceKey) {
        DynamicDataSourceHolder.putDataSource(dataSourceKey);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        Object obj = DynamicDataSourceHolder.getDataSouce();
        if (log.isDebugEnabled()) {
            log.debug("get current datasource is {}", obj);
        }
        return obj;
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        if(targetDataSources!=null){
            for(Entry<Object,Object> entry : targetDataSources.entrySet()){
                if(entry.getKey()!=null && entry.getKey() instanceof String){
                    dataSourceValus.add((String)entry.getKey());
                }
            }
        }
    }

    public List<String> getDataSourceValues(){
        return dataSourceValus;
    }
}
