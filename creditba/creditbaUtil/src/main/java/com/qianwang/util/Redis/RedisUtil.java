package com.qianwang.util.Redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class RedisUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    private JedisSentinelPool pool;

    public void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            log.error("redisutil error : {}", e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);

        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public boolean exist(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public void del(String... key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }
    public void batchDel(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> set = jedis.keys(key +"*");
            Iterator<String> it = set.iterator();
            while(it.hasNext()){
                String keyStr = it.next();
                jedis.del(keyStr);
            }
        } catch (Exception e) {
            log.error("RedisUtil has redis batchDel error:", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public void lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public void rpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.rpush(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpop(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public void incr(String key, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.incrBy(key, count);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public void hdel(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hdel(key, fields);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 向key赋值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);

        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 向key赋值
     *
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);

        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public void set(String key, String value, int expired) {
        set(key, value);
        expire(key, expired);
    }

    /**
     * 获取key的值
     *
     * @param key
     * @return
     */

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String value = jedis.get(key);
            return value;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 获取key的值
     *
     * @param key
     * @return
     */

    public byte[] get(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 将多个field - value(域-值)对设置到哈希表key中。
     *
     * @param key
     * @param map
     */
    public void hmset(String key, Map<String, String> map) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.hmset(key, map);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 将多个field - value(域-值)对设置到哈希表key中。
     *
     * @param key
     * @param fields
     */
    public List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 给key赋值，并生命周期设置为seconds
     *
     * @param key
     * @param seconds 生命周期 秒为单位
     * @param value
     */

    public void setex(String key, int seconds, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 为给定key设置生命周期
     *
     * @param key
     * @param seconds 生命周期 秒为单位
     */
    public void expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 从哈希表key中获取field的value
     *
     * @param key
     * @param field
     */

    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            String value = jedis.hget(key, field);
            return value;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * @param key
     * @param field
     * @param dbIndex
     * @return
     */
    public String hget(String key, String field, int dbIndex) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.select(dbIndex);
            String value = jedis.hget(key, field);
            return value;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 从哈希表key中获取field的value MAP
     *
     * @param key
     */
    public Map<String, String> hget(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Map<String, String> value = jedis.hgetAll(key);
            return value;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * set add 操作
     *
     * @param key
     * @param member
     * @return
     */
    public Long sadd(String key, String... member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, member);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * <p>通过key获取set中的差集</p>
     * <p>以第一个set为标准</p>
     *
     * @param keys 可以使一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Set<String> sdiff(String... keys) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.sdiff(keys);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            log.error(e.getMessage());
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
        return res;
    }

    public Set<String> getByRange(String key, int start, int end) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.smembers(key);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            log.error(e.getMessage());
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
        return res;
    }

    public String sPop(String key, int start, int end) {
        Jedis jedis = null;
        String res = null;
        try {
            jedis = pool.getResource();
            res = jedis.spop(key);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            log.error(e.getMessage());
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
        return res;
    }

    public List<String> listByRange(String key, long start, long end) {
        Jedis jedis = null;
        List<String> res = null;
        try {
            jedis = pool.getResource();
            res = jedis.lrange(key, start, end);
        } catch (Exception e) {
            pool.returnBrokenResource(jedis);
            log.error(e.getMessage());
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
        return res;
    }

    /**
     * set pop
     *
     * @param key
     * @return
     */
    public String setPop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.spop(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * set pop
     *
     * @param key
     * @return
     */
    public long setLen(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public void srem(String key, String... member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.srem(key, member);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * zadd
     *
     * @param key
     * @param map
     */
    public void zadd(String key, Map<String, Double> map) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zadd(key, map);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public boolean sismember(String key, String member) {
        boolean flag = false;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            flag = jedis.sismember(key, member);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
        return flag;
    }

    /**
     * sadd stu zhangsan lisi wangwu #新增
     * smembers stu    #得到set的所有member
     * sismember tech jim #jim 是set的member   如果值是set的member返回1，否则，返回0
     * scard key  返回set的member个数，如果set不存在，返回0
     * srandmember key  从set中返回一个随机member
     * spop key  移除并返回一个随机member
     * srem key member [member ...]    移除一个或多个member
     * smove source destination member   将source中的member移动到destination
     * sunion key[key...]   多个set的并集
     * sunionstore destination key [key ...]   求多个set并集，并把结果存储到destination
     * sinter key[key...] 多个set的交集
     * sinterstore destination key [key ...]   把多个set的交集结果存储到destination
     *
     * @param key
     * @param members
     */
    public void sadd(String key, List<String> members) {
        Jedis jedis = null;
        int size = members.size();
        String[] arr = new String[size];

        for (int i = 0; i < size; i++) {
            arr[i] = members.get(i);
        }
        try {
            jedis = pool.getResource();
            jedis.sadd(key, arr);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public void zadd(final String key, final double score, final String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.zadd(key, score, member);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public Set<String> zrevrangeByScore(String key, long max, long min) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * zcard
     *
     * @param key
     */
    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public Double zscore(int idx, String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            if (idx != 0) {
                jedis.select(idx);
            }
            return jedis.zscore(key, member);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnBrokenResource(jedis);
            }
        }
    }

    /**
     * get len
     *
     * @param key
     * @return
     */
    public long getlen(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.llen(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * @param key
     * @return
     */
    public long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decr(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public long decr(String key, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decrBy(key, count);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpop(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 获取key的TTL
     *
     * @param key mao.wang
     * @return
     */
    public long ttl(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ttl(key);
        } catch (Exception e) {
            log.error("RedisUtil has redis getTtl error:", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    public Set<String> getKeys(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.keys(key);
        } catch (Exception e) {
            log.error("RedisUtil has redis getTtl error:", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }

    /**
     * 模糊删除
     *
     * @param pattern
     * @return
     */
    public boolean delPattern(String pattern) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> keys = jedis.keys(pattern);
            for (String str : keys) {
                jedis.del(str);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil has redis getTtl error:", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
    }
    /**
     * mao.wang 2016/08/13
     * @param key
     * @param size
     * @return
     */
    public List<Object> lPipelinePop(String key,int size){
        List<Object> resultList=null;
        Jedis jedis = null;
        Pipeline p=null;
        try {
            jedis = pool.getResource();
            p = jedis.pipelined();
            for (int i = 0; i < size; i++) {
                p.lpop(key);
            }
            resultList=p.syncAndReturnAll();
        } catch (Exception e) {
            log.error("RedisUtil-lPipelinePop():{}", e);
            if(null!=p){
                resultList=p.syncAndReturnAll();
            }
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
        } finally {
            if (pool != null) {
                pool.returnResource(jedis);
            }
        }
        return resultList;
    }

    /**
     * mao.wang 2016/08/13
     * @param key
     * @param list
     */
    public int  lPipelinePush(String key, List<Object> list) {
        Jedis jedis = null;
        int i=0;
        Pipeline p =null;
        while (i<list.size()){
            try{
                if(null==jedis){
                    jedis = pool.getResource();
                }
                if(null==p){
                    p=jedis.pipelined();
                }
                p.lpush(key,list.get(i).toString());
                i++;
            }catch(Exception e){
                log.info("RedisUtil-lPipelinePush():LPush异常等待5S重试:{}",e);
                exceptionSleepThread();
            }
        }
        if (pool != null) {
            pool.returnBrokenResource(jedis);
        }
        return i;
    }
    public static void exceptionSleepThread(){
        try{
            Thread.sleep(5000L);
        } catch (Exception e) {
            log.error("RedisUtil-exceptionSleepThread():等待异常：{}", e);
        }
    }

    public void setPool(JedisSentinelPool pool) {
        this.pool = pool;
    }
}
