package file;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * 将word文件解压缩 获得其中的xml修改得到xml模板
 */
public class Xml2Excel {
    private static Xml2Excel tplm = null;
    private Configuration cfg = null;

    private static final String path = null;

    private Xml2Excel() {
        cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(new File(path));
        } catch (Exception e) {

        }
    }

    private static Template getTemplate(String name) throws IOException {
        if(tplm == null) {
            tplm = new Xml2Excel();
        }
        return tplm.cfg.getTemplate(name);
    }

    /**
     * xml来自word解压缩出来的xml
     * @param templatefile 模板文件
     * @param param        需要填充的内容
     * @param out          填充完成输出的文件
     * @throws IOException
     * @throws TemplateException
     */
    public static void process(String templatefile, Map param , Writer out) throws IOException, TemplateException{
//        templatefile="laodong.xml";
        Template template= Xml2Excel.getTemplate(templatefile);
        template.setOutputEncoding("UTF-8");
        //合并数据
        template.process(param, out);
        if(out!=null){
            out.close();
        }
    }
}
