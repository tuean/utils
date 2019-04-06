package file;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * hdfs操作相关
 */
@Component
public class HdfsUtils {
	
	@Value("${hdfs.nameNode}")
	private String nameNode;

	private Configuration conf;
	
	/**
	 * 获取 创建文件的流
	 * @param path
	 * @return
	 * @throws IOException
	 * 注意使用完毕后关闭流
	 */
	public FSDataOutputStream getCreate(String path) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		FSDataOutputStream hdfsOutStream = fs.create(new Path(path));
		return hdfsOutStream;
	}
	
//	@PostConstruct
	public void init() {
		Configuration conf = new Configuration();
	    conf.set("fs.defaultFS", nameNode);
	    this.conf = conf;
	}
	
//	@PreDestroy
	public void destroy() {
		if(conf != null) {
			conf = null;
		}
	}
}
