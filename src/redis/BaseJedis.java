package redis;

import org.springframework.util.StopWatch;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.List;

import static common.StringUtil.randomString;

public class BaseJedis {

    public static void main(String[] args) throws IOException {
        int size = 1500000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String key = "test";

        Jedis jedis = new Jedis("ip", 6379);
        jedis.auth("password");
        jedis.select(3);

        Pipeline p = jedis.pipelined();
        for (int x = 0; x < size; x++) {
            String tmpValue = randomString(19);
            p.rpush(key, tmpValue);

            if (x % 1000 == 0) {
                System.out.println("当前进度：" + x);
            }
        }
        p.close();


        stopWatch.stop();
        System.out.println("总插入耗时：" + stopWatch.getTotalTimeMillis() + "ms");

        stopWatch.start();
        List<String> list = jedis.lrange(key, 0, -1);
        stopWatch.stop();
        System.out.println("获取耗时：" + stopWatch.getLastTaskTimeMillis() + "ms");

        jedis.close();
    }
}
