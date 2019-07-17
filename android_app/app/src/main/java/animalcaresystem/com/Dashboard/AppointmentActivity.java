package animalcaresystem.com.Dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import animalcaresystem.com.HelperClass;
import animalcaresystem.com.Model.ResponseModel;
import animalcaresystem.com.R;
import animalcaresystem.com.WebService.RetrofitAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentActivity extends AppCompatActivity implements
        View.OnClickListener {

    Button btndate, btntime;
    EditText in_date, in_timee;
//    TextInputLayout in_date;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextInputLayout mNfullname;
    private TextInputLayout mNpetname;
    private TextInputLayout mNemail;
    private TextInputLayout mNphno;
    private Button mBtnAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);


        btntime = findViewById(R.id.btntime);


        in_timee = findViewById(R.id.in_timee);
        in_date = findViewById(R.id.in_date);
        btndate = (Button) findViewById(R.id.btndate);
        btndate.setOnClickListener(this);
        btntime.setOnClickListener(this);


        mNfullname = findViewById(R.id.nfullname);
        mNpetname = findViewById(R.id.npetname);
        mNemail = findViewById(R.id.nemail);
        mNphno = findViewById(R.id.nphno);
        mBtnAppointment = findViewById(R.id.btn_appointment);
        mBtnAppointment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if (v == btndate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            in_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btntime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            in_timee.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if (v == mBtnAppointment) {
            String name = mNfullname.getEditText().getText().toString();
            String petname = mNpetname.getEditText().getText().toString();
            String email = mNemail.getEditText().getText().toString();
            String phone = mNphno.getEditText().getText().toString();
            String date = in_date.getText().toString();
            String time = in_timee.getText().toString();

            String url = RetrofitAPI.url + "appointment";

            if (HelperClass.v_input_lyt(mNfullname) && HelperClass.v_input_lyt(mNpetname) && HelperClass.v_input_lyt(mNemail) && HelperClass.v_input_lyt(mNphno) && HelperClass.v_edt_txt(in_date) && HelperClass.v_edt_txt(in_timee)) {
                final Call<ResponseModel> userInfo = RetrofitAPI.getService().takeAppointment(url, name, petname, email, phone, date, time);
                userInfo.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        ResponseModel server_response = response.body();
                        Toast.makeText(AppointmentActivity.this, "Success!!!", Toast.LENGTH_SHORT).show();
                        resetAllField();
                        HelperClass.givenotification(AppointmentActivity.this, server_response.getSuccess());
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(AppointmentActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }

                });
            }else{
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);
            }


        }

    }

    private void resetAllField() {
        mNfullname.getEditText().setText("");
        mNpetname.getEditText().setText("");
        mNemail.getEditText().setText("");
        mNphno.getEditText().setText("");
        in_date.setText("");
        in_timee.setText("");
    }
}
