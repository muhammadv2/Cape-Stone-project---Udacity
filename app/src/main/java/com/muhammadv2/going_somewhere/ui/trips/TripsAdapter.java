package com.muhammadv2.going_somewhere.ui.trips;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhammadv2.going_somewhere.R;
import com.muhammadv2.going_somewhere.model.Trip;

import java.util.List;

import butterknife.ButterKnife;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {

    private final Context mContext;
    private int mItemsInTheList;
    private final OnItemClickListener mItemClickListener;
    private final List<Trip> trips;

    /**
     * Adapter constructor helping setup the Adapter and ViewHolder with
     *
     * @param context             needed to be passed to picasso library
     * @param data                needed to update the imageView with Movie objects
     * @param onItemClickListener This allow us to use the Adapter as a component with MainActivity
     */
    public TripsAdapter(Context context, List<Trip> data, OnItemClickListener onItemClickListener) {

        mContext = context;

        //member variable will be updated with the list size to be returned in getITemCount method
        if (data != null && data.size() > 0) mItemsInTheList = data.size();

        trips = data;

        mItemClickListener = onItemClickListener;
    }


    /**
     * interface that will define our listener
     */
    public interface OnItemClickListener {
        void onClick(int position);
    }

    /**
     * To create our ViewHolder by inflating our XML and returning a new MovieVieHolder
     */
    @Override
    public TripsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int id = R.layout.card_trip;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(id, parent, false);

        return new TripsViewHolder(view);
    }

    /**
     * By getting our Movie object associated with the @param position and then creating a Url from
     * the base url and every object path to complete the url to be passed to the holder bind method
     */
    @Override
    public void onBindViewHolder(TripsViewHolder holder, int position) {

        Trip trip = trips.get(position);

        holder.itemView.setTag(position);

    }


    @Override
    public int getItemCount() {
        return mItemsInTheList; // Simply return the number of the list size
    }


    /**
     * Creating of the ViewHolder class which will help decreasing the calls to findViewById method
     */
    class TripsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


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
