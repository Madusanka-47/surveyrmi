package com.survey.mongoclient;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.Date;
import javax.mail.Message;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;
import java.net.UnknownHostException;
import java.security.spec.KeySpec;

public class SurveyMailService {

    /**
     * Outgoing Mail (SMTP) Server Use Authentication: Yes Port for TLS/STARTTLS:
     * 587
     * 
     * @author Madusanka47
     **/

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    public SurveyMailService() throws Exception {
        myEncryptionKey = "ThisIsSpartaThisIsSparta";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
    }

    /**
     * Function used for create the email body
     * 
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    public static void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress("admin@survey.com", "@noreply"));
            msg.setReplyTo(InternetAddress.parse("admin@survey.com", false));
            msg.setSubject(subject, "UTF-8");
            msg.setContent(body, "text/html");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("Email Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function use to dycript the password from meta pane
     * 
     * @param encryptedString
     * @return
     */
    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    /**
     * This is a diffrent encryption from the survy encryption Used for encrypt
     * passwords which are coming form outside sources.
     * 
     * @param unencryptedString
     * @return encryptedString
     */
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    /**
     * Get encrypted password from survey meta table
     * 
     * @return
     * @throws UnknownHostException
     */
    public String getAccessKey() throws UnknownHostException {
        try {
            final MongoDatabase database = MongoConnector.getInstance();
            final MongoCollection<org.bson.Document> collection = database.getCollection("survey_meta");
            final FindIterable<org.bson.Document> source = collection.find(eq("service", "mailservice"));
            for (final org.bson.Document src : source.collation(null)) {
                return decrypt(src.get("accesskey").toString());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public void sentCreationEmail(String pwd, String usermail, String name, boolean isNew) throws UnknownHostException {
        final String greet = name.substring(0, 1).toUpperCase() + name.substring(1);
        final String fromEmail = "cmb7srilanka@gmail.com";
        final String password = getAccessKey();
        final String toEmail = usermail;
        String body = null;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        if (isNew) {
            body = "<p>Hi " + greet
                    + ",<br></br><br>Your admin has created a survey account for you. Please use your current email and password as <b>"
                    + pwd + " &#128519;</b><br></br><br> Cheers, <br>Survey Admin</br>.</br></p>";
        } else {
            body = "<p>Hi " + greet + ",<br></br><br>We have changed your password. Please use password as <b>" + pwd
                    + " &#128526;</b><br></br><br> Cheers, <br>Survey Admin</br>.</br></p>";
        }
        if (!body.equals("") && body != null) {
            sendEmail(session, toEmail, "Survey Login", body);
        }

    }

}
