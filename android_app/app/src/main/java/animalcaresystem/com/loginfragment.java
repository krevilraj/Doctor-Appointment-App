package animalcaresystem.com;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import animalcaresystem.com.Dashboard.DashboardActivity;
import animalcaresystem.com.Model.TokenModel;
import animalcaresystem.com.Model.UserModel;
import animalcaresystem.com.WebService.RetrofitAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class loginfragment extends Fragment {


    public loginfragment() {
        // Required empty public constructor
    }

    EditText edt_email, edt_password;
    String email = "", password = "";
    Retrofit retrofit;
    Button btnlogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_loginfragment, container, false);

        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        btnlogin = view.findViewById(R.id.btn_login);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });


        return view;
    }


    private void checkLogin() {
        String url = RetrofitAPI.url + "authenticate";
        email = edt_email.getText().toString();
        password = edt_password.getText().toString();

        final Call<TokenModel> userInfo = RetrofitAPI.getService().getToken(url, email, password);
        userInfo.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                TokenModel token = response.body();
                String message = "";
                message = token.getSuccess();
                if (message.equals("false")) {
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                } else {
                    SharedPreferences pref = getActivity().getSharedPreferences("UserToken", 0); // 0 - for private mode

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", email); // Storing string
                    editor.putString("username", token.getUsername());
                    editor.putString("password", password);
                    editor.putString("token", token.getToken());
                    editor.putString("user_id", token.getId());

                    editor.commit();

                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    getActivity().startActivity(intent);
                }
                Toast.makeText(getActivity(), "" + token.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });

    }
}
