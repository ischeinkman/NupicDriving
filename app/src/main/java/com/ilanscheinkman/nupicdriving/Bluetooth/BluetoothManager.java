package com.ilanscheinkman.nupicdriving.Bluetooth;

import android.bluetooth.BluetoothDevice;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;
import com.ilanscheinkman.nupicdriving.ContextManager;

import java.util.UUID;

import rx.Observable;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * A class for connecting to the OBD device via bluetooth.
 * Created by ilan on 3/12/16.
 */
public class BluetoothManager {

    /**
     * The UUID to use when connecting to bluetooth devices.
     */
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static RxBluetooth bluetooth;
    /**
     * Scan and connect to devices.
     * @return an observable that emits obd bluetooth devices
     */
    public static Observable<BluetoothWrapper> scanForDevice(){
        if (bluetooth == null) bluetooth = new RxBluetooth(ContextManager.getAppContext());
        if(!bluetooth.isBluetoothEnabled()) return Observable.error(new Exception("Bluetooth not enabled."));
        if (!bluetooth.isBluetoothAvailable()) return Observable.error(new Exception("Bluetooth not available."));
        if (!bluetooth.isDiscovering() && !bluetooth.startDiscovery()) return Observable.error(new Exception("Bluetooth not discovering."));
        Observable<BluetoothDevice> obDevices = bluetooth.observeDevices();
        Observable<BluetoothWrapper> obWrappers = obDevices.map(new Func1<BluetoothDevice, BluetoothWrapper>() {
            @Override
            public BluetoothWrapper call(BluetoothDevice bluetoothDevice) {
                return new BluetoothWrapperImpl(bluetoothDevice);
            }
        });
        ConnectableObservable<BluetoothWrapper> pubWrappers = obWrappers.subscribeOn(Schedulers.computation()).publish();
        pubWrappers.connect();
        return pubWrappers;
    }
}
