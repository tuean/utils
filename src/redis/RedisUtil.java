package redis;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisUtil {

    private String redis = "";

    static JedisPool jp;

    public void init(){
        jp=new JedisPool(redis);
    }

    public String set(String key, String value) {
        Jedis j = jp.getResource();
        return j.set(key, value);
    }

    public boolean exists(String key) {
        Jedis j = jp.getResource();
        return j.exists(key);
    }

    public long hset(String key, String e, String value) {
        Jedis j = jp.getResource();
        return j.hset(key, e, value);
    }

    public long expire(String key, int time) {
        Jedis j = jp.getResource();
        return j.expire(key, time);
    }

    public long del(String key) {
        Jedis j = jp.getResource();
        return j.del(key);
    }

    public String hget(String key, String e) {
        Jedis j = jp.getResource();
        return j.hget(key, e);
    }

    public String setJSON(String key, Object value) {
        Jedis j = jp.getResource();
        return j.set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        Jedis j = jp.getResource();
        return j.get(key);
    }

    public  <T> T getJSON(String key, Class<T> clazz) {
        Jedis j = jp.getResource();
        String o = j.get(key);
        return o == null ? null : JSON.parseObject(o, clazz);
    }

    public long setnx(String key, String value) {
        Jedis j = jp.getResource();
        return j.setnx(key, value);
    }

    public long setnxJSON(String key, Object value) {
        Jedis j = jp.getResource();
        return j.setnx(key, JSON.toJSONString(value));
    }

    public Long hsetnx(String key, String e, String value) {
        Jedis  j = jp.getResource();
        return j.hsetnx(key, e, value);
    }

}
