package com.mandy.m8.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8.Activities.Terms.TermsActivity;
import com.mandy.m8.R;
import com.mandy.m8.ApiInterface;
import com.mandy.m8.util.CheckInternet;
import com.mandy.m8.util.ProgressBarClass;
import com.mandy.m8.RetrofitModel.RegisterApi;
import com.mandy.m8.ServiceGenerator;
import com.mandy.m8.SplashScreen;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText firstName, lastName, Email, Contact, Passsword, cPassword;
    LinearLayout login;
    Button SignUp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RadioButton radioButton;
    RoundedImageView imageFacebook, linkdin;
    LoginButton facebooklogin;
    CallbackManager callbackManager;
    CheckBox imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        callbackManager = CallbackManager.Factory.create();
        init();

        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(firstName.getText().toString()) && firstName.getText().length() > 20) {
                    firstName.setError("Enter your first name");
                } else if (TextUtils.isEmpty(lastName.getText().toString()) && lastName.getText().length() > 20) {
                    lastName.setError("Enter your last name");
                } else if (TextUtils.isEmpty(Email.getText().toString()) && Email.getText().length() > 30) {
                    Email.setError("Enter your email");
                } else if (TextUtils.isEmpty(Contact.getText().toString()) && Contact.getText().length() > 16 && Contact.getText().length() < 6) {
                    Contact.setError("Enter your Number");
                } else if (TextUtils.isEmpty(Passsword.getText().toString()) && TextUtils.isEmpty(cPassword.getText().toString())) {
                    Passsword.setError("Enter your Password");
                    cPassword.setError("Enter your Confirm password");
                } else if (Passsword.getText().length() < 8 || Pattern.matches(Passsword.getText().toString(), "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")) {
                    Passsword.setError("Password contains 8 character with uppercase and lowercase with number");
                } else if (!Passsword.getText().toString().equals(cPassword.getText().toString())) {
                    cPassword.setError("Password Does not match");
                } else if (!radioButton.isChecked()) {
                    Toast.makeText(SignupActivity.this, "Please Accept the Terms and condition", Toast.LENGTH_LONG).show();
                } else {
                    if (CheckInternet.isInternetAvailable(SignupActivity.this)) {
                        RegisterApi();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + getResources().getString(R.string.noInternet), Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        facebooklogin.setReadPermissions(Arrays.asList("email", "public_profile"));

        FacebookLogin();

        imageFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternet.isInternetAvailable(getApplicationContext())) {
                    facebooklogin.performClick();
                } else {
                    Toast.makeText(SignupActivity.this, "" + getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
                }

            }
        });


        imageView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Passsword.setTransformationMethod(null);
                    cPassword.setTransformationMethod(null);
                } else {
                    Passsword.setTransformationMethod(new PasswordTransformationMethod());
                    cPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        linkdin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getHashKey();

//get the art & condition
        termsAndCondition();


    }

    private void init() {
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        Email = (EditText) findViewById(R.id.email);
        Contact = (EditText) findViewById(R.id.contact);
        Passsword = (EditText) findViewById(R.id.password);
        cPassword = (EditText) findViewById(R.id.c_password);
        login = (LinearLayout) findViewById(R.id.login_linear);
        SignUp = (Button) findViewById(R.id.sign_btn);
        radioButton = (RadioButton) findViewById(R.id.radio);
        facebooklogin = (LoginButton) findViewById(R.id.signUpFace);
        imageFacebook = (RoundedImageView) findViewById(R.id.facebookSign);
        linkdin = (RoundedImageView) findViewById(R.id.linkDin);
        imageView = (CheckBox) findViewById(R.id.password_check);
    }


    //here Register the client
    public void RegisterApi() {
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String email = Email.getText().toString();
        String password = Passsword.getText().toString();
        String contact = Contact.getText().toString();
        String c_Password = cPassword.getText().toString();

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<RegisterApi> call = apiInterface.register(first, last, email, password, contact, c_Password, SplashScreen.code);
        //Progress bar
        final ProgressDialog myDialog = ProgressBarClass.showProgressDialog(this, "Please wait...");
        myDialog.show();

        call.enqueue(new Callback<RegisterApi>() {
            @Override
            public void onResponse(Call<RegisterApi> call, Response<RegisterApi> response) {
                myDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("UserId", response.body().getData().getUserId().toString());
                        startActivity(intent);
                        finish();

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {

                }

            }

            @Override
            public void onFailure(Call<RegisterApi> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                myDialog.dismiss();
            }
        });


    }


    public void FacebookLogin() {
        facebooklogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                boolean loggedin = AccessToken.getCurrentAccessToken() == null;

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
                        , new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                if (AccessToken.getCurrentAccessToken() != null) {

                                    try {
                                        firstName.setText(object.getString("first_name"));
                                        lastName.setText(object.getString("last_name"));
                                        Email.setText(object.getString("email"));
                                        String id = object.getString("id");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void termsAndCondition() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.iagree));
        //click for terms and condtion
        ClickableSpan Terms = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignupActivity.this, TermsActivity.class);
                intent.putExtra("terms", "Terms & Condition");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };


        //privecy
        ClickableSpan privecy = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignupActivity.this, TermsActivity.class);
                intent.putExtra("terms", "Privacy policy");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);

            }
        };

        //art
        ClickableSpan art = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignupActivity.this, TermsActivity.class);
                intent.putExtra("terms", "Art & Statement");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(Terms, 18, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(privecy, 40, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(art, 59, 72, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        radioButton.setText(ss, TextView.BufferType.SPANNABLE);
        radioButton.setHighlightColor(Color.TRANSPARENT);
        radioButton.setText(ss);
        radioButton.setMovementMethod(LinkMovementMethod.getInstance());
    }


    //Genrate HashKey here...
    public void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.m8",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

}



