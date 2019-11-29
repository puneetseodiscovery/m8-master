package com.m8.m8.Fragments;


import android.Manifest;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.m8.controller.Controller;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.Activities.NoInternetActivity;
import com.m8.m8.Fragments.DrawerNavigation.MyPropertyFragment;
import com.m8.m8.Fragments.MyAccountSubFragment.BusinessFragment.Business2Fragment2;
import com.m8.m8.Models.SearchResultResponseModel;
import com.m8.m8.RetrofitModel.GetMetaData;
import com.m8.m8.RetrofitModel.GetShareNumberApi;
import com.m8.m8.util.CheckInternet;
import com.m8.m8.R;
import com.m8.m8.ApiInterface;
import com.m8.m8.AppMapView;
import com.m8.m8.RetrofitModel.GetPotentialEarn;
import com.m8.m8.RetrofitModel.MapApi;
import com.m8.m8.ServiceGenerator;


import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.util.SharedToken;
import com.m8.m8.util.UtileDilog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.maps.android.ui.IconGenerator;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener, Controller.GetPropertyNumber {


    RoundedImageView myPotentail, Commission, Upload, myShare, ViewAll, myAccount;
    Spinner spinner;
    TextView textView;
    View view;

    FragmentManager manager;
    FragmentTransaction transaction;
    ImageView imageView;
    Toolbar toolbar;
    EditText editSearch;
    AppMapView mapView;
    GoogleMap Gmap;
    NestedScrollView nestedScrollView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Double> arrayLat = new ArrayList<>();
    ArrayList<Double> arrayLong = new ArrayList<>();
    ArrayList<String> arrayCost = new ArrayList<>();
    ArrayList<Bitmap> arrayLogo = new ArrayList<>();
    Marker marker;

    private GoogleApiClient googleApiClient;
    LocationManager locationManager;
    Context context;
    IconGenerator generator;
    int propertyNumber;
    TextView txtEarn;

    String countryCode, userId, categoryid;
    public  static String earning;
    SharedToken sharedToken;
    Controller controller;


    String lat = "", lng = "";


    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
    GetMetaData.Data data;
     public Bitmap bitmap;
    Activity activity1;
    String mapTextColor;

    ImageView iconimage;
    TextView icontext;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        controller = new Controller(this);
        sharedToken = new SharedToken(context);
        userId = sharedToken.getUserId();
        categoryid = sharedToken.getCatId();
        //Toast.makeText(context, ""+categoryid, Toast.LENGTH_SHORT).show();
        //find All id
        init();

        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    HomeActivity.drawerLayout.closeDrawer(Gravity.RIGHT);
                } else
                    HomeActivity.drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });


        Places.initialize(context, context.getResources().getString(R.string.googleclientId));

        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(Previous work by amit)
