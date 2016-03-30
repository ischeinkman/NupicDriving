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


    /**
     * What the presenter should do when the play/pause button is pressed.
     */
    void onPlayPause();

    /**
     * Connect to the provided bluetooth device to enable the reading of data.
     * @param device the device to connect to
     */
    void connectBluetooth(BluetoothWrapper device);

    /**
     * Get the nearby bluetooth devices and pass them to this presenter's view
     */
    void getBluetoothOptions();

    /**
     * Set this presenter's view.
     * @param view the view to connect this presenter to
     */
    void setView(View view);


    interface View {

        /**
         * Display the car readings as they are emitted.
         * @param stream the stream of readings to display
         */
        void displayReadings(Observable<CarReading> stream);

        /**
         * Clear the currently displayed readings.
         */
        void clearReadings();

        /**
         * Show the loading icon.
         */
        void showLoading();

        /**
         * Hide the loading icon.
         */
        void hideLoading();

        /**
         * Show the "Playing" icon. Used when the view is receiving new CarReadings.
         */
        void showPlay();

        /**
         * Show the "Paused" icon. Used when the CarReading stream is paused.
         */
        void showPause();

        /**
         * Show text in the view. Can be for a message, an error, or anthing else.
         * @param text the text to display
         */
        void showText(String text);

        /**
         * List out the available bluetooth devices to connect to.
         * @param deviceList the nearby devices to diplay
         */
        void showDeviceOptions(List<BluetoothWrapper> deviceList);
    }


}
