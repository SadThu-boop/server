const ctx = document.getElementById('combinedChart');

// Function to get the current time label (HH:MM:SS)
function getCurrentTimeLabel() {
  const now = new Date();
  return now.toLocaleTimeString();
}

// Create the combined chart with temperature, humidity, and light intensity
const combinedChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: Array(7).fill(getCurrentTimeLabel()),
    datasets: [
      {
        label: 'Temperature (°C)',
        data: Array(7).fill(0),  // Initialize with empty data
        borderWidth: 1,
        borderColor: 'rgba(255, 99, 132, 1)',
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
      },
      {
        label: 'Humidity Levels (%)',
        data: Array(7).fill(0),  // Initialize with empty data
        borderWidth: 1,
        borderColor: 'rgba(54, 162, 235, 1)',
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
      },
      {
        label: 'Light Intensity (lux)',
        data: Array(7).fill(0),  // Initialize with empty data
        borderWidth: 1,
        borderColor: 'rgba(255, 206, 86, 1)',
        backgroundColor: 'rgba(255, 206, 86, 0.2)',
      },
    ],
  },
  options: {
    scales: {
      y: {
        beginAtZero: true,
      },
    },
  },
});

// Function to fetch sensor data from the API
function fetchSensorData() {
  fetch('/api/sensor/latest')  // Replace with your actual API endpoint
    .then(response => response.json())
    .then(data => {
      // Update the chart with new data from the API
      updateCombinedChart(data);
    })
    .catch(error => console.error('Error fetching sensor data:', error));
}

// Function to update the chart with new data
function updateCombinedChart(sensorData) {
  const currentTimeLabel = getCurrentTimeLabel();

  // Update labels (time)
  combinedChart.data.labels.push(currentTimeLabel);
  combinedChart.data.labels.shift();

  // Update temperature data
  combinedChart.data.datasets[0].data.push(sensorData.temperature);
  combinedChart.data.datasets[0].data.shift();

  // Update humidity data
  combinedChart.data.datasets[1].data.push(sensorData.humidity);
  combinedChart.data.datasets[1].data.shift();

  // Update light intensity data
  combinedChart.data.datasets[2].data.push(sensorData.light);
  combinedChart.data.datasets[2].data.shift();

  combinedChart.update();
}

// Fetch new data every 2 seconds
setInterval(fetchSensorData, 5000);
