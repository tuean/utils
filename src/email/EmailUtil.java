package email;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件通知工具
 */
@Component
public class EmailUtil {
    private static Session session;
    private static Transport transport;

    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    private static final String account = "";

    private static final String pwd = "";

    /**
     * 发送会议邀请
     * @param title
     * @param content
     * @param encoding
     * @param html 是否作为HTML发送
     * @param icalText 会议邀请信息
     * @param to
     * @param annexList 附件信息 fileName:文件名 filePath:文件绝对路径
     */

    public void sendIcsMail(String title, String content, String encoding, boolean html, String icalText, String to, List<Map<String, String>> annexList)
            throws MessagingException, IOException {
        String mimeType = html ? "text/html; charset=" + encoding : "text/calendar; charset=" + encoding;
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(account));
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(title, encoding);

        MimeMultipart multipart = new MimeMultipart();
        BodyPart bodypart = new MimeBodyPart();
        bodypart.setContent(content, mimeType);
        multipart.addBodyPart(bodypart);
        if (icalText != null && icalText.trim().length() > 0) {
            bodypart = new MimeBodyPart();
            bodypart.setContent(icalText, "text/calendar; method=REQUEST; charset=" + encoding);
            bodypart.setDataHandler(new DataHandler(new ByteArrayDataSource(icalText,
                    "text/calendar;method=REQUEST;charset=" + encoding)));
            multipart.addBodyPart(bodypart);
        }

        //附件
        if(annexList != null && !annexList.isEmpty()){
            for(int i = 0; annexList.size() > i; i++){
                String filePath = annexList.get(i).get("filePath");
                String fileName = annexList.get(i).get("fileName");
                //解决中文乱码问题
                fileName = MimeUtility.encodeWord(fileName);
                DataSource source = new FileDataSource(filePath);
                BodyPart filebodypart = new MimeBodyPart();
                filebodypart.setDataHandler(new DataHandler(source));
                filebodypart.setFileName(fileName);
                multipart.addBodyPart(filebodypart);
            }
        }

        message.setContent(multipart);
        connect();
        transport.sendMessage(message, message.getAllRecipients());
    }


    /***
     * 发送邮件 可以带附件
     * @Title infos中存放路径 filePath   文件名fileName
     * @param
     * @param
     */
    public void sendWithAnnex(String title, String content, String encoding, List<Map<String, Object>> infos, String to)
            throws MessagingException, UnsupportedEncodingException {

        String mimeType = "text/html; charset=" + encoding;
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(account));
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(title, encoding);

        MimeMultipart multipart = new MimeMultipart();
        BodyPart bodypart = new MimeBodyPart();
        bodypart.setContent(content, mimeType);
        multipart.addBodyPart(bodypart);

        BodyPart filebodypart;
        DataSource source;

        for (int i = 0; i < infos.size(); i++) {
            String filePath = (String) infos.get(i).get("filePath");
            String fileName = (String) infos.get(i).get("fileName");
            //解决中文乱码问题
            fileName = MimeUtility.encodeWord(fileName);

            source = new FileDataSource(filePath);
            filebodypart = new MimeBodyPart();
            filebodypart.setDataHandler(new DataHandler(source));
            filebodypart.setFileName(fileName);
            multipart.addBodyPart(filebodypart);
        }

        message.setContent(multipart);
        connect();
        transport.sendMessage(message, message.getAllRecipients());

    }

    /***
     * 发送包含图片的邮件
     * @Title 邮件正文包含图片
     * @throws MessagingException
     */
    public void sendWithPhoto(String title, String content, String encoding, List<Map<String, Object>> infos, String to)
            throws Exception {
        String mimeType = "text/html; charset=" + encoding;
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(account));
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(title, encoding);

        MimeMultipart allPart = new MimeMultipart("mixed");

        for (int i = 0; i < infos.size(); i++){
            if(infos.get(i).get("filePath") != null){
                MimeBodyPart param = null;
                param = createContent(content, infos.get(i).get("filePath").toString());
                if(param != null){
                    allPart.addBodyPart(param);
                }
            }else{
                logger.info("发送邮件失败了。。。缺少图片路径" + content);
            }
        }

        message.setContent(allPart);
        connect();
        transport.sendMessage(message, message.getAllRecipients());
    }


    /**
     * 可同时发送附件和图片
     * @param title
     * @param content
     * @param encoding
     * @param receivers
     * @param annexs
     * @param photos
     */
    public Boolean sendWithAnnexAndPhoto(String title, String content, String encoding, String[] receivers,
                                             List<Map<String, Object>> annexs, List<Map<String, Object>> photos) {
        try{
            String mimeType = "text/html; charset=" + encoding;
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(account));

            //多个收件人
            for(String receiver : receivers){

            }
            message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(joinStringsByComma(receivers)));