//                Intent intent = new Autocomplete.IntentBuilder(
//                        AutocompleteActivityMode.FULLSCREEN, fields)
//                        .build(context);
//                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

                //(new api by puneet)


            }
        });

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click

                    //...perform search
                    performSearch(categoryid,v.getText().toString());
                    editSearch.clearFocus();
                    editSearch.setText("");
                    InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });


        //Add language into spinner
        AddIntoSpinner();

        //check for location
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();


        onClick();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        MapsInitializer.initialize(this.getActivity());


        if (CheckInternet.isInternetAvailable(context)) {
            getLoaction();
            getEarning();
            controller.setGetPropertyNumber(userId);
        } else {
            startActivity(new Intent(context, NoInternetActivity.class));
        }


        return view;
    }

    public void performSearch(String categoryid,String location){

        if (CheckInternet.isInternetAvailable(getContext())) {

            final ProgressDialog progressDialog = ProgressBarClass.showProgressDialog(getContext(),"Please wait.");

            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<SearchResultResponseModel> call = apiInterface.getSearchResult(categoryid, location);
            call.enqueue(new Callback<SearchResultResponseModel>() {
                @Override
                public void onResponse(Call<SearchResultResponseModel> call, Response<SearchResultResponseModel> response) {

                    if (progressDialog!=null)
                    progressDialog.dismiss();

                    if (response.body().getStatus() != null) {
                        if (response.body().getStatus().equals(200)) {

                            if (response.body().getLat()!=null) {

                                lat = response.body().getLat();
                                lng = response.body().getLng();
//                            FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            if (Build.VERSION.SDK_INT >= 26) {
//                                ft.setReorderingAllowed(false);
//                            }
//                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                                Gmap.clear();
                                Gmap.addCircle(new CircleOptions()
                                        .center(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).radius(25000)
                                        .strokeWidth(0f)
                                        .fillColor(context.getResources().getColor(R.color.lightred)));

                                CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).zoom(9.3f).bearing(0).tilt(40).build();
                                Gmap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                getMapData();
                            }else
                            {
                                Toast.makeText(context, "No item found for your loacation.", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SearchResultResponseModel> call, Throwable t) {

                    if (progressDialog!=null)
                        progressDialog.dismiss();
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(context, "Check your internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {
        if (CheckInternet.isInternetAvailable(context)) {

        } else {
            startActivity(new Intent(context, NoInternetActivity.class));
        }
        spinner = (Spinner) view.findViewById(R.id.spineer);
        myPotentail = (RoundedImageView) view.findViewById(R.id.Home_mypotential);
        Commission = (RoundedImageView) view.findViewById(R.id.Home_commission);
        Upload = (RoundedImageView) view.findViewById(R.id.Home_Upload);
        myShare = (RoundedImageView) view.findViewById(R.id.Home_myshare);
        ViewAll = (RoundedImageView) view.findViewById(R.id.Home_viewall);
        myAccount = (RoundedImageView) view.findViewById(R.id.Home_myaccount);
        textView = (TextView) view.findViewById(R.id.Home_click_here);
        imageView = (ImageView) view.findViewById(R.id.drawer);
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        mapView = (AppMapView) view.findViewById(R.id.map);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrool);
        editSearch = (EditText) view.findViewById(R.id.edtSearch);

        txtEarn = (TextView) view.findViewById(R.id.txtEaring);


        HomeActivity.cunter = 0;
        GetMetaDetails();

        generator = new IconGenerator(context);
        generator.setStyle(IconGenerator.STYLE_WHITE);
        View clusterView = LayoutInflater.from(context).inflate(R.layout.icongeneratorimage, null);
        iconimage = clusterView.findViewById(R.id.iconimage);
        icontext = clusterView.findViewById(R.id.icontext);
        generator.setContentView(clusterView);

    }



    private void onClick() {
        myPotentail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.framelayout, new MyPotentialFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.navigationCommission);
            }
        });
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.getPlanName().equals("Free") && data.getProfile().getBName() == null) {

                    final Dialog dialog = new Dialog(context);

                    dialog.setContentView(R.layout.custom_dialog_upload);
                    dialog.setCancelable(true);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    dialog.show();

                    final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                    Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
                    TextView text = (TextView) dialog.findViewById(R.id.txt_title);
                    text.setText("Update your business profile.");

                    btnBuy.setText("Ok");

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            transaction.replace(R.id.framelayout, new Business2Fragment2());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });

                } else {
                    if (data.getPlanName().equals("Free") && data.getProfile().getAddress() == null) {
                        final Dialog dialog = new Dialog(context);

                        dialog.setContentView(R.layout.custom_dialog_upload);
                        dialog.setCancelable(false);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        dialog.show();

                        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                        Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
                        TextView text = (TextView) dialog.findViewById(R.id.txt_title);
                        text.setText("Update your profile.");

                        btnBuy.setText("Ok");

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        btnBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                transaction.replace(R.id.framelayout, new MyAccountFragment());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                    } else {
                        if (propertyNumber==0)
                        {
                            if (controller!=null)
                                controller.setGetPropertyNumber(userId);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    // Actions to do after 10 seconds
                                }
                            }, 10000);
                        }
                        if (propertyNumber == 0) {
                            Dialog dialog = UtileDilog.dialog(context, context.getResources().getString(R.string.doyou), "Buy", manager);
                            dialog.show();
                        } else {
                            HomeActivity.bottomNavigationView.setSelectedItemId(R.id.navigationUpload);
                        }
                    }
                }
            }
        });
        myShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.framelayout, new MyShareFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.navigationView);
            }
        });

        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.bottomNavigationView.setSelectedItemId(R.id.navigationMyAccount);
            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.framelayout, new ReferFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void AddIntoSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("English");
        arrayList.add("Deutsch (in Kürze)");
        arrayList.add("Français (Bientôt disponible)");
        arrayList.add("Italiano (Prossimamente)");
        arrayList.add("Español (próximamente)");
        arrayList.add("हिंदी (जल्द ही आ रही है))");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
