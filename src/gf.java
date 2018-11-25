import common.DateUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class gf {


    /**
     * 计算丹姐周末啥时候休息
     */
    public static void calNotWorkDate() {
        LocalDate end = LocalDate.of(2019, 1, 30);
        LocalDate date = LocalDate.of(2018, 11, 25);

        String[] source = new String[]{"夜班", "休息，晚上可约", "休息，可约", "休息，可约", "日班", "中班"};
        int n = 0;
        while (date.isBefore(end)) {
            if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                System.out.println(DateUtil.localDateToString(date) + " " + date.getDayOfWeek() + " " + source[n]);
            }
            n = n > 4 ? 0 : (n + 1);
            date = date.plusDays(1);
        }
    }

    public static void main(String[] args) {
        calNotWorkDate();
    }
}
