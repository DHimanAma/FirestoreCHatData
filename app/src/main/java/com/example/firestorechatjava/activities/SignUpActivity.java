package com.example.firestorechatjava.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.example.firestorechatjava.BuildConfig;
import com.example.firestorechatjava.R;
import com.example.firestorechatjava.databinding.ActivitySignUpBinding;
import com.example.firestorechatjava.listner.IImageCompressTaskListener;
import com.example.firestorechatjava.utilities.Constants;
import com.example.firestorechatjava.utilities.ImageCompressTask;
import com.example.firestorechatjava.utilities.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding activitySignUpBinding;
    public static final int CAMRE = 1000;
    public static final int Gallerye = 2006;
    public static final int CROP_PIC = 2007;
    Bitmap photo;
    Bitmap bitmap;
    String imagePath = " ";
    Uri uri;
    File file;
    Dialog dialog;
    String path = " ";
    private File fileimage;
    private String encodeImage;
    private ProgressDialog progressDialog;
    PreferenceManager preferenceManager;
    private ExecutorService mExecutorService;
    private String permissionDenyMsg = "";
    private RuntimePermission runtimePermissionInterFace;
    private int permissionCode;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());
        setlistner();
         mExecutorService = Executors.newFixedThreadPool(1);


        preferenceManager = new PreferenceManager(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("sdksdjskdj", "<<<<nkdskad>>>" + android_id);
        preferenceManager.putString(Constants.DEVICEUNIQUEID, android_id);


        if (encodeImage == null) {
            activitySignUpBinding.imageprofile.setImageResource(R.drawable.aman);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setlistner() {
        activitySignUpBinding.inputSignIn.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)));

        activitySignUpBinding.buttonSignUp.setOnClickListener(view ->
        {
            if (isvalidSignupDetails()) {
                signUp();
            }
        });
        activitySignUpBinding.layoutImage.setOnClickListener(view -> {
            OpenGalleryCamera(SignUpActivity.this);
        });
    }

    public void OpenGalleryCamera(final Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.open_gallery_camera);

        final Button Gallery = dialog.findViewById(R.id.Gallery);
        final Button Camera = dialog.findViewById(R.id.Camera);


        Camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int hasPermission = ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        // Display UI and wait for user interaction
                        showToast("please allow the camera permission");
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMRE);
                    }
                    return;
                } else {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMRE);
                      dialog.dismiss();


                }
            }
        });
        Gallery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int hasPermission = ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Display UI and wait for user interaction
                        showToast("please allow the Gallery permission");
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Gallerye);
                    }
                    return;
                } else {

            Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
           startActivityForResult(intent1,Gallerye);
           dialog.dismiss();
                    //  pickimage.launch(intent1);
//dialog.dismiss();
                    // GalleryPickup();

                }
            }

        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d("APP_DEBUG", String.valueOf(requestCode));

        try {
            // When an Image is picked
            if (requestCode == Gallerye && resultCode == Activity.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start( this);

            }else if  (requestCode == CAMRE && resultCode == Activity.RESULT_OK
                    && null != data) {
                Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                encodeImage=encodedImage(bitmap);
                activitySignUpBinding.removeaddimage.setVisibility(View.GONE);
                activitySignUpBinding.imageprofile.setImageBitmap(bitmap);


            }
            // when image is cropped
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Log.d("APP_DEBUG",result.toString());
                if (resultCode == Activity.RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Log.d("APP_DEBUG",resultUri.toString());
                    Bitmap bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    //pofilePic.setImageBitmap(bitmap);
                    encodeImage=encodedImage(bitmap);
                    activitySignUpBinding.removeaddimage.setVisibility(View.GONE);
                    activitySignUpBinding.imageprofile.setImageBitmap(bitmap);
                    Toast.makeText(this, "sssss", Toast.LENGTH_SHORT).show();





                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong"+e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }








//    private Uri getImageUri(Context context, Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Toast.makeText(context, "Image uploaded Successfully", Toast.LENGTH_LONG).show();
//        path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
//        return Uri.parse(path);
//    }
//
//    public Uri getCroperImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//    private final ActivityResultLauncher<Intent>pickimage=registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if(result.getResultCode()==RESULT_OK){
//                    Uri imageUri=result.getData().getData();
//                    try{
//                        InputStream inputStream=getContentResolver().openInputStream(imageUri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        activitySignUpBinding.imageprofile.setImageBitmap(bitmap);
//                        activitySignUpBinding.removeaddimage.setVisibility(View.GONE);
//                        encodeImage=encodedImage(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//    );



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