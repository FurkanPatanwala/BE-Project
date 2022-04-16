#include <SoftwareSerial.h>
#include <Servo.h>

SoftwareSerial BT(3, 4); //Rx, Tx respectively 
String readdata;

Servo myServo; 

void setup() {
  BT.begin(9600);
  Serial.begin(9600);
  

  myServo.attach(2);
}

void loop() {
  while (BT.available()){      //Check if there is an available byte to read  
    delay(10);                  //Delay added to make thing stable
    char c = BT.read();         //Conduct a serial read
    readdata += c;              //build the string- "forward", "reverse", "left" and "right"
  }

  if (readdata.length() > 0){
    Serial.println(readdata);
    if(readdata == "1"){
      myServo.write(180);
      delay(7000);
      myServo.write(0);
    }
   readdata="";        //Reset the variable
 }
}
