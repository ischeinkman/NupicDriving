package com.ilanscheinkman.nupicdriving.ReadingStream;

import com.ilanscheinkman.nupicdriving.Bluetooth.BluetoothManager;
import com.ilanscheinkman.nupicdriving.Bluetooth.BluetoothWrapper;
import com.ilanscheinkman.nupicdriving.ContextManager;
import com.ilanscheinkman.nupicdriving.Database.DbHelper;
import com.ilanscheinkman.nupicdriving.Model.CarReading;
import com.ilanscheinkman.nupicdriving.Model.ObdManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ilan on 3/24/16.
 */
public class ReadingStreamPresenterImpl implements ReadingStreamPresenter {

    private View view;
    private ObdManager manager;
    private boolean paused;
    private DbHelper dbHelper;

    public ReadingStreamPresenterImpl(){
        paused = true;
        dbHelper = new DbHelper(ContextManager.getAppContext());
    }

    public ReadingStreamPresenterImpl(View view){
        this();
        setView(view);
    }

    public void setView(View view){
        this.view = view;
        onPause(); //We pause so as not to leak old observables.
    }


    private void onResume() {
        view.showLoading();
        if (manager == null) {
            view.hideLoading();
            view.showText("No Bluetooth device currently connected.");
            return;
        }
        view.clearReadings();
        Observable<CarReading> readings = manager.start();

        //We store readings into the database 5 at a time.
        readings.buffer(5).flatMap(new Func1<List<CarReading>, Observable<?>>() {
            @Override
            public Observable<?> call(List<CarReading> carReadings) {
                return dbHelper.insertReadings(carReadings);
            }
        }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.showText(throwable.getMessage());
            }
        });

        //We display them in real time.
        view.displayReadings(readings);
        view.hideLoading();
        paused = false;
        view.showPlay();
    }

    private void onPause() {
        if (manager != null) manager.stop();
        paused = true;
        view.showPause();
    }

    @Override
    public void onPlayPause() {
        if (view == null) return; //Cannot display so won't do unnecessary work.

        if (paused){
            onResume();
            paused = false;
        }
        else {
            onPause();
            paused = true;
        }
    }

    @Override
    public void connectBluetooth(BluetoothWrapper device) {
        if (view == null) return; //Cannot display so won't do unnecessary work.

        view.showLoading();
        try {
            if (!device.isConnected()) device.connect();
            manager = new ObdManager(device.getInputStream(), device.getOutputStream());
        } catch (IOException e) {
            view.hideLoading();
            view.showText(e.getMessage());
        }

    }

    @Override
    public void getBluetoothOptions() {
        if (view == null) return; //Cannot display so won't do unnecessary work.
        view.showLoading();
        Observable<BluetoothWrapper> devices = BluetoothManager.scanForDevice(ContextManager.getAppContext());
        Observable<List<BluetoothWrapper>> deviceList = devices.buffer(5, TimeUnit.SECONDS, 3).first().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        deviceList.subscribe(new Action1<List<BluetoothWrapper>>() {
            @Override
            public void call(List<BluetoothWrapper> devices) {view.hideLoading();
                view.showDeviceOptions(devices);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                view.hideLoading();
                view.showText(throwable.getMessage());
            }
        });
    }
}
