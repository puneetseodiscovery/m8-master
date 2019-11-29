package com.m8.m8.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.m8.m8.util.CheckInternet;
import com.m8.m8.R;
import com.m8.m8.ApiInterface;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.RetrofitModel.PasswordOTP;
import com.m8.m8.RetrofitModel.PasswordReset;
import com.m8.m8.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmEmailActivity extends AppCompatActivity {

    EditText edtEmail, edtOtp, edtPassword, edtCPassword;
    Button button;
    String email, otp, password, cpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email);

        init();

        edtOtp.setVisibility(View.GONE);
        edtPassword.setVisibility(View.GONE);
        edtCPassword.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().toString().equals("Send than check your email")) {
                    if (CheckInternet.isInternetAvailable(ConfirmEmailActivity.this)) {
                        getOtp();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();
                    }

                }
                if (button.getText().toString().equals("Ok")) {
                    if (CheckInternet.isInternetAvailable(ConfirmEmailActivity.this)) {
                        sendOTP();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();
                    }

                }

                if (button.getText().toString().equals("Set password")) {
                    if (CheckInternet.isInternetAvailable(ConfirmEmailActivity.this)) {
                        confirmPasswrod();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });


        if (getIntent() != null) {
            edtEmail.setText(getIntent().getStringExtra("emial"));
        }

    }

    private void init() {
        edtEmail = (EditText) findViewById(R.id.email);
        edtOtp = (EditText) findViewById(R.id.otp);
        edtPassword = (EditText) findViewById(R.id.password);
        edtCPassword = (EditText) findViewById(R.id.c_password);
        button = (Button) findViewById(R.id.button);
    }


    private void getOtp() {
        email = edtEmail.getText().toString();
        if (TextUtils.isEmpty(email) && email.length() > 30) {
            edtEmail.setError("Enter a valid email address");
            edtEmail.requestFocus();
        } else {

            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<PasswordOTP> call = apiInterface.passwrdOTP(email);
            final ProgressDialog dialog = ProgressBarClass.showProgressDialog(this, "Please wait...");
            dialog.show();
            call.enqueue(new Callback<PasswordOTP>() {
                @Override
                public void onResponse(Call<PasswordOTP> call, Response<PasswordOTP> response) {

                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(200)) {
                            Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                            edtOtp.setVisibility(View.VISIBLE);
                            button.setText("Ok");
                            edtEmail.setVisibility(View.GONE);

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ConfirmEmailActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PasswordOTP> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(ConfirmEmailActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }


    private void sendOTP() {
        otp = edtOtp.getText().toString();
        if (TextUtils.isEmpty(otp) || otp.length() > 4) {
            edtOtp.setError("Invalid recovery code");
        } else {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<PasswordOTP> call = apiInterface.sendPasswrodOtp(email, otp);
            final ProgressDialog dialog = ProgressBarClass.showProgressDialog(this, "please wait...");
            dialog.show();
            call.enqueue(new Callback<PasswordOTP>() {
                @Override
                public void onResponse(Call<PasswordOTP> call, Response<PasswordOTP> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(200)) {
                            Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                            edtCPassword.setVisibility(View.VISIBLE);
                            edtPassword.setVisibility(View.VISIBLE);
                            edtOtp.setVisibility(View.GONE);

                            button.setText("Set password");
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(ConfirmEmailActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PasswordOTP> call, Throwable t) {
                    Toast.makeText(ConfirmEmailActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }
    }


    private void confirmPasswrod() {
        password = edtPassword.getText().toString();
        cpassword = edtCPassword.getText().toString();

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<PasswordReset> call = apiInterface.passwordReset(email, otp, password, cpassword);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(this, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<PasswordReset>() {
            @Override
            public void onResponse(Call<PasswordReset> call, Response<PasswordReset> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(ConfirmEmailActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("Email", response.body().getData().getEmail());
                        startActivity(intent);
                        finish();
                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ConfirmEmailActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<PasswordReset> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ConfirmEmailActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
