package com.muhammadv2.going_somewhere.utils;


import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;


/**
 * Using the help of picasso library to fetch our images
 */
public class ImageUtils {

    public static void bindImage(Uri url, Context context, ImageView intoImage, View backGround) {

        Picasso.with(context).load(url.toString()).into(intoImage,
                PicassoPalette.with(url.toString(), intoImage)
                        .use(PicassoPalette.Profile.VIBRANT_LIGHT)
                        .intoBackground(backGround)

        );
    }
}
