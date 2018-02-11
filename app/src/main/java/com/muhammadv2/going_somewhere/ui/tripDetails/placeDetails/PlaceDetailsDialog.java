package com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PlaceDetailsDialog extends DialogFragment {

    private String placeName;
    private String placeAddress;
    private float placeRating;
    private String placePhoto;

    @BindView(R.id.image_place)
    ImageView ivPlacePhoto;
    @BindView(R.id.text_place_title)
    TextView tvPlaceTitle;
    @BindView(R.id.text_place_adress)
    TextView tvplaceAddress;
    @BindView(R.id.tex_place_rating)
    RatingBar rbPlaceRating;
    @BindView(R.id.btn_edit_place)
    Button btnEditPlace;


    public static PlaceDetailsDialog newInstance(String placeName,
                                                 String placeAdress,
                                                 float placeRating,
                                                 String placePhoto) {
        PlaceDetailsDialog f = new PlaceDetailsDialog();

        // Supply tripPosition input as an argument.
        Bundle args = new Bundle();
        args.putString(Constants.PLACE_NAME, placeName);
        args.putString(Constants.PLACE_ADRESS, placeAdress);
        args.putFloat(Constants.PLACE_RATING, placeRating);
        args.putString(Constants.PLACE_PHOTO, placePhoto);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dialog_place_details, container, false);

        ButterKnife.bind(this, view);
        Timber.plant(new Timber.DebugTree());

        placeName = getArguments().getString(Constants.PLACE_NAME);
        placeAddress = getArguments().getString(Constants.PLACE_ADRESS);
        placeRating = getArguments().getFloat(Constants.PLACE_RATING);
        placePhoto = getArguments().getString(Constants.PLACE_PHOTO);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPlaceTitle.setText(placeName);
        tvplaceAddress.setText(placeAddress);
        rbPlaceRating.setRating(placeRating);
    }

}
