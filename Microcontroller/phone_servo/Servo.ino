int servoPin = 11;

void initServo(){
  pinMode(servoPin,OUTPUT);
  moveServo(STOP);
}

// This sends a pulse for value microseconds
void moveServo(int value){
  cli();   // disable interrupts
  digitalWrite(servoPin, HIGH);
  delayMicroseconds(value);
  digitalWrite(servoPin, LOW);
  delay(20);
  sei();   // enable interrupts
}

















/*#include <Servo.h> 
 
 Servo myservo; 
 int servoPin = 11;
 
 void turnServo(int value, int delayTime){
 myservo.attach(servoPin);
 myservo.writeMicroseconds(value);
 delay(delayTime);
 myservo.detach();
 }*/

