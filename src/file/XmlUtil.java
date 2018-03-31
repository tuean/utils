package file;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class XmlUtil {
    private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);
    /**
     * 生成xml文件
     * @param args
     */
    public static String buildXml(String[] args) throws IOException {
        //DocumentHelper提供了创建Document对象的方法
        Document document = DocumentHelper.createDocument();
        //添加节点信息
        Element rootElement = document.addElement("modules");
        //这里可以继续添加子节点，也可以指定内容
        rootElement.setText("这个是module标签的文本信息");
        Element element = rootElement.addElement("module");

        Element nameElement = element.addElement("name");
        Element valueElement = element.addElement("value");
        Element descriptionElement = element.addElement("description");
        nameElement.setText("名称");
        //为节点添加属性值
        nameElement.addAttribute("language", "java");
        valueElement.setText("值");
        valueElement.addAttribute("language", "c#");
        descriptionElement.setText("描述");
        descriptionElement.addAttribute("language", "sql server");
        //将document文档对象直接转换成字符串输出
        System.out.println(document.asXML());
        return document.asXML();
    }

    /**
     * 解析xml
     * @param args
     */
    public static void parseXml(String[] args) throws FileNotFoundException, DocumentException {
        InputStream inputStream = new FileInputStream(new File(""));
        //创建SAXReader读取器，专门用于读取xml
        SAXReader saxReader = new SAXReader();
        //必须指定文件的绝对路径
        Document document = saxReader.read(new File(""));
        Element rootElement = document.getRootElement();
        Iterator<Element> modulesIterator = rootElement.elements("module").iterator();
        while(modulesIterator.hasNext()){
            Element moduleElement = modulesIterator.next();
            Element nameElement = moduleElement.element("name");
            System.out.println(nameElement.getName() + ":" + nameElement.getText());
            Element valueElement = moduleElement.element("value");
            System.out.println(valueElement.getName() + ":" + valueElement.getText());
            Element descriptElement = moduleElement.element("descript");
            System.out.println(descriptElement.getName() + ":" + descriptElement.getText());
        }

    }


    /**
     *
     * @param documentFile 动态生成数据的docunment.xml文件
     * @param docxTemplate docx的模板
     * @throws ZipException
     * @throws IOException
     */

    public void outDocx(File documentFile, String docxTemplate, String toFilePath) throws Exception {

        try {
            String fileName = docxTemplate;
            File docxFile = new File(fileName);
            ZipFile zipFile = new ZipFile(docxFile);
            Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(toFilePath));
            int len=-1;
            byte[] buffer=new byte[1024];
            while(zipEntrys.hasMoreElements()) {
                ZipEntry next = zipEntrys.nextElement();
                InputStream is = zipFile.getInputStream(next);
                //把输入流的文件传到输出流中 如果是word/document.xml由我们输入
                zipout.putNextEntry(new ZipEntry(next.toString()));
                if("word/document.xml".equals(next.toString())){
                    InputStream in = new FileInputStream(documentFile);
                    while((len = in.read(buffer))!=-1){
                        zipout.write(buffer,0,len);
                    }
                    in.close();
                }else {
                    while((len = is.read(buffer))!=-1){
                        zipout.write(buffer,0,len);
                    }
                    is.close();
                }
            }
            zipout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
