package com.example.firestorechatjava.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.firestorechatjava.Adapter.UserAdapter;
import com.example.firestorechatjava.Models.User;
import com.example.firestorechatjava.R;
import com.example.firestorechatjava.databinding.ActivityMainBinding;
import com.example.firestorechatjava.databinding.ActivityUserBinding;
import com.example.firestorechatjava.listner.UserListner;
import com.example.firestorechatjava.utilities.Constants;
import com.example.firestorechatjava.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserListner {
ActivityUserBinding activityUserBinding;
    private ProgressDialog progressDialog;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserBinding= ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(activityUserBinding.getRoot());
        preferenceManager=new PreferenceManager(getApplicationContext());
        getUsers();
      setListner();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
    }

    private void setListner() {
        activityUserBinding.imageback.setOnClickListener(view -> onBackPressed());
    }

    private  void showeeormessage(){
        activityUserBinding.textErrormessage.setText(String.format("%s","No user available"));
        activityUserBinding.textErrormessage.setVisibility(View.VISIBLE);
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
    private void getUsers(){
        //showDialog();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("priyanka1")
                .get()
                .addOnCompleteListener(task -> {
                    hideDialog();
                    String currentId =PreferenceManager.getLoginCredentials(UserActivity.this);
                    Log.e("jdskdjksd","<<<<sndjsdnsjx>>>>"+currentId);
                    if(task.isSuccessful() && task.getResult()!=null){
                        List<User>users =new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user =new User();
                            user.name=   queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIl);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id =    queryDocumentSnapshot.getId();
                            users.add(user);
                            Log.e("jdskdjksd","<<<<sndjsdnsjx>>>>"+user.id);
                        }if(users.size() > 0 ){
                            UserAdapter userAdapter =new UserAdapter(users,this);
                            activityUserBinding.userrecyclerview.setAdapter(userAdapter);
                            activityUserBinding.userrecyclerview.setVisibility(View.VISIBLE);
                        }else{
                            showeeormessage();

                        }
                    }else {
                        showeeormessage();
                    }
                });
    }

    @Override
    public void onUserCliked(User user) {
        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
        finish();
    }
}