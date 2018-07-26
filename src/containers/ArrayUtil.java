package containers;

import org.apache.commons.lang.ArrayUtils;

/**
 * Created by zhongxiaotian on 2018/7/26.
 */
public class ArrayUtil {


    public static boolean arrayContains(Object[] a, Object source){
        return ArrayUtils.contains(a, source);
    }


    public static <T> T[] addTwoArray(T[] source1, T[] source2){
        return (T[]) ArrayUtils.addAll(source1, source2);
    }



}
