package data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qbao.config.DbFactory;
import com.qbao.dao.*;
import com.qbao.dto.*;
import org.apache.commons.lang.StringUtils;
import redis.RedisConstant;
import util.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenghaijiang on 2017/11/17.
 */
public class RedisUnit {
    /**--------------------行为系数相关配置------------------**/
    /**
     * 获取分数的系数等级
     * @param score
     * @return
     */
    public static JSONObject getRatio(double score){
        JSONObject ratioLevel = null;
        String keyValue = String.format(RedisConstant.CREDIT_CONF_RATIO_USER.key,score);
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ratioLevel = JSON.parseObject(obj);
        }else{
            ratioLevel = calRatio(getRatio(),score);
            redis.RedisUtil.getInstance().set(keyValue,ratioLevel.toJSONString(),DateUtil.getRedisExpired().intValue());
        }
        return ratioLevel;
    }

    /**
     * 获取系数权重列表
     * @return
     */
    private static List<ConfRatio> getRatio(){
        String key = RedisConstant.CREDIT_CONF_RATIO.key;
        String obj = redis.RedisUtil.getInstance().get(key);
        List<ConfRatio> confRatioList = JSON.parseArray(obj,ConfRatio.class);
        if(confRatioList == null) {
            ConfRatioMapper confRatioMapper = DbFactory.createBean(ConfRatioMapper.class);
            confRatioList = confRatioMapper.getAll();
            redis.RedisUtil.getInstance().set(key,JSON.toJSONString(confRatioList),DateUtil.getRedisExpired().intValue());
        }
        return confRatioList;
    }

    /**
     * 计算信用分在行为系数哪个等级
     * @param ratios
     * @param score
     * @return
     */
    private static JSONObject calRatio(List<ConfRatio> ratios,double score){
        JSONObject params = new JSONObject();
        double nextScore = getCreditScore();
        double currentScore = getCreditScore();
        double nextRatio = 0.00;
        double currentRatio = 0.00;

        for (ConfRatio key : ratios) {
            double value = key.getRatio()==null?0:key.getRatio().doubleValue();
            nextRatio = value;
            nextScore = key.getStartScore()==null?0:key.getStartScore().doubleValue();
            if(score <= nextScore) break;
            currentRatio = value;
            currentScore = key.getStartScore()==null?0:key.getStartScore().doubleValue();
        }
        params.put("nextScore",nextScore);
        params.put("currentScore",currentScore);
        params.put("nextRatio",nextRatio);
        params.put("currentRatio",currentRatio);
        return params;
    }

    /**--------------------签到相关配置--------------------**/
    /**
     * 签到上限分数
     * @return
     */
    public static double getSignScoreLimit() {
        double signScoreLimit = getMaxLimitScore();
        String keyValue = RedisConstant.CREDIT_CONF_SIGN.key;
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfSign confSign = JSON.parseObject(obj,ConfSign.class);
            signScoreLimit = confSign.getTotalScoreLimit().doubleValue();
        }else{
            ConfSignMapper confSignMapper = DbFactory.createBean(ConfSignMapper.class);
            ConfSign confSign = confSignMapper.getEntity();
            if(confSign!=null){
                signScoreLimit = confSign.getTotalScoreLimit().doubleValue();
            }else{
                confSign = new ConfSign();
                confSign.setScore(BigDecimal.ZERO);
                confSign.setTotalScoreLimit(BigDecimal.valueOf(signScoreLimit));
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confSign),DateUtil.getRedisExpired().intValue());
        }
        return signScoreLimit;
    }

    /**
     * 设定默认的无上限分
     * @return
     */
    private static double getMaxLimitScore(){
        return -1;
    }

    /**
     * 签到总分
     * @return
     */
    public static double getSignScore() {
        double signScore = 0.00;
        String keyValue = RedisConstant.CREDIT_CONF_SIGN.key;
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfSign confSign = JSON.parseObject(obj,ConfSign.class);
            signScore = confSign.getScore().doubleValue();
        }else{
            ConfSignMapper confSignMapper = DbFactory.createBean(ConfSignMapper.class);
            ConfSign confSign = confSignMapper.getEntity();
            if(confSign!=null){
                signScore = confSign.getScore().doubleValue();
            }else{
                confSign = new ConfSign();
                confSign.setScore(BigDecimal.ZERO);
                confSign.setTotalScoreLimit(BigDecimal.valueOf(getMaxLimitScore()));
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confSign), DateUtil.getRedisExpired().intValue());
        }
        return signScore;
    }

    /**
     * 用户今天是否签到
     * @param userId
     * @return
     */
    public static boolean isSign(Integer userId) {
        boolean flag = false;
        String keyValue = String.format(RedisConstant.CREDIT_CONF_SIGN_FLAG.getKey(),userId);
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)&&"1".equals(obj)){
            flag = true;
        }
        return flag;
    }

    /**--------------------任务相关配置--------------------**/
    /**
     * 判断该任务是否可以重复加分
     * @param taskId
     * @return
     */
    public static String getIsDuplicate(Integer taskId) {
        String isDuplicate = "N";
        String keyValue = String.format(RedisConstant.CREDIT_CONF_TASK.getKey(),taskId);
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfTask confTask = JSON.parseObject(obj,ConfTask.class);
            isDuplicate = confTask.getIsDuplicate();
        }else{
            ConfTaskMapper confTaskMapper = DbFactory.createBean(ConfTaskMapper.class);
            ConfTask confTask = confTaskMapper.getEntity(taskId);
            if(confTask!=null){
                isDuplicate = confTask.getIsDuplicate();
            }else{
                confTask = new ConfTask();
                confTask.setIsDuplicate("N");
                confTask.setAddScore(BigDecimal.ZERO);
                confTask.setSubScore(BigDecimal.ZERO);
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confTask),DateUtil.getRedisExpired().intValue());
        }
        return isDuplicate;
    }

    /**
     * 该任务减分是多少
     * @param taskId
     * @return
     */
    public static double getTaskSubScore(Integer taskId) {
        double taskSubScore = 0.00;
        String keyValue = String.format(RedisConstant.CREDIT_CONF_TASK.getKey(),taskId);
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfTask confTask = JSON.parseObject(obj,ConfTask.class);
            taskSubScore = confTask.getSubScore().doubleValue();
        }else{
            ConfTaskMapper confTaskMapper = DbFactory.createBean(ConfTaskMapper.class);
            ConfTask confTask = confTaskMapper.getEntity(taskId);
            if(confTask!=null){
                taskSubScore = confTask.getSubScore().doubleValue();
            }else{
                confTask = new ConfTask();
                confTask.setIsDuplicate("N");
                confTask.setAddScore(BigDecimal.ZERO);
                confTask.setSubScore(BigDecimal.ZERO);
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confTask),DateUtil.getRedisExpired().intValue());
        }
        return taskSubScore;
    }

    /**
     * 任务总分上限
     * @return
     */
    public static double getTaskScoreLimit() {//任务总分上限
        double taskScoreLimit = getMaxLimitScore();
        String keyValue = RedisConstant.CREDIT_CONF_TASK_SET.getKey();
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfTaskSet confTaskSet = JSON.parseObject(obj,ConfTaskSet.class);
            taskScoreLimit = confTaskSet.getTotalScore().doubleValue();
        }else{
            ConfTaskSetMapper confTaskSetMapper = DbFactory.createBean(ConfTaskSetMapper.class);
            ConfTaskSet confTaskSet = confTaskSetMapper.getEntity();
            if(confTaskSet!=null){
                taskScoreLimit = confTaskSet.getTotalScore().doubleValue();
            }else{
                confTaskSet = new ConfTaskSet();
                confTaskSet.setOnedayScore(BigDecimal.valueOf(getMaxLimitScore()));
                confTaskSet.setTotalScore(BigDecimal.valueOf(getMaxLimitScore()));
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confTaskSet),DateUtil.getRedisExpired().intValue());
        }
        return taskScoreLimit;
    }

    /**
     * 今日任务分上限
     * @return
     */
    public static double getTodayTaskScoreLimit() {//单日任务分上限
        double todayTaskScoreLimit = getMaxLimitScore();
        String keyValue = RedisConstant.CREDIT_CONF_TASK_SET.getKey();
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfTaskSet confTaskSet = JSON.parseObject(obj,ConfTaskSet.class);
            todayTaskScoreLimit = confTaskSet.getOnedayScore().doubleValue();
        }else{
            ConfTaskSetMapper confTaskSetMapper = DbFactory.createBean(ConfTaskSetMapper.class);
            ConfTaskSet confTaskSet = confTaskSetMapper.getEntity();
            if(confTaskSet!=null){
                todayTaskScoreLimit = confTaskSet.getOnedayScore().doubleValue();
            }else{
                confTaskSet = new ConfTaskSet();
                confTaskSet.setOnedayScore(BigDecimal.valueOf(getMaxLimitScore()));
                confTaskSet.setTotalScore(BigDecimal.valueOf(getMaxLimitScore()));
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confTaskSet),DateUtil.getRedisExpired().intValue());
        }
        return todayTaskScoreLimit;
    }

    /**
     * 单个任务分上限
     * @param taskId
     * @return
     */
    public static double getTaskAddScore(Integer taskId) {//单个任务上限
        double taskAddScore = getMaxLimitScore();
        String keyValue = String.format(RedisConstant.CREDIT_CONF_TASK.getKey(),taskId);
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfTask confTask = JSON.parseObject(obj,ConfTask.class);
            taskAddScore = confTask.getAddScore().doubleValue();
        }else{
            ConfTaskMapper confTaskMapper = DbFactory.createBean(ConfTaskMapper.class);
            ConfTask confTask = confTaskMapper.getEntity(taskId);
            if(confTask!=null){
                taskAddScore = confTask.getAddScore().doubleValue();
            }else{
                confTask = new ConfTask();
                confTask.setIsDuplicate("N");
                confTask.setAddScore(BigDecimal.ZERO);
                confTask.setSubScore(BigDecimal.ZERO);
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confTask),DateUtil.getRedisExpired().intValue());
        }
        return taskAddScore;
    }

    /**
     * 是否是合法的任务id
     * @param taskId
     * @return
     */
    public static boolean isValidTaskId(Integer taskId) {
        boolean isValidTaskId = false;
        String keyValue = String.format(RedisConstant.CREDIT_CONF_TASK_FLAG.getKey(),taskId);
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            isValidTaskId = true;
        }else{
            ConfTaskMapper confTaskMapper = DbFactory.createBean(ConfTaskMapper.class);
            ConfTask confTask = confTaskMapper.getEntity(taskId);
            if(confTask!=null){
                isValidTaskId = true;
                redis.RedisUtil.getInstance().set(keyValue,"1",DateUtil.getRedisExpired().intValue());
            }
        }
        return isValidTaskId;
    }
    /**--------------------总分上限配置--------------------**/
    /**
     * 今日总分上限
     * @return
     */
    public static double getTodayScoreLimit() {//单日总上限
        double todayScoreLimit = getMaxLimitScore();
        String keyValue = RedisConstant.CREDIT_CONF_BASE_SET.getKey();
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfBaseSet confBaseSet = JSON.parseObject(obj,ConfBaseSet.class);
            todayScoreLimit = confBaseSet.getOnedayScoreLimit().doubleValue();
        }else{
            ConfBaseSetMapper confBaseSetMapper = DbFactory.createBean(ConfBaseSetMapper.class);
            ConfBaseSet confBaseSet = confBaseSetMapper.selectByOnline();
            if(confBaseSet!=null){
                todayScoreLimit = confBaseSet.getOnedayScoreLimit().doubleValue();
            }else{
                confBaseSet = new ConfBaseSet();
                confBaseSet.setOnedayScoreLimit(BigDecimal.valueOf(getMaxLimitScore()));
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confBaseSet),DateUtil.getRedisExpired().intValue());
        }
        return todayScoreLimit;
    }

    /**
     * 获取默认的总分
     * @return
     */
    public static double getTotalScore() {
        return getCreditScore();
    }

    /**
     * 获取默认的信用分
     * @return
     */
    public static double getCreditScore() {
        double creditScore = 401.00;
        try {
            String keyValue = String.format(RedisConstant.CREDIT_SYS_CONFIG.getKey(), "creditScore");
            String obj = redis.RedisUtil.getInstance().get(keyValue);
            if (StringUtils.isNotEmpty(obj)) {
                creditScore = Double.parseDouble(obj);
            } else {
                SysConfigMapper sysConfigMapper = DbFactory.createBean(SysConfigMapper.class);
                SysConfig sysConfig = sysConfigMapper.getEntity("creditScore");
                if (sysConfig != null) {
                    creditScore = Double.parseDouble(sysConfig.getSysValue());
                }
                redis.RedisUtil.getInstance().set(keyValue, String.valueOf(creditScore), DateUtil.getRedisExpired().intValue());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return creditScore;
    }

    /**--------------------加分项列表配置--------------------**/
    /**
     * 获取加分项列表附带是否签到的状态
     * @param finishStatus
     * @return
     */
    public static List<CreditConfItem> getItems(Integer finishStatus) {
        String retryKey = String.format(RedisConstant.CREDIT_CONF_ITEM_FLAG.getKey(),finishStatus);
        List<CreditConfItem> creditConfItems = JSON.parseArray(redis.RedisUtil.getInstance().get(retryKey),CreditConfItem.class);
        if(creditConfItems == null){
            ConfItemMapper confItemMapper = DbFactory.createBean(ConfItemMapper.class);
            List<ConfItem> items = confItemMapper.selectUse();
            CreditConfItem creditConfItem = null;
            creditConfItems = new ArrayList<CreditConfItem>();
            for(ConfItem confItem:items){
                creditConfItem = new CreditConfItem();
                String busName = confItem.getBusName();
                creditConfItem.setClassName(confItem.getClassName());
                creditConfItem.setClassType(confItem.getClassType());
                creditConfItem.setImageUrl(confItem.getImageUrl());
                creditConfItem.setLinkUrl(confItem.getLinkUrl());
                creditConfItem.setSubTitle(confItem.getSubTitle());
                creditConfItem.setTitle(confItem.getTitle());
                if(busName.equals("签到")){
                    creditConfItem.setFinishStatus(finishStatus);
                }
                creditConfItems.add(creditConfItem);
            }
            if(items!=null)
            redis.RedisUtil.getInstance().set(retryKey, JSON.toJSONString(creditConfItems), DateUtil.getRedisExpired().intValue());
        }

        return creditConfItems;
    }

    /**
     * 获取业务id
     * @param busType
     * @return
     */
    public static int getBusId(String busType) {
        int busId = 0;
        String keyValue = String.format(RedisConstant.CREDIT_CONF_BUS.getKey(),busType);
        String obj = redis.RedisUtil.getInstance().get(keyValue);
        if(StringUtils.isNotEmpty(obj)){
            ConfBus confBus = JSON.parseObject(obj,ConfBus.class);
            busId = confBus.getBusId();
        }else{
            ConfBusMapper confBusMapper = DbFactory.createBean(ConfBusMapper.class);
            ConfBus confBus = confBusMapper.getEntity(busType);
            if(confBus!=null){
                busId = confBus.getBusId();
            }else{
                confBus = new ConfBus();
                confBus.setBusId(0);
                confBus.setBusName(busType);
            }
            redis.RedisUtil.getInstance().set(keyValue,JSON.toJSONString(confBus),DateUtil.getRedisExpired().intValue());
        }
        return busId;
    }
    public static void initAll(){
        getBusId("签到");
        getBusId("任务");
        getItems(0);
        getItems(1);
        getCreditScore();
        getTodayScoreLimit();
        getTodayTaskScoreLimit();
        getTaskScoreLimit();
        getSignScore();
        getSignScoreLimit();
        getRatio();
        getRatio(401);
    }
}
