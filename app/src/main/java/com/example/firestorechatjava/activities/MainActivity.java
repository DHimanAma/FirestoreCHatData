package com.example.firestorechatjava.activities;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firestorechatjava.Adapter.RecentConversionAdapter;
import com.example.firestorechatjava.BaseActivity;
import com.example.firestorechatjava.Models.ChatMessage;
import com.example.firestorechatjava.Models.User;

import com.example.firestorechatjava.databinding.ActivityMainBinding;

import com.example.firestorechatjava.listner.ConversionListner;
import com.example.firestorechatjava.utilities.Constants;
import com.example.firestorechatjava.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements ConversionListner {
ActivityMainBinding activityMainBinding;
PreferenceManager preferenceManager;
private List<ChatMessage> conversions =new ArrayList<>();
private RecentConversionAdapter conversionAdapter;
private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        preferenceManager=new PreferenceManager(getApplicationContext());
        database=FirebaseFirestore.getInstance();
     LoadUserDetails();
        getToken();
        setListner();
        listnerMessage();
        init();
        String aman =preferenceManager.getString(Constants.KEY_USER_ID);
        Log.e("jdskdjksd","<<<<sndjsdnsjx>>>>"+aman);
}
   // this mehotd used for set list in adapter then set adapter on rercycler view:
private void init(){
    conversionAdapter =new RecentConversionAdapter(conversions,this);
    activityMainBinding.conversionrecyclerview.setAdapter(conversionAdapter);
    database=FirebaseFirestore.getInstance();
}

           // this mehtod used for set the user id are equal to reciever id  && reciever id are equal for user id
    private void listnerMessage(){
        database.collection(Constants.KEY_COLLECTION_CONVERSIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,PreferenceManager.getLoginCredentials(MainActivity.this))

                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSIONS)

                .whereEqualTo(Constants.KEY_RECEIVERID,PreferenceManager.getLoginCredentials(MainActivity.this))
                .addSnapshotListener(eventListener);
       // Log.e("dksdjskd","<<<<<sdsdsdsd>>>>>"+recieverUser.id);
    }



    // this Method use for set the conversion data of user in list with the help of queary  pleae check carefully :
    private final EventListener<QuerySnapshot> eventListener =((value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {

            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {

                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String recieverId = documentChange.getDocument().getString(Constants.KEY_RECEIVERID);
                    ChatMessage chatMessage1 = new ChatMessage();
                    chatMessage1.senderId = senderId;
                    chatMessage1.recieverId = recieverId;

                    if(PreferenceManager.getLoginCredentials(MainActivity.this).equals(senderId)) {
                        chatMessage1.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECIEVER_IMAGE);
                        chatMessage1.conversionName = documentChange.getDocument().getString(Constants.KEY_RECIEVER_NAME);
                        chatMessage1.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVERID);
                    }else{
                        chatMessage1.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage1.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage1.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage1.message=documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage1.dateobject=documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversions.add(chatMessage1);

                }else if(documentChange.getType() ==DocumentChange.Type.MODIFIED){
                    for (int i =0; i<conversions.size(); i++){
                        String senderId =documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String recievedID= documentChange.getDocument().getString((Constants.KEY_RECEIVERID));
                        if(conversions.get(i).equals(senderId) && conversions.get(i).equals(recievedID)){
                            conversions.get(i).message =documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversions.get(i).dateobject =documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversions, (obj1,obj2)-> obj1.dateobject.compareTo(obj2.dateobject));

                conversionAdapter.notifyDataSetChanged();


                activityMainBinding.conversionrecyclerview.smoothScrollToPosition(0);

            activityMainBinding.conversionrecyclerview.setVisibility(View.VISIBLE);
        }

    });


private  void setListner(){
        activityMainBinding.imagesignout.setOnClickListener(view -> signOut());

        activityMainBinding.fabNEwChat.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),UserActivity.class)));

    }

    // this mehtod use for sedt save user name and image here :
    private void LoadUserDetails() {
        activityMainBinding.textname.setText(preferenceManager.getString(Constants.KEY_NAME));
       byte[] bytes= Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        activityMainBinding.imageProfile.setImageBitmap(bitmap);

    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    //call update token method here with colletion of user and then save token of different usert on here with shared preference
    private void updateToken(String token){
    preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("priyanka1").document(
               PreferenceManager.getLoginCredentials(MainActivity.this));
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
//                addOnSuccessListener(
//                unused -> showToast("updated Token Succesfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));

    }

    // this method use for get the api token then update according to different user registeration
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void signOut(){

    // this method use for clear data for preference and delete toekn of user in firebase
        showToast("Signing out...");
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("priyanka1").document(
                PreferenceManager.getLoginCredentials(MainActivity.this)
        );
        HashMap<String,Object>updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(
                unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                    finish();
                }).addOnFailureListener(e -> showToast("Unable to Sign Out"));

    }


    //  this clicked use for when user last message  conversion availabe on Main avitivty
    @Override
    public void ConversionOnClicked(User user) {
        Intent intent =new Intent(getApplicationContext(),ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
        finish();
    }
}