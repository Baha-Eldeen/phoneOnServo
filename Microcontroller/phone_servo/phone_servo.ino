// Author: Et8an, http://www.youtube.com/user/Et8an
// Date : 12-Mar-2013
// Description: This sketch controls an Android's camera by Bluetooth. It uses a TV remote as an input from the user. 
//              It recieves inputs from the TV remote and sends it to an Android then it expects an acknowledgment from Android.
//              LED shows the state of Arduino in this manner: 
//              YELLOW: bluetooth is looking for a device to connect to
//              GREEN: bluetooth established a connection
//              BLUE: camera is on with face detection turned off
//              CYAN: camera is on with face detection turned on
//              RED: to indicate whether a video is being recorded or a picture is being taken 
//              WHITE and PINK are not used

#include <Metro.h>
enum { 
    RIGHT = 3280, LEFT = 720, UP = 752, DOWN = 2800, PICTURE = 3928, VIDEO = 1488, FACE = 14318, CAMERAON = 2704, TOOLS = 14057, NOTHING = 0, DONOTHING = -1,
    RED = 1, BLUE = 2, PINK = 3, GREEN = 4, YELLOW = 5, CYAN = 6, WHITE = 7, OFF = 8, // le
    COUNTERCLOCKWISE = 1350, STOP = 1500, CLOCKWISE = 1650,  // servo
    MAIN = 0, CAMERA = 1  // android
};
int faceLocation = 0; // store last face location
int faceBuffer[2]; // buffer that recieves face locations from Android
int state = NOTHING, ledState = YELLOW;
boolean faceFound = false, isFaceDetectionEnabled = false, isVideoOn = false, videoLEDOn = false;
Metro faceDetectionMetro = Metro(40); // check face location every 500 ms
Metro videoMetro = Metro(1000); // toggle LED every 1 second

void setup() 
{ 
    Serial.begin(9600);
    //Initialize components 
    initIR();
    initLED();
    initServo();
} 

void loop() 
{ 
     // read IR
    readIR();
    
     // check face location 
    if(isFaceDetectionEnabled && faceFound && faceDetectionMetro.check()==1){
        handleFace();
    }
    
     // toggle led every second if video is on
    if(isVideoOn && videoMetro.check() == 1){
      if(videoLEDOn){
          changeLED(ledState);
          videoLEDOn = false;
      }else{
          changeLED(RED);
          videoLEDOn = true;
      }
    }
} 
