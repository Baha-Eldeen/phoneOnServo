// Author: Et8an, http://www.youtube.com/user/Et8an
// Date : 12-Mar-2013
// Description: This app is used to communicate with an Arduino(Master) which controls 
//				Android's(Slave) camera. In return, Android reply to messages from Arduino
//				to inform it that a message has been received

package com.phoneonservo;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Main extends Activity{
	boolean isCameraAlive = false;
	private Context mContext;

	 boolean isBluetoothConnected = false;
	//Bluetooth constatns
	 private final int REQUEST_ENABLE_BT = 1;
	 protected final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); 
	 // UUID is a unique identifier. To get your phone's ID, use this command:
	 //TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	 //String uuid = tManager.getDeviceId();
	 protected final String address = "00:06:66:09:8B:23"; // This is the MAC address of the bluetooth chip 
	 //Bluetooth Thread
	 BluetoothThread mBluetoothThread;
	 ConnectBluetooth bluetoothAsyncTask;
	 
	@Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       
       //register a broadcast for bluetooth events
       LocalBroadcastManager.getInstance(this).registerReceiver(bluetoothBroadcastReceiver,
    		      new IntentFilter("bluetooth-event"));  
       LocalBroadcastManager.getInstance(this).registerReceiver(faceBroadcastReceiver,
 		      new IntentFilter("face-event"));  
       initBluetooth();
	}
	
	
	private class ConnectBluetooth extends AsyncTask<Void, Void, Integer> {
		private BluetoothSocket tmp = null;
		private BluetoothSocket mSocket;
	    private BluetoothAdapter mBluetoothAdapter; 

	     protected Integer doInBackground(Void... a) {
	    	// Get a BluetoothSocket to connect with the given BluetoothDevice
	         try {
	        	 BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
	             // MY_UUID is the app's UUID string, also used by the server code
	             tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	         } catch (IOException e) { }
	         mSocket = tmp;		 
	         // Cancel discovery because it will slow down the connection
	         mBluetoothAdapter.cancelDiscovery();
	         try {
	             // Connect the device through the socket. This will block
	             // until it succeeds or throws an exception
	        	 mSocket.connect();
	             Log.d("Bluetooth", "connected!");
	         } catch (IOException connectException) {
	             // Unable to connect; close the socket and get out
	             try {
	            	 mSocket.close();
	             } catch (IOException closeException) { }
	             return 0;
	         }
			return 1;
	        
	     }
	     protected void onPostExecute(Integer isConnected) {
	    	 if(isConnected == 1){ // 1 to indicate a successful connection
		    	// Do work to manage the connection (in a separate thread)
	    		 TextView bluetoothState = (TextView)findViewById(R.id.bluetoothState);
	    		 bluetoothState.setText("Bluetooth Connected");
	    		 
	    		 ProgressBar mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
	    		 mProgressBar.setVisibility(View.GONE);
	    		 
	    		 CheckBox mCheckBox = (CheckBox)findViewById(R.id.checkBoxIsConnected);
	    		 mCheckBox.setVisibility(View.VISIBLE);
	    		 
	    		 isBluetoothConnected = true;
	    		 mBluetoothThread = new BluetoothThread(mSocket, mContext);
	    		 mBluetoothThread.start();
	    	 }
	     } 
	 }
	
	private void initBluetooth(){
		BluetoothAdapter mBluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
	   	if(mBluetoothAdapter != null){
	   		checkBluetoothState(mBluetoothAdapter);
	   		bluetoothAsyncTask = new ConnectBluetooth();
	   		bluetoothAsyncTask.mBluetoothAdapter = mBluetoothAdapter;
	   		bluetoothAsyncTask.execute();
	   	}
	}
	
	public void checkBluetoothState(BluetoothAdapter mBluetoothAdapter){
    	if (!mBluetoothAdapter.isEnabled()) {  // check if bluetooth is enabled
        	Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
	}
	
	public void sendBluetooth(char msg){
		mBluetoothThread.bluetoothWrite(msg);
		 Log.d("Bluetooth Main", "sending.." + msg);
	}
	
	// don't call this. This method is called by broadcast
	public void receivedBluetooth(char msg){
		switch(msg){
		case 'O': // Turn on Camera
			if(!isCameraAlive){
				sendBluetooth('C'); // inform Arduino that Camera is on
				startActivity(new Intent("com.phoneonservo.CameraHandler"));
				isCameraAlive = true;
			}
			break;
		case 'D':
			sendBluetooth('D'); // inform Arduino that Camera is off
			isCameraAlive = false; 
			break;    
		case 'M':
			sendBluetooth('M'); // inform Arduino that a picture was taken
			break;  
		case 'X':
			sendBluetooth('X'); // sends to Arduino to inform it that Video just started
			break; 
		case 'Y':
			sendBluetooth('Y'); // sends to Arduino to inform it that Video just stopped
			break; 
		}
	}

	private BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    char buffer = intent.getCharExtra("message", '0');
	    receivedBluetooth(buffer);
	  }
	};
	
	private BroadcastReceiver faceBroadcastReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
			int faceBuffer = intent.getIntExtra("faceCoordinates", 0);
			sendBluetooth('F'); // inform Arduino that a face has been detected
			byte lowerByte = (byte) (faceBuffer & 0xff); // low byte
			byte higherByte = (byte) ((faceBuffer >> 8) & 0xff); // high byte
			sendBluetooth((char)lowerByte); // send face coordinates
			sendBluetooth((char) higherByte);
		  }
		};


	
	@Override
	protected void onDestroy() {
	  // Unregister since the activity is about to be closed.
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(bluetoothBroadcastReceiver);
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(faceBroadcastReceiver);
	  bluetoothAsyncTask.cancel(true);
	  if(isBluetoothConnected){
		  sendBluetooth('L'); // sends to Arduino to inform it that Android is closing
		  mBluetoothThread.cancel();
	  }
	  super.onDestroy();
	}

}
