const hamBurger = document.querySelector(".toggle-btn");

hamBurger.addEventListener("click", function () {
  document.querySelector("#sidebar").classList.toggle("expand");
});

document.querySelector('.sidebar-item a[href="#"]').addEventListener('click', function(event) {
  event.preventDefault();
  showUserInfo();
});

function showUserInfo() {
document.getElementById('blurBackground').classList.remove('d-none');
}

function closeUserInfo() {
document.getElementById('blurBackground').classList.add('d-none');
}


function showSelectedChart() {
  // Ẩn tất cả các biểu đồ
  document.getElementById('temperatureChart').classList.add('d-none');
  document.getElementById('humidityChart').classList.add('d-none');
  document.getElementById('lightChart').classList.add('d-none');

  // Hiển thị biểu đồ được chọn
  var selectedChart = document.getElementById('chartSelector').value;
  if (selectedChart === 'temperature') {
      document.getElementById('temperatureChart').classList.remove('d-none');
  } else if (selectedChart === 'humidity') {
      document.getElementById('humidityChart').classList.remove('d-none');
  } else if (selectedChart === 'light') {
      document.getElementById('lightChart').classList.remove('d-none');
  }
}

// Hàm cập nhật thanh tiến trình nhiệt độ
function updateTemperatureProgressBar(temperature) {
  let heightPercentage = (temperature / 100) * 100;

  const tempBarFill = document.getElementById('tempBarFill');
  const temperatureDisplay = document.getElementById('temperatureDisplay');

  tempBarFill.style.height = heightPercentage + '%';
  temperatureDisplay.textContent = temperature + '°C';
}

// Hàm cập nhật thanh tiến trình độ ẩm
function updateHumidityProgressBar(percent) {
  let heightPercentage = (percent / 100) * 100;

  const humidBarFill = document.getElementById('humidBarFill');
  const humidityDisplay = document.getElementById('humidityDisplay');

  humidBarFill.style.height = heightPercentage + '%';
  humidityDisplay.textContent = percent + '%';
}

// Hàm cập nhật thanh tiến trình ánh sáng
function updateLightProgressBar(lux) {
  let heightPercentage = (lux / 1000) * 100;

  const lightBarFill = document.getElementById('lightBarFill');
  const lightDisplay = document.getElementById('lightDisplay');

  lightBarFill.style.height = heightPercentage + '%';
  lightDisplay.textContent = lux + ' lux';
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
  setInterval(fetchSensorData, 5000);  // Lấy dữ liệu mỗi 10 giây
});
