package file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 递归删除文件夹下所有的文件
     * @param file
     * @param ignoreFile
     */
    public static void deleteAllFilesInFolder(File file, String ignoreFile){
        try{
            if(file.isDirectory()){
                String[] children = file.list();
                for(String x : children){
                    deleteAllFilesInFolder(new File(file, x), ignoreFile);
                }
            }
            if(ignoreFile == null || !ignoreFile.equals(file.getName())){
                file.delete();
                logger.info("删除文件 " + file.getName() + "成功");
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.info("删除文件 " + file.getName() + "失败");
        }
    }


    /**
     * 读取文件所有的内容
     *
     * @param path
     * @param encoding
     * @return
     */
    public static String readAllFile(String path, Charset encoding) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        } catch (IOException var){

        }
        return null;
    }

    /**
     * 按行读取文件的所有内容
     *
     * @param path
     * @param encoding
     * @return
     */
    public static List readFileByLine(String path, Charset encoding){
        try {
            List<String> lines = Files.readAllLines(Paths.get(path), encoding);
            return lines;
        } catch (Exception var){

        }
        return null;
    }

    public static String getProjectPath() {
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        return dir.toString();
    }

}
