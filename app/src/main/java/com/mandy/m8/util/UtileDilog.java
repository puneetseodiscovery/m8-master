package com.mandy.m8.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.mandy.m8.Fragments.MyShareSubFragment.SharePackageFragment;
import com.mandy.m8.R;


public class UtileDilog {

    public static Dialog dialog(Context context, String txtTile, String Buy, final FragmentManager fragmentManager) {
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.custom_dialog_upload);
        dialog.setCancelable(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnBuy = (Button) dialog.findViewById(R.id.btnDelete);
        TextView text = (TextView) dialog.findViewById(R.id.txt_title);
        text.setText(txtTile);

        btnBuy.setText(Buy);

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
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.framelayout, new SharePackageFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return dialog;
    }
}
