#include <ESP8266WiFi.h>
#include <DHT.h>

#define DHTPIN 2 //D4
#define DHTTYPE DHT22
#define TIMEOUT 1000

const char* ssid = ".";
const char* password = ".";

#define MAX_MOISTURE 999

DHT dht(DHTPIN, DHTTYPE);
WiFiServer server(80);

void setup() {
  pinMode(A0, INPUT);
  dht.begin();
  Serial.begin(115200);

  WiFi.begin(ssid, password);

  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("Connected");
  Serial.print("IP: ");
  Serial.println(WiFi.localIP());

  server.begin(); //add check status!!
  Serial.println("Server started");
}

void loop() {
  WiFiClient client = server.available();
  if(!client)
    return;
  Serial.println("new client");
  int cnt = 0;
  while (!client.available() && cnt <= TIMEOUT) {
    cnt++;
    delay(1);
  }
  if (cnt > 1000) {
    delay(1);
    return;
  }
  Serial.println(cnt);
  client.flush();
  
  
  float hum = dht.readHumidity();
  float temp = dht.readTemperature();
  float moist = analogRead(A0);
  moist = 100 * (MAX_MOISTURE - moist) / MAX_MOISTURE;

  client.print(moist);
  client.print(";");
  client.print(temp);
  client.print(";");
  client.print(hum);
  
  delay(1);
  client.stop();
}
