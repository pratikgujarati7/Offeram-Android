package com.offeram.couponbouquet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.responses.Common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Referral extends AppCompatActivity {
    private ImageView img_profile;
    private EditText usernameEt,promocodeEt;
    private TextView txt_havepromo,submitBtn;
    private TextInputLayout promocodeLayout;
    public int REQUEST_CODE = 124;
    int REQUEST_CAMERA = 100, REQUEST_GALLERY = 101;
    Toolbar toolbar;
    CircleImageView imageIv;
    TextInputLayout nameInput;
    EditText nameEt;
    String userDetails = "", isImage = "0", userChosenTask = "";
    File imagePath = null;
    ProgressDialog pd;
    Boolean isValid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        Init();
        txt_havepromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promocodeLayout.setVisibility(View.VISIBLE);
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Referral.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Referral.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Referral.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Referral.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                    android.Manifest.permission.CAMERA}, REQUEST_CODE);
                } else {
                    selectImage();
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValid = true;
                String message = "";
                if (isImage.equals("1")) {
                    if (imagePath == null) {
                        isValid = false;
                        message = "Must select picture to upload image";
                    }
                }
               if (usernameEt.getText().toString().equalsIgnoreCase("")) {
                    isValid = false;
                    message = "Must Enter name";
                }
                //64yv60
                if (isValid) {
                    if (Config.isConnectedToInternet(Referral.this)) {
                        pd = new ProgressDialog(Referral.this);
                        pd.setMessage("Loading...");
                        pd.setCancelable(false);
                        pd.show();
                        RequestBody userId = createPartFromString(Config.getSharedPreferences(Referral.this, "userId"));
                        RequestBody userName = createPartFromString(usernameEt.getText().toString());
                        RequestBody refferalCode = createPartFromString(promocodeEt.getText().toString());
                        RequestBody isImageUploaded = createPartFromString(isImage);
                        MultipartBody.Part userImage = null;
                        if (imagePath != null)
                            userImage = prepareFilePart("user_image");
                        RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                        Call<Common> call = apiInterface.updateProfile(userId, userName,refferalCode, isImageUploaded, userImage);
                        call.enqueue(new Callback<Common>() {
                            @Override
                            public void onResponse(Call<Common> call, Response<Common> response) {
                                pd.dismiss();
                                Common c = response.body();
                                if (c.getSuccess() == 1) {
                                    Intent intent=new Intent(Referral.this,HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    Log.e("In UpdateProfile", "Update Response : " + new Gson().toJson(response.body()));
                                } else {
                                    Config.showDialog(Referral.this, c.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Common> call, Throwable t) {
                                pd.dismiss();
                                Log.e("In UpdateProfile", "OnFailure Msg : " + t.getMessage());
                                Config.showDialog(Referral.this, Config.FailureMsg);
                            }
                        });

                    } else {
                        Config.showAlertForInternet(Referral.this);
                    }
                } else {
                    Config.showDialog(Referral.this, message);
                }
            }
        });
        
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Referral.this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = true;
                if (items[item].equals("Camera")) {
                    userChosenTask = "Camera";
                    cameraIntent();
                } else if (items[item].equals("Gallery")) {
                    userChosenTask = "Gallery";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                }
            }
        } else {
            ActivityCompat.requestPermissions(Referral.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA}, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap photo = null;
            if (requestCode == REQUEST_GALLERY) {
                if (data != null) {
                    try {
                        photo = MediaStore.Images.Media.getBitmap(Referral.this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        Log.e("In MyProfile", "IO Excp Msg : " + e.getMessage());
                    }
                }
//                userImageIv.setImageBitmap(photo);
                imagePath = new File(data.getData().toString());
//                Picasso.with(UpdateProfile.this).load(imagePath).transform(new CircleTransform()).into(userImageIv);
            } else if (requestCode == REQUEST_CAMERA) {
                photo = (Bitmap) data.getExtras().get("data");
//                userImageIv.setImageBitmap(photo);
            }
            img_profile.setImageBitmap(photo);
            String imageStoragePath = Environment.getExternalStorageDirectory() + "/Offeram";
            File imageStorageDir = new File(imageStoragePath);
            if (!imageStorageDir.exists()) {
                imageStorageDir.mkdirs();
            }
            try {
                isImage = "1";
                String userId = Config.getSharedPreferences(Referral.this, "userId");
                imagePath = new File(imageStorageDir.toString() + File.separator + userId + ".jpg");
                if (imagePath.exists()) {
                    imagePath.delete();
                }
                imagePath.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(imagePath);
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                //choose another format if PNG doesn't suit you
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
//                Picasso.with(UpdateProfile.this).load(imagePath).transform(new CircleTransform()).into(userImageIv);
//                new UpdateProfileImageTask().execute();
                Log.e("In MyProfile", "OnActResult In File(Ex-Storage) Path : " + imagePath.toString());
            } catch (FileNotFoundException e) {
                Log.e("In MyProfile", "Excp Error saving image file: " + e.getMessage());
            } catch (IOException e) {
                Log.e("In MyProfile", "Excp Error saving image file: " + e.getMessage());
            }
        }
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(imagePath.getPath()),
                        imagePath
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, imagePath.getName(), requestFile);
    }

    private void Init() {
        img_profile=(ImageView)findViewById(R.id.img_profile);
        usernameEt=(EditText)findViewById(R.id.usernameEt);
        promocodeEt=(EditText)findViewById(R.id.promocodeEt);
        txt_havepromo=(TextView)findViewById(R.id.txt_havepromo);
        submitBtn=(TextView)findViewById(R.id.submitBtn);
        promocodeLayout=(TextInputLayout)findViewById(R.id.promocodeLayout);
    }
}
