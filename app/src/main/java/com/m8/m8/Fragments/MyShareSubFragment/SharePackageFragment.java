package com.m8.m8.Fragments.MyShareSubFragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m8.m8.Activities.HomeActivity;
import com.m8.m8.ApiInterface;
import com.m8.m8.SpripePayment;
import com.m8.m8.UploadActivity.UploadActivity;
import com.m8.m8.util.Config;
import com.m8.m8.util.ProgressBarClass;
import com.m8.m8.R;
import com.m8.m8.RetrofitModel.GetPlanApi;
import com.m8.m8.RetrofitModel.SendPlanData;
import com.m8.m8.ServiceGenerator;
import com.m8.m8.util.SharedRate;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SharePackageFragment extends Fragment {


    View view;
    Context context;
    Toolbar toolbar;
    ImageView drawer;
    TextView textView;
    Button btnNovice, btnPlayer, btnProfessional, btnBusiness;
    String number;
    SharedRate sharedRate;

    TextView txtFreePrice, txtFreeHeading, txtFreeShare, txtNovicePrice, txtNoviceHeading, txtNoviceShare, txtPlayerPrice, txtPlayerHeading, txtPlayerShare,
            txtProPrice, txtProHeading, txtProShare, txtBusinessPrice, txtBusinessHeading, txtBusinessShare,
            txtFreeProperty, txtNoviceProperty, txtPlayerProperty, txtProProperty, getTxtBusinessProperty;

    String shareId;

    //paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);


    String amount = "";


    public SharePackageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_share_package, container, false);

        init();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // Set title bar
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

        textView.setText("Buy Package");


        // start payment Gateway

        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        getActivity().startService(intent);


        //get the paln details
        getPlanDetails();


        return view;
    }


    //paypal payment
    private void PaymentProgress(String id, String plan) {

        /*PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Purchase the " + plan + " share package", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);*/

        Intent intent = new Intent(getContext(),SpripePayment.class);
        intent.putExtra("userId",HomeActivity.userId);
        intent.putExtra("shareId",shareId);
        startActivity(intent);


    }


    @Override
    public void onDestroy() {
        getActivity().startService(new Intent(context, PayPalService.class));
        super.onDestroy();
    }

    private void init() {
        toolbar = (Toolbar) view.findViewById(R.id.tooolbar);
        drawer = (ImageView) view.findViewById(R.id.tooolbarImage);
        textView = (TextView) view.findViewById(R.id.toolbarText);
        btnNovice = (Button) view.findViewById(R.id.btnNovice);
        btnBusiness = (Button) view.findViewById(R.id.btnBusiness);
        btnPlayer = (Button) view.findViewById(R.id.btnPlayer);
        btnProfessional = (Button) view.findViewById(R.id.btnProfessional);


        txtFreePrice = (TextView) view.findViewById(R.id.txtFreePrice);
        txtFreeHeading = (TextView) view.findViewById(R.id.txtFreeHeading);
        txtFreeShare = (TextView) view.findViewById(R.id.txtFreeShares);
        txtFreeProperty = (TextView) view.findViewById(R.id.txtProperty);

        txtNoviceHeading = (TextView) view.findViewById(R.id.txtNoviceHeading);
        txtNoviceShare = (TextView) view.findViewById(R.id.txtNoviceShare);
        txtNovicePrice = (TextView) view.findViewById(R.id.txtNovicePrice);
        txtNoviceProperty = (TextView) view.findViewById(R.id.txtNoviceProperty);

        txtPlayerPrice = (TextView) view.findViewById(R.id.txtPlayerPrice);
        txtPlayerHeading = (TextView) view.findViewById(R.id.txtPlayerHeading);
        txtPlayerShare = (TextView) view.findViewById(R.id.txtPlayerShare);
        txtPlayerProperty = (TextView) view.findViewById(R.id.txtPlayerProperty);

        txtProPrice = (TextView) view.findViewById(R.id.txtProfessionalPrice);
        txtProHeading = (TextView) view.findViewById(R.id.txtProfessionalHeading);
        txtProShare = (TextView) view.findViewById(R.id.txtProfessionalShare);
        txtProProperty = (TextView) view.findViewById(R.id.txtProfessionalProperty);

        txtBusinessPrice = (TextView) view.findViewById(R.id.txtBusinessPrice);
        txtBusinessHeading = (TextView) view.findViewById(R.id.txtBusinessHeading);
        txtBusinessShare = (TextView) view.findViewById(R.id.txtBusinessShare);
        getTxtBusinessProperty = (TextView) view.findViewById(R.id.txtBusinessProperty);

        number = String.valueOf(HomeActivity.propertyNumber);

        sharedRate = new SharedRate(context);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null) {
                    try {
                        String payemtDetails = confirmation.toJSONObject().toString(4);
                        String id = confirmation.getProofOfPayment().getPaymentId().toString();

                        Log.d("++++", "" + id);

                        sendintoServer(id);


                        Log.d("payment", payemtDetails + "\n" + id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "Transaction Cancel", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(context, "Invalid Transaction", Toast.LENGTH_LONG).show();
        }
    }

    private void sendintoServer(String id) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<SendPlanData> call = apiInterface.sendBuyshareDetails(HomeActivity.userId, shareId, id);
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait");
        dialog.show();
        call.enqueue(new Callback<SendPlanData>() {
            @Override
            public void onResponse(Call<SendPlanData> call, Response<SendPlanData> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Payment Sucessful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context,HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SendPlanData> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getPlanDetails() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<GetPlanApi> call = apiInterface.getPlanData();
        final ProgressDialog dialog = ProgressBarClass.showProgressDialog(context, "Please wait...");
        dialog.show();

        call.enqueue(new Callback<GetPlanApi>() {
            @Override
            public void onResponse(Call<GetPlanApi> call, Response<GetPlanApi> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        if (i == 0) {
                            GetPlanApi.Datum datum = response.body().getData().get(0);
                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());

                            txtFreePrice.setText("USD" + "\n" + String.format("%.2f", price));
                            txtFreeHeading.setText(datum.getPlanName());
                            //txtFreeShare.setText(datum.getPlanShareQty() + " Shares");
                            //txtFreeProperty.setText("Upload " + datum.getPlanItemQty() + " Property to Sell");
                        }

                        if (i == 1) {
                            final GetPlanApi.Datum datum = response.body().getData().get(1);
//                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());
                            txtNovicePrice.setText("USD\n" + datum.getPlanPrice().toString());
                            txtNoviceHeading.setText(datum.getPlanName());
                            //txtNoviceShare.setText(datum.getPlanShareQty() + " Shares");
                            //txtNoviceProperty.setText("Upload " + datum.getPlanItemQty() + " Property to Sell");
                            btnNovice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (number.equals("-1")) {
                                        Snackbar.make(getActivity().findViewById(android.R.id.content), "You already buy the Business Package", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        shareId = datum.getId().toString();
                                        amount = datum.getPlanPrice();
                                        String id = datum.getId().toString();
                                        PaymentProgress(id, datum.getPlanPrice());
                                    }
                                }
                            });
                        }


                        if (i == 2) {
                            final GetPlanApi.Datum datum = response.body().getData().get(2);
//                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());
                            txtPlayerPrice.setText("USD\n" + datum.getPlanPrice().toString());
                            txtPlayerHeading.setText(datum.getPlanName());
                            //txtPlayerShare.setText(datum.getPlanShareQty() + " Shares");
                            //txtPlayerProperty.setText("Upload " + datum.getPlanItemQty() + " Property to Sell");
                            btnPlayer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (number.equals("-1")) {
                                        Snackbar.make(getActivity().findViewById(android.R.id.content), "You already buy the Business Package", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        shareId = datum.getId().toString();
                                        amount = datum.getPlanPrice();
                                        String id = datum.getId().toString();
                                        PaymentProgress(id, datum.getPlanPrice());
                                    }
                                }
                            });
                        }

                        if (i == 3) {
                            final GetPlanApi.Datum datum = response.body().getData().get(3);
                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());

                            txtProPrice.setText("USD\n" + datum.getPlanPrice().toString());
                            txtProHeading.setText(datum.getPlanName());
                            //txtProShare.setText(datum.getPlanShareQty() + " Shares");
                            //txtProProperty.setText("Upload " + datum.getPlanItemQty() + " Property to Sell");
                            btnProfessional.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (number.equals("-1")) {
                                        Snackbar.make(getActivity().findViewById(android.R.id.content), "You already buy the Business Package", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        shareId = datum.getId().toString();
                                        amount = datum.getPlanPrice();
                                        String id = datum.getId().toString();
                                        PaymentProgress(id, datum.getPlanPrice());
                                    }
                                }
                            });
                        }

                        if (i == 4) {
                            final GetPlanApi.Datum datum = response.body().getData().get(4);
                            double price = Double.valueOf(sharedRate.getShared()) * Double.valueOf(datum.getPlanPrice());

                            txtBusinessPrice.setText("USD\n" + datum.getPlanPrice().toString());
                            txtBusinessHeading.setText(datum.getPlanName());
                            //getTxtBusinessProperty.setText("Upload  Unlimted  Property to Sell");
                            //txtBusinessShare.setText("Unlimted Shares");
                            btnBusiness.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (number.equals("-1")) {
                                        Snackbar.make(getActivity().findViewById(android.R.id.content), "You already buy the Business Package", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        shareId = datum.getId().toString();
                                        amount = datum.getPlanPrice();
                                        String id = datum.getId().toString();
                                        PaymentProgress(id, datum.getPlanPrice());
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetPlanApi> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
        if (UploadActivity.linaer!=null) {
            UploadActivity.linaer.setVisibility(View.GONE);
            UploadActivity.toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (UploadActivity.linaer!=null) {
            UploadActivity.linaer.setVisibility(View.VISIBLE);
            UploadActivity.toolbar.setVisibility(View.VISIBLE);
        }
    }
}
