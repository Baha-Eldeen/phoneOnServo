void serialEvent() {
  while (Serial.available()) {
    char inChar = (char)Serial.read(); 
    switch(inChar){
        case 'B':  // bluetooth is connected 
            changeLED(GREEN);
            ledState = GREEN;
        break;
        
        case 'C':  // camera is on 
            changeLED(BLUE);
            ledState = BLUE;
        break;
        
        case 'D': // camera is off
            changeLED(GREEN);
            ledState = GREEN;
        break;
        
        case 'M': // capturing a picture
            changeLED(RED);
            delay(100);
            changeLED(ledState);
        break;
        
        case 'X': // video started
            isVideoOn = true;
        break;
        
        case 'Y': // video stopped
            isVideoOn = false;
            changeLED(ledState);
        break;
        
        case 'F':
            for (int i=0; i<2; i++){ // get two bytes for face location
                      while (!Serial.available()); // wait till serial is available
                      faceBuffer[i] = Serial.read();
                }
            faceLocation = faceBuffer[1] <<8 | faceBuffer[0];
            faceFound = true;
        break;
        
        case 'L': // Android closed, reset everyting
            isFaceDetectionEnabled = false; // reset variable 
            changeLED(YELLOW);
            ledState = YELLOW;
        break;
      
    }
  }
}
