const deviceData = [
    { id: '1', name: 'Air Conditioner', status: 'On', timestamp: '2024-08-28 14:00:00' },
    { id: '2', name: 'Fan', status: 'Off', timestamp: '2024-08-28 14:15:00' },
    { id: '3', name: 'Light Bulb', status: 'On', timestamp: '2024-08-28 14:30:00' },
    { id: '4', name: 'Air Conditioner', status: 'Off', timestamp: '2024-08-28 14:45:00' },
    { id: '5', name: 'Fan', status: 'On', timestamp: '2024-08-28 15:00:00' },
    { id: '6', name: 'Light Bulb', status: 'Off', timestamp: '2024-08-28 15:15:00' },
    { id: '7', name: 'Air Conditioner', status: 'On', timestamp: '2024-08-28 15:30:00' },
    { id: '8', name: 'Fan', status: 'Off', timestamp: '2024-08-28 15:45:00' },
    { id: '9', name: 'Light Bulb', status: 'On', timestamp: '2024-08-28 16:00:00' },
    { id: '10', name: 'Air Conditioner', status: 'Off', timestamp: '2024-08-28 16:15:00' },
    { id: '11', name: 'Fan', status: 'On', timestamp: '2024-08-28 16:30:00' },   
    { id: '12', name: 'Light Bulb', status: 'Off', timestamp: '2024-08-28 16:45:00' },
    { id: '13', name: 'Air Conditioner', status: 'On', timestamp: '2024-08-28 17:00:00' },
    { id: '14', name: 'Fan', status: 'Off', timestamp: '2024-08-28 17:15:00' },
    { id: '15', name: 'Light Bulb', status: 'On', timestamp: '2024-08-28 17:30:00' },
    { id: '16', name: 'Air Conditioner', status: 'Off', timestamp: '2024-08-28 17:45:00' },
    { id: '17', name: 'Fan', status: 'On', timestamp: '2024-08-28 18:00:00' },
    { id: '18', name: 'Light Bulb', status: 'Off', timestamp: '2024-08-28 18:15:00' },
    { id: '19', name: 'Air Conditioner', status: 'On', timestamp: '2024-08-28 18:30:00' },
    { id: '20', name: 'Fan', status: 'Off', timestamp: '2024-08-26 18:45:00' }
];

let rowsPerPage = 10; // Số lượng hàng trên mỗi trang mặc định
let currentPage = 1;
let filteredData = deviceData; // Data after filtering

function displayTableData(page) {
    const tableBody = document.getElementById('deviceDataTable');
    tableBody.innerHTML = '';

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedData = filteredData.slice(start, end);

    paginatedData.forEach((row) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${row.id}</td>
            <td>${row.name}</td>
            <td>${row.status}</td>
            <td>${row.timestamp}</td>
        `;
        tableBody.appendChild(tr);
    });
}

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

function filterByDevice() {
    const filterValue = document.getElementById('deviceFilter').value;
    if (filterValue === 'All') {
        filteredData = deviceData;
    } else {
        filteredData = deviceData.filter(row => row.name === filterValue);
    }
    currentPage = 1; // Reset to first page
    applyFilters();
}

function filterByTime() {
    const timeValue = document.getElementById('timeSearch').value;
    filteredData = deviceData.filter(row => row.timestamp.includes(timeValue));
    currentPage = 1; // Reset to first page
    applyFilters();
}

function applyFilters() {
    setupPagination();
    displayTableData(currentPage);
}

let isAscending = true; // Trạng thái sắp xếp (tăng dần hay giảm dần)

function sortTableByTimestamp() {
    const sortIcon = document.getElementById('timestampSortIcon');

    // Sắp xếp dữ liệu theo timestamp
    filteredData.sort((a, b) => {
        const dateA = new Date(a.timestamp);
        const dateB = new Date(b.timestamp);
        return isAscending ? dateA - dateB : dateB - dateA;
    });

    // Đổi trạng thái sắp xếp
    isAscending = !isAscending;

    // Cập nhật biểu tượng mũi tên
    sortIcon.classList.toggle('bi-arrow-down', isAscending);
    sortIcon.classList.toggle('bi-arrow-up', !isAscending);

    // Hiển thị lại dữ liệu bảng sau khi sắp xếp
    displayTableData(currentPage);
}

function changePageSize() {
    const newPageSize = parseInt(document.getElementById('pageSize').value, 10);
    rowsPerPage = newPageSize; // Cập nhật số hàng mỗi trang
    currentPage = 1; // Reset về trang đầu tiên
    setupPagination(); // Thiết lập lại pagination
    displayTableData(currentPage); // Hiển thị dữ liệu cho trang hiện tại
}

// Initial setup
displayTableData(currentPage);
setupPagination();
