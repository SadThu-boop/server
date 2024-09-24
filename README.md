# IoT Project
Dự án IoT trên trường xây dựng kiểm soát nhà thông minh thu thập thông tin về nhiệt độ, độ ẩm và ánh sáng trong nhà. Điều khiển ba thiết bị: quạt, điều hòa và bóng đèn. Dự án được chia làm 2 phần:
- Phần cứng
- Phần mềm
  
![IoT](https://i.imgur.com/xsoRQnb.jpg)


## Phần mềm
![home](https://i.imgur.com/c93NCOI.png)

**Dự án này sử dụng công nghệ:**
- Java(JDK 21)
- Spring boot
- Thymeleaf/CSS
- C++ (Arduino IDE)
- JS
- MySQL
- Bootstrap
- MQTT mosquitto




## Phần cứng
Dự án sử dụng:
- ESP8266
- DHT11
- BH1750 
- LED 
- Dây nối

 Sử dụng Arduino IDE để nạp code cho 3 thiết bị lấy thông tin về nhiệt độ, ánh sáng và độ ẩm của phòng.
- ESP8266
  
 ![ESP8266](https://i.imgur.com/dfNIDLRb.jpg)

 - DHT11
   
 ![DHT11](https://i.imgur.com/0BTInuZb.jpg)

 - BH1750
   
 ![BH1750](https://i.imgur.com/LJMSY37b.jpg)

 
## Cài đặt

Cài đặt dự án thông qua github

```cmd
  git clone https://github.com/SadThu-boop/server.git
  cd server
```
config pom file của dự án 

Sử dụng code final.ino để nạp chương trình vào ESP8266 thông qua Arduino IDE
    
