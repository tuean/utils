/**
 * Created by zhongxiaotian on 2018/7/26.
 */
public class others {

    /**
     * 跳出指定循环
     * copry from stackoverflow
     *
     * @param args
     */
    public static void main(String[] args) {
        outerloop:
        for (int i=0; i < 5; i++) {
            for (int j=0; j < 5; j++) {
                if (i * j > 6) {
                    System.out.println("Breaking");
                    break outerloop;
                }
                System.out.println(i + " " + j);
            }
        }
        System.out.println("Done");
    }

}
