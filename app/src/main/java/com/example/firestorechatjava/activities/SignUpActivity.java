package com.example.firestorechatjava.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.firestorechatjava.R;
import com.example.firestorechatjava.databinding.ActivitySignInBinding;
import com.example.firestorechatjava.databinding.ActivitySignUpBinding;
import com.example.firestorechatjava.utilities.Constants;
import com.example.firestorechatjava.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
ActivitySignUpBinding activitySignUpBinding;
private String encodeImage;
    private ProgressDialog progressDialog;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());
        setlistner();

         preferenceManager=new PreferenceManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("sdksdjskdj","<<<<nkdskad>>>"+android_id);
        preferenceManager.putString(Constants.DEVICEUNIQUEID,android_id);
    }

    private void setlistner() {
        activitySignUpBinding.inputSignIn.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),SignInActivity.class)));

        activitySignUpBinding.buttonSignUp.setOnClickListener(view ->
        {
            if(isvalidSignupDetails()){
                signUp();
            }
        });
        activitySignUpBinding.layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickimage.launch(intent);
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
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // this method use for send all user Data to firebase :
    private void signUp(){
        showDialog();
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        HashMap<String,Object> database=new HashMap<>();
        database.put(Constants.KEY_NAME,activitySignUpBinding.inputName.getText().toString());
        database.put(Constants.KEY_EMAIl,activitySignUpBinding.inputEmail.getText().toString());
        database.put(Constants.KEY_PASSWORD,activitySignUpBinding.inputPassword.getText().toString());
        database.put(Constants.KEY_IMAGE,encodeImage);
        firebaseFirestore.collection("priyanka1")
                .add(database)
                .addOnSuccessListener(documentReference -> {
                    hideDialog();
                    preferenceManager.putBooleaN(Constants.KEY_IS_SIGNED_IN,true);
               //     preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    PreferenceManager.setLoginCredentials(documentReference.getId(),SignUpActivity.this);
                    preferenceManager.putString(Constants.KEY_NAME,activitySignUpBinding.inputName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE,encodeImage);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }).addOnFailureListener(e -> {
                    hideDialog();
                    showToast(e.getMessage());
                });

    }
// this m ethod user for make bitmape for getting image for gallery && camera :
    private String encodedImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeight=bitmap.getHeight()*previewWidth / bitmap.getWidth();
        Bitmap previewBitmap=Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent>pickimage=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    Uri imageUri=result.getData().getData();
                    try{
                        InputStream inputStream=getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        activitySignUpBinding.imageprofile.setImageBitmap(bitmap);
                        activitySignUpBinding.removeaddimage.setVisibility(View.GONE);
                        encodeImage=encodedImage(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
    );



    private Boolean isvalidSignupDetails(){
        if(encodeImage ==null){
            showToast("Select Profile Image");
            return  false;
        }else if(activitySignUpBinding.inputName.getText().toString().trim().isEmpty()){
            showToast("Please  Enter Name");
            return  false;
        }else if(activitySignUpBinding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Please  Enter Email");
            return  false;
    }else if(!Patterns.EMAIL_ADDRESS.matcher(activitySignUpBinding.inputEmail.getText().toString()).matches()){
            showToast("Please  Enter Valid Email");
            return  false;
}else if(activitySignUpBinding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Please  Enter password");
            return  false;
        }else if(activitySignUpBinding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Please  Enter ConfirmPassword");
            return false;
        }else if(!activitySignUpBinding.inputConfirmPassword.getText().toString().equals(activitySignUpBinding.inputPassword.getText().toString())){
            showToast("password  and ConfirmPassword must be same");
            return false;
        }else{
            return true;
        }
        }
    }