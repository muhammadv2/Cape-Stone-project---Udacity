package com.muhammadv2.going_somewhere.ui.tripDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.CityPlace;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TripDetailsAdapter extends RecyclerView.Adapter<TripDetailsAdapter.PlacesViewHolder> {

    private final TripDetailsAdapter.OnItemClickListener mItemClickListener;
    private final ArrayList<CityPlace> mPlaces;

    /**
     * interface that will define our listener
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }

    /**
     * Adapter constructor helping setup the Adapter and ViewHolder with
     *
     * @param places              needed to update the body of the details fragment
     * @param onItemClickListener This allow us to use the Adapter as a component with MainActivity
     */
    public TripDetailsAdapter(ArrayList<CityPlace> places,
                              TripDetailsAdapter.OnItemClickListener onItemClickListener) {
        mPlaces = places;
        mItemClickListener = onItemClickListener;
    }

    /**
     * To create our ViewHolder by inflating our XML and returning a new TripsViewHolder
     */
    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        int place_details = R.layout.card_place_details;
        View view = inflater.inflate(place_details, parent, false);
        return new PlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder holder, int position) {

        CityPlace place = mPlaces.get(position);
        holder.placeName.setText(place.getPlaceName());
    }


    @Override
    public int getItemCount() {
        if (mPlaces == null) return 0;
        return mPlaces.size();
    }

    /**
     * Creating of the ViewHolders classes which will help decreasing the calls to findViewById method
     */
    class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_place_name)
        TextView placeName;

        //Constructor help finding our view and set up the view with onClickListener
        PlacesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //inject butterKnife library to use the constructor to set it self
            ButterKnife.bind(this, itemView);
        }


        @Override
        public void onClick(View view) {
            mItemClickListener.onClick(getAdapterPosition()); //pass the adapter position to our interface
        }
    }
}
