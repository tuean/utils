package common;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;

@Component
public class JSONObjectUtil {

    private static Logger logger = LoggerFactory.getLogger(JSONObjectUtil.class);

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public JSONObjectUtil(){

    }

    public static JSONObject getEnumValue(Object object, Class clazz) {

        try{
            PropertyDescriptor[] ps = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            for(PropertyDescriptor prop : ps) {
                if (!prop.getName().contains("ForES")) {
                    Object srcVal = null;

                    //基本数据类型
                    if (prop.getPropertyType().isPrimitive()) {

                        setValue(object, prop.getReadMethod(), prop.getWriteMethod());

                        //包装类型
                    } else if (isWrapType(prop.getPropertyType().getTypeName())) {

                        setValue(object, prop.getReadMethod(), prop.getWriteMethod());

                        //集合类型
                    } else if (prop.getPropertyType().isInterface() &&
                            prop.getPropertyType().getInterfaces().length > 0 &&
                            prop.getPropertyType().getInterfaces()[0] == Collection.class) {

                        Object obj = getValue(object, clazz, prop.getReadMethod().getName());
                        if (obj != null && obj instanceof Collection) {
                            Collection srcList = (Collection) obj;
                            if (srcList != null && srcList.toArray().length > 0) {
                                Class collClazz = srcList.toArray()[0].getClass();
                                for (Object srcParam : srcList) {
                                    getEnumValue(srcParam, collClazz);
                                }
                            }
                        }

                        //date类型
                    } else if (prop.getPropertyType().getTypeName().equals("java.util.Date")) {
                        continue;

                        //自定义数据类型
                    } else {
                        Class propertyClass = prop.getPropertyType();
                        srcVal = getValue(object, clazz, prop.getReadMethod().getName());
                        getEnumValue(srcVal, propertyClass);
                    }
                }
            }
        }catch (Exception var){
            var.printStackTrace();
        }

//        JSONObject re = JSON.parseObject(JSON.toJSONString(object));
//        JSONObject re = (JSONObject) JSONObject.toJSON(object);

        JSONObject re = null;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(object);
            re = JSONObject.parseObject(jsonStr);
        }catch (JsonProcessingException var1){
            logger.info("实体类转json报错" + var1);
        }

        return re;
    }


    /**
     * 获取属性值
     * @param obj
     * @param clazz
     * @param methodName
     * @return
     */
    public static Object getValue(Object obj,Class clazz,String methodName){
        Object value=null;
        try {
            Method method=clazz.getMethod(methodName);
            if(method !=null){
                value=method.invoke(obj);
            }
        } catch (Exception e) {
            logger.info("getValue报错" + e.getMessage());
        }
        return value;
    }

    /**
     * 设置属性值
     * 只设置BizEnum中存在的 code 转 value
     * @param obj
     * @param getMethod
     * @param setMethod
     */
    public static void setValue(Object obj, Method getMethod, Method setMethod){
        Object value=null;
        try{
            if(getMethod != null){
                value = getMethod.invoke(obj);
            }

            if(value != null){

                if(setMethod != null){
                    setMethod.invoke(obj, value);
                }
            }
        }catch (Exception var){
            logger.info("setValue报错" + var.getMessage());
        }
    }

    /**
     * 日期需要经过处理
     * @param obj
     * @param getMethod
     * @param setMethod
     */
    public static void setDateValue(Object obj, Method getMethod, Method setMethod){
        Object value = null;
        try{
            if(getMethod != null){
                value = getMethod.invoke(obj);
            }
            if(value != null){
                String formatDate = SDF.format(value);
                if(formatDate != null && setMethod != null){
                    setMethod.invoke(obj, formatDate);
                }

            }
        }catch (Exception var){
            logger.info("setDateValue报错" + var.getMessage());
        }
    }


    /**
     * 是否是包装类型
     * @param typeName
     * @return
     */
    public static boolean isWrapType(String typeName){
        String[] types = {"java.lang.Integer",
                "java.lang.Double",
                "java.lang.Float",
                "java.lang.Long",
                "java.lang.Short",
                "java.lang.Byte",
                "java.lang.Boolean",
                "java.lang.Character",
                "java.lang.String"};
        return ArrayUtils.contains(types, typeName);
    }

   /* public static void main(String[] args) {
        EstalentInfo esInfo = new EstalentInfo();
        TalentOpenSea talentOpenSea = new TalentOpenSea();
        talentOpenSea.setSex("10101");
        talentOpenSea.setBssid(677789L);
        esInfo.setTalentOpenSea(talentOpenSea);
        PoolWorkexpInfo poolWorkexpInfo = new PoolWorkexpInfo();
        poolWorkexpInfo.setStartDate(new Date());
        poolWorkexpInfo.setStatus("11101");
        List<PoolWorkexpInfo> poolList = new ArrayList<>();
        poolList.add(poolWorkexpInfo);
        esInfo.setWorkList(poolList);
        JSONObject result = getEnumValue(esInfo, EstalentInfo.class);
        System.out.println(result);
    }*/

}
