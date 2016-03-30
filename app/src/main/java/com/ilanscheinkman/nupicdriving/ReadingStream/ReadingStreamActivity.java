package com.ilanscheinkman.nupicdriving.ReadingStream;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ilanscheinkman.nupicdriving.Bluetooth.BluetoothWrapper;
import com.ilanscheinkman.nupicdriving.Model.CarReading;
import com.ilanscheinkman.nupicdriving.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class ReadingStreamActivity extends AppCompatActivity implements ReadingStreamPresenter.View{

    private ReadingStreamPresenter presenter;
    private ReadingStreamAdapter adapter;

    @Bind(R.id.reading_stream_data_list) RecyclerView readingList;
    @Bind(R.id.reading_stream_loading) ProgressBar bar;
    @Bind(R.id.reading_stream_fab) FloatingActionButton playPauseButton;
    @Bind(R.id.reading_stream_toolbar) Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new ReadingStreamPresenterImpl(this);

        setSupportActionBar(toolbar);

        readingList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_connect_bluetooth){
            presenter.getBluetoothOptions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick (R.id.reading_stream_fab)
    public void onPlayPause(){
        presenter.onPlayPause();
    }

    @Override
    public void displayReadings(Observable<CarReading> stream) {
        adapter = new ReadingStreamAdapter(stream);
        readingList.setAdapter(adapter);
        if (readingList.getVisibility() != View.VISIBLE) readingList.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearReadings() {
        if (adapter != null)adapter.clear();
    }

    @Override
    public void showLoading() {
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        bar.setVisibility(View.GONE);
    }

    @Override
    public void showPlay() {
        playPauseButton.setImageResource(R.drawable.ic_pause_white); //Click the pause button to pause
    }

    @Override
    public void showPause() {
        playPauseButton.setImageResource(R.drawable.ic_play_arrow_white); //Click the play button to start playing
    }

    @Override
    public void showText(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeviceOptions(final List<BluetoothWrapper> deviceList) {
        if (deviceList == null || deviceList.size() == 0){
            showText("No devices found.");
            return;
        }
        String[] names = new String[deviceList.size()];
        for (int i = 0; i< names.length; i++) names[i] = deviceList.get(i).getName();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select OBD Device");
        builder.setSingleChoiceItems(names,0,null);
        builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BluetoothWrapper selectedDevice = deviceList.get(((AlertDialog) dialog).getListView().getCheckedItemPosition());
                presenter.connectBluetooth(selectedDevice);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
