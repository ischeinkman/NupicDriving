package com.ilanscheinkman.nupicdriving.Bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ilan on 3/28/16.
 */
public interface BluetoothWrapper {
    String getName();
    void connect() throws IOException;
    boolean isConnected();
    InputStream getInputStream() throws IOException;
    OutputStream getOutputStream() throws IOException;
}
