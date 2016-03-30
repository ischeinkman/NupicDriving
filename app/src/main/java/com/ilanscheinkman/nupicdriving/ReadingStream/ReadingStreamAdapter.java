package com.ilanscheinkman.nupicdriving.ReadingStream;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ilanscheinkman.nupicdriving.Model.CarReading;
import com.ilanscheinkman.nupicdriving.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ilan on 2/20/16.
 */
public class ReadingStreamAdapter extends RecyclerView.Adapter<ReadingStreamAdapter.ViewHolder>{

    private Observable<CarReading> readings;
    private List<CarReading> allReadings;

    public ReadingStreamAdapter(Observable<CarReading> readings) {
        allReadings = new ArrayList<>();
        this.readings = readings.observeOn(AndroidSchedulers.mainThread());
        this.readings.subscribe(new Action1<CarReading>() {
            @Override
            public void call(CarReading reading) {
                allReadings.add(reading);
                notifyItemRangeChanged(0,allReadings.size());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_reading_element_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CarReading posReading = allReadings.get(position);
        holder.timestamp.setText(""+posReading.getTimeStamp());
        StringBuilder databuilder = new StringBuilder();
        for (String fieldName : posReading.getReadingMap().keySet()){
            databuilder.append(fieldName).append(": ").append(posReading.getReading(fieldName)).append("\n");
        }
        holder.data.setText(databuilder.toString());
    }

    @Override
    public int getItemCount() {
        return allReadings.size();
    }

    public void clear(){
        allReadings.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timestamp;
        public TextView data;
        public ViewHolder(View itemView) {
            super(itemView);
            timestamp = (TextView) itemView.findViewById(R.id.car_reading_time_stamp);
            data = (TextView) itemView.findViewById(R.id.car_reading_data);
        }
    }
}
