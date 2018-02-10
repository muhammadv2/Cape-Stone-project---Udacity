package com.muhammadv2.going_somewhere.ui.tripDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.City;
import com.muhammadv2.going_somewhere.model.CityPlace;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TripDetailsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private final TripDetailsAdapter.OnItemClickListener mItemClickListener;
    private final ArrayList<City> mCities;
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
     * @param cities              needed to update the header of details fragment
     * @param places              needed to update the body of the details fragment
     * @param onItemClickListener This allow us to use the Adapter as a component with MainActivity
     */
    public TripDetailsAdapter(Context context,
                              ArrayList<City> cities,
                              ArrayList<CityPlace> places,
                              TripDetailsAdapter.OnItemClickListener onItemClickListener) {
        mCities = cities;
        mPlaces = places;
        mContext = context;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    /**
     * Creating of the ViewHolders classes which will help decreasing the calls to findViewById method
     */
    class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_place_name)
        TextView placeName;

        @BindView(R.id.text_city_name)
        TextView cityName;

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
