package com.ilanscheinkman.nupicdriving.Bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A wrapper class for bluetooth devices to allow
 * for easy mocking and removal of android dependencies.
 * Created by ilan on 3/28/16.
 */
public interface BluetoothWrapper {

    /**
     * Get the name of this bluetooth device.
     *
     * @return the name of the device
     */
    String getName();

    /**
     * Connect to this bluetooth device.
     * @throws IOException if the connect fails
     */
    void connect() throws IOException;

    /**
     * Check whether or not this device is connected.
     * @return whether or not this device is connected
     */
    boolean isConnected();

    /**
     * Get an inputstream to this connected bluetooth device.
     * @return inputstream to this connected bluetooth device
     * @throws IOException if the connection fails
     */
    InputStream getInputStream() throws IOException;


    /**
     * Get an outputstream to this connected bluetooth device.
     * @return outputstream to this connected bluetooth device
     * @throws IOException if the connection fails
     */
    OutputStream getOutputStream() throws IOException;
}