//            message.addRecipients(MimeMessage.RecipientType.CC, );
            message.setSubject(title, encoding);

            MimeMultipart multipart = new MimeMultipart("mixed");
            BodyPart bodypart = new MimeBodyPart();
            bodypart.setContent(content, mimeType);
            multipart.addBodyPart(bodypart);


            //附件
            for (int i = 0; i < annexs.size(); i++) {
                String filePath = (String) annexs.get(i).get("filePath");
                String fileName = (String) annexs.get(i).get("fileName");
                String fileContent = (String) annexs.get(i).get("fileContent");
                if(fileName != null){
                    //解决中文乱码问题
                    fileName = MimeUtility.encodeWord(fileName);

                    DataSource source = new FileDataSource(filePath);
                    BodyPart filebodypart = new MimeBodyPart();
                    filebodypart.setDataHandler(new DataHandler(source));
                    filebodypart.setFileName(fileName);
                    multipart.addBodyPart(filebodypart);
                }else if(fileContent != null){
                    byte[] bytes = fileContent.getBytes();
                    BodyPart filebodypart = new MimeBodyPart();
                    filebodypart.setDataHandler(new DataHandler(bytes, "application/octet-stream"));
//                    filebodypart.setFileName(fileName);
                    multipart.addBodyPart(filebodypart);
                }

            }

            //图片
            for (int i = 0; i < photos.size(); i++){
                if(photos.get(i).get("filePath") != null){
                    MimeBodyPart param = null;
                    param = createContent(content, photos.get(i).get("filePath").toString());
                    if(param != null){
                        multipart.addBodyPart(param);
                    }
                }else{
                    logger.info("发送候选人邮件失败了。。。缺少图片路径" + content);
                    return false;
                }
            }

            message.setContent(multipart);
            connect();
            transport.sendMessage(message, message.getAllRecipients());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String joinStringsByComma(String[] resource){
        StringBuffer sb = new StringBuffer();
        Boolean commaFlag = true;
        for(String res : resource){
            if(!StringUtils.isEmpty(res)){
                if(!commaFlag){
                    sb.append(",");
                }
                sb.append(res);
                commaFlag = false;
            }
        }

        return sb == null ? null : sb.toString();
    }

    /**
     * 根据传入的文件路径创建附件并返回
     */
    public MimeBodyPart createAttachment(String fileName) throws Exception {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(fileName);
        attachmentPart.setDataHandler(new DataHandler(fds));
        attachmentPart.setFileName(fds.getName());
        return attachmentPart;
    }

    /**
     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分
     */
    public MimeBodyPart createContent(String body, String filePath)
            throws Exception {
        // 用于保存最终正文部分
        MimeBodyPart contentBody = new MimeBodyPart();
        // 用于组合文本和图片，"related"型的MimeMultipart对象
        MimeMultipart contentMulti = new MimeMultipart("related");

        // 正文的文本部分
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(body, "text/html;charset=utf-8");
        contentMulti.addBodyPart(textBody);

        // 正文的图片部分
        MimeBodyPart jpgBody = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filePath);
        jpgBody.setDataHandler(new DataHandler(fds));
        jpgBody.setContentID("photo");
        contentMulti.addBodyPart(jpgBody);

        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文
        contentBody.setContent(contentMulti);
        return contentBody;
    }

    public void connect() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.host","smtp.xx.xx");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.auth.plain.disable", "true");
        // 使用SSL连接，见com.sun.mail.smtp.SMTPTransport
        // 某些邮件服务器，如Google，需要SSL安全连接
//        props.setProperty("mail.smtp.ssl.enable", "true");

        session = Session.getInstance(props);
        transport = session.getTransport("smtp");
        transport.connect(account, pwd);
    }

    public static void close() {
        try {
            if (transport != null) {
                transport.close();
                transport = null;
            }
        } catch (Exception e) {

        }
        session = null;
    }


}