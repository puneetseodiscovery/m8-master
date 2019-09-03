package com.mandy.m8.Fragments;


import android.Manifest;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8.Activities.HomeActivity;
import com.mandy.m8.Activities.NoInternetActivity;
import com.mandy.m8.util.CheckInternet;
import com.mandy.m8.R;
import com.mandy.m8.ApiInterface;
import com.mandy.m8.AppMapView;
import com.mandy.m8.RetrofitModel.MapApi;
import com.mandy.m8.ServiceGenerator;


import com.mandy.m8.util.SharedRate;
import com.mandy.m8.util.UtileDilog;
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
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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
public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener {


    RoundedImageView myPotentail, Commission, Upload, myShare, ViewAll, myAccount;
    Spinner spinner;
    TextView textView;
    View view;

    FragmentManager manager;
    FragmentTransaction transaction;
    ImageView imageView;
    Toolbar toolbar;
    TextView editSearch;
    AppMapView mapView;
    GoogleMap Gmap;
    NestedScrollView nestedScrollView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Double> arrayLat = new ArrayList<>();
    ArrayList<Double> arrayLong = new ArrayList<>();
    ArrayList<String> arrayCost = new ArrayList<>();
    Marker marker;

    private GoogleApiClient googleApiClient;
    LocationManager locationManager;
    Context context;
    IconGenerator generator;

    TextView txtEarn;

    String countryCode, countryCurrency;


    String lat = "", lng = "";


    int AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //find All id
        init();

        sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
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

                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(context);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
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
        } else {
            startActivity(new Intent(context, NoInternetActivity.class));
        }


        return view;
    }

    private void init() {
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
        editSearch = (TextView) view.findViewById(R.id.edtSearch);

        txtEarn = (TextView) view.findViewById(R.id.txtEaring);

        txtEarn.setText(HomeActivity.earning);

        HomeActivity.cunter = 0;


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
                if (HomeActivity.propertyNumber == 0) {
                    Dialog dialog = UtileDilog.dialog(context, context.getResources().getString(R.string.doyou), "Buy", manager);
                    dialog.show();
                } else {
                    HomeActivity.bottomNavigationView.setSelectedItemId(R.id.navigationUpload);
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
        Call<MapApi> call = apiInterface.getPropertyMap(HomeActivity.categoryId, lat, lng);
        call.enqueue(new Callback<MapApi>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<MapApi> call, Response<MapApi> response) {
                if (response.isSuccessful()) {
                    clear();
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        arrayLat.add(Double.valueOf(response.body().getData().get(i).getLatitude()));
                        arrayLong.add(Double.valueOf(response.body().getData().get(i).getLongitude()));
                        if (response.body().getData().get(i).getMandate() != null) {

                            ArrayList<String> arrayList = new ArrayList<String>();
                            arrayList.add(response.body().getData().get(i).getMandate().getCalculatePrice());

                            for (int j = 0; j < arrayList.size(); j++) {
                                double price = Double.parseDouble(arrayList.get(j));
                                SharedRate sharedRate = new SharedRate(context);
                                price = price * Double.parseDouble(sharedRate.getShared());
                                arrayCost.add(sharedRate.getCurrencyCode() + " " + String.format("%.2f", price));
                            }
                        } else {
                            arrayCost.add("CHF 0");

                        }

                        LatLng latLng = new LatLng(Double.parseDouble(response.body().getData().get(i).getLatitude()), Double.parseDouble(response.body().getData().get(i).getLongitude()));
                        generator = new IconGenerator(context);
                        generator.setStyle(IconGenerator.STYLE_WHITE);
                        String colorText = "<font color=\"#EE373E\"><b>"
                                + arrayCost.get(i)
                                + "</b></font>";
                        marker = Gmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generator.makeIcon(Html.fromHtml(colorText)))).
                                position(latLng).
                                anchor(generator.getAnchorU(), generator.getAnchorV()));
                        marker.setTag(response.body().getData().get(i).getId());

                        Gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                String id = String.valueOf(marker.getTag());
                                getProperty(id);

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


}
