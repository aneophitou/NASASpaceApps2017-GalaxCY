#include "DHT.h"
#include <SFE_BMP180.h>
#include <Wire.h>
#include <VirtualWire.h>
#include <string.h>
#include <avr/sleep.h>
#include <avr/power.h>
#include "LowPower.h"
#include <SoftwareSerial.h>


#define DHTPIN 2    
#define DHTTYPE DHT22   

int rain = 0;
DHT dht(DHTPIN, DHTTYPE);
SFE_BMP180 pressure;

float vPow = 2.490;
float r1 = 47300;
float r2 = 9700;
unsigned long otherTime;
float battery;

void setup() {
    pinMode(2, INPUT);
  pinMode(3, INPUT);
  pinMode(4, INPUT);
  pinMode(5, INPUT);
  pinMode(6, INPUT);
  pinMode(7, INPUT);
  pinMode(8, INPUT);
  pinMode(9, INPUT);
  pinMode(10, INPUT);
  pinMode(11, INPUT);
  pinMode(12, OUTPUT);
  pinMode(13, INPUT);
  pinMode(A2, OUTPUT);
  pinMode(A3, INPUT);
  pinMode(A4, INPUT);
  pinMode(A5, INPUT);
  pinMode(A2,OUTPUT);
  analogReference(EXTERNAL);
  Serial.begin(4800);
  dht.begin();
  vw_setup(2000);
  if (pressure.begin()){
  }
  else
  {
    while(1);
  }
  pinMode(8, INPUT);
}

SoftwareSerial gps(4,3);

char dato=' ';

void setup()
{
 Serial.begin(115200);            
 gps.begin(9600); 
}


void loop()
{
  if(gps.available())
  {
    dato=gps.read();
    Serial.print(dato);
  }
}

void loop() {
  charge();
  float p = getPressure();
  char pbuf[20];
  dtostrf(p, 7, 2, pbuf);
  char pmsg[30];
  sprintf(pmsg, "%s %s", "h", pbuf);

  float h = dht.readHumidity();
  char hbuf[20];
  dtostrf(h, 7, 2, hbuf);
  char hmsg[20];
  sprintf(hmsg, "%s %s", "%" , hbuf);

  float t = dht.readTemperature();
  char tbuf[20];
  dtostrf(t, 7, 2, tbuf);
  char tmsg[20];
  sprintf(tmsg, "%s %s", "c", tbuf);
  
  char bbuf[20];
  dtostrf(battery, 7, 2, bbuf);
  char bmsg[20];
  sprintf(bmsg, "%s %s", "v", bbuf);

  int i = 8;
  while (i > 0){
    send(pmsg);
    delay(100);
    send(hmsg);
    delay(100);
    send(tmsg);
    delay(100);
    send(bmsg);
    delay(100);
    i--;
  }
  //digitalWrite(12, LOW);
  sleep();
}
void sleep() {
  for (int i = 0; i < 75; i++) {
  LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF); 
  }
}

void send (char *message)
{
  vw_send((uint8_t *)message, strlen(message));
  vw_wait_tx(); 
}

double getPressure()
{
  char status;
  double T,P,p0,a;
  status = pressure.startTemperature();
  if (status != 0)
  {
    delay(status);
    status = pressure.getTemperature(T);
    if (status != 0)
    {
      status = pressure.startPressure(3);
      if (status != 0)
      {
        delay(status);
        status = pressure.getPressure(P,T);
        if (status != 0)
        {
          return(P);
        }
        else Serial.println("error retrieving pressure measurement\n");
      }
      else Serial.println("error starting pressure measurement\n");
    }
    else Serial.println("error retrieving temperature measurement\n");
  }
  else Serial.println("error starting temperature measurement\n");
}


//void sleepTenMinutes()
//{
//  for (int i = 0; i < 75; i++) { 
//     LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF); 
//  }
//}

static void charge() {
  float voltage = GetVoltage();
  battery = voltage;
  if (voltage <= 6.4) {
    //digitalWrite(A2, LOW);
  }
  else {
    //digitalWrite(A2, HIGH);
  }
}

float GetVoltage(){
  //analogWrite(A2, 255);
  delay(500);
  float v = (analogRead(A3) * vPow) / 1023.0;
  v = v / (r2 / (r1 + r2));
  return v;
}




