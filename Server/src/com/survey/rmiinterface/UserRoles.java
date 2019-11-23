package com.survey.rmiinterface;

import java.io.Serializable;

import com.survey.mongoclient.SurveyAccessService;
import com.survey.mongoclient.SurveyLogingService;

public class UserRoles implements Serializable {

    private static final long serialVersionUID = -6010824197871684473L;
    private String userId;

    public UserRoles() {
        super();
    }

    public boolean generateUserLogin(String userName, String login) {
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
                return true;
            }
        }
        return false;
    }

}