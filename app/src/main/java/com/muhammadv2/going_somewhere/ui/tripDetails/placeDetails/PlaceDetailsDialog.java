package com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PlaceDetailsDialog extends DialogFragment {

    private GeoDataClient mClient;

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


    public static PlaceDetailsDialog newInstance(String placeId,
                                                 String placeName,
                                                 String placeAddress,
                                                 float placeRating) {
        PlaceDetailsDialog f = new PlaceDetailsDialog();

        // Supply tripPosition input as an argument.
        Bundle args = new Bundle();
        args.putString(Constants.PLACE_NAME, placeName);
        args.putString(Constants.PLACE_ADRESS, placeAddress);
        args.putFloat(Constants.PLACE_RATING, placeRating);
        args.putString(Constants.PLACE_PHOTO, placeId);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_place_details, container, false);

        ButterKnife.bind(this, view);
        Timber.plant(new Timber.DebugTree());

        mClient = Places.getGeoDataClient(getActivity(), null);

        return view;
    }

    private void getPhotos(String placeID) {
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse =
                mClient.getPlacePhotos(placeID);
        photoMetadataResponse
                .addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                        // Get the list of photos.
                        PlacePhotoMetadataResponse photos = task.getResult();
                        // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        // Get the first photo in the list.
                        Timber.plant(new Timber.DebugTree());
                        Timber.d("sss " + (photoMetadataBuffer.getCount() == 0));
                        if (photoMetadataBuffer.getCount() != 0) {
                            PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                            // Get a full-size bitmap for the photo.
                            Task<PlacePhotoResponse> photoResponse = mClient.getPhoto(photoMetadata);
                            photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                                @Override
                                public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                    PlacePhotoResponse photo = task.getResult();
                                    ivPlacePhoto.setImageBitmap(photo.getBitmap());

                                }
                            });

                        } else {
                            ivPlacePhoto.setImageDrawable(getResources().
                                    getDrawable(R.drawable.trip_place_holder));
                        }
                    }

                });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String placeName = getArguments().getString(Constants.PLACE_NAME);
        String placeAddress = getArguments().getString(Constants.PLACE_ADRESS);
        float placeRating = getArguments().getFloat(Constants.PLACE_RATING);
        String placeId = getArguments().getString(Constants.PLACE_PHOTO);

        tvPlaceTitle.setText(placeName);
        tvplaceAddress.setText(placeAddress);
        rbPlaceRating.setRating(placeRating);
        getPhotos(placeId);

    }


}
