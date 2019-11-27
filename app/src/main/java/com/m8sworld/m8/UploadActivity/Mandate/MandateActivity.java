package com.m8sworld.m8.UploadActivity.Mandate;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.m8sworld.m8.Activities.HomeActivity;
import com.m8sworld.m8.ApiInterface;
import com.m8sworld.m8.util.ProgressBarClass;
import com.m8sworld.m8.R;
import com.m8sworld.m8.RetrofitModel.AddMandataApi;
import com.m8sworld.m8.RetrofitModel.UploadBulkMandateApi;
import com.m8sworld.m8.ServiceGenerator;
import com.m8sworld.m8.util.SharedRate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MandateActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView textView;
    ImageView drawer, toolbarLogo;
    EditText edtPrice;
    Spinner edtPercentage;
    TextView edtCurrency;
    public static String currency, Id, pType, priceValue;

    public static String offer;
    String date, type;
    double min, max;
    String Smin, Smax;
    Button btnSubmit;
    TextView textHint;
    int mi, ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandate2);

        init();


        Id = getIntent().getStringExtra("PropertyId");
        type = getIntent().getStringExtra("type");
        if (type.equals("1")) {
            Smin = getIntent().getStringExtra("min");
            Smax = getIntent().getStringExtra("max");

            min = Double.parseDouble(Smin);
            max = Double.parseDouble(Smax);

            mi = (int) min;
            ma = (int) max;

            edtPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (TextUtils.isEmpty(edtPrice.getText().toString())) {

                    } else {
                        int number = Integer.parseInt(edtPrice.getText().toString());
                        if (number > max || number < mi) {
                            edtPrice.setError("Enter the minimun amount " + mi + " and max amount: " + max);
                        }else {
                            SharedRate sharedRate = new SharedRate(MandateActivity.this);
                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(edtPrice.getText().toString());
                            priceValue = String.valueOf(price);
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }


        textHint.setText("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer.setVisibility(View.INVISIBLE);

        textView.setText("Create your Mandate");

//        toolbarLogo.setVisibility(View.GONE);
        // data add into offer
        AddSpinner();


        if (type.equals("0")) {
            edtPrice.setEnabled(false);
            edtPrice.setFocusable(false);
            edtPrice.setFocusableInTouchMode(false);
            edtPrice.setClickable(false);
        }


        //view thw text
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("1")) {
                    if (!offer.equals("25 % Launch offer only") && !edtPrice.getText().toString().equals("")) {
                        Snackbar.make(findViewById(android.R.id.content), "Enter only one  Comission field ", Snackbar.LENGTH_LONG).show();
                    } else if (!offer.equals("25 % Launch offer only")) {
                        pType = "P";
                        priceValue = offer;
                        SendData();
                    } else if (!edtPrice.getText().toString().equals("")) {
//                        SharedRate sharedRate = new SharedRate(MandateActivity.this);
//                        double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(edtPrice.getText().toString());
//                        priceValue = String.valueOf(price);
                        pType = "F";
                        SendData();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Please enter your offer.", Snackbar.LENGTH_LONG).show();
                    }
                }
                if (type.equals("0")) {
                    getBulkmandate();
                }
            }
        });

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


//        edtCurrency.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager manager = getSupportFragmentManager();
//                final CurrencyPicker picker = CurrencyPicker.newInstance("Currecny Picker");
//                picker.setListener(new CurrencyPickerListener() {
//                    @Override
//                    public void onSelectCurrency(String s, String s1, String s2, int i) {
//                        currency = s1;
//                        edtCurrency.setText(currency);
//                        picker.dismiss();
//                    }
//                });
//
//                picker.show(manager, "CURRENCY_PICKER");
//            }
//        });


    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        toolbarLogo = (ImageView) findViewById(R.id.toolbarLogo);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        edtCurrency = (TextView) findViewById(R.id.mandata_curreny);
        edtPercentage = (Spinner) findViewById(R.id.mandata_add);
        edtPrice = (EditText) findViewById(R.id.mandata_price);
        textHint = (TextView) findViewById(R.id.txtPriceHint);


    }


    //send data for see the mandate
    public void SendData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<AddMandataApi> call = apiInterface.mandataApi(pType, priceValue, date + " 00:00:00", HomeActivity.userId, "1", Id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(MandateActivity.this, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<AddMandataApi>() {
            @Override
            public void onResponse(Call<AddMandataApi> call, Response<AddMandataApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Intent intent = new Intent(MandateActivity.this, Mandate2Activity.class);
                        intent.putExtra("responce", "https://app.m8s.world/public/upload/items/mandate/" + response.body().getData().getMandatePdf());
                        intent.putExtra("type", type);
                        intent.putExtra("PropertyId", Id);

                        startActivity(intent);

                    } else {
                        Toast.makeText(MandateActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MandateActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddMandataApi> call, Throwable t) {

                dialog.dismiss();
                Toast.makeText(MandateActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void AddSpinner() {
        String data[] = {"RE/MAX", "50", "55", "60", "65"};
        //mandate percentage   drop down
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(MandateActivity.this, R.layout.spinnertext, data);
        arrayAdapter1.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        edtPercentage.setAdapter(arrayAdapter1);

        edtPercentage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem().toString().equals("RE/MAX"))
                {
                    offer = "25";
                }
                else {
                    offer = parent.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getBulkmandate() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<UploadBulkMandateApi> call = apiInterface.bulkMandate(Id, HomeActivity.userId, "1", date + " 00:00:00", "P", priceValue);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(MandateActivity.this, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<UploadBulkMandateApi>() {
            @Override
            public void onResponse(Call<UploadBulkMandateApi> call, Response<UploadBulkMandateApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals(200)) {

                        Intent intent = new Intent(MandateActivity.this, Mandate2Activity.class);
                        intent.putExtra("responce", "https://app.m8s.world/public/upload/items/mandate/" + response.body().getData().getMandatePdf());
                        intent.putExtra("type", type);
                        intent.putExtra("PropertyId", Id);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MandateActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MandateActivity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadBulkMandateApi> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
