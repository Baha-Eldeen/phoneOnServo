#include <IRremote.h>


int RECV_PIN = 9;
IRrecv irrecv(RECV_PIN);
decode_results results;

void initIR(){
    irrecv.enableIRIn(); // Start the receiver
}

void readIR(){
    if(irrecv.decode(&results)){
        // This switch is used to define the states we want the infrared to have
        switch(results.value){
            case RIGHT:
                state = RIGHT;  // Right button pressed
                break;
            case LEFT:
                state = LEFT;  // Left button pressed
                break; 
            case PICTURE:
                state = PICTURE;  // Scene button pressed
                break;
            case CAMERAON:
                state = CAMERAON;  // Power button pressed
                break;
            case FACE:
                state = FACE;  // Favorites button pressed
                break;
            case VIDEO:
                state = VIDEO;  // Display button pressed
                break;
            case NOTHING:
                // do nothing
                break;
            case DONOTHING:
                // do nothing
                break;
            default:
                state = NOTHING;
                break;
        }
        handleIR(state); // make an action besed on the pressed button
        irrecv.resume(); // Receive the next value
    }
}

int handleIR(int value){
    switch(value){
        case RIGHT:
            moveServo(COUNTERCLOCKWISE);
            break;
        case LEFT:
            moveServo(CLOCKWISE);
            break; 
        case PICTURE:
            Serial.write('P');  // send a 'P' to Android over Bluetooth to take a picture
            delay(200);
            break; 
        case VIDEO:
            Serial.write('V');  // send a 'V' to Android over Bluetooth to start/stop a video
            delay(200);
            break;
        case CAMERAON:
            changeLED(OFF); 
            Serial.write('O');  // send a 'O' to Android over Bluetooth to turn camera on or off 
            delay(1000); // wait till camera activity open or close
            break; 
        case FACE:
            if(ledState == BLUE || ledState == CYAN){ // if camera is on
              if(isFaceDetectionEnabled){ // turn off face detection
                changeLED(BLUE);
                ledState = BLUE;
                isFaceDetectionEnabled = false;
              }else{ // turn on face detection 
                changeLED(CYAN);
                ledState = CYAN;
                isFaceDetectionEnabled = true;
              }
            }
            delay(200);
            break;
     }
}

