package com.phoneonservo;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

class MyFaceDetectionListener implements Camera.FaceDetectionListener {
	Context mContext;
	
	public MyFaceDetectionListener(Context context){
		mContext = context;	
	}
	
    @Override
    public void onFaceDetection(Face[] faces, Camera camera) {
        if (faces.length > 0){
            Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
            broadcastMessageFace(faces[0].rect.centerX());
        }
    }
    
    private void broadcastMessageFace(int buffer) {
  	  Intent intent = new Intent("face-event");
  	  intent.putExtra("faceCoordinates", buffer);
  	  LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);   
  	}
}