package containers;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhongxiaotian on 2018/7/26.
 */
public class ArrayUtil {


    public static boolean arrayContains(Object[] a, Object source){
        return ArrayUtils.contains(a, source);
    }

    public static List<Integer> IntArrayToIntegerList(int[] t){
        List<Integer> list = Arrays.stream(t).boxed().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        return list;
    }

    public static <T> T[] addTwoArray(T[] source1, T[] source2){
        return (T[]) ArrayUtils.addAll(source1, source2);
    }



}
