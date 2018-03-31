package hive;

/**
 * copy from
 * https://github.com/jaysensharma/MiddlewareMagicDemos/blob/master/HDP_Ambari/Hive/HiveJavaClient/src/main/java/client/HiveClientDemo.java
 */
public class HiveClientDemo {

    private static String commonDriverName = "org.apache.hive.jdbc.HiveDriver";
//    private static String commonDriverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://ip:port/default;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2;";

    private static String userName = "user";
    private static String userPass = "pwd";
//    public static void main(String[] args) throws Exception {
//        Class.forName(commonDriverName);
//        Connection con = DriverManager.getConnection(url, userName, userPass);
//        System.out.println("\n\t Got Connection: " + con);
//
//        System.out.println("\n\t Listing 'default' Database tables of hive.");
//        Statement stmt = con.createStatement();
//        String sql = "show tables";
//        System.out.println("Executing Query: " + sql);
//        ResultSet rs = stmt.executeQuery(sql);
//        while (rs.next()) {
//            System.out.println(rs.getString(1));
//        }
//    }

}
