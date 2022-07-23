package com.example.firestorechatjava.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.firestorechatjava.Adapter.RecentConversionAdapter;
import com.example.firestorechatjava.Models.ChatMessage;
import com.example.firestorechatjava.R;
import com.example.firestorechatjava.databinding.ActivityMainBinding;
import com.example.firestorechatjava.databinding.ActivitySignInBinding;
import com.example.firestorechatjava.utilities.Constants;
import com.example.firestorechatjava.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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
     LoadUserDetails();
        getToken();
        setListner();
        init();
        String aman =preferenceManager.getString(Constants.KEY_USER_ID);
        Log.e("jdskdjksd","<<<<sndjsdnsjx>>>>"+aman);
}
private void init(){
    conversionAdapter =new RecentConversionAdapter(conversions);
    activityMainBinding.conversionrecyclerview.setAdapter(conversionAdapter);
    database=FirebaseFirestore.getInstance();
}


private  void setListner(){
        activityMainBinding.imagesignout.setOnClickListener(view -> signOut());

        activityMainBinding.fabNEwChat.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),UserActivity.class)));

    }

    private void LoadUserDetails() {
        activityMainBinding.textname.setText(preferenceManager.getString(Constants.KEY_NAME));
       byte[] bytes= Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        activityMainBinding.imageProfile.setImageBitmap(bitmap);

    }
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void updateToken(String token){
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("priyanka1").document(
               PreferenceManager.getLoginCredentials(MainActivity.this)
        );
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
//                addOnSuccessListener(
//                unused -> showToast("updated Token Succesfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));

    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void signOut(){
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
}