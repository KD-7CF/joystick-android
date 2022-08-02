package com.game.pad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.ParcelUuid;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public class Bluetooth {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    public Set<BluetoothDevice> pairedDevices;
    private OutputStream outpuStream;

    @SuppressLint("MissingPermission")
    public Bluetooth(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this.context, "Bluetooth is not available!", Toast.LENGTH_SHORT).show();
            ((Activity) this.context).finish();
        }
        if (!bluetoothAdapter.isEnabled())
            bluetoothAdapter.enable();
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        if(bluetoothSocket != null)
            cancel();
    }

    @SuppressLint("MissingPermission")
    public boolean connect(BluetoothDevice device) {

        ParcelUuid[] uuids = device.getUuids();

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(bluetoothSocket == null)
            return false;

        try {
            bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!bluetoothSocket.isConnected())
            return false;

        OutputStream tmpOut = null;
        try {
            tmpOut = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outpuStream = tmpOut;
        return true;
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] bytes) {
        if(bluetoothSocket == null)
            return;
        try {
            outpuStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
