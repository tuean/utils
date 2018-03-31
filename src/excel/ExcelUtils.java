package excel;

import common.BeanUtils;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel 工具类
 * @date 2018/3/2
 */
public class ExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 单个excel最大行数
     */
    private static final long MAX_ROW = 40000L;

    private static final String DEFAULT_TITLE = "excel.xlsx";


    /**
     * 创建excel表格
     * @param headerMap
     * @param contentList
     * @return null or XSSFWorkbook
     */
    public static XSSFWorkbook createExcel(LinkedHashMap<String, String> headerMap, List contentList, String title){

        if(contentList == null || contentList.isEmpty()){
            return null;
        }

        Map maps = new HashMap();

        // 第一步，创建一个webbook，对应一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workbook.createSheet(title);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        XSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格样式
//        XSSFCellStyle style = getHeadStyle(workbook); // 表头样式
        XSSFCell cell = null;
        if(headerMap != null && !headerMap.isEmpty()){
            // 迭代创建表头
            Integer x = headerMap.size();
            Iterator iterator = headerMap.entrySet().iterator();
            int i = 0;
            while(iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                cell = row.createCell(i);
                cell.setCellValue(String.valueOf(entry.getValue()));
                i++;
            }
            if(x > 0){
                int columnIndex = cell.getColumnIndex();
                //一下两行粗暴解决
                cell = row.createCell(columnIndex + 1);
            }
        }

        // 创建数据
        for (int i = 0; i < contentList.size(); i++) {

            row = sheet.createRow( i + 1);
            Map<String, Object> map;
            if( contentList.get(i) instanceof String){
                map = new HashMap<>();
                map.put("test", contentList.get(i));
            } else if (!(contentList.get(i) instanceof Map)) {
                map = BeanUtils.convertBean2Map(contentList.get(i));
            } else {
                map = (Map) contentList.get(i);
            }
            if(map == null){
                continue;
            }
            // 第四步，创建单元格，并设置值
            Iterator iter2 = map.entrySet().iterator();
            for (short j = 0; iter2.hasNext(); j++) {
                Map.Entry entry = (Map.Entry) iter2.next();
                Object obj = map.get(entry.getKey());
                if (obj != null) {
//                    cell.setCellStyle(style);
                    if (obj instanceof Date || obj instanceof java.sql.Date) {
                        cell = row.createCell(j);
                        cell.setCellValue(new SimpleDateFormat("yyyy/MM/dd").format(obj));
                    } else {
                        row.createCell(j).setCellValue(String.valueOf(obj));
                    }
                }
            }
        }

        return workbook;

    }


    /**
     * 指定一个路径存放excel
     * @param workbook
     * @param path
     * @param fileName
     * @return true or false
     */
    public static boolean save(XSSFWorkbook workbook, String path, String fileName){

        boolean flag = false;

        try{
            if(workbook == null){
                throw new NullPointerException("workbook is null");
            }
            File folder = new File(path);
            if(!folder.exists()){
                if(!folder.mkdirs()){
                    throw new FileNotFoundException("cannot create the parent folder of the given path");
                }
            }
            if(path.endsWith(File.separator)){
                path = path + File.separator;
            }
            File file = new File(path + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            workbook.close();
            logger.info(path + fileName + "created successfully");
            flag = true;

        }catch(Exception var){
            var.printStackTrace();
            flag = false;
        }

        return flag;
    }

    /**
     * 将workbook转换为bytes
     * 出现异常返回null
     * @param workbook
     * @return byte[] or null
     */
    public static byte[] convertWorkbookToBytes(XSSFWorkbook workbook){
        if(workbook == null){
            return null;
        }
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();
            return baos.toByteArray();
        }catch (Exception var){
            var.printStackTrace();
        }

        return null;
    }

//    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        for (int x = 0; x < 10; x++) {
//            list.add(String.valueOf(x));
//        }
//        XSSFWorkbook xssfWorkbook = createExcel(null, list, "test");
//        String path = "/Users/zhongxiaotian/Desktop/pic/";
//        String file = "test.xlsx";
//        save(xssfWorkbook, path, file);
//    }


}
