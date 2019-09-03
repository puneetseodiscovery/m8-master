package com.mandy.m8.Fragments.MyAccountSubFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mandy.m8.Activities.HomeActivity;
import com.mandy.m8.ApiInterface;
import com.mandy.m8.util.CheckInternet;
import com.mandy.m8.util.ProgressBarClass;
import com.mandy.m8.R;
import com.mandy.m8.RetrofitModel.BankDeatilsApi;
import com.mandy.m8.RetrofitModel.BankDeatilsUploadApi;
import com.mandy.m8.ServiceGenerator;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditBankFragment extends Fragment {

    Toolbar toolbar;
    ImageView toolbarLogo, drawer;
    View view;
    Context context;
    Button btnOk;
    EditText edtBankName, edtAddress1, edtCity, edtPostcode, edtAccountName, edtAccountNumber, edtIBAN, edtSwift, edtPaypal;
    String bankName, address1, address2, city, country, postcode, accountName, accountNumber, iban, swift, paypal;

    CountryCodePicker ccp;

    public EditBankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_bank, container, false);

        init();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        toolbarLogo.setVisibility(View.VISIBLE);

        if (CheckInternet.isInternetAvailable(context)) {
            GetBankDetail();
        } else {
            Toast.makeText(context, "" + getActivity().getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
        }


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtBankName.getText().toString()) && edtBankName.getText().length() > 20) {
                    edtBankName.setError("Invalid Bank name");
                } else if (TextUtils.isEmpty(edtAddress1.getText().toString())) {
                    edtAddress1.setError("Invalid Address");
                } else if (TextUtils.isEmpty(edtCity.getText().toString())) {
                    edtCity.setError("Invalid City");
                } else if (TextUtils.isEmpty(edtPostcode.getText().toString())) {
                    edtPostcode.setError("Invalid Postcode");
                } else if (TextUtils.isEmpty(edtAccountName.getText().toString())) {
                    edtAccountName.setError("Invalid Account Name");
                } else if (TextUtils.isEmpty(edtAccountNumber.getText().toString())) {
                    edtAccountNumber.setError("Invalid Accout Number");
                } else if (TextUtils.isEmpty(edtIBAN.getText().toString())) {
                    edtIBAN.setError("Invalid IBAN");
                } else if (TextUtils.isEmpty(edtSwift.getText().toString())) {
                    edtSwift.setError("Invalid Swift code");
                } else if (TextUtils.isEmpty(edtPaypal.getText().toString())) {
                    edtPaypal.setError("Invalid PayPal email");
                } else {
                    if (CheckInternet.isInternetAvailable(context)) {

                        BankApi();
                    } else {
                        Toast.makeText(context, "" + getActivity().getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        return view;
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        toolbarLogo = (ImageView) view.findViewById(R.id.toolbarLogo);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        btnOk = (Button) view.findViewById(R.id.button_ok);
        edtAccountName = (EditText) view.findViewById(R.id.bank_accountName);
        edtAccountNumber = (EditText) view.findViewById(R.id.bank_accountNumber);
        edtAddress1 = (EditText) view.findViewById(R.id.bank_address1);
        edtBankName = (EditText) view.findViewById(R.id.bank_name);
        edtCity = (EditText) view.findViewById(R.id.bank_city);
        ccp = (CountryCodePicker) view.findViewById(R.id.bank_country);
        edtIBAN = (EditText) view.findViewById(R.id.bank_iban);
        edtPostcode = (EditText) view.findViewById(R.id.bank_postcode);
        edtSwift = (EditText) view.findViewById(R.id.bank_swift);
        edtPaypal = (EditText) view.findViewById(R.id.paypal);
    }


    //send the bank information to api
    private void BankApi() {
        bankName = edtBankName.getText().toString();
        address1 = edtAddress1.getText().toString();
        city = edtCity.getText().toString();
        postcode = edtPostcode.getText().toString();
        country = ccp.getSelectedCountryName();
        accountName = edtAccountName.getText().toString();
        accountNumber = edtAccountNumber.getText().toString();
        iban = edtIBAN.getText().toString();
        swift = edtSwift.getText().toString();
        paypal = edtPaypal.getText().toString();

        Log.d("getStringData", bankName + address2 + postcode + paypal);

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BankDeatilsUploadApi> call = apiInterface.bankDetailsApi(HomeActivity.userId, bankName, address1, city, postcode, country, accountName, accountNumber, iban, swift, paypal);

        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(getActivity(), "Please wait");
        dialog.show();

        call.enqueue(new Callback<BankDeatilsUploadApi>() {
            @Override
            public void onResponse(Call<BankDeatilsUploadApi> call, Response<BankDeatilsUploadApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BankDeatilsUploadApi> call, Throwable t) {
                dialog.dismiss();

            }
        });

    }


    //getBank details from apis
    private void GetBankDetail() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BankDeatilsApi> call = apiInterface.bankDetailsGet(HomeActivity.userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(getActivity(), "Please wait...");
        dialog.show();
        call.enqueue(new Callback<BankDeatilsApi>() {
            @Override
            public void onResponse(Call<BankDeatilsApi> call, Response<BankDeatilsApi> response) {

                dialog.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        edtBankName.setText(response.body().getData().getBankName());
                        edtAddress1.setText(response.body().getData().getAddress1());
                        edtCity.setText(response.body().getData().getTown());
                        edtPostcode.setText(response.body().getData().getPostCode());
                        edtAccountName.setText(response.body().getData().getAccountName());
                        edtAccountNumber.setText(response.body().getData().getAccountNumber());
                        edtIBAN.setText(response.body().getData().getIban());
                        edtSwift.setText(response.body().getData().getSwiftCode());
                        edtPaypal.setText(response.body().getData().getPaypalEmail());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(context, "Fill the bank Details", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BankDeatilsApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

}
