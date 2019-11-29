package com.m8.m8.UploadActivity.UploadSubFragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.m8.m8.ApiInterface;
import com.m8.m8.RetrofitModel.CurrecnyConvter;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.UploadActivity.Upload2Activity;
import com.m8.m8.R;
import com.hbb20.CountryCodePicker;
import com.m8.m8.UploadActivity.UploadImagesActivity;
import com.m8.m8.util.Config;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.util.SharedToken;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
public class UploadFragment extends Fragment {

    final int RESULT_LOAD_FILE = 11;
    final int RESULT_LOAD_IMAGE = 121;
    final int REQUEST_CAMERA = 122;

    public static int MY_PERMISSIONS_REQUEST_CAMERA = 555;
    public static MultipartBody.Part part = null;
    MultipartBody.Part filepart = null;
    String[] dialogOptions = new String[]{"Camera", "Gallery", "Cancel"};
    RoundedImageView roundedImageView;
    Bitmap bitmap;

    CountryCodePicker ccp;
    Button buttonNext;
    EditText edtCounty, edtCity, edtPostcode, edtPrice,relative1;
    Spinner spinner, sOther, sType;
    TextView edtCurrecny;
    EditText edtAddress1, edtAddress2;
    String otherType="";
    String currency, country, property="";
    String price;
    View view;
    String type="";
    Context context;
    RelativeLayout property_layout,relative;
    TextView txt_upload_image1;


