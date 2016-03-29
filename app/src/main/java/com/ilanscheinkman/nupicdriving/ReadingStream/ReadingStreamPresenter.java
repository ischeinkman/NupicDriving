package com.ilanscheinkman.nupicdriving.ReadingStream;

import com.ilanscheinkman.nupicdriving.Bluetooth.BluetoothWrapper;
import com.ilanscheinkman.nupicdriving.Model.CarReading;

import java.util.List;

import rx.Observable;

/**
 * Presenter for the reading stream view.
 * Created by ilan on 3/24/16.
 */
public interface ReadingStreamPresenter {


    void onPlayPause();
    void connectBluetooth(BluetoothWrapper device);
    void getBluetoothOptions();


    interface View {
        void displayReadings(Observable<CarReading> stream);
        void clearReadings();
        void showLoading();
        void hideLoading();
        void showPlay();
        void showPause();
        void showText(String text);
        void showDeviceOptions(List<BluetoothWrapper> deviceList);
    }


}
