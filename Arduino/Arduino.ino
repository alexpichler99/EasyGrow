#include <ESP8266WiFi.h>
#include <DHT.h>

#include <FS.h>
#include <Time.h>



#define DHTPIN 2 //D4
#define DHTTYPE DHT22
#define TIMEOUT 1000

#define LOG_FILE "/data.log"

#define LOG_ENTRY_INTERVAL 6 //hours
#define MAX_LOG_ENTRIES 120 //30 days

const char* ssid = "";
const char* password = "";

const int moisturePin = A0;

unsigned long hrCnt = 0;
unsigned long hr = hour();
  
#define MAX_MOISTURE 999

DHT dht(DHTPIN, DHTTYPE);
WiFiServer server(80);

void setup() {
  pinMode(moisturePin, INPUT);
  dht.begin();
  Serial.begin(115200);

  SPIFFS.begin();
  Serial.println("FS mounted");

  SPIFFS.format();
  Serial.println("FS formatted");
  
  FSInfo info;
  if(SPIFFS.info(info)) {
    Serial.print("Total: ");
    Serial.print(info.totalBytes);
    Serial.println("bytes");

    Serial.print("Used: ");
    Serial.print(info.usedBytes);
    Serial.println("bytes");

    Serial.print("Max open files: ");
    Serial.println(info.maxOpenFiles);
  }
  checkLogFile();

  WiFi.begin(ssid, password);

  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("Connected");
  Serial.print("IP: ");
  Serial.println(WiFi.localIP());

  server.begin();
  Serial.println("Server started");

  Dir dir = SPIFFS.openDir("/");
  while (dir.next()) {
    Serial.print(dir.fileName());
    File f = dir.openFile("r");
    Serial.println(f.size());
  }
  addFirstLog();
  
  //createTemplateFile();
}

void addFirstLog() {
  float hum = dht.readHumidity();
  float temp = dht.readTemperature();
  float moist = analogRead(A0);
  moist = 100 * (MAX_MOISTURE - moist) / MAX_MOISTURE;
  addLog(moist, temp, hum);
}

void createTemplateFile() {
  File file = SPIFFS.open(LOG_FILE, "a+");
  for (int i = 0; i < 120; i++) {
    file.print(i);
    file.print(';');
    file.print(random(0, 100));
    file.print(';');
    file.print(random(-15, 50));
    file.print(';');
    file.println(random(0, 100));
  }
  file.close();
}

void checkLogFile() {
  if (!SPIFFS.exists(LOG_FILE)) {
    File f = SPIFFS.open(LOG_FILE, "w");
    f.close();
    Serial.println("log file created");
  }
}

//timestamp;moist;temp;hum
bool addLog(float moist, float temp, float hum) {
  File f = SPIFFS.open(LOG_FILE, "r");
  if (SPIFFS.exists("/tmp.tmp"))
    SPIFFS.remove("/tmp.tmp");
  File tf = SPIFFS.open("/tmp.tmp", "a+");
  hrCnt += 6;
  tf.print(hrCnt);
  tf.print(';');
  tf.print(moist);
  tf.print(';');
  tf.print(temp);
  tf.print(';');
  tf.println(hum);

  int lineCnt = 1;
  while(f.available()) {
    char c = f.read();
    if (c == '\n')
      lineCnt++;
    tf.write(c);
    if (lineCnt >= MAX_LOG_ENTRIES)
      break;
  }
  tf.close();
  f.close();
  SPIFFS.remove(LOG_FILE);
  SPIFFS.rename("/tmp.tmp", LOG_FILE);
}

void sendLog(WiFiClient client) {
  File f = SPIFFS.open(LOG_FILE, "r");
  while(f.available())
    client.print((char)f.read());
  f.close();
}

void sendCurrentValues(WiFiClient client) {
  float hum = dht.readHumidity();
  float temp = dht.readTemperature();
  float moist = analogRead(A0);
  moist = 100 * (MAX_MOISTURE - moist) / MAX_MOISTURE;
  client.print(moist);
  client.print(";");
  client.print(temp);
  client.print(";");
  client.println(hum);
}

void loop() {
  //add every 6 hours one log
  if (hr + 6 <= hour()) {
    float hum = dht.readHumidity();
    float temp = dht.readTemperature();
    float moist = analogRead(A0);
    moist = 100 * (MAX_MOISTURE - moist) / MAX_MOISTURE;
    addLog(moist, temp, hum);
    hr = hour();
  }
  
  WiFiClient client = server.available();
  if(!client)
    return;
  Serial.println("new client");
  int cnt = 0;
  while (!client.available() && cnt <= TIMEOUT) {
    cnt++;
    delay(1);
  }
  if (cnt > TIMEOUT) {
    delay(1);
    Serial.println("Client timed out");
    return;
  }   
  Serial.println(cnt);

  String in = client.readStringUntil('\n');
  Serial.println(in);
  client.flush();
  if (in[3] == '1')
    sendLog(client);
  else
    sendCurrentValues(client);  
  
  delay(1);
  client.stop();
}