    public static Map<String, String> hashMap = new HashMap<>();

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // view = inflater.inflate(R.layout.fragment_upload, container, false);
        SharedToken sharedToken = new SharedToken(context);
        sharedToken.getCatId();
        if (sharedToken.getCatId().equals("1"))
        {
            view = inflater.inflate(R.layout.fragment_upload, container, false);
            init();
            getType();
            //get the property list
            getPropertyCateogry();
        }else if (sharedToken.getCatId().equals("2"))
        {
            view = inflater.inflate(R.layout.fragment_uploadjobs, container, false);
            init();
            getJobType();
        }else if (sharedToken.getCatId().equals("3"))
        {
            view = inflater.inflate(R.layout.fragment_uploadcar, container, false);
            init();
            getCarType();
            getCarYear();

        }else if (sharedToken.getCatId().equals("4"))
        {
            view = inflater.inflate(R.layout.fragment_uploadboats, container, false);
            init();
            getBoatType();
            getYear();
        }else if (sharedToken.getCatId().equals("5"))
        {
            view = inflater.inflate(R.layout.fragment_uploadjewellery, container, false);
            init();
            getJewelleryType();
            getYear();
        }else if (sharedToken.getCatId().equals("6"))
        {
            view = inflater.inflate(R.layout.fragment_uploadinvestments, container, false);
            init();
            relative = view.findViewById(R.id.relative);
            relative1 = view.findViewById(R.id.relative1);
            txt_upload_image1 = view.findViewById(R.id.txt_upload_image1);
            getInvestmentYear();
            relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialg();
                }
            });
            edtPrice.setFocusable(false);

            edtPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> years1 = new ArrayList<String>();
                    int thisYear1 = 50000000;
                    for (int i = 500000; i <= thisYear1;) {
                        years1.add(i+"");
                        i += 500000;
                    }
                    final String[] items = years1.toArray(new String[years1.size()]);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Select price:");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection
                            edtPrice.setText(items[item]);
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }else if (sharedToken.getCatId().equals("7"))
        {
            view = inflater.inflate(R.layout.fragment_uploadmisc, container, false);
            init();
            getYear();
        }
        return view;
    }

    private void getCarYear() {
            ArrayList<String> years = new ArrayList<String>();
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            years.add("Select Year*");
            for (int i = 1900; i <= thisYear; i++) {
                years.add(Integer.toString(i));
            }
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, years);
            arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
            spinner.setAdapter(arrayAdapter2);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                property = parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void getYear() {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Select Year*");
        for (int i = 1800; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, years);
        arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(arrayAdapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                property = parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void getInvestmentYear() {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Select Year*");
        for (int i = thisYear; i <= thisYear+10; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, years);
        arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(arrayAdapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                property = parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> years1 = new ArrayList<String>();
        int thisYear1 = 100;
        years1.add("Number of investors*");
        for (int i = 1; i <= thisYear1+1; i++) {

            if (i>100)
            {
                years1.add("Unlimited");
            }
            else
            {
                years1.add(Integer.toString(i));
            }
        }
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, years1);
        arrayAdapter3.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sOther.setAdapter(arrayAdapter3);
        sOther.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("Number of investors*"))
                {
                    otherType = "";
                }
                else if (parent.getSelectedItem().toString().equals("Unlimited"))
                {
                    otherType = "Unlimited";
                }
                else
                {
                    otherType = parent.getSelectedItem().toString();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(view);
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

        //radio button click listner
        edtCurrecny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                final CurrencyPicker picker = CurrencyPicker.newInstance("Currency");
                Log.d("dadafadadaa", "" + picker);
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String s, String s1, String s2, int i) {
                        currency = s1;
                        edtCurrecny.setText(currency);
                        Log.d("dadafadada", "" + currency);
                        picker.dismiss();
                    }
                });
                picker.show(manager, "CURRENCY_PICKER");
            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data into shared prefrence
                SharedToken sharedToken = new SharedToken(context);
                sharedToken.getCatId();
                if (sharedToken.getCatId().equals("1")) {
                    if (TextUtils.isEmpty(edtCounty.getText().toString()) || edtCounty.getText().length() > 25) {
                        edtCounty.setError("Invalid input.");
                        edtCounty.requestFocus();
                    } else if (TextUtils.isEmpty(edtCity.getText().toString()) || edtCity.getText().length() > 25) {
                        edtCity.setError("Invalid input.");
                        edtCity.requestFocus();
                    } else if (TextUtils.isEmpty(edtPostcode.getText().toString()) || edtPostcode.getText().length() > 10) {
                        edtPostcode.setError("Invalid input.");
                        edtPostcode.requestFocus();
                    } else if (TextUtils.isEmpty(edtPrice.getText().toString()) || edtPrice.getText().length() > 18) {
                        edtPrice.setError("Invalid input.");
                        edtPrice.requestFocus();
                    } else if (TextUtils.isEmpty(currency)) {
                        Toast.makeText(getActivity(), "Select the Currency type", Toast.LENGTH_LONG).show();
                        edtCurrecny.requestFocus();
                    } else if (spinner.getSelectedItemPosition() == 0) {
                        Toast.makeText(context, "Select property type.", Toast.LENGTH_SHORT).show();
                    } else {
                        getCurrencyConvert();
                    }
                }
                else
                {
                    if (sharedToken.getCatId().equals("7")) {
                        type = edtCounty.getText().toString();
                        getCurrencyConvert();
                    }
                    else if (sharedToken.getCatId().equals("6")) {
                        if (relative1.getText().toString().length()>0)
                        {
                            if (Patterns.WEB_URL.matcher(relative1.getText().toString()).matches()) {
                                UploadFragment.hashMap.put("item_video", relative1.getText().toString());
                                type = edtCounty.getText().toString();
                                getCurrencyConvert();
                            }
                            else
                                relative1.setError("Invalid input");
                        }
                        else
                        {
                            type = edtCounty.getText().toString();
                            getCurrencyConvert();
                        }

                    }
                    else {
                        getCurrencyConvert();
                    }
                }

            }
        });

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
        final String stype[] = {"Select Property Type*","Residential", "Commercial"};
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, stype);
        arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        sType.setAdapter(arrayAdapter2);
        sType.setPrompt("Select Property Type*");
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

    private void getJobType() {
        final String stype[] = {"A","Accounting","General Business","Other","Admin & Clerical","General Labor","Pharmaceutical","Automotive","Government","Professional Services","Banking","Grocery","Purchasing ","Procurement","Biotech","Health Care","QA - Quality Control","Broadcaster ","Journalism","Hotel - Hospitality","Real Estate","Business Development ","Human Resources - Research","Construction","Information Technology","Restaurant - Food Service","Consultant","Installation ","Maintenance Repair","Retail","Customer Service","Insurance","Sales","Design","Inventory","Science","Distribution ","Shipping","Legal","Skilled Labor ","Trades","Education ","Teaching","Legal Admin","Strategy - Planning","Engineering","Management","Supply Chain","Entry Level - New Grad","Manufacturing","Telecommunications","Executive","Marketing","Training","Facilities","Media ","Journalism ","Newspaper","Transportation","Finance","Nonprofit ","Social Services","Warehouse","Franchise","Nurse"};
        Arrays.sort(stype);
        stype[0]="Select Job Type*";
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

    private void getCarType() {
        final String stype[] = {"A","Alfa Romeo","Aston Martin","Audi","Bentley","Benz","BMW","Bugatti","Cadillac","Chevrolet","Chrysler","Citroen","Corvette","DAF","Dacia","Daewoo","Daihatsu","Datsun","De Lorean","Dino","Dodge","Farboud","Ferrari","Fiat","Ford","Honda","Hummer","Hyundai","Jaguar","Jeep","KIA","Koenigsegg","Lada","Lamborghini","Lancia","Land Rover","Lexus","Ligier","Lincoln","Lotus","Martini","Maserati","Maybach","Mazda","McLaren","Mercedes","Mercedes-Benz","Mini","Mitsubishi","Nissan","Noble","Opel","Peugeot","Pontiac","Porsche","Renault","Rolls-Royce","Rover","Saab","Seat","Skoda","Smart","Spyker","Subaru","Suzuki","Toyota","Vauxhall","Volkswagen","Volvo"};
        Arrays.sort(stype);
        stype[0]="Select Car Make*";
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
    private void getBoatType() {
        final String stype[] = {"A","AB","AB Inflatables","ACM","ATX Surf Boats","Abacus","Abati Yachts","Abeking & Rasmussen","Absolute","Achilles","Action Craft","Admiral","Advantage","Aegean","Agilis","Aicon","Airon","Airship","Ala Blu","Alajuela","Alalunga","Alarnia","Albatro","Albatross","Albemarle","Alberg","Albin","Alden","Alen","Alerion","Alfamarine","Alfastreet Marine","Alliage","Allied","Alm","Aloha","Aloha Pontoon","Alpa","Altair","Altamarea","Altena","Alu Marine","Alubat","Alumacraft","Alumarine","Alumaweld","Alweld","Amel","Amer","American Marine","American Tug","Angler","Angler Qwest","Antago","Antares","Antaris","Apache","Apex Marine","Aphrodite","Apreamare","Aqua Patio","Aquador","Aqualine","Aquanaut","Aquasport","Aquastar","Aquila","Arcadia","Archambault","Arcoa","Arcona","Argos Nautic","Arima","Arno","Arno Leopard","Arvor","Ascend","Astinor","Astromar","Atlantic","Atlantis","Austin Parker","Avalon","Avenger","Aviara","Avila","Avon","Axis","Axopar","Azimut","B-Yachts","BSC","BWA","Baba","Back Cove","Baglietto","Baha Cruisers","Bahama","Baia","Baja","Balance","Bali","Ballistic","Baltic","Bani","Barbican","Barkas","Barletta","Barracuda","Bass Cat","Baumarine","Bavaria","Bay Craft","Bay Stealth","Bayfield","Bayliner","Beavertail","Bekebrede","Belliure","Beneteau","Beneteau America","Benetti","Benetti Sail Division","Bennington","Bentley Pontoons","Berkshire","Bertram","Bianca","Bilgin","Bimax","Bimini","Birchwood","Black Pepper","Black Thunder","Black Watch","BlackJack","Blackfin","Blackwell","Blackwood","Blazer","Blu Martin","Blue Jacket","Blue Water","Blue Wave","Bluegame","Bluewater","Bluewater Sportfishing","Bluewater Yachts","Bodrum","Boesch","Bombardier","Bossman","Boston Whaler","Botnia","Bowman","Brabus","Brewer","Brig Inflatables","Bristol","Broadblue","Broom","Broward","Bruce Roberts","Bruckmann","Bruno Abbate","Bryant","Buddy Davis","Bugari","Bullet","Bulls Bay","Burger","Buster","C&C","C-Dory","C-Hawk","CAL","CHB","CL Yachts","CNB","CRN","CS","CSY","CT","Cabo","Cabo Rico","Cajun","Calabria","Calafuria","Caliber","Californian","Camano","Campbell","Camper & Nicholsons","Campion","Camuffo","Canadian Electric Boats","Canadian Sailcraft","Canados","Canoe Cove","Cantiere Mimi","Cantieri Estensi","Cantieri di Pisa","Cantieri di Sarnico","Cape Craft","Cape Dory","Cape Horn","Capelli","Caravelle","Caribe","Carlson","Carnevali","Carolina","Carolina Classic","Carolina Skiff","Carrera Boats","Carroll Marine","Carter","Carver","Cast & Blast","Catalina","Catamaran Cruisers","Catana","Cayman","Cayman Yachts","Caymas","Celebrity","Centurion","Century","Cerri Cantieri Navali","Champion","Channel Island","Chaos","Chaparral","Charger","Checkmate","Cheetah","Cheoy Lee","Cherokee","Cherubini","Chesapeake","Cheverton","Chris-Craft","Cigarette","Class 40","Clearwater","Clipper","Coastal Craft","Cobalt","Cobia","Cobra","Cobra Ribs","Codecasa","Colin Archer","Collingwood","Colombo","Columbia","Colvic","Com-Pac","Comar","Comet","Comfortina","Comitti","Commander","Commercial","Compromis","Conam","Concept","Concordia","Contender","Contessa","Contest","Cooper","Corby","Core Ice","Cornish Crabbers","Coronado","Correct Craft","Corsiva","Corvette","Cougar","Cranchi","Creek","Crescent","Crest","Crestliner","Crevalle","Crownline","Crowther","Cruisers","Cruisers Yachts","Custom Line","Custom-Craft","Cutter","Cutwater","Cypress Cay","DaVinci","Dale","Dalla Pieta","Dargel","Davis","De Antonio Yachts","De Birs","De Groot","De Vries Lentsch","DeFever","Deep Impact","Defiance","Dehler","Dellapasqua","Delphia","Delta","Delta Powerboats","Denison","Derecktor","Devon","Di Donna","Diano","Diesel Duck","Discovery","Dixon","Dolphin","Dominator","Donzi","Doqueve","Dorado","Doral","Downeast","Draco","Drago","Dragonfly","Drascombe","Dreamline","Dromeas","Duckworth","Dudley Dix","Duffy","Dufour","Dusky","Dyer","Dyna","Eagle","EagleCraft","Eastbay","Eastern","Ebbtide","Edership","Edge Duck Boats","Edgewater","Egemar","Egg Harbor","Elan Boats","Elan Power","Eliminator","Elling","Elliott","Ellis","Endeavour","Endurance","Enterprise Marine","Envision","Eolo","Epic","Ericson","Essex Boats","Etap","Eurobanker","Eurocraft","Evans","Everglades","Evo Yachts","Excel","Explorer","Explorer Motor Yachts","Faeton","Fairey","Fairline","Falcon","Falcon Boats","Fantasy","Farr","Farrier","Feadship","Feeling","Ferretti Yachts","Fiart","Fibrafort","Filippetti","Finnmaster","Finseeker","Fish Rite","Fisher","Fjord","Flash Cat","Fleming","Fletcher","Focus Motor Yachts","Foldable RIB","Folkboat","Forest River","Formosa","Formula","Fortier","Fountain","Fountaine Pajot","Four Winns","Franchini","Fratelli Aprea","Frauscher","Freedom","Freeman","Frers","Frontier","Fuji","Furia","Futuna","G3","Gagliotta","Gala","Galeon","Galia","Gallart","Garcia","Garin","Garlington","Gator Tail","Gator Trax","Gekko","Gemini","Gheenoe","Gianetti","Gib'Sea","Gibson","Gillissen","Giorgi","Glacier Bay","Glasstream","Glastron","Glen-L","Gobbi","Godfrey","Gozzard","Grady-White","Grainger","Grand","Grand Banks","Grand Harbour","Grand Soleil","Granocean","Greenline","Gregor","Grumman","Gulf","Gulf Coast","Gulf Craft","Gulfstar","Guy Couach","HCB","Haines","Hake / Seaward","Hallberg-Rassy","Hallett","Halmatic","Hampton","Hans Christian","Hanse","Harbor Cottage","Harbor Master","Hardy","Hargrave","Harmony","Harris","Harris FloteBote","Harris-Kayot","Hatteras","Heard","Heesen","Helmsman Trawlers","Henriques","Heritage East","Herreshoff","Hershine","Hewes","Hewescraft","Heyday","Heysea","Hi-Star","Highfield","Hille","Hillyard","Hilter Royal","Hinckley","Hinterhoeller","Hobie","Hobie Cat","Holiday Mansion","Holman","Horizon","Huckins","Hudson","Hughes","Humber","Hunt Yachts","Hunter","Hunton","Hurricane","Hustler","Hydra-Sports","Hylas","ISA","Ice Yachts","Ilver","Inace","Innovazione e Progetti","Interboat","Intercruiser","Intermare","Intrepid","Invictus","Invincible","Irwin","Island Gypsy","Island Packet","Island Trader","Islander","Italcraft","Italia","Itama","J Boats","JC","Jade","Jaguar","Jamestowner","Janmor","Jarrett Bay","Javelin","Jeanneau","Jefferson","Jersey","Jersey Cape","Jetten","Johnson","Jones Brothers","Jongert","Jouet","Judel and Vrolijk","Jupiter","Kadey-Krogen","Kalik","Kanter","Karnic","Kawasaki","Kayot","Kelly Peterson","Kelt","Kencraft","Kenner","Kettenburg","Key Largo","Key West","Kha Shing","KingFisher","Kirie","Knysna","Koopmans","LM","Lagoon","Laguna","Lakeview","Lancer Yachts","Landamores","Landau","Larson","Laser","Latitude 46","Laurent Giles","Lavey Craft","Legacy","Legacy Yachts","Legend","Legend Craft","Lema","Lemsteraak","Leonard","Leopard","Liberty","Limestone","Lindell","Linssen","Little Harbor","Livingston","Lochin","Lomac","Lord Nelson","Low Country","Lowe","Luders","Luhrs","Lund","Luxe-Motor","Lyman","Lyman-Morse","MB","MJM Yachts","MTI","MacGregor","Macwester","Mag Bay","Magnum","Mainship","Maiora","Majek","Majesty","Majoni","Mako","Malibu","Malo","Mangusta","Manitou","Mano","Mano Marine","Manta","Maori","Maple Leaf","Marchi","Mares","Marex","Mariah","Maril","Marine Projects","Marine Trader","Marine Yachting","Mariner","Marinette","Maritime","Maritime Skiff","Maritimo","Marlago","Marlow","Marlow-Hunter","Marquis","Marshall","Mason","Mastercraft","Matthews","Maverick Boat Co.","Maxi","Maxi Dolphin","Maxim","Maxum","May-Craft","Mazury","McConaghy","McKinna","Mediterranean","Mengi Yay","Menorquin","Mercury Inflatables","Meridian","Meta","Midnight Express","Mikelson","Minor Offshore","Mirage","MirroCraft","Misty Harbor","Mitchell","Mochi Craft","Monark","Mondomarine","Monk","Monte Carlo","Monte Carlo Yachts","Monte Fino","Montego Bay","Monterey","Moody","Moomba","Moonen","Moorings","Morgan","Morris","Motomar","Mulder","Mustang","Myabca","Mylius","Mystic","Mystic Powerboats","Najad","Nantucket","Native Watercraft","Nauset","NauticStar","Nautica","Nautica Esposito","Nauticat","Nautique","Nautitech","Nautor","Nautor Swan","Naval Yachts","Navigator","Neel","Nelson","Neptune","Neptunus","New Cruiser Yachts","Newbridge","Newport","Newton","Niagara","Nicholson","Nidelv","Nimbus","Nitro","Nomad","Nonsuch","Nor-Tech","Nord Star","Nordhavn","Nordic","Nordic Tugs","Norseman","North River","North Wind","NorthCoast","Northwest","Northwest Boats","Northwind","Northwood","Nova","Novamarine","Novatec","Novurania","Numarine","Nuova Jolly","Nuva","O'Day","Ocean","Ocean Alexander","Ocean Craft Marine","Ocean Express","Ocean Kayak","Ocean King","Ocean Master","Ocean Yachts","Ocqueteau","Odyssey","Odyssey Pontoons","Offshore Yachts","Okean","Old Town","Olympic","Open","Orca","Orkney","Oromarine","Osprey Pilothouse","Otam","Outback Yachts","Outbound","Outer Reef Trident","Outer Reef Yachts","Outerlimits","Outremer","Oyster","PDQ","PT","Pacemaker","Pacific","Pacific Craft","Pacific Seacraft","Paddle King","Palm Beach","Palm Beach Motor Yachts","Palmer Johnson","Panga","Paragon","Pardo","Parker","Passport","Pathfinder","Patrone","Pearl","Pearson","Pedro","Penn Yan","PerMare","Performance","Perini Navi","Pershing","Peterson","Pfeil","Phantom","Phoenix","Piantoni","Picchiotti","Pioneer","Pirate","Pirelli","Platinum","PlayCraft","Playbuoy","Pluckebaum","Pogo","Polar","Polar Kraft","Polaris","Portofino","Posillipo","Post","Powercat","Powerquest","Premier","President","Prestige","Prima","Primatist","Princecraft","Princess","Privateer","Privilege","Pro Sports","Pro-Line","ProCraft","ProKat","Proficiat","Protector","Prout","Prowler","Puffin","Puma","Pursuit","Queens Yachts","Queenship","Quicksilver","Qwest","RH Boats","RIO","RM Yachts","Raffaelli","Raider","Rampage","Ranger","Ranger Tugs","Ranieri","Raphael","Rapsody","Reflex","Regal","Regency","Regulator","Reichel/Pugh","Reinell","Reinke","Release","Reliance","Renegade","Renken","Revenger","Rhea","Rhodes","Ribcraft","Ribeye","Ribtec","Rinker","Rio Yachts","Riva","Rival","Riviera","Riviera Cruiser","Rizzardi","Robalo","Robert Clark","Robert Perry","Roberts","Rodman","Ronautica","Rosborough","Rose Island","Rosetti Superyachts","Rossiter","Royal Denship","Royal Huisman","Ruby","Rustler","Rybovich","S2","SBM","SCB","Sabre","Sabreline","Sacs","Sadler","Saffier","Saga","Sailfish","Salona","Salpa","San Juan","Sanger","Sangermani","Sanlorenzo","Sanpan","Sargo","Sarnico","Sas-Vektor","Sasga Yachts","Saver","Scanner","Scarab","Schaefer","Schionning","Schock","Sciallino","Scorpion","Scout","Sea Born","Sea Boss","Sea Cat","Sea Chaser","Sea Fox","Sea Hawk","Sea Hunt","Sea Master","Sea Nymph","Sea Ox","Sea Pro","Sea Ranger","Sea Ray","Sea Sport","Sea-Doo","Sea-Doo Sport Boats","SeaArk","SeaCraft","SeaHunter","SeaVee","Seafarer","Seahorse","Sealegs","Sealine","Seamaster","Seascape","Seaswirl","Seaswirl Striper","Seaward","Seawater","Seaway","Seawind","Seawolf","Selene","Selva","Sessa Marine","Shallow Sport","Shallow Stalker","Shamrock","Shannon","ShearWater","Shetland","Shipman","Shiren","Shoalwater","Shockwave","Sigma","Silent","Siltala","Silver Wave","Silverton","Sirius","Skagen","Skater","Skeeter","Skiff Craft","Skipjack","Skipperliner","Sleekcraft","Slickcraft","Slocum","Sly","Smartliner","Smelne","Smoker Craft","Smoky Mountain","Solare","Solaris","Solemar","Sonic","South Bay","SouthWind","Southerly","Southern Cross","Southern Ocean","Southfork","Southport","Soverel","Sparkman & Stephens","Spartan","Spectre","Spencer","Spindrift","Spirit","Spirit Yachts","Sport-Craft","Sportsman","Sprint","Spyder","Stabicraft","Stabile","Stamas","Starcraft","Stardust Cruisers","Starfisher","Starweld","Statement","Steeler","Steiger Craft","Sterling","Stevens","Stilecatalini","Stingray","Storebro","Stott Craft","Stratos","Striker","Striper","Stumpnocker","Sturier","Succes","Sugar Sand","Sumerset","Sun Runner","Sun Tracker","SunCatcher","SunChaser","Sunbeam","Sunbird","Suncruiser","Sundance","Sundancer Pontoons","Sundeck Yachts","Sunreef","Sunsail","Sunsation","Sunseeker","Sunstar","Super Van Craft","Supra","Supreme","Swan","Sweden Yachts","Sweetwater","Sydney","Sylvan","Symbol","Ta Chiao","Ta Shing","Tahoe","Tahoe Pontoon","Tanzer","Targa","Tarquin","Tartan","Taswell","Tayana","Technohull","Tecnomar","Tecnomarine","Tecnorib","Terranova Yachts","Thompson","Thunder Jet","Tiara","Tiara Sport","Tiburon","Tidewater","Tige","Tjalk","Tofinou","Tollycraft","Topaz","Tornado","Toy","Tracker","Traditional","Trapper","Trehard","Trident","Trifecta","Trintella","Triplast","Tripp","Triton","Triumph","Trojan","Troller","Trophy","True North","Trumpy","Tullio Abbate","Tuna","Turquoise","Twin Vee","Uniesse","Uniflite","VIP","VZ","Vagabond","Valhalla Boatworks","Valiant","Valiant RIBs","Valk","Valkkruiser","Van Dam","Van De Stadt","Van der Heijden","Van der Valk","VanDutch","Vancouver","Velocity","Venture","Veranda","Versilcraft","Vexus","Vicem","Victoire","Victoria","Viking","Viking Boats","Viking Princess","Viko","Vindo","Viper","Vismara","Vitech","Vortex","Voyage Yachts","Voyager","Voyager Pontoons","Vripack","W-Class","Wahoo","Wajer","Walker Bay","Wally","War Eagle","Warrior","Watkins","Wauquiez","Weaver","Weeres","WeldBilt","Weldcraft","Wellcraft","Wesmac","West Bay","Westerly","Westport","Westsail","Whaly","Whitby","White River","White Shark","Wide Beam Narrowboat","Wilbur","Willard Marine","William Fife","William Garden","Williams Jet Tenders","Willie","Windy","Winner","Winter Custom Yachts","World Cat","Wrighton","X-Yachts","XL Marine","Xcursion","Xpress","Xtreme","Yamaha Boats","Yamaha WaveRunner","Yar-Craft","Yellowfin","Young Sun","Zar","Zar Formenti","Zeelander","Zodiac","Zuanelli"};
        Arrays.sort(stype);
        stype[0]="Select Boats Type*";
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

    private void getJewelleryType() {
        final String stype[] = {"A","Crowns","Coronet","Corolla","Diadem or circlet","Kokoshnik","Makuṭa","Pschent and khepresh","Tiara","Earrings","Fascinator","Hairpin","Hatpin","Sarpech or aigrette","Bolo tie","Carcanet","Choker","Necklace","Pendant","Torc","RBP","Armlet (upper arm bracelets)","Bangle","Bracelet","Friendship bracelet","Gospel bracelet","Cuff links","Ladies watch","Mens watch","Childs Watch","Ring","Championship ring","Class ring","Engagement ring","Promise ring","Pre-engagement ring","Wedding ring","Slave bracelet","Belly chain","Body piercing jewellery","Breastplate","Brooch","Chatelaine","Anklet (ankle bracelets)","Toe ring","Amulet","Celibacy vow ring","Medical alert jewelry","Membership pin","Military dog tags","Pledge pins","Prayer jewelry","Japa malas","Prayer beads","Prayer rope","Rosary beads","Puzzle jewelry","Puzzle ring","Signet ring","Thumb ring","Cameo","Emblem","Findings","Locket","Medallion","Pendant"};
        Arrays.sort(stype);
        stype[0]="Select Jewellery Type*";
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
        SharedToken sharedToken = new SharedToken(context);
        if (sharedToken.getCatId().equals("1"))
        {
            final String[] addedArray = new String[]{"Property", "House", "Appartment house", "Flat", "Other object"};
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, addedArray);
            arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
            spinner.setAdapter(arrayAdapter2);
        }
        if (sharedToken.getCatId().equals("3"))
        {
            ArrayList<String> years = new ArrayList<String>();
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = 1900; i <= thisYear; i++) {
                years.add(Integer.toString(i));
            }
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinnertext, years);
            arrayAdapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown);
            spinner.setAdapter(arrayAdapter2);
        }


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
                        editor.putString("currency", currency);
                        Log.d("dadafadada", "" + currency);
                        editor.putString("price", price);
                        Log.d("dadafadada", "" + price);
                        editor.putString("address1", edtAddress1.getText().toString());
                        editor.putString("address2", edtAddress2.getText().toString());
                        editor.putString("cur_price", edtPrice.getText().toString());
                        editor.apply();

                        SharedToken sharedToken = new SharedToken(context);
                        sharedToken.getCatId();
                        if (sharedToken.getCatId().equals("1")) {
                            Intent intent = new Intent(context, Upload2Activity.class);
                            startActivity(intent);
                        }else
                        {
                            UploadFragment.hashMap.clear();
//                            UploadFragment.hashMap.put("property_rooms", type);
//                            UploadFragment.hashMap.put("property_minm", "");
//                            UploadFragment.hashMap.put("property_particularities", "");
//                            UploadFragment.hashMap.put("property_mls_id", "");
//                            UploadFragment.hashMap.put("property_floor", "");
//                            UploadFragment.hashMap.put("property_available_type", "");
//                            UploadFragment.hashMap.put("property_added_since", "");
//                            UploadFragment.hashMap.put("property_real_estate_category", "");
//                            UploadFragment.hashMap.put("cur_price", "");
                            UploadFragment.hashMap.put("model", type);
                            UploadFragment.hashMap.put("year", property);
                            UploadFragment.hashMap.put("no_of_investor", otherType);
                            if (sharedToken.getCatId().equals("6")) {
                                if (relative1.getText().toString().length() > 0) {
                                    UploadFragment.hashMap.put("item_video", relative1.getText().toString());
                                }
                            }
                            Intent intent = new Intent(context, UploadImagesActivity.class);
                            startActivity(intent);
                        }




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
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, -1);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }


                } else if ("Gallery".equals(dialogOptions[which])) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
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
                    //File videoFile = new File(getRealPathFromURIPath(uri));
                    File videoFile = new File(compressImage(getRealPathFromURIPath(uri)));
                    RequestBody videoBody = RequestBody.create(MediaType.parse("*/*"), videoFile);
                    part = MultipartBody.Part.createFormData("item_video", videoFile.getName(), videoBody);
                    txt_upload_image1.setVisibility(View.VISIBLE);
                    txt_upload_image1.setText(videoFile.getName());
                }
                break;

            case REQUEST_CAMERA:
                if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    File videoFile = new File(getRealPathFromURIPath(uri));
                    RequestBody videoBody = RequestBody.create(MediaType.parse("*/*"), videoFile);
                    part = MultipartBody.Part.createFormData("item_video", videoFile.getName(), videoBody);
                    txt_upload_image1.setVisibility(View.VISIBLE);
                    txt_upload_image1.setText(videoFile.getName());
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
                    filepart = MultipartBody.Part.createFormData("logov", file.getName(), requestFile);
                }
                break;
            default:
                break;
        }
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

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public MultipartBody.Part sendImageFileToserver(Bitmap bitMap) throws IOException {
        File filesDir = getActivity().getFilesDir();
        File file = new File(filesDir, "logov" + ".mp4");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        RequestBody reqFile = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("item_video", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "logov");
        return body;
    }

    private String getRealPathFromURIPath(Uri contentURI) {
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
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
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
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
}
