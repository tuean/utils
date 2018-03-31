package hive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * hive_test 表操作
 * @date 2018/3/1
 */
@Component
public class HiveHelper {

    private static Logger logger = LoggerFactory.getLogger(HiveHelper.class);

    @Autowired
    @Qualifier("hiveJdbcTemplate")
    private JdbcTemplate hiveJdbcTemplate;

    private static JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init(){
        logger.info("HiveTestHelper init ");
        jdbcTemplate = this.hiveJdbcTemplate;
    }

    public static List selectHiveTestAll(String table){
        String sql = "select * from " + table;
        return jdbcTemplate.queryForList(sql);
    }

    public static List selectHiveTestById(String table, Integer id){
        if(id == null){
            return null;
        }
        String sql = "select * from " + table + " where id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{id});
    }

    public static List selectHiveTestByNameOrderById(String table, String name){
        if(name == null){
            return null;
        }
        String sql2 = "select * from "+ table + " where name = ? order by id desc";
        return jdbcTemplate.queryForList(sql2, new Object[]{name});
    }

    public static Integer countByName(String table, String name){
        if(name == null){
            return null;
        }
        String sql3 = "select count(1) from " + table + " where name = ?";
        return jdbcTemplate.queryForObject(sql3, new Object[]{name}, Integer.class);
    }

    public static void insertIntoHiveTest(String table, Map<String, Object> map){
        Integer id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String sql4 = "insert into table " + table + " values (?, ?)";
        //返回值为0
        jdbcTemplate.update(sql4, new Object[]{id, name});
    }

    public static void batchInsertIntoHiveTest(String table, List<Map<String, Object>> list){
        String sql5 = "insert into table " + table + " values (?, ?)";
        jdbcTemplate.batchUpdate(sql5, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, (Integer) list.get(i).get("id"));
                ps.setObject(2, list.get(i).get("name"));
            }
            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
