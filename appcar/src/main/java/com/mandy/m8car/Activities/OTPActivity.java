package com.mandy.m8car.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mandy.m8car.R;
import com.mandy.m8car.util.CheckInternet;

import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.util.ProgressBarClass;
import com.mandy.m8car.RetrofitModel.GetOtpApi;
import com.mandy.m8car.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    EditText editText;
    String userId;
    Button button;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        editText = findViewById(R.id.Otp);
        button = findViewById(R.id.btn_send);


        userId = getIntent().getStringExtra("UserId");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setError("Enter your recovery code");
                    editText.requestFocus();
                } else if (editText.getText().length() > 4) {
                    editText.setError("Enter only 4 numbers  recovery code");
                    editText.requestFocus();
                } else {
                    if (CheckInternet.isInternetAvailable(OTPActivity.this)) {
                        getData();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetOtpApi> call = apiInterface.getOtp(editText.getText().toString(), userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(OTPActivity.this, "Please wait...");
        call.enqueue(new Callback<GetOtpApi>() {
            @Override
            public void onResponse(Call<GetOtpApi> call, Response<GetOtpApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Token", response.body().getData().getUserId().toString());
                        editor.putString("UserStatus", response.body().getData().getProfileStatus().toString());
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), StartActivity.class));
                        finish();

                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetOtpApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