//        if (CheckInternet.isInternetAvailable(context)) {
//            getLoaction();
//            getEarning();
//            controller.setGetPropertyNumber(userId);
//        } else {
//            startActivity(new Intent(context, NoInternetActivity.class));
//        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                editSearch.setText(place.getName());

                getMakrer(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Helloamit", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Gmap = googleMap;
        Gmap.getUiSettings().setMyLocationButtonEnabled(true);
        Gmap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            return;
        }
        Gmap.setMyLocationEnabled(true);

    }


    //get all data in mapView
    private void getMapData() {
        final ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        //lat="30.7046";
        //lng="76.7179";
        Call<MapApi> call = apiInterface.getPropertyMap(categoryid, lat, lng);
        call.enqueue(new Callback<MapApi>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<MapApi> call, final Response<MapApi> response) {
                if (response.isSuccessful()) {
                    clear();
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        arrayLat.add(Double.valueOf(response.body().getData().get(i).getLatitude()));
                        arrayLong.add(Double.valueOf(response.body().getData().get(i).getLongitude()));
//                        if (response.body().getData().get(i).getMandate() != null) {
//
//                            ArrayList<String> arrayList = new ArrayList<String>();
//                            arrayList.add(response.body().getData().get(i).getMandate().getCalculatePrice());
//
//                            arrayCost.add("CHF " + response.body().getData().get(i).getMandate().getCalculatePrice());
////                            for (int j = 0; j < arrayList.size(); j++) {
//////                                double price = Double.parseDouble(arrayList.get(j));
//////                                SharedRate sharedRate = new SharedRate(context);
//////                                price = price * Double.parseDouble(sharedRate.getShared());
//////                                arrayCost.add(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
////                            }
//                        } else {
//                            arrayCost.add("CHF 0");
//
//                        }

                        // --- code alteration -- //
                        if (response.body().getData().get(i).getMandateCount() != null) {

                            ArrayList<String> arrayList = new ArrayList<String>();
                            arrayList.add(response.body().getData().get(i).getMandateCount());

                            arrayCost.add("CHF " + response.body().getData().get(i).getMandateCount());
//                            for (int j = 0; j < arrayList.size(); j++) {
////                                double price = Double.parseDouble(arrayList.get(j));
////                                SharedRate sharedRate = new SharedRate(context);
////                                price = price * Double.parseDouble(sharedRate.getShared());
////                                arrayCost.add(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
//                            }
                        } else {
                            arrayCost.add("CHF 0");

                        }

                        final LatLng latLng = new LatLng(Double.parseDouble(response.body().getData().get(i).getLatitude()), Double.parseDouble(response.body().getData().get(i).getLongitude()));
//                        generator = new IconGenerator(context);
//                        generator.setStyle(IconGenerator.STYLE_WHITE);
//                        View clusterView = LayoutInflater.from(context).inflate(R.layout.icongeneratorimage, null);
//                        generator.setContentView(clusterView);

                        String colorText = "<font color=\"#EE373E\"><b>"
                                + arrayCost.get(i)
                                + "</b></font>";
                        if (sharedToken.getCatId().equals("1"))
                        {
                            colorText = "<font color=\"#EE373E\"><b>"
                                    + arrayCost.get(i)
                                    + "</b></font>";
                        }else if (sharedToken.getCatId().equals("2"))
                        {
                            colorText = "<font color=\"#50316C\"><b>"
                                    + arrayCost.get(i)
                                    + "</b></font>";
                        }else if (sharedToken.getCatId().equals("3"))
                        {
                            colorText = "<font color=\"#383840\"><b>"
                                    + arrayCost.get(i)
                                    + "</b></font>";
                        }else if (sharedToken.getCatId().equals("4"))
                        {
                            colorText = "<font color=\"#5FB9D5\"><b>"
                                    + arrayCost.get(i)
                                    + "</b></font>";
                        }else if (sharedToken.getCatId().equals("5"))
                        {
                            colorText = "<font color=\"#C78D5D\"><b>"
                                    + arrayCost.get(i)
                                    + "</b></font>";
                        }else if (sharedToken.getCatId().equals("6"))
                        {
                            colorText = "<font color=\"#7ED492\"><b>"
                                    + arrayCost.get(i)
                                    + "</b></font>";
                        }else if (sharedToken.getCatId().equals("7"))
                        {
                            colorText = "<font color=\"#B85187\"><b>"
                                    + arrayCost.get(i)
                                    + "</b></font>";
                        }
                        //bitmap = generator.makeIcon(Html.fromHtml(colorText));
//                        "https://app.m8s.world/public/upload/users/logos/" + response.body().getData().get(finalJ).getLogo()
                        if (response.body().getData().get(i).getLogo()!=null) {

                            final int finalI = i;
                            final String finalColorText = colorText;
                            Glide.with(context.getApplicationContext())
                                    .asBitmap()
                                    .load("https://app.m8s.world/public/upload/users/logos/" + response.body().getData().get(i).getLogo())
                                    .dontTransform()
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            iconimage.setVisibility(View.VISIBLE);
                                            iconimage.setImageBitmap(resource);
                                            icontext.setText(Html.fromHtml(finalColorText));
                                            bitmap = generator.makeIcon();

//                                            final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
//                                            int pixels = (int) (40 * scale + 0.5f);
//                                            Bitmap bitmap1 = Bitmap.createScaledBitmap(resource, pixels, pixels-25, true);
//                                            bitmap = mergeBitmap(bitmap1,generator.makeIcon(Html.fromHtml(finalColorText)));
                                            marker = Gmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).
                                                    position(latLng).
                                                    anchor(generator.getAnchorU(), generator.getAnchorV()));
                                            marker.setTag(response.body().getData().get(finalI).getUserId()+"#"+response.body().getData().get(finalI).getPostcode());
                                        }


                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            icontext.setText(Html.fromHtml(finalColorText));
                                            marker = Gmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generator.makeIcon(Html.fromHtml(finalColorText)))).
                                                    position(latLng).
                                                    anchor(generator.getAnchorU(), generator.getAnchorV()));
                                            marker.setTag(response.body().getData().get(finalI).getUserId()+"#"+response.body().getData().get(finalI).getPostcode());
                                        }
                                    });
                        }
                        else
                        {
                            iconimage.setVisibility(View.GONE);
                            icontext.setText(Html.fromHtml(colorText));
                            bitmap = generator.makeIcon();
                            marker = Gmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).
                                    position(latLng).
                                    anchor(generator.getAnchorU(), generator.getAnchorV()));
                            marker.setTag(response.body().getData().get(i).getUserId()+"#"+response.body().getData().get(i).getPostcode());
                        }


                        Gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

