function toggleDevice(button) {
    const deviceCard = button.closest('.device-card');
    const deviceIcon = deviceCard.querySelector('.device-icon');
    const deviceName = deviceCard.querySelector('p').textContent.toLowerCase();
    let status = false;

    if (button.textContent === "Off") {
        button.textContent = "On";
        button.classList.remove("off");
        button.classList.add("on");
        status = true;

        if (deviceName.includes("Fan")) {
            deviceIcon.classList.add("spin");
        } else if (deviceName.includes("Air Conditioner")) {
            deviceIcon.classList.add("fade-out");
            setTimeout(() => {
                deviceIcon.src = "img/ac_on.svg";
                deviceIcon.classList.remove("fade-out");
                deviceIcon.classList.add("fade-in");
            }, 500);
        } else if (deviceName.includes("Light Bulb")) {
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

    // Gửi yêu cầu Ajax để cập nhật trạng thái thiết bị
    fetch('/api/updateDeviceStatus', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `name=${encodeURIComponent(deviceName)}&status=${encodeURIComponent(status)}`
    })
    .then(response => response.text())
    .then(data => {
        console.log(data);
        // Hiển thị thông báo thành công

    })
    .catch(error => console.error('Error:', error));

    setTimeout(() => {
        deviceIcon.classList.remove("fade-in");
    }, 1000);
}
