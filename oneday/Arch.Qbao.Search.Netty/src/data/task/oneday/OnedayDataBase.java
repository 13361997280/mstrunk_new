package data.task.oneday;

import com.qbao.config.DataSourceEnvironment;
import com.qbao.config.DbFactory;
import com.qbao.dao.TResultOnedayTimeaxisDMapper;
import com.qbao.dto.TResultOnedayTimeaxisD;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import redis.RedisUtil;

import java.util.List;

/**
 * oneday 数据库数据同步脚本。已抛弃
 *
 * @author song.j
 * @create 2017-09-14 09:09:44
 **/
@Deprecated
public class OnedayDataBase {

    private static ESLogger logger = Loggers.getLogger(OnedayDataBase.class);

    private static final int DATA_SIZE = 5000; //一次取数据库的数量

    private static final String ONEDAY_INCR_KEY = "tengrong:oneday:oneday:dataindex";

    RedisUtil redisUtil = new RedisUtil();

    /**
     * 定时启动数据处理
     */
    public void start() {
        //大数据组 数据源
        TResultOnedayTimeaxisDMapper bigDataMapper = DbFactory.createBean(TResultOnedayTimeaxisDMapper.class, DataSourceEnvironment.BIGDATA);
        //oneday项目 数据源
        TResultOnedayTimeaxisDMapper onedayMapper = DbFactory.createBean(TResultOnedayTimeaxisDMapper.class, DataSourceEnvironment.ONEDAY);

        //循环处理数据
        eachHandlData(bigDataMapper, onedayMapper);

    }

    /**
     * 递归
     * 数据转移
     */
    public void eachHandlData(TResultOnedayTimeaxisDMapper bigDataMapper, TResultOnedayTimeaxisDMapper onedayMapper) {

        //获取大数据分析好的数据库表
        List<TResultOnedayTimeaxisD> datalist = bigDataMapper.select(getStartNum(), DATA_SIZE);

        if (datalist.isEmpty()) {
            return;
        }
        //批量插入数据
        try {
            onedayMapper.insertBatch(datalist);
        } catch (Exception e) {
            logger.error("oneday insertbatch error ", e);
            return;
        }

        eachHandlData(bigDataMapper, onedayMapper);
    }


    /**
     * 获取分页页数
     * <p>
     * 这里用到了redis incr 保证集群下所处理的数据不重复
     *
     * @return
     */
    public int getStartNum() {

        Long index = (redisUtil.incr(ONEDAY_INCR_KEY) - 1) * DATA_SIZE;

        return Integer.parseInt(index.toString());
    }

    /**
     * 数据取完 删除这个KEY
     */
    public void endData() {
        redisUtil.del(ONEDAY_INCR_KEY);
    }
}
