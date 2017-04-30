#include <SPI.h>
#include <Ethernet.h>
#include <NewSoftSerial.h>
#include <string.h>   



//addresss
byte mac[] = {  
  
 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
byte ip[] = {
 192,168,1,177 };                   
byte server[] = {
 173,236,156,236 };                   

String content="                    "; 
int stringPos=0;                        
char inChar=0;                   

char incoming_char=0;    

NewSoftSerial cell(2,3);  

// Ethernet library

void setup() {

 Ethernet.begin(mac, ip);
   
 delay(1000);
 
 Serial.begin(9600);
 delay(1000);
 //start
 cell.begin(9600); 
}

void loop()
{
// Ethernet libr
 Client client =Client(server, 80); //
 
   
 if(client.connect()){
   Serial.println("connected");
   client.println("GET /sms/out.php HTTP/1.1");
   client.println("Host: insight.no.com");
   client.println();
 }
 else{
      
   Serial.println("Error connecting");
 }

 delay(1000);

  if (client.available()) {
    readAgain:
    inChar=client.read();
    if(inChar!='`'){
      goto readAgain;}
      Serial.println("found message start");
  }
 
 delay(1000);
 // reads data
  if (client.available()) {
    getMore:
    inChar=client.read();
if(inChar!='~'){
    content[stringPos]=inChar;
    stringPos++;
   goto getMore;}
  }
 
// MESSAGE
Serial.println(content);


 if (!client.connected()) {
   Serial.println();
   Serial.println("disconnecting");
 }

 inChar=-1;
 content="                    ";
 client.stop();
 stringPos=0;
 

   while(cell.available())
 {
   incoming_char=cell.read();
   Serial.print(incoming_char); 
 }
 delay(2500);

}
