package common;

/**
 * Created by zhongxiaotian on 2018/7/26.
 */
public class MathUtils {


    public static int getRandomInt(int start, int end){
        return start + (int) (Math.random() * (end - start + 1));
    }


}
