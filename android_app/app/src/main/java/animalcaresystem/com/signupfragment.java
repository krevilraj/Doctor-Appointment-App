package animalcaresystem.com;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import animalcaresystem.com.Model.ImageModel;
import animalcaresystem.com.Model.ResponseModel;
import animalcaresystem.com.WebService.RetrofitAPI;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class signupfragment extends Fragment {
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputRePassword;
    private TextInputLayout textInputEmail;
    private ImageView camera_capture;
    private Button btn_register;
    private Button btn_login;
    private String email = "", username = "", password = "", passwordConf = "";
    Uri uri;
    Bitmap bitmap;
    private static final int PICK_IMAGE = 1;
    ImageView img_profile;
    String ImageUrl;
    boolean imageSet = false;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private CheckBox mChkBox1;

    public signupfragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signupfragment, container, false);
        textInputEmail = view.findViewById(R.id.nemail);
        textInputUsername = view.findViewById(R.id.nusername);
        textInputPassword = view.findViewById(R.id.npassword);
        textInputRePassword = view.findViewById(R.id.nrepassword);
        camera_capture = view.findViewById(R.id.img_profile_pic);
        img_profile = view.findViewById(R.id.img_profile);
        btn_register = view.findViewById(R.id.btn_register);
        btn_login = view.findViewById(R.id.btn_login);
        mChkBox1 = view.findViewById(R.id.chkBox1);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mChkBox1.isChecked()) {
                    if (confirmInput()) {
                        register_user();
                       /* if (imageSet) {
                            SaveImage(bitmap);
                        } else {
                            Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }else{
                    Toast.makeText(getActivity(), "Please accept the agreement", Toast.LENGTH_SHORT).show();
                }
            }
        });
        camera_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSet = false;
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

//                openGallery();
            }
        });
        return view;
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

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String passwordReInput = textInputRePassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
        }
        if (passwordReInput.isEmpty()) {
            textInputRePassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public boolean confirmInput() {
        if (validateEmail() && validateUsername() && validatePassword()) {
            return true;
        }
        return false;
    }

    private void register_user() {
        String url = RetrofitAPI.url + "signup";
        username = textInputUsername.getEditText().getText().toString();
        email = textInputEmail.getEditText().getText().toString();
        password = textInputPassword.getEditText().getText().toString();
        passwordConf = password;
        final Call<ResponseModel> userInfo = RetrofitAPI.getService().register_user(url, username, email, ImageUrl, passwordConf, password);
        userInfo.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel server_response = response.body();

                if (server_response.getSuccess().equals("You are regestered,You can login now.")) {
                    Toast.makeText(getActivity(), "Success!!! Now you can login  ", Toast.LENGTH_SHORT).show();

                    final Intent intent = new Intent(getActivity(), MainActivity.class);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    }, 3000);
                } else {
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(getActivity(), "Failed!!! " + server_response.getSuccess(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(), "Please select a image", Toast.LENGTH_SHORT).show();
            }
            uri = data.getData();
        }

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            img_profile.setImageBitmap(bitmap);
            imageSet = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    private void openGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Choose Image"), PICK_IMAGE);
    }


    //insert image

    private void SaveImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        try {
            File file = new File(getActivity().getCacheDir(), "image.jpg");
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
                        register_user();
                    }

                }

                @Override
                public void onFailure(Call<ImageModel> call, Throwable t) {
                    Toast.makeText(getActivity(), "Image upload Failed", Toast.LENGTH_SHORT).show();
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

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            img_profile.setImageBitmap(bitmap);
            imageSet = true;
        }
    }

}
