package com.m8.m8.Fragments.MyAccountSubFragment.BusinessFragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.hbb20.CountryCodePicker;
import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.ApiInterface;
import com.m8.m8.Fragments.MyAccountSubFragment.ViewProfileFragment;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.BulkUploadApi;
import com.m8.m8.RetrofitModel.GetMetaData;
import com.m8.m8.RetrofitModel.ProfileDataApi;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.UploadActivity.Mandate.AgentActivity;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.util.SharedToken;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Business2Fragment2 extends Fragment {

    final int RESULT_LOAD_FILE = 11;
    final int RESULT_LOAD_IMAGE = 121;
    final int REQUEST_CAMERA = 122;
    TextView txtSelectImage;
    RoundedImageView roundedImageView;
    Bitmap bitmap;

    Toolbar toolbar;
    TextView textView;
    TextView countryText;
    ImageView drawer;
    Button btnNext;
    View view;
    Context context;
    CountryCodePicker ccp;
    String Lcountry;
    String bCountry, bName, bContact, bEmail, bPhone, bAddress1, bAddress2, bCity, bPostcode, bType, Lname, Lemail,
            Lphone, Laddress1, Laddress2, Lcity, Lpostcode;
    EditText edtLname, edtLemail, edtLphone, edtLaddress1, edtLaddress2, edtLcity, edtLpostcode,legal_cname;

    String name, email, mobile, address;
    SharedPreferences sharedPreferences;
    String userId;
    MultipartBody.Part part = null;
    MultipartBody.Part filepart = null;
    String[] dialogOptions = new String[]{"Camera", "Gallery", "Cancel"};
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 555;
    String fileType = "";

    public Business2Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business22, container, false);

        init();

        //toolbar
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

        textView.setText("My Business Profile");

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryText.setText(ccp.getSelectedCountryName());
            }
        });


        //get the Countrydata
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtLname.getText().toString())) {
                    edtLname.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLemail.getText().toString())) {
                    edtLemail.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLphone.getText().toString())) {
                    edtLphone.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLaddress1.getText().toString())) {
                    edtLaddress1.setError("Invalid data");
                }  else if (TextUtils.isEmpty(edtLcity.getText().toString())) {
                    edtLcity.setError("Invalid data");
                } else if (TextUtils.isEmpty(edtLpostcode.getText().toString())) {
                    edtLpostcode.setError("Invalid data");
                }else if (!isValidEmail(edtLemail.getText().toString())){
                    edtLemail.setError("Invalid data");
                } else {
                    //Lcountry = ccp.getSelectedCountryName();
                    Lcountry = countryText.getText().toString();
                    setData();

                }

