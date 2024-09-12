// Example data - 20 rows
//const sensorData = [
//    { id:'1', temperature: 24.1, humidity: 61, light: 72, timestamp: '2024-08-28 14:00:00' },
//    { id:'1', temperature: 24.3, humidity: 60, light: 70, timestamp: '2024-08-28 14:15:00' },
//    { id:'1', temperature: 24.5, humidity: 59, light: 75, timestamp: '2024-08-28 14:30:00' },
//    { id:'1', temperature: 24.8, humidity: 62, light: 80, timestamp: '2024-08-28 14:45:00' },
//    { id:'1', temperature: 25.0, humidity: 64, light: 68, timestamp: '2024-08-28 15:00:00' },
//    { id:'1', temperature: 24.6, humidity: 63, light: 76, timestamp: '2024-08-28 15:15:00' },
//    { id:'1', temperature: 24.7, humidity: 61, light: 73, timestamp: '2024-08-28 15:30:00' },
//    { id:'1', temperature: 24.9, humidity: 60, light: 79, timestamp: '2024-08-28 15:45:00' },
//    { id:'1', temperature: 25.1, humidity: 65, light: 77, timestamp: '2024-08-28 16:00:00' },
//    { id:'1', temperature: 25.3, humidity: 66, light: 74, timestamp: '2024-08-28 16:15:00' },
//    { id:'1', temperature: 25.2, humidity: 64, light: 70, timestamp: '2024-08-28 16:30:00' },
//    { id:'1', temperature: 25.4, humidity: 67, light: 72, timestamp: '2024-08-28 16:45:00' },
//    { id:'1', temperature: 25.5, humidity: 68, light: 69, timestamp: '2024-08-28 17:00:00' },
//    { id:'1', temperature: 25.6, humidity: 66, light: 71, timestamp: '2024-08-28 17:15:00' },
//    { id:'1', temperature: 25.7, humidity: 67, light: 73, timestamp: '2024-08-28 17:30:00' },
//    { id:'1', temperature: 25.8, humidity: 65, light: 78, timestamp: '2024-08-28 17:45:00' },
//    { id:'1', temperature: 26.0, humidity: 64, light: 74, timestamp: '2024-08-28 18:00:00' },
//    { id:'1', temperature: 26.1, humidity: 66, light: 75, timestamp: '2024-08-28 18:15:00' },
//    { id:'1', temperature: 26.2, humidity: 68, light: 76, timestamp: '2024-07-26 18:30:00' },
//    { id:'1', temperature: 26.3, humidity: 69, light: 77, timestamp: '2024-08-27 18:45:00' }
//];

// Xóa dữ liệu giả lập
// const sensorData = [...];

let rowsPerPage = 10;
let currentPage = 1;
let filteredData = [];  // Dữ liệu sẽ được lấy từ API

// Hàm để hiển thị dữ liệu lên bảng
function displayTableData(page) {
    const tableBody = document.getElementById('sensorDataTable');
    tableBody.innerHTML = '';

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedData = filteredData.slice(start, end);

    paginatedData.forEach(row => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${row.id}</td>
            <td>${row.temperature}</td>
            <td>${row.humidity}</td>
            <td>${row.light}</td>
            <td>${row.timestamp}</td>
        `;
        tableBody.appendChild(tr);
    });
}

// Thiết lập phân trang
function setupPagination() {
    const totalPages = Math.ceil(filteredData.length / rowsPerPage);
    const paginationControls = document.getElementById('paginationControls');
    paginationControls.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        const li = document.createElement('li');
        li.className = 'page-item';
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener('click', function () {
            currentPage = i;
            displayTableData(currentPage);
            updatePaginationControls();
        });
        paginationControls.appendChild(li);
    }
    updatePaginationControls();
}

function updatePaginationControls() {
    const paginationControls = document.getElementById('paginationControls');
    Array.from(paginationControls.children).forEach((li, index) => {
        li.classList.remove('active');
        if (index + 1 === currentPage) {
            li.classList.add('active');
        }
    });
}

function changePageSize() {
    rowsPerPage = parseInt(document.getElementById('pageSize').value);
    currentPage = 1; // Quay lại trang đầu tiên
    displayTableData(currentPage);
    setupPagination();
}

function filterByTime() {
    const searchTime = document.getElementById('searchByTime').value.trim();

    if (searchTime) {
        filteredData = sensorData.filter(row => row.timestamp.includes(searchTime));
    } else {
        filteredData = [...sensorData];  // Nếu không có tìm kiếm, reset dữ liệu
    }

    currentPage = 1;
    displayTableData(currentPage);
    setupPagination();
}

function sortTable(column) {
    const sortIcon = document.getElementById(`${column}SortIcon`);
    const isAscending = sortIcon.classList.contains('bi-arrow-down');

    filteredData.sort((a, b) => {
        if (a[column] > b[column]) return isAscending ? 1 : -1;
        if (a[column] < b[column]) return isAscending ? -1 : 1;
        return 0;
    });

    // Đảo biểu tượng mũi tên
    sortIcon.classList.toggle('bi-arrow-down');
    sortIcon.classList.toggle('bi-arrow-up');

    displayTableData(currentPage);
}

// Hàm để lấy dữ liệu từ API Spring Boot
function fetchDataFromAPI() {
    fetch('/api/sensor')  // Đảm bảo đường dẫn khớp với API
        .then(response => response.json())
        .then(data => {
            filteredData = data;  // Gán dữ liệu API vào filteredData
            displayTableData(currentPage);  // Hiển thị dữ liệu
            setupPagination();  // Thiết lập phân trang
        })
        .catch(error => console.error('Error fetching sensor data:', error));
}

// Gọi hàm fetchDataFromAPI khi tải trang
document.addEventListener('DOMContentLoaded', function () {
    fetchDataFromAPI();
});

