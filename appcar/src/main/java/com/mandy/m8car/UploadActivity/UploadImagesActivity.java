package com.mandy.m8car.UploadActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mandy.m8car.Activities.HomeActivity;
import com.mandy.m8car.R;
import com.mandy.m8car.UploadActivity.Mandate.AgentActivity;
import com.mandy.m8car.Adapter.UploadImageAdapter;
import com.mandy.m8car.ApiInterface;
import com.mandy.m8car.UploadActivity.UploadSubFragment.UploadFragment;
import com.mandy.m8car.util.ProgressBarClass;

import com.mandy.m8car.RetrofitModel.UploadPropertyApi;
import com.mandy.m8car.ServiceGenerator;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImagesActivity extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE = 121;
    Toolbar toolbar;
    TextView textView, uploadImage;
    ImageView drawer;
    UploadImageAdapter adapter;
    RecyclerView recyclerView;
    Button button;
    EditText edtDescripation;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //    MultipartBody.Part part;
    ArrayList<MultipartBody.Part> part = new ArrayList<>();
    ArrayList<String> image_uris = new ArrayList<>();


    String country, county, city, postcode, currency, price, address1, address2, descripation;
    String bulk = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);


        init();

        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);

        setSupportActionBar(toolbar);
        // Set title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        drawer.setVisibility(View.INVISIBLE);

        textView.setText("Upload 10 pictures");

        Fresco.initialize(UploadImagesActivity.this);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                select multiple image

                // start multiple photos selector
                Intent intent = new Intent(UploadImagesActivity.this, ImagesSelectorActivity.class);

                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 10);
                // intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, image_uris);
                // start the selector
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

            }
        });

        //get data from  shared prefrence
        sharedPreferences = getSharedPreferences("UserProperty", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        country = sharedPreferences.getString("country", "");
        county = sharedPreferences.getString("county", "");
        city = sharedPreferences.getString("city", "");
        postcode = sharedPreferences.getString("postcode", "");
        currency = sharedPreferences.getString("currency", "");
        price = sharedPreferences.getString("price", "");
        address1 = sharedPreferences.getString("address1", "");
        address2 = sharedPreferences.getString("address2", "");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //upload the data into apis
                if (TextUtils.isEmpty(edtDescripation.getText().toString())) {
                    edtDescripation.setError("Invalid filed");
                } else {
                    descripation = edtDescripation.getText().toString();
                    UploadData();
                }

            }
        });


    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        textView = (TextView) findViewById(R.id.toolbarText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        uploadImage = (TextView) findViewById(R.id.upload_logo);
        edtDescripation = (EditText) findViewById(R.id.txt_descripation);
        button = (Button) findViewById(R.id.View_share);

    }


    public void SetImage(ArrayList<String> image_uris) {
        adapter = new UploadImageAdapter(UploadImagesActivity.this, image_uris);
        recyclerView.setLayoutManager(new GridLayoutManager(UploadImagesActivity.this, 4));
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_IMAGE) {
            image_uris.clear();
            part.clear();

            if (resultCode == RESULT_OK) {
                image_uris = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert image_uris != null;

                SetImage(image_uris);
                for (int i = 0; i < image_uris.size(); i++) {
                    File file = new File(compressImage(image_uris.get(i)));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part filepart = MultipartBody.Part.createFormData("item_images[]", file.getName(), requestFile);
                    part.add(filepart);

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    private void UploadData() {
        JSONObject jsonObject = new JSONObject(UploadFragment.hashMap);

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<UploadPropertyApi> call = apiInterface.uploadProperty(HomeActivity.userId, HomeActivity.categoryId, descripation, Double.parseDouble(price), address1, address2, country, county, city, postcode, currency, jsonObject, part);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(UploadImagesActivity.this, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<UploadPropertyApi>() {
            @Override
            public void onResponse(Call<UploadPropertyApi> call, Response<UploadPropertyApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(200)) {
                        Toast.makeText(UploadImagesActivity.this, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UploadImagesActivity.this, AgentActivity.class);
                        intent.putExtra("type", bulk);
                        intent.putExtra("PropertyId", response.body().getData().getId().toString());
                        intent.putExtra("boolean", response.body().getData().getPackage());
                        Log.d("boolean1", response.body().getData().getPackage().toString());
                        startActivity(intent);


                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "" + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(UploadImagesActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadPropertyApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(UploadImagesActivity.this, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
