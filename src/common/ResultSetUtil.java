package common;

import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ResultSet 转换为指定的 Bean 对象
 */
public class ResultSetUtil<T> {

    /**
     * 将jdbc查询返回转化为制定的对象
     * @param rs
     * @param targetClass
     * @return
     */
    public List<T> resultSetToObject(ResultSet rs, Class targetClass){

        if(rs == null || targetClass == null){
            return null;
        }

        List<T> list = new ArrayList<T>();
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            Field[] fields = targetClass.getDeclaredFields();
            while(rs.next()){
                T bean = (T) targetClass.newInstance();
                for(int x = 1; rsmd.getColumnCount() >= x; x++){
                    String columnName = rsmd.getColumnName(x);
                    Object columnValue = rs.getObject(x);
                    for(Field field : fields){
                        String name = field.getName();
                        Annotation annotation = field.getAnnotation(Column.class);
                        if(annotation != null){
                            Column column = (Column) annotation;
                            name = column.name();
                        }
                        if(name.equals(columnName)){
                            BeanUtils.setProperty(bean, field.getName(), columnValue);
                            break;
                        }
                    }
                }
                list.add(bean);
            }

        }catch (SQLException var1){
            var1.printStackTrace();
        }catch (InstantiationException var2){
            var2.printStackTrace();
        }catch (IllegalAccessException var3){
            var3.printStackTrace();
        }catch (InvocationTargetException var4){
            var4.printStackTrace();
        }finally {

        }

        return list;
    }


    /**
     * 将 jdbc 查询返回转化为 List<Map>
     * 未对 key 值做任何处理
     * @param rs
     * @return
     * @throws SQLException 异常直接抛出
     */
    public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException{

        ResultSetMetaData rsmd = rs.getMetaData();
        int rowNum = rsmd.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<>(rowNum);

        Map<String, Object> map;
        while(rs.next()){
            map = new HashMap<>(rowNum);
            for(int i=1; i<=rowNum; ++i){
                map.put(rsmd.getColumnName(i), rs.getObject(i));
            }
            if(!map.isEmpty()){
                list.add(map);
            }
        }

        return list;
    }
}
