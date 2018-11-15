package common;

/**
 * Created by zhongxiaotian on 2018/7/26.
 */
public class MathUtils {


    public static int getRandomInt(int start, int end){
        return start + (int) (Math.random() * (end - start + 1));
    }


    /**
     * 这个判断是否素数的方法很巧妙
     *
     * @param temp
     * @return
     */
    private boolean isPrime(int temp) {
        for (int i = 3; i*i <= temp; i+=2) {
            if (temp % i == 0) {
                return false;
            }
        }

        return true;
    }


}
