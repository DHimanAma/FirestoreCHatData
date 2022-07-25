package com.example.firestorechatjava;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firestorechatjava.utilities.Constants;
import com.example.firestorechatjava.utilities.PreferenceManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {
   private DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager preferenceManager =new PreferenceManager(getApplicationContext());
        FirebaseFirestore database =FirebaseFirestore.getInstance();
       documentReference =database.collection("priyanka1").document(PreferenceManager.getLoginCredentials(BaseActivity.this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constants.KEY_AVALBILTY,0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constants.KEY_AVALBILTY,1);
    }
}