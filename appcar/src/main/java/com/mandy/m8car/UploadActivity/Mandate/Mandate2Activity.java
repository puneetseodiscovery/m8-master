package com.mandy.m8car.UploadActivity.Mandate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.R;
import com.mandy.m8car.util.ProgressBarClass;

import com.mandy.m8car.RetrofitModel.AddMandataApi;
import com.mandy.m8car.RetrofitModel.UploadBulkMandateApi;
import com.mandy.m8car.ServiceGenerator;
import com.mandy.m8car.TouchyWebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mandate2Activity extends AppCompatActivity {
    public static final int RESULT_CODE = 2;
    Toolbar toolbar;
    ImageView drawer;
    String pdf;
    TextView textView, txtDate;
    Button button;
    TouchyWebView wv;
    ProgressDialog pd;

    SignaturePad signaturePad;
    public static MultipartBody.Part part;
    Bitmap bitmap;
    Button btnClear, btnOk;
    ImageView imgSet;
    String date, getItemId;
    String url;
    DownloadZipFileTask downloadZipFileTask;
    String fileName;
    FragmentManager manager;
    String type, id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandate3);

        init();

        manager = getSupportFragmentManager();

        pdf = getIntent().getStringExtra("responce");
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("PropertyId");


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer.setVisibility(View.GONE);
        textView.setText("Sign your mandate");

        wv.getSettings().setJavaScriptEnabled(true);

        pd = new ProgressDialog(Mandate2Activity.this);
        pd.setMessage("Please wait Loading...");
        pd.show();

        wv.setWebViewClient(new MyWebViewClient());
        wv.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        wv.getSettings().setBuiltInZoomControls(true);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("1")) {
                    SendData2();
                }
                if (type.equals("0")) {
                    sendMandate2();
                }
            }
        });

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        txtDate.setText("Date \n" + date);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = signaturePad.getSignatureBitmap();
                imgSet.setImageBitmap(bitmap);
                imgSet.requestFocus();
                try {
                    part = sendImageFileToserver(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });


    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        button = (Button) findViewById(R.id.btnOk);
        wv = (TouchyWebView) findViewById(R.id.webview);


        imgSet = (ImageView) findViewById(R.id.image_set);

        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        btnClear = (Button) findViewById(R.id.clear_sign);
        btnOk = (Button) findViewById(R.id.mandata_ok);
        txtDate = (TextView) findViewById(R.id.mandata_date);

    }


    //send data for mandate
    public void SendData2() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);

        Call<AddMandataApi> call = apiInterface.mandataApi2(date + " 00:00:00", HomeActivity.userId, "2", id, part);

        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(Mandate2Activity.this, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<AddMandataApi>() {
            @Override
            public void onResponse(Call<AddMandataApi> call, Response<AddMandataApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(Mandate2Activity.this, "Sucessful " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        getItemId = response.body().getData().getItemId();
                        Log.d("itemidHere", getItemId);

                        url = "http://m8.amrdev.site/public/upload/items/mandate/" + response.body().getData().getMandatePdf();

                        BottomDIlog();

                    } else {
                        Toast.makeText(Mandate2Activity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Mandate2Activity.this, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddMandataApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Mandate2Activity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //for bulk uploading
    private void sendMandate2() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<UploadBulkMandateApi> call = apiInterface.bulkMandate2(id, HomeActivity.userId, "2", date + " 00:00:00", part);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(Mandate2Activity.this, "Please wait...");
        dialog.show();
        call.enqueue(new Callback<UploadBulkMandateApi>() {
            @Override
            public void onResponse(Call<UploadBulkMandateApi> call, Response<UploadBulkMandateApi> response) {
                dialog.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Intent intent = new Intent(Mandate2Activity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Mandate2Activity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Mandate2Activity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadBulkMandateApi> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }

    public MultipartBody.Part sendImageFileToserver(Bitmap bitMap) throws IOException {
        File filesDir = Mandate2Activity.this.getFilesDir();
        File file = new File(filesDir, "sign_image" + ".png");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("sign_image", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "sign_image");

        return body;
    }

    private void BottomDIlog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(Mandate2Activity.this);
        dialog.setContentView(R.layout.bottomdilog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button delete, cancel;
        dialog.show();

        delete = dialog.findViewById(R.id.dilog_delete);
        cancel = dialog.findViewById(R.id.dilog_cancel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPdf();
                dialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                passIntent();
            }
        });
    }

    private void passIntent() {
        Intent intent = new Intent(Mandate2Activity.this, HomeActivity.class);
        intent.putExtra("ItemId", getItemId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }


    private void getPdf() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.downloadPdf(url);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(Mandate2Activity.this, "Please wait...");
        dialog.dismiss();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                fileName = url.substring(url.lastIndexOf('/') + 1);
                downloadZipFileTask = new DownloadZipFileTask();
                downloadZipFileTask.execute(response.body());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


    //download from server and save into storage
    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], "" + fileName);
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.d("API123", progress[0].second + " ");

            if (progress[0].first == 100)
                Toast.makeText(getApplicationContext(), "File downloaded successfully", Toast.LENGTH_LONG).show();


            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);
            }

            if (progress[0].first == -1) {
                Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_LONG).show();
            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {
            passIntent();
        }
    }

    private void saveToDisk(ResponseBody body, String filename) {
        try {

            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d("hellofile", "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                    Log.d("hellofile", "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d("hellofile", destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d("hellofile", "Failed to save the file!");
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("hellofile", "Failed to save the file!");
            return;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
