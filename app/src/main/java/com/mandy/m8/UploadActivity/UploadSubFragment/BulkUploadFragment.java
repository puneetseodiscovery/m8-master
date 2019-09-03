package com.mandy.m8.UploadActivity.UploadSubFragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8.Activities.HomeActivity;
import com.mandy.m8.util.ProgressBarClass;
import com.mandy.m8.RetrofitModel.GetMessage;
import com.mandy.m8.UploadActivity.Mandate.AgentActivity;
import com.mandy.m8.ApiInterface;
import com.mandy.m8.R;
import com.mandy.m8.RetrofitModel.BulkUploadApi;
import com.mandy.m8.ServiceGenerator;
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


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BulkUploadFragment extends Fragment {
    final int RESULT_LOAD_FILE = 11;
    final int RESULT_LOAD_IMAGE = 121;
    final int REQUEST_CAMERA = 122;
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 555;
    String[] dialogOptions = new String[]{"Camera", "Gallery", "Cancel"};

    TextView txtSelectImage;

    RoundedImageView roundedImageView;
    Button btnNext;
    ProgressBar progressBar;
    View view;

    Bitmap bitmap;
    Spinner spinner;
    Button btnChoose;
    String fileType = "";
    String bulk = "0";
    String[] type = {"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "text/*"};

    MultipartBody.Part part = null;
    MultipartBody.Part filepart = null;

    Context context;
    FragmentManager manager;

    public BulkUploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business3, container, false);

        init();

        txtSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialg();
            }
        });

        //radio button click
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.csv:
//                        fileType = "csv";
//                        break;
//                    case R.id.xlsx:
//                        fileType = "xlxs";
//                        break;
//
//                }
//            }
//        });


//        txtFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (fileType.equals("xlxs")) {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        intent.setType(type.length == 1 ? type[0] : "*/*");
//                        if (type.length > 0) {
//                            intent.putExtra(Intent.EXTRA_MIME_TYPES, type);
//                        }
//                    } else {
//                        String mimeTypesStr = "";
//                        for (String mimeType : type) {
//                            mimeTypesStr += mimeType + "|";
//                        }
//                        intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
//                    }
//                    startActivityForResult(Intent.createChooser(intent, "ChooseFile"), RESULT_LOAD_FILE);
//
//                } else if (fileType.equals("csv")) {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("text/*");
//                    startActivityForResult(Intent.createChooser(intent, "Open CSV"), RESULT_LOAD_FILE);
//
//                } else {
//                    Toast.makeText(context, "Selct the File type", Toast.LENGTH_LONG).show();
//                }
//            }
//        });


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setType(type.length == 1 ? type[0] : "*/*");
                    if (type.length > 0) {
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, type);
                    }
                } else {
                    String mimeTypesStr = "";
                    for (String mimeType : type) {
                        mimeTypesStr += mimeType + "|";
                    }
                    intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                }
                startActivityForResult(Intent.createChooser(intent, "ChooseFile"), RESULT_LOAD_FILE);
            }
        });

        setSipinner();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (part == null) {
                    Toast.makeText(context, "Select the Business logo", Toast.LENGTH_SHORT).show();
                } else if (filepart == null) {
                    Toast.makeText(context, "Choose the file formate", Toast.LENGTH_SHORT).show();
                } else if (fileType.equalsIgnoreCase("Choose upload file from here")) {
                    spinner.requestFocus();
                    Toast.makeText(context, "Choose the file formate", Toast.LENGTH_SHORT).show();
                } else {
                    getData();
                }
            }
        });

        return view;
    }

    private void setSipinner() {
        String min[] = {"Choose upload file from here", "csv", "xlsx", "REMAX"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, min);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fileType = parent.getSelectedItem().toString();
                if (!fileType.equalsIgnoreCase("Choose upload file from here")) {
                    getCsv();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //get the csv file
    private void getCsv() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetMessage> call = apiInterface.getCsv(HomeActivity.categoryId, HomeActivity.userId);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetMessage>() {
            @Override
            public void onResponse(Call<GetMessage> call, Response<GetMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMessage> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void init() {
        btnNext = (Button) view.findViewById(R.id.button_next);
        txtSelectImage = (TextView) view.findViewById(R.id.txt_upload_image);
        roundedImageView = (RoundedImageView) view.findViewById(R.id.round_upload_logo);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        spinner = (Spinner) view.findViewById(R.id.spinnerFile);
        btnChoose = (Button) view.findViewById(R.id.btnFileSelector);


        manager = getActivity().getSupportFragmentManager();

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
                    filepart = MultipartBody.Part.createFormData("port_file", file.getName(), requestFile);
                }
                break;
            default:
                break;
        }
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
                        intent.putExtra("type", bulk);
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


    //download from server and save into storage
//    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(ResponseBody... urls) {
//            //Copy you logic to calculate progress and call
//            saveToDisk(urls[0], "" + fileName);
//            return null;
//        }
//
//        protected void onProgressUpdate(Pair<Integer, Long>... progress) {
//
//            Log.d("API123", progress[0].second + " ");
//
//            if (progress[0].first == 100)
//                Toast.makeText(getApplicationContext(), "File downloaded successfully", Toast.LENGTH_LONG).show();
//
//
//            if (progress[0].second > 0) {
//                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
////                progressBar.setProgress(currentProgress);
//
////                txtProgressPercent.setText("Progress " + currentProgress + "%");
//
//            }
//
//            if (progress[0].first == -1) {
//                Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_LONG).show();
//            }
//
//        }
//
//        public void doProgress(Pair<Integer, Long> progressDetails) {
//            publishProgress(progressDetails);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//        }
//    }

//    private void saveToDisk(ResponseBody body, String filename) {
//        try {
//
//            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
//
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(destinationFile);
//                byte data[] = new byte[4096];
//                int count;
//                int progress = 0;
//                long fileSize = body.contentLength();
//                Log.d("hellofile", "File Size=" + fileSize);
//                while ((count = inputStream.read(data)) != -1) {
//                    outputStream.write(data, 0, count);
//                    progress += count;
//                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
//                    downloadZipFileTask.doProgress(pairs);
//                    Log.d("hellofile", "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
//                }
//
//                outputStream.flush();
//
//                Log.d("hellofile", destinationFile.getParent());
//                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
//                downloadZipFileTask.doProgress(pairs);
//                return;
//            } catch (IOException e) {
//                e.printStackTrace();
//                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
//                downloadZipFileTask.doProgress(pairs);
//                Log.d("hellofile", "Failed to save the file!");
//                return;
//            } finally {
//                if (inputStream != null) inputStream.close();
//                if (outputStream != null) outputStream.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("hellofile", "Failed to save the file!");
//            return;
//        }
//    }

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


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
    }

}
