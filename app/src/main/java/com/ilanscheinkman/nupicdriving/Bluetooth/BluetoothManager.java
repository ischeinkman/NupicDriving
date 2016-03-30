package com.ilanscheinkman.nupicdriving.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.github.ivbaranov.rxbluetooth.RxBluetooth;

import java.util.UUID;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A class for connecting to the OBD device via bluetooth.
 * Created by ilan on 3/12/16.
 */
public class BluetoothManager {

    /**
     * The UUID to use when connecting to bluetooth devices.
     */
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    /**
     * Scan and connect to devices.
     * @param context the context to use
     * @return an observable that emits obd bluetooth devices
     */
    public static Observable<BluetoothWrapper> scanForDevice(Context context){
        final RxBluetooth bluetooth = new RxBluetooth(context);
        bluetooth.startDiscovery();
        if (bluetooth.isBluetoothEnabled()) {
            Observable stream = bluetooth.observeDevices().map(new Func1<BluetoothDevice, BluetoothWrapper>() {
                @Override
                public BluetoothWrapper call(BluetoothDevice device) {
                    return new BluetoothWrapperImpl(device);
                }
            });
            stream.doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    bluetooth.cancelDiscovery();
                }
            });
            return stream.publish().autoConnect();
        }
        else return Observable.error(new Exception("Bluetooth not enabled."));
    }
}
