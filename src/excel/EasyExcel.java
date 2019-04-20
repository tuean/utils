package excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class EasyExcel {


    /**
     * 使用easyExcel生成
     *
     * @param data
     * @param clazz
     * @param path
     */
    private static void createExcel(List<BaseRowModel> data, Class clazz, String path) {
        String filePath = path == null ? "/tmp/78.xlsx" : path;
        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(1, 0, clazz);
            writer.write(data, sheet1);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // todo 使用easyExcel解析excel
}
