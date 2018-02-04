package com.muhammadv2.going_somewhere.utils;


import android.content.Context;
import android.widget.ImageView;

import com.muhammadv2.going_somewhere.R;
import com.squareup.picasso.Picasso;


/**
 * Using the help of picasso library to fetch our images
 */
public class ImageUtils {

    public static void bindImage(Context context, String imgPath, ImageView intoImage) {

        Picasso
                .with(context)
                .load(imgPath)
                .centerCrop()
                .placeholder(R.drawable.ic_location_city_black_24dp)
                .into(intoImage);
    }
}