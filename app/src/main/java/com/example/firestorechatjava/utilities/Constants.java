package com.example.firestorechatjava.utilities;

import java.util.HashMap;

public class Constants {

    /// this is all user key for use in every page Moast important //

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIl = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatAppPREference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderID";
    public static final String KEY_RECEIVERID = "reciverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECIEVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECIEVER_IMAGE = "recieverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVALBILTY = "keyavaliblity";

    //for push notification
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTAION_IDS = "registration_ids";
    public static final String DEVICEUNIQUEID = "deviceId";


// this mehtod are use for hit the push notification for specific user then add the server key and content type
    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMessageHeader() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION, "key=AAAABUxEHDI:APA91bHsHfY60xAQoQURgWXw0hm-aCvkBUtNotcgkdFt1w1pxJnKkCm6AyLpdvjntwEUqdXL5OMihcI34634lFOoAwppTVSULO2VZtSuFVuiIZTAQwmnD4K-CfOrbPH__pFE2kOcqzqE");
            remoteMsgHeaders.put(REMOTE_MSG_CONTENT_TYPE, "application/json");
        }
        return  remoteMsgHeaders;
    }






   public static String INSTANCE_NAMEPHONEToKEN="AmanTel_Device234";
    public static String INSTANCE_NAME_Fav="AmanTel_Device_fav";
}
