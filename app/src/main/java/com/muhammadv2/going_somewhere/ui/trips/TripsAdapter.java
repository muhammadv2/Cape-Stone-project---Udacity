package com.muhammadv2.going_somewhere.ui.trips;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhammadv2.going_somewhere.Constants;
import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.Trip;
import com.muhammadv2.going_somewhere.ui.tripDetails.TripDetailsActivity;
import com.muhammadv2.going_somewhere.utils.FormattingUtils;
import com.muhammadv2.going_somewhere.utils.ImageUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {

    private Context mContext;
    private final OnItemClickListener mItemClickListener;
    private final ArrayList<Trip> mData;


    /**
     * Adapter constructor helping setup the Adapter and ViewHolder with
     *
     * @param data                needed to update the trip content
     * @param onItemClickListener This allow us to use the Adapter as a component with TripsFragment
     */
    public TripsAdapter(Context context, ArrayList<Trip> data, OnItemClickListener onItemClickListener) {
        mData = data;
        mContext = context;
        mItemClickListener = onItemClickListener;
    }


    /**
     * interface that will define our listener
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }

    /**
     * To create our ViewHolder by inflating our XML and returning a new TripsViewHolder
     */
    @Override
    public TripsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int id = R.layout.card_trip;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(id, parent, false);

        return new TripsAdapter.TripsViewHolder(view);
    }

    /**
     * By getting our Trip object associated with the @param position and then creating a Url from
     * the base url and every object path to complete the url to be passed to the holder bind method
     */
    @Override
    public void onBindViewHolder(TripsViewHolder holder, final int position) {

        if (mData.size() != 0 && mData != null) {
            final Trip trip = mData.get(position);

            ImageUtils.bindImage(trip.getImageUrl(), mContext, holder.tripImage, holder.cardView);
            holder.tripTitle.setText(trip.getTripName());
            holder.tripDuration.setText
                    (FormattingUtils.countHowManyDays(trip.getStartTime(), trip.getEndTime()));

            String cities = trip.getCities().size() + " Cities";
            holder.cityCount.setText(cities);

            holder.btnPlans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TripDetailsActivity.class);
                    intent.putExtra(Constants.TRIP_POSITION, position);
                    intent.putExtra(Constants.ADD_TRIP_NAME, trip.getTripName());
                    mContext.startActivity(intent);
                }
            });

            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size(); // Simply return the number of the list size
    }


    /**
     * Creating of the ViewHolder class which will help decreasing the calls to findViewById method
     */
    class TripsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_trip)
        ImageView tripImage;
        @BindView(R.id.text_trip_title)
        TextView tripTitle;
        @BindView(R.id.text_trip_duration)
        TextView tripDuration;
        @BindView(R.id.btn_add_place)
        Button btnPlans;
        @BindView(R.id.btn_trip_notes)
        Button btnNotes;
        @BindView(R.id.text_city_count)
        TextView cityCount;
        @BindView(R.id.trips_card_view)
        CardView cardView;

        //Constructor help finding our view and set up the view with onClickListener
        TripsViewHolder(View itemView) {
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
