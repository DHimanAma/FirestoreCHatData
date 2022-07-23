package com.example.firestorechatjava.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.firestorechatjava.R;
import com.example.firestorechatjava.databinding.ActivitySignInBinding;
import com.example.firestorechatjava.utilities.Constants;
import com.example.firestorechatjava.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
ActivitySignInBinding activitySignInBinding;
    private ProgressDialog progressDialog;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignInBinding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(activitySignInBinding.getRoot());
        setlistner();
        preferenceManager=new PreferenceManager(getApplicationContext());
         if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
             Intent intent=new Intent(getApplicationContext(),MainActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(intent);
         }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
    }
    private void setlistner(){
        activitySignInBinding.inputcreateaccount.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class)));


       activitySignInBinding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(isvalidSignInDetails()){
                   SignInUser();
               }
           }
       });

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void SignInUser() {
        showDialog();
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("priyanka1")
                .whereEqualTo(Constants.KEY_EMAIl,activitySignInBinding.inputEmail.getText().toString().trim())
                .whereEqualTo(Constants.KEY_PASSWORD,activitySignInBinding.inputPassword.getText().toString().trim())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null && task.getResult().getDocuments().size()>0){
                        DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                        preferenceManager.putBooleaN(Constants.KEY_IS_SIGNED_IN,true);
                        PreferenceManager.setLoginCredentials(documentSnapshot.getId(),SignInActivity.this);
                     //   preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE,documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                       hideDialog();
                       showToast("Unable to Sign In");
                    }
                }).addOnFailureListener(e -> {
                    hideDialog();
                    showToast(e.getMessage());
                });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
//    private void addDataToFirestore(){
//        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
//        HashMap<String,Object>data=new HashMap<>();
//        data.put("first_name","chirag");
//        data.put("last_name","aman");
//        firebaseFirestore.collection("user")
//                .add(data)
//                .addOnSuccessListener(documentReference -> {
//                    Toast.makeText(this, "data inserted", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
    private Boolean isvalidSignInDetails(){
     if(activitySignInBinding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Please  Enter Email");
            return  false;
        }else if(activitySignInBinding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Please  Enter Password");
            return  false;
        }else {
         return true;
     }}}