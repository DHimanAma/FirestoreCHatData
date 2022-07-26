package com.example.firestorechatjava.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.firestorechatjava.Models.User;
import com.example.firestorechatjava.R;
import com.example.firestorechatjava.activities.ChatActivity;
import com.example.firestorechatjava.utilities.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Random;

public class MessagingServices extends FirebaseMessagingService {
    private Context context;

    // this method provide for fresh token for different User:
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("FCM","Token"+token);
    }

    //All your firebase meesage are rercieve in this on recieve message Method:
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    Log.e("FCM", "<skdjdsd>>>>>>>>>>>>>" + message);


// Message recieve in Remoite message then we get remote message response through Json object
       if(message.getData().size() > 0){
           String type="",Subject="",Message="";
           if(message!=null && message.getData()!=null)
           {
               JSONObject object = new JSONObject(message.getData());
               String result =  object.toString();

               try
               {
                   JSONObject obj = new JSONObject(result);
                   Log.e("FCM", "<skdjdsd>>>>>>>>>>>>>" + obj);
//                   type=obj.getString("type");
//                   Subject=obj.getString("Subject");
//                   Message=obj.getString("Message");


//                   if (!isAppIsInBackground(getApplicationContext())) {
//
//                       sendNotification(type,Message,Subject);
//
//
//                   } else {
//
//                       sendNotificationKilled(type,Message,Subject);
//
//                   }

               } catch (Throwable t) {

//                    Log.e("My App", "Could not parse malformed JSON: \"" + t.toString() + "\"");
               }


           }




       }

        User user = new User();
        user.id = message.getData().get(Constants.KEY_USER_ID);
        user.name = message.getData().get(Constants.KEY_NAME);
        user.token = message.getData().get(Constants.KEY_FCM_TOKEN);

        int notificationId = new Random().nextInt();
        String channelId = "chat_message";


     Intent intent =new Intent(this, ChatActivity.class);
     intent.putExtra(Constants.KEY_USER,user);
     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,channelId);
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
        builder.setContentTitle(user.name);
        builder.setContentText(message.getData().get(Constants.KEY_MESSAGE));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                message.getData().get(Constants.KEY_MESSAGE)
        ));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence channelname ="Chat Message";
            String channelDescription ="this notification channel is used for chat message notification";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel =new NotificationChannel(channelId,channelname,importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationId,builder.build());
    }

}
