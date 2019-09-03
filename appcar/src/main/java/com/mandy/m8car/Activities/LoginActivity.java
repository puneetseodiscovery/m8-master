package com.mandy.m8car.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.R;
import com.mandy.m8car.util.CheckInternet;
import com.mandy.m8car.util.ProgressBarClass;
import com.mandy.m8car.RetrofitModel.LoginApi;
import com.mandy.m8car.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editLogin, editPassword;
    Button btnLogin;
    TextView textForgot;
    CheckBox imageView;
    LinearLayout SignUp;
    CheckBox checkBox;
    RelativeLayout relativeLayout;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        init();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editLogin.getText().toString())) {
                    editLogin.setError("Invalid Email");
                } else if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    editPassword.setError("Invalid Password");
                } else {
                    if (CheckInternet.isInternetAvailable(LoginActivity.this)) {
                        LoginApi();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();
                    }

                }

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();
            }
        });

        imageView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editPassword.setTransformationMethod(null);
                } else {
                    editPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });


        SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
        String id = shared.getString("hello", "");
        if (id.equals("1")) {
            checkBox.setChecked(true);
            editPassword.setText(shared.getString("pem", ""));
            editLogin.setText(shared.getString("lem", ""));

        } else {
            checkBox.setChecked(false);
            editPassword.setText("");
            editLogin.setText("");

        }


        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfirmEmailActivity.class);
                intent.putExtra("emial", editLogin.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void init() {
        editLogin = (EditText) findViewById(R.id.login_email);
        editPassword = (EditText) findViewById(R.id.login_password);
        btnLogin = (Button) findViewById(R.id.login_btn);
        SignUp = (LinearLayout) findViewById(R.id.linear_sign);
        imageView = (CheckBox) findViewById(R.id.passwordshow);
        checkBox = (CheckBox) findViewById(R.id.checkboxSign);
        textForgot = (TextView) findViewById(R.id.forgot);
        relativeLayout = (RelativeLayout) findViewById(R.id.layoutLogin);
    }


    public void LoginApi() {

        String email = editLogin.getText().toString();
        String password = editPassword.getText().toString();
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<LoginApi> call = apiInterface.login(email, password);

        final ProgressDialog myDialog = ProgressBarClass.showProgressDialog(this, "Please wait...");
        myDialog.show();


        call.enqueue(new Callback<LoginApi>() {

            @Override
            public void onResponse(Call<LoginApi> call, Response<LoginApi> response) {
                myDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        startActivity(new Intent(getApplicationContext(), StartActivity.class));
                        finish();
                        SharedPreferences sharedPreferences;
                        SharedPreferences.Editor editor;
                        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("Token", response.body().getData().getUserId().toString());
                        editor.putString("UserStatus", response.body().getData().getProfileStatus().toString());
                        editor.apply();

                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        if (checkBox.isChecked()) {
                            SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = shared.edit();
                            edit.putString("hello", "1");
                            edit.putString("pem", editPassword.getText().toString());
                            edit.putString("lem", editLogin.getText().toString());
                            edit.apply();
                        } else {
                            SharedPreferences shared = getSharedPreferences("check", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = shared.edit();
                            edit.putString("hello", "0");
                            edit.apply();
                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        editPassword.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginApi> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                myDialog.dismiss();
            }
        });

    }

}