//                                String id = String.valueOf(marker.getTag());
//                                getProperty(id);

                                MyPropertyFragment fragmentB = new MyPropertyFragment ();
                                Bundle args = new Bundle();
                                args.putString(MyPropertyFragment.DATA_RECEIVE, marker.getTag().toString());
                                fragmentB .setArguments(args);

                                transaction.replace(R.id.framelayout, fragmentB);
                                transaction.addToBackStack(null);
                                transaction.commit();

                                return true;
                            }
                        });


                    }

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MapApi> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public Bitmap mergeBitmap(Bitmap fr, Bitmap sc)
    {

        Bitmap comboBitmap;

        int width, height;

        width = fr.getWidth() + sc.getWidth();
        height = sc.getHeight();

        comboBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(comboBitmap);
        //comboImage.drawARGB(0,0,0,0); //This represents White color

        comboImage.drawBitmap(fr, 0f, 5f, null);
        comboImage.drawBitmap(sc, fr.getWidth()-10, 0f , null);
        return comboBitmap;

    }

    private void getProperty(String id) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ItemId", id);
        detailsFragment.setArguments(bundle);
        transaction.replace(R.id.framelayout, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void clear() {
        arrayLat.clear();
        arrayCost.clear();
        arrayLong.clear();
        arrayLogo.clear();
    }


    void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (Gmap!=null) {
            Gmap.clear();
            if (location != null) {
                lat = String.valueOf(location.getLatitude());
                lng = String.valueOf(location.getLongitude());

                //draw the radius in map view
                Gmap.addCircle(new CircleOptions()
                        .center(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).radius(25000)
                        .strokeWidth(0f)
                        .fillColor(context.getResources().getColor(R.color.lightred)));


                CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).zoom(9.3f).bearing(0).tilt(40).build();
                Gmap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                getMapData();

                try {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address address = addresses.get(0);
                    countryCode = address.getCountryCode();


                } catch (Exception e) {

                }
            } else {
                Toast.makeText(context, "Please enable gps", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    //search location
    private void getMakrer(Place place) {

        Gmap.clear();

        if (Geocoder.isPresent()) {
            try {
                String location = place.getName();
                Geocoder gc = new Geocoder(context);
                List<Address> addresses = gc.getFromLocationName(location, 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                        lat = String.valueOf(a.getLatitude());
                        lng = String.valueOf(a.getLongitude());

                        Gmap.addCircle(new CircleOptions()
                                .center(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).radius(25000)
                                .strokeWidth(0f)
                                .fillColor(context.getResources().getColor(R.color.lightred)));

                        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).zoom(9.3f).bearing(0).tilt(40).build();
                        Gmap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        getMapData();


                    }
                }
            } catch (IOException e) {
                // handle the exception
            }
        }
    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), 12);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

    private void getLoaction() {
        getLocation();

        enableLoc();

    }


    //get the earing price
    private void getEarning() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetPotentialEarn> call = apiInterface.getPotentialEarn(userId, categoryid);

        call.enqueue(new Callback<GetPotentialEarn>() {
            @Override
            public void onResponse(Call<GetPotentialEarn> call, Response<GetPotentialEarn> response) {
                if (response.isSuccessful()) {
//                        Log.d("++++++++++++++", response.body().getData().getTotal().toString());
//                        String ad = response.body().getData().getTotal().replaceAll(",", "");
//                        double price = Double.parseDouble(ad);
//                        SharedRate sharedRate = new SharedRate(context);
//                        price = price * Double.parseDouble(sharedRate.getShared());
//                        earning = sharedRate.getCurrencyCode() + " " + String.format("%.2f", price);
                    earning=response.body().getData().getCurrency() + " " + response.body().getData().getTotal();
                    txtEarn.setText(response.body().getData().getCurrency() + " " + response.body().getData().getTotal());

                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetPotentialEarn> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onSucessnumber(Response<GetShareNumberApi> response) {
        if (response.body().getStatus() == 200) {
            propertyNumber = response.body().getData();
        } else {
            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
    }


    // --- Puneet work --//
    //Get the user details
    public void GetMetaDetails() {
        if (CheckInternet.isInternetAvailable(context)) {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<GetMetaData> call = apiInterface.getMetaData(userId);
            final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
            dialog.show();

            call.enqueue(new Callback<GetMetaData>() {
                @Override
                public void onResponse(Call<GetMetaData> call, Response<GetMetaData> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        data = response.body().getData();
                            if (data.getProfile().getAddress()==null || data.getProfile().getAddress().equals("")) {
                                Toast.makeText(context, "Update your profile.", Toast.LENGTH_SHORT).show();
                                transaction.replace(R.id.framelayout, new MyAccountFragment());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }

//                    txtAgent.setText("My Profile");
//
//                    txtName.setText(response.body().getData().getName());
//                    txtPhone.setText(response.body().getData().getContactNo());
//                    txtEmail.setText(response.body().getData().getEmail());
//
//                    if (response.body().getData().getProfile() != null) {
//                        txtAddress.setText(response.body().getData().getProfile().getAddress());
//                    }
//                    if (data.getProfile().getBName() != null) {
//                        //linearbusiness.setVisibility(View.VISIBLE);
//                        linearbusiness.setVisibility(View.GONE);
//                        txtBName.setText(data.getProfile().getBName().toString());
//                        //txtBAdress.setText(data.getProfile().getBAddress().toString());
//                        txtBPhone.setText(data.getProfile().getBPhone().toString());
//                        //txtBEmail.setText(data.getProfile().getBEmail().toString());
//                    }
//                    if (response.body().getData().getProfileImage() == null) {
//                        profileImage.setImageResource(R.drawable.ic_person);
//                    } else {
//                        Glide.with(context).load("https://app.m8s.world/public/upload/users/profiles/" + response.body().getData().getProfileImage()).dontAnimate().placeholder(R.drawable.myaccount).into(profileImage);
//                    }


                    } else {
                        Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetMetaData> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
                }


            });

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity1 = activity;
    }
}
