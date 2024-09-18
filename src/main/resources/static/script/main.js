
// Hàm cập nhật thanh tiến trình nhiệt độ
function updateTemperatureProgressBar(temperature) {
  let heightPercentage = (temperature / 100) * 100;

  const tempBarFill = document.getElementById('tempBarFill');
  const temperatureDisplay = document.getElementById('temperatureDisplay');

  tempBarFill.style.height = heightPercentage + '%';
  temperatureDisplay.textContent = temperature + '°C';

  if(temperature > 40) {
    tempBarFill.style.backgroundColor = '#FF3632';
  } else {
    tempBarFill.style.backgroundColor = '#f8d7da';
  }
}

// Hàm cập nhật thanh tiến trình độ ẩm
function updateHumidityProgressBar(humidity) {
  let heightPercentage = (humidity / 100) * 100;

  const humidBarFill = document.getElementById('humidBarFill');
  const humidityDisplay = document.getElementById('humidityDisplay');

  humidBarFill.style.height = heightPercentage + '%';
  humidityDisplay.textContent = humidity + '%';

  if(humidity > 70) {
    humidBarFill.style.backgroundColor = '#01579B';
  } else {
    humidBarFill.style.backgroundColor = '#d1ecf1';
  }

}

// Hàm cập nhật thanh tiến trình ánh sáng
function updateLightProgressBar(lux) {
  let heightPercentage = (lux / 1000) * 100;

  const lightBarFill = document.getElementById('lightBarFill');
  const lightDisplay = document.getElementById('lightDisplay');

  lightBarFill.style.height = heightPercentage + '%';
  lightDisplay.textContent = lux + ' lux';

  if(lux > 800) {
    lightBarFill.style.backgroundColor = '#ffb400';
  } else {
    lightBarFill.style.backgroundColor = '#cce5ff';
  }
}

// Hàm lấy dữ liệu từ API và cập nhật thanh tiến trình
function fetchSensorData() {
  fetch('/api/sensor/latest')  // Thay thế đường dẫn API với API thực tế của bạn
    .then(response => response.json())
    .then(data => {
      // Cập nhật các thanh tiến trình với dữ liệu từ API
      updateTemperatureProgressBar(data.temperature);  // Cập nhật nhiệt độ
      updateHumidityProgressBar(data.humidity);        // Cập nhật độ ẩm
      updateLightProgressBar(data.light);              // Cập nhật ánh sáng
    })
    .catch(error => console.error('Error fetching sensor data:', error));
}

// Gọi hàm fetchSensorData() mỗi khi trang tải hoặc cập nhật theo chu kỳ
document.addEventListener('DOMContentLoaded', function() {
  fetchSensorData();  // Lấy dữ liệu khi trang tải

  // Tùy chọn: Lấy dữ liệu mới sau mỗi vài giây (ví dụ, mỗi 10 giây)
  setInterval(fetchSensorData, 1000);  // Lấy dữ liệu mỗi 10 giây
});
