package file;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;

/**
 * Created by niushuang on 2017/10/31.
 */
public class Word2Html {

    public final static String  tempPath = System.getProperty("catalina.home") + File.separator  + "webapps" + File.separator + "files";


    /**
     * 输出html文件
     * @param content
     * @param path
     */
    public static void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        org.jsoup.nodes.Document doc = Jsoup.parse(content);
        content=doc.html();
        try {
            File file = new File(path);
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
            bw.write(content);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null){
                    bw.close();
                }
                if (fos != null){
                    fos.close();
                }
            } catch (IOException ie) {

            }
        }
    }


    /**
     * docx格式word转换为html
     * @param document 最新生成的word对象
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     * @return html文本内容
     */
    public static String docx2Html(XWPFDocument document) throws TransformerException, IOException, ParserConfigurationException {

        XHTMLOptions options = XHTMLOptions.create().indent(4);
        // 导出图片
        File imageFolder = new File(tempPath);
        options.setExtractor(new FileImageExtractor(imageFolder));
        // URI resolver
        options.URIResolver(new FileURIResolver(imageFolder));

        OutputStream out = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(document, out, options);
        StringBuffer sb = new  StringBuffer();
        String head = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        String str = baos.toString();

        return str;
    }
}

