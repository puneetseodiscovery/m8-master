package com.m8.m8.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.m8.m8.Fragments.DetailsFragment;
import com.m8.m8.R;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> arrayList = new ArrayList<>();
    LayoutInflater layoutInflater;

    public ImageAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((RelativeLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final ImageView imageView;

        final AVLoadingIndicatorView avLoadingIndicatorView;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.customimageview, container, false);

        imageView = itemView.findViewById(R.id.image_view);

        avLoadingIndicatorView = itemView.findViewById(R.id.avloading);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DetailsFragment.abc) {
                    DetailsFragment.viewPager.setVisibility(View.VISIBLE);
                    DetailsFragment.scrollView.setVisibility(View.GONE);
                    DetailsFragment.abc = true;

                } else {
                    DetailsFragment.viewPager.setVisibility(View.GONE);
                    DetailsFragment.scrollView.setVisibility(View.VISIBLE);
                    DetailsFragment.abc = false;

                }
            }
        });

        Glide.with(context).load(arrayList.get(position)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView);

        ((ViewPager) container).addView(itemView);

//        if (imageView.getRotation()==0)
//        {
//            imageView.setRotation(90);
//        }

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }


}
