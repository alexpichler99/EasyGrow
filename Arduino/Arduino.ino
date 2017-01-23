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
  
  createTemplateFile();
}

void addFirstLog() {
  float hum = dht.readHumidity();
  float temp = dht.readTemperature();
  float moist = analogRead(A0);
  moist = 100 * (MAX_MOISTURE - moist) / MAX_MOISTURE;
  addLog(moist, temp, hum);
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
  else if (in[3] == '3')
    client.print("hello");
  else
    sendCurrentValues(client);
  delay(1);
  client.stop();
}

void createTemplateFile() {
addLog(50,20,45);
addLog(50,21,45);
addLog(52,22,44);
addLog(54,22,43);
addLog(56,21,42);
addLog(57,20,41);
addLog(60,20,40);
addLog(65,19,40);
addLog(65,18,40);
addLog(65,17,39);
addLog(65,16,38);
addLog(65,15,37);
addLog(64,14,36);
addLog(64,12,36);
addLog(62,10,36);
addLog(62,8,36);
addLog(60,7,38);
addLog(58,6,40);
addLog(55,5,42);
addLog(54,2,43);
addLog(53,1,44);
addLog(50,0,45);
addLog(49,-1,46);
addLog(49,-2,47);
addLog(49,-3,48);
addLog(49,-3,49);
addLog(49,-4,50);
addLog(47,-5,51);
addLog(46,-5,53);
addLog(45,-5,55);
addLog(44,-5,57);
addLog(43,-5,59);
addLog(42,-5,60);
addLog(41,-4,60);
addLog(40,-3,62);
addLog(39,-2,62);
addLog(38,-1,60);
addLog(37,0,59);
addLog(36,1,58);
addLog(35,2,57);
addLog(35,3,56);
addLog(35,4,55);
addLog(35,5,53);
addLog(45,6,53);
addLog(46,10,53);
addLog(47,11,53);
addLog(48,12,52);
addLog(49,13,52);
addLog(50,14,52);
addLog(51,15,51);
addLog(52,16,51);
addLog(52,17,51);
addLog(51,18,50);
addLog(50,19,50);
addLog(50,21,48);
addLog(50,22,48);
addLog(49,22,48);
addLog(49,22,46);
addLog(49,21,45);
addLog(49,21,44);
addLog(50,22,45);
addLog(51,23,46);
addLog(52,24,47);
addLog(53,25,48);
addLog(53,26,50);
addLog(54,28,51);
addLog(55,30,50);
addLog(52,28,49);
addLog(50,26,45);
addLog(47,25,44);
addLog(46,23,46);
addLog(44,22,47);
addLog(42,21,45);
addLog(39,19,41);
addLog(40,18,43);
addLog(41,17,42);
addLog(38,15,41);
addLog(37,14,39);
addLog(35,12,35);
addLog(33,10,34);
addLog(32,8,33);
addLog(30,5,32);
addLog(34,3,35);
addLog(35,0,34);
addLog(35,-1,35);
addLog(33,-3,37);
addLog(31,-5,38);
addLog(27,-6,40);
addLog(25,-8,42);
addLog(22,-10,45);
addLog(26,-9,46);
addLog(30,-5,49);
addLog(35,0,50);
addLog(36,2,55);
addLog(37,3,57);
addLog(38,5,58);
addLog(40,7,60);
addLog(41,8,61);
addLog(42,9,62);
addLog(44,12,61);
addLog(45,13,62);
addLog(47,15,61);
addLog(49,16,60);
addLog(50,18,59);
addLog(52,20,61);
addLog(54,22,63);
addLog(55,23,61);
addLog(56,21,59);
addLog(58,20,56);
addLog(60,19,52);
addLog(65,18,53);
addLog(66,20,49);
addLog(70,22,45);
addLog(65,19,49);
addLog(62,17,55);
addLog(58,15,60);
addLog(55,12,65);
addLog(50,11,70);
addLog(42,9,75);
addLog(40,10,72);
}

