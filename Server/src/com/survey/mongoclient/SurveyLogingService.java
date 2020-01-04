package com.survey.mongoclient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.Binary;

public class SurveyLogingService {
    private static final String hashMap = "access_keys";

    public String encryptLogings(String usp, String usrid, byte[] s4lt, boolean isStore)
            throws NoSuchProviderException {
        String hashcode = null;
        try {
            byte[] salt;
            if (isStore) {
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
                salt = new byte[16];
                sr.nextBytes(salt);
            } else {
                salt = s4lt;
            }
            if (!usp.isEmpty() || usp != null) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(salt);
                byte[] bytes = md.digest(usp.getBytes());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                }
                hashcode = sb.toString();
                if (isStore) {
                    hASHmAP(usrid, salt, true);
                }
            }
            return hashcode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return hashcode;
        }
    }

    public String decryptLoggins(String usp, String usrid) {
        try {
            Document doc = hASHmAP(usrid, null, false);
            final Binary binary = doc.get("hashkey", Binary.class);
            final byte[] salt = binary.getData();
            return encryptLogings(usp, usrid, salt, false);
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public Document hASHmAP(String userid, byte[] salt, boolean isStore) {
        Document hashkey = null;
        try {
            //MongoConnector dbo = new MongoConnector();
            final MongoDatabase database = MongoConnector.getInstance();
            MongoCollection<org.bson.Document> collection = database.getCollection(hashMap);
            if (!userid.isEmpty()) {
                if (isStore) {
                    collection.insertOne(new Document("userid", userid).append("hashkey", salt));
                } else {
                    hashkey = collection.find(eq("userid", userid)).first();
                }
            }
        } catch (Exception ex) {

        }
        return hashkey;
    }
}

/**
 * Debug main for SurveyQuestionService Remove once the implementaion completed
 */
class StartLoginService {
    public static void main(String[] args)
            throws UnsupportedEncodingException, NoSuchProviderException, NoSuchAlgorithmException {
        SurveyLogingService slt = new SurveyLogingService();
        System.out.println(slt.decryptLoggins("Name128$", "8"));

    }
}