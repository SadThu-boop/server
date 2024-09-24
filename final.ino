#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <DHT.h>
#include <BH1750.h>

BH1750 lightMeter(0x23);

// Cấu hình mạng WiFi
const char* ssid = "Hm";
const char* password = "88888888";

// Cấu hình MQTT
const char* mqtt_server = "172.20.10.2";
const char* mqtt_user = "hminh"; 
const char* mqtt_password = "B21DCCN522"; 
const char* sensor_topic = "data/sensor";  // Topic để gửi dữ liệu BH1750 và DHT11
const char* device_topic = "home/device/+";

// Cấu hình chân cảm biến DHT11 và LED
#define DHTPIN D4   // Chân kết nối cảm biến DHT11
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

const int ledFan = D7;    // LED tượng trưng cho quạt
const int ledAC = D8;     // LED tượng trưng cho điều hòa
const int ledLight = D3;  // LED tượng trưng cho bóng đèn

WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[100];  // Tăng kích thước bộ đệm để chứa dữ liệu từ cả hai cảm biến
int value = 0;

// Hàm kết nối WiFi
void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

// Hàm xử lý khi nhận thông điệp từ MQTT
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  String message = "";
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
  Serial.println(message);

  // Điều khiển LED dựa trên thông điệp nhận được
  if (String(topic) == "home/device/fan") {
    digitalWrite(ledFan, message == "on" ? HIGH : LOW);
  } else if (String(topic) == "home/device/ac") {
    digitalWrite(ledAC, message == "on" ? HIGH : LOW);
  } else if (String(topic) == "home/device/light") {
    digitalWrite(ledLight, message == "on" ? HIGH : LOW);
    
  }
}


// Hàm kết nối tới MQTT broker và đăng ký các topic
void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect("ESP8266Client", mqtt_user, mqtt_password)) {
      Serial.println("connected");
      client.subscribe(device_topic); // Đăng ký lắng nghe điều khiển thiết bị
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void setup() {
  pinMode(ledFan, OUTPUT);
  pinMode(ledAC, OUTPUT);
  pinMode(ledLight, OUTPUT);

  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  dht.begin();

  Wire.begin(D1,D2); //SDA, SCL
  
  if(lightMeter.begin()) {
    Serial.println("BH1750 connected");
  } else {
    Serial.println("BH1750 not connected");
  }
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  // Đẩy dữ liệu cảm biến DHT11 và BH1750 lên MQTT mỗi 10 giây
  long now = millis();
  if (now - lastMsg > 10000) {
    lastMsg = now;

    // Đọc dữ liệu từ DHT11
    float humidity = dht.readHumidity();
    float temperature = dht.readTemperature();
    
    // Đọc dữ liệu từ BH1750
    float light = lightMeter.readLightLevel();

    // Kiểm tra nếu dữ liệu không hợp lệ
    if (isnan(humidity) || isnan(temperature) || light == -1) {
      Serial.println("Failed to read from sensors!");
      return;
    }

    // Tạo payload với cả dữ liệu từ DHT11 và BH1750
    String sensorPayload = String("{\"temperature\":") + temperature + 
                           ",\"humidity\":" + humidity + 
                           ",\"light\":" + light + "}";

    // Chuyển payload thành chuỗi C-style và gửi lên MQTT
    sensorPayload.toCharArray(msg, 100);
    client.publish(sensor_topic, msg);

    Serial.print("Temperature: ");
    Serial.print(temperature);
    Serial.print(" °C, Humidity: ");
    Serial.print(humidity);
    Serial.print(" %, Light: ");
    Serial.print(light);
    Serial.println(" lx");
  }
}
