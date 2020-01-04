package com.survey.rmiinterface;

import java.io.Serializable;
import java.util.ArrayList;

import com.survey.mongoclient.SurveyAccessService;
import com.survey.mongoclient.SurveyLogingService;

import org.bson.Document;

public class UserRoles implements Serializable {

    private static final long serialVersionUID = -6010824197871684473L;
    private String userId;

    public UserRoles() {
        super();
    }

    public ArrayList<Document> generateUserLogin(String userName, String login) {
        String encyPass = null;
        SurveyAccessService userInfo = new SurveyAccessService();
        SurveyLogingService surveyAccs = new SurveyLogingService();

        if (!userName.isEmpty()) {
            this.userId = userInfo.getUserIdByUserName(userName);
            encyPass = surveyAccs.decryptLoggins(login, userId);
            System.out.println(encyPass);
        }
        if (!encyPass.isEmpty()) {
            String userHash = userInfo.getUserAccessHash(this.userId);
            if (encyPass.equals(userHash)) {
                SurveyAccessService sas = new SurveyAccessService();
                return sas.getUserDetails(userName);
            }
        }
        return null;
    }

}


// class StartAccessService {
//     public static void main(final String[] args) throws Exception {
//         final UserRoles acc = new UserRoles();
//         acc.generateUserLogin("admin@survey.com", "madusanka");
//         // "wettewa", false);
//         // acc.updatePaneUser("admin@survey.com", "madusanka.wettewa@gmail.com",
//         // "dulanjan", "madusanka", false, false);
//         // System.out.println( acc.getUserDetails("madusanka.wettewa@gmail.com"));
//     }
// }

