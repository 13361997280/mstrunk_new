package com.qbao.search.engine.service;

import com.alibaba.fastjson.JSON;
import com.qbao.config.DbFactory;
import com.qbao.dao.AdvertisementMapper;
import com.qbao.dto.Advertisement;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.util.DateUtils;
import data.service.CompassDataService;
import org.apache.ibatis.session.SqlSession;
import vo.CompassPo;
import vo.TimeSort;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author song.j
 * @create 2017-09-15 09:09:37
 **/
public class CompassService {

    private static ESLogger log = Loggers.getLogger(CompassService.class);


    private static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");

    private static final String defalutModule = "weather,video,news,sign,research,rates";
    private static final Map<String,String> CHINESS_MODULE = new HashMap(){{
        put("weather","天气");
        put("news","新闻");
        put("video","直播");
//        put("baoy","宝约");
        put("sign","签到");
        put("research","问卷");
        put("rates","汇率");
    }};

    public CompassPo.DataBean getUserOneday(String userId) {
        List<TimeSort> list = null;
        try {
            list = CompassDataService.getInstance().getUserOneday(userId);
            log.info("CompassService-getUserOneday es data = {}", JSON.toJSONString(list));
        } catch (Exception e) {
            log.error(e);
            list = new ArrayList<>();
        }

        CompassPo.DataBean dataBean = new CompassPo.DataBean();

        Date currentDate = new Date();
        dataBean.setCurrentDate(date.format(currentDate));
        dataBean.setCurrentTime(df.format(currentDate));
        dataBean.setCurrentWeek(DateUtils.getWeekDayStr(currentDate));


        //如果没有数据。用默认数据
        if (list == null || list.isEmpty()) {
            list = getDefaultData();
        } else {
            //es数据处理
            handEsData(list);
            log.info("CompassService-getUserOneday handEsData data = {}", JSON.toJSONString(list));
            //排序
            list.sort(Comparator.comparingInt(TimeSort::getHour));
            log.info("CompassService-getUserOneday sort data = {}", JSON.toJSONString(list));
            //当前模块选择
            hasCurrent(list);
            log.info("CompassService-getUserOneday hasCurrent data = {}", JSON.toJSONString(list));
            //数据填充
            fillData(list);
            log.info("CompassService-getUserOneday fillData data = {}", JSON.toJSONString(list));
        }

        try {
            addChinese(list);
        } catch (Exception e) {
            log.error("CompassService-add chinese error", e);
        }


        try {
            addAdvertisement(list);
        } catch (Exception e) {
            log.error("CompassService-addAdvertisement error", e);
        }


        dataBean.setTimeSort(list);
        return dataBean;
    }

    /**
     * 添加中文注释
     * @param list
     */
    private void addChinese(List<TimeSort> list){
        list.forEach(sort -> {
            String modules = sort.getModuleType();

            for (String key : CHINESS_MODULE.keySet()) {
                modules = modules.replaceAll(key,CHINESS_MODULE.get(key));
            }

            sort.setModuleName(modules);
        });
    }

    /**
     * 添加广告
     * @param list
     */
    private void addAdvertisement(List<TimeSort> list) {
//        AdvertisementMapper mapper = DbFactory.createBean(AdvertisementMapper.class);


        SqlSession sqlSession = DbFactory.getSqlSession();
        AdvertisementMapper mapper = sqlSession.getMapper(AdvertisementMapper.class);
        List<Advertisement> data = mapper.selectRelaseStatus();
        sqlSession.close();

        if (data == null || data.isEmpty()) {
            log.info("no found Advertisement");
            return;
        }

        int dataIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            TimeSort sort = list.get(i);

            if (!(dataIndex < data.size())) {
                dataIndex = 0;
            }
            sort.setAdvertisement(data.get(dataIndex));

            dataIndex++;
        }
    }

    public static void main(String[] args) {
        List a = new ArrayList();
        a.add(1);
        a.add(2);
        a.add(3);


        System.out.println(a.get(3));
    }

    /**
     * es数据做处理,将小时数重复的聚合。模块拼装
     *
     * @param list
     * @return SftimeAll{max_hour=15, module_id='rates,research'}
     */
    private void handEsData(List<TimeSort> list) {
        Map<Integer, TimeSort> map = new HashMap();
        for (TimeSort time : list) {
            if (map.containsKey(time.getHour())) {
                String module = map.get(time.getHour()).getModuleType();

                if (module != null && time.getModuleType() != null) {       //两个module_id 都不是空  则逗号拼装
                    time.setModuleType(module + "," + time.getModuleType());
                } else if (time.getModuleType() == null) {                  //后面一个是空。则赋值前面一个
                    time.setModuleType(module);
                }                                                           //都不是。默认使用后面一个值

            }
            map.put(time.getHour(), time);
        }
        list.clear();
        list.addAll(map.values());
    }


    /**
     * 当前时间轴
     */
    private void hasCurrent(List<TimeSort> list) {

        int hour = DateUtils.getCurrentHour();

        boolean flag = true;

        //list  倒序循环处理
        for (int i = list.size() - 1; i >= 0; i--) {
            TimeSort sort = list.get(i);
            if (flag && hour >= sort.getHour()) {
                flag = false;
                sort.setHasCurrent(true);
            }
        }
        if (flag) {
            list.get(0).setHasCurrent(true);
        }

    }

    /**
     * es 里面什么数据都没有。
     * @return
     */
    private List<TimeSort> getDefaultData() {

        //天气预报，新闻，有好物，有好货，宝约，签到，问卷，汇率。
        //7:00、9:00、13:00、18:00、20:00

        List<TimeSort> list = new ArrayList();

        list.add(new TimeSort(7));
        list.add(new TimeSort(9));
        list.add(new TimeSort(13));
        list.add(new TimeSort(18));
        list.add(new TimeSort(20));

        hasCurrent(list);

        fillData(list);

        return list;
    }

    /**
     * 数据填充
     *
     * weather,news,shopping,goodThings,baoy,sign,research,rates
     *
     * @param list
     */
    private void fillData(List<TimeSort> list) {
        String[] data;

        switch (list.size()){
            case 3:
                data = new String[]{"weather,video,news","research,video"};
                break;
            case 4:
                data = new String[]{"weather,video","news,video,research","video"};
                break;
            case 5:
                data = new String[]{"weather,video","news,video,research","video","video"};
                break;
            case 6:
                data = new String[]{"weather,video","news,video","research,video","video","video"};
                break;
            default:
                list.clear();
                list.addAll(getDefaultData());
                log.warn("CompassService-getUserOneday fillData invalid but size = {}",list.size());
                return;
        }

        fillData(list,data);
    }

    /**
     * 将数据按 ABCD 坑位依次 填充
     * @param list
     * @param strings
     */
    private void fillData(List<TimeSort> list,String[] strings){
        int size = list.size();
        int i = 0 ;
        int index = 0 ;
        while (i < size){
            TimeSort sort = list.get(i);
            if (!sort.isHasCurrent()){
                sort.setModuleType(strings[index]);
                index ++;
            }else {
                sort.setModuleType(defalutModule);
            }
            i++;
        }
    }



}
