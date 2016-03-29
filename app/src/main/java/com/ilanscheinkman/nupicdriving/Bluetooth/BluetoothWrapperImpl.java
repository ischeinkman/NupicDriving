package com.ilanscheinkman.nupicdriving.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by ilan on 3/28/16.
 */
public class BluetoothWrapperImpl implements BluetoothWrapper {

    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothDevice device;
    private BluetoothSocket socket;

    public BluetoothWrapperImpl(BluetoothDevice device){
        this.device = device;
    }

    @Override
    public String getName() {
        return device.getName();
    }

    @Override
    public void connect() throws IOException{
        socket = device.createRfcommSocketToServiceRecord(MY_UUID);
        socket.connect();
    }

    @Override
    public boolean isConnected() {
        return (socket != null && socket.isConnected());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }
}
