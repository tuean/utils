package email;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Calendar;

/**
 * eml格式文件处理
 */
public class EmlUtil {

    /**
     * eml转html
     * @param fileString
     * @return
     */
    public static String emlToHtml(String fileString){
        if (StringUtils.isNotEmpty(fileString)) {
            fileString = fileString.substring(fileString.indexOf("<"));
        }
        if (StringUtils.isNotEmpty(fileString)) {
            fileString = fileString.substring(0, fileString.lastIndexOf(">") + 1);
        }
        return fileString;
    }

    /**
     * 将eml的附件转为html
     *
     * @param path
     * @param name
     * @return
     */
    public static String turnEmltoHtml(String path, String name){

        // TODO: 2017/4/14  设置零时文件夹
        //设置存储的文件夹
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        String fileDir = "";
        fileDir = System.getProperty("catalina.home") + File.separator + "webapps" + File.separator + "File" + File.separator + "Application" + File.separator;
        String folder = fileDir + year + "-" + month;
        File folderNow = new File(folder);
        if(folderNow.getParentFile().isDirectory() && !folderNow.exists()){
            folderNow.mkdirs();
        }

        //存储文件
        String outPath;
        outPath = folder + File.separator + name + ".html";
        Integer n = 1;
        while(new File(outPath).exists()){
            outPath = folder + File.separator + name + n + ".html";
            n++;
        }

        //转化eml为html格式
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            Reader reader = null;
            reader = new InputStreamReader(new FileInputStream(path));
            StringBuffer content = new StringBuffer();
            String contentString = null;
            // 读入多个字符到字符数组中，charread为一次读取字符数
            try {
                while ((charread = reader.read(tempchars)) != -1) {
                    // 同样屏蔽掉\r不显示
                    if ((charread == tempchars.length)
                            && (tempchars[tempchars.length - 1] != '\r')) {
                        content.append(tempchars);
                    } else {
                        for (int i = 0; i < charread; i++) {
                            if (tempchars[i] == '\r') {
                                continue;
                            } else {
                                content.append(tempchars[i]);
                            }
                        }
                    }
                }
                Integer firstMatch = null;
                firstMatch = content.indexOf("<");
                if(firstMatch != null){
                    contentString = content.substring(firstMatch, content.length());
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            if(contentString != null){
                File file = new File(outPath);

                if(!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                FileWriter fw;
                try {
                    fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(contentString);
                    bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return outPath;
    }
}
