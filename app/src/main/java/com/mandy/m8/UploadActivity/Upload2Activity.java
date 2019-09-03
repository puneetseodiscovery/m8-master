package com.mandy.m8.UploadActivity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mandy.m8.Adapter.RoomsAdapter;
import com.mandy.m8.UploadActivity.UploadSubFragment.UploadFragment;
import com.mandy.m8.R;
import com.thomashaertel.widget.MultiSpinner;

public class Upload2Activity extends AppCompatActivity {

    Button button;
    Toolbar toolbar;
    TextView textView;
    ImageView drawer;
    RecyclerView recyclerView;
    RoomsAdapter roomsAdapter;
    String rooms[] = new String[]{"1", "2", "3", "4", "5+"};
    EditText edtSuche;

    RadioGroup radioGroup;
    String room, min, particular, suche, floor, openhouse, added, realestate;
    Spinner edtMin;
    MultiSpinner multiParticular;
    ArrayAdapter<String> arrayAdapter;

    Spinner sReal, sAdded, sFloor;

    String[] minData;

    String realState[] = new String[]{
            "Real estate category", "Farm", "Holiday Rentals", "Recreation", "Communal property"
            , "Property", "Industry", "Capital investment", "Luxury property", "New"
            , "Seniors apartment", "Social housing", "Business sale", "RE/MAX Commercial", "The RE/MAX Collection"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload2);

        init();

        roomsAdapter = new RoomsAdapter(Upload2Activity.this, rooms);

        LinearLayoutManager layoutManager = new LinearLayoutManager(Upload2Activity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(roomsAdapter);


        setSupportActionBar(toolbar);
        // Set title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setTitle("");
        textView.setText("Upload a item");
        drawer.setVisibility(View.GONE);


        roomsAdapter.setOnItemClickListener(new RoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                room = String.valueOf(position);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.multi:
                        openhouse = "Multi media";
                        break;
                    case R.id.onlywithopen:
                        openhouse = "Open house";

                        break;

                }
            }
        });


        //min m2 Drop down
        minData = new String[]{"Meter squared", "10", "20", "30", "40", "50", "60", "70", "80", "90"
                , "100", "150", "200", "250", "300", "350", "400", "450", "500", "600", "700", "800", "900", "1000", "1250", "1500"
                , "1750", "2000", "3000", "4000", "5000"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Upload2Activity.this, R.layout.spinnertext, minData);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        edtMin.setAdapter(arrayAdapter);
        edtMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                min = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Real State drop down
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(Upload2Activity.this, R.layout.spinnertext, realState);
        arrayAdapter1.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sReal.setAdapter(arrayAdapter1);
        sReal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                realestate = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Added since drop down
        final String[] addedArray = new String[]{"Added in the last", "All", "Day", "Week", "Month"};
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(Upload2Activity.this, R.layout.spinnertext, addedArray);
        arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sAdded.setAdapter(arrayAdapter2);
        sAdded.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                added = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Floor drop down
        getFloor();

        // get particular data
        getParticular();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(particular)) {
                    multiParticular.requestFocus();
                    Toast.makeText(Upload2Activity.this, "Enter the Property Features", Toast.LENGTH_LONG).show();
                } else {
                    suche = edtSuche.getText().toString();
                    setData();


                    Intent intent = new Intent(Upload2Activity.this, UploadImagesActivity.class);
                    startActivity(intent);

                }
            }
        });


    }

    private void init() {
        button = (Button) findViewById(R.id.upload_image_btn);
        toolbar = (Toolbar) findViewById(R.id.tooolbar);
        textView = (TextView) findViewById(R.id.toolbarText);
        drawer = (ImageView) findViewById(R.id.tooolbarImage);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        multiParticular = (MultiSpinner) findViewById(R.id.particular);
        edtSuche = (EditText) findViewById(R.id.suche);
        sFloor = (Spinner) findViewById(R.id.floor);
        sAdded = (Spinner) findViewById(R.id.added);
        sReal = (Spinner) findViewById(R.id.realestate);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        edtMin = (Spinner) findViewById(R.id.min);

    }


    //set data into sharedprefrence
    public void setData() {
        UploadFragment.hashMap.put("property_rooms", room);
        UploadFragment.hashMap.put("property_minm", min);
        UploadFragment.hashMap.put("property_particularities", particular);
        UploadFragment.hashMap.put("property_mls_id", suche);
        UploadFragment.hashMap.put("property_floor", floor);
        UploadFragment.hashMap.put("property_available_type", openhouse);
        UploadFragment.hashMap.put("property_added_since", added);
        UploadFragment.hashMap.put("property_real_estate_category", realestate);


    }


    private void getFloor() {

        String[] flooor = {
                "Floor", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
                , "Duplex", "Screed", "Back Split", "Basement,celler", "Basement"
                , "Gallery", "Attlic", "Ground Floor", "Last Floor", "Mezzanine",
                "Penthouse", "Caretaker's house", "Top Floor", "Apartment over three level",
                "Entire building", "Ground Floor+"
        };

        //Floor  drop down
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(Upload2Activity.this, R.layout.spinnertext, flooor);
        arrayAdapter1.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sFloor.setAdapter(arrayAdapter1);

        sFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                floor = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    // get particular data
    private void getParticular() {
        arrayAdapter = new ArrayAdapter<String>(Upload2Activity.this, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.add("Directly on the lake");
        arrayAdapter.add("Rural region");
        arrayAdapter.add("Downtown");
        arrayAdapter.add("Balcony");
        arrayAdapter.add("Roof terrace");
        arrayAdapter.add("Garage");
        arrayAdapter.add("Garden");
        arrayAdapter.add("Parking spot");
        arrayAdapter.add("Shell");
        arrayAdapter.add("Terrace");
        arrayAdapter.add("Lift/elevator");
        arrayAdapter.add("Renovated");
        arrayAdapter.add("For refurbishment");
        arrayAdapter.add("Wheelchair accessible");

        multiParticular.setAdapter(arrayAdapter, false, onSelectedListener);


    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        @Override
        public void onItemsSelected(boolean[] selected) {
            particular = multiParticular.getText().toString();
        }
    };

}
