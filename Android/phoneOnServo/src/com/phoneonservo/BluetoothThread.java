package com.phoneonservo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class BluetoothThread extends Thread {
	private Context mContext;
    private final BluetoothSocket mSocket;
    private final InputStream mInStream; // incoming Bluetooth data
    private final OutputStream mOutStream; // outgoing Bluetooth data
 
    public BluetoothThread(BluetoothSocket socket, Context context) {
    	mContext = context;
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
 
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { } 
  
        mInStream = tmpIn;
        mOutStream = tmpOut;
        bluetoothWrite('B'); // inform Arduino that bluetooth is connected
    }
 
    public void run() {
        char buffer;  // buffer store for the stream
        			  // I'm only sending one character, use mmInStrea.read(byte[])
        			  // for more stoarge
 
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                buffer = (char)mInStream.read();
                // broadcast message to activities
                broadcastMessage(buffer);
                Log.d("Bluetooth", "recieved: " + buffer);
            } catch (IOException e) { 
                break;
            }
        }
    }
    
    private void broadcastMessage(char buffer) {
    	  Intent intent = new Intent("bluetooth-event");
    	  intent.putExtra("message", buffer);
    	  LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);   
    	}
    
    /* Call this from the main activity to send data to the remote device */
    public void bluetoothWrite(int msg) {
        try {
        	mOutStream.write(msg);
        } catch (IOException e) { }
    }
    
    public void bluetoothWriteBytes(byte[] bytes) {
        try {
        	mOutStream.write(bytes);
        } catch (IOException e) { }
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) { }
    }
}
