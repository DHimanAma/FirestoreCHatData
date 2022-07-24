package com.example.firestorechatjava.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firestorechatjava.Adapter.ChatAdapter;
import com.example.firestorechatjava.Models.ChatMessage;
import com.example.firestorechatjava.Models.User;
import com.example.firestorechatjava.databinding.ActivityChatBinding;
import com.example.firestorechatjava.utilities.Constants;
import com.example.firestorechatjava.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding activityChatBinding;
    private User recieverUser;
    private String conversionId = null;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private ChatAdapter chatAdapter;
    PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    String aman = "lIyOSK4CqoEuPEANkRSu";
    String aman1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());
        loadRecievedDetalis();
        setLisatner();
        database = FirebaseFirestore.getInstance();
        //    init();
        listnerMessage();
        preferenceManager = new PreferenceManager(getApplicationContext());
        aman1 = preferenceManager.getString(Constants.KEY_USER_ID);
        Log.e("asdamsm", "sdjskd>>>>" + aman1);

//    private void init(){
//       // database=FirebaseFirestore.getInstance();
//


    chatAdapter=new ChatAdapter(chatMessages, getUSerimage(recieverUser.image),PreferenceManager.getLoginCredentials(ChatActivity.this));

        activityChatBinding.chatrecyclerview.setAdapter(chatAdapter);
}
//
//    }

    private void sendMessage(){
        database=FirebaseFirestore.getInstance();
        HashMap<String,Object>message = new HashMap<>();
       message.put(Constants.KEY_SENDER_ID,PreferenceManager.getLoginCredentials(ChatActivity.this));
        message.put(Constants.KEY_RECEIVERID,recieverUser.id);
        message.put(Constants.KEY_MESSAGE,activityChatBinding.inputmessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP,new Date());

        database.collection("priyanka").add(message);



        if(conversionId !=null){
            updateConversion(activityChatBinding.inputmessage.getText().toString());
        }else{
            HashMap<String , Object> conversion =new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID ,PreferenceManager.getLoginCredentials(ChatActivity.this));
            conversion.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_NAME));
            conversion.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_IMAGE));
            conversion.put(Constants.KEY_RECEIVERID,recieverUser.id);
            conversion.put(Constants.KEY_RECIEVER_NAME,recieverUser.name);
            conversion.put(Constants.KEY_RECIEVER_IMAGE,recieverUser.image);
            conversion.put(Constants.KEY_LAST_MESSAGE,activityChatBinding.inputmessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP,new Date());
            addConversion(conversion);

        }
        activityChatBinding.inputmessage.setText(null);

    }

    private void listnerMessage(){
        database.collection("priyanka")
                .whereEqualTo(Constants.KEY_SENDER_ID,PreferenceManager.getLoginCredentials(ChatActivity.this))
                .whereEqualTo(Constants.KEY_RECEIVERID,recieverUser.id)
                .addSnapshotListener(eventListener);
        database.collection("priyanka")
                .whereEqualTo(Constants.KEY_SENDER_ID,recieverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVERID,PreferenceManager.getLoginCredentials(ChatActivity.this))
                .addSnapshotListener(eventListener);
        Log.e("dksdjskd","<<<<<sdsdsdsd>>>>>"+recieverUser.id);
    }

private final EventListener<QuerySnapshot> eventListener =((value, error) -> {
    if(error!=null){
        return;
    }if(value!=null){
        int count =chatMessages.size();
        for(DocumentChange documentChange : value.getDocumentChanges()){
            if(documentChange.getType() == DocumentChange.Type.ADDED){


            ChatMessage chatMessage1 =new ChatMessage();
             chatMessage1.senderId=documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
             chatMessage1.recieverId=documentChange.getDocument().getString(Constants.KEY_RECEIVERID);
             chatMessage1.message=documentChange.getDocument().getString(Constants.KEY_MESSAGE);
             chatMessage1.datetime=getReadableTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
             chatMessage1.dateobject=documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
             chatMessages.add(chatMessage1);
            }
        }
       Collections.sort(chatMessages, (obj1,obj2)-> obj1.dateobject.compareTo(obj2.dateobject));
        if(count ==0){
            chatAdapter.notifyDataSetChanged();
        }else{
            chatAdapter.notifyItemRangeInserted(chatMessages.size(),chatMessages.size());
            activityChatBinding.chatrecyclerview.smoothScrollToPosition(chatMessages.size() -1);
        }
        activityChatBinding.chatrecyclerview.setVisibility(View.VISIBLE);
    }
    if(conversionId ==null){
        checkForConversion();
    }
}) ;

    private void addConversion(HashMap<String, Object> conversion){
        database.collection(Constants.KEY_COLLECTION_CONVERSIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message){
        DocumentReference documentReference =database.collection(Constants.KEY_COLLECTION_CONVERSIONS).document(conversionId);
        documentReference.update(Constants.KEY_LAST_MESSAGE,message, Constants.KEY_TIMESTAMP, new Date());
    }

    private Bitmap getUSerimage(String encodedImage){
        byte[]bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);

    }
    private void loadRecievedDetalis(){
        recieverUser =(User) getIntent().getSerializableExtra(Constants.KEY_USER);
        Log.e("dddfff","sddfefg"+recieverUser.name+" "+recieverUser.id);
        activityChatBinding.textName.setText(recieverUser.name);
    }
    private void setLisatner(){
        activityChatBinding.imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        activityChatBinding.layoutsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activityChatBinding.inputmessage.getText().toString().isEmpty()){
                    Toast.makeText(ChatActivity.this, "Please enter the Required Field", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage();
                }
            }
        });
    }
    private String getReadableTime(Date date){
        return new SimpleDateFormat("MMMM,yyyy - hh:mm a",Locale.getDefault()).format(date);
    }

    private void checkForConversion(){
        if(chatMessages.size() !=0){
            checkForconversionRemotely(PreferenceManager.getLoginCredentials(ChatActivity.this), recieverUser.id);
            checkForconversionRemotely( recieverUser.id,PreferenceManager.getLoginCredentials(ChatActivity.this));

        }
    }

    private void checkForconversionRemotely(String senderId, String reciverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,senderId)
                .whereEqualTo(Constants.KEY_RECEIVERID,reciverId)
                .get()
                .addOnCompleteListener(conversionCompleteListner);
    }

    private final OnCompleteListener<QuerySnapshot> conversionCompleteListner = task -> {
        if(task.isSuccessful() && task.getResult() !=null && task.getResult().getDocuments().size() >0){
            DocumentSnapshot documentSnapshot =task.getResult().getDocuments().get(0);
            conversionId=documentSnapshot.getId();
        }
    };
}