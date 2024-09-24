let rowsPerPage = 10;
let currentPage = 1;
let originalData = [];  // Original data from API
let filteredData = [];  // Filtered data

// Function to format timestamps
function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// Function to display table data based on the current page
function displayTableData(page) {
    const tableBody = document.getElementById('sensorDataTable');
    tableBody.innerHTML = '';

    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const paginatedData = filteredData.slice(start, end);

    paginatedData.forEach(row => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>1</td>
            <td>${row.temperature}</td>
            <td>${row.humidity}</td>
            <td>${row.light}</td>
            <td>${formatTimestamp(row.timestamp)}</td>
        `;
        tableBody.appendChild(tr);
    });
}

// Function to setup pagination controls
function setupPagination() {
    const totalPages = Math.ceil(filteredData.length / rowsPerPage);
    const paginationControls = document.getElementById('paginationControls');
    paginationControls.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        const li = document.createElement('li');
        li.className = 'page-item';
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener('click', function () {
            changePage(i);
        });
        paginationControls.appendChild(li);
    }
    updatePaginationControls();
}

// Function to change page, display data, and update URL
function changePage(pageNumber) {
    currentPage = pageNumber;
    displayTableData(currentPage);
    updatePaginationControls();
    updateURLWithPageNumber(currentPage); // Update the URL with the current page number
}

// Function to update the URL with the current page number as a parameter
function updateURLWithPageNumber(pageNumber) {
    const url = new URL(window.location);
    url.searchParams.set('page', pageNumber); // Set the 'page' parameter in the URL
    window.history.pushState({ page: pageNumber }, '', url); // Update the URL without reloading the page
}

// Function to update pagination controls highlighting the current page
function updatePaginationControls() {
    const paginationControls = document.getElementById('paginationControls');
    Array.from(paginationControls.children).forEach((li, index) => {
        li.classList.remove('active');
        if (index + 1 === currentPage) {
            li.classList.add('active');
        }
    });
}

// Function to change the number of rows per page and reset pagination
function changePageSize() {
    rowsPerPage = parseInt(document.getElementById('pageSize').value);
    currentPage = 1; // Reset to first page
    displayTableData(currentPage);
    setupPagination();
}

// Function to filter data by time and update display
function filterByTime() {
    const timeValue = document.getElementById('searchByTime').value;
    if (!timeValue) {
        filteredData = [...originalData]; // Restore original data
    } else {
        filteredData = originalData.filter(row => row.timestamp.includes(timeValue));
    }
    currentPage = 1; // Reset to first page
    applyFilters();
}

// Function to apply filters and update pagination
function applyFilters() {
    setupPagination(); // Update pagination
    displayTableData(currentPage); // Display data for the current page
}

// Function to filter data based on the selected search type and input
function filterData() {
    const searchType = document.getElementById('searchType').value;
    const searchValue = document.getElementById('searchInput').value.toLowerCase();

    filteredData = originalData.filter(row => {
        let cellValue;
        switch (searchType) {
            case 'time':
                cellValue = row.timestamp;
                break;
            case 'temperature':
                cellValue = row.temperature.toString();
                break;
            case 'humidity':
                cellValue = row.humidity.toString();
                break;
            case 'light':
                cellValue = row.light.toString();
                break;
        }
        return cellValue.toLowerCase().includes(searchValue);
    });

    currentPage = 1; // Reset to first page
    applyFilters(); // Apply filtering and update display
}

// Function to sort table data based on a selected column
function sortTable(column) {
    const sortIcon = document.getElementById(`${column}SortIcon`);
    const isAscending = sortIcon.classList.contains('bi-arrow-down');

    filteredData.sort((a, b) => {
        if (a[column] > b[column]) return isAscending ? 1 : -1;
        if (a[column] < b[column]) return isAscending ? -1 : 1;
        return 0;
    });

    // Toggle the sorting icon
    sortIcon.classList.toggle('bi-arrow-down');
    sortIcon.classList.toggle('bi-arrow-up');

    displayTableData(currentPage);
}

// Function to fetch data from API and initialize table
function fetchDataFromAPI() {
    fetch('/api/sensor')  // Ensure the path matches your API endpoint
        .then(response => response.json())
        .then(data => {
            originalData = data;  // Store the original data from the API
            filteredData = [...originalData]; // Initialize filteredData with original data
            displayTableData(currentPage);  // Display the data
            setupPagination();  // Setup pagination controls
        })
        .catch(error => console.error('Error fetching sensor data:', error));
}

// Function to initialize the page based on the URL parameters
function initializePageFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    const pageParam = urlParams.get('page');

    // If a page parameter exists, use it; otherwise, default to page 1
    currentPage = pageParam ? parseInt(pageParam, 10) : 1;
    displayTableData(currentPage);
    setupPagination();
}

// Initialize the data fetch and setup on DOM load
document.addEventListener('DOMContentLoaded', function () {
    fetchDataFromAPI();
    initializePageFromURL(); // Initialize the page based on URL parameters
});
