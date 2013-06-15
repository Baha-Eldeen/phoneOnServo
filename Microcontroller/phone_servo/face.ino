void handleFace(){
      // if face is out of range, move servo toward face. Note the range return by Android is around -800 to 800 
      if(faceLocation  > 350){
            moveServo(1600); // move clockwise but slower speed than the value of CLOCKWISE
      }  
      else if (faceLocation  < -350){
            moveServo(1430); //  move counterclockwise but slower speed than the value of COUNTERCLOCKWISE
      }
      faceFound = false; // clear flag
}
