package com.muhammadv2.going_somewhere.ui.tripDetails.placeDetails;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
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

public class PlaceDetailsDialog extends DialogFragment {

    private GeoDataClient mClient;

    @BindView(R.id.image_place)
    ImageView ivPlacePhoto;
    @BindView(R.id.text_place_title)
    TextView tvPlaceTitle;
    @BindView(R.id.text_place_adress)
    TextView tvPlaceAddress;
    @BindView(R.id.tex_place_rating)
    RatingBar rbPlaceRating;
    @BindView(R.id.btn_edit_place)
    Button btnEditPlace;

    public static PlaceDetailsDialog newInstance(String placeId,
                                                 String placeName,
                                                 String placeAddress,
                                                 float placeRating,
                                                 int tripPosition,
                                                 int placeDbId) {
        PlaceDetailsDialog f = new PlaceDetailsDialog();

        // Create a new instance with the needed data show the place details dialog
        Bundle args = new Bundle();
        args.putString(Constants.PLACE_NAME, placeName);
        args.putString(Constants.PLACE_ADRESS, placeAddress);
        args.putFloat(Constants.PLACE_RATING, placeRating);
        args.putString(Constants.PLACE_PHOTO, placeId);
        args.putInt(Constants.TRIP_POSITION, tripPosition);
        args.putInt(Constants.PLACE_ID, placeDbId);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_place_details, container, false);

        // ButterKnife binding
        ButterKnife.bind(this, view);

        // GeoData Client the one we use to request place photos
        mClient = Places.getGeoDataClient(getActivity(), null);

        return view;
    }

    // Method copied for places Api docs to retrieve photo from associated placeID
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

                        // Check if the photo buffer null or not first to not call .get on null obj
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
                            // If null use place holder photo
                            ivPlacePhoto.setImageDrawable(getResources().
                                    getDrawable(R.drawable.trip_place_holder));
                        }
                    }

                });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the passed data from the Details fragment and then populate the views
        final String placeName = getArguments().getString(Constants.PLACE_NAME);
        final String placeAddress = getArguments().getString(Constants.PLACE_ADRESS);
        final float placeRating = getArguments().getFloat(Constants.PLACE_RATING);
        final String placeId = getArguments().getString(Constants.PLACE_PHOTO);
        final int tripPosition = getArguments().getInt(Constants.TRIP_POSITION);
        final int placeDbId = getArguments().getInt(Constants.PLACE_ID);

        tvPlaceTitle.setText(placeName);
        tvPlaceAddress.setText(placeAddress);
        rbPlaceRating.setRating(placeRating);
        getPhotos(placeId);

        btnEditPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment prev = fm.findFragmentByTag(Constants.ADD_TRIP_DIALOG);
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                // Create and show the dialog.
                AddPlaceDialog dialog =
                        AddPlaceDialog.newEditInstance(tripPosition, placeDbId, placeId, placeName);

                dialog.setTargetFragment(getParentFragment(), Constants.DIALOG_FRAGMENT_REQUEST);

                dialog.show(ft, Constants.ADD_TRIP_DIALOG);
                getActivity().getSupportFragmentManager().executePendingTransactions();

                Dialog yourDialog = dialog.getDialog();
                yourDialog.getWindow().setLayout((6 * width) / 7, (2 * height) / 5);
            }
        });

    }
}