//                else if (TextUtils.isEmpty(edtLaddress2.getText().toString())) {
//                    edtLaddress2.setError("Invalid data");
//                }
            }
        });

        //set the text into edit text
        seEdittData();

        return view;
    }

    private void init() {
        txtSelectImage = (TextView) view.findViewById(R.id.txt_upload_image);
        roundedImageView = (RoundedImageView) view.findViewById(R.id.round_upload_logo);
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        countryText = (TextView) view.findViewById(R.id.countryText);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        btnNext = (Button) view.findViewById(R.id.button_next);
        edtLname = (EditText) view.findViewById(R.id.legal_name);
        legal_cname = (EditText) view.findViewById(R.id.legal_cname);
        edtLemail = (EditText) view.findViewById(R.id.legal_email);
        edtLphone = (EditText) view.findViewById(R.id.legal_phone);
        edtLaddress1 = (EditText) view.findViewById(R.id.legal_address1);
        edtLaddress2 = (EditText) view.findViewById(R.id.legal_address2);
        edtLcity = (EditText) view.findViewById(R.id.legal_city);
        edtLpostcode = (EditText) view.findViewById(R.id.legal_postcode);
        ccp = (CountryCodePicker) view.findViewById(R.id.legal_country);


        sharedPreferences = getActivity().getSharedPreferences("Business", Context.MODE_PRIVATE);
        txtSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showDialg();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                        roundedImageView.setImageBitmap(bitmap);
                        part = sendImageFileToserver(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case REQUEST_CAMERA:
                if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                    roundedImageView.setImageBitmap(bitmap);
                    try {
                        part = sendImageFileToserver(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case RESULT_LOAD_FILE:
                if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_OK) {
                    Toast.makeText(context, "File Added", Toast.LENGTH_LONG).show();
                    //String path = data.getData().getPath();
                    String path = getPathFromURI(context, data.getData());
                    File file = new File(path);
//                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    filepart = MultipartBody.Part.createFormData("logo", file.getName(), requestFile);
                }
                break;
            default:
                break;
        }
    }

    //set data into shared memory
    private void setData() {
        Lname = sharedPreferences.getString("Lname", "");
        Lemail = sharedPreferences.getString("Lemail", "");
        Lphone = sharedPreferences.getString("Lphone", "");
        Laddress1 = sharedPreferences.getString("Laddress1", "");
        Laddress2 = sharedPreferences.getString("Laddress2", "");
        Lcity = sharedPreferences.getString("Lcity", "");
        Lpostcode = sharedPreferences.getString("Lpostcode", "");


//        bName = sharedPreferences.getString("bName", "");
//        bContact = sharedPreferences.getString("bContact", "");
//        bEmail = sharedPreferences.getString("bEmail", "");
//        bPhone = sharedPreferences.getString("bPhone", "");
//        bAddress1 = sharedPreferences.getString("bAddress1", "");
//        bAddress2 = sharedPreferences.getString("bAddress2", "");
//        bCity = sharedPreferences.getString("bCity", "");
//        bPostcode = sharedPreferences.getString("bPostcode", "");
//        bType = sharedPreferences.getString("bType", "");
//        bCountry = sharedPreferences.getString("bCountry", "");

        bName = edtLname.getText().toString();
        bContact = legal_cname.getText().toString();
        bEmail = edtLemail.getText().toString();
        bPhone = edtLphone.getText().toString();
        bAddress1 = edtLaddress1.getText().toString();
        bAddress2 = edtLaddress2.getText().toString();
        bCity = edtLcity.getText().toString();
        bPostcode = edtLpostcode.getText().toString();
        bType = "bussiness";
        bCountry = countryText.getText().toString();

        name = sharedPreferences.getString("name", "");
        mobile = sharedPreferences.getString("phone", "");
        address = sharedPreferences.getString("address", "");
        email = sharedPreferences.getString("email", "");


        //hit the api to create the profile
        getShareData();
    }


    //Add data into apis sharer user
    private void getShareData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
//        Call<ProfileDataApi> call = apiInterface.getShareProfileApiP(HomeActivity.userId, bType, bName, bContact, bEmail, bPhone, bAddress1,
//                bAddress2, bCity, bPostcode, bCountry, Lname, Lemail, Lphone, Laddress1, Laddress2, Lcity, Lpostcode, Lcountry, name, mobile, address, email,part);
        Call<ProfileDataApi> call = apiInterface.getShareProfileApiP(HomeActivity.userId,bType,"","","","","","","","","",
               "","","","","","","","",bName,bPhone,bAddress1,bAddress2,bCity,bPostcode,bContact,bCountry,bEmail,part
        );
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<ProfileDataApi>() {
            @Override
            public void onResponse(Call<ProfileDataApi> call, Response<ProfileDataApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.framelayout, new ViewProfileFragment());
                    //transaction.replace(R.id.framelayout, new HomeFragment());
                    transaction.commit();
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileDataApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void seEdittData() {
        if (ViewProfileFragment.data==null)
        {
            GetMetaDetails();
        }
        else {
            if (ViewProfileFragment.data.getProfile() != null) {
                if (ViewProfileFragment.data.getProfile().getLogo() != null)
                Glide.with(context).load("https://app.m8s.world/public/upload/users/logos/" + ViewProfileFragment.data.getProfile().getLogo()).dontAnimate().placeholder(R.drawable.myaccount).into(roundedImageView);
                if (ViewProfileFragment.data.getProfile().getBName() != null)
                    edtLname.setText(ViewProfileFragment.data.getProfile().getBName().toString());
                if (ViewProfileFragment.data.getProfile().getBContactName() != null)
                    legal_cname.setText(ViewProfileFragment.data.getProfile().getBContactName());
                if (ViewProfileFragment.data.getProfile().getBEmail() != null)
                    edtLemail.setText(ViewProfileFragment.data.getProfile().getBEmail().toString());
                if (ViewProfileFragment.data.getProfile().getBPhone() != null)
                    edtLphone.setText(ViewProfileFragment.data.getProfile().getBPhone().toString());
                if (ViewProfileFragment.data.getProfile().getBAddress() != null)
                    edtLaddress1.setText(ViewProfileFragment.data.getProfile().getBAddress().toString());
                if (ViewProfileFragment.data.getProfile().getBAddress2() != null) {
                    edtLaddress2.setText(ViewProfileFragment.data.getProfile().getBAddress2().toString());
                }
                if (ViewProfileFragment.data.getProfile().getBTown() != null)
                    edtLcity.setText(ViewProfileFragment.data.getProfile().getBTown());
                if (ViewProfileFragment.data.getProfile().getBPostalcode() != null)
                    edtLpostcode.setText(ViewProfileFragment.data.getProfile().getBPostalcode());
                if (ViewProfileFragment.data.getProfile().getBCountry() != null)
                    countryText.setText(ViewProfileFragment.data.getProfile().getBCountry().toString());
            }
        }
    }

    //Get the user details
    public void GetMetaDetails() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        SharedToken sharedToken = new SharedToken(context);
        String userId = sharedToken.getUserId();
        Call<GetMetaData> call = apiInterface.getMetaData(userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetMetaData>() {
            @Override
            public void onResponse(Call<GetMetaData> call, Response<GetMetaData> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    GetMetaData.Data data1 = response.body().getData();
                    ViewProfileFragment.data = data1;
                    if (ViewProfileFragment.data.getProfile() != null) {
                        if (ViewProfileFragment.data.getProfile().getLogo() != null)
                            Glide.with(context).load("https://app.m8s.world/public/upload/users/logos/" + ViewProfileFragment.data.getProfile().getLogo()).dontAnimate().placeholder(R.drawable.myaccount).into(roundedImageView);
                        if (ViewProfileFragment.data.getProfile().getBName() != null)
                            edtLname.setText(ViewProfileFragment.data.getProfile().getBName().toString());
                        if (ViewProfileFragment.data.getProfile().getBContactName() != null)
                            legal_cname.setText(ViewProfileFragment.data.getProfile().getBContactName());
                        if (ViewProfileFragment.data.getProfile().getBEmail() != null)
                            edtLemail.setText(ViewProfileFragment.data.getProfile().getBEmail().toString());
                        if (ViewProfileFragment.data.getProfile().getBPhone() != null)
                            edtLphone.setText(ViewProfileFragment.data.getProfile().getBPhone().toString());
                        if (ViewProfileFragment.data.getProfile().getBAddress() != null)
                            edtLaddress1.setText(ViewProfileFragment.data.getProfile().getBAddress().toString());
                        if (ViewProfileFragment.data.getProfile().getBAddress2() != null) {
                            edtLaddress2.setText(ViewProfileFragment.data.getProfile().getBAddress2().toString());
                        }
                        if (ViewProfileFragment.data.getProfile().getBTown() != null)
                            edtLcity.setText(ViewProfileFragment.data.getProfile().getBTown());
                        if (ViewProfileFragment.data.getProfile().getBPostalcode() != null)
                            edtLpostcode.setText(ViewProfileFragment.data.getProfile().getBPostalcode());
                        if (ViewProfileFragment.data.getProfile().getBCountry() != null)
                            countryText.setText(ViewProfileFragment.data.getProfile().getBCountry().toString());
                    }

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


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

    //select image from camera and gallery
    public void showDialg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Images");
        builder.setCancelable(false);
        //builder.setPositiveButton("OK", null);

        builder.setItems(dialogOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Camera".equals(dialogOptions[which])) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }


                } else if ("Gallery".equals(dialogOptions[which])) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                } else if ("Cancel".equals(dialogOptions[which])) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    //Add data into  apis business user
    private void getData() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<BulkUploadApi> call = apiInterface.bulkUpload(fileType, HomeActivity.userId, HomeActivity.categoryId, part, filepart);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<BulkUploadApi>() {
            @Override
            public void onResponse(Call<BulkUploadApi> call, Response<BulkUploadApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        Intent intent = new Intent(context, AgentActivity.class);
                        //intent.putExtra("type", bulk);
                        intent.putExtra("PropertyId", response.body().getData().getId().toString());
                        context.startActivity(intent);

                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<BulkUploadApi> call, Throwable t) {
                dialog.dismiss();
                Log.d("+++++++", "" + t.getMessage());
            }
        });


    }

    public MultipartBody.Part sendImageFileToserver(Bitmap bitMap) throws IOException {
        File filesDir = getActivity().getFilesDir();
        File file = new File(filesDir, "logo" + ".png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("logo", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "logo");
        return body;
    }


    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

//                    final String id = DocumentsContract.getDocumentId(uri);
//                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                    return getDataColumn(context, contentUri, null, null);
                    final String id = DocumentsContract.getDocumentId(uri);
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        try {
                            final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                            return getDataColumn(context, contentUri, null, null);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
