package common;

import java.util.UUID;

public class UuidUtil {


    public static String uuid() {
        return UUID.randomUUID().toString();
    }


    public static void main(String[] args) {
        System.out.println(uuid());
    }

}
