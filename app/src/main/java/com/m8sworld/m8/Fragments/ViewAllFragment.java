package com.m8sworld.m8.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.m8sworld.m8.Activities.HomeActivity;
import com.m8sworld.m8.ApiInterface;
import com.m8sworld.m8.util.CheckInternet;
import com.m8sworld.m8.Fragments.SearchProperties.SearchReultFragment;
import com.m8sworld.m8.util.ProgressBarClass;
import com.m8sworld.m8.R;
import com.m8sworld.m8.RetrofitModel.SearchApi;
import com.m8sworld.m8.ServiceGenerator;
import com.m8sworld.m8.util.SharedToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass. 9346834541
 */
public class ViewAllFragment extends Fragment {

    Toolbar toolbar;
    ImageView drawer, toolbarLogo;

    TextView toolbarStatus;
    Button btnSearch;
    EditText txtPrice, txtPrice2;

    AutoCompleteTextView txtCity;
    Spinner spinner, spinnerRooms;
    RelativeLayout relativeRooms;
    String room;

    HashMap<String, String> hashMap = new HashMap<>();
    View view;
    Context context;
    FragmentManager manager;

    String category;
    String price;
    SharedToken sharedToken;
    String categoryId, userId;

    CountryCodePicker ccp;

    ArrayList<String> arrayList = new ArrayList<>();
    public static ArrayList<SearchApi.Datum> arrayData = new ArrayList<>();
    public static String catName;

    public ViewAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_all, container, false);
        sharedToken = new SharedToken(context);
        categoryId = sharedToken.getCatId();
        userId = sharedToken.getUserId();
        init();

        //toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        manager = getActivity().getSupportFragmentManager();

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
        toolbarStatus.setText("View all items");
        toolbarStatus.setPadding(0,0,30,0);

        //set data into dropdown(Spinner)
        setData();

        //set into Rooms
        setRooms();

        //this is used for search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckInternet.isInternetAvailable(context)) {
                    if (!TextUtils.isEmpty(txtPrice.getText().toString()) || !TextUtils.isEmpty(txtPrice2.getText().toString())) {

                        if (TextUtils.isEmpty(txtPrice.getText().toString())) {
                            txtPrice.setError("Fill the Minimum amount");
                            txtPrice.requestFocus();
                        } else if (TextUtils.isEmpty(txtPrice2.getText().toString())) {
                            txtPrice2.setError("Fill the Maximum amount");
                            txtPrice2.requestFocus();
                        } else {
                            price = txtPrice.getText().toString() + "-" + txtPrice2.getText().toString();
                            room = ccp.getSelectedCountryName();
                            SearchApi();
                        }
                    } else {
                        price = "";
                        room = ccp.getSelectedCountryName();
                        SearchApi();
                    }
                } else {
                    Toast.makeText(context, "" + getActivity().getResources().getString(R.string.NoInternet), Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        toolbarLogo = (ImageView) view.findViewById(R.id.toolbarLogo);
        toolbarStatus = (TextView) view.findViewById(R.id.toolbarText);
        btnSearch = (Button) view.findViewById(R.id.button_search);
        txtCity = (AutoCompleteTextView) view.findViewById(R.id.city);
        txtPrice = (EditText) view.findViewById(R.id.price_range);
        spinnerRooms = (Spinner) view.findViewById(R.id.rooms);
        spinner = (Spinner) view.findViewById(R.id.spinner_category);
        relativeRooms = (RelativeLayout) view.findViewById(R.id.relRooms);
        txtPrice2 = (EditText) view.findViewById(R.id.price_range2);
        ccp = view.findViewById(R.id.ccp);
        HomeActivity.cunter = 0;
    }

    private void setData() {
        arrayList.clear();
        arrayList.add("Highest paying commission");
        arrayList.add("Property");
        arrayList.add("Jobs");
        arrayList.add("Cars");
        arrayList.add("Boats");
        arrayList.add("Jewellery");
        arrayList.add("Investments");
        arrayList.add("Misc");

        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinnertext, arrayList);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(langAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("Highest paying commission"))
                {
                    category = "hc";
                }
                else
                {
                    //category = parent.getSelectedItem().toString();
                    category = String.valueOf(parent.getSelectedItemPosition());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //here the
    private void SearchApi() {
        String city = txtCity.getText().toString();
        if (room.equals("Rooms")) {
        } else {
            hashMap.put("property_rooms", room);
        }
        hashMap.put("property_sub_type", category);

        JSONObject jsonObject = new JSONObject(hashMap);

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<SearchApi> call = apiInterface.searchApi(city, price, jsonObject, category,ccp.getSelectedCountryName());
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(getActivity(), "Please wait...");
        dialog.show();

        call.enqueue(new Callback<SearchApi>() {
            @Override
            public void onResponse(Call<SearchApi> call, Response<SearchApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    catName = category;
                    arrayData.clear();
                    if (response.body().getStatus().equals(200)) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            arrayData.add(response.body().getData().get(i));
                        }
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.framelayout, new SearchReultFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "r" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchApi> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    //set Rooms data
    private void setRooms() {
        ArrayList<String> arrayRooms = new ArrayList<>();

        arrayRooms.add("Rooms");
        arrayRooms.add("1");
        arrayRooms.add("2");
        arrayRooms.add("3");
        arrayRooms.add("4");
        arrayRooms.add("5");

        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinnertext, arrayRooms);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerRooms.setAdapter(langAdapter);

        spinnerRooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                room = parent.getSelectedItem().toString();
                //country = ccp.getSelectedCountryName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }
}
