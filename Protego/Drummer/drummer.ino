
#include <VirtualWire.h>
#include "SIM900.h"
#include <SoftwareSerial.h>
#include "sms.h"
#include "DHT.h"
#include <SFE_BMP180.h>
#include <Wire.h>
#include <string.h>
#include <avr/sleep.h>
#include <avr/power.h>
#include "LowPower.h"

int led = 12;


void setup()
{
Serial.begin(9600);   
Serial.println("setup");
    
vw_setup(2000);    
vw_set_rx_pin(2);   
vw_rx_start();  
pinMode(led, OUTPUT);
}

void loop()
{
    uint8_t buf[VW_MAX_MESSAGE_LEN];
    uint8_t buflen = VW_MAX_MESSAGE_LEN;

    if (vw_get_message(buf, &buflen)) // Non-blocking
    {
   
        digitalWrite(13, true); // Flash a light to show received good message
   // Message with a good checksum received, dump it.
   for (int i = 0; i < buflen; i++)
     {
        Serial.print(buf);//print received command
             if(buf == '1')//if button 1 is pressed.... i.e.forward buton
              {
                digitalWrite(led, HIGH);
              }
           }
           delay(1000);
           digitalWrite(led, LOW);
           
    }
}

// Phone N
char number[20]= "???????????"; 


int savedPinState = LOW;

void setup()
{
  
  Serial.begin(9600);

  Serial.println("SMS Messages Sender");

   if (gsm.begin(9600))
   {
     Serial.println("\nstatus=READY");
   }
   
   else
   {
     Serial.println("\nstatus=IDLE");
   }
   
}

void loop()
{
if (digitalRead(12) != savedPinState) {
            if (digitalRead(12) == HIGH) {
                Serial.print("Message to mobile number: ");
                Serial.println(number);

               // *************SMS*************
              Serial.println("SENDING");
              Serial.println();
              Serial.println("Message:");
              Serial.println(txtMsg);

              // send the message
              sms.SendSMS(number,txtMsg); 
              Serial.println();
              Serial.println("Complete!!!");
              savedPinState = HIGH;
           }
           else if (digitalRead(12) == LOW) {
                savedPinState = LOW;
            }
 }
else if(digitalRead(12) == savedPinState)
{
            
 }
}
