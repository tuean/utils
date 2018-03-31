package presto;


import com.alibaba.druid.pool.DataSourceNotAvailableException;
import common.ApplicationContextHolder;
import common.ResultSetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 由于当前使用的presto-jdbc0.189包中实现Connection.prepareStatement 的方法全部都是抛出异常，无法使用jdbcTemplate
 * 因此需要手动创建presto的query等方法
 *
 * @date 2018/2/9
 * question from
 * https://github.com/prestodb/presto/issues/1195
 *
 * resolve by
 * presto 提供 prepare sql命令 随后使用 execute 执行该 query
 * note： 此query仅在当前session生效 故无法通过cli使用
 *
 * 来源：
 * https://prestodb.io/docs/0.189/sql/execute.html
 * https://programtalk.com/vs/presto/presto-main/src/test/java/com/facebook/presto/execution/TestPrepareTask.java/
 */
@Component
public class PrestoConnectHelper {

    private static Logger logger = LoggerFactory.getLogger(PrestoConnectHelper.class);


    /**
     * sql超时时间 默认为-1
     */
    private static int TIMEOUT = 60000;

    @Autowired
    @Qualifier("prestoDataSource")
    private DataSource dataSource;

    /**
     * spring boot应用中 如果presto节点连接失败 知道停止该应用
     * @throws DataSourceNotAvailableException
     */
    @PostConstruct
    public void init() throws DataSourceNotAvailableException {
        if(dataSource == null){
            //强制停止spring boot应用
            //方法是来自EmbeddedWebApplicationContext 类中的 stopAndReleaseEmbeddedServletContainer()
            EmbeddedWebApplicationContext context = (EmbeddedWebApplicationContext) ApplicationContextHolder.getApplicationContext();
            context.getEmbeddedServletContainer().stop();
            throw new DataSourceNotAvailableException(new Throwable("no presto datasource"));
        }
    }

    public List query(String sql) throws SQLException{
        return this.query(sql, null);
    }


    /**
     * presto 查询方法
     * example:
     * sql = select * from table where id = ? or name = ?
     * params = {1, "Jack"}
     *
     * @param sql throws SQLException if none
     * @param params null if none
     * @return
     * @throws SQLException
     */
    public List query(String sql, Object[] params) throws SQLException{

        //生成一个随机方法名
        Double postfix = Math.random();
        String fuc = "fuc" + postfix.toString().replace(".", "");

        List<Map<String, Object>> list = new ArrayList<>(1);

        //prepare 一个sql  随后通过 execute 上述方法名 执行该sql
        String prepareSql = "prepare " + fuc + " from " + sql;
        String executeSql = "execute " + fuc;
        String removeSql  = "deallocate prepare " + fuc;
        if(params != null && params.length > 0){
            executeSql = executeSql + " using " + arrayToString(params);
        }

        Connection con = DataSourceUtils.getConnection(dataSource);

        Statement statement = null;
        ResultSet rs = null;

        try{
            Connection conToUse = con;
            statement = conToUse.createStatement();
            //设置超时时间 默认取datasource设定值 其次取当前设定值
//            DataSourceUtils.applyTimeout(statement, dataSource, TIMEOUT);
            statement.execute(prepareSql);
            //执行查询sql
            rs = statement.executeQuery(executeSql);
            ResultSet rsToUse = rs;
            //数据转换为map
            list = ResultSetUtil.resultSetToList(rsToUse);
            //打印warn
            handleWarns(statement);
            //去掉此处的prepare
            statement.execute(removeSql);

        }catch (SQLException var1){
            throw var1;

        }finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(statement);
            DataSourceUtils.releaseConnection(con, dataSource);
        }

        return list;
    }


    /**
     * ResultSet中存在warning 直接抛出异常
     * @param stmt
     * @throws SQLException
     */
    private static void handleWarns(Statement stmt) throws SQLException{
        if(stmt != null && stmt.getWarnings() != null){
            throw new SQLWarningException("Warning not ignored", stmt.getWarnings());
        }
    }

    public static String arrayToString(Object[] src) {
       return Arrays.toString(src).replaceAll("[\\[\\]\\s]", "");
    }


}
