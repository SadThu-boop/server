function toggleDevice(button) {
    const deviceCard = button.closest('.device-card');
    const deviceIcon = deviceCard.querySelector('.device-icon');
    const deviceName = deviceCard.querySelector('p').textContent.toLowerCase().trim();  // Lấy tên thiết bị và loại bỏ khoảng trắng
    let status = false;

    if (button.textContent === "Off") {
        button.textContent = "On";
        button.classList.remove("off");
        button.classList.add("on");
        status = true;

        if (deviceName.includes("fan")) {
            deviceIcon.classList.add("spin");
        } else if (deviceName.includes("air conditioner")) {
            deviceIcon.classList.add("fade-out");
            setTimeout(() => {
                deviceIcon.src = "img/ac_on.svg";
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in");
            }, 500);
        } else if (deviceName.includes("light bulb")) {
            deviceIcon.classList.add("fade-out");
            setTimeout(() => {
                deviceIcon.src = "img/bulb_on.svg";
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in");
            }, 500);
        }
    } else {
        button.textContent = "Off";
        button.classList.remove("on");
        button.classList.add("off");
        status = false;

        if (deviceName.includes("fan")) {
            deviceIcon.classList.remove("spin");
        } else if (deviceName.includes("air conditioner")) {
            deviceIcon.classList.add("fade-out");
            setTimeout(() => {
                deviceIcon.src = "img/ac.svg";
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in");
            }, 500);
        } else if (deviceName.includes("light bulb")) {
            deviceIcon.classList.add("fade-out");
            setTimeout(() => {
                deviceIcon.src = "img/bulb.svg";
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in");
            }, 500);
        }
    }

    // Save state to localStorage
    localStorage.setItem(deviceName, status ? 'on' : 'off');

    console.log("Device Name:", deviceName);  // Thêm dòng này để kiểm tra deviceName
    console.log("Status:", status ? "on" : "off");  // Thêm dòng này để kiểm tra status

    // Gửi yêu cầu Ajax để cập nhật trạng thái thiết bị
    fetch('/api/device/control', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            name: deviceName,  // Đảm bảo biến này chứa giá trị chính xác
            action: status ? 'on' : 'off'
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.text();
    })
    .then(data => {
        console.log(`${deviceName} is turned ${status ? 'on' : 'off'}`);
    })
    .catch(error => console.error('Error:', error));

    setTimeout(() => {
        deviceIcon.classList.remove("fade-in");
    }, 1000);
}

// Load device state on page load
document.addEventListener('DOMContentLoaded', function () {
    const devices = document.querySelectorAll('.device-card');
    devices.forEach(device => {
        const button = device.querySelector('button');
        const deviceName = device.querySelector('p').textContent.toLowerCase().trim();
        const savedState = localStorage.getItem(deviceName);

        if (savedState === 'on') {
            button.textContent = "On";
            button.classList.remove("off");
            button.classList.add("on");

            if (deviceName.includes("fan")) {
                device.querySelector('.device-icon').classList.add("spin");
            } else if (deviceName.includes("air conditioner")) {
                device.querySelector('.device-icon').src = "img/ac_on.svg";
            } else if (deviceName.includes("light bulb")) {
                device.querySelector('.device-icon').src = "img/bulb_on.svg";
            }
        }
    });
});
