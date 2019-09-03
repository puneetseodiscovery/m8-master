package com.mandy.m8.UploadActivity.UploadSubFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8.ApiInterface;
import com.mandy.m8.RetrofitModel.CurrecnyConvter;
import com.mandy.m8.ServiceGenerator;
import com.mandy.m8.UploadActivity.Upload2Activity;
import com.mandy.m8.R;
import com.hbb20.CountryCodePicker;
import com.mandy.m8.util.Config;
import com.mandy.m8.util.ProgressBarClass;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {
    CountryCodePicker ccp;
    Button buttonNext;
    EditText edtCounty, edtCity, edtPostcode, edtPrice;
    Spinner spinner, sOther, sType;
    TextView edtCurrecny;
    EditText edtAddress1, edtAddress2;
    String otherType;
    String currency, country, property;
    String price;
    View view;
    String type;
    Context context;


    public static Map<String, String> hashMap = new HashMap<>();

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload, container, false);


        init();

        getType();


        //radio button click listner
        edtCurrecny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                final CurrencyPicker picker = CurrencyPicker.newInstance("Currency");
                Log.d("dadafadada", "" + picker);
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String s, String s1, String s2, int i) {
                        currency = s1;
                        edtCurrecny.setText(currency);
                        picker.dismiss();
                    }
                });
                picker.show(manager, "CURRENCY_PICKER");
            }
        });


        //get the property list
        getPropertyCateogry();


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data into shared prefrence
                if (TextUtils.isEmpty(edtCounty.getText().toString()) || edtCounty.getText().length() > 25) {
                    edtCounty.setError("Invalid filed");
                    edtCounty.requestFocus();
                } else if (TextUtils.isEmpty(edtCity.getText().toString()) || edtCity.getText().length() > 25) {
                    edtCity.setError("Invalid filed");
                    edtCity.requestFocus();
                } else if (TextUtils.isEmpty(edtPostcode.getText().toString()) || edtPostcode.getText().length() > 10) {
                    edtPostcode.setError("Invalid filed");
                    edtPostcode.requestFocus();

                } else if (TextUtils.isEmpty(edtPrice.getText().toString()) || edtPrice.getText().length() > 18) {
                    edtPrice.setError("Invalid filed");
                    edtPrice.requestFocus();

                } else if (TextUtils.isEmpty(currency)) {
                    Toast.makeText(getActivity(), "Select the Currency type", Toast.LENGTH_LONG).show();
                    edtCurrecny.requestFocus();
                } else {

                    getCurrencyConvert();
                }

            }
        });


        return view;
    }

    private void init() {
        buttonNext = (Button) view.findViewById(R.id.Upload_Next);
        edtCounty = (EditText) view.findViewById(R.id.upload_county);
        edtCity = (EditText) view.findViewById(R.id.upload_city);
        edtPostcode = (EditText) view.findViewById(R.id.upload_postcode);
        edtCurrecny = (TextView) view.findViewById(R.id.upload_currency);
        edtPrice = (EditText) view.findViewById(R.id.upload_price);
        edtAddress1 = (EditText) view.findViewById(R.id.upload_address1);
        edtAddress2 = (EditText) view.findViewById(R.id.upload_address2);
        spinner = (Spinner) view.findViewById(R.id.Spinner_Property);
        sOther = (Spinner) view.findViewById(R.id.upload_other);
        sType = (Spinner) view.findViewById(R.id.upload_type);
        ccp = view.findViewById(R.id.ccp);
    }

    // get the sub category on the proerty
    private void getOther(String[] category) {
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, category);
        arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sOther.setAdapter(arrayAdapter2);

        sOther.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                otherType = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getType() {
        final String stype[] = {"Residential", "Commercial"};
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, stype);

        arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sType.setAdapter(arrayAdapter2);

        sType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //here get all the property list
    private void getPropertyCateogry() {
        final String[] addedArray = new String[]{"Property", "House", "Appartment house", "Flat", "Other object"};
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, addedArray);
        arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(arrayAdapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                property = parent.getSelectedItem().toString();
                switch (property) {
                    case "Property":
                        String[] category1 = new String[]{"Building ground", "Building land", "Land for agriculture", "Country"};
                        getOther(category1);
                        break;

                    case "House":
                        String[] category2 = new String[]{"Farm", "Bungalow", "Chalet", "Shop with Flat", "Corner house"
                                , "Detached house", "Detached chalet", "Hut", "Country house", "Duplex", "Town house", "Rustico", "Terrace house", "Villa", "Two family house"};
                        getOther(category2);
                        break;

                    case "Appartment house":
                        String[] category3 = new String[]{"Apartment house",};
                        getOther(category3);
                        break;

                    case "Flat":
                        String[] category4 = new String[]{"Attic", "Servants apartment", "Penthouse", "Holiday home", "Garden apartment", "Funished residential", "Studio", "Flat"};
                        getOther(category4);
                        break;

                    case "Other object":
                        String[] category5 = new String[]{"Investment", "Garage", "House with trade share", "Parking spot", "Barn for remodeling"};
                        getOther(category5);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //get the currency After convert
    private void getCurrencyConvert() {
        String url = Config.CURRENCY_LINK + currency + "&to=CHF&amount=" + edtPrice.getText().toString();
        Log.d("adadacadca", url);
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<CurrecnyConvter> call = apiInterface.GetCurrency(url);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<CurrecnyConvter>() {
            @Override
            public void onResponse(Call<CurrecnyConvter> call, Response<CurrecnyConvter> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == true) {
                        price = response.body().getResult().toString();
                        Log.d("pricreab", price);

                        country = ccp.getSelectedCountryName();
                        hashMap.put("property_type", type);
                        hashMap.put("property_sub_type", property);
                        hashMap.put("property_sub_type_child", otherType);


                        SharedPreferences sharedPreferences;
                        SharedPreferences.Editor editor;

                        sharedPreferences = getActivity().getSharedPreferences("UserProperty", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();

                        editor.putString("country", country);
                        editor.putString("county", edtCounty.getText().toString());
                        editor.putString("city", edtCity.getText().toString());
                        editor.putString("postcode", edtPostcode.getText().toString());
                        editor.putString("currency", "CHF");
                        editor.putString("price", price);
                        editor.putString("address1", edtAddress1.getText().toString());
                        editor.putString("address2", edtAddress2.getText().toString());
                        editor.apply();

                        Intent intent = new Intent(context, Upload2Activity.class);
                        startActivity(intent);


                    }
                }
            }

            @Override
            public void onFailure(Call<CurrecnyConvter> call, Throwable t) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }
}
