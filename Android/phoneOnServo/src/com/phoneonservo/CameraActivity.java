package com.phoneonservo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.FrameLayout;

public class CameraActivity extends Activity {
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private Preview mPreview;
    private FrameLayout framePreview;
    private boolean isRecording = false;
    final String TAG = "CameraActivity";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;  

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
      //register a broadcast for bluetooth events
        LocalBroadcastManager.getInstance(this).registerReceiver(bluetoothBroadcastReceiver,
     		      new IntentFilter("bluetooth-event")); 
        initCamera();
    }

   
    /** A safe way to get an instance of the Camera object. */
    public Camera getCameraInstance(){
        Camera c = null;
        try {c = Camera.open(); Log.d(TAG, "Found a camera");} // attempt to get a Camera instance    
        catch (Exception e){ Log.e(TAG, "Coulnd't find a camera");} // Camera is not available (in use or does not exist)
        return c; // returns null if camera is unavailable
    }
    
    public void initCamera(){
    	 // Create an instance of Camera
         mCamera = getCameraInstance();
          Camera.Parameters params = mCamera.getParameters();
          // set the focus mode and flash mode
          params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
          params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
          //cam_mode is a special command to fix a bug in Galaxy S3
          params.set("cam_mode",1);
          // only tried this on Galaxy S3
          params.setPreviewSize(1920,1080);
          mCamera.setParameters(params);
          mCamera.setFaceDetectionListener(new MyFaceDetectionListener(getApplicationContext()));
        // Create our Preview view and set it as the content of our activity.
        mPreview = new Preview(this, mCamera);
        Log.d(TAG, "start preview");
        framePreview = (FrameLayout) findViewById(R.id.camera_preview);
        framePreview.addView(mPreview);  
    }
    
    ShutterCallback mShutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

    // set up callback function that is triggered when a picture is taken
    private PictureCallback mPictureCallback = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };
    
    private void recordVideo(){
        if (isRecording) {
            // stop recording and release camera
            mMediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            Log.d("Video", "Video Stopped");
            broadcastMessage('Y'); // sends to Arduino to inform it that Video just stopped
            isRecording = false;
        } else {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();

                // inform the user that recording has started
                Log.d("Video", "Video Started");
                broadcastMessage('X'); // sends to Arduino to inform it that Video just started
                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                // inform user
            }
        }
    }
    
    private boolean prepareVideoRecorder(){
    	
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
    
    
    private BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
  	  @Override
  	  public void onReceive(Context context, Intent intent) {
  	    char buffer = intent.getCharExtra("message", '0');
  	    receivedBluetooth(buffer);
  	  }
  	};
    
	public void receivedBluetooth(char msg){
		if(msg == 'P'){
         	Log.d(TAG, "Taking a picture!");	
         	mCamera.takePicture(mShutterCallback, null, mPictureCallback);
         	broadcastMessage('M'); // sends to Arduino to inform it that a picture was taken
         	try {Thread.sleep(500);} catch (InterruptedException e) {} // shows captured image for 0.5 second
         	mPreview.mStartPreview();
            } 
		else if(msg == 'V'){
			Log.d(TAG, "Video Event");
			recordVideo();
		}
		else if(msg == 'O'){
			finish();
		}
	}
	
	 @Override
	protected void onResume() {
		 Log.d(TAG, "onResume()");
		 super.onResume();
		 
    }
	
    @Override
    protected void onPause() {
    	Log.d(TAG, "onPause()");
        super.onPause();
        finish();
    }
    
    protected void onDestroy() { 
    	Log.d(TAG, "onDestroy()");
    	LocalBroadcastManager.getInstance(this).unregisterReceiver(bluetoothBroadcastReceiver);
    	if (isRecording)
			 recordVideo(); // turn off video before closing activity
    	releaseMediaRecorder();
    	releaseCamera();
        super.onDestroy();
    }
    
    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }
    
    private void releaseCamera(){
    	Log.e("Camera Activity", "releasing camera");
        if (mCamera != null){
        	broadcastMessage('D');
        	mCamera.setPreviewCallback(null);
        	mCamera.stopPreview();
            mCamera.release();     
            mCamera = null;
        }
    }
    
    private void broadcastMessage(char buffer) { // broadcast messages to Main then bluetoothThread
  	  Intent intent = new Intent("bluetooth-event");
  	  intent.putExtra("message", buffer);
  	  LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);   
  	}

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }
    
    
}

