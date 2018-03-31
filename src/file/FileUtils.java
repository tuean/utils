package file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 递归删除文件夹下所有的文件
     * @param file
     * @param ignoreFile
     */
    private static void deleteAllFilesInFolder(File file, String ignoreFile){
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
}
