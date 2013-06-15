int pin1 = 6, pin2 = 7, pin3 = 8;
void initLED(){
    pinMode(pin1, OUTPUT); 
    pinMode(pin2, OUTPUT);
    pinMode(pin3, OUTPUT);
    changeLED(ledState); 
}

void changeLED(int color){
    switch(color){
        case OFF:
            digitalWrite(pin1, HIGH);
            digitalWrite(pin2, HIGH);
            digitalWrite(pin3, HIGH);
            break;
        case RED:
            digitalWrite(pin1, LOW);
            digitalWrite(pin2, HIGH);
            digitalWrite(pin3, HIGH);
            break;
        case BLUE:
            digitalWrite(pin1, HIGH);
            digitalWrite(pin2, LOW);
            digitalWrite(pin3, HIGH);
            break;
        case PINK:
            digitalWrite(pin1, LOW);
            digitalWrite(pin2, LOW);
            digitalWrite(pin3, HIGH);
            break;
        case GREEN:
            digitalWrite(pin1, HIGH);
            digitalWrite(pin2, HIGH);
            digitalWrite(pin3, LOW);
            break;
        case YELLOW:
            digitalWrite(pin1, LOW);
            digitalWrite(pin2, HIGH);
            digitalWrite(pin3, LOW);
            break;
        case CYAN:
            digitalWrite(pin1, HIGH);
            digitalWrite(pin2, LOW);
            digitalWrite(pin3, LOW);
            break;
        case WHITE:
            digitalWrite(pin1, LOW);
            digitalWrite(pin2, LOW);
            digitalWrite(pin3, LOW);
            break;
    } 
}
