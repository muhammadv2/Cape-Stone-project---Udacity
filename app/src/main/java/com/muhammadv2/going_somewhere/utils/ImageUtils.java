package com.muhammadv2.going_somewhere.utils;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.muhammadv2.going_somewhere.R;
import com.squareup.picasso.Picasso;


/**
 * Using the help of picasso library to fetch our images and also Picasso palette to set associated
 * color on the passed background
 */
public final class ImageUtils {

    public static void bindImage(String url, Context context, ImageView intoImage, View backGround) {

        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.trip_place_holder)
                .into(intoImage,
                        PicassoPalette.with(url, intoImage)
                                .use(PicassoPalette.Profile.VIBRANT_LIGHT)
                                .intoBackground(backGround)

                );
    }

    public static void bindImageWithoutBackground(String url, Context context, ImageView intoImage) {

        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.trip_place_holder)
                .into(intoImage);
    }
}
