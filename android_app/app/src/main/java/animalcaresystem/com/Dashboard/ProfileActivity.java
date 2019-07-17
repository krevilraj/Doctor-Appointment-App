package animalcaresystem.com.Dashboard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import animalcaresystem.com.HelperClass;
import animalcaresystem.com.MainActivity;
import animalcaresystem.com.Model.ImageModel;
import animalcaresystem.com.Model.ResponseModel;
import animalcaresystem.com.Model.UserModel;
import animalcaresystem.com.R;
import animalcaresystem.com.WebService.RetrofitAPI;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextInputLayout textInputPhone;
    private TextInputLayout textInputName;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputRePassword;
    private TextInputLayout textInputEmail;
    private ImageView camera_capture;
    private Button btn_register;
    private Button btn_login;
    private String email = "", name = "", username = "", phone = "", password = "", passwordConf = "";
    Uri uri;
    Bitmap bitmap;
    private static final int PICK_IMAGE = 1;
    ImageView img_profile;
    String ImageUrl;
    boolean imageSet = false;
    boolean s_imageSet = false;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String token_id = "";
    String token_user_id = "";
    String token_username = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textInputName = findViewById(R.id.nname);
        textInputPhone = findViewById(R.id.nphone);
        textInputEmail = findViewById(R.id.nemail);
        textInputUsername = findViewById(R.id.nusername);
        textInputPassword = findViewById(R.id.npassword);
        textInputRePassword = findViewById(R.id.nrepassword);
        camera_capture = findViewById(R.id.img_profile_pic);
        img_profile = findViewById(R.id.img_profile);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmInput()) {
                    if (imageSet) {
                        if (!s_imageSet) {
                            SaveImage(bitmap);
                        } else {
                            update_user();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                }
            }
        });
        camera_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSet = false;
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        getToken();


    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputUsername.setError("Username too long");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else if (nameInput.length() > 15) {
            textInputName.setError("Name too long");
            return false;
        } else {
            textInputName.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phoneInput = textInputPhone.getEditText().getText().toString().trim();

        if (phoneInput.isEmpty()) {
            textInputPhone.setError("Field can't be empty");
            return false;
        } else if (phoneInput.length() > 15) {
            textInputPhone.setError("Name too long");
            return false;
        } else {
            textInputPhone.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String passwordReInput = textInputRePassword.getEditText().getText().toString().trim();

        if (passwordInput.equals(passwordReInput)) {
            return true;
        } else {
            Toast.makeText(this, "Confirm password should be same.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public boolean confirmInput() {
        if (validateEmail() && validateUsername() && validatePassword() && validateName() && validatePhone()) {
            return true;
        }
        return false;
    }

    private void update_user() {
        String url = RetrofitAPI.url + "edtProfile";
        name = textInputName.getEditText().getText().toString();
        phone = textInputPhone.getEditText().getText().toString();
        username = textInputUsername.getEditText().getText().toString();
        email = textInputEmail.getEditText().getText().toString();
        password = textInputPassword.getEditText().getText().toString();
        passwordConf = password;
        if (password == null || password.equals("")) {
            SharedPreferences pref = getSharedPreferences("UserToken", 0); // 0 - for private mode
            String s_password = pref.getString("password", null);
            if (s_password == null) {
                Toast.makeText(ProfileActivity.this, "Login session out", Toast.LENGTH_SHORT).show();

                final Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            } else {
                password = s_password;
                passwordConf = password;
            }
        }

        HelperClass.logme(url, name, username, email, phone, ImageUrl, passwordConf, password, token_id, token_user_id);

        final Call<ResponseModel> userInfo = RetrofitAPI.getService().update_user(url, name, username, email, phone, ImageUrl, passwordConf, password, token_user_id, token_id);
        userInfo.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel server_response = response.body();

                if (server_response.getSuccess().equals("Profile Updated Successfully!!")) {
                    Toast.makeText(ProfileActivity.this, "Success!!! Profile Updated Successfully ", Toast.LENGTH_SHORT).show();

                    final Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            finish();
                        }
                    }, 3000);
                } else {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(ProfileActivity.this, "Failed!!! " + server_response.getSuccess(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void get_user() {
        String url = RetrofitAPI.url + "profile";
        final Call<UserModel> userInfo = RetrofitAPI.getService().get_user_detail(url, token_id, token_username, token_user_id);
        userInfo.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel server_response = response.body();
                textInputEmail.getEditText().setText(server_response.getEmail());
                textInputUsername.getEditText().setText(server_response.getUsername());
                textInputName.getEditText().setText(server_response.getName());
                textInputPhone.getEditText().setText(server_response.getPhone());
                ImageUrl = server_response.getImage();
                if (server_response.getImage() == null || server_response.getImage().equals("")) {
                    imageSet = false;
                    s_imageSet = false;
                } else {
                    s_imageSet = true;
                    imageSet = true;
                }


                String image_url = RetrofitAPI.base_url + server_response.getImage();
                Picasso.with(ProfileActivity.this).load(image_url).fit().centerCrop()
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.no_avatar)
                        .into(img_profile);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void SaveImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        try {
            File file = new File(getCacheDir(), "image.jpg");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

            final Call<ImageModel> call = RetrofitAPI.getService().uploadImage(body);
            call.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    Log.i("test", "image re" + response.body().toString());
                    if (response.isSuccessful()) {
                        ImageModel imageModel = response.body();

                        ImageUrl = imageModel.getImage();
                        update_user();
                    }

                }

                @Override
                public void onFailure(Call<ImageModel> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Image upload Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.i("test", "error");
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            img_profile.setImageBitmap(bitmap);
            s_imageSet = false;
            imageSet = true;
        }
    }

    private void getToken() {
        SharedPreferences pref = getSharedPreferences("UserToken", 0); // 0 - for private mode
        String s_token = pref.getString("token", null);
        String s_user_id = pref.getString("user_id", null);
        String s_username = pref.getString("username", null);

        if (s_token == null || s_user_id == null || s_username == null) {
            Toast.makeText(ProfileActivity.this, "Login session out", Toast.LENGTH_SHORT).show();

            final Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        } else {
            token_id = s_token;
            token_user_id = s_user_id;
            token_username = s_username;
            get_user();
        }
    }

}
